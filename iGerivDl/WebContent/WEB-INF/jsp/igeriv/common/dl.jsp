<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<div id="dlDiv" style="width:220px; float:left; white-space:nowrap;">
	<div id="dlImgDiv" style="float:left; width:155px; text-align:left">
		<s:if test="authUser.tipoUtente == 1 || authUser.tipoUtente == 3">
			<s:if test="authUser.imgLogo != null && authUser.imgLogo != ''">
				<s:if test="authUser.urlDL != null && authUser.urlDL != ''">
					<a href="<s:text name="authUser.urlDL"/>" target="_new">
				</s:if>
					<img id="dlDivImg" src="/app_img/<s:property value="authUser.imgLogo"/>" style="vertical-align:top;" alt="<s:property value="authUser.ragioneSocialeDl" />" border="0" title="<s:property value="authUser.ragioneSocialeDl" /> - <s:property value="authUser.indirizzoAgenziaPrimaRiga" /> - <s:property value="authUser.localitaAgenziaPrimaRiga" />" />
				<s:if test="authUser.urlDL != null && authUser.urlDL != ''">
					</a>
				</s:if>	
			</s:if>
		</s:if>
	</div>
	<div id="alertMessageDiv" style="width:50px; font-size:80%; margin-left:5px; margin-top:10px; background:transparent; float:left; white-space:normal; text-align:center"></div>
</div>
