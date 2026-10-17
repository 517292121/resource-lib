     // JScript 文件






//客户接待表单


//下拉选项数据源设置
var idhide = new Ext.form.Hidden({
       name : "id",
       id: "id"
});    
idhide.setValue("0");

//车型
var chexin_store = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({url: '../System/selectitem.aspx?sid=1'}),
    reader: new Ext.data.ArrayReader({},[
        {name: 'value'},
        {name: 'text'}
    ])
});

//维修类型
var wxlx_store = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({url: '../System/selectitem.aspx?sid=6'}),
    reader: new Ext.data.ArrayReader({},[
        {name: 'value'},
        {name: 'text'}
    ])
});

//维修方式
var wxfs_store = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({url: '../System/selectitem.aspx?sid=7'}),
    reader: new Ext.data.ArrayReader({},[
        {name: 'value'},
        {name: 'text'}
    ])
});


//客户单位
var company_store = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({url: '../system/selectjson.aspx?table=customer&value=id&text=companyname'}),
    reader: new Ext.data.ArrayReader({},[
        {name: 'value'},
        {name: 'text'}
    ])
});



//////表单元素

///车牌号
var cph = new Ext.form.TextField({
    fieldLabel: '车牌号',
    enableKeyEvents: true,
    name: 'cph',
    id: '_cph',
    allowBlank: false,
    blankText: '请输入车牌号',
    width: 120,
    msgTarget: 'side',
    listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[4].focus(true,true);
            } 
        }
    } 
});
cph.setValue("渝A");


///车型
var chexin = new Ext.form.ComboBox({
    fieldLabel: "车型",
    mode: 'remote',
    store: chexin_store,
    name: 'chexing',
   // allowBlank: false,
   // blankText: '请选择车型',
    width: 120,
    msgTarget: 'side',
    triggerAction: 'all',
    valueField: 'value',
    displayField: 'text',
    listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[7].focus(true,true);
            } 
        }
    }     
});

///所属单位
var company = new Ext.form.ComboBox({
    fieldLabel: "所属单位",
    mode: 'remote',
    name: '_ssdw',
   // allowBlank: false,
   // blankText: '请选择所属单位',
    width: 120,
    msgTarget: 'side',
    store: company_store,
    triggerAction: 'all',
    valueField: 'value',
    hiddenName: 'ssdw',
    displayField: 'text',
    listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[1].focus(true,true);
            } 
        }
    }     
});

///维修类型
var wxlx = new Ext.form.ComboBox({
    fieldLabel: "维修类型",
    mode: 'remote',
    store: wxlx_store,
    name: 'mend_id',
    //hiddenName: 'mend_id',
    width: 120,
    triggerAction: 'all',
    valueField: 'value',
    displayField: 'text',
    listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[5].focus(true,true);
            } 
        }
    }      
});

///交车日期
var jcrq = new Ext.form.DateField({
    fieldLabel: '进厂日期',
    name: 'bxrq',
    width: 120,
    format:'Y-m-d',
    listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[8].focus(true,true);
            } 
        }
    }      
});
jcrq.setValue(new Date());

var ccrq = new Ext.form.DateField({
    fieldLabel: '出厂日期',
    name: 'jcrq',
    width: 120,
    format:'Y-m-d'     
});
ccrq.setValue(new Date());

///维修里程
var wxlc = new Ext.form.TextField({
    fieldLabel: '维修里程',
    width: 120,
    name: 'wxlc',
    listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[10].focus(true,true);
            } 
        }
    } 
})

///工程师
var gcs = new Ext.form.TextField({
    fieldLabel: '工程师',
    width: 120,
    name: 'gcs',
    listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[2].focus(true,true);
            } 
        }
    } 
})

var sfgz = new Ext.form.Checkbox({
    fieldLabel: '是否挂账',
    name:'fin',
    value: '1'
})



