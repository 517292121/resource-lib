package cn.com.adtech.riss2.fileservice;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/** Read-only extraction: unlike the old reader, never writes back over the source. */
public final class FileTextReader {
    private FileTextReader() { }

    public static String read(Path file) throws IOException {
        String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".pdf")) {
            PDDocument document = PDDocument.load(file.toFile());
            try {
                return new PDFTextStripper().getText(document);
            } finally {
                document.close();
            }
        }
        if (name.endsWith(".docx")) {
            try (InputStream input = Files.newInputStream(file)) {
                XWPFDocument document = new XWPFDocument(input);
                try {
                    // POI 3.9's extractor dereferences a null policy for valid DOCX without sectPr.
                    if (document.getHeaderFooterPolicy() == null) {
                        StringBuilder text = new StringBuilder();
                        for (IBodyElement element : document.getBodyElements()) {
                            if (element instanceof XWPFParagraph) {
                                text.append(((XWPFParagraph) element).getText()).append('\n');
                            } else if (element instanceof XWPFTable) {
                                text.append(((XWPFTable) element).getText()).append('\n');
                            }
                        }
                        return text.toString();
                    }
                    return new XWPFWordExtractor(document).getText();
                } finally {
                    document.getPackage().close();
                }
            }
        }
        if (name.endsWith(".doc")) {
            try (InputStream input = Files.newInputStream(file)) {
                return new WordExtractor(input).getText();
            }
        }
        if (!name.endsWith(".txt")) {
            throw new IllegalArgumentException("Only txt, doc, docx and pdf can be read");
        }
        byte[] bytes = Files.readAllBytes(file);
        if (bytes.length >= 2 && bytes[0] == (byte) 0xff && bytes[1] == (byte) 0xfe) {
            return new String(bytes, 2, bytes.length - 2, StandardCharsets.UTF_16LE);
        }
        if (bytes.length >= 2 && bytes[0] == (byte) 0xfe && bytes[1] == (byte) 0xff) {
            return new String(bytes, 2, bytes.length - 2, StandardCharsets.UTF_16BE);
        }
        try {
            String text = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes)).toString();
            return text.startsWith("\ufeff") ? text.substring(1) : text;
        } catch (CharacterCodingException e) {
            return new String(bytes, Charset.forName("GB18030"));
        }
    }
}
