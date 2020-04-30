<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<div id="contentDiv" style="margin-top:0px; z-index:999999">					
	<a href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}/video/<s:text name="param"/>"
		 style="display:block;width:720px;height:480px"  
		 id="player"> 			
	</a> 
</div>