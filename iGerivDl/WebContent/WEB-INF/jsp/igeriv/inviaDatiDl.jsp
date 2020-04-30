<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>	
	div#filter { height:50px;}
</style>
<s:url id="smdUrl" namespace="/" action="ScambioDatiDlRpc.action"/>
<div style="width:100%; text-align:center">	
	<fieldset class="filterBolla"><legend style="font-size:110%"><s:text name="plg.esporta.dati.gestionale" /></legend>
		<s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA && (authUser.codGruppo eq constants.COD_GRUPPO_DL_RIFORNIMENTI || authUser.codGruppo eq constants.COD_GRUPPO_DL_RESA)">
			<s:if test="authUser.codGruppo eq constants.COD_GRUPPO_DL_RIFORNIMENTI">
				<div style="height:50px; margin-top:50px;">
					<input type="button" id="memorizza" value="<s:text name='plg.esporta.dati.rifornimenti'/>" align="center" style="align:center; width:200px;" onclick="javascript: setTimeout(function() {esportaRifornimenti();},10);"/>
				</div>
			</s:if>
			<s:elseif test="authUser.codGruppo eq constants.COD_GRUPPO_DL_RESA">
				<div style="height:50px; margin-top:50px;">
					<input type="button" id="memorizza1" value="<s:text name='plg.esporta.altri.dati'/>" align="center" style="align:center; width:200px;" onclick="javascript: setTimeout(function() {esportaAltriDati();},10);"/>
				</div>
			</s:elseif>
		</s:if>
		<s:else>
			<div style="height:50px; margin-top:50px;">
				<input type="button" id="memorizza" value="<s:text name='plg.esporta.dati.rifornimenti'/>" align="center" style="align:center; width:200px;" onclick="javascript: setTimeout(function() {esportaRifornimenti();},10);"/>
			</div>
			<div style="height:50px; margin-top:50px;">
				<input type="button" id="memorizza1" value="<s:text name='plg.esporta.altri.dati'/>" align="center" style="align:center; width:200px;" onclick="javascript: setTimeout(function() {esportaAltriDati();},10);"/>
			</div>
		</s:else>
	</fieldset>
</div>
<div style="width:100%; text-align:center; margin-top:30px">	
	<applet id="dlApplet" code="it.dpe.igeriv.web.applet.scaricoDatiDl.SendToFtpApplet" archive="sendToFtpApplet<s:text name="igeriv.version.timestamp"/>.jar, httpclient-4.0.1.jar, httpcore-4.0.1.jar, commons-net-2.0.jar,activation.jar, commons-logging-1.1.1.jar" name="<s:text name="igeriv.applet.invio.dati.gestionale"/>" width="1" height="1">
		<param name="type" value="application/x-java-applet;version=1.7">
	    <param name="codebase" value="${pageContext.request.contextPath}${appletDir}">
	    <param name="scriptable" value="true">
	    <param name="ftpUser" value="${ftpUser}">
		<param name="ftpPwd" value="${ftpPwd}">
		<param name="ftpDir" value="${ftpDir}">
		<param name="ftpServerUrl" value="${ftpServerUrl}">
		<param name="ftpPort" value="${ftpPort}">
		<param name="httpProxyServer" value="${httpProxyServer}">
		<param name="httpProxyPort" value="${httpProxyPort}">
		<param name="sessionId" value="${sessionId}">
		<param name="url" value="${urlDl}"> 
	</applet>	
</div>

