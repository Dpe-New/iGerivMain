<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:if test="env eq 'CDL'">
	<div id="logo"><a href="${pageContext.request.contextPath}"><img src="/app_img/rodis.gif" alt="Cdl On Line" border="0"/></a></div>
</s:if>
<s:else>
	<div id="logo"><a href="/"><img src="/app_img/igeriv.gif" alt="iGeriv" border="0"/></a></div>
</s:else>