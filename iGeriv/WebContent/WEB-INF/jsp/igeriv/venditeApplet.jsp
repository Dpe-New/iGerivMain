<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>

<div
	style="text-align: center; position: absolute; top: 45%; width: 100%">
	<applet id="regCassaApplet"
		code="it.dpe.igeriv.web.applet.regcassa.RegistratoreCassaApplet"
		archive="regCassaApplet<s:text name="igeriv.version.timestamp"/>.jar, json-lib-2.3.jar, ezmorph-1.0.3.jar, commons-collections-3.1.jar, commons-lang-2.0.jar, commons-logging-1.1.1.jar, commons-beanutils-1.8.0.jar, commons-io-1.3.2.jar, commons-net-2.0.jar, httpclient-4.0.1.jar, httpcore-4.0.1.jar, spring-security-core-3.0.5.RELEASE.jar, mail-1.4.jar, activation.jar, guava-10.0.1.jar"
		name="<s:text name="igeriv.reg.cassa"/>" width="1" height="1"
		align=middle>
		<param name="type" value="application/x-java-applet;version=1.7">
		<param name="codebase"
			value="${pageContext.request.contextPath}${appletDir}">
		<param name="scheme" value="${pageContext.request.scheme}">
		<param name="serverName" value="${pageContext.request.serverName}">
		<param name="serverPort" value="${pageContext.request.serverPort}">
		<param name="context" value="${pageContext.request.contextPath}">
		<param name="scriptable" value="true">
		<param name="toSendDir" value="${registratoreCassa.pathLocaleFile}">
		<param name="userRegCassaLocalDir"
			value="<s:property value="%{#session.userRegCassaLocalDir}"/>">
		<param name="scontriniRegCassaLocalDir"
			value="<s:property value="%{#session.scontriniRegCassaLocalDir}"/>">
		<param name="fileNamePrefix"
			value="${registratoreCassa.fileNamePrefix}">
		<param name="fileNameDigitLength"
			value="${registratoreCassa.fileNameDigitLength}">
		<param name="codRegCassa" value="${registratoreCassa.codRegCassa}">
		<param name="millsTaskTimeout"
			value="${registratoreCassa.millsTaskTimeout}">
		<param name="nomeProcesso" value="${registratoreCassa.nomeProcesso}">
		<param name="iniFileContent"
			value="${registratoreCassa.contentFileIni}">
		<param name="abilitaLog" value="${registratoreCassa.abilitaLog}">
		<param name="mailFrom" value="${registratoreCassa.mailFrom}">
		<param name="mailTo" value="${registratoreCassa.mailTo}">
		<param name="logFileName" value="${registratoreCassa.logFileName}">
		<param name="logFileSize" value="${registratoreCassa.logFileSize}">
		<param name="smtpHost" value="${registratoreCassa.smtpHostC}">
		<param name="smtpUser" value="${registratoreCassa.smtpUserC}">
		<param name="smtpPwd" value="${registratoreCassa.smtpPwdC}">
		<param name="supportedOS"
			value="${registratoreCassa.sistemiOperativiSupportati}">
		<param name="binaryUnixLinux"
			value="${registratoreCassa.binaryUnixLinux}">
		<param name="binaryMac" value="${registratoreCassa.binaryMac}">
		<param name="binarySolaris" value="${registratoreCassa.binarySolaris}">
		<param name="codEdicola"
			value="<s:text name="authUser.codDpeWebEdicola"/>">
		<param name="millsTaskInterval"
			value="<s:property value="%{#application.millsTaskInterval}"/>">
	</applet>
</div>