(function () {
  'use strict';
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
  var typeId = url.searchParams.get('rltyId') || 'all';
  var key = url.searchParams.get('key') || '';
  var field = url.searchParams.get('field') || 'all';
  var currentPage = Math.max(0, parseInt(url.searchParams.get('pn'), 10) || 0);
  var totalPages = 0;
  var totalItems = 0;
  var pendingRequest = null;

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
        [{ id:'all', name:'全部文献' }].concat(result.items).forEach(function (item) {
          var button = document.createElement('button');
          button.type = 'button';
          button.dataset.typeFilter = String(item.id);
          button.textContent = item.name;
          typeItems.appendChild(button);
        });
        filters = Array.from(typeItems.querySelectorAll('[data-type-filter]'));
        typeItems.style.setProperty('--lit-type-gap', filters.length >= 7 ? '8' : '64');
        typeItems.setAttribute('aria-busy', 'false');
        selectType(typeId);
        loadResources(currentPage);
      })
      .catch(function () {
        typeItems.setAttribute('aria-busy', 'false');
        grid.setAttribute('aria-busy', 'false');
        showMessage('文献类型加载失败，请稍后重试。', true);
        count.textContent = '类型加载失败';
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
      retry.textContent = '重新加载';
      retry.addEventListener('click', function () { if (!filters.length) loadTypes(); else loadResources(currentPage); });
      status.appendChild(retry);
    }
    grid.appendChild(status);
  }

  function makeCard(item) {
    var card = document.createElement('a');
    card.className = 'lit-book-card';
    card.href = basePath + 'res/' + encodeURIComponent(item.id);
    if (key) {
      var context = new URLSearchParams({ key: key });
      var attributes = { title: 'rsdaTitle', creator: 'rsdaCreator', identifier: 'identifier' };
      if (attributes[field]) context.set('attr', attributes[field]);
      card.href += '?' + context;
    }
    var titleText = text(item.title, '未命名文献');
    var identifier = text(item.identifier, '—');
    card.title = titleText + '（卷号：' + identifier + '）';

    var cover = document.createElement('span');
    cover.className = 'lit-book-cover';
    var image = document.createElement('img');
    image.alt = titleText + '封面';
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
    title.textContent = '卷 名：' + titleText;
    var number = document.createElement('small');
    number.textContent = '卷 号：' + identifier;
    meta.append(title, number);
    card.append(cover, meta);
    return card;
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
    ['rmstId', 'rltyId', 'pn', 'key', 'field'].forEach(function (name) { nextUrl.searchParams.delete(name); });
    if (storeId !== 'all') nextUrl.searchParams.set('rmstId', storeId);
    if (typeId !== 'all') nextUrl.searchParams.set('rltyId', typeId);
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
    grid.setAttribute('aria-busy', 'true');
    showMessage('正在读取文献资源…');
    count.textContent = '正在加载资源…';
    updatePagination(true);
    var params = new URLSearchParams({ pn: String(currentPage), key: key, field: field });
    if (storeId !== 'all') params.set('rmstId', storeId);
    if (typeId !== 'all') params.set('rltyId', typeId);

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
        var items = Array.isArray(result.items) ? result.items : [];
        grid.replaceChildren();
        if (!items.length) showMessage(key ? '未找到符合条件的文献，请尝试其他检索内容。' : '当前分类暂无文献资源。');
        else items.forEach(function (item) { grid.appendChild(makeCard(item)); });
        grid.setAttribute('aria-busy', 'false');
        count.textContent = '当前' + totalItems + '条记录　当前页次' + (totalPages ? currentPage + 1 : 0) + '/' + totalPages + '页';
        updatePagination(false);
        rememberLocation();
        pendingRequest = null;
      })
      .catch(function (error) {
        if (error.name === 'AbortError' || pendingRequest !== request) return;
        grid.setAttribute('aria-busy', 'false');
        showMessage('文献资源加载失败，请稍后重试。', true);
        count.textContent = '资源加载失败';
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
      destination.searchParams.set('rltyId', typeId);
      var selected = filters.find(function (button) { return button.dataset.typeFilter === typeId; });
      if (selected) destination.searchParams.set('resType', selected.textContent.trim());
    }
    window.location.assign(destination.pathname + destination.search);
  });
  viewButtons.forEach(function (button) {
    button.addEventListener('click', function () {
      grid.classList.toggle('is-list-view', button.dataset.viewMode === 'list');
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
