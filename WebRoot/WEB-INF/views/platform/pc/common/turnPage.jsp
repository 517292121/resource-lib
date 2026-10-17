<%@ page  contentType="text/html; charset=UTF-8"%>
<c:if test="${pagination!=null&&pagination.totalPages>1}">
<div class="p_bar pBar">
	<!-- <a class="p_total">&nbsp;${pagination.currentPage+1}/${pagination.totalPages}&nbsp;</a> -->
	<c:if test="${pagination!=null}">					
		<c:if test="${pagination.totalPages>0}">	
			<c:if test="${pagination.currentPage>0}">
				<a href="<c:url value="${pageUrl}&currentPage=0"/>"><fmt:message key='home.page'/></a>		
        		<a href="<c:url value="${pageUrl}&currentPage=${pagination.currentPage-1}"/>">&lt;</a>
        	</c:if>
        	<c:choose>
        	<c:when test="${pagination.totalPages<=9}">
       		<c:forEach begin="1" end="${pagination.totalPages}" var="page">
       			<c:choose>
       			<c:when test="${pagination.currentPage+1!=page}"> 
       				<a href="<c:url value="${pageUrl}&currentPage=${page-1}"/>" class="p_num"><c:out value="${page}"/></a>
       			</c:when>
       			<c:otherwise> 
					<a href='javascript:void(0);' id="curr"><c:out value="${page}"/></a>
       			</c:otherwise>
       			</c:choose>
       		</c:forEach>
       		</c:when>
       		<c:otherwise>
        		<c:choose>
        		<c:when test="${pagination.currentPage<5}">
        		<c:forEach begin="1" end="9" var="page">
        			<c:choose>
        			<c:when test="${pagination.currentPage+1!=page}"> 
        				<a href="<c:url value="${pageUrl}&currentPage=${page-1}"/>"><c:out value="${page}"/></a>
        			</c:when>
        			<c:otherwise> 
						<a href='javascript:void(0);' id="curr"><c:out value="${page}"/></a>
        			</c:otherwise>
        			</c:choose>
        		</c:forEach>
        		</c:when>
        		<c:otherwise>
        			<c:choose>
	        		<c:when test="${pagination.totalPages>pagination.currentPage+4}">
	        		<c:forEach begin="${pagination.currentPage-3}" end="${pagination.currentPage+5}" var="page">
	        			<c:choose>
	        			<c:when test="${pagination.currentPage+1!=page}"> 
	        				<a href="<c:url value="${pageUrl}&currentPage=${page-1}"/>"><c:out value="${page}"/></a>
	        			</c:when>
	        			<c:otherwise> 
							<a href='javascript:void(0);' id="curr"><c:out value="${page}"/></a>
	        			</c:otherwise>
	        			</c:choose>
	        		</c:forEach>
	        		</c:when>
	        		<c:otherwise>
		        		<c:forEach begin="${pagination.totalPages-9}" end="${pagination.totalPages}" var="page">
			        		<c:choose>
		        			<c:when test="${pagination.currentPage+1!=page}"> 
		        				<a href="<c:url value="${pageUrl}&currentPage=${page-1}"/>"><c:out value="${page}"/></a>
		        			</c:when>
		        			<c:otherwise> 
								<a href='javascript:void(0);' id="curr"><c:out value="${page}"/></a>
		        			</c:otherwise>
		        			</c:choose>
		        		</c:forEach>				        		
	        		</c:otherwise>
	        		</c:choose>
        		</c:otherwise>
        		</c:choose>
       		</c:otherwise>
       		</c:choose>
     		
        	<c:if test="${pagination.currentPage+1<pagination.totalPages}">        		
        		<a href="<c:url value="${pageUrl}&currentPage=${pagination.currentPage+1}"/>">&gt;</a>
				<a href="<c:url value="${pageUrl}&currentPage=${pagination.totalPages-1}"/>"><fmt:message key='shadowe'/></a>
        	</c:if>
			<span><fmt:message key='share'/>${pagination.totalResults}<fmt:message key='bar.record'/></span>
			<!-- <a  class="input">
			<input name="custompage" onkeydown="if(event.keyCode==13) {window.location='<c:url value="${pageUrl}&currentPage="/>'+(this.value-1); return false;}" type="text">
			</a> -->
       	</c:if>	
	</c:if>       
	<p class="clear" />
</div>
</c:if>