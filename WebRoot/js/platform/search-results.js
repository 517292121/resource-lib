(function () {
  'use strict';
  var base = document.body.dataset.base || '/';
  var form = document.getElementById('sr-search-form');
  var input = document.getElementById('sr-key');
  var field = document.getElementById('sr-field');
  var results = document.getElementById('sr-results');
  var numbers = document.getElementById('sr-page-numbers');
  var pager = document.querySelector('.sr-pagination');
  var status = document.getElementById('sr-page-status');
  var bookCount = document.getElementById('sr-book-count');
  var recordCount = document.getElementById('sr-record-count');
  var currentPage = 1;
  var totalPages = 0;
  var totalItems = 0;
  var pageSize = 6;
  var activeRequest = null;
  var state;

  function text(value, fallback) {
    return value == null || value === 'null' || String(value).trim() === '' ? (fallback || '') : String(value);
  }

  function readLocation() {
    var url = new URL(location.href);
    state = new URLSearchParams(url.search);
    if (!state.has('key')) {
      var legacy = url.pathname.match(/\/ls\/([^/]+)$/);
      state.set('key', legacy ? decodeURIComponent(legacy[1]) : document.body.dataset.key || '');
    }
    var attr = state.get('attr') || '';
    var aliases = { title: 'rsdaTitle', creator: 'rsdaCreator', publisher: 'rsdaPublisher', keyword: 'rsdaKeyword' };
    attr = aliases[attr] || attr;
    if (!Array.from(field.options).some(function (option) { return option.value === attr; })) attr = '';
    field.value = attr;
    state.set('attr', attr);
    input.value = state.get('key') || '';
    var match = state.get('match') || state.get('accurate') || '0';
    state.set('match', ['0', '1', '2'].includes(match) ? match : '0');
    currentPage = Math.max(1, parseInt(state.get('pn'), 10) || 1);
    var filters = ['resLib', 'resType', 'creator', 'publisher', 'keyword', 'date'];
    document.getElementById('sr-filter-note').textContent = filters.map(function (name) { return state.get(name); }).filter(Boolean).join(' · ');
    document.title = (input.value ? input.value + ' - ' : '') + '检索结果 - 西藏大学敦煌文献智能数据库';
  }

  // Index highlighters return HTML. Retain emphasis only; never insert index HTML into the DOM.
  function highlight(target, value) {
    var source = text(value).replace(/<\s*(?:span|em|font|mark)\b[^>]*>/gi, '\u0001')
      .replace(/<\/\s*(?:span|em|font|mark)\s*>/gi, '\u0002')
      .replace(/<br\s*\/?>/gi, '\n').replace(/<[^>]*>/g, '');
    var highlighted = false;
    source.split(/([\u0001\u0002])/).forEach(function (part) {
      if (part === '\u0001') { highlighted = true; return; }
      if (part === '\u0002') { highlighted = false; return; }
      var decoder = document.createElement('textarea');
      decoder.innerHTML = part.replace(/</g, '&lt;').replace(/>/g, '&gt;');
      if (highlighted) {
        var mark = document.createElement('mark');
        mark.textContent = decoder.value;
        target.appendChild(mark);
      } else target.appendChild(document.createTextNode(decoder.value));
    });
  }

  function plain(value) {
    var holder = document.createElement('span');
    highlight(holder, value);
    return holder.textContent;
  }

  function resultUrl(item) {
    var prefix = item.dataType === '文本' ? 'text/' : item.dataType === '资讯' ? 'info/detail/' : 'res/';
    var url = base + prefix + encodeURIComponent(text(item.id));
    if (prefix === 'res/') {
      var context = new URLSearchParams({ key: state.get('key') || '' });
      ['attr', 'match'].forEach(function (name) {
        if (state.get(name)) context.set(name, state.get(name));
      });
      url += '?' + context;
    }
    return url;
  }

  function ordinal(number) {
    var small = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
    if (number < 10) return small[number];
    if (number < 20) return '十' + (number % 10 ? small[number % 10] : '');
    if (number < 100) return small[Math.floor(number / 10)] + '十' + (number % 10 ? small[number % 10] : '');
    return String(number);
  }

  function makeResult(item, index) {
    var article = document.createElement('article');
    article.className = 'sr-result';
    var detailUrl = resultUrl(item);
    var titleText = plain(text(item.title, '未命名文献'));
    var header = document.createElement('div');
    header.className = 'sr-result-header';
    var title = document.createElement('h2');
    var titleLink = document.createElement('a');
    titleLink.href = detailUrl;
    titleLink.appendChild(document.createTextNode('第' + ordinal((currentPage - 1) * pageSize + index + 1) + '本文献名称：《'));
    highlight(titleLink, text(item.title, '未命名文献').replace(/^《|》$/g, ''));
    titleLink.appendChild(document.createTextNode('》'));
    title.appendChild(titleLink);
    var detail = document.createElement('a');
    detail.className = 'sr-detail';
    detail.href = detailUrl;
    detail.textContent = '详 情';
    detail.setAttribute('aria-label', titleText + '详情');
    header.append(title, detail);

    var list = document.createElement('ol');
    list.className = 'sr-snippets';
    var passages = Array.isArray(item.passages) ? item.passages.filter(function (entry) { return text(entry.content); }).slice(0, 3) : [];
    if (passages.length) {
      passages.forEach(function (entry) {
        var line = document.createElement('li');
        var link = document.createElement('a');
        var browse = new URL(base + 'res/browse/' + encodeURIComponent(text(item.id)), location.origin);
        browse.searchParams.set('key', state.get('key') || '');
        browse.searchParams.set('match', state.get('match') || '0');
        if (text(entry.title)) browse.searchParams.set('p', entry.title);
        link.href = browse.pathname + browse.search;
        if (entry.pageNum) link.title = '查看第' + entry.pageNum + '页原文';
        highlight(link, entry.content);
        line.appendChild(link);
        list.appendChild(line);
      });
    } else {
      var snippet = text(item.content);
      if (!snippet) {
        var metadata = [];
        if (text(item.creator)) metadata.push('责任者：' + plain(item.creator));
        if (text(item.publisher)) metadata.push('出版者：' + plain(item.publisher));
        snippet = metadata.join('　') || '暂无摘要，可点击详情查看文献信息。';
      }
      var summary = document.createElement('li');
      highlight(summary, snippet);
      list.appendChild(summary);
    }
    article.append(header, list);
    return article;
  }

  function showMessage(message, error) {
    results.replaceChildren();
    var p = document.createElement('p');
    p.className = 'sr-message' + (error ? ' is-error' : '');
    p.textContent = message;
    if (error) {
      var retry = document.createElement('button');
      retry.type = 'button';
      retry.className = 'sr-retry';
      retry.textContent = '重新检索';
      retry.addEventListener('click', function () { load(currentPage); });
      p.appendChild(retry);
    }
    results.appendChild(p);
  }

  function pagination(loading) {
    pager.querySelectorAll('[data-page]').forEach(function (button) {
      var action = button.dataset.page;
      button.disabled = loading || totalPages < 2 ||
        ((action === 'first' || action === 'prev') && currentPage <= 1) ||
        ((action === 'last' || action === 'next') && currentPage >= totalPages);
    });
    numbers.replaceChildren();
    if (loading) return;
    var start = Math.max(1, Math.min(currentPage - 2, totalPages - 4));
    for (var page = start; page <= Math.min(totalPages, start + 4); page++) {
      var button = document.createElement('button');
      button.type = 'button';
      button.dataset.pageIndex = String(page);
      button.textContent = String(page);
      if (page === currentPage) { button.className = 'is-current'; button.setAttribute('aria-current', 'page'); }
      numbers.appendChild(button);
    }
  }

  function load(page) {
    currentPage = Math.max(1, page || 1);
    if (activeRequest) activeRequest.abort();
    var request = new AbortController();
    activeRequest = request;
    results.setAttribute('aria-busy', 'true');
    showMessage('正在检索文献…');
    status.textContent = '正在加载…';
    bookCount.textContent = recordCount.textContent = '—';
    pagination(true);
    var params = new URLSearchParams();
    ['key','attr','match','rmstId','rltyId','resLib','resType','subjectClass','creator','publisher','keyword','date','dataType'].forEach(function (name) {
      if (state.has(name)) params.set(name, state.get(name));
    });
    params.set('pn', String(currentPage));
    fetch(base + 'search/results?' + params, { credentials: 'same-origin', signal: request.signal })
      .then(function (response) { if (!response.ok) throw new Error('HTTP ' + response.status); return response.json(); })
      .then(function (data) {
        if (activeRequest !== request) return;
        if (!data || data.success !== true || !Array.isArray(data.items)) throw new Error('Invalid search result');
        totalItems = Math.max(0, Number(data.totalLine) || 0);
        totalPages = totalItems ? Math.max(1, Number(data.totalPage) || 1) : 0;
        currentPage = Math.max(1, Number(data.currentPage) || 1);
        pageSize = Math.max(1, parseInt(data.pageSize, 10) || 6);
        if (totalPages && currentPage > totalPages) { load(totalPages); return; }
        bookCount.textContent = String(Math.max(0, Number(data.bookCount) || 0));
        recordCount.textContent = String(Math.max(0, Number(data.recordCount) || 0));
        results.replaceChildren();
        data.items.forEach(function (item, index) { results.appendChild(makeResult(item, index)); });
        if (!data.items.length) showMessage('未找到符合条件的文献，请更换检索词或检索字段。');
        results.setAttribute('aria-busy', 'false');
        status.textContent = '当前' + totalItems + '条文献记录　当前页次' + (totalPages ? currentPage : 0) + '/' + totalPages + '页';
        pagination(false);
        var url = new URL(location.href);
        if (currentPage > 1) url.searchParams.set('pn', String(currentPage));
        else url.searchParams.delete('pn');
        history.replaceState(null, '', url.pathname + url.search);
        activeRequest = null;
      })
      .catch(function (error) {
        if (error.name === 'AbortError' || activeRequest !== request) return;
        showMessage('检索暂时失败，请稍后重试。', true);
        results.setAttribute('aria-busy', 'false');
        status.textContent = '检索失败';
        totalPages = 0;
        pagination(false);
        activeRequest = null;
      });
  }

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    var next = new URL(base + 'search', location.origin);
    next.search = state.toString();
    next.searchParams.set('key', input.value.trim());
    if (field.value) next.searchParams.set('attr', field.value);
    else next.searchParams.delete('attr');
    next.searchParams.delete('pn');
    next.searchParams.delete('accurate');
    history.pushState(null, '', next.pathname + next.search);
    readLocation();
    load(1);
  });
  pager.addEventListener('click', function (event) {
    var button = event.target.closest('button');
    if (!button || button.disabled) return;
    var action = button.dataset.page;
    var page = button.dataset.pageIndex ? Number(button.dataset.pageIndex) :
      action === 'first' ? 1 : action === 'last' ? totalPages : action === 'prev' ? currentPage - 1 : currentPage + 1;
    if (page < 1 || page > totalPages) return;
    var url = new URL(location.href);
    url.searchParams.set('pn', String(page));
    history.pushState(null, '', url.pathname + url.search);
    load(page);
    document.querySelector('.sr-scroll').scrollIntoView({ block: 'start' });
  });
  window.addEventListener('popstate', function () { readLocation(); load(currentPage); });
  readLocation();
  load(currentPage);
})();
