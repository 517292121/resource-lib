<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="zwGroupDiv">
	<div class="flxdh_2">
 		<span><fmt:message key='group.consultancy.classification'/></span>
       	<ul>
           	 <li><a href="<%=path %>/platform/scConsultation.htm?action=initConsultationView"><fmt:message key='repeat.all'/></a></li>	
             <c:forEach var="cType" items="${consultationTypeList}">
             <li><a href="<%=path %>/platform/scConsultation.htm?action=initConsultationTypeView&sctyId=${cType.sctyId }" title="${cType.sctyName }">${cType.subSctyName }</a></li>	
             </c:forEach>
        </ul>
	</div>
</div>
	