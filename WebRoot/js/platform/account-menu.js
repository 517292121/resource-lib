(function () {
  'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

  function ready() {
    document.querySelectorAll('[data-account-menu]').forEach(function (account) {
      if (account.dataset.accountReady) return;
      account.dataset.accountReady = 'true';
      var trigger = account.querySelector('[data-account-trigger]');
      var dropdown = account.querySelector('[data-account-dropdown]');
      var logout = account.querySelector('[data-account-logout]');
      var error = account.querySelector('[data-account-error]');
      function show(open) {
        dropdown.hidden = !open;
        trigger.setAttribute('aria-expanded', String(open));
      }
      account.addEventListener('mouseenter', function () { show(true); });
      account.addEventListener('mouseleave', function () {
        if (!account.contains(document.activeElement)) show(false);
      });
      account.addEventListener('focusin', function () { show(true); });
      account.addEventListener('focusout', function (event) {
        if (!account.contains(event.relatedTarget)) show(false);
      });
      account.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') {
          event.preventDefault(); trigger.focus(); show(false);
        } else if (event.key === 'ArrowDown' && event.target === trigger) {
          event.preventDefault(); show(true); logout.focus();
        }
      });
      document.addEventListener('click', function (event) {
        if (!account.contains(event.target)) show(false);
      });
      logout.addEventListener('click', function () {
        if (logout.disabled || !window.confirm(uiText('确定要退出登录吗？'))) return;
        var label = logout.textContent;
        logout.disabled = true;
        logout.textContent = uiText('正在退出…');
        error.hidden = true;
        fetch(account.dataset.accountBase + 'member/exit?t=' + Date.now(), {
          method: 'GET', credentials: 'same-origin', cache: 'no-store'
        }).then(function (response) {
          if (!response.ok) throw new Error(uiText('退出失败，请重试'));
          return response.json();
        }).then(function (data) {
          if (!data || (data.success !== true && data.success !== 'true')) {
            throw new Error(data && data.msg ? data.msg : uiText('退出失败，请重试'));
          }
          window.location.reload();
        }).catch(function () {
          logout.disabled = false;
          logout.textContent = label;
          error.textContent = uiText('退出失败，请检查网络后重试');
          error.hidden = false;
          show(true);
        });
      });
    });
  }
  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', ready);
  else ready();
})();
