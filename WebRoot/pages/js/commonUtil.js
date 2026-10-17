function alertWarring(msg) {
	Ext.MessageBox.show({
		title : "提示",
		msg : msg,
		width : 250,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.WARNING
	});
}

function alertInformation(msg) {
	Ext.MessageBox.show({
		title : "提示",
		msg : msg,
		width : 250,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.INFO
	});
}
function alertSuccess(msg){
	Ext.MessageBox.show({
		title : "提示",
		msg : '<font style="color:green">'+msg+'</font>',
		width : 250,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.INFO
	});
}

function alertError(msg) {
	Ext.MessageBox.show({
		title : "提示",
		msg : '<font style="color:red">'+msg+'</font>',
		width : 250,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.ERROR
	});
}

function alertOkorCancel(msg, fn)
{
	Ext.MessageBox.show({
        title: '提示',
        msg: msg,
        width : 250,
        buttons: Ext.MessageBox.YESNO,
        fn: fn,
        icon : Ext.MessageBox.QUESTION
    });
}

// 重写FORM，解决FormPanel.form.setValues方法对这2个东东内部的radio和checkbox无法动态赋值
//Ext.override(Ext.form.BasicForm,{   
//	findField : function(id){           
//   	var field = this.items.get(id);           
//   	if(!field){   
//    	this.items.each(function(f){   
//           	if(f.isXType('radiogroup')||f.isXType('checkboxgroup')){   
//               	f.items.each(function(c){   
//                   	if(c.isFormField && (c.dataIndex == id || c.id == id || c.getName() == id)){   
//                       	field = c;   
//                       	return false;   
//                   	}   
//               	});   
//            }   
//                                   
//            if(f.isFormField && (f.dataIndex == id || f.id == id || f.getName() == id)){   
//                field = f;   
//                return false;   
//            }   
//        });   
//    }   
//    return field || null;   
//    }    
//});

Ext.override(Ext.form.RadioGroup, {
    getValue : function() {
        var v;
        this.items.each(function(item) {
            if (item.getValue()) {
                v = item.getRawValue();
                return false;
			}
		});
		return v;
	},
	setValue : function(v) {
        if (this.rendered) {
            this.items.each(function(item) {
                item.setValue(item.getRawValue() == v);
			});
        } else {
            for (k in this.items) {
                this.items[k].checked = this.items[k].inputValue == v;
            }
        }
	}
});


