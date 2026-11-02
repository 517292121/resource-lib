(function () {
    'use strict';
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};

    var book = document.getElementById('reader-book');
    if (book && book.dataset.previewOnly === 'true') {
        return;
    }
    var manager = document.getElementById('annotation-records');
    if (!book && !manager) return;
    var base = (book || manager).dataset.base;
    var endpoint = book ? base + 'res/' + encodeURIComponent(book.dataset.resourceCode) + '/annotations' : manager.dataset.endpoint;
    var list = document.getElementById('annotation-list');
    var status = document.getElementById('annotation-status');
    var editor = document.getElementById('annotation-editor');
    var form = document.getElementById('annotation-editor-form');
    var content = document.getElementById('annotation-content');
    var errorBox = document.getElementById('annotation-editor-error');
    var saveButton = document.getElementById('annotation-save');
    var message = document.getElementById('annotation-message');
    var canWrite = false, csrfToken = '', rows = [], page = null, selected = null, editing = null;
    var saving = false, loading = 0, activeId = '', annotationsLoaded = false, recordsPage = 1, recordsPages = 1, activeLeaf = 0;
    var tools = document.getElementById('annotation-tools');
    var readerToolbar = document.querySelector('.reader-toolbar');
    var myAnnotations = document.getElementById('reader-my-annotations');
    var selectionButton = document.getElementById('annotation-selection');
    var pageButton = document.getElementById('annotation-page');
    var reviewDialog=document.getElementById('annotation-review'), reviewing=null, reviewSaving=false;
    var markerLayer;
    if (book) { markerLayer = node('div','annotation-markers'); book.appendChild(markerLayer); }

    function node(tag, className, text) {
        var element = document.createElement(tag);
        if (className) element.className = className;
        if (text !== undefined) element.textContent = text;
        return element;
    }
    function button(text, action, className) {
        var element = node('button', className, text); element.type = 'button'; element.addEventListener('click', action); return element;
    }
    async function request(url, data) {
        var options = {credentials:'same-origin', headers:{'Accept':'application/json'}};
        if (data) { options.method='POST'; options.headers['Content-Type']='application/json;charset=UTF-8'; options.headers['X-Annotation-CSRF']=csrfToken; options.body=JSON.stringify(data); }
        var response = await fetch(url,options), result;
        try { result = await response.json(); } catch (ignore) { throw new Error(uiText('批注服务暂时不可用，请刷新页面后重试。')); }
        if (!response.ok || !result.success) throw new Error(uiText(result.message || uiText('批注操作失败，请稍后重试。')));
        return result;
    }
    function notify(text, confirm) {
        return new Promise(function (resolve) {
            document.getElementById('annotation-message-text').textContent=uiText(text);
            document.getElementById('annotation-message-title').textContent=confirm ? uiText('删除批注') : uiText('提示');
            var cancel=document.getElementById('annotation-message-cancel'), ok=document.getElementById('annotation-message-ok');
            cancel.hidden=!confirm; ok.textContent=confirm ? uiText('确定删除') : uiText('确定');
            var finish=function(value){message.close();ok.onclick=cancel.onclick=null;message.oncancel=null;resolve(value);};
            ok.onclick=function(){finish(true);};cancel.onclick=function(){finish(false);};message.oncancel=function(event){event.preventDefault();finish(false);};
            message.showModal();
        });
    }
    function mutationUrl(row) {
        return book ? endpoint + '/' + encodeURIComponent(row.id) : (manager.dataset.admin==='true'
            ? base+'console/annotations/'+encodeURIComponent(row.id)
            : base+'res/'+encodeURIComponent(row.resourceCode)+'/annotations/'+encodeURIComponent(row.id));
    }
    function openEditor(row, anchor) {
        if (saving) return;
        editing=row ? {row:row} : {anchor:anchor};
        content.value=row ? row.content : '';
        document.getElementById('annotation-editor-title').textContent=row ? uiText('编辑批注') : (anchor.scope==='page' ? uiText('整页批注') : uiText('选文批注'));
        document.getElementById('annotation-editor-location').textContent=(row ? row.chapter : anchor.chapter) + (row ? '' : uiText(' · 全书第 {page} 页', {page:page.page}));
        document.getElementById('annotation-editor-quote').textContent=row ? row.quote : anchor.quote;
        errorBox.textContent=''; count(); editor.showModal(); content.focus();
    }
    function closeEditor() { if (!saving) { editor.close(); editing=null; } }
    document.querySelectorAll('[data-close-annotation]').forEach(function (element) { element.addEventListener('click',closeEditor); });
    editor.addEventListener('cancel',function(event){ if(saving) event.preventDefault(); else editing=null; });
    function count() { document.getElementById('annotation-character-count').textContent=content.value.length; }
    content.addEventListener('input',count);
    form.addEventListener('submit',async function(event){
        event.preventDefault(); if(saving||!editing) return;
        if (!content.value.trim()) {errorBox.textContent=uiText('请填写批注内容。'); content.focus();return;}
        saving=true; saveButton.disabled=true; saveButton.textContent=uiText('保存中…'); errorBox.textContent='';
        try {
            var data=editing.row ? {version:editing.row.version,content:content.value} : Object.assign({},editing.anchor,{content:content.value});
            var result=await request(editing.row ? mutationUrl(editing.row) : endpoint,data);
            editor.close(); editing=null; selected=null; if(selectionButton) selectionButton.disabled=true;
            await load(); await notify(result.message);
        } catch(error) { errorBox.textContent=error.message; }
        finally {saving=false;saveButton.disabled=false;saveButton.textContent=uiText('保存批注');}
    });
    async function remove(row) {
        if (!await notify(uiText('确定删除这条批注吗？删除后无法恢复。'),true)) return;
        try {var result=await request(mutationUrl(row)+'/delete',{version:row.version}); await load(); await notify(result.message);}
        catch(error){await notify(error.message);}
    }
    function actions(row, parent) {
        if (!row.editable) return;
        var wrap=node('div','annotation-row-actions');
        wrap.appendChild(button(uiText('编辑'),function(){openEditor(row);}));
        wrap.appendChild(button(uiText('删除'),function(){remove(row);},'annotation-delete'));
        parent.appendChild(wrap);
    }
    function reviewActions(row,parent) {
        if(!manager || manager.dataset.admin!=='true' || !reviewDialog)return;
        var wrap=node('div','annotation-review-actions');
        if(row.editable)wrap.appendChild(button(uiText('删除'),function(){remove(row);},'annotation-delete'));
        function open(state) {
            if(reviewSaving||saving)return;
            reviewing={row:row,status:state};
            document.getElementById('annotation-review-title').textContent=state==='APPROVED'?uiText('审核通过'):uiText('审核不通过');
            document.getElementById('annotation-review-summary').textContent=row.resourceTitle+' · '+row.chapter+' · '+row.author;
            document.getElementById('annotation-review-remark').value='';document.getElementById('annotation-review-error').textContent='';reviewDialog.showModal();
        }
        if(row.auditStatus!=='APPROVED')wrap.appendChild(button(uiText('审核通过'),function(){open('APPROVED');},'annotation-review-approve'));
        if(row.auditStatus!=='REJECTED')wrap.appendChild(button(uiText('审核不通过'),function(){open('REJECTED');},'annotation-review-reject'));
        parent.appendChild(wrap);
    }
    if(reviewDialog) {
        document.querySelectorAll('[data-close-review]').forEach(function(element){element.addEventListener('click',function(){if(!reviewSaving){reviewDialog.close();reviewing=null;}});});
        reviewDialog.addEventListener('cancel',function(event){if(reviewSaving)event.preventDefault();else reviewing=null;});
        document.getElementById('annotation-review-form').addEventListener('submit',async function(event){
            event.preventDefault();if(!reviewing||reviewSaving)return;
            reviewSaving=true;var submit=document.getElementById('annotation-review-submit');submit.disabled=true;
            try {
                var result=await request(mutationUrl(reviewing.row)+'/review',{version:reviewing.row.version,auditStatus:reviewing.status,remark:document.getElementById('annotation-review-remark').value});
                reviewDialog.close();reviewing=null;await load();await notify(result.message);
            } catch(error){document.getElementById('annotation-review-error').textContent=error.message;}
            finally{reviewSaving=false;submit.disabled=false;}
        });
    }
    function pageLeaves() {
        if (!page) return [];
        return page.leaves || [
            {chapter:page.chapter,title:page.title,text:page.text,textHash:page.textHash,start:page.leftStart,end:page.rightStart},
            {chapter:page.chapter,title:page.title,text:page.text,textHash:page.textHash,start:page.rightStart,end:page.end}
        ];
    }
    function matchesLeaf(row, leaf) {
        return row.chapter===leaf.chapter && row.textHash===leaf.textHash && row.end>leaf.start && row.start<leaf.end;
    }
    function isVisible(row) {
        return pageLeaves().some(function(leaf){return matchesLeaf(row,leaf);});
    }
    function pageAnchor() {
        var current=pageLeaves()[activeLeaf];
        if(!current||current.end<=current.start)return null;
        var start=current.start,end=current.end;
        return {chapter:current.chapter,textHash:current.textHash,start:start,end:end,quote:current.text.slice(start,end),scope:'page'};
    }
    function updatePageButton() {
        var anchor=pageAnchor(),locations=pageLeaves();pageButton.disabled=!canWrite||!anchor;
        [document.getElementById('reader-left'),document.getElementById('reader-right')].forEach(function(leaf,index){
            var selectable=canWrite&&locations[index]&&locations[index].end>locations[index].start;
            var current=!!selectable&&index===activeLeaf;
            leaf.classList.toggle('is-annotation-page-selected',current);
            if(selectable)leaf.setAttribute('tabindex','0');else leaf.removeAttribute('tabindex');
            if(current)leaf.setAttribute('aria-current','page');else leaf.removeAttribute('aria-current');
        });
    }
    function activate(row, navigate) {
        activeId=row.id;
        if (!navigate) {
            if (!row.stale) book.dispatchEvent(new CustomEvent('reader:jump',{detail:{chapter:row.chapter,offset:row.start}}));
            book.dispatchEvent(new CustomEvent('reader:showannotations'));
        }
        if (navigate && row.stale) { notify(uiText('该批注对应的原文已修改或章节已移除，暂时无法准确定位。')); return; }
        if (navigate && !pageLeaves().some(function(leaf){return matchesLeaf(row,leaf)&&row.start>=leaf.start&&row.start<leaf.end;})) {
            book.dispatchEvent(new CustomEvent('reader:jump',{detail:{chapter:row.chapter,offset:row.start}}));
        }
        var selectedPage=pageLeaves().findIndex(function(leaf){return matchesLeaf(row,leaf)&&row.start>=leaf.start&&row.start<leaf.end;});
        if(selectedPage>=0){activeLeaf=selectedPage;updatePageButton();}
        document.querySelectorAll('[data-annotation-id]').forEach(function(element){element.classList.toggle('is-active',element.dataset.annotationId===row.id);});
        var card=list.querySelector('[data-annotation-id="'+row.id+'"]');
        var panel=document.querySelector('.reader-catalog');
        if(navigate && panel && panel.getBoundingClientRect().top>=book.getBoundingClientRect().bottom-2) {
            book.scrollIntoView({block:'start',behavior:'smooth'});
        } else if(card) card.scrollIntoView({block:'nearest',behavior:'smooth'});
    }
    function readerList() {
        list.setAttribute('aria-label',uiText('当前文献全部批注'));
        list.replaceChildren();
        var controls=node('div','annotation-list-tools');
        controls.append(node('span','annotation-count',uiText('共 {count} 条批注', {count:rows.length})),button(uiText('刷新'),load,'annotation-refresh'));list.appendChild(controls);
        if(!rows.length) list.appendChild(node('p','annotation-empty',uiText('本文献暂无已审核批注')));
        rows.forEach(function(row){
            var card=node('article','annotation-card'); card.dataset.annotationId=row.id; card.classList.toggle('is-active',row.id===activeId);
            card.addEventListener('click',function(event){
                if(event.target.closest('button,a') || !window.getSelection().isCollapsed) return;
                activate(row,true);
            });
            var heading=button('',function(){activate(row,true);},'annotation-card-heading');
            heading.title=uiText('定位批注原文：')+row.chapter;
            heading.append(node('span','annotation-number',String(row.number)),node('span','annotation-author',row.author));
            card.appendChild(heading);
            var meta=node('p','annotation-meta',row.chapter+' · '+row.updatedAt+' · '+(row.scope==='page'?uiText('整页批注'):uiText('选文批注'))+(row.stale?uiText(' · 原文已变化'):''));
            var quote=node('blockquote','annotation-quote',row.quote);
            quote.title=row.quote;
            card.append(meta,quote,node('p','annotation-body',row.content));actions(row,card);list.appendChild(card);
        });
    }
    function paintLeaf(leaf, start, text, annotations) {
        var points=[start,start+text.length];
        annotations.forEach(function(row){points.push(Math.max(start,row.start),Math.min(start+text.length,row.end));});
        points=points.filter(function(value){return value>=start && value<=start+text.length;}).sort(function(a,b){return a-b;});
        var fragment=document.createDocumentFragment();
        for(var i=0;i<points.length-1;i++){
            if(points[i]===points[i+1]) continue;
            var ids=annotations.filter(function(row){return row.start<points[i+1]&&row.end>points[i];});
            var part=text.slice(points[i]-start,points[i+1]-start);
            if(ids.length){var mark=node('mark','annotation-highlight',part);mark.dataset.annotationIds=ids.map(function(row){return row.id;}).join(' ');fragment.appendChild(mark);}
            else fragment.appendChild(document.createTextNode(part));
        }
        leaf.replaceChildren(fragment);
    }
    function textRange(leaf, start, end) {
        var walker=document.createTreeWalker(leaf,NodeFilter.SHOW_TEXT), textNode, offset=0, range=document.createRange(), found=false;
        while((textNode=walker.nextNode())) {
            var next=offset+textNode.length;
            if(!found && start<=next){range.setStart(textNode,Math.max(0,start-offset));found=true;}
            if(found && end<=next){range.setEnd(textNode,Math.max(0,end-offset));return range;}
            offset=next;
        }
        return null;
    }
    function paint() {
        if(!book||!page) return;
        var visible=rows.filter(isVisible), left=document.getElementById('reader-left'),right=document.getElementById('reader-right'), locations=pageLeaves(), leaves=[left,right];
        leaves.forEach(function(leaf,index){
            var location=locations[index];
            if(!location){leaf.replaceChildren();return;}
            paintLeaf(leaf,location.start,location.text.slice(location.start,location.end),visible.filter(function(row){return matchesLeaf(row,location);}));
        });
        if(locations[0]&&!locations[0].text) left.textContent=uiText('本章节暂无正文。');
        if(book.paintSearchHighlights) book.paintSearchHighlights();
        markerLayer.replaceChildren();
        var bounds=book.getBoundingClientRect(), positions=[];
        visible.forEach(function(row){
            var index=locations.findIndex(function(location){return matchesLeaf(row,location);});
            var location=locations[index],leaf=leaves[index],position=Math.max(location.start,row.start),leafStart=location.start;
            var range=textRange(leaf,position-leafStart,Math.min(position+1,location.end)-leafStart);
            if(!range) return;
            var rect=range.getBoundingClientRect(), leafRect=leaf.getBoundingClientRect();
            if(rect.height===0) return;
            var overlaps=positions.filter(function(p){return p.leaf===leaf && Math.abs(p.top-rect.top)<12;}).length;
            positions.push({leaf:leaf,top:rect.top});
            var badge=button(String(row.number),function(){activate(row,false);},'annotation-marker');
            badge.dataset.annotationId=row.id; badge.setAttribute('aria-label',uiText('查看批注 {number}', {number:row.number}));
            badge.style.left=(leafRect.left-bounds.left-23-(overlaps%2)*20)+'px';
            badge.style.top=Math.min(leafRect.bottom-bounds.top-18,rect.top-bounds.top+3+Math.floor(overlaps/2)*20)+'px';
            badge.classList.toggle('is-active',row.id===activeId);markerLayer.appendChild(badge);
        });
    }
    function captureSelection() {
        if(!canWrite || !page || book.hasAttribute('aria-busy') || editor.open) return;
        var selection=window.getSelection();
        if(!selection || selection.isCollapsed || !selection.rangeCount) {selected=null;selectionButton.disabled=true;return;}
        var range=selection.getRangeAt(0), leaves=[document.getElementById('reader-left'),document.getElementById('reader-right')], locations=pageLeaves();
        function absolute(container,offset){
            for(var i=0;i<leaves.length;i++) if(leaves[i].contains(container)||leaves[i]===container){
                var prefix=document.createRange();prefix.selectNodeContents(leaves[i]);prefix.setEnd(container,offset);
                return locations[i] ? {location:locations[i],offset:locations[i].start+prefix.toString().length} : null;
            }
            return null;
        }
        var start=absolute(range.startContainer,range.startOffset),end=absolute(range.endContainer,range.endOffset);
        if(start===null||end===null||start.location.chapter!==end.location.chapter||start.location.textHash!==end.location.textHash||end.offset<=start.offset||!start.location.text.slice(start.offset,end.offset).trim()){selected=null;selectionButton.disabled=true;return;}
        selected={chapter:start.location.chapter,textHash:start.location.textHash,start:start.offset,end:end.offset,quote:start.location.text.slice(start.offset,end.offset),scope:'selection'};
        selectionButton.disabled=false;
    }
    function recordsList(data) {
        list.replaceChildren();recordsPage=data.page;recordsPages=data.pages;
        document.getElementById('annotation-record-count').textContent=uiText('共 {count} 条批注', {count:data.total});
        document.getElementById('annotation-record-page').textContent=data.page+' / '+data.pages;
        document.getElementById('annotation-record-prev').disabled=data.page<=1;
        document.getElementById('annotation-record-next').disabled=data.page>=data.pages;
        if(!rows.length) list.appendChild(node('p','annotation-empty',uiText('暂无批注记录')));
        rows.forEach(function(row){
            var card=node('article','annotation-record');
            var header=node('div','annotation-record-heading');
            var heading=node('h2');var link=node('a','',row.resourceTitle);
            var url=new URL(base+'res/browse/'+encodeURIComponent(row.resourceCode),window.location.origin);
            url.searchParams.set('p',row.chapter);url.searchParams.set('offset',row.start);url.hash='annotation='+row.id;
            link.href=url.toString();heading.appendChild(link);header.appendChild(heading);
            var auditStatus=row.auditStatus||'PENDING';
            var audit=node('div','annotation-review-status');
            audit.appendChild(node('span','annotation-audit-badge is-'+auditStatus.toLowerCase(),uiText(row.auditStatusLabel||uiText('待审核'))));
            if(row.auditedAt)audit.appendChild(node('span','annotation-audit-time',uiText('审核时间：')+row.auditedAt));
            header.append(audit,node('p','annotation-meta',row.chapter+' · '+(row.scope==='page'?uiText('整页批注'):uiText('选文批注'))+' · '+row.author+' · '+row.updatedAt));
            card.append(header,node('blockquote','annotation-record-quote',row.quote),node('p','annotation-body',row.content));
            if(row.auditRemark)card.appendChild(node('p','annotation-review-remark',uiText('审核说明：')+row.auditRemark));
            if(manager.dataset.admin==='true') {
                reviewActions(row,card);
            } else {
                actions(row,card);var view=node('a','annotation-view',uiText('查看原文'));view.href=link.href;card.appendChild(view);
            }
            list.appendChild(card);
        });
    }
    async function load() {
        if(book && !page) return;
        var token=++loading;status.textContent='';
        try {
            var url=endpoint;
            if(!book) {
                url+='/data?page='+recordsPage+'&q='+encodeURIComponent(document.getElementById('annotation-record-search').value.trim());
                var filter=document.getElementById('annotation-audit-filter');
                if(filter)url+='&auditStatus='+encodeURIComponent(filter.value);
            }
            var data=await request(url);
            if(token!==loading) return;
            rows=book ? data.annotations : data.records;canWrite=data.canWrite;csrfToken=data.csrfToken||'';
            if(book) {
                annotationsLoaded=true;
                var authenticated=data.authenticated===true;
                canWrite=authenticated&&canWrite;
                tools.hidden=!canWrite;updatePageButton();
                if(readerToolbar)readerToolbar.classList.toggle('has-annotation-tools',canWrite);
                if(myAnnotations)myAnnotations.hidden=!canWrite;
                readerList();paint();
                var wanted=new URLSearchParams(window.location.hash.slice(1)).get('annotation');
                if(wanted){var row=rows.find(function(item){return item.id===wanted;});if(row)activate(row,false);}
            } else recordsList(data);
        } catch(error) {
            if(token!==loading) return;status.textContent=error.message;
            if(book){annotationsLoaded=false;canWrite=false;tools.hidden=true;updatePageButton();if(readerToolbar)readerToolbar.classList.remove('has-annotation-tools');if(myAnnotations)myAnnotations.hidden=true;rows=[];list.replaceChildren(button(uiText('重新加载'),load));paint();}
        }
    }
    if(book) {
        book.addEventListener('reader:pagechange',function(event){page=event.detail;activeLeaf=pageLeaves().findIndex(function(leaf){return leaf.chapter===page.chapter&&leaf.end>leaf.start;});if(activeLeaf<0)activeLeaf=Math.max(0,pageLeaves().findIndex(function(leaf){return leaf.end>leaf.start;}));selected=null;selectionButton.disabled=true;updatePageButton();paint();if(!annotationsLoaded)load();});
        function selectPage(event){
            if(!canWrite||!page||editor.open||(event.type==='mousedown'&&event.button!==0))return;
            [document.getElementById('reader-left'),document.getElementById('reader-right')].forEach(function(leaf,index){if(leaf.contains(event.target)){activeLeaf=index;updatePageButton();}});
        }
        book.addEventListener('mousedown',selectPage);
        book.addEventListener('touchstart',selectPage,{passive:true});
        book.addEventListener('focusin',selectPage);
        book.addEventListener('mouseup',captureSelection);
        book.addEventListener('keyup',captureSelection);
        document.addEventListener('selectionchange',function(){if(!editor.open && selectionButton && document.activeElement!==selectionButton) captureSelection();});
        selectionButton.addEventListener('mousedown',function(event){event.preventDefault();});
        selectionButton.addEventListener('click',function(){if(selected)openEditor(null,Object.assign({},selected));});
        pageButton.addEventListener('click',function(){var anchor=pageAnchor();if(anchor&&anchor.end>anchor.start)openEditor(null,anchor);});
        if(book.readerPage){page=book.readerPage;load();}
    } else {
        document.getElementById('annotation-search-form').addEventListener('submit',function(event){event.preventDefault();recordsPage=1;load();});
        var auditFilter=document.getElementById('annotation-audit-filter');
        if(auditFilter)auditFilter.addEventListener('change',function(){recordsPage=1;load();});
        document.getElementById('annotation-record-prev').addEventListener('click',function(){if(recordsPage>1){recordsPage--;load();}});
        document.getElementById('annotation-record-next').addEventListener('click',function(){if(recordsPage<recordsPages){recordsPage++;load();}});
        load();
    }
}());