///工时费
var gsf = new Ext.form.NumberField({    
    fieldLabel: '工时费',
    width: 120,
    name: 'gsf',
    listeners :{
        change : function(field,e){
           // var z =parseFloat(qtf.getValue() + clf.getValue() + wxf.getValue());
           // var o = parseFloat(this.getValue());
           // zje.setValue(z+o);
           SetMoneySum();
        },
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[6].focus(true,true);               
            } 
        }
    } 
})
gsf.setValue(0);


///材料费
var clf = new Ext.form.NumberField({
    fieldLabel: '材料费',
    width: 120,
    name: 'clf',
    listeners :{
        change : function(field,e){
           // var z =parseFloat(gsf.getValue() + qtf.getValue() + wxf.getValue());
           // var o = parseFloat(this.getValue());
           // zje.setValue(z+o);
           SetMoneySum();
        },
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[9].focus(true,true);               
            } 
        }
    } 
})
clf.setValue(0);

///外协费
var wxf = new Ext.form.NumberField({
    fieldLabel: '管理费',
    width: 120,
    name: 'wxf',
    listeners :{
        change : function(field,e){
          //  var z =parseFloat(gsf.getValue() + clf.getValue() + qtf.getValue());
          //  var o = parseFloat(this.getValue());
          //  zje.setValue(z+o);
          SetMoneySum();
        },
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[11].focus(true,true);               
            } 
        }
    } 
})
wxf.setValue(0);



///总金额
var zje = new Ext.form.NumberField({
    fieldLabel: '总金额',
    width: 120,
    name: 'zje',
    allowBlank: false,
    blankText: '总金额不能为空',
    msgTarget: 'side',
    listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[2].focus(true,true);
            } 
        }
    } 
})
zje.setValue(0);

///辅料费
var qtf = new Ext.form.NumberField({
    fieldLabel: '辅料费',
    width: 120,
    name: 'qtf',
    listeners :{ 
        change : function(field,e){
           // var z =parseFloat(gsf.getValue() + clf.getValue() + wxf.getValue());
           // var o = parseFloat(this.getValue());
           // zje.setValue(z+o);
           SetMoneySum();
        },
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[12].focus(true,true);               
            } 
        }       
    } 
})
qtf.setValue(0);

///备注
var remark = new Ext.form.TextField({
    fieldLabel: '备注',
    width: 120,
    name: 'remark',
     listeners :{
        specialKey :function(field,e){
            if (e.getKey() == Ext.EventObject.ENTER){
               form.findByType("textfield")[3].focus(true,true);
            } 
        }
    }
})


var formset = new Ext.form.FieldSet({
      autoHeidht:true,
      
      border:false,
      style: 'margin:0 auto',
      labelWidth: 66,
      items: [{
          layout: 'column',     
          items:[
               {
                   columnWidth: .25,
                   layout: 'form',
                   border: false,
                   items:[
                       idhide,
                       cph,
                       wxlx,
                       gsf,
                       ccrq
                   ]
               },
               {
                   columnWidth: .25,
                   layout: 'form',
                   border: false,
                   items:[
                       chexin,
                       jcrq,
                       clf,                       
                       sfgz
                   ]
               },
               {
                   columnWidth: .25,
                   layout: 'form',
                   border: false,
                   items:[
                       wxlc,
                       wxf,
                       qtf
                   ]
               },
               {
                   columnWidth: .25,
                   layout: 'form',
                   border: false,
                   items:[
                       company,
                       zje,
                       
                       remark 
                   ]
               }]           
       }
   ]
});

//客户表单
var form = new Ext.form.FormPanel({
   // title:'添加员工',
   // autoHeight: true,
   style: 'margin-top:1px;',
   height: 113,
    fileUpload: true,
    border: true,
    labelWidth: 80,
    labelAlign: 'right',
    buttonAlign: 'center',
    layout: 'column',
    autoWidth: true,
    frame: true,
    items: [
        formset
    ]
});

