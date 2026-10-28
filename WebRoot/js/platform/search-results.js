(function () {
  'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

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
  var advanced = false;
  var conditions = [];
  var fieldLabels = {rsdaTitle:uiText('题名'), rsdaCreator:uiText('责任者'), rsdaPublisher:uiText('出版者'), rsdaSubject:uiText('主题'), rsdaKeyword:uiText('关键词'), rsdaDescription:uiText('描述'), rsdaIdentifier:uiText('卷号')};

  function advancedParameter(name) {
    return /^(?:KEY[1-4]|COL[1-4]|LIKE[1-4]|CON[2-4]|libId|typeId|SORT)$/.test(name);
  }

  function text(value, fallback) {
    return value == null || value === 'null' || String(value).trim() === '' ? (fallback || '') : String(value);
  }

  function readLocation() {
    var url = new URL(location.href);
    state = new URLSearchParams(url.search);
    advanced = /\/resAdvs\/?$/.test(url.pathname) || document.body.dataset.searchMode === 'advanced';
    conditions = [];
    if (advanced) {
      for (var i = 1; i <= 4; i++) {
        var key = text(state.get('KEY' + i)).trim();
        if (key) conditions.push({index:i, key:key, field:state.get('COL' + i) || 'rsdaTitle', exact:state.get('LIKE' + i) === '=', connector:state.get('CON' + i) || 'AND'});
      }
      var primary = conditions[0] || {index:1, key:'', field:'rsdaTitle', exact:false};
      state.set('key', primary.key);
      state.set('attr', primary.field);
      state.set('match', primary.exact ? '1' : '0');
      if (!Array.from(field.options).some(function (option) { return option.value === primary.field; })) {
        var option = document.createElement('option'); option.value = primary.field;
        option.textContent = fieldLabels[primary.field] || uiText('题名'); field.appendChild(option);
      }
    }
    field.options[0].disabled = advanced;
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
    currentPage = Math.max(1, parseInt(state.get('pn'), 10) || (advanced ? (parseInt(state.get('currentPage'), 10) || 0) + 1 : 1));
    var filters = ['resLib', 'resType', 'categoryName', 'creator', 'publisher', 'keyword', 'date'];
    var note = advanced ? uiText('高级检索：') + conditions.map(function (condition, index) {
      return (index ? (condition.connector === 'OR' ? uiText(' 或 ') : uiText(' 且 ')) : '') + (fieldLabels[condition.field] || uiText('题名')) +
        (condition.exact ? uiText('等于') : uiText('包含')) + '“' + condition.key + '”';
    }).join('') : filters.map(function (name) { return state.get(name); }).filter(Boolean).join(' · ');
    document.getElementById('sr-filter-note').textContent = note;
    document.getElementById('sr-filter-note').title = note;
    document.title = (input.value ? input.value + ' - ' : '') + uiText('检索结果 - 西藏大学敦煌文献智能数据库');
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

  function resultText(target, value, name) {
    if (!advanced) { highlight(target, value); return; }
    var source = text(value), lower = source.toLowerCase(), offset = 0;
    var keys = conditions.filter(function (condition) {
      return condition.field === name && (!condition.exact || lower === condition.key.toLowerCase());
    })
      .map(function (condition) { return condition.key.toLowerCase(); }).filter(Boolean);
    while (offset < source.length) {
      var next = -1, length = 0;
      keys.forEach(function (key) {
        var position = lower.indexOf(key, offset);
        if (position >= 0 && (next < 0 || position < next || (position === next && key.length > length))) {
          next = position; length = key.length;
        }
      });
      if (next < 0) { target.appendChild(document.createTextNode(source.slice(offset))); break; }
      target.appendChild(document.createTextNode(source.slice(offset, next)));
      var mark = document.createElement('mark'); mark.textContent = source.slice(next, next + length);
      target.appendChild(mark); offset = next + length;
    }
  }

  function resultUrl(item) {
    var prefix = item.dataType === '文本' ? 'text/' : item.dataType === '资讯' ? 'info/detail/' : 'res/';
    var url = base + prefix + encodeURIComponent(text(item.id));
    if (prefix === 'res/') {
      var context = new URLSearchParams({ key: state.get('key') || '' });
      ['attr', 'match'].forEach(function (name) {
        if (state.get(name)) context.set(name, state.get(name));
      });
      if (advanced) state.forEach(function (value, name) { if (advancedParameter(name)) context.append(name, value); });
      url += '?' + context;
    }
    return url;
  }

  function previewUrl(item, passage, excerpt) {
    var url = new URL(base + 'res/browse/' + encodeURIComponent(text(item.id)), location.origin);
    url.searchParams.set('preview', 'search');
    url.searchParams.set('key', state.get('key') || '');
    url.searchParams.set('match', state.get('match') || '0');
    url.searchParams.set('bright', 'true');
    if (passage && text(passage.title)) url.searchParams.set('p', passage.title);
    if (passage && passage.pageNum && /^\d+$/.test(text(passage.title))) url.searchParams.set('page', passage.pageNum);
    var marks = Array.from(excerpt.querySelectorAll('mark')).filter(function (mark) { return mark.textContent.trim(); });
    var hits = [];
    marks.forEach(function (mark) {
      var hit = mark.textContent.trim().slice(0, 256);
      if (!hits.includes(hit) && hits.length < 16) hits.push(hit);
    });
    hits.forEach(function (hit) { url.searchParams.append('hit', hit); });
    if (marks.length) {
      // Start the context at the displayed highlight, not at metadata prepended by the indexer.
      // This also distinguishes repeated hits in different passages/chapters.
      var range = document.createRange();
      range.selectNodeContents(excerpt); range.setEnd(marks[0], 0);
      var start = range.toString().length;
      url.searchParams.set('context', excerpt.textContent.slice(start, start + 160));
    }
    return url.pathname + url.search;
  }

  function ordinal(number) {
    if (window.PlatformI18n && window.PlatformI18n.language === 'zw') return String(number);
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
    var titleText = advanced ? text(item.title, uiText('未命名文献')) : plain(text(item.title, uiText('未命名文献')));
    var header = document.createElement('div');
    header.className = 'sr-result-header';
    var title = document.createElement('h2');
    var titleLink = document.createElement('a');
    titleLink.href = detailUrl;
    var titleParts = uiText('第{number}本文献名称：《{title}》', {number:ordinal((currentPage - 1) * pageSize + index + 1)}).split('{title}');
    titleLink.appendChild(document.createTextNode(titleParts[0]));
    resultText(titleLink, text(item.title, uiText('未命名文献')).replace(/^《|》$/g, ''), 'rsdaTitle');
    titleLink.appendChild(document.createTextNode(titleParts.slice(1).join('{title}')));
    title.appendChild(titleLink);
    var detail = document.createElement('a');
    detail.className = 'sr-detail';
    detail.href = detailUrl;
    detail.textContent = uiText('详 情');
    detail.setAttribute('aria-label', uiText('查看{title}详情', {title:titleText}));
    var actions = document.createElement('div');
    actions.className = 'sr-result-actions';
    actions.appendChild(detail);
    header.append(title, actions);

    var list = document.createElement('ol');
    list.className = 'sr-snippets';
    var passages = Array.isArray(item.passages) ? item.passages.filter(function (entry) { return text(entry.content); }).slice(0, 3) : [];
    if (passages.length) {
      passages.forEach(function (entry) {
        var line = document.createElement('li');
        var link = document.createElement('a');
        if (entry.pageNum) link.title = uiText('查看第{page}页原文', {page:entry.pageNum});
        highlight(link, entry.content);
        link.href = previewUrl(item, entry, link);
        line.appendChild(link);
        list.appendChild(line);
      });
    } else if (advanced) {
      var fields = item.fields || {}, seen = [];
      conditions.map(function (condition) { return condition.field; })
        .concat(['rsdaDescription', 'rsdaCreator', 'rsdaPublisher', 'rsdaKeyword']).forEach(function (name) {
          if (name === 'rsdaTitle' || seen.includes(name) || seen.length >= 3 || !text(fields[name])) return;
          seen.push(name);
          var line = document.createElement('li');
          line.appendChild(document.createTextNode((fieldLabels[name] || uiText('文献信息')) + '：'));
          resultText(line, fields[name], name); list.appendChild(line);
        });
      if (!seen.length) {
        var fallback = document.createElement('li'); fallback.textContent = uiText('可点击详情查看文献信息。'); list.appendChild(fallback);
      }
    } else {
      var snippet = text(item.content);
      if (!snippet) {
        var metadata = [];
        if (text(item.creator)) metadata.push(uiText('责任者：') + plain(item.creator));
        if (text(item.publisher)) metadata.push(uiText('出版者：') + plain(item.publisher));
        snippet = metadata.join('　') || uiText('暂无摘要，可点击详情查看文献信息。');
      }
      var summary = document.createElement('li');
      highlight(summary, snippet);
      list.appendChild(summary);
    }
    if (!item.dataType || item.dataType === '资源') {
      var preview = document.createElement('a');
      preview.className = 'sr-preview';
      var excerpt = list.querySelector('mark');
      excerpt = excerpt ? excerpt.closest('li') : list.querySelector('li') || list;
      var passage = passages.length ? passages[Array.from(list.children).indexOf(excerpt)] : null;
      preview.href = previewUrl(item, passage, excerpt);
      preview.textContent = uiText('预览');
      preview.setAttribute('aria-label', uiText('预览') + '：' + titleText);
      preview.target = '_blank'; preview.rel = 'noopener noreferrer';
      actions.appendChild(preview);
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
      retry.textContent = uiText('重新检索');
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
    showMessage(uiText('正在检索文献…'));
    status.textContent = uiText('正在加载…');
    bookCount.textContent = recordCount.textContent = '—';
    pagination(true);
    var params = new URLSearchParams();
    ['key','attr','match','rmstId','rltyId','rdtyAId','resLib','resType','subjectClass','creator','publisher','keyword','date','dataType'].forEach(function (name) {
      if (state.has(name)) params.set(name, state.get(name));
    });
    if (advanced) {
      params = new URLSearchParams();
      state.forEach(function (value, name) { if (advancedParameter(name)) params.append(name, value); });
    }
    params.set('pn', String(currentPage));
    fetch(base + (advanced ? 'search/advanced-results?' : 'search/results?') + params, { credentials: 'same-origin', signal: request.signal })
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
        if (!data.items.length) showMessage(uiText('未找到符合条件的文献，请更换检索词或检索字段。'));
        results.setAttribute('aria-busy', 'false');
        status.textContent = uiText('当前{count}条文献记录　当前页次{page}/{pages}页', {count:totalItems, page:totalPages ? currentPage : 0, pages:totalPages});
        pagination(false);
        var url = new URL(location.href);
        if (advanced) url.searchParams.delete('currentPage');
        if (currentPage > 1) url.searchParams.set('pn', String(currentPage));
        else url.searchParams.delete('pn');
        history.replaceState(null, '', url.pathname + url.search);
        activeRequest = null;
      })
      .catch(function (error) {
        if (error.name === 'AbortError' || activeRequest !== request) return;
        showMessage(uiText('检索暂时失败，请稍后重试。'), true);
        results.setAttribute('aria-busy', 'false');
        status.textContent = uiText('检索失败');
        totalPages = 0;
        pagination(false);
        activeRequest = null;
      });
  }

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    var next = new URL(base + (advanced ? 'resAdvs' : 'search'), location.origin);
    next.search = state.toString();
    next.searchParams.set('key', input.value.trim());
    if (field.value) next.searchParams.set('attr', field.value);
    else next.searchParams.delete('attr');
    next.searchParams.delete('pn');
    next.searchParams.delete('accurate');
    if (advanced) {
      var primary = conditions[0] || {index:1};
      next.searchParams.set('KEY' + primary.index, input.value.trim());
      next.searchParams.set('COL' + primary.index, field.value || 'rsdaTitle');
      ['key', 'attr', 'match', 'currentPage'].forEach(function (name) { next.searchParams.delete(name); });
    }
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
