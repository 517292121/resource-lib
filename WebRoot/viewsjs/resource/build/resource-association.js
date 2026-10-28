(function () {
  'use strict';
  function init() {
    document.querySelectorAll('[data-resource-selector]').forEach(function (root,index) {
      if(root.dataset.selectorInitialized==='true')return;root.dataset.selectorInitialized='true';
      var input=root.querySelector('input[type=search]'),popup=root.querySelector('.resource-selector-popup');
      var hidden=root.querySelector('input[name=relatedResourceIds],input[name=rmin_id]'),selected=root.querySelector('.resource-selector-selected');
      var multiple=root.dataset.multiple==='true',items=new Map(),timer,request,sequence=0;
      var url=root.dataset.url,id='resource-options-'+index;
      popup.id=id;input.setAttribute('role','combobox');input.setAttribute('aria-controls',id);input.setAttribute('aria-expanded','false');input.setAttribute('aria-autocomplete','list');
      function close() { popup.hidden=true;input.setAttribute('aria-expanded','false'); }
      function open() { popup.hidden=false;input.setAttribute('aria-expanded','true'); }
      function metadata() {
        if(!multiple) return;
        var row=document.querySelector('[data-research-relation="true"]');
        var field=row && row.querySelector('textarea,select,input');
        if(field) field.value=Array.from(items.values()).join('；');
      }
      function render(changed) {
        hidden.value=Array.from(items.keys()).join(',');
        if(multiple) {
          selected.replaceChildren();
          items.forEach(function(title,id) {
            var tag=document.createElement('span'),remove=document.createElement('button');tag.className='resource-selector-tag';
            tag.appendChild(document.createTextNode(title));remove.type='button';remove.textContent='×';remove.setAttribute('aria-label','取消关联 '+title);
            remove.addEventListener('click',function(){items.delete(id);render(true);});tag.appendChild(remove);selected.appendChild(tag);
          });
          if(changed) metadata();
        }
        hidden.dispatchEvent(new Event('change',{bubbles:true}));
      }
      root.querySelectorAll('[data-selected-id]').forEach(function(node){items.set(node.dataset.selectedId,node.textContent);});
      if(multiple) render(false);
      function message(value) { var node=document.createElement('div');node.className='resource-selector-message';node.textContent=value;popup.appendChild(node); }
      function load(page,append) {
        clearTimeout(timer);if(request) request.abort();request=new AbortController();var current=++sequence;
        if(!append) popup.replaceChildren();open();
        if(!root.dataset.type && root.dataset.option==='mission') { message('请先选择资源类型');return; }
        if(!append) message('正在加载…');
        var query=new URLSearchParams({q:input.value.trim(),pn:String(page),option:root.dataset.option||'resource',self:root.dataset.self||'0',type:root.dataset.type||'0'});
        fetch(url+'&'+query,{credentials:'same-origin',signal:request.signal}).then(function(response){if(!response.ok) throw new Error();return response.json();})
          .then(function(data){
            if(current!==sequence) return;if(!data.success) throw new Error();
            if(!append) popup.replaceChildren();
            if(!data.items.length && !append) message('没有匹配项');
            data.items.forEach(function(item){
              if(items.has(String(item.id)) && multiple) return;
              var button=document.createElement('button'),description=document.createElement('small');button.type='button';button.className='resource-selector-option';
              button.textContent=item.title||'未命名资源';description.textContent=item.description||'';button.appendChild(description);
              button.addEventListener('click',function(){
                if(!multiple) items.clear();
                if(multiple && items.size>=30) { window.alert('最多关联30条资源');return; }
                items.set(String(item.id),item.title||'未命名资源');render(true);input.value=multiple?'':item.title;input.focus();close();
              });popup.appendChild(button);
            });
            if(page+1<data.pages) { var next=document.createElement('button');next.type='button';next.className='resource-selector-option';next.textContent='加载更多';next.addEventListener('click',function(){next.remove();load(page+1,true);});popup.appendChild(next); }
          }).catch(function(error){if(current!==sequence || error.name==='AbortError')return;popup.replaceChildren();message('加载失败，请重新展开重试');});
      }
      input.addEventListener('input',function(){if(!multiple){items.clear();hidden.value='';hidden.dispatchEvent(new Event('change',{bubbles:true}));}clearTimeout(timer);if(request)request.abort();sequence++;timer=setTimeout(function(){load(0,false);},250);});
      input.addEventListener('focus',function(){if(popup.hidden)load(0,false);});
      root.querySelector('.resource-selector-toggle').addEventListener('click',function(){if(popup.hidden)load(0,false);else close();});
      root.addEventListener('keydown',function(event){
        if(event.key==='Escape'){close();input.focus();return;}
        if(event.key==='Enter' && event.target===input){event.preventDefault();load(0,false);return;}
        if(event.key==='ArrowDown' || event.key==='ArrowUp') {
          event.preventDefault();var buttons=Array.from(popup.querySelectorAll('button')),index=buttons.indexOf(document.activeElement);
          if(popup.hidden){load(0,false);return;}if(buttons.length)buttons[(index+(event.key==='ArrowDown'?1:-1)+buttons.length)%buttons.length].focus();
        }
      });
      document.addEventListener('click',function(event){if(!root.contains(event.target))close();});
      root.resetResourceSelector=function(){items.clear();input.value='';hidden.value='';if(request)request.abort();sequence++;close();render(false);};
    });
    var type=document.getElementById('maintenance-type'),mission=document.getElementById('maintenance-mission');
    if(type && mission){type.addEventListener('change',function(){mission.dataset.type=type.value;mission.resetResourceSelector();});}
    document.querySelectorAll('[data-research-relation="true"]').forEach(function(row){if(document.querySelector('[data-multiple="true"]'))row.style.display='none';});
  }
  if(document.readyState==='loading')document.addEventListener('DOMContentLoaded',init);else init();
})();