//卡片
  var tabs = new Ext.TabPanel({
        width: document.documentElement.clientWidth,
               style: 'margin-top:1px;',
        height: document.documentElement.clientHeight - 142,
        activeTab: 0,
        frame:true,
        items:[
            {contentEl:'script', title: '维修项目'},
            {contentEl:'markup', title: '领料清单'}
        ]
  });
  
  
  ///////////////维修项目表格开始///////////////////////////////
  
        
  
       //客户单位
        var dept_store = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({url: '../system/selectjson.aspx?table=s_department&text=deptname&value=id'}),
            reader: new Ext.data.ArrayReader({},[
                {name: 'value'},
                {name: 'text'}
            ])
        });
        
        //客户单位
        var fzr_store = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({url: '../system/selectjson.aspx?table=employee&text=pname&value=id'}),
            reader: new Ext.data.ArrayReader({},[
                {name: 'value'},
                {name: 'text'}
            ])
        });


       
      ///维修班组
        var dept = new Ext.form.ComboBox({
            mode: 'remote',
            store: dept_store,
            name: '_dept',
            id: '_dept',
            hiddenName: 'dept',
            triggerAction: 'all',
            valueField: 'value',
            displayField: 'text',
              listeners :{
                 specialKey :function(field,e){
                     if (e.getKey() == Ext.EventObject.ENTER){
                        mend_project_grid.startEditing(editorRow,4);
                     } 
                 }
              }    
        });
        

       var editorRow = 0;
       var sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
       var cm = new Ext.grid.ColumnModel([
          new Ext.grid.RowNumberer(),
          sm,
          {header:'项目名称', dataIndex:'projname',editor:new Ext.grid.GridEditor(new Ext.form.TextField({
              allowBlank: false,
              blankText:'请输入项目名称',              
              listeners :{
                 specialKey :function(field,e){
                     try{
                         if (e.getKey() == Ext.EventObject.ENTER){  
                            //alert(editorRow);          
                            mend_project_grid.startEditing(editorRow,3);
                         } 
                     }catch(e){
                        alert("输入错误:"+e.message);
                     }
                 }
              }
          }))},
          {header:'维修班组', dataIndex:'mendclass',editor:dept,
              renderer: function(value, cellmeta, record,rowIndex,columnIndex) {
                  var index = dept_store.find(Ext.getCmp('_dept').valueField,value);
                  var record = dept_store.getAt(index);
                  var returnvalue = "";
                  if (record) {
                     returnvalue = record.data.text;
                  }
                  return returnvalue;//注意这个地方的value是上面displayField中的value
              }
           },
           {header:'工时', dataIndex:'gs',editor:new Ext.grid.GridEditor(new Ext.form.NumberField({
              listeners :{
                 specialKey :function(field,e){
                     if (e.getKey() == Ext.EventObject.ENTER){  
                        //alert(editorRow);          
                        mend_project_grid.startEditing(editorRow,5);
                     } 
                 }
              }
          }))},
          {header:'单价', dataIndex:'price',editor:new Ext.grid.GridEditor(new Ext.form.NumberField({
              listeners :{
                 specialKey :function(field,e){
                     if (e.getKey() == Ext.EventObject.ENTER){  
                        //alert(editorRow);          
                        mend_project_grid.startEditing(editorRow,7);
                     } 
                 }
              }
          }))},
          {header:'工时费', dataIndex:'gsf',editor:new Ext.grid.GridEditor(new Ext.form.NumberField({
              listeners :{
                 specialKey :function(field,e){
                     if (e.getKey() == Ext.EventObject.ENTER){  
                        //alert(editorRow);          
                        mend_project_grid.startEditing(editorRow,7);
                     } 
                 }
              }
          }))},
          {header:'负责人', dataIndex:'fzr',editor:new Ext.grid.GridEditor(new Ext.form.TextField({
              listeners :{
                 specialKey :function(field,e){
                     if (e.getKey() == Ext.EventObject.ENTER){  
                        //alert(editorRow);          
                        mend_project_grid.startEditing(editorRow,8);
                     } 
                 }
              }
          }))},
          
          {header:'备注', dataIndex:'remark',editor:new Ext.grid.GridEditor(new Ext.form.TextField({
              allowBlank: false,
              blankText: '请输入备注',
              listeners :{
                 specialKey :function(field,e){                    
                     if (e.getKey() == Ext.EventObject.ENTER){
                        mend_project_grid.startEditing(editorRow,2);
                     } 
                 }
              }
          }))}          
       ]);
       
       
       var project_record = new Ext.data.Record.create([
           {name:'projname', type: 'string'},
           {name:'mendclass', type: 'string'},
           {name:'gs', type: 'string'},   
           {name:'price', type: 'string'},   
           {name:'gsf', type: 'string'},   
           {name:'fzr', type: 'string'},
           {name:'remark', type: 'string'}
       ]);
       
      var store = new Ext.data.Store({
           proxy: new Ext.data.HttpProxy({url:'data/cuslist.aspx'}),
           reader: new Ext.data.JsonReader({
               totalProperty: 'totalProperty',
               root: 'root'
           },[
                  {name:'projname'},
                  {name:'mendclass'},
                  {name:'gs'},
                  {name:'price'},
                  {name:'gsf'},
                  {name:'fzr'},
                  {name:'remark'}
             ]
           )
       });
       
       var mend_project_grid = new Ext.grid.EditorGridPanel({
               height: document.documentElement.clientHeight-171,
               stripeRows: true,  //隔行换颜色
               store: store,
               loadMask: true,     //加载时提示load...
               viewConfig: {
                    forceFit: true
               },
               clicksToEdit: 1,
               cm: cm,
               sm: sm,
               autoScroll : true,
               tbar: new Ext.Toolbar([
                 '-'
                 ,
                 {
                    text: '新增',
                    handler: function(){
                       var n = mend_project_grid.getStore().getCount();// 获得总行数
                       var p = new project_record({
                           projname: '',
                           mendclass: '',
                           gs: '',                          
                           price:'',
                           gsf: '',
                           fzr: '',
                           remark: '',                           
                           del: ''
                       });
                       mend_project_grid.stopEditing();
                       store.insert(n,p);
                       mend_project_grid.startEditing(n, 2);// 开始编辑1单元格
                       
                    }
                 },'-',
                 {
                     text: '删除',
                     handler : function(){ 
                        var rows = mend_project_grid.getSelectionModel().getSelections();// 返回值为 Record 数组
                        if(rows.length==0){ 
                             Ext.MessageBox.alert('警告', '最少选择一条信息，进行删除!'); 
                        }else{ 
                             for(var i=0; i<rows.length; i++){
                                 store.remove(rows[i]);
                             }
                             setcountmoney();
                        }

                     }
                 },
                 '-'
               ])
           });
           store.load();         
           
          
          mend_project_grid.addListener('beforeedit', onEnter_project);          
          function onEnter_project(e){
              try{
                 editorRow = e.row;       
              }catch(e){
                 alert(e.message);
              }
          }
          
          mend_project_grid.addListener('afteredit', editor_after);          
          function editor_after(e){ 
             // if(e.column == 4 || e.column == 5){
                 setGsf(e);
            //  }      
                        
          } 
          
         
          
          //更换总金额
          function setGsf(e){
              var view = mend_project_grid.getView();
              var gsf_xiaoji = 0;
              
              
              var price = view.getCell(e.row,5);  //得到单价对象
              var gs = view.getCell(e.row,4);    //得到工时对象
              try{
                  gsf_xiaoji = parseFloat(gs.innerText * price.innerText).toFixed(2);  //得到工时费小计
                  
                  if(gsf_xiaoji != 0){
                     view.getCell(e.row,6).innerText = gsf_xiaoji;
                  }
              }catch(e){
                  gsf_xiaoji = 0;
                  alert("6:"+e.message);
              }
              
              setcountmoney(); 
          }
          
          function setcountmoney(){
              var rows = mend_project_grid.getStore();// 获得总行数
              var view = mend_project_grid.getView();
              var rowNumber = rows.getCount();
              var gsf_count = 0;
              for(i=0; i<rowNumber; i++){   //得到工时费总金额 
                  var selfgsf = view.getCell(i,6).innerText;
                  try{
                      if(Number(selfgsf) != 0){
                          gsf_count += parseFloat(selfgsf);
                      }                      
                  }catch(e){
                      alert("异常："+e.message);
                  }
                                              
              }
              
              try{
                 gsf.setValue(gsf_count);  //设置工时费总金额
                // zje.setValue(parseInt(clf.getValue()) + gsf_count+ parseFloat(qtf.getValue())+parseFloat(wxf.getValue()));  //设置总金额
                 SetMoneySum();
              }catch(e){
                 alert("异常:" + e.message);
                 zje.setValue(gsf_count);
              } 
          }
          
          function SetMoneySum(){
               var c_clf = parseFloat(clf.getValue());
               var c_gsf = parseFloat(gsf.getValue());
               var c_qtf = parseFloat(qtf.getValue());
               var c_wxf = parseFloat(wxf.getValue());
               
               //var c_count = parseInt(parseFloat(clf.getValue()).toFixed(2) + parseFloat(gsf.getValue()).toFixed(2) + parseFloat(qtf.getValue()).toFixed(2) + parseFloat(wxf.getValue()).toFixed(2));
               var c_count = (c_clf + c_gsf + c_qtf + c_wxf).toFixed(0);
               zje.setValue(c_count);
          }
 
 /////////////////领料清单表格////////////////////////////
        var editorRow1 = 0;
        //客户单位
        var chexin_store = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({url: '../System/selectitem.aspx?sid=1'}),
            reader: new Ext.data.ArrayReader({},[
                {name: 'value'},
                {name: 'text'}
            ]),
            autoLoad: true
        });
        
        //客户单位
        var product_store = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({url: 'data/product_select.aspx'}),
            reader: new Ext.data.ArrayReader({},[
                {name: 'value'},
                {name: 'text'}
            ])
        });
        
        //产品数量
