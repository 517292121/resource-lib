Ext.onReady(function() {
	Ext.QuickTips.init();//初始化信息提示功能
	
	Ext.apply(Ext.form.VTypes,{
	   "va":function(_v){
	       if(/^\d+$/.test(_v)){
	         
	             return true;
	       }
	       return false;
	   },
	   "vaText":"验证码只能是数字",
	   "vaMask":/[0-9]/i
	},{
	    "isChinese":function isChinese(name)  //中文值检测
			{  
			if(name.length  ==  0)
			return  false;
			for(i  =  0;  i  <=  name.length;  i++)  {  
			if(name.charCodeAt(i)  <  128)
			return  true;
			}
			return  false;
			}
	});
	
	
	var _loginForm=new Ext.form.FormPanel({
			bodyStyle : 'padding:10 0 0 20',
			magginBottom:10,
			height:110,
			labelAlign : 'center',
			labelWidth : 60,
			labelPad : 0,
			border:false,
			frame:true,
			buttonAlign:'right',
			defaults : {
					xtype:'textfield',
					width:200,
					border:false,
					allowBlank:false,
					msgTarget:'qtip',
					cls:'text_css'
			},
			items : [{
						name : 'userName',
						fieldLabel : '帐&nbsp; &nbsp;号',
						blankText : '帐号不能为空',
						maxLength:20,
						maxLengthText:'用户名最多20个字符！',
						vtype:"isChinese",
						vtypeText:'用户名不能输入中文',
						value:'root'
					},{
						name : 'userPassword',
						id:'pwd',
						inputType : 'password',
						fieldLabel : '密&nbsp; &nbsp;码',
						blankText : '密码不能为空',
						maxLength:20,
						maxLengthText:'密码最多20个字符！',
						minLength:1,
						minLengthText:'密码不能少于1位！',
						value:'root'
					}
			]
		});
	

		var _keyEnter=new Ext.KeyNav(document,{
												'enter':login
											});
		_keyEnter.enable();
		
		function login(){
			if(_loginForm.form.isValid()){
				var pwdField = _loginForm.form.findField('userPassword');
				var pwd = pwdField.getValue();
				alert(pwd);
				var tempPwd ='';//将填充值设置跟密码一样长
				for(var i=0;i<pwd.length;i++)
					tempPwd +='*';
				pwdField.setValue(tempPwd); //改变userPassword的值，避免明码传递密码。
				_loginForm.form.submit({
						waitTitle : '请稍候',
						waitMsg : '正在登录.......',	
						params:{
							pwd:hex_sha1(pwd)							
						},
						url : getPath()+'login.htm',
						method : 'POST',
						scope : this,
						success : function(_loginForm, action) {
							Ext.getCmp("LoginWin").destroy();
							window.location.href =getPath()+"console/index.htm";
						},
						failure : function(_loginForm, action) {
							Ext.MessageBox.alert('警告', action.result.errors);
								_loginForm.findField("userPassword").setRawValue('');
						}
				});
			}
		}
		
		function _closeWindow(){
			Ext.Msg.confirm("提示", "确认要退出系统吗？\n", function(btn) {
								if (btn == "yes")
									window.close();
							});
		}
	
		var LoginWin =new Ext.Window({
			id:'LoginWin',
			title : '登录窗口',
			width :380,
			height : 210,
			closable : false,
			resizable:false,
			collapsible : true,
			border:false,
			items:[_loginForm,
					{
					title:"用户登录",
					id:'tab_first',
					frame:true,
					autoWidth:true,
					autoHeight:true,
					items:[_loginForm],
					buttons:[{
							text:'后台登录',
							handler:login
						},{
							text:'关闭窗体',
							handler:_closeWindow
						}]
				}]
		});
		
		LoginWin.show();
		
});