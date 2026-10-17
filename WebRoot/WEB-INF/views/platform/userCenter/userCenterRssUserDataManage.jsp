<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../pc/common/header.jsp"%>

	<script src="<%=basePath %>viewsjs/search/searchCommon.js" type=text/javascript></script>
	<script src="<%=basePath %>/viewsjs/search/json_sans_eval.js" type=text/javascript></script>
	<script type="text/javascript">
	
		function checkAll() {
			var check = document.getElementById("checkAll");
			var infoIds = document.getElementsByName("infoId");
			for(var i = 0; i < infoIds.length; i++) {
				infoIds[i].checked = check.checked; 
			}
		}
		function deleteThis(obj, id) {
			if(window.confirm("<fmt:message key='manage.sure.delete'/>")) {
				document.getElementById("ids").value = id + ",";
				document.getElementById("formn").submit();
			}
		}
		function deleteMany() {
			var infoIds = document.getElementsByName("infoId");
			var checkedIds = [];
			for(var i = 0; i < infoIds.length; i++) {
				if(infoIds[i].checked) {
					checkedIds[i] = infoIds[i].value;
				}
			}
			if(checkedIds.length == 0) {
				alert("<fmt:message key='user.choose.a.delete.options'/>");
			} else {
				if(window.confirm("<fmt:message key='manage.sure.delete'/>")) {
					document.getElementById("ids").value = checkedIds.toString();
					document.getElementById("formn").submit();
				}
			}
		}		
		function modifyRssData(obj, resourceId) {
			if(resourceId > 0){
				riss2.ajax.request("<%=basePath %>usercenter/rssUserData.htm?action=getRssDataById", "POST", {id: resourceId}, function() {
					document.getElementById("addBtn").style.display = "none";
					document.getElementById("modBtn").style.display = "block";
					document.getElementById("subWay").value = "updateRssData";
					//document.getElementById("rssbg").style.display = "block";
					var obj = eval('(' + riss2.ajax.xmlHttp.responseText + ')');
					document.getElementById("keyWord2").value = obj["rssDataSourceUrl"];
					document.getElementById("rssDataName2").value = obj["rssDataName"];
					document.getElementById("rssDataDisplayNum2").value = obj["rssDataDisplayNum"];
					document.getElementById("rssDataId2").value = obj["id"];
					document.getElementById("rssDataOrder2").value = obj["rssDataOrder"];
				});
			}else{
				document.getElementById("modBtn").style.display = "none";
				document.getElementById("addBtn").style.display = "block";
				document.getElementById("subWay").value = "addRssData";
				//document.getElementById("rssbg").style.display = "block";
				document.getElementById("keyWord2").value = "";
				document.getElementById("rssDataName2").value = "";
				document.getElementById("rssDataDisplayNum2").value = "";
				document.getElementById("rssDataId2").value = "";
				document.getElementById("rssDataOrder2").value = "";
			}
			
			layer.open({
			  	type: 1,
			  	title: false,
			  	closeBtn: 1,
			  	area: ['530px', '360px'],
			  	shadeClose: true,
			  	content: $('#rssbg')
			});
		}
		riss2.displaySelecteRssResource = {
			RSS_REQUEST_DISPLAY_HREF: "<%=basePath %>usercenter/rssFeedLink.htm",
			preSelect: null,
			loadedRssData: [],
			//CONTENT_EXTRACT_BORDER: "blue 1px solid",
			//CONTENT_ALL_EXTRACT_BORDER: "gray 1px solid",
			//CONTENT_CONTRACT_BORDER: "",
			//CONTENT_EXTRACT_HEIGHT: "70px",
			//CONTENT_CONTRACT_HEIGHT: "14px",
			preContent: null,
			//allContentCurrentStatus: "extract",
			//allContentDisplayBtns: (function() {return {contentExtract: $("contentExtractBtn"), contentContract: $("contentContractBtn") };})(),
			setObjsCss: function(obj, cssObj) {
				if(obj && cssObj) {
					for(var i in cssObj) {
						obj.style[i] = cssObj[i];
					}
				}
			},
			// 展开RSS feed
			rssEntryDisplayContentClick: function(obj, desc) {
				var that = riss2.displaySelecteRssResource;
				if(obj.className === "rssentrydisplaycontent1") {
					obj.className = "rssentrydisplaycontent";
				} else {
					obj.className = "rssentrydisplaycontent1";
				}
				if(that.preContent && that.preContent !== obj)	 {
					that.preContent.className = "rssentrydisplaycontent"
				}
				that.preContent = obj;	
			},
			bindEvent: function(obj, eventName, func) {
				if(document.all) {
					obj.attachEvent("on" + eventName, func);
				} else {
					obj.addEventListener(eventName, func, false);
				}
			},
			rebindEvent: function(obj, eventName, func) {
				if(document.all) {
					obj.detachEvent("on" + eventName, func);
				} else if(document.removeEventListener) {
					obj.removeEventListener(eventName, func, false);
				} else {
					//obj["on" + eventName] = "";
				}
			},
			contentMouseover: function(obj) {
				var that = riss2.displaySelecteRssResource;
				this.style.border = that.CONTENT_EXTRACT_BORDER;
			},
			contentMouseout: function(obj) {
				var that = riss2.displaySelecteRssResource;
				this.style.border = that.CONTENT_ALL_EXTRACT_BORDER;
			},
			/*rssEntriesDisplayAllBtnClick: function(obj) {
				var that = riss2.displaySelecteRssResource;
				that.setObjsCss(obj, {color: "black", cursor: "default"});
 				if(obj == that.allContentDisplayBtns["contentExtract"]) {
 					var contents = document.getElementsByName("rssContentDisplayDiv");
 					if(contents && contents.length > 0) {
 						for(var i = 0; i < contents.length; i++) {
 							that.setObjsCss(contents[i], {height: that.CONTENT_EXTRACT_HEIGHT, 
 								border: that.CONTENT_ALL_EXTRACT_BORDER});
 							that.bindEvent(contents[i], "mouseover", that.contentMouseover);	
 							that.bindEvent(contents[i], "mouseout", that.contentMouseout);	
 							contents[i].onclick = "";
 						}
 					}
					that.setObjsCss(that.allContentDisplayBtns["contentContract"], {color: "blue", cursor: "pointer"});
				} else {
					var contents = document.getElementsByName("rssContentDisplayDiv");
 					if(contents && contents.length > 0) {
 						for(var i = 0; i < contents.length; i++) {
 							that.setObjsCss(contents[i], {height: that.CONTENT_CONTRACT_HEIGHT, 
 								border: ""});
 							that.rebindEvent(contents[i], "mouseover", that.contentMouseover);	
 							that.rebindEvent(contents[i], "mouseout", that.contentMouseout);	
 							contents[i].setAttribute("onclick", "riss2.displaySelecteRssResource.rssEntryDisplayContentClick(this)");
 						}
					}
					that.setObjsCss(that.allContentDisplayBtns["contentExtract"], {color: "blue", cursor: "pointer"});
				}
			},	*/		
			// 展开RSS源
			display: function(event, obj, id) {
				var event = event || window.event;
				var target = event.srcElement || event.target;
				if(target.tagName === "A") return;
				var that = riss2.displaySelecteRssResource;
				var displayDiv =document.getElementById(obj.id + "Div");
				var fontDiv =document.getElementById(obj.id);
				var extractRssData = function() {
					if(displayDiv.style.display === "block") {
						displayDiv.parentNode.parentNode.style.display = "none";
						displayDiv.style.display = "none";
						fontDiv.innerHTML = "<fmt:message key='manage.display.list'/>";
					} else {
						displayDiv.parentNode.parentNode.style.display = "";
						displayDiv.parentNode.style.display = "";
						//displayDiv.parentNode.colSpan = 5;
						displayDiv.style.display = "block";
						fontDiv.innerHTML = "<fmt:message key='manage.pick.up.a.list'/>";
					}
					if(that.preSelect && that.preSelect !== displayDiv) {
						that.preSelect.parentNode.parentNode.style.display = "none";
						that.preSelect.style.display = "none";
					}
					that.preSelect = displayDiv;
				}
				for(var i = 0; i < that.loadedRssData.length; i++) {
					if(obj.id === that.loadedRssData[i]) {
						extractRssData();
						return;
					}
				}
				riss2.ajax.request(that.RSS_REQUEST_DISPLAY_HREF, "POST", {rssFeedId: id}, function() {
					var contentStr = '';
					if(riss2.ajax.xmlHttp.responseText === "error") {
						contentStr += '<div class="rssentrydisplaycontent">';
						contentStr += "<span class='rssentrydisplaytitle'><fmt:message key='manage.source.parsing.error'/>!!";
						contentStr += '</span></div>';
						displayDiv.style.height = "20px";
					} else {
						var rssFeeds = jsonParse(riss2.ajax.xmlHttp.responseText);
						if(rssFeeds.length > 0) {
							that.loadedRssData.push(obj.id);
							for(var i = 0; i < rssFeeds.length; i++) {
								var feed = rssFeeds[i];
								contentStr += '<div class="rssentrydisplaycontent" onclick="riss2.displaySelecteRssResource.rssEntryDisplayContentClick(this);" name="rssContentDisplayDiv">';
								contentStr += '<span class="rssentrydisplaytitle"><a href="'+feed['link']+'" target="_blank" title="'+feed['title']+'">'+feed['title'].substring(0, 30)+'</a>&nbsp;-&nbsp;</span>';
								//contentStr += '<span class="rssentrydisplaytitledesc">'+feed['desc']+'&nbsp;-&nbsp;</span>';	
								contentStr += '<span class="rssentrydisplaytitletime">'+feed['publishDate']+'</span>'
								contentStr += '</div>';					
							}
						} else {
							contentStr += '<div class="rssentrydisplaycontent">';
							contentStr += "<span class='rssentrydisplaytitle'><fmt:message key='manage.source.parsing.error'/>";
							contentStr += '</span></div>';
							displayDiv.style.height = "20px";
						}
					}
					extractRssData();
					displayDiv.innerHTML = contentStr;
				});
			}
		}
		
		if('${error }') {
			alert("<fmt:message key='manage.correct.number'/>!");
		}
		function xclose(id){
			document.getElementById(id).style.display="none";		
		}
		function addOrUpdate(){
			if(document.getElementById('keyWord2').value.trim() == '') {
				alert("<fmt:message key='manage.source.address'/>"); 
				document.getElementById('keyWord2').focus();
				return ;
			} else { 
				document.getElementById('form2').submit(); 
			}
		}
	</script>
	
	
		
		<div id="rssbg" style="display: none;">
	 		<div class="tandiv">
	 			<form action="<%=basePath %>usercenter/rssUserData.htm" id="form2" method="post">
	 				<!-- 区分新增和修改 默认新增-->
	 				<input type="hidden" id="subWay" name="action" value="addRssData"/>
	 				<table cellspacing="15" cellpadding="0" border="0" class="tanTable">
	 					<tr>
	 						<td align="center" colspan="2"><h2 class="tanH2"><fmt:message key='manage.subscribe'/></h2></td>
						</tr>
	 					<tr>
	 						<td align="right"><fmt:message key='manage.source.address'/>：</td>
	 						<td align="left"><input class="tanInput" name="rssDataSourceUrl" value="${rss.rssDataSourceUrl }" id='keyWord2' type="text" /></td>
						</tr>
	 					<tr>
	 						<td align="right">RSS<fmt:message key='manage.name'/>：</td>
	 						<td align="left"><input class="tanInput" name="rssDataName" value="${rss.rssDataName }" type="text" id="rssDataName2"/></td>
						</tr>
	 					<tr>
	 						<td align="right">RSS<fmt:message key='manage.display.list.size'/>：</td>
	 						<td align="left"><input class="tanInput small"  name="rssDataDisplayNum1" type="text" id="rssDataDisplayNum2"/></td>
						</tr>
	 					<tr>
	 						<td align="right"><fmt:message key='manage.sort.number'/>：</td>
	 						<td align="left"><input class="tanInput small"  name="rssDataOrder1" type="text" id="rssDataOrder2"/></td>
						</tr>
	 					<tr>
	 						<td>&nbsp;</td>
	 						<td align="left">
		 						<a href="javascript:void(0);" id="modBtn" class="tbtn cabtn" onclick="addOrUpdate();"><fmt:message key='list.modify'/></a>
			                	<a href="javascript:void(0);" id="addBtn" class="tbtn cabtn" onclick="addOrUpdate();"><fmt:message key='manage.add.to'/></a>
	                		</td>
						</tr>
	 				</table>
	               	<input type="hidden" name="currentPage" value="${pagination.currentPage }"/> 			
	               	<input type="hidden" name="rssDataId" id="rssDataId2"/> 			
	 			</form>
	 		</div>
	 	</div>
		
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow" >
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key='edit.current.location'/>：
		  			<a href="<%=path %>/index.htm" class="navigation"><fmt:message key='home.page'/></a> >> 
			  		<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='user.conter'/></a> >> 
			  		<fmt:message key='left.subscribe'/>
		  			</b>
		  			<div class="tbtns">
		  				<a class="tbtn mgr" onclick="modifyRssData(this, 0)"><fmt:message key='manage.add.to'/></a>
						<a class="tbtn" onclick="deleteMany();"><fmt:message key='user.deleting'/></a>
					</div>
	  			</div>
		  
				<form action="<%=path %>/usercenter/rssUserData.htm?action=delRssData" id="formn" method="post">
					<input type="hidden" name="currentPage" value="${pagination.currentPage }"/>
					<input type="hidden" name="ids" value="" id="ids"/>
					<input type="hidden" name="display" value="block"/>
				</form>

				<div class="ucinfo_div">
					<table border="0" cellpadding="0" cellspacing="0" class="zxlbk listTable">
				          <tr id="tr1">
				          	<td class="ltabtd5" align="center"><input type="checkbox" onclick="checkAll();" id="checkAll"></input></td>
				          	<td class="ltabtd45" align="left"><fmt:message key='manage.name'/></td>
				          	<td class="ltabtd20" align="center"><fmt:message key='user.add.time'/></td>
				          	<td class="ltabtd30" align="center"><fmt:message key='user.operation'/></td>
				          </tr>
				          
				          <c:choose>
				          	<c:when test="${!empty result}">
				          		<c:forEach var="is" items="${result}" varStatus="status">
						          	  <tr>
							            <td class="ltabtd5" align="center"><input type="checkbox" name="infoId" id="brId${status.count }" value="${is.rssDataId }"></td>
							            <td class="ltabtd45" align="left">
							            		${is.rssDataName }<br/>
							            	<span class="sbylist" name="rssDisplayTitleSpan" onclick="riss2.displaySelecteRssResource.display(event, this, '${is.rssDataId }')" id="selecteRssResource${status.count }">
							            		<fmt:message key='manage.display.list'/>
						            		</span>
							            </td>
							            <td class="ltabtd20" align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${is.rssDataAddDate}"/></td>
							            <td class="ltabtd30"align="center">
							            	<a class="isbtn right mgr"onclick="deleteThis(this, ${is.rssDataId })"><fmt:message key='edit.delete'/></a>
							            	<a class="isbtn right mgr" onclick="modifyRssData(this, ${is.rssDataId })"><fmt:message key='list.modify'/></a>
							            </td>
							          </tr>
							         <!-- 显示详情 -->
							      	 <tr class="detailTr" style="display: none;">
				                      	<td colspan="4" style="display: none;">
				                      		<div class="rssresourcedisplayclass" id="selecteRssResource${status.count }Div"></div>
				                      	</td>
				                      </tr>
			                    </c:forEach>
				          </c:when>
				          <c:otherwise>
				          		<tr id="tr2"  bgcolor="#FFFFFF" class="lb_bottom" style="height:30px;">
						            <td align="center" colspan="6">
										<span class="noList">
							    			<span><fmt:message key='manage.no.related.subscriptions'/></span>
						    			</span>
									</td>
						        </tr>
	                      </c:otherwise>
			          </c:choose>
					</table>
					<!-- 分页 -->
					<div class="pages">
				       <c:if test="${pagination.totalPages>0}">
				    		<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
					   </c:if>
				    </div>
				</div>
			</div> 
			<div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
	<script type="text/javascript">
		function displayShow(obj, id) {
				var that = riss2.displaySelecteRssResource;
				var displayDiv = document.getElementById(obj.id + "Div");
				var fontDiv =document.getElementById(obj.id);
				var extractRssData = function() {
					if(displayDiv.style.display === "block") {
						displayDiv.parentNode.parentNode.style.display = "none";
						displayDiv.style.display = "none";
						fontDiv.innerHTML = "<fmt:message key='manage.display.list'/>";
					} else {
						displayDiv.parentNode.parentNode.style.display = "";
						displayDiv.parentNode.style.display = "";
						//displayDiv.parentNode.colSpan = 5;
						displayDiv.style.display = "block";
						fontDiv.innerHTML = "<fmt:message key='manage.pick.up.a.list'/>";
					}
					if(that.preSelect && that.preSelect !== displayDiv) {
						that.preSelect.parentNode.parentNode.style.display = "none";
						that.preSelect.style.display = "none";
					}
					that.preSelect = displayDiv;
				}
				for(var i = 0; i < that.loadedRssData.length; i++) {
					if(obj.id === that.loadedRssData[i]) {
						extractRssData();
						return;
					}
				}
				riss2.ajax.request(that.RSS_REQUEST_DISPLAY_HREF, "POST", {rssFeedId: id}, function() {
					var contentStr = '';
					if(riss2.ajax.xmlHttp.responseText === "error") {
						contentStr += '<div class="rssentrydisplaycontent">';
						contentStr += '<span class="rssentrydisplaytitle"><fmt:message key='manage.source.parsing.error'/>!!';
						contentStr += '</span></div>';
						displayDiv.style.height = "20px";
					} else {
						var rssFeeds = jsonParse(riss2.ajax.xmlHttp.responseText);
						if(rssFeeds.length > 0) {
							that.loadedRssData.push(obj.id);
							for(var i = 0; i < rssFeeds.length; i++) {
								var feed = rssFeeds[i];
								contentStr += '<div class="rssentrydisplaycontent" onclick="riss2.displaySelecteRssResource.rssEntryDisplayContentClick(this);" name="rssContentDisplayDiv">';
								contentStr += '<span class="rssentrydisplaytitle"><a href="'+feed['link']+'" target="_blank" title="'+feed['title']+'">'+feed['title'].substring(0, 30)+'</a>&nbsp;-&nbsp;</span>';
								contentStr += '<span class="rssentrydisplaytitletime">'+feed['publishDate']+'</span>'
								contentStr += '</div>';					
							}
						} else {
							contentStr += '<div class="rssentrydisplaycontent">';
							contentStr += '<span class="rssentrydisplaytitle">'+"<fmt:message key='manage.no.data.or.no.login'/>";
							contentStr += '</span></div>';
							displayDiv.style.height = "20px";
						}
					}
					extractRssData();
					displayDiv.innerHTML = contentStr;
				});
			}
		displayShow(document.getElementById('selecteRssResource1'), document.getElementById('brId1').value)
	</script>
<%@ include file="../pc/common/footer.jsp"%>