//        var number_store = new Ext.data.Store({
//            proxy: new Ext.data.HttpProxy({url: 'data/number.aspx?id=12'}),
//            reader: new Ext.data.ArrayReader({},[
//                {name: 'value'},
//                {name: 'text'}
//            ])
//        });
        
//        var numberCombox = new Ext.form.ComboBox({
//            mode: 'remote',
//            store: number_store,
//            name: '_number',
//            hiddenName: 'number',
//            triggerAction: 'all',
//            valueField: 'value',
//            displayField: 'text'    
//        });
        
        var chexinS = new Ext.form.ComboBox({
            mode: 'remote',
            store: chexin_store,
            name: '_chexin',
            hiddenName: 'chexin',
            triggerAction: 'all',
            valueField: 'value',
            displayField: 'text'    
        });
        
        chexinS.on("select",function(){
            var id = chexinS.getValue();
            product_store.proxy= new Ext.data.HttpProxy({url: 'data/product_select.aspx?cx=' + id}); 
          //  alert('data/product_select.aspx?cx=' + escape(id));
            product_store.load();
        });
 
        var remark = new Ext.form.TextField({
              name: 'remark',
              listeners :{
                 specialKey :function(field,e){
                     product_bill_grid.startEditing(editorRow1,8); 
                 }
              }
        });
        var danjia = new Ext.form.TextField();
 
      var sm1 = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
      var p_cm = new Ext.grid.ColumnModel([
          new Ext.grid.RowNumberer(),
          sm1,
          {header:'',dataIndex:'car_type',hidden:true},
          {header:'材料名称', dataIndex:'product_name',editor:new Ext.grid.GridEditor(
               new Ext.form.ComboBox({
                    mode: 'remote',
                    store: product_store,
                    name: '_product',
		    resizable: true,
                    id: '_product',
                    emptyText:'没有数据',
                    hiddenName: 'product',
                    triggerAction: 'all',
                    loadingText:'没有数据',
                    valueField: 'value',
                    displayField: 'text',
                    enableKeyEvents:true,
                    listeners : {
//                         select : function(combo, record, index) 
//                         { 
//                            var url = 'data/number.aspx?id=' + escape(combo.getValue());
//                            number_store.proxy= new Ext.data.HttpProxy({url: url});
//                            number_store.load();
//                          },
                          specialKey :function(field,e){   
                           //  alert("事件开始:" + editorRow1);     
                             product_bill_grid.startEditing(editorRow1,7);            
//                             if (e.getKey() == Ext.EventObject.ENTER){
//                                alert(editorRow1);
//                                product_bill_grid.startEditing(editorRow1,3);
//                             } 
                          },
                          keyup :function(field,e) {
                            if(!(e.keyCode == 38 || e.keyCode == 40)){
                                var url = 'data/product_select.aspx?name=' + escape(this.getRawValue());
                                product_store.proxy= new Ext.data.HttpProxy({url: url});
                                product_store.load();
                            }
                          }
                    }
               })),
                    renderer: function(value, cellmeta, record,rowIndex,columnIndex) {
                      var index = product_store.find(Ext.getCmp('_product').valueField,value);
                      var record = product_store.getAt(index);
                      var returnvalue = "";
                      if (record) {
                         returnvalue = record.data.text;
                      }
                      return returnvalue;
                   }
           },          
          {header:'目前库存', dataIndex:'chandi'},
          {header:'单位', dataIndex:'unit'},
          {header:'单价', dataIndex:'price',editor:danjia},
          {header:'备注', dataIndex:'remark',editor:remark},
//          {header:'数量', dataIndex:'number',editor:new Ext.grid.GridEditor(
//               new Ext.form.ComboBox({
//                    mode: 'remote',
//                    store: number_store,
//                    name: '_number',
//                    hiddenName: 'number',
//                    triggerAction: 'all',
//                    valueField: 'value',                    
//                    displayField: 'text'                   
//                    }))
//          },
          
          {header:'数量', dataIndex:'number',editor:new Ext.grid.GridEditor(new Ext.form.NumberField())},
          {header:'小计', dataIndex:'xiaoji'}
       ]);
       
       
       
       var p_store = new Ext.data.Store({
           proxy: new Ext.data.HttpProxy({url:''}),
           reader: new Ext.data.JsonReader({
               totalProperty: 'totalProperty',
               root: 'root'
           },[                  
                  {name:'car_type'}, 
                  {name:'product_name'},                 
                  {name:'chandi'},
                  {name:'unit'},
                  {name:'price'},
                  {name:'remark'},
                  {name:'number'},
                  {name:'xiaoji'}
             ]
           )
       });
       
        var project_bill_record = new Ext.data.Record.create([
           {name:'car_type', type: 'string'},
           {name:'product_name', type: 'string'},
           {name:'chandi', type: 'string'},
           {name:'unit', type: 'string'},
           {name:'price', type: 'string'}, 
           {name:'remark', type: 'string'},                  
           {name:'number', type: 'string'},
           {name:'xiaoji', type: 'string'}
       ]);
       
       var product_bill_grid = new Ext.grid.EditorGridPanel({
               height: document.documentElement.clientHeight-200,
               stripeRows: true,  //隔行换颜色
               store: p_store,
               loadMask: true,     //加载时提示load...
               clicksToEdit: 1,
               viewConfig: {
                    forceFit: true
               },
               cm: p_cm,
               sm:sm1,
               autoScroll : true,
               tbar: new Ext.Toolbar([
                 '-'
                 ,
                 {
                    text: '新增',
                    handler: function(){
                       var n = product_bill_grid.getStore().getCount();// 获得总行数
                       var p = new project_bill_record({
                           car_type: '',
                           product_name: '',
                           chandi: '',
                           unit: '',
                           price: '',
                           remark:'',
                           number: '',
                           xiaoji: ''
                       });
                       product_bill_grid.stopEditing();
                       p_store.insert(n,p);
                       product_bill_grid.startEditing(n, 3);// 开始编辑1单元格
                    }
                 },'-',
                 {
                     text: '删除',
                     handler : function(){ 
                        var rows = product_bill_grid.getSelectionModel().getSelections();// 返回值为 Record 数组
                        if(rows.length==0){ 
                             Ext.MessageBox.alert('警告', '最少选择一条信息，进行删除!'); 
                        }else{ 
                             for(var i=0; i<rows.length; i++){
                                 p_store.remove(rows[i]);
                             }
                             setClf();
                        }

                     }
                 },
                 '-'
               ])
           });  
       
       product_bill_grid.addListener('rowClick', rowClick);
       function rowClick(grid, rowIndex, e){
                var record = product_bill_grid.getStore().getAt(rowIndex); //得到行号
               
                var fieldName = product_bill_grid.getColumnModel().getDataIndex(2); //得到第3列的dataIndex
                var data = record.get(fieldName);  //得到第3列的值  赋给第2个下拉列表 
                var url = 'data/product_select.aspx?cx=' + escape(data);              
                product_store.proxy= new Ext.data.HttpProxy({url: url});
                product_store.load();
                
              //  var product = product_bill_grid.getColumnModel().getDataIndex(3); //得到第3列的dataIndex
              //  var productid = record.get(product);
               // number_store.proxy= new Ext.data.HttpProxy({url: 'data/number.aspx?id='+productid});
              //  number_store.load();
       }
       
       
          //编辑之前
          product_bill_grid.addListener('beforeedit', onEnter_product);          
          function onEnter_product(e){
              //product_bill_grid.startEditing(e.row,e.column+1);
              editorRow1 = e.row;
          }
          
          //编辑之后
          
          function setInfo(e){
                  var record = product_bill_grid.getStore().getAt(e.row); //得到行号
               
                  var fieldName = product_bill_grid.getColumnModel().getDataIndex(3); //得到第3列的dataIndex
                  var data = record.get(fieldName);  //得到第3列的值  赋给第2个下拉列表 
                      if(data.length>0){
                          var url = "data/GetProductParam.aspx?pid="+data;
                          var params = doStart(url);
                          if(params.length>1){
                          var paramArray = params.split('&');
                          var chandi = product_bill_grid.getView().getCell(e.row,4);
                          var unit = product_bill_grid.getView().getCell(e.row,5);
                          var price = product_bill_grid.getView().getCell(e.row,6);
                          
                          chandi.innerHTML = paramArray[0];
                          unit.innerHTML = paramArray[1];
                          price.innerHTML = paramArray[2];
                      }
                  }
                  
          }
          
          
          product_bill_grid.addListener('afteredit', setParam);          
          function setParam(e){   
              setInfo(e); 
              if(e.column==3){
                  
              }else{
                      var chandi = product_bill_grid.getView().getCell(e.row,4);
                      var unit = product_bill_grid.getView().getCell(e.row,5);
                      var price = product_bill_grid.getView().getCell(e.row,6);
                      
              }
              
              if(e.column == 8){
                  var record = product_bill_grid.getStore().getAt(e.row);              
                  var fieldName = product_bill_grid.getColumnModel().getDataIndex(6); 
                  var product_price = record.get(fieldName); 
                  
                  var record = product_bill_grid.getStore().getAt(e.row);              
                  var fieldName = product_bill_grid.getColumnModel().getDataIndex(8); 
                  var product_num = record.get(fieldName); 
              
                  var xiaoji = product_bill_grid.getView().getCell(e.row,9);
                  if(product_price.lenght>0){
                      xiaoji.innerHTML = (product_id*product_num);                   
                  }else{
                      var record = product_bill_grid.getStore().getAt(e.row);              
                      var fieldName = product_bill_grid.getColumnModel().getDataIndex(3); 
                      var product_id = record.get(fieldName); 
                      
                      var url = "data/getXiaoji.aspx?pid="+product_id+"&num="+product_num;
                      var res = doStart(url);
                      xiaoji.innerHTML = res;
                      
                      setClf();
                  }  
                                     
              }
          }
            var rates = document.all.rate.value;
          function setClf(){
              var rows = product_bill_grid.getStore();// 获得总行数;
              var view = product_bill_grid.getView();
              var rowNumber = rows.getCount();
              var xiaoji_count = 0;
              for(i=0; i<rowNumber; i++){
                  xiaoji_count += parseFloat(view.getCell(i,9).innerText);               
              }
              clf.setValue(xiaoji_count);
              var wxfss = xiaoji_count*parseFloat(rates);
              wxfss = wxfss.toFixed(2);
              wxf.setValue(wxfss);
              SetMoneySum();
//              try{
//                var zjess = parseFloat(gsf.getValue())+xiaoji_count+parseFloat(wxfss)+parseFloat(qtf.getValue());
//                zje.setValue(zjess.toFixed(0));
//              }catch(e){
//                 zje.setValue(xiaoji_count);
//              } 
          }
          
          

       
       
       
 ////////////////////////////////////////////////////////////////////////////      

    var isprint = "0";

    var toolbar = new Ext.Toolbar([
      '-',{
          text: '保存',
          //style: 'padding-buttom: 5px',
          handler: function(){
                myform = form.getForm();
                var m1 = store.modified.slice(0);
                           var jsonArray1 = [];
                           Ext.each(m1,function(item){
                               jsonArray1.push(item.data);
                           });
                           if(jsonArray1.length < 1)
                           {
			       if(!window.confirm("您确定不需要录入维修项目吗?")){
                                   return ;
                               } 
                           }
                          //获取领料清单
                          var m2 = p_store.modified.slice(0);
                           var jsonArray2 = [];
                           Ext.each(m2,function(item){
                                jsonArray2.push(item.data);
                           });
                           if(jsonArray2.length < 1)
                           {
                               if(!window.confirm("您确定该项目不需要领取材料吗?")){
                                   return ;
                               }                      
                           }
                           
                myform.submit({
                    url: 'data/SaveProject.aspx',
                    success: function(form,action){
                           
                          
                        var id = action.result.params.id;
                        if(id == 0){
                            alert("项目保存失败!");
                            return;
                        }
                        //获取维修项目                      
                           Ext.lib.Ajax.request(
                                'post',
                                'data/SaveProjects.aspx',
                                {success:function(response){
                                    var res = response.responseText;
                                    if(res != "success"){
                                        Ext.Msg.alert("错误","保存维修项目是出错"); 
                                    }
                                },faliure:function(){
                                    Ext.Msg.alert("错误","维修项目保存有出错!");
                                }},
                                'data=' + encodeURIComponent(Ext.encode(jsonArray1))+'&bid=' + id
                           );
                           
                         
                           Ext.lib.Ajax.request(
                                'post',
                                'data/SaveProduct.aspx',
                                {success:function(response){  
                                    window.location.reload();                                   
                                    if(window.confirm("保存成功,是否打印!")){
                                       window.open('Dy.aspx?pid=' + id); 
                                    }
                                   
//                                   try{
//                                        myform.reset();
//                                    }catch(e){
//                                        alert("重置表单发生异常："+e.message);
//                                    }
//                                    
//                                    try{
//                                        store.removeAll();
//                                    }catch(e){
//                                        alert("重置维修项目单发生异常："+e.message);
//                                    }
//                                    
//                                    try{
//                                        p_store.removeAll();
//                                    }catch(e){
//                                        alert("重置领料单发生异常："+e.message);
//                                    }
                                    
                                      //window.open('Dy.aspx?pid=' + id);                                   
                                },faliure:function(){
                                    Ext.Msg.alert("错误","Ajax连接失败");
                                }},
                                'data=' + encodeURIComponent(Ext.encode(jsonArray2))+'&bid=' + id
                           );
                    },
                    failure: function(f,a){
                      alert("保存失败！")
                    }
                });              
          }
     },'-',{
         text:'重置',
         handler: function(){
             window.location.reload();
         }
     },'-'
     ]);    

Ext.onReady(function(){
    Ext.QuickTips.init();
    toolbar.render("toolbar");
    form.render("form");
    tabs.render("tabs1");
    mend_project_grid.render("mend_project");
    product_bill_grid.render("product_bill");
})
