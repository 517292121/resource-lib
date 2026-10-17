//编辑框状态
var isEditing = false;
//inputId
var isEditId="";
//TD对象
var isTd;
//inputValue
var isValue="";

//鼠标移开恢复
function mouseout(obj) {
	var tr = $(obj);
	if(!tr) return;
	tr.css("background","#ffffff");
}

//鼠标移动到上面变色
function mouseover(obj) {
	var tr = $(obj);
	if(!tr) return;
	tr.css("background","#FAFAFA"); 
}
/*****************事件***********************/
//单击TD变输入框修改说明
function EditRemarkOnclick(obj,resId){
	 if(isEditNULL(obj)){
		var Remarktd = $(obj);
		var Remarkval = $(obj).html();
		Remarktd.html("<input name=\"input"+resId+"\" id=\"input"+resId+"\" style=\"width: 100%;height: 21px;\" value=\""+Remarkval+"\" class=\"text\" maxLength='1500' onblur=\"SaveEditRemarkOnblur('"+resId+"');\">");
		isEditing=true;
		isEditId="input"+resId;
		isTd = obj;
		isValue=Remarkval;
		document.getElementById(isEditId).focus();
	}
}

//单击Index修改排序
function EditIndexOnclick(obj,resId){
	if(isEditNULL(obj)){
		var Remarktd = $(obj);
		var Remarkval = $(obj).html();
		Remarktd.html("<input name=\"index"+resId+"\" id=\"index"+resId+"\" style=\"width: 33px;height: 20px;\" value=\""+Remarkval+"\" class=\"text\" onblur=\"SaveEditIndexOnblur('"+resId+"'); \">");
		isEditing=true;
		isEditId="index"+resId;
		isTd = obj;
		isValue=Remarkval;
		document.getElementById(isEditId).focus();
	}
}

function addResFile(){
	winshow(); 
}
/**************确认********************/
//保存修改说明
function SaveEditRemarkOnblur(resFileId){
	var editTd = $(isTd);
	var editinput = $("#"+isEditId);
	var Remarkval = editinput.val();
	if(Remarkval.trim()!=isValue)
	SaveEditRemark(Remarkval,resFileId)
		editTd.html(Remarkval);
		clearEditNULL();
}

//保存修改排序
function SaveEditIndexOnblur(resFileId){
	var editTd = $(isTd);
	var editinput = $("#"+isEditId);
	var Indexval = editinput.val();
	if(isNaN(Indexval)){
		alert("请输入数字！");
		return false;
	}
	if(Indexval<1){
		alert("输入错误!");
		return false;
	}
	if(Indexval.trim()!=isValue)
	SaveEditIndex(Indexval,resFileId)
		editTd.html(Indexval);
		clearEditNULL();
}
//删除确认
function SaveEditDeleteResFile(resFileId){
	var con = confirm("是否确认删除该附件？");
	if(con)
	deleResfile(resFileId);
	else return false;
}

/**************方法********************/
//判断是否有修改未保存
function isEditNULL(obj){
	if(isTd===obj){
		return false;
	} 
	if(isEditing){
		alert("有编辑框未保存");
		document.getElementById(isEditId).focus();
		return false;
	}
	return true;
}

//清空修改状态
function clearEditNULL(){
	 //编辑框状态
	 isEditing = false;
	//inputId
	 isEditId="";
	//TD对象
	 isTd = null;
	//inputValue
	var isValue="";
}

//刷新
function refresh(){
     window.location.reload();  
}

//返回
function goBack(){
	window.location.href=""+basePath+"/console/data/inputSingle.htm?action=doResourceEdit&reqType=1&sid=" + resSFileId;
}

// 去除空格
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

