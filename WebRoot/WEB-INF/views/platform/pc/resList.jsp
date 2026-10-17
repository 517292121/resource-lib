<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/platform/reslist.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/myPager.js"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/reslist.js"></script>
<link rel="stylesheet" href="<%=basePath %>/wtree/dtree.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>/wtree/dtree.js"></script>
<!-- jQuery滚动执行动画插件FadeThis -->
<script type="text/javascript" src="<%=path %>/js/jquery.fadethis.js"></script>

<p id="cntline"></p>
<div class="main booklist">
	<div class="pleft">
		<c:if test="${!empty storeList }">
		<div class="resclass">
			<p class="slide-left"><fmt:message key="repeat.resource.library"/></p>
			<ul id="ku">
				<c:forEach items="${storeList }" var="v" varStatus="s">
			     <li id="ku_${v[0] }"><a href="javascript:rmstQuery(${v[0] });">${v[1] }</a></li>
				</c:forEach>
			</ul>
		</div>
		</c:if>
		<c:if test="${groupA > 0 }">
		<div class="resclass">
			<p class="slide-bottom">${metaStore.rmstGroupOne }</p>
			<ul>
			     <script type="text/javascript">
			           var d1 = new dTree("d1");
				       d1.add(0,-1,"<fmt:message key='repeat.all.classification'/>","javascript:rdtyQuery(0, 1);","","");
			           <c:forEach items="${groupList }" var="v" varStatus="s">
			           	  <c:if test="${v.rdtyType == 1 }">
			           	  	<c:choose>
		    					<c:when test='<%=lang == null || language.equals("zh") %>'>
		    						d1.add("${v.rdtyId }","${empty v.rdtyPid ? 0 : v.rdtyPid }","${v.rdtyName }","javascript:rdtyQuery(${v.rdtyId }, 1);","","");
		    					</c:when>
		    					<c:otherwise>
		    						d1.add("${v.rdtyId }","${empty v.rdtyPid ? 0 : v.rdtyPid }","${v.rdtyZwName }","javascript:rdtyQuery(${v.rdtyId }, 1);","","");
		    					</c:otherwise>
		    				</c:choose>
			           	  </c:if>
			           </c:forEach>
			           document.write(d1);
			     </script>
			</ul>
		</div>
		</c:if>
		<c:if test="${groupB > 0 }">
		<div class="resclass">
			<p class="slide-left">${metaStore.rmstGroupTwo }</p>
			<ul>
			     <script type="text/javascript">
			           var d2 = new dTree("d2");
				       d2.add(0,-1,"<fmt:message key='repeat.all.classification'/>","javascript:rdtyQuery(0, 2);","","");
			           <c:forEach items="${groupList }" var="v" varStatus="s">
			           	  <c:if test="${v.rdtyType == 2 }">
			           		<c:choose>
		    					<c:when test='<%=lang == null || language.equals("zh") %>'>
		    						d2.add("${v.rdtyId }","${empty v.rdtyPid ? 0 : v.rdtyPid }","${v.rdtyName }","javascript:rdtyQuery(${v.rdtyId }, 2);","","");
		    					</c:when>
		    					<c:otherwise>
		    						d2.add("${v.rdtyId }","${empty v.rdtyPid ? 0 : v.rdtyPid }","${v.rdtyZwName }","javascript:rdtyQuery(${v.rdtyId }, 2);","","");
		    					</c:otherwise>
		    				</c:choose>
			           	  </c:if>	  
			           </c:forEach>
			           document.write(d2);
			     </script>
			</ul>
		</div>
		</c:if>
		<c:if test="${groupC > 0 }">
		<div class="resclass">
			<p class="slide-bottom">${metaStore.rmstGroupThree }</p>
			<ul>
			     <script type="text/javascript">
			           var d3 = new dTree("d3");
				       d3.add(0,-1,"<fmt:message key='repeat.all.classification'/>","javascript:rdtyQuery(0, 3);","","");
			           <c:forEach items="${groupList }" var="v" varStatus="s">
			           	  <c:if test="${v.rdtyType == 3 }">
			           		<c:choose>
		    					<c:when test='<%=lang == null || language.equals("zh") %>'>
		    						d3.add("${v.rdtyId }","${empty v.rdtyPid ? 0 : v.rdtyPid }","${v.rdtyName }","javascript:rdtyQuery(${v.rdtyId }, 3);","","");
		    					</c:when>
		    					<c:otherwise>
		    						d3.add("${v.rdtyId }","${empty v.rdtyPid ? 0 : v.rdtyPid }","${v.rdtyZwName }","javascript:rdtyQuery(${v.rdtyId }, 3);","","");
		    					</c:otherwise>
		    				</c:choose>
			           	  </c:if>	  
			           </c:forEach>
			           document.write(d3);
			     </script>
			</ul>
		</div>
		</c:if>
		<c:if test="${!empty subjectClass }">
		<%-- <div class="resclass">
			<p class="slide-left"><fmt:message key='repeat.list.mode'/></p>
			<ul>
			     <script type="text/javascript">
			           var d4 = new dTree("d4");
				       d4.add(0,-1,"<fmt:message key='repeat.all.classification'/>","javascript:rstyQuery(0);","","");
			           <c:forEach items="${subjectClass }" var="v" varStatus="s">
			           		d4.add("${v.rstyId }","${empty v.rstyPid ? 0 : v.rstyPid }","${v.rstyName }","javascript:rstyQuery(${v.rstyId });","","");
			           </c:forEach>
			           document.write(d4);
			     </script>
			</ul>
		</div> --%>
		</c:if>
	</div>
	
	
	<div class="pright">
		<ul class="nav" id="chars">
			<a href="javascript:charQuery('');" id="all" style="margin-left: 0px" class="active">ALL</a>
			<a href="javascript:charQuery('A');" >A</a>
			<a href="javascript:charQuery('B');" >B</a>
			<a href="javascript:charQuery('C');" >C</a>
			<a href="javascript:charQuery('D');" >D</a> 
			<a href="javascript:charQuery('E');" >E</a> 
			<a href="javascript:charQuery('F');" >F</a> 
			<a href="javascript:charQuery('G');" >G</a> 
			<a href="javascript:charQuery('H');" >H</a> 
			<a href="javascript:charQuery('I');" >I</a> 
			<a href="javascript:charQuery('J');" >J</a> 
			<a href="javascript:charQuery('K');" >K</a> 
			<a href="javascript:charQuery('L');" >L</a> 
			<a href="javascript:charQuery('M');" >M</a> 
			<a href="javascript:charQuery('N');" >N</a> 
			<a href="javascript:charQuery('O');" >O</a> 
			<a href="javascript:charQuery('P');" >P</a> 
			<a href="javascript:charQuery('Q');" >Q</a> 
			<a href="javascript:charQuery('R');" >R</a> 
			<a href="javascript:charQuery('S');" >S</a> 
			<a href="javascript:charQuery('T');" >T</a> 
			<a href="javascript:charQuery('U');" >U</a> 
			<a href="javascript:charQuery('V');" >V</a> 
			<a href="javascript:charQuery('W');" >W</a> 
			<a href="javascript:charQuery('X');" >X</a> 
			<a href="javascript:charQuery('Y');" >Y</a> 
			<a href="javascript:charQuery('Z');" >Z</a>  
			<li class="clear"></li>
		</ul>
		
		<ul class="nav" id="restype"></ul>
		<ul class="toolbar nav">
			<li id="l">
				<a href="javascript:void(0);" class="active" id="1"><fmt:message key="repeat.list.mode"/></a>
				<a href="javascript:void(0);" id="2"><fmt:message key="repeat.detailed.pattern"/></a>
				<a href="javascript:void(0);" id="3"><fmt:message key="repeat.table.mode"/></a>
			</li>
			<li id="r">
				<span><fmt:message key="res.sort.order"/>：</span>
				<a href="javascript:sortQuery('time');" class="active"><fmt:message key="repeat.entry.time"/></a>
				<a href="javascript:sortQuery('rsdaLooknum');" style="margin-right: 0px;"><fmt:message key="res.visit.frequency"/></a>
			</li>
			<p class="clear">
		</ul>
		
		<div class="listbox">
			<div id="box">
				<ul id="res1">
					<div class="jz"><img src="<%=basePath %>/images/platform/loading.gif"></div>
				</ul>
				<ul id="res2"></ul>
				<ul id="res3"></ul>
			</div>
		</div>
	</div>
	<p class="clear">
</div>

<input type="hidden" id="_rmstId" value="${numId }">
<input type="hidden" id="_rltyId" value="${rltyId }">
<input type="hidden" id="_tchar" value="${tchar }">
<input type="hidden" id="_sort" value="${sort }">
<input type="hidden" id="_rdtyAId" value="${rdtyAId }">
<input type="hidden" id="_rdtyBId" value="${rdtyBId }">
<input type="hidden" id="_rdtyCId" value="${rdtyCId }">
<input type="hidden" id="_rstyId" value="${rstyId }">
<input type="hidden" id="_currentPage" value="${currentPage }">
<%@ include file="common/footer.jsp" %>
