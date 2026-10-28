(function () {
  'use strict';
  var data=window.frontendI18nData || {language:'zh',messages:{}};
  function normalize(value) { return String(value).replace(/[\s\u00a0\u3000]+/g,''); }
  function text(source,parameters) {
    var value=Object.prototype.hasOwnProperty.call(data.messages,normalize(source))?data.messages[normalize(source)]:source;
    if (normalize(value)===normalize(source)) value=source;
    return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){
      return parameters && Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;
    });
  }
  function escape(value) { return value.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;').replace(/'/g,'&#39;'); }
  function translated(source) {
    var decoder=document.createElement('textarea'); decoder.innerHTML=source;
    var key=normalize(decoder.value);
    // Leave unchanged entities and split HTML fragments exactly as authored.
    return Object.prototype.hasOwnProperty.call(data.messages,key)&&normalize(data.messages[key])!==key?escape(String(data.messages[key])):source;
  }
  // Called only on developer-authored HTML fragments, never resource/annotation data.
  function markup(value) {
    return value.replace(/(^|>)([^<>]*)(?=<|$)/g,function(match,prefix,source){return prefix+translated(source);})
      .replace(/\b(title|aria-label|placeholder|alt)=(['"])(.*?)\2/g,function(match,name,quote,source){return name+'='+quote+translated(source)+quote;});
  }
  window.PlatformI18n={language:data.language,t:text,html:markup};
  window.platformText=text;
  window.platformMarkup=markup;
  document.documentElement.lang=data.language==='zw'?'bo':'zh-CN';
  function renderTemplates() {
    document.querySelectorAll('[data-ui-template]').forEach(function(element){
      var slots={}; element.querySelectorAll('[data-ui-slot]').forEach(function(slot){slots[slot.dataset.uiSlot]=slot;});
      var parts=text(element.dataset.uiTemplate).split(/(\{[a-zA-Z0-9_]+\})/g), fragment=document.createDocumentFragment();
      parts.forEach(function(part){fragment.appendChild(slots[part.slice(1,-1)]||document.createTextNode(part));});
      element.replaceChildren(fragment);
    });
  }
  if(document.readyState==='loading') document.addEventListener('DOMContentLoaded',renderTemplates); else renderTemplates();
})();
