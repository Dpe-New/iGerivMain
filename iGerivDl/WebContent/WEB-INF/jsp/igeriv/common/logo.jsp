<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<div id="logo" style="margin-top:8px">
	<a href="home.action">
		<s:if test="%{#session['codFiegDl'] eq #application['CDL_CODE']}">
			<img id="igerivImg" src="/app_img/rodis.gif" alt="Cdl On Line" border="0" title="Cdl On Line - Home"/>
		</s:if>
		<s:else>
			<img id="igerivImg" src="/app_img/igeriv.gif" alt="<s:text name="gp.titolo" /> - <s:text name="gp.gestione.minicard" />" border="0" title="iGeriv - Home"/>
		</s:else>
	</a>
</div>