(function () {
  'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

  var tools = document.querySelector('[data-platform-base]');
  if (!tools) return;
  var base = tools.dataset.platformBase;
  document.documentElement.lang = tools.dataset.language === 'zw' ? 'bo' : 'zh-CN';
  function openMemberDialog(path, title) {
    var dialog = document.getElementById('platform-login-dialog');
    if (!dialog) {
      dialog = document.createElement('dialog');
      dialog.id = 'platform-login-dialog'; dialog.className = 'platform-login-dialog';
      dialog.setAttribute('aria-label', title);
      var close = document.createElement('button');
      close.type = 'button'; close.className = 'platform-login-close'; close.textContent = '×';
      close.setAttribute('aria-label', uiText('关闭'));
      close.addEventListener('click', function () { dialog.close(); });
      var frame = document.createElement('iframe');
      frame.title = title; frame.src = base + path;
      dialog.append(close, frame); document.body.appendChild(dialog);
      dialog.addEventListener('click', function (event) {
        var rect = dialog.getBoundingClientRect();
        if (event.target === dialog && (event.clientX < rect.left || event.clientX > rect.right || event.clientY < rect.top || event.clientY > rect.bottom)) dialog.close();
      });
    } else {
      dialog.setAttribute('aria-label', title);
      var frame = dialog.querySelector('iframe');
      if (frame) {
        frame.title = title;
        frame.style.height = '';
        frame.src = base + path;
      }
    }
    dialog.classList.toggle('platform-register-dialog', path === 'register');
    if (!dialog.open) dialog.showModal();
  }
  function openLogin() { openMemberDialog('member/login', uiText('会员登录')); }
  window.openLogin = openLogin;
  window.openPlatformLogin = openLogin;
  // The existing member form calls parent.openReg(), and reloads its parent after login.
  window.openReg = function () { openMemberDialog('register', uiText('用户注册')); };
  window.addEventListener('message', function (event) {
    if (event.origin !== window.location.origin || !event.data || event.data.type !== 'platform-member-resize') return;
    var dialog = document.getElementById('platform-login-dialog');
    var frame = dialog && dialog.querySelector('iframe');
    if (!dialog || !dialog.open || !frame || event.source !== frame.contentWindow) return;
    var maxHeight = Math.max(180, Math.min(window.innerHeight - 64, Math.floor(window.innerHeight * 0.92) - 34));
    frame.style.height = Math.min(Math.max(180, Number(event.data.height) || 0), maxHeight) + 'px';
  });
  var login = tools.querySelector('[data-platform-login]');
  if (login) login.addEventListener('click', openLogin);
  var language = tools.querySelector('[data-platform-language]');
  language.addEventListener('change', function () {
    language.disabled = true;
    var status = tools.querySelector('[role="status"]');
    status.textContent = uiText('正在切换语言…');
    // Set the existing locale session, then reload the original URL so search/filter/page state is preserved exactly.
    fetch(base + 'locale.htm?locale=' + encodeURIComponent(language.value) + '&from=' + encodeURIComponent(base + 'index'), { credentials: 'same-origin' })
      .then(function (response) { if (!response.ok) throw new Error('Locale failed'); window.location.reload(); })
      .catch(function () { language.disabled = false; status.classList.remove('platform-sr-only'); status.textContent = uiText('切换失败，请重试'); });
  });
})();
