<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:if test="authUser.edicolaDeviettiTodis eq true">
	<div style="width: 450px;">
		<s:if
			test="%{#request.sf eq constants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS}">
			<!-- ticket 0000265	<div style="float:left"><a href="http://webtest.devietti.it/NewsEditoriali" target="_blank"><img id="newsDvtdImg" src="/app_img/NewsEdit_button.png" alt="News Editoriali" border="0" title="News Editoriali" /></a></div> -->
			<div style="float: left">
				<a href="http://newsedit.devietti.it" target="_blank"><img
					id="newsDvtdImg" src="/app_img/NewsEdit_button.png"
					alt="News Editoriali" border="0" title="News Editoriali" /></a>
			</div>

		</s:if>
</s:if>
<div style="float: left; font-size: 80%;">
	<s:if test="authUser.tipoUtente == 1">
		<span style="font-family: century; width: 100%; float: left"><s:property
				value="authUser.ragioneSocialeEdicola" /></span>
			<!-- 
			&nbsp;<s:text
			name="dpe.utente" />:&nbsp;<b><s:property
				value="authUser.codUtente" /></b>
			-->
		<span
			style="font-family: century; width: 100%; float: left; font-size: 90%">
			<!--	<s:property	value="authUser.indirizzoDlPrimaRiga" /> - <s:property value="authUser.localitaDlPrimaRiga" /> -->
			<s:text
			name="dpe.utente" />:&nbsp;<b><s:property
				value="authUser.codUtente" /></b>
		</span>
	</s:if>
	<s:elseif test="authUser.tipoUtente == 2">
		<s:property value="authUser.nome" />&nbsp;<s:property
			value="authUser.cognome" />
	</s:elseif>
	<s:else>
		<s:property value="authUser.ragioneSocialeDl" />&nbsp;<s:property
			value="authUser.indirizzoDlPrimaRiga" />&nbsp;<s:property
			value="authUser.localitaDlPrimaRiga" />
	</s:else>
</div>
<s:if test="authUser.edicolaDeviettiTodis eq true">
	</div>
</s:if>
