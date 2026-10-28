(function () {
  'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

  var basePath = window.literatureBasePath || '/';
  var defaultCover = basePath + 'images/platform/psb/literature/default-cover.webp';
  var form = document.getElementById('lit-search-form');
  var searchInput = document.getElementById('lit-search-key');
  var searchField = document.getElementById('lit-search-field');
  var typeItems = document.getElementById('lit-type-items');
  var filters = [];
  var grid = document.getElementById('lit-book-grid');
  var pagination = document.querySelector('.lit-pagination');
  var pageNumbers = document.getElementById('lit-page-numbers');
  var viewButtons = Array.from(document.querySelectorAll('[data-view-mode]'));
  var count = document.querySelector('.lit-result-count');
  var url = new URL(window.location.href);
  var storeId = url.searchParams.get('rmstId') || 'all';
  var typeId = url.searchParams.get('rdtyAId') || 'all';
  var key = url.searchParams.get('key') || '';
  var field = url.searchParams.get('field') || 'all';
  var currentPage = Math.max(0, parseInt(url.searchParams.get('pn'), 10) || 0);
  var totalPages = 0;
  var totalItems = 0;
  var pendingRequest = null;
  var viewMode = 'grid';
  var resourceItems = [];
  var resourcesReady = false;
  var pageSize = 15;

  function text(value, fallback) {
    return value == null || String(value).trim() === '' || value === 'null' ? (fallback || '') : String(value);
  }

  function selectType(id) {
    if (!filters.some(function (button) { return button.dataset.typeFilter === id; })) id = 'all';
    typeId = id;
    filters.forEach(function (button) {
      var selected = button.dataset.typeFilter === typeId;
      button.classList.toggle('is-selected', selected);
      button.setAttribute('aria-pressed', String(selected));
    });
  }

  function loadTypes() {
    typeItems.setAttribute('aria-busy', 'true');
    grid.setAttribute('aria-busy', 'true');
    var params = new URLSearchParams();
    if (storeId !== 'all') params.set('rmstId', storeId);
    return fetch(basePath + 'literature/types?' + params, { credentials: 'same-origin' })
      .then(function (response) {
        if (!response.ok) throw new Error('HTTP ' + response.status);
        return response.json();
      })
      .then(function (result) {
        if (!result || result.success !== true || !Array.isArray(result.items)) throw new Error('Type request failed');
        typeItems.replaceChildren();
        [{ id:'all', name:uiText('全部文献') }].concat(result.items).forEach(function (item) {
          var button = document.createElement('button');
          button.type = 'button';
          button.dataset.typeFilter = String(item.id);
          button.textContent = item.name;
          typeItems.appendChild(button);
        });
        filters = Array.from(typeItems.querySelectorAll('[data-type-filter]'));
        typeItems.setAttribute('aria-busy', 'false');
        selectType(typeId);
        loadResources(currentPage);
      })
      .catch(function () {
        typeItems.setAttribute('aria-busy', 'false');
        grid.setAttribute('aria-busy', 'false');
        showMessage(uiText('文献类型加载失败，请稍后重试。'), true);
        count.textContent = uiText('类型加载失败');
      });
  }

  function showMessage(message, isError) {
    grid.replaceChildren();
    var status = document.createElement('p');
    status.className = 'lit-empty' + (isError ? ' lit-error' : '');
    status.textContent = message;
    if (isError) {
      var retry = document.createElement('button');
      retry.type = 'button';
      retry.textContent = uiText('重新加载');
      retry.addEventListener('click', function () { if (!filters.length) loadTypes(); else loadResources(currentPage); });
      status.appendChild(retry);
    }
    grid.appendChild(status);
  }

  function resourceHref(item) {
    var href = basePath + 'res/' + encodeURIComponent(item.id);
    if (key) {
      var context = new URLSearchParams({ key: key });
      var attributes = { title: 'rsdaTitle', creator: 'rsdaCreator', identifier: 'identifier' };
      if (attributes[field]) context.set('attr', attributes[field]);
      href += '?' + context;
    }
    return href;
  }

  function makeCard(item) {
    var card = document.createElement('a');
    card.className = 'lit-book-card';
    card.href = resourceHref(item);
    var titleText = text(item.title, uiText('未命名文献'));
    var identifier = text(item.identifier, '—');
    card.title = uiText('{title}（卷号：{identifier}）', {title:titleText, identifier:identifier});

    var cover = document.createElement('span');
    cover.className = 'lit-book-cover';
    var image = document.createElement('img');
    image.alt = uiText('{title}封面', {title:titleText});
    image.loading = 'lazy';
    image.width = 208;
    image.height = 285;
    image.onerror = function () {
      image.onerror = null;
      image.src = defaultCover;
    };
    image.src = text(item.cover, defaultCover);
    cover.appendChild(image);

    var meta = document.createElement('span');
    meta.className = 'lit-book-meta';
    var title = document.createElement('strong');
    title.textContent = uiText('卷 名：') + titleText;
    var number = document.createElement('small');
    number.textContent = uiText('卷 号：') + identifier;
    meta.append(title, number);
    card.append(cover, meta);
    return card;
  }

  function emptyMessage() {
    return key ? uiText('未找到符合条件的文献，请尝试其他检索内容。') : uiText('当前分类暂无文献资源。');
  }

  function makeListTable(items) {
    var table = document.createElement('table');
    table.className = 'lit-resource-table';
    table.setAttribute('aria-label', uiText('文献资源列表'));
    var heading = table.createTHead().insertRow();
    [uiText('序号'), uiText('卷名'), uiText('语种'), uiText('学科'), uiText('录入日期')].forEach(function (label) {
      var cell = document.createElement('th');
      cell.scope = 'col';
      cell.textContent = label;
      heading.appendChild(cell);
    });
    var body = table.createTBody();
    items.forEach(function (item, index) {
      var row = body.insertRow();
      row.insertCell().textContent = String(currentPage * pageSize + index + 1);
      var title = document.createElement('a');
      title.href = resourceHref(item);
      title.textContent = text(item.title, uiText('未命名文献'));
      row.insertCell().appendChild(title);
      row.insertCell().textContent = text(item.language, '—');
      row.insertCell().textContent = text(item.discipline, '—');
      row.insertCell().textContent = text(item.entryDate, '—');
    });
    if (!items.length) {
      var empty = body.insertRow().insertCell();
      empty.colSpan = 5;
      empty.className = 'lit-list-empty';
      empty.textContent = emptyMessage();
    }
    return table;
  }

  function renderResources() {
    grid.replaceChildren();
    if (viewMode === 'list') grid.appendChild(makeListTable(resourceItems));
    else if (!resourceItems.length) showMessage(emptyMessage());
    else resourceItems.forEach(function (item) { grid.appendChild(makeCard(item)); });
  }

  function pageButton(page) {
    var button = document.createElement('button');
    button.type = 'button';
    button.dataset.pageIndex = String(page);
    button.textContent = String(page + 1);
    if (page === currentPage) {
      button.className = 'is-current';
      button.setAttribute('aria-current', 'page');
    }
    return button;
  }

  function updatePagination(loading) {
    pagination.querySelectorAll('[data-page]').forEach(function (button) {
      var action = button.dataset.page;
      button.disabled = loading || totalPages < 2 ||
        ((action === 'first' || action === 'prev') && currentPage <= 0) ||
        ((action === 'next' || action === 'last') && currentPage >= totalPages - 1);
    });
    pageNumbers.replaceChildren();
    if (!loading && totalPages) {
      var start = Math.max(0, Math.min(currentPage - 2, totalPages - 5));
      for (var page = start; page < Math.min(totalPages, start + 5); page++) pageNumbers.appendChild(pageButton(page));
    }
  }

  function rememberLocation() {
    var nextUrl = new URL(window.location.href);
    ['rmstId', 'rltyId', 'rdtyAId', 'pn', 'key', 'field'].forEach(function (name) { nextUrl.searchParams.delete(name); });
    if (storeId !== 'all') nextUrl.searchParams.set('rmstId', storeId);
    if (typeId !== 'all') nextUrl.searchParams.set('rdtyAId', typeId);
    if (currentPage) nextUrl.searchParams.set('pn', String(currentPage));
    if (key) nextUrl.searchParams.set('key', key);
    if (field !== 'all') nextUrl.searchParams.set('field', field);
    window.history.replaceState(null, '', nextUrl.pathname + nextUrl.search + nextUrl.hash);
  }

  function loadResources(page) {
    currentPage = Math.max(0, page || 0);
    if (pendingRequest) pendingRequest.abort();
    var request = new AbortController();
    pendingRequest = request;
    resourcesReady = false;
    grid.setAttribute('aria-busy', 'true');
    showMessage(uiText('正在读取文献资源…'));
    count.textContent = uiText('正在加载资源…');
    updatePagination(true);
    var params = new URLSearchParams({ pn: String(currentPage), key: key, field: field });
    if (storeId !== 'all') params.set('rmstId', storeId);
    if (typeId !== 'all') params.set('rdtyAId', typeId);

    fetch(basePath + 'literature/resources?' + params, { credentials: 'same-origin', signal: request.signal })
      .then(function (response) {
        if (!response.ok) throw new Error('HTTP ' + response.status);
        return response.json();
      })
      .then(function (result) {
        if (pendingRequest !== request) return;
        if (!result || result.success !== true) throw new Error('Resource request failed');
        totalItems = parseInt(result.totalLine, 10) || 0;
        totalPages = totalItems ? (parseInt(result.totalPage, 10) || 1) : 0;
        currentPage = parseInt(result.currentPage, 10) || 0;
        // A bookmarked page may no longer exist after resources are removed.
        if (totalPages && currentPage >= totalPages) { loadResources(totalPages - 1); return; }
        pageSize = Math.max(1, parseInt(result.pageSize, 10) || 15);
        resourceItems = Array.isArray(result.items) ? result.items : [];
        resourcesReady = true;
        renderResources();
        grid.setAttribute('aria-busy', 'false');
        count.textContent = uiText('当前{count}条记录　当前页次{page}/{pages}页', {count:totalItems, page:totalPages ? currentPage + 1 : 0, pages:totalPages});
        updatePagination(false);
        rememberLocation();
        pendingRequest = null;
      })
      .catch(function (error) {
        if (error.name === 'AbortError' || pendingRequest !== request) return;
        grid.setAttribute('aria-busy', 'false');
        showMessage(uiText('文献资源加载失败，请稍后重试。'), true);
        count.textContent = uiText('资源加载失败');
        totalPages = 0;
        updatePagination(false);
        pendingRequest = null;
      });
  }

  typeItems.addEventListener('click', function (event) {
    var button = event.target.closest('[data-type-filter]');
    if (!button || !filters.includes(button)) return;
    selectType(button.dataset.typeFilter);
    loadResources(0);
  });
  pagination.addEventListener('click', function (event) {
    var button = event.target.closest('button');
    if (!button || button.disabled) return;
    if (button.dataset.pageIndex !== undefined) { loadResources(parseInt(button.dataset.pageIndex, 10)); return; }
    var action = button.dataset.page;
    var page = action === 'first' ? 0 : action === 'prev' ? currentPage - 1 : action === 'last' ? totalPages - 1 : currentPage + 1;
    if (page >= 0 && page < totalPages) loadResources(page);
  });
  form.addEventListener('submit', function (event) {
    event.preventDefault();
    var destination = new URL(basePath + 'search', window.location.origin);
    destination.searchParams.set('key', searchInput.value.trim());
    var attributes = { title: 'rsdaTitle', creator: 'rsdaCreator', identifier: 'identifier' };
    if (attributes[searchField.value]) destination.searchParams.set('attr', attributes[searchField.value]);
    if (storeId !== 'all') {
      destination.searchParams.set('rmstId', storeId);
    }
    if (typeId !== 'all') {
      destination.searchParams.set('rdtyAId', typeId);
      var selected = filters.find(function (button) { return button.dataset.typeFilter === typeId; });
      if (selected) destination.searchParams.set('categoryName', selected.textContent.trim());
    }
    window.location.assign(destination.pathname + destination.search);
  });
  viewButtons.forEach(function (button) {
    button.addEventListener('click', function () {
      viewMode = button.dataset.viewMode;
      grid.classList.toggle('is-list-view', viewMode === 'list');
      if (resourcesReady) renderResources();
      viewButtons.forEach(function (item) {
        var active = item === button;
        item.classList.toggle('is-active', active);
        item.setAttribute('aria-pressed', String(active));
      });
    });
  });
  if (!['all', 'title', 'identifier', 'creator'].includes(field)) field = 'all';
  searchInput.value = key;
  searchField.value = field;
  loadTypes();
})();
