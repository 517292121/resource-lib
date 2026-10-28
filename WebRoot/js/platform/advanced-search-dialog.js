(function () {
  'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

  if (window.openAdvancedSearch) return;
  var script = document.currentScript;
  var endpoint = new URL('../../resAdv', script.src);
  var dialog, frame, closeButton, shade, lastTrigger;

  function restore() {
    document.body.classList.remove('dh-advanced-search-open');
    if (shade) { shade.remove(); shade = null; }
    if (lastTrigger && lastTrigger.isConnected) lastTrigger.focus();
  }

  function close() {
    if (typeof dialog.close === 'function') dialog.close();
    else { dialog.removeAttribute('open'); restore(); }
  }

  function create() {
    dialog = document.createElement('dialog');
    dialog.className = 'dh-advanced-dialog';
    dialog.setAttribute('aria-labelledby', 'dh-advanced-dialog-title');
    var header = document.createElement('div');
    header.className = 'dh-advanced-dialog-header';
    var title = document.createElement('h2');
    title.className = 'dh-advanced-dialog-title';
    title.id = 'dh-advanced-dialog-title';
    title.textContent = uiText('高级检索');
    closeButton = document.createElement('button');
    closeButton.type = 'button';
    closeButton.className = 'dh-advanced-dialog-close';
    closeButton.setAttribute('aria-label', uiText('关闭高级检索'));
    closeButton.textContent = '×';
    closeButton.addEventListener('click', close);
    frame = document.createElement('iframe');
    frame.title = uiText('高级检索条件');
    frame.addEventListener('load', function () {
      // The form submits to _parent, preserving the existing results handler.
      var doc = frame.contentDocument;
      if (doc) doc.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') { event.preventDefault(); close(); }
      });
    });
    header.append(title, closeButton);
    dialog.append(header, frame);
    dialog.addEventListener('close', restore);
    document.body.appendChild(dialog);
    document.addEventListener('keydown', function (event) {
      if (event.key === 'Escape' && dialog.hasAttribute('open')) {
        event.preventDefault();
        close();
      }
    });
  }

  window.openAdvancedSearch = function (trigger) {
    if (!dialog) create();
    if (dialog.hasAttribute('open')) return false;
    lastTrigger = trigger && trigger.nodeType === 1 ? trigger : document.activeElement;
    var target = new URL(endpoint.href);
    var context = new URL(location.href).searchParams;
    var selected = document.querySelector('[data-type-filter].is-selected');
    var typeId = selected ? selected.dataset.typeFilter : (context.get('rdtyAId') || context.get('rltyId'));
    context.forEach(function (value, name) {
      if (/^(?:KEY[1-4]|COL[1-4]|LIKE[1-4]|CON[2-4]|libId|typeId)$/.test(name)) target.searchParams.append(name, value);
    });
    if (/^\d+$/.test(context.get('rmstId') || '')) target.searchParams.set('rmstId', context.get('rmstId'));
    if (/^\d+$/.test(typeId || '')) target.searchParams.set('typeId', typeId);
    frame.src = target.href;
    if (typeof dialog.showModal === 'function') dialog.showModal();
    else {
      dialog.classList.add('is-fallback');
      dialog.setAttribute('open', '');
      dialog.setAttribute('role', 'dialog');
      dialog.setAttribute('aria-modal', 'true');
      shade = document.createElement('div');
      shade.className = 'dh-advanced-shade';
      document.body.appendChild(shade);
    }
    document.body.classList.add('dh-advanced-search-open');
    closeButton.focus();
    return false;
  };

  // Existing pages call advSearch(); redesigned pages use the same dialog via links.
  window.advSearch = window.openAdvancedSearch;
  document.addEventListener('click', function (event) {
    var trigger = event.target.closest('[data-advanced-search]');
    if (!trigger) return;
    event.preventDefault();
    window.openAdvancedSearch(trigger);
  });
})();
