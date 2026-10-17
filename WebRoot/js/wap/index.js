$(function(){
	
    		var w_width = $(window).width();
    		$('.long-nav .nav-content .nav li').width((w_width-20)/5.5);
    		$('.long-nav .nav-content').width((w_width-20)/5.5*$('.long-nav .nav-content .nav li').length);
    		
    		var list_data={},
    		choose_html_content="<div class='img-all'><ul class='img-list'>";
    		//数据初始化
    		$.ajax({
					type:"get",
					dataType:'json',
					url: basePath + "wap/cmdres",
					async:true,
					timeout:3000,
					success:function(data){
						//将请求到的接口存入缓存
						console.log(data)
	    				localStorage.setItem('list_data', JSON.stringify(data));
	    				var nav_id = $('.choose li.active').data('id');
	    				list_data = data;
	    				for(var i in list_data[nav_id]){
		    				var li_data = list_data[nav_id][i];
		    				if(isNull(li_data[2])){
		    					choose_html_content+="<li><a href='"+basePath+"wap/resdetail/"+li_data[0]+"' id='"+li_data[0]+"'><img src='"+basePath+"images/platform/doc-cover.jpg'/><span>"+li_data[1]+"</span></a></li>";
		    				}else{
			    				choose_html_content+="<li><a href='"+basePath+"wap/resdetail/"+li_data[0]+"' id='"+li_data[0]+"'><img src="+ li_data[2]+" onerror='"+basePath+"images/platform/doc-cover.jpg'/><span>"+li_data[1]+"</span></a></li>";
		    				}
		    			}
	    				
	    				$('.content-img').html(choose_html_content+'</ul></div>');
			    		$('.content-img .img-list li a').width((w_width-30)/3.5);
			    		$('.content-img .img-list').width((w_width)/3.5*$('.content-img .img-list li').length);
			    		
					},
					error:function(xhr,textStatus){
						if(textStatus=='timeout'){
							//alert('请求超时，请检查您的网络')
						}
					}
							
				});
				
				
				var w_width = $(window).width();
	    		$('.long-nav .nav-content .nav li').width((w_width-20)/5.5);
	    		$('.long-nav .nav-content').width((w_width-20)/5.5*$('.long-nav .nav-content .nav li').length);
    		
    		//图片滑动
    		
    		//最新资源列表
    		$.ajax({
					type:"get",
					dataType:'json',
					url: basePath + "index/resList/rsdaBuilddate",
					async:true,
					timeout:3000,
					success:function(data){
						localStorage.setItem('new_news', JSON.stringify(data));
						var html_list='';
						$.each(data.dataList,function(i,n){
							html_list+="<li data-id="+n[0]+"><a href='"+basePath+"wap/resdetail/"+idToStr(n[0])+"' ><p class='new-title'>"+n[1]
							+" </p><p class='new-name'>"+poput_alert_release+"："+n[2]+"  "+poput_alert_borowse+"："+n[3]+"</p></a></li>"
						})
						$('.news-li').html(html_list);
					},
					error:function(xhr,textStatus){
						if(textStatus=='timeout'){
							alert(poput_alert_request_timeout)
						}
					}
							
				});
    		
    		
    		
    		
    		$('.choose li').click(function(){
    			$(this).addClass('active').siblings().removeClass('active');
    			var nav_id='';
    			//data-id判断导航选项
    			nav_id = $(this).data('id');
    			//判断缓存是否有数据
    			if(!JSON.parse(localStorage.getItem('list_data'))){
    				list_data = JSON.parse(localStorage.getItem('list_data'));
    			}else{
    				//缓存没有请求接口
    				$.ajax({
						type:"get",
						dataType:'json',
						url: basePath + "wap/cmdres",
						async:true,
						timeout:3000,
						success:function(data){
							//将请求到的接口存入缓存
		    				localStorage.setItem('list_data', JSON.stringify(data));
		    				list_data = data;
						},
						error:function(xhr,textStatus){
							if(textStatus=='timeout'){
								//alert('请求超时，请检查您的网络')
							}
						}
								
					});
	    		
    			}
    			//遍历数据
    			choose_html_content="<div class='img-all'><ul class='img-list'>";
				for(var i in list_data[nav_id]){
		    		var li_data = list_data[nav_id][i];
		    		
		    		if(isNull(li_data[2])){
		    			choose_html_content+="<li><a href='"+basePath+"wap/resdetail/"+li_data[0]+"' id='  "+li_data[0]+" '><img src='"+basePath+"images/platform/doc-cover.jpg'/><span>"+li_data[1]+"</span></a></li>";
		    		}else{
			    		choose_html_content+="<li><a href='"+basePath+"wap/resdetail/"+li_data[0]+"' id='  "+li_data[0]+" '><img src="+li_data[2]+" onerror=\"this.src='"+basePath+"images/platform/doc-cover.jpg'\"/><span>"+li_data[1]+"</span></a></li>";
		    		}
		    	}  			
    			//更改内容
    			$('.content-img').html(choose_html_content+'</ul></div>');
			    $('.content-img .img-list li a').width((w_width-30)/3.5);
			    $('.content-img .img-list').width((w_width)/3.5*$('.content-img .img-list li').length);
    			return false;
    		})
    	
    	

			$('.choose-news li').click(function(){
				$(this).addClass('active').siblings().removeClass('active');
				var data_type = $(this).data('type'),news_list='',html_list='';
				//判断选项
				if(data_type===1){
					
					if(JSON.parse(localStorage.getItem('new_news'))){
						news_list = JSON.parse(localStorage.getItem('new_news'));
					}else{
						$.ajax({
							type:"get",
							dataType:'json',
							url: basePath + "index/resList/rsdaBuilddate",
							async:true,
							timeout:3000,
							success:function(data){
								news_list = data;
								localStorage.setItem('new_news', JSON.stringify(data));
							},
							error:function(xhr,textStatus){
								if(textStatus=='timeout'){
							        alert(poput_alert_request_timeout)
							     }
							}
							
						});
					}
					
					
					
				}else if(data_type===2){
					if(JSON.parse(localStorage.getItem('hot_news'))){
						news_list = JSON.parse(localStorage.getItem('hot_news'));
					}else{
						$.ajax({
							type:"get",
							dataType:'json',
							url: basePath + "index/resList/rsdaLooknum",
							async:true,
							timeout:3000,
							success:function(data){
								news_list = data;
								localStorage.setItem('hot_news', JSON.stringify(data));
							},
							error:function(xhr,textStatus){
								if(textStatus=='timeout'){
							        alert(poput_alert_request_timeout)
							     }
							}
							
						});
					}
					
				}
				//遍历数据
				$.each(news_list.dataList,function(i,n){
					html_list+="<li data-id="+n[0]+"><a href='"+basePath+"wap/resdetail/"+idToStr(n[0])+"' ><p class='new-title'>"+n[1]
					+" </p><p class='new-name'>"+poput_alert_release+"："+n[2]+"  "+poput_alert_borowse+"："+n[3]+"</p></a></li>"
				})
				$('.news-li').html(html_list);
				return false;
			})
})