/**********************************修改操作**************************/
//更改说明
function SaveEditRemark(remarkVal,resFileId){
			$.ajax({
			   type: "POST",
			   url: basePath+"/console/data/inputSingle.htm?action=updateResSourceFile",
			   data: "resFileId="+resFileId+"&resFileRemark="+remarkVal+"&uptype=1",
			   success: function(msg){ 
			   }
			}); 
}
//更改序号
function SaveEditIndex(index,resFileId){
			$.ajax({
			   type: "POST",
			   url: basePath+"/console/data/inputSingle.htm?action=updateResSourceFile",
			   data: "resFileId="+resFileId+"&index="+index+"&uptype=2",
			   success: function(msg){ 
			     refresh();  
			   }
			}); 
}
/**********************************删除操作**************************/
function deleResfile(resFileId){
			$.ajax({
			   type: "POST",
			   url: basePath+"/console/data/inputSingle.htm?action=deleteResSourceFile",
			   data: "resFileId="+resFileId,
			   success: function(msg){ 
			     refresh();
			   }
			});
}
/*****************新增附件***************************/
function SaveFile(){
	if(dataForm.form.isValid()) {
		RESSOURCEId.setValue(resSFileId);
		storehidden.setValue(storeId);
		rminidhidden.setValue(rminid);
		dataForm.getForm().submit({
			waitTitle:"数据提交", 
			waitMsg:"正在提交表单数据，请稍候...", 
			success: function(form, action){ 
				win.hide(); 
				Ext.Msg.alert('信息',action.result.msg,function(){
					refresh();
				});
			}, 
			failure:function(form, action){ 
				win.hide(); 
				Ext.Msg.alert('失败','添加失败：'+action.result.msg);
			} 
		});
	}
}

/*****************EXT弹出窗新增**************************/   
    		
    		var win; 
			function winshow(){ 
				dataForm.getForm().reset();  //将之前的清空 
				if(!win){ 
					win = new Ext.Window({ 
						title: '新增附件', 
						border : false,
						name: 'editwin', 
						resizable : false,
						width: 420, 
						closeAction: 'hide', 
						modal: true, 
						items: [dataForm] 
					}); 
				} 
				win.show(); 
			} 
			//资源ID
			var RESSOURCEId = new Ext.form.Hidden({ 
				name : "RESSOURCEId", 
				id: "RESSOURCEId" ,
				value :resSFileId
			}); 
			
			//所属库ID
			var storehidden = new Ext.form.Hidden({ 
				name : "storeId", 
				id: "storeId" ,
				value :storeId
			});
			
			//所属作业ID
			var rminidhidden = new Ext.form.Hidden({ 
				name : "rminid", 
				id: "rminid" ,
				value :rminid
			});
			
			///说明
			var remark =new Ext.form.TextArea({
				fieldLabel:'说明',
				name: 'remark', 
				inputType :'text',
				maxLength : 1500, 
				height: 75,
				width:280
			});
			
			var fileAccessType = new Ext.form.RadioGroup({
				id : 'fileAccessType',
				fieldLabel : '访问权限',
				name : 'accessType',
				anchor : '50%',
				items : [{
					boxLabel : '公开',
					name : 'accessType',
					inputValue : 'PUBLIC',
					checked : true
				}, {
					boxLabel : '不公开',
					name : 'accessType',
					inputValue : 'PRIVATE'
				}]
			});
			
			var dataForm = new Ext.form.FormPanel({
				height: 210, //定义表单窗体高度，根据实际情况何必.
				fileUpload:true,
				labelWidth: 80, 
				labelAlign: 'right', 
				buttonAlign: 'center', 
				url: url_save_form, 
				frame: true,  //如果设为false或不设则表单的背景为白色 
				bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
				items: [{
	                fieldLabel:'文件',
	                xtype:'textfield',
	                name:'resFile',
	                autoCreate:{
                    tag:'input',
                    type:'file',
                    size:'39'
                	}
				},
					remark,
					fileAccessType,
					RESSOURCEId,
					storehidden,
					rminidhidden
				], 
				buttons:[
					{ 
						text: '确&nbsp;定', 
						handler: function(){ 
							SaveFile(); 
						}
					},
					{ 
						text: '取&nbsp;消', 
						handler: function(){
							win.hide(); 
						} 
					}  
				] 
			}); 
