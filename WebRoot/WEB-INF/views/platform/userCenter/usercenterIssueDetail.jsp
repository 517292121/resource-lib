<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.sc.ScIssue"%>
<%@ include file="../pc/common/header.jsp"%>
	<style type="text/css">
		.show {display: block;}
		.hide {display: none;}
		.show_border{border:1px #d1d3c7 double;}
	</style>
		
		<script type="text/javascript">
		    var path = '<%=request.getContextPath() %>';
			String.prototype.trim = function()    
			{    
			return this.replace(/(^\s*)|(\s*$)/g, "");    
			}
			function submit(val){
				var form1 = document.getElementById(val);
				if(form1)
					form1.submit();
			}
			function submit2(){
				var t = document.getElementById('scirContent').value;
				
				if(t=="" || t.trim().length<1){
					var info = "<fmt:message key='detail.reply.not.empty'/>！";
					alert(info);			
					return ;
				}else if(t.trim().length>8000){
					var info_1 = "<fmt:message key='detail.reply.max'/>!";
					alert(info_1);
					return;
				}
				
				var form2 = document.getElementById("form2");
				if(form2)
					form2.submit();
			}
			function reset(val){
				var form1 = document.getElementById(val);
				if(form1){
					document.getElementById('scirContent').value='';
					document.getElementById('scirAttachmentName').value='';
				}
			}
	  			
	  			function textCounterByLength(obj, showid, maxlimit){
					var len = obj.value.length;
					var showobj = document.getElementById(showid);
					if(len > maxlimit) {
						obj.value = getStrbylen(obj.value, maxlimit);
						showobj.innerHTML = '0';
					} else {
						showobj.innerHTML = maxlimit - len;
					}
					if(maxlimit - len > 0) {
						showobj.parentNode.style.color = "";
					} else {
						showobj.parentNode.style.color = "red";
					}
				}
	  			function getStrbylen(str, len) {
					var num = 0;
					var strlen = 0;
					var newstr = "";
					var obj_value_arr = str.split("");
					for(var i = 0; i < obj_value_arr.length; i ++) {
						if(i < len && num + byteLength(obj_value_arr[i]) <= len) {
							num += byteLength(obj_value_arr[i]);
							strlen = i + 1;
						}
					}
					if(str.length > strlen) {
						newstr = str.substr(0, num);
					} else {
						newstr = str;
					}
					return newstr;
				}
	  			function byteLength (sStr) {
					aMatch = sStr.match(/[^\x00-\x80]/g);
					return (sStr.length + (! aMatch ? 0 : aMatch.length));
				}
		</script>
		
		<script type="text/javascript">
		var path = '<%=request.getContextPath() %>';
		function get_rate(rate,scirId){
			 rate=rate.toString();
			 var s;
			 var g;
			 if (rate.length>=3){
				  s=10; 
				  g=0;
			 }else if(rate=="0"){
				  s=0;
				  g=0;
			 }else{
				  s=rate.substr(0,1);
				  g=rate.substr(1,1);
			 }
			 $(".big_rate_up").animate({width:(parseInt(s)+parseInt(g)/10) * 10,height:20},1000);
			 $(".big_rate_bak b").each(function(){
				  $(this).mouseover(function(){
					   $(".big_rate_up").width($(this).attr("rate") * 10 );
			  }).click(function(){
				    var temp = $(this).attr("rate")/2;
				    var grade = document.getElementById("scirGrade"+scirId);
					if(grade){
						grade.value=temp;
					}
			  })
			 })
			 $(".big_rate_bak").mouseout(function(){
				  $(".big_rate_up").width((parseInt(s)+parseInt(g)/10) * 10);
			 }) 
		}
		function up_rate(rate,scirId){
			 $(".big_rate_up").width("0");
			 get_rate(rate,scirId);
		}
		function showDiv(scirId,scisId){
			var div = document.getElementById(scirId);
			if(div){
			    var txt = '<div class="r_conent" id="div'+scirId+'">';
			        txt += '<form id="11'+scirId+'" action="'+path+'/platform/scIssue.htm?action=issueClose" method="post">';
					txt += '<table cellpadding="0" cellspacing="0" border="0" class="rContent">';
					txt += '<tr>';
					txt += '<td valign="top" width="100px;"align="right"><strong class="str"><fmt:message key='detail.word.or.comment'/>：</strong></td>'; 
					txt += '<td align="left">';
					txt += '<textarea name="scirContent" onkeyup="textCounterByLength(this, \'maxlimit'+scirId+'\', 300)"></textarea><br/>';
					txt += '<span style="float: right;"><fmt:message key='detail.can.also.enter'/><strong id="maxlimit'+scirId+'">300</strong> <fmt:message key='detail.a.word'/>！</span>';
					txt += '<input type="hidden" name="scisId" value="'+scisId+'">';
					txt += '<input type="hidden" name="scirId" value="'+scirId+'">';
					txt += '<input type="hidden" name="scirGrade" value="3" id="scirGrade'+scirId+'">';
					txt += '</td>';
					txt += '</tr>';
					
					txt += '<tr>';
					txt += '<td align="right"><strong class="str"><fmt:message key='detail.satisfaction'/>：</strong></td>';
					txt += '<td align="left">';
					
					/*
					txt += '<ul class="star-rating" id="selectStarShow'+scirId+'">';
					txt += '<li><a href="javascript:void(0);" title="很差" class="one-star" onclick="selectStar(1,'+scirId+')">1</a></li>';
					txt += '<li><a href="javascript:void(0);" title="一般" class="two-stars" onclick="selectStar(2,'+scirId+')">2</a></li>';
					txt += '<li><a href="javascript:void(0);" title="不错" class="three-stars" onclick="selectStar(3,'+scirId+')">3</a></li>';
					txt += '<li><a href="javascript:void(0);" title="很好" class="four-stars" onclick="selectStar(4,'+scirId+')">4</a></li>';
					txt += '<li><a href="javascript:void(0);" title="完美" class="five-stars" onclick="selectStar(5,'+scirId+')">5</a></li>';
					txt += '</ul>';
					*/
					txt += '<div class="user_rate">';
					txt += '<div class="big_rate_bak" >';
					txt += '<b rate="2" onclick="javascript:up_rate(20,'+scirId+');" title="<fmt:message key='detail.poor'/>"> </b>';
					txt += '<b rate="4" onclick="javascript:up_rate(40,'+scirId+');" title="<fmt:message key='detail.commonly'/>"> </b>';
					txt += '<b rate="6" onclick="javascript:up_rate(60,'+scirId+');" title="<fmt:message key='detail.pretty.good'/>"> </b>';
					txt += '<b rate="8" onclick="javascript:up_rate(80,'+scirId+');" title="<fmt:message key='detail.very.good'/>"> </b>';
					txt += '<b rate="10" onclick="javascript:up_rate(100,'+scirId+');" title="<fmt:message key='detail.perfect'/>"> </b>';
					txt += '<div class="big_rate_up"></div>';
					txt += '</div>';
					txt += '</div>';
					
					
					txt += '</td>';
					txt += '</tr>';
					
					txt += '<tr>';
					txt += '<td>&nbsp;</td>';
					txt += '<td align="left">';
					txt += '<a href="javascript:void(0);" class="button3" onclick="javascript:submit(\'11'+scirId+'\');"><b>&nbsp;<fmt:message key='detail.indeed'/>&nbsp;<fmt:message key='detail.set'/>&nbsp;</b></a>';
					txt += '<a href="javascript:void(0);" class="button3" onclick="javascript:hideDiv('+scirId+','+scisId+');"><b>&nbsp;<fmt:message key='detail.take'/>&nbsp;<fmt:message key='detail.eliminate'/>&nbsp;</b></a>';
					txt += '</td>';
					txt += '</tr>';
					txt += '</table>';
					txt += '</form>';
					txt += '</div>';
					
					div.innerHTML = txt;
					// 默认选中3星        
					//selectStar(3,scirId);	
					get_rate(60,scirId);
			}
		}
		function hideDiv(scirId,scisId){
			var div = document.getElementById(scirId);
			if(div){
				div.innerHTML='<a href="javascript:void(0);" class="good_da button3" onclick="javascript:showDiv('+scirId+','+scisId+')"><b><fmt:message key='detail.set.best.answer'/></b></a>';
			}
		}
		
		// 满意度
		var grade = 3;
		function selectStar(star,scirId){
			var obj = document.getElementById('selectStarShow'+scirId);
			if(obj){	
				obj.style.background = "url(<%=path %>/images/start"+star+".gif) no-repeat";
				var grade = document.getElementById("scirGrade"+scirId);
				if(grade){
					grade.value=star;
				}
			}
		}
	</script>
		
		
		
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow">
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key='edit.current.location'/>：
		  			<a href="<%=path %>/index" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView" class="navigation"><fmt:message key='left.literature.seek.help'/></a> >> 
		  			${scIssue.subScisTitle}</b>
	  			</div>
				
				<!-- 
				<div class="ckzxss">
		                <p>
		                	<a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView">搜索</a> &nbsp;|&nbsp;
		                	<a href="<%=path %>/sc/createScIssue.htm">我要求助</a> &nbsp;|&nbsp;
		                	<a href="<%=path %>/platform/scIssue.htm?action=initPlatformMyIssueView">我的求助</a> &nbsp;|&nbsp;
		                	<a href="<%=path %>/platform/scIssue.htm?action=initPlatformMyJoinIssueView">我参与的</a>
	                	</p>
	                	<div class="uc_search_wrap">
		  					<form action="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView" method="post" id="form1">
			  					<input class="uc_search_input" name="search" value="${search }" type="text"/>
			  					<input class="uc_search_btn" type="submit" title="搜索"/>
				            </form>
	  					</div>
	            </div>
				 -->
				
				
				<div class="daijj">
                
                	<span class="djj_bt1">
	               		<h5>
		                	<c:if test="${scIssue.scisState == 1 }"><span class="sred">[<fmt:message key='issue.to.be.solved'/>]</span></c:if>
							<c:if test="${scIssue.scisState == 2 }"><span class="sgreen">[<fmt:message key='issue.resolved'/>]</span></c:if>
							<c:if test="${scIssue.scisState == 3 }"><span class="sred">[<fmt:message key='issue.closed'/>]</span></c:if>
							${scIssue.scisTitle}
	                	</h5>
                      	<c:if test="${scIssue.scisState == 1 && isMe == true}">
	                        <a href="<c:url value='/platform/scIssue.htm?action=issueClose'/>&scisId=${scIssue.scisId }" class="navbtn selected right" onclick="return confirm(<fmt:message key='detail.turn.off.for.help'/>+'？');"><fmt:message key='detail.closing.the.problem'/></a>
                     	</c:if>
                	</span>
                    
                   	<span class="djj_bt2" >
                       
                    </span>
                           
                      <span class="djj_bt3"><p>${scIssue.handlerScisContent}</p></span>
                      
                    <div class="djj_bt4">
                    	<h2><fmt:message key='detail.last.update'/>：${scIssue.scisSuin.suinName} | <fmt:message key='search.time'/>：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${scIssue.scisCreationTime }"/> </h2>
                    </div>
                      
                </div>
				
				
		        <c:if test="${scIssue.scisState == 2 }">
			        <div class="zjda">
	                 	<h5 class="navbtn selected"><fmt:message key='detail.best.reply'/></h5>
	                    	<div class="zjda_nr">
	                        	<div class="zjda_1">
	                            	<p><span><fmt:message key='detail.respondent'/>：${scIssue.closeScIssueReply.sysUserinfo.suinName } | <fmt:message key='search.time'/>：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${scIssue.scisCloseTime }"/>
	                            		 <% 
		                            	 	ScIssue scIssue = (ScIssue)request.getAttribute("scIssue");
		                            	 	int temp = 0;
		                            	 	try{
		                            	 		temp = scIssue.getCloseScIssueReply().getScirGrade();
		                            	 	}catch(Exception e){
		                            	 		temp = 0;
		                            	 	}
	                            			if(temp > 0){
	                            				%>
				                            	| <fmt:message key='detail.satisfaction'/>：</span>
	                            				<em>
	                            				<%
			                            	 	for(int i=0;i<temp;i++){
			                            	 	%><img  src="<%=path %>/images/one_stars.gif"/><%
			                            	 	}
			                            	 	%>
			                            	 	</em>
			                            	 	<%
	                            			}else{
	                            			%>
	                            				</span>
	                            			<%
	                            			}
		                            	 %>
	                            	 </p>
	                            </div>
	                            
	                            
	                            
	                            <c:if test="${empty scIssue.handlerScisCloseContent && scIssue.closeScIssueReply.scirIsuse == true}">
	                            	<div class="zjda_2"><p>${scIssue.closeScIssueReply.handlerScirContent }</p></div>
	                            	<c:if test="${!empty scIssue.closeScIssueReply.scirAttachmentName}">
		                           		<span class="qtda_22"></span>
		                            </c:if>
		                            <div class="qtda_3">
		                            	<c:if test="${!empty scIssue.closeScIssueReply.scirAttachmentName}">
		                            		<p><b><fmt:message key='detail.enclosure'/>：(</b><b><a href="<%=SysConfigInfo.FILE_URL_PATH %>${scIssue.closeScIssueReply.scirAttachmentUrl }" title="${scIssue.closeScIssueReply.scirAttachmentName }">${scIssue.closeScIssueReply.scirAttachmentName }</a> )</b> <a href="<%=SysConfigInfo.FILE_URL_PATH %>${scIssue.closeScIssueReply.scirAttachmentUrl }" class="download"><fmt:message key='detail.download'/></a></p><br/>
		                            	</c:if>
		                            </div>
	                            </c:if>
	                            <c:if test="${empty scIssue.handlerScisCloseContent && scIssue.closeScIssueReply.scirIsuse == false}">
	                            	<div class="zjda_2"><p><fmt:message key='detail.reply.administrator.shield'/>！</p></div>
	                            </c:if>
	                            
	                            
	                            <c:if test="${!empty scIssue.handlerScisCloseContent && scIssue.closeScIssueReply.scirIsuse == true}">
		                            <div class="zjda_2 dashed"><p>${scIssue.closeScIssueReply.handlerScirContent }</p></div>
		                            <c:if test="${!empty scIssue.closeScIssueReply.scirAttachmentName}">
		                           		<span class="qtda_22"></span>
		                            </c:if>
		                            <div class="qtda_3">
		                            	<c:if test="${!empty scIssue.closeScIssueReply.scirAttachmentName}">
		                            		<p><b><fmt:message key='detail.enclosure'/>：(</b><b><a href="<%=SysConfigInfo.FILE_URL_PATH %>${scIssue.closeScIssueReply.scirAttachmentUrl }" title="${scIssue.closeScIssueReply.scirAttachmentName }">${scIssue.closeScIssueReply.scirAttachmentName }</a> )</b> <a href="<%=SysConfigInfo.FILE_URL_PATH %>${scIssue.closeScIssueReply.scirAttachmentUrl }" class="download"><fmt:message key='detail.download'/></a>  </p><br/>
		                            	</c:if>
		                            </div>
		                            <div class="zjda_3">
		                            	<h5><fmt:message key='detail.questionerreply.evaluation'/>：</h5>
		                                <p>${scIssue.handlerScisCloseContent }</p>
		                            </div>
	                            </c:if>
	                            <c:if test="${!empty scIssue.handlerScisCloseContent && scIssue.closeScIssueReply.scirIsuse == false}">
		                            <div class="zjda_2 dashed"><p><fmt:message key='detail.reply.administrator.shield'/>！</p></div>
		                            <div class="zjda_3">
		                            	<h5><fmt:message key='detail.questionerreply.evaluation'/>：</h5>
		                                <p>${scIssue.handlerScisCloseContent }</p>
		                            </div>
	                            </c:if>
	                            
	                            
	                        </div>
	                 </div>       
                 </c:if>   
                  
                 <!--其他答案-->
                 <c:if test="${scIssue.scisReplyNumber > 0 && empty scIssue.scisCloseScirId || scIssue.scisReplyNumber > 1  && !empty scIssue.scisCloseScirId}">
	                 <div class="qtda">
	                 	<h5 class="navbtn ngreen">
		                 	<c:if test="${scIssue.scisState == 2 }"><fmt:message key='detail.other.responses'/></c:if>
		                 	<c:if test="${scIssue.scisState != 2 }"><fmt:message key='detail.recommended.reply'/></c:if>
	                 	</h5>
	                    <!--1排-->
	                    <table width="100%" cellpadding="0" cellspacing="0" class="otherTable">
	                    	<%int num=0; String cla="qtda_nr";%>
		                    <c:forEach items="${scIssueReplyList}" var="scIssueReply">
		                    	<%num++; 
		                    	  if(num==1)
		                    	  	cla = "qtda_nr2";
		                    	  else
		                    	  	cla = "qtda_nr";
		                    	%>
		                    	
		                    	<tr>
		                    		<td class="<%=cla %>">
		                    			<div class="qtda_1">
			                            	<p><fmt:message key='detail.respondent'/>：${scIssueReply.sysUserinfo.suinName } | <fmt:message key='search.time'/>：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${scIssueReply.scirReplyTime }"/> </p>
			                            </div>
		
			                            <div class="qtda_2"><p>${scIssueReply.handlerScirContent}</p>
			                            </div>
			                            <c:if test="${!empty scIssueReply.scirAttachmentName}">
			                           		<span class="qtda_22"></span>
			                            </c:if>
			                            <div class="qtda_3">
			                            	<c:if test="${!empty scIssueReply.scirAttachmentName}">
			                            		<p><b><fmt:message key='detail.enclosure'/>：(</b><b><a href="<%=SysConfigInfo.FILE_URL_PATH %>${scIssueReply.scirAttachmentUrl }" title="${scIssueReply.scirAttachmentName }">${scIssueReply.scirAttachmentName }</a> )</b> <a href="<%=SysConfigInfo.FILE_URL_PATH %>${scIssueReply.scirAttachmentUrl }" class="download"><fmt:message key='detail.download'/></a>  </p><br/>
			                            	</c:if>
				                            <c:if test="${scIssue.scisState == 1 && isMe == true}">
				                            	<div id="${scIssueReply.scirId }">
					                            	<a href="javascript:void(0);" class="good_da button3" id="bt${scIssueReply.scirId }" onclick="javascript:showDiv(${scIssueReply.scirId },${scIssue.scisId })"><b><fmt:message key='detail.set.best.answer'/></b></a>
				                            	</div>
				                           </c:if>
			                            </div>
		                    		</td>
		                    	</tr>
		                   </c:forEach>
	                   </table>     
	                 </div>
	                 
	                 <!-- 分页 -->
					<div class="pages">
				       <c:if test="${pagination.totalPages>0}">
				    	<div style="float:right;">
				    		<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
				    	</div>
					   </c:if>
				    </div>
				    
                 </c:if> 
                 <!--其他答案结束-->
                 <!--回答框-->
                 <c:if test="${scIssue.scisState == 1 }">
		              <div class="answer">  
		                 <form action="<c:url value='/sc/createScIssueReply.htm'/>" method="post" enctype="multipart/form-data" id="form2" >
			                    <table width="98%" border="0">
			                      <tr>
			                        <td valign="top" width="50"><b class="t"><fmt:message key='detail.reply'/>：</b></td>
			                        	<td>
				                        	<input type="hidden" value="${scIssue.scisId}" name="scisId"/>
				                          	<textarea  class="an_ssk2" name="scirContent" cols="" rows="" id="scirContent">${scIssueReply.scirContent }</textarea>
				                          	<strong>*</strong><p><fmt:message key='detail.reply.max'/>&nbsp;&nbsp;<span style="color: red;">${scirContent }</span></p>
										</td>
			                      </tr>
			                      <tr>
			                        <td><b class="t"><fmt:message key='detail.enclosure'/>：</b></td>
			                        <td align="left">
			                        	<input type="file" name="scirAttachmentName2" id="scirAttachmentName2" style="float: left;"/><p style="color: red;">${scirAttachmentName }</p>  
									</td>
			                      </tr>
			                      <tr>
			                        <td>&nbsp;</td>
			                        <td>                    
			                            <a href="javascript:void(0);" class="tbtn cabtn" onclick="javascript:submit2();"><fmt:message key='consul.submit'/></a>
			                	 		<a href="javascript:void(0);" class="tbtn cabtn" onclick="javascript:reset('form2');"><fmt:message key='consul.reset'/></a>
									</td>
			                      </tr>
			                    </table>
						</form>
					</div>
				</c:if>
                <!--回答框结束-->
			</div> 
			<div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>