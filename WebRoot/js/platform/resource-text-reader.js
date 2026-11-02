(function () {
    'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

    var source = document.getElementById('reader-source');
    var book = document.getElementById('reader-book');
    if (!source || !book) return;
    var left = document.getElementById('reader-left');
    var right = document.getElementById('reader-right');
    var previous = document.getElementById('reader-prev');
    var next = document.getElementById('reader-next');
    var number = document.getElementById('reader-page-number');
    var total = document.getElementById('reader-page-total');
    var sizes = document.getElementById('reader-font-size');
    var status = document.getElementById('reader-status');
    var chapters = Array.prototype.map.call(source.querySelectorAll('.reader-chapter-source'), function (element) {
        return {path:element.getAttribute('data-path'), hash:element.getAttribute('data-hash'), title:element.getAttribute('data-title'), text:element.textContent.replace(/^\uFEFF/, '').replace(/\r\n?/g, '\n'), startPage:0, pageCount:0};
    });
    if (!chapters.length) return;
    var chapterLinks = Array.prototype.slice.call(document.querySelectorAll('.reader-chapters [data-chapter-index]'));
    var sourceCharacters = chapters.reduce(function (sum, chapter) { return sum + chapter.text.length; }, 0);
    var spreads = [], current = 0, started = false, timer, generation = 0, pending = false, catalogChapter = -1, queuedJump = null, focusedPosition = null;
    var fontSize = 18;
    var availableSizes = [14, 16, 18, 20, 22, 24];
    var initial = new URL(window.location.href);
    var initialPage = Math.max(1, parseInt(initial.searchParams.get('page'), 10) || 1);
    var startAtEnd = initial.searchParams.get('last') === '1';
    var initialChapter = Math.max(0, Math.min(chapters.length - 1, parseInt(book.getAttribute('data-initial-chapter'), 10) || 0));
    var initialGlobal = initial.searchParams.get('paging') === 'resource';
    var initialOffset = Math.max(0, parseInt(initial.searchParams.get('offset'), 10) || 0);
    var searchTerms = initial.searchParams.getAll('hit').map(function (value) { return value.trim().slice(0,256); }).filter(Boolean).slice(0,16);
    if (!searchTerms.length) {
        var key = (initial.searchParams.get('key') || '').trim().slice(0,1024);
        if (key) searchTerms = [key];
    }
    var searchContext = (initial.searchParams.get('context') || '').trim().slice(0,256);
    var searchTarget = null;
    function literalSearch(value) {
        var pattern = value.trim().split(/\s+/).map(function (part) { return part.replace(/[.*+?^${}()|[\]\\]/g,'\\$&'); }).join('\\s+');
        return pattern ? new RegExp(pattern,'gi') : null;
    }
    if (initial.searchParams.get('preview') === 'search' && !initial.searchParams.has('offset') && !initial.hash.includes('annotation=')) {
        var order = [initialChapter];
        chapters.forEach(function (chapter,index) { if (index !== initialChapter) order.push(index); });
        var candidates = searchContext ? [searchContext].concat(searchTerms) : searchTerms;
        for (var candidate = 0; candidate < candidates.length && !searchTarget; candidate++) {
            var expression = literalSearch(candidates[candidate]);
            if (!expression) continue;
            for (var chapterPosition = 0; chapterPosition < order.length; chapterPosition++) {
                expression.lastIndex = 0;
                var found = expression.exec(chapters[order[chapterPosition]].text);
                if (found) { searchTarget = {chapter:order[chapterPosition],offset:found.index}; break; }
            }
        }
        if (searchTarget) { initialChapter = searchTarget.chapter; initialOffset = searchTarget.offset; }
        book.dataset.searchLocated = searchTarget ? 'true' : 'false';
    }
    var searchExpressions = searchTerms.map(literalSearch).filter(Boolean);
    var chapterDialog = document.getElementById('reader-chapter-dialog');
    document.getElementById('reader-open-chapters').addEventListener('click', function () { chapterDialog.showModal(); });
    chapterDialog.querySelector('[data-close-chapters]').addEventListener('click', function () { chapterDialog.close(); });
    try {
        var saved = parseInt(window.localStorage.getItem('dh-reader-font-size'), 10);
        if (availableSizes.indexOf(saved) >= 0) fontSize = saved;
    } catch (ignore) { }

    var measure = document.createElement('div');
    measure.className = 'reader-text reader-measure';
    measure.setAttribute('aria-hidden', 'true');
    measure.style.overflow = 'hidden';
    document.body.appendChild(measure);

    function spreadCount() { return Math.max(1, spreads.length); }
    function offset() {
        var spread = spreads[current];
        if (!spread) return {chapter:initialChapter,offset:0};
        if (focusedPosition && containsPosition(spread,focusedPosition)) return focusedPosition;
        if (searchTarget && containsPosition(spread,searchTarget)) return searchTarget;
        return {chapter:spread.leaves[0].chapter,offset:spread.leaves[0].start};
    }
    function containsPosition(spread, position) {
        return spread.leaves.some(function (leaf) {
            return leaf.chapter === position.chapter && position.offset >= leaf.start &&
                (position.offset < leaf.end || (leaf.end === chapters[leaf.chapter].text.length && position.offset === leaf.end));
        });
    }
    function findSpread(position) {
        for (var index = 0; index < spreads.length; index++) if (containsPosition(spreads[index],position)) return index;
        return chapters[position.chapter].startPage;
    }
    function safeEnd(text, end) {
        if (end > 0 && end < text.length && /[\uDC00-\uDFFF]/.test(text.charAt(end)) && /[\uD800-\uDBFF]/.test(text.charAt(end - 1))) return end - 1;
        return end;
    }

    function paintSearchHighlights() {
        var page = book.readerPage;
        if (!page || !searchExpressions.length) return;
        [left,right].forEach(function (leaf,index) {
            leaf.querySelectorAll('mark.reader-search-highlight').forEach(function (mark) { mark.replaceWith(document.createTextNode(mark.textContent)); });
            leaf.normalize();
            var location = page.leaves[index];
            if (!location) return;
            // Each leaf can belong to a different volume; offsets always refer to its own source text.
            var begin = Math.max(0,location.start - 1024), finish = Math.min(location.text.length,location.end + 1024);
            var text = location.text.slice(begin,finish), ranges = [];
            searchExpressions.forEach(function (expression) {
                expression.lastIndex = 0;
                var match, count = 0;
                while ((match = expression.exec(text)) && count++ < 2000) {
                    var start = begin + match.index, end = start + match[0].length;
                    if (end > location.start && start < location.end) ranges.push({start:start,end:end});
                }
            });
            ranges.sort(function (a,b) { return a.start - b.start || a.end - b.end; });
            var merged = [];
            ranges.forEach(function (range) {
                var previous = merged[merged.length - 1];
                if (previous && range.start <= previous.end) previous.end = Math.max(previous.end,range.end);
                else merged.push({start:range.start,end:range.end});
            });
            var walker = document.createTreeWalker(leaf,NodeFilter.SHOW_TEXT), nodes = [], node, offset = location.start;
            while ((node = walker.nextNode())) { nodes.push({node:node,start:offset}); offset += node.length; }
            nodes.forEach(function (entry) {
                var value = entry.node.textContent, start = entry.start, end = start + value.length;
                var matches = merged.filter(function (range) { return range.end > start && range.start < end; });
                if (!matches.length) return;
                var fragment = document.createDocumentFragment(), position = 0;
                matches.forEach(function (range) {
                    var from = Math.max(0,range.start - start), to = Math.min(value.length,range.end - start);
                    fragment.appendChild(document.createTextNode(value.slice(position,from)));
                    var mark = document.createElement('mark'); mark.className = 'reader-search-highlight'; mark.textContent = value.slice(from,to);
                    fragment.appendChild(mark); position = to;
                });
                fragment.appendChild(document.createTextNode(value.slice(position)));
                entry.node.replaceWith(fragment);
            });
        });
    }
    // Annotation painting may replace leaf nodes asynchronously; reapply without removing its marks.
    book.paintSearchHighlights = paintSearchHighlights;

    function render() {
        var count = spreadCount();
        current = Math.max(0, Math.min(count - 1, current));
        var spread = spreads[current];
        if (!spread) return;
        var position = offset();
        var chapter = chapters[position.chapter];
        var locations = spread.leaves.map(function (leaf) {
            var source = chapters[leaf.chapter];
            return {chapter:source.path,title:source.title,text:source.text,textHash:source.hash,start:leaf.start,end:leaf.end};
        });
        var primary = locations.filter(function (leaf) { return leaf.chapter === chapter.path; });
        var start = primary[0].start, end = primary[primary.length - 1].end;
        left.textContent = spread.left;
        right.textContent = spread.right;
        if (!locations[0].text) left.textContent = uiText('本章节暂无正文。');
        number.value = current + 1;
        number.max = count;
        total.textContent = '/ ' + count;
        previous.disabled = current === 0;
        next.disabled = current === count - 1;
        document.getElementById('reader-zoom-in').disabled = fontSize === availableSizes[availableSizes.length - 1];
        document.getElementById('reader-zoom-out').disabled = fontSize === availableSizes[0];
        status.textContent = uiText('全书第 {page} / {pages} 页，{chapter}，字号 {size} 像素。', {page:current + 1, pages:count, chapter:chapter.title, size:fontSize});
        book.setAttribute('data-spread-count', count);
        book.setAttribute('data-chapter-count', chapters.length);
        book.setAttribute('data-current-chapter', chapter.path);
        document.getElementById('reader-title').textContent = book.getAttribute('data-resource-title') + ' · ' + chapter.title;
        if (catalogChapter !== position.chapter) {
            catalogChapter = position.chapter;
            chapterLinks.forEach(function (link, index) {
                link.classList.toggle('current', index === catalogChapter);
                if (index === catalogChapter) link.setAttribute('aria-current', 'page');
                else link.removeAttribute('aria-current');
            });
            var catalog = document.querySelector('.reader-chapters');
            var selected = chapterLinks[catalogChapter];
            if (selected && catalog) catalog.scrollTop = selected.offsetTop - (catalog.clientHeight - selected.offsetHeight) / 2;
        }
        var url = new URL(window.location.href);
        url.searchParams.set('page', current + 1);
        url.searchParams.set('paging', 'resource');
        url.searchParams.set('p', chapter.path);
        url.searchParams.delete('last');
        url.searchParams.set('offset', start);
        window.history.replaceState(null, '', url.toString());
        var page = {chapter:chapter.path, title:chapter.title, text:chapter.text, textHash:chapter.hash, start:start,
            end:end, leftStart:locations[0].start, rightStart:locations[1] ? locations[1].start : locations[0].end,
            page:current + 1, leaves:locations};
        book.readerPage = page;
        book.dispatchEvent(new CustomEvent('reader:pagechange', {detail:page}));
        paintSearchHighlights();
    }

    async function paginate(anchor) {
        var token = ++generation;
        document.documentElement.style.setProperty('--reader-font-size', fontSize + 'px');
        sizes.value = String(fontSize);
        var bounds = left.getBoundingClientRect();
        if (bounds.width < 1 || bounds.height < 1) return;
        pending = true;
        book.setAttribute('aria-busy', 'true');
        previous.disabled = next.disabled = number.disabled = true;
        status.textContent = uiText('正在计算全书页码…');
        measure.style.width = bounds.width + 'px';
        measure.style.height = bounds.height + 'px';
        var calculated = [], ranges = [], allLeaves = [], batchStart = performance.now();
        for (var chapterIndex = 0; chapterIndex < chapters.length; chapterIndex++) {
            var text = chapters[chapterIndex].text, leaves = [], start = 0;
            while (start < text.length) {
                var low = start, high = Math.min(text.length, start + 8192);
                while (low < high) {
                    var middle = Math.ceil((low + high) / 2);
                    measure.textContent = text.slice(start, middle);
                    if (measure.scrollHeight <= measure.clientHeight && measure.scrollWidth <= measure.clientWidth) low = middle;
                    else high = middle - 1;
                }
                var end = safeEnd(text, low);
                if (end <= start) end = safeEnd(text, Math.min(text.length, start + 2));
                leaves.push({chapter:chapterIndex, start:start, end:end, text:text.slice(start, end)});
                start = end;
                if (performance.now() - batchStart > 16) {
                    await new Promise(function (resolve) { window.setTimeout(resolve, 0); });
                    if (token !== generation) return;
                    batchStart = performance.now();
                }
            }
            if (!leaves.length) leaves.push({chapter:chapterIndex, start:0, end:0, text:''});
            var chapterStart = Math.floor(allLeaves.length / 2);
            allLeaves = allLeaves.concat(leaves);
            ranges.push({start:chapterStart, count:Math.floor((allLeaves.length - 1) / 2) - chapterStart + 1});
        }
        // Pair consecutive leaves across volume boundaries instead of leaving a right-hand page blank.
        for (var leafIndex = 0; leafIndex < allLeaves.length; leafIndex += 2) {
            var pair = allLeaves.slice(leafIndex,leafIndex + 2);
            calculated.push({left:pair[0].text,right:pair[1] ? pair[1].text : '',leaves:pair});
        }
        if (token !== generation) return;
        spreads = calculated;
        measure.textContent = '';
        chapters.forEach(function (chapter, index) { chapter.startPage = ranges[index].start; chapter.pageCount = ranges[index].count; });
        chapterLinks.forEach(function (link, index) {
            var url = new URL(link.href);
            url.searchParams.set('page', chapters[index].startPage + 1);
            url.searchParams.set('paging', 'resource');
            url.searchParams.set('offset', '0');
            link.href = url.toString();
            link.setAttribute('data-start-page', chapters[index].startPage + 1);
            link.setAttribute('data-end-page', chapters[index].startPage + chapters[index].pageCount);
            link.title = uiText('{chapter}（全书第 {page} 页）', {chapter:chapters[index].title, page:chapters[index].startPage + 1});
        });
        if (!started) {
            var selected = chapters[initialChapter];
            current = initialGlobal ? initialPage - 1 : selected.startPage + (startAtEnd ? selected.pageCount - 1 : Math.min(selected.pageCount - 1, initialPage - 1));
            if (initial.searchParams.has('offset') || searchTarget) {
                focusedPosition = {chapter:initialChapter,offset:initialOffset};
                current = findSpread(focusedPosition);
            } else if (!initialGlobal) {
                var selectedLeaf = spreads[current].leaves.find(function (leaf) { return leaf.chapter === initialChapter; });
                if (selectedLeaf) focusedPosition = {chapter:initialChapter,offset:selectedLeaf.start};
            }
            started = true;
        } else {
            var position = anchor || {chapter:initialChapter, offset:0};
            focusedPosition = position;
            current = findSpread(position);
        }
        pending = false;
        number.disabled = false;
        book.removeAttribute('aria-busy');
        book.setAttribute('data-source-characters', sourceCharacters);
        book.setAttribute('data-paged-characters', spreads.reduce(function (sum, spread) { return sum + spread.left.length + spread.right.length; }, 0));
        catalogChapter = -1;
        if (queuedJump) { locateAnnotation(queuedJump); queuedJump = null; }
        render();
    }

    function turn(step) {
        if (!pending && current + step >= 0 && current + step < spreadCount()) {
            focusedPosition = null;
            current += step;
            render();
        }
    }
    function locateAnnotation(target) {
        var index = chapters.findIndex(function (chapter) { return chapter.path === target.chapter; });
        if (index < 0) return false;
        focusedPosition = {chapter:index,offset:Math.max(0,Math.min(chapters[index].text.length,target.offset || 0))};
        current = findSpread(focusedPosition);
        return true;
    }
    book.addEventListener('reader:jump', function (event) {
        if (pending) { queuedJump = event.detail; return; }
        if (locateAnnotation(event.detail)) render();
    });

    function changeSize(size) {
        if (availableSizes.indexOf(size) < 0) return;
        var anchor = offset();
        fontSize = size;
        try { window.localStorage.setItem('dh-reader-font-size', String(size)); } catch (ignore) { }
        paginate(anchor);
    }
    previous.addEventListener('click', function () { turn(-1); });
    next.addEventListener('click', function () { turn(1); });
    function jumpToPage() { if (pending) return; focusedPosition = null; current = (parseInt(number.value, 10) || 1) - 1; render(); }
    number.addEventListener('change', jumpToPage);
    number.addEventListener('keydown', function (event) { if (event.key === 'Enter') { event.preventDefault(); jumpToPage(); number.blur(); } });
    sizes.addEventListener('change', function () { changeSize(parseInt(sizes.value, 10)); });
    chapterLinks.forEach(function (link, index) {
        link.addEventListener('click', function (event) {
            if (event.ctrlKey || event.metaKey || event.shiftKey || event.altKey || event.button !== 0) return;
            event.preventDefault();
            if (pending) return;
            locateAnnotation({chapter:chapters[index].path,offset:0});
            chapterDialog.close();
            render();
        });
    });
    document.getElementById('reader-zoom-in').addEventListener('click', function () { changeSize(availableSizes[availableSizes.indexOf(fontSize) + 1]); });
    document.getElementById('reader-zoom-out').addEventListener('click', function () { changeSize(availableSizes[availableSizes.indexOf(fontSize) - 1]); });
    document.addEventListener('keydown', function (event) {
        if (event.ctrlKey || event.altKey || event.metaKey || document.querySelector('dialog[open]') || /^(INPUT|SELECT|TEXTAREA)$/.test(event.target.tagName) || event.target.isContentEditable) return;
        if (event.key === 'ArrowLeft' || event.key === 'ArrowRight') { event.preventDefault(); turn(event.key === 'ArrowLeft' ? -1 : 1); }
    });
    var lastWidth = 0, lastHeight = 0;
    var scene = document.querySelector('.reader-scene');
    var layout = document.querySelector('.reader-layout');
    var annotationPanel = document.getElementById('reader-catalog');
    var annotationToggle = document.getElementById('reader-annotations-toggle');
    function showAnnotations(visible) {
        if (!annotationPanel || !annotationToggle) return;
        annotationPanel.hidden = !visible;
        annotationToggle.setAttribute('aria-expanded', String(visible));
        layout.classList.toggle('is-annotations-collapsed', !visible);
        resized();
    }
    if (annotationToggle && annotationPanel) {
        annotationToggle.addEventListener('click', function () {
            showAnnotations(annotationPanel.hidden);
        });
        book.addEventListener('reader:showannotations', function () { showAnnotations(true); });
        showAnnotations(false);
    }
    function fitWindow() {
        // Follow available width at the book's natural aspect ratio; the document may scroll vertically.
        layout.style.removeProperty('width');
        resized();
    }
    function resized() {
        var box = left.getBoundingClientRect();
        if (Math.abs(box.width - lastWidth) < 1 && Math.abs(box.height - lastHeight) < 1) return;
        lastWidth = box.width; lastHeight = box.height;
        window.clearTimeout(timer);
        timer = window.setTimeout(function () { paginate(offset()); }, 120);
    }
    if (window.ResizeObserver) {
        new ResizeObserver(resized).observe(book);
        new ResizeObserver(fitWindow).observe(scene);
    }
    window.addEventListener('resize', fitWindow);
    (document.fonts ? document.fonts.ready : Promise.resolve()).then(function () {
        fitWindow();
        paginate(0);
    });
}());
