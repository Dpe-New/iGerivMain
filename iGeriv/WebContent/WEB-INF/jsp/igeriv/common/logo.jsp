<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<div id="logo"
	style="<s:if test="%{#request.sf eq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">margin-top:0px; margin-left:6px</s:if><s:else>margin-top:8px</s:else>">
	<a href="home.action"> <s:if
			test="%{#session['codFiegDl'] eq #application['CDL_CODE']}">
			<img id="igerivImg" src="/app_img/rodis.gif" alt="Cdl On Line"
				border="0" title="Cdl On Line - Home" />
		</s:if> <s:elseif
			test="%{#request.sf eq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
			<img id="igerivImg" src="/app_img/edismartlogo.gif"
				alt="<s:text name="gp.titolo" /> - <s:text name="gp.gestione.minicard" />"
				border="0" title="iGeriv - Home" />
		</s:elseif> <s:else>
			<img id="igerivImg" src="/app_img/igeriv.gif"
				alt="<s:text name="gp.titolo" /> - <s:text name="gp.gestione.minicard" />"
				border="0" title="iGeriv - Home" />
		</s:else>
	</a>
</div>