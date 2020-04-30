<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<div id="mainDiv" style="width:620px; text-align:center">
	<s:form id="MessaggioForm1" action="avviso_saveMessageEdicola.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
		<s:if test="messaggioRivendita != null">			
			<div class="messaggioDiv" style="text-align:center">
				<s:text name="messaggioRivendita.messaggioEscape"/>
			</div>
			<div style="text-align:left; width:600px">				
				<s:if test="messaggioRivendita.attachmentName1 != null && messaggioRivendita.attachmentName1 != ''">			
					<div style="float:left; text-align:left; width:150px; font-weight:bold"><s:text name="igeriv.allegati"/>:</div>
					<div class="required" style="float:left; height:20px; width:150px"><a href='${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggioRivendita.attachmentName1"/>' target="new"><img src="/app_img/attachment.png" border="0px" id="att1" style="border-style: none" title="<s:text name="igeriv.mostra.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a></div>
				</s:if>
				<s:if test="messaggioRivendita.attachmentName2 != null && messaggioRivendita.attachmentName2 != ''">
					<div class="required" style="float:left; height:20px; width:150px"><a href='${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggioRivendita.attachmentName2"/>' target="new"><img src="/app_img/attachment.png" border="0px" id="att2" style="border-style: none" title="<s:text name="igeriv.mostra.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a></div>
				</s:if>
				<s:if test="messaggioRivendita.attachmentName3 != null && messaggioRivendita.attachmentName3 != ''">
					<div class="required" style="float:right; height:20px; width:150px"><a href='${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggioRivendita.attachmentName3"/>' target="new"><img src="/app_img/attachment.png" border="0px" id="att3" style="border-style: none" title="<s:text name="igeriv.mostra.allegato"/>" alt="<s:text name="igeriv.allegato"/>" /></a></div>
				</s:if>
			</div>		
			<br><br>
			<s:if test="messaggioRivendita.messaggioLetto != 1">
				<div style="text-align:left; width:600px">
					<div style="float:left; width:300px; margin-top:20px; margin-left:80px; height:30px; text-align:center; font-size:120%;">		
						<s:text name="igeriv.segna.come.letto"/>&nbsp;&nbsp;&nbsp;&nbsp;<s:checkbox name="chk1" id="letto" cssClass="tableFields" />
					</div>
					<div id="messageDiv1" style="float:right; width:200px; height:30px; font-size:14"></div>
				</div>
			</s:if>				
			<s:hidden name="messaggioRivendita.codFiegDl" id="codFiegDl"/>
			<s:hidden name="messaggioRivendita.codEdicola"/>
			<s:hidden name="messaggioRivendita.dtMessaggio" id="dtMessaggio"/>
			<s:hidden name="messaggioLetto" id="msgLetto"/>			
		</s:if>			
</s:form>
</div>

