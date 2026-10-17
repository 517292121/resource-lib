(function () {
  'use strict';
  var body = document.body;
  var basePath = body.dataset.basePath || '/';
  var resourceId = body.dataset.resourceId;
  // Use the original URL value; the legacy request wrapper HTML-escapes query parameters.
  var searchContext = new URLSearchParams(window.location.search);
  document.getElementById('rd-search-key').value = searchContext.get('key') || '';
  var metadata = document.getElementById('rd-metadata-list');
  var researchList = document.getElementById('rd-research-list');
  var arrows = Array.from(document.querySelectorAll('[data-research-step]'));
  var more = document.getElementById('rd-research-more');
  var pageStatus = document.getElementById('rd-page-status');
  var feedback = document.getElementById('rd-feedback');
  var state = { page: 0, totalPages: 0, totalItems: 0, request: null };

  // The existing login form reloads its parent after a successful sign-in.
  function openLogin() {
    var dialog = document.getElementById('rd-login-dialog');
    if (!dialog) {
      dialog = document.createElement('dialog');
      dialog.id = 'rd-login-dialog';
      dialog.className = 'rd-login-dialog';
      dialog.setAttribute('aria-label', '会员登录');
      var close = document.createElement('button');
      close.type = 'button'; close.className = 'rd-login-close'; close.textContent = '×';
      close.setAttribute('aria-label', '关闭登录');
      close.addEventListener('click', function () { dialog.close(); });
      var frame = document.createElement('iframe');
      frame.title = '会员登录'; frame.src = basePath + 'member/login';
      dialog.append(close, frame); body.appendChild(dialog);
      dialog.addEventListener('click', function (event) { if (event.target === dialog) dialog.close(); });
    }
    dialog.showModal();
  }
  window.openReg = function () { window.location.assign(basePath + 'register'); };

  function text(value) { return value == null || value === 'null' ? '' : String(value).trim(); }
  function json(url, options) {
    return fetch(url, Object.assign({ credentials: 'same-origin' }, options)).then(function (response) {
      if (!response.ok) throw new Error('HTTP ' + response.status);
      return response.text();
    }).then(function (value) { return value.trim() ? JSON.parse(value) : null; });
  }
  // The legacy metadata endpoint adds HTML links/line breaks. Read it inertly,
  // then create text nodes and trusted search links instead of injecting HTML.
  function plainText(value) {
    var parsed = new DOMParser().parseFromString(text(value).replace(/<br\s*\/?\s*>/gi, '\n'), 'text/html');
    parsed.querySelectorAll('script,style,iframe,object').forEach(function (node) { node.remove(); });
    return text(parsed.body.textContent);
  }
  function searchUrl(key, attr) {
    var params = new URLSearchParams({ key: key });
    if (attr) params.set('attr', attr);
    return basePath + 'search?' + params;
  }
  function loadMetadata() {
    json(basePath + 'res/meta/' + encodeURIComponent(resourceId)).then(function (result) {
      var rows = result && Array.isArray(result.dataList) ? result.dataList : [];
      var fragment = document.createDocumentFragment();
      var count = 0;
      rows.forEach(function (row) {
        var code = text(row[0]).toLowerCase();
        var value = plainText(row[2]);
        var label = plainText(row[1]).replace(/[：:]\s*$/, '');
        if (!value || !label || (code === 'title' && value === text(document.getElementById('rd-title').textContent))) return;
        var line = document.createElement('div');
        var term = document.createElement('dt');
        var definition = document.createElement('dd');
        term.textContent = label;
        if (code === 'creator' || code === 'keyword') {
          var link = document.createElement('a');
          link.href = searchUrl(value, code === 'creator' ? 'rsdaCreator' : 'rsdaKeyword');
          link.textContent = value;
          definition.appendChild(link);
        } else definition.textContent = value;
        line.append(term, definition);
        fragment.appendChild(line);
        count++;
      });
      if (count) metadata.replaceChildren(fragment);
    }).catch(function () { /* Server-rendered basic metadata remains readable. */ });
  }
  function safeUrl(value) {
    value = text(value);
    if (!/^(https?:\/\/|\/(?!\/))/i.test(value)) return '';
    try {
      var url = new URL(value, window.location.origin);
      return /^https?:$/.test(url.protocol) ? url.href : '';
    } catch (error) { return ''; }
  }
  function initResourceLinks() {
    ['rd-annotation-link', 'rd-image-link'].forEach(function (id) {
      var link = document.getElementById(id);
      var destination = safeUrl(link.dataset.url);
      if (!destination) return;
      link.href = destination;
      link.removeAttribute('aria-disabled');
      link.title = '打开' + link.textContent.trim();
    });
  }
  function controls(loading) {
    arrows.forEach(function (button) {
      button.disabled = loading || !state.totalPages ||
        (button.dataset.researchStep === '-1' ? state.page <= 0 : state.page >= state.totalPages - 1);
    });
    more.hidden = state.totalPages <= 1 || state.page >= state.totalPages - 1;
    more.disabled = loading;
    pageStatus.textContent = loading || !state.totalItems ? '' : '第 ' + (state.page + 1) + ' / ' + state.totalPages + ' 页 · 共 ' + state.totalItems + ' 条';
  }
  function message(value, retry) {
    var paragraph = document.createElement('p');
    paragraph.className = 'rd-empty';
    paragraph.textContent = value;
    if (retry) {
      var button = document.createElement('button');
      button.type = 'button';
      button.textContent = '重新加载';
      button.addEventListener('click', function () { loadResearch(0, false); });
      paragraph.appendChild(button);
    }
    researchList.replaceChildren(paragraph);
  }
  function researchItem(item, index) {
    var card = document.createElement('a');
    card.className = 'rd-research-item';
    card.href = basePath + 'res/' + encodeURIComponent(item.id);
    var number = document.createElement('span');
    number.className = 'rd-research-number';
    number.textContent = String(index + 1).padStart(2, '0');
    number.setAttribute('aria-hidden', 'true');
    var content = document.createElement('div');
    content.className = 'rd-research-item-content';
    var title = document.createElement('h3');
    title.textContent = text(item.title) || '未命名文献';
    var description = document.createElement('p');
    var creator = document.createElement('span');
    creator.textContent = '责任者：' + (text(item.creator) || '未详');
    var date = document.createElement('span');
    date.textContent = '日期：' + (text(item.date) || '未详');
    description.append(creator, date);
    content.append(title, description);
    card.append(number, content);
    return card;
  }
  function loadResearch(page, append) {
    if (state.request) state.request.abort();
    state.page = page;
    if (!/^[1-9]\d*$/.test(body.dataset.subjectId || '')) {
      state.totalPages = state.totalItems = 0;
      researchList.setAttribute('aria-busy', 'false');
      message('暂无与此资源关联的研究内容。');
      controls(false);
      return;
    }
    var request = new AbortController();
    state.request = request;
    researchList.setAttribute('aria-busy', 'true');
    if (!append) message('正在读取相关研究…');
    controls(true);
    var params = new URLSearchParams({ pn: String(page) });
    json(basePath + 'res/research/' + encodeURIComponent(resourceId) + '?' + params, { signal: request.signal })
      .then(function (result) {
        if (state.request !== request) return;
        if (!result || result.success !== true) throw new Error('Research request failed');
        state.totalItems = Number(result.totalLine) || 0;
        state.totalPages = Number(result.totalPage) || 0;
        var items = Array.isArray(result.items) ? result.items : [];
        if (!append) researchList.replaceChildren();
        if (!items.length && !append) message('暂无与此资源关联的研究内容。');
        items.forEach(function (item, index) { researchList.appendChild(researchItem(item, page * 6 + index)); });
        state.request = null;
        researchList.setAttribute('aria-busy', 'false');
        controls(false);
      }).catch(function (error) {
        if (error.name === 'AbortError' || state.request !== request) return;
        state.request = null;
        state.totalPages = state.totalItems = 0;
        researchList.setAttribute('aria-busy', 'false');
        message('相关研究暂时加载失败。', true);
        controls(false);
      });
  }
  arrows.forEach(function (button) { button.addEventListener('click', function () { loadResearch(state.page + Number(button.dataset.researchStep), false); }); });
  more.addEventListener('click', function () { loadResearch(state.page + 1, true); });
  document.getElementById('rd-favorite').addEventListener('click', function (event) {
    var button = event.currentTarget;
    if (!/^[1-9]\d*$/.test(body.dataset.memberId || '')) {
      feedback.textContent = '请先登录后收藏。';
      var login = document.createElement('button');
      login.type = 'button'; login.textContent = '前往登录';
      login.addEventListener('click', openLogin);
      feedback.appendChild(login);
      return;
    }
    button.disabled = true;
    json(basePath + 'res/favorite?rsdaId=' + encodeURIComponent(resourceId)).then(function (result) {
      feedback.textContent = result && (result.success === true || result.success === 'true') ? '收藏成功。' : text(result && result.errmsg) || '收藏失败，请稍后重试。';
    }).catch(function () { feedback.textContent = '收藏失败，请稍后重试。'; }).finally(function () { button.disabled = false; });
  });
  initResourceLinks();
  loadMetadata();
  loadResearch(0, false);
})();
