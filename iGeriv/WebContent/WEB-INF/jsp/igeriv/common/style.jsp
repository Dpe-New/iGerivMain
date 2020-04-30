<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/igeriv_page_style<s:if test="%{#request.sf != ''}"><s:text name="%{#request.sf}"/></s:if>_<s:text name="igeriv.version.timestamp"/>.css" />