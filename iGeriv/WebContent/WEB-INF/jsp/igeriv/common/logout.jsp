<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:if
	test="%{#request.sf eq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
	<div style="margin-right: 0px">
</s:if>

<a href="<s:url value="./logout.action"/>"
<%-- <a href="<s:url value="./j_spring_security_logout"/>" --%>
	style="font-size: 14px; font-weight: bold"> <s:if
		test="%{#request.sf eq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
		<img id="logoutImg" src="/app_img/logout_dvtd.jpg" alt="Logout"
			border="0" title="Logout" />
	</s:if> <s:else>
		<img id="logoutImg" src="/app_img/logout.gif" alt="Logout" border="0"
			title="Logout" />
	</s:else>
</a>
<s:if
	test="%{#request.sf eq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
	</div>
</s:if>