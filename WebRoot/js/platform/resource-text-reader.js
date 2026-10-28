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
    var spreads = [], current = 0, started = false, timer, generation = 0, pending = false, catalogChapter = -1, queuedJump = null;
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
        var end = spread.start + spread.left.length + spread.right.length;
        // Resizing a freshly located search page should keep the hit visible, not just its preceding whitespace.
        return searchTarget && searchTarget.chapter === spread.chapter && searchTarget.offset >= spread.start && searchTarget.offset < end
            ? {chapter:spread.chapter,offset:searchTarget.offset} : {chapter:spread.chapter,offset:spread.start};
    }
    function safeEnd(text, end) {
        if (end > 0 && end < text.length && /[\uDC00-\uDFFF]/.test(text.charAt(end)) && /[\uD800-\uDBFF]/.test(text.charAt(end - 1))) return end - 1;
        return end;
    }

    function paintSearchHighlights() {
        var page = book.readerPage;
        if (!page || !searchExpressions.length) return;
        // Include nearby characters so a match split across leaves/spreads keeps its exact offsets.
        var begin = Math.max(0,page.start - 1024), finish = Math.min(page.text.length,page.end + 1024);
        var text = page.text.slice(begin,finish), ranges = [];
        searchExpressions.forEach(function (expression) {
            expression.lastIndex = 0;
            var match, count = 0;
            while ((match = expression.exec(text)) && count++ < 2000) {
                var start = begin + match.index, end = start + match[0].length;
                if (end > page.start && start < page.end) ranges.push({start:start,end:end});
            }
        });
        ranges.sort(function (a,b) { return a.start - b.start || a.end - b.end; });
        var merged = [];
        ranges.forEach(function (range) {
            var previous = merged[merged.length - 1];
            if (previous && range.start <= previous.end) previous.end = Math.max(previous.end,range.end);
            else merged.push({start:range.start,end:range.end});
        });
        [left,right].forEach(function (leaf,index) {
            leaf.querySelectorAll('mark.reader-search-highlight').forEach(function (mark) { mark.replaceWith(document.createTextNode(mark.textContent)); });
            leaf.normalize();
            var walker = document.createTreeWalker(leaf,NodeFilter.SHOW_TEXT), nodes = [], node, offset = index ? page.rightStart : page.leftStart;
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
        var chapter = chapters[spread.chapter];
        left.textContent = spread.left;
        right.textContent = spread.right;
        if (!chapter.text) left.textContent = uiText('本章节暂无正文。');
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
        if (catalogChapter !== spread.chapter) {
            catalogChapter = spread.chapter;
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
        url.searchParams.set('offset', spread.start);
        window.history.replaceState(null, '', url.toString());
        var page = {chapter:chapter.path, title:chapter.title, text:chapter.text, textHash:chapter.hash, start:spread.start,
            end:spread.start + spread.left.length + spread.right.length, leftStart:spread.start,
            rightStart:spread.start + spread.left.length, page:current + 1};
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
        var calculated = [], ranges = [], batchStart = performance.now();
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
                leaves.push({start:start, end:end, text:text.slice(start, end)});
                start = end;
                if (performance.now() - batchStart > 16) {
                    await new Promise(function (resolve) { window.setTimeout(resolve, 0); });
                    if (token !== generation) return;
                    batchStart = performance.now();
                }
            }
            var chapterStart = calculated.length;
            if (!leaves.length) leaves.push({start:0, end:0, text:''});
            // Each chapter begins on a fresh spread, retaining its original text and chapter boundary.
            for (var leafIndex = 0; leafIndex < leaves.length; leafIndex += 2) {
                calculated.push({chapter:chapterIndex, start:leaves[leafIndex].start, left:leaves[leafIndex].text, right:leaves[leafIndex + 1] ? leaves[leafIndex + 1].text : ''});
            }
            ranges.push({start:chapterStart, count:calculated.length - chapterStart});
        }
        if (token !== generation) return;
        spreads = calculated;
        measure.textContent = '';
        chapters.forEach(function (chapter, index) { chapter.startPage = ranges[index].start; chapter.pageCount = ranges[index].count; });
        chapterLinks.forEach(function (link, index) {
            var url = new URL(link.href);
            url.searchParams.set('page', chapters[index].startPage + 1);
            url.searchParams.set('paging', 'resource');
            link.href = url.toString();
            link.setAttribute('data-start-page', chapters[index].startPage + 1);
            link.setAttribute('data-end-page', chapters[index].startPage + chapters[index].pageCount);
            link.title = uiText('{chapter}（全书第 {page} 页）', {chapter:chapters[index].title, page:chapters[index].startPage + 1});
        });
        if (!started) {
            var selected = chapters[initialChapter];
            current = initialGlobal ? initialPage - 1 : selected.startPage + (startAtEnd ? selected.pageCount - 1 : Math.min(selected.pageCount - 1, initialPage - 1));
            if (initial.searchParams.has('offset') || searchTarget) {
                current = selected.startPage;
                while (current + 1 < spreads.length && spreads[current + 1].chapter === initialChapter && spreads[current + 1].start <= initialOffset) current++;
            }
            started = true;
        } else {
            var position = anchor || {chapter:initialChapter, offset:0};
            current = chapters[position.chapter].startPage;
            while (current + 1 < spreads.length && spreads[current + 1].chapter === position.chapter && spreads[current + 1].start <= position.offset) current++;
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
            current += step;
            render();
        }
    }
    function locateAnnotation(target) {
        var index = chapters.findIndex(function (chapter) { return chapter.path === target.chapter; });
        if (index < 0) return false;
        current = chapters[index].startPage;
        while (current + 1 < spreads.length && spreads[current + 1].chapter === index && spreads[current + 1].start <= target.offset) current++;
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
    function jumpToPage() { if (pending) return; current = (parseInt(number.value, 10) || 1) - 1; render(); }
    number.addEventListener('change', jumpToPage);
    number.addEventListener('keydown', function (event) { if (event.key === 'Enter') { event.preventDefault(); jumpToPage(); number.blur(); } });
    sizes.addEventListener('change', function () { changeSize(parseInt(sizes.value, 10)); });
    chapterLinks.forEach(function (link, index) {
        link.addEventListener('click', function (event) {
            if (event.ctrlKey || event.metaKey || event.shiftKey || event.altKey || event.button !== 0) return;
            event.preventDefault();
            if (pending) return;
            current = chapters[index].startPage;
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
    var header = document.querySelector('.reader-header');
    var footer = document.querySelector('.reader-footer');
    function fitWindow() {
        if (window.matchMedia('(max-width:760px), (max-width:1100px) and (orientation:portrait)').matches) {
            layout.style.removeProperty('width');
            resized();
            return;
        }
        var style = window.getComputedStyle(scene);
        var gap = parseFloat(window.getComputedStyle(layout).columnGap) || 5;
        var availableWidth = scene.clientWidth - parseFloat(style.paddingLeft) - parseFloat(style.paddingRight);
        var availableHeight = Math.max(340, window.innerHeight - header.getBoundingClientRect().height - footer.getBoundingClientRect().height - parseFloat(style.paddingTop) - parseFloat(style.paddingBottom));
        // Preserve the original book/frame proportions while fitting both window dimensions.
        var width = Math.floor(Math.min(availableWidth, availableHeight * 1423 / 805 + gap));
        if (layout.style.width !== width + 'px') layout.style.width = width + 'px';
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
        var frameObserver = new ResizeObserver(fitWindow);
        frameObserver.observe(header);
        frameObserver.observe(footer);
    }
    window.addEventListener('resize', fitWindow);
    (document.fonts ? document.fonts.ready : Promise.resolve()).then(function () {
        fitWindow();
        paginate(0);
    });
}());
