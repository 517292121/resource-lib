
	/**获取资源语种的信息**/
	function changLanguage(sourceId, lanId, store_id, stype, addType){
		 var jsonPath = getPath()+ "/Json_Rpc";
	     try{
		   jsonrpc = new JSONRpcClient(jsonPath);
		   var result = jsonrpc.resourceRpc.getSourceInfo(sourceId,lanId,store_id, stype);
		   if("check" == addType)
		   	   setValue(result,addType);
		   else
		       setValue(result);
		  }catch(e){
		      alert("错误：" +e);
		  }
	}
	
	function setValue(sourceData,addType){
	     var eleArr = sourceData.split(element_split);
	     var dataArr;
	     for(i=0; i<eleArr.length; i++){
	         dataArr = eleArr[i].split(element_data_split);
	         try{
		         if(dataArr != null && dataArr.length > 1){
		         	if("check" == addType){
		         		var obj = document.getElementById(dataArr[0]);
		         		if(obj)
		         			obj.innerHTML = eleArr[i].replace(dataArr[0]+element_data_split,"");
		         	}else{
		         		document.getElementById(dataArr[0]).value = eleArr[i].replace(dataArr[0]+element_data_split,"");
		         	}
		         }
	         }catch(e){
	            //alert("异常：" + dataArr[0]);
	         }
	     }
	}