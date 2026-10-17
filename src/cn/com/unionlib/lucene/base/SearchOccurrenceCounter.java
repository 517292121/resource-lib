package cn.com.unionlib.lucene.base;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

/** Counts query occurrences, not matching documents, scores, snippets, or child pages. */
public final class SearchOccurrenceCounter {
    private SearchOccurrenceCounter() { }

    public static long count(IndexSearcher searcher, Query keywordQuery, Bits acceptedDocs) throws IOException {
        Set<Term> terms = new HashSet<Term>();
        Set<Query> phrases = new HashSet<Query>();
        collectQueries(searcher, keywordQuery, terms, phrases);
        long count = 0L;
        for (Term term : terms) count += countTerm(searcher, term, acceptedDocs);
        // Phrase scorers expose the number of matching phrase positions, not token count.
        for (Query phrase : phrases) {
            Weight weight = searcher.createNormalizedWeight(phrase);
            for (AtomicReaderContext leaf : searcher.getIndexReader().leaves()) {
                Scorer scorer = weight.scorer(leaf, true, false, leaf.reader().getLiveDocs());
                if (scorer == null) continue;
                for (int doc = scorer.nextDoc(); doc != DocIdSetIterator.NO_MORE_DOCS; doc = scorer.nextDoc()) {
                    if (acceptedDocs.get(leaf.docBase + doc)) count += scorer.freq();
                }
            }
        }
        return count;
    }

    private static void collectQueries(IndexSearcher searcher, Query query, final Set<Term> terms,
                                       Set<Query> phrases) throws IOException {
        if (query instanceof BooleanQuery) {
            for (BooleanClause clause : (BooleanQuery) query) {
                if (!clause.isProhibited()) collectQueries(searcher, clause.getQuery(), terms, phrases);
            }
        } else if (query instanceof DisjunctionMaxQuery) {
            for (Query child : (DisjunctionMaxQuery) query) collectQueries(searcher, child, terms, phrases);
        } else if (query instanceof TermQuery) {
            terms.add(((TermQuery) query).getTerm());
        } else if (query instanceof FuzzyQuery) {
            // Respect the original fuzzy query's top-term/max-expansion selection.
            collectQueries(searcher, searcher.rewrite(query), terms, phrases);
        } else if (query instanceof MultiTermQuery) {
            // Enumerate accepted prefix/wildcard/range terms without the 1024-clause limit.
            new MultiTermQuery.RewriteMethod() {
                public Query rewrite(IndexReader reader, MultiTermQuery multi) throws IOException {
                    Terms field = MultiFields.getTerms(reader, multi.getField());
                    if (field != null) {
                        TermsEnum accepted = getTermsEnum(multi, field, new AttributeSource());
                        for (BytesRef term = accepted.next(); term != null; term = accepted.next()) {
                            terms.add(new Term(multi.getField(), BytesRef.deepCopyOf(term)));
                        }
                    }
                    return multi;
                }
            }.rewrite(searcher.getIndexReader(), (MultiTermQuery) query);
        } else if (query instanceof ConstantScoreQuery) {
            Query nested = ((ConstantScoreQuery) query).getQuery();
            if (nested != null) collectQueries(searcher, nested, terms, phrases);
        } else if (!(query instanceof MatchAllDocsQuery)) {
            phrases.add(query);
        }
    }

    private static long countTerm(IndexSearcher searcher, Term term, Bits acceptedDocs) throws IOException {
        long count = 0L;
        for (AtomicReaderContext leaf : searcher.getIndexReader().leaves()) {
            Terms field = leaf.reader().terms(term.field());
            if (field == null) continue;
            TermsEnum terms = field.iterator(null);
            if (!terms.seekExact(term.bytes())) continue;
            DocsEnum docs = terms.docs(leaf.reader().getLiveDocs(), null, DocsEnum.FLAG_FREQS);
            for (int doc = docs.nextDoc(); doc != DocIdSetIterator.NO_MORE_DOCS; doc = docs.nextDoc()) {
                if (!acceptedDocs.get(leaf.docBase + doc)) continue;
                if (field.hasFreqs()) count += docs.freq();
                else {
                    // Legacy creator/publisher/keyword fields omit frequencies, but store
                    // every original value. Count repeated equal values rather than guessing 1.
                    for (String value : leaf.reader().document(doc).getValues(term.field())) {
                        if (term.text().equals(value)) count++;
                    }
                }
            }
        }
        return count;
    }

    /** The database identifier search uses LIKE rather than a tokenized Lucene field. */
    public static long countLiteralOccurrences(String value, String key) {
        if (value == null || key == null || key.isEmpty()) return 0L;
        long count = 0L;
        for (int i = 0; i <= value.length() - key.length();) {
            if (value.regionMatches(true, i, key, 0, key.length())) {
                count++;
                i += key.length();
            } else i++;
        }
        return count;
    }
}
