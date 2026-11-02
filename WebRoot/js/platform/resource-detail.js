(function () {
  'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

  var body = document.body;
  var basePath = body.dataset.basePath || '/';
  var resourceId = body.dataset.resourceId;
  // Use the original URL value; the legacy request wrapper HTML-escapes query parameters.
  var searchContext = new URLSearchParams(window.location.search);
  document.getElementById('rd-search-key').value = searchContext.get('key') || '';
  var metadata = document.getElementById('rd-metadata-list');
  var researchList = document.getElementById('rd-research-list');
  var arrows = Array.from(document.querySelectorAll('[data-research-step]'));
  var researchTabs = Array.from(document.querySelectorAll('[data-research-kind]'));
  var more = document.getElementById('rd-research-more');
  var pageStatus = document.getElementById('rd-page-status');
  var state = { kind: 'paper', page: 0, totalPages: 0, totalItems: 0, request: null };

  // Bind headings to stable metadata codes, never to the editable repoTitle returned by the API.
  var metadataLabels = {
    title:'题名', alternative:'交替题名', creator:'责任者', subject:'主题',
    description:'描述', publisher:'出版者', contributor:'其他责任者', date:'日期',
    created:'创建日期', valid:'生效日期', available:'可获得日期', issued:'发布日期',
    modified:'修改日期', datecopyrighted:'版权日期', datecaptured:'获取日期',
    type:'类型', format:'格式', identifier:'标识符', source:'来源', language:'语种',
    relation:'关联', coverage:'时空范围', rights:'权限', keyword:'关键词',
    abstract:'摘要', tableofcontents:'目次', extent:'范围', medium:'媒体',
    bibliographiccitation:'文献出处', organization:'机构', role:'责任方式',
    isversionof:'原版本', hasversion:'其他版本', isreplacedby:'被替代', replaces:'替代',
    isrequiredby:'被需求', requires:'需求', ispartof:'包含于', haspart:'包含',
    isreferencedby:'被参照', references:'参照', isformatof:'格式转换于', hasformat:'格式转换为',
    conformsto:'遵循', spatial:'空间范围', temporal:'时间范围', accessrights:'访问权限',
    audience:'适用对象', edition:'版本', location:'馆藏位置', email:'Email', tel:'电话',
    address:'地址', discipline:'学科', clc:'中图法分类号', rightsstatement:'权限声明',
    securitylclassfication:'保密级别', page:'页码', fund:'资助', dateaccepted:'接受日期',
    istranslatedfrom:'译自', f:'副标题', institution:'培养单位', achievementslist:'成果目录',
    descriptionrelation:'相关文献附注', defensedate:'答辩日期', datesubmitted:'提交日期',
    relationadditionresource:'附加资源关联', degree:'学位', grantor:'学位授予单位',
    callnumber:'典藏号', xueke:'学科'
  };
  // Current book metadata uses Subject/Medium for 题记 and Keyword for 卷号.
  var metadataTypeLabels = {
    '1': {creator:'创建者', subject:'题记', description:'注释', abstract:'内容',
      extent:'行号', medium:'题记', keyword:'卷号', coverage:'空间范围', bibliographiccitation:'书目引文'},
    '3': {abstract:'文摘', discipline:'学科专业', location:'馆藏信息', available:'全文可获得日期'}
  };
  function metadataLabel(code) {
    var typeLabels = metadataTypeLabels[body.dataset.resourceType] || {};
    var label = Object.prototype.hasOwnProperty.call(typeLabels, code) ? typeLabels[code] :
      Object.prototype.hasOwnProperty.call(metadataLabels, code) ? metadataLabels[code] : '其他信息';
    return label;
  }
  var metadataOrder = ['语种', '卷号', '题名', '注释', '学科', '内容', '行号', '题记'];
  function metadataPriority(code) {
    var index = metadataOrder.indexOf(metadataLabel(code));
    return index < 0 ? metadataOrder.length : index;
  }

  function text(value) { return value == null || value === 'null' ? '' : String(value).trim(); }
  function json(url, options) {
    return fetch(url, Object.assign({ credentials: 'same-origin' }, options)).then(function (response) {
      if (!response.ok) throw new Error('HTTP ' + response.status);
      return response.text();
    }).then(function (value) { return value.trim() ? JSON.parse(value) : null; });
  }
  // Metadata values are display-only, even when the legacy endpoint returns links.
  // Read its HTML inertly and render text nodes instead of links or injected HTML.
  function plainText(value) {
    var parsed = new DOMParser().parseFromString(text(value).replace(/<br\s*\/?\s*>/gi, '\n'), 'text/html');
    parsed.querySelectorAll('script,style,iframe,object').forEach(function (node) { node.remove(); });
    return text(parsed.body.textContent);
  }
  function loadMetadata() {
    json(basePath + 'res/meta/' + encodeURIComponent(resourceId)).then(function (result) {
      var rows = result && Array.isArray(result.dataList) ? result.dataList : [];
      var fragment = document.createDocumentFragment();
      var count = 0;
      // Sort by fixed headings before translation, independently of backend template order.
      rows.map(function (row, index) { return {row: row, index: index}; }).sort(function (a, b) {
        return metadataPriority(text(a.row[0]).toLowerCase()) - metadataPriority(text(b.row[0]).toLowerCase()) || a.index - b.index;
      }).forEach(function (entry) {
        var row = entry.row;
        var code = text(row[0]).toLowerCase();
        var value = plainText(row[2]);
        var label = uiText(metadataLabel(code));
        if (!value) return;
        var line = document.createElement('div');
        var term = document.createElement('dt');
        var definition = document.createElement('dd');
        term.textContent = label;
        definition.textContent = value;
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
    ['rd-image-link'].forEach(function (id) {
      var link = document.getElementById(id);
      if (!link) return;
      var destination = safeUrl(link.dataset.url);
      if (!destination) return;
      link.href = destination;
      link.removeAttribute('aria-disabled');
      link.title = uiText('打开{title}', {title:link.textContent.trim()});
    });
  }
  function controls(loading) {
    arrows.forEach(function (button) {
      button.disabled = loading || !state.totalPages ||
        (button.dataset.researchStep === '-1' ? state.page <= 0 : state.page >= state.totalPages - 1);
    });
    more.hidden = state.totalPages <= 1 || state.page >= state.totalPages - 1;
    more.disabled = loading;
    pageStatus.textContent = loading || !state.totalItems ? '' : uiText('第 {page} / {pages} 页 · 共 {count} 条', {page:state.page + 1, pages:state.totalPages, count:state.totalItems});
  }
  function message(value, retry) {
    var paragraph = document.createElement('p');
    paragraph.className = 'rd-empty';
    paragraph.textContent = value;
    if (retry) {
      var button = document.createElement('button');
      button.type = 'button';
      button.textContent = uiText('重新加载');
      button.addEventListener('click', function () { loadResearch(0, false); });
      paragraph.appendChild(button);
    }
    researchList.replaceChildren(paragraph);
  }
  function researchItem(item, index) {
    var card = document.createElement('a');
    card.className = 'rd-research-item';
    // Decode the legacy wrapper's ampersands without treating URL query values as HTML.
    var source = text(item.source).replace(/&amp;/g, '&');
    var sourceUrl = /^https?:\/\//i.test(source) ? safeUrl(source) : '';
    card.href = sourceUrl || basePath + 'res/' + encodeURIComponent(item.id);
    card.target = '_blank';
    card.rel = 'noopener noreferrer';
    var number = document.createElement('span');
    number.className = 'rd-research-number';
    number.textContent = String(index + 1).padStart(2, '0');
    number.setAttribute('aria-hidden', 'true');
    var content = document.createElement('div');
    content.className = 'rd-research-item-content';
    var title = document.createElement('h3');
    title.textContent = text(item.title) || uiText('未命名文献');
    var description = document.createElement('p');
    var creator = document.createElement('span');
    creator.textContent = uiText('责任者：') + (text(item.creator) || uiText('未详'));
    var date = document.createElement('span');
    date.textContent = uiText('日期：') + (text(item.date) || uiText('未详'));
    description.append(creator, date);
    content.append(title, description);
    card.append(number, content);
    return card;
  }
  function loadResearch(page, append) {
    if (state.request) state.request.abort();
    state.page = page;
    if (!/^[1-9]\d*$/.test(resourceId || '')) {
      state.totalPages = state.totalItems = 0;
      researchList.setAttribute('aria-busy', 'false');
      message(uiText('暂无与此资源关联的研究内容。'));
      controls(false);
      return;
    }
    var request = new AbortController();
    state.request = request;
    researchList.setAttribute('aria-busy', 'true');
    if (!append) message(uiText('正在读取相关研究…'));
    controls(true);
    var params = new URLSearchParams({ pn: String(page), kind: state.kind });
    json(basePath + 'res/research/' + encodeURIComponent(resourceId) + '?' + params, { signal: request.signal })
      .then(function (result) {
        if (state.request !== request) return;
        if (!result || result.success !== true) throw new Error('Research request failed');
        state.totalItems = Number(result.totalLine) || 0;
        state.totalPages = Number(result.totalPage) || 0;
        var items = Array.isArray(result.items) ? result.items : [];
        if (!append) researchList.replaceChildren();
        if (!items.length && !append) message(uiText('暂无与此资源关联的研究内容。'));
        items.forEach(function (item, index) { researchList.appendChild(researchItem(item, page * 6 + index)); });
        state.request = null;
        researchList.setAttribute('aria-busy', 'false');
        controls(false);
      }).catch(function (error) {
        if (error.name === 'AbortError' || state.request !== request) return;
        state.request = null;
        state.totalPages = state.totalItems = 0;
        researchList.setAttribute('aria-busy', 'false');
        message(uiText('相关研究暂时加载失败。'), true);
        controls(false);
      });
  }
  arrows.forEach(function (button) { button.addEventListener('click', function () { loadResearch(state.page + Number(button.dataset.researchStep), false); }); });
  function selectResearchTab(tab) {
    if (tab.dataset.researchKind === state.kind) return;
    state.kind = tab.dataset.researchKind;
    researchTabs.forEach(function (button) {
      var selected = button === tab;
      button.classList.toggle('is-active', selected);
      button.setAttribute('aria-selected', String(selected));
      button.tabIndex = selected ? 0 : -1;
    });
    researchList.setAttribute('aria-labelledby', tab.id);
    state.totalPages = state.totalItems = 0;
    loadResearch(0, false);
  }
  researchTabs.forEach(function (tab, index) {
    tab.addEventListener('click', function () { selectResearchTab(tab); });
    tab.addEventListener('keydown', function (event) {
      var next = event.key === 'ArrowRight' ? (index + 1) % researchTabs.length : event.key === 'ArrowLeft' ? (index + researchTabs.length - 1) % researchTabs.length : event.key === 'Home' ? 0 : event.key === 'End' ? researchTabs.length - 1 : -1;
      if (next < 0) return;
      event.preventDefault(); researchTabs[next].focus(); selectResearchTab(researchTabs[next]);
    });
  });
  more.addEventListener('click', function () { loadResearch(state.page + 1, true); });
  initResourceLinks();
  loadMetadata();
  loadResearch(0, false);
})();
