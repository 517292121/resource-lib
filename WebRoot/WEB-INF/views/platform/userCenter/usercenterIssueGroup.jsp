<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
				
					<div class="zwGroupDiv">
						<div class="flxdh_2">
			        		<span><fmt:message key='group.help.classification'/></span>
			                	<ul>
			                        	<li><a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView"><fmt:message key='repeat.all'/></a></li>
			                		<c:forEach var="scIssueType" items="${issueTypeList}">
			                        	<li><a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueTypeView&scitId=${scIssueType.scitId }" title="${scIssueType.scitName }">${scIssueType.subScitName }</a></li>
						  			</c:forEach>
			                    </ul>
			    		</div>
			    		<!-- 
					    <div class="flxdh_2">
			        		<span><h3>求助状态</h3></span>
			                	<ul>
					    			<li>
					    				<a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView&sctype=1">待解决</a>
				    				</li>
				    				<li>
					        			<a href="<%=path %>/platform/scIssue.htm?action=initPlatformLatestScIssueView">最新解决</a>
					        		</li>
			                    </ul>
			    		</div>
			    		 -->
					</div>
