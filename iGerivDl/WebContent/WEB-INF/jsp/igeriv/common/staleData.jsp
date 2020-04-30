<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<div class="textSide" style="align:center; text-align:center;margin-left: auto; margin-right: auto; width: 500px;">	
	<s:text name="dpe.stale.data"/>
	<br>
	<br>
	<s:text name="dpe.stale.data1"/>&nbsp; 	
	<s:if test="%{#request.action != null && #request.action != ''}">
		<s:url id="url" action="%{#request.action}" />			
		<a href="%{url}%{#request.params}" style="font-size:14px; font-weight:bold"><s:text name="dpe.stale.data2"/></a>&nbsp;	
		<s:text name="dpe.stale.data3"/>&nbsp;<s:text name="dpe.stale.data5"/>&nbsp;<s:text name="dpe.stale.data6"/>&nbsp;<s:text name="dpe.stale.data7"/>
	</s:if>
	<s:else>
		<a href="javascript: history.back()" style="font-size:14px; font-weight:bold"><s:text name="dpe.user.unauthorized4"/></a> <s:text name="dpe.user.unauthorized5"/><s:text name="dpe.comma"/>	
		<s:text name="dpe.stale.data4"/>&nbsp;<s:text name="dpe.stale.data5"/>&nbsp;<s:text name="dpe.stale.data6"/>&nbsp;<s:text name="dpe.stale.data7"/>
		<br><br>
	</s:else>
	<br><br>
	<img src="/app_img/out_of_date.png" width="100" height="100" align="top" border="0"/>
</div>	
