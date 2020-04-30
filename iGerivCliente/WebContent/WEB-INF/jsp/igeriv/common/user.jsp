<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<div style="font-size: 80%">
	<s:if test="authUser.tipoUtente == 1">
		<span style="font-family:century; width:100%; float:left"><s:property value="authUser.ragioneSocialeEdicola" /></span>&nbsp;<s:text name="dpe.utente"/>:&nbsp;<b><s:property value="authUser.codUtente" /></b>
		<span style="font-family:century; width:100%; float:left; font-size: 90%"><s:property value="authUser.indirizzoDlPrimaRiga" /> - <s:property value="authUser.localitaDlPrimaRiga" /></span>
	</s:if>
	<s:elseif test="authUser.tipoUtente == 2">
		<s:property value="authUser.nome" />&nbsp;<s:property value="authUser.cognome" />	
	</s:elseif>
	<s:else>
		<s:property value="authUser.ragioneSocialeDl" />&nbsp;<s:property value="authUser.indirizzoDlPrimaRiga" />&nbsp;<s:property value="authUser.localitaDlPrimaRiga" />
	</s:else>
</div>
