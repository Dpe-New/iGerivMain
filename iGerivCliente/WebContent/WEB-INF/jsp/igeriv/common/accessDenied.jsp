<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<br><br><br><br>
<div class="textSide" style="align:center; text-align:center;margin-left: auto; margin-right: auto; width: 500px;">
	<s:text name="dpe.user.unauthorized"/>
	<br>
	<br>
	<s:text name="dpe.user.unauthorized1"/>&nbsp; 
	<a href="<s:url value="./j_spring_security_logout"/>" style="font-size:14px; font-weight:bold"><s:text name="dpe.user.unauthorized2"/></a> 
	<s:text name="dpe.user.unauthorized3"/>
	<a href="javascript: history.back()" style="font-size:14px; font-weight:bold"><s:text name="dpe.user.unauthorized4"/></a> <s:text name="dpe.user.unauthorized5"/>	
	<br><br>
	<img src="/app_img/forbidden.jpg" width="100" height="100" align="top" border="0"/>
</div>	
