<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
<style>
		div#filter { height:30px;} 
</style> 
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="MessageForm" action="email_sendEmailRivendita.action" method="POST" enctype="multipart/form-data" theme="igeriv" validate="true" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width:100%">
		<div id="mainDiv" style="width:100%; margin-left:auto; margin-right:auto">	
			<div style="width:600px; margin-left:auto; margin-right:auto">	
				<div class="required">
					<div style="float:left; width:90px;"><s:text name="igeriv.destinatari" /></div>
					<div style="float:left; width:20px;">					
						<s:if test='destinatari == null'>
							<img id="destinatariImg" src="/app_img/recepients.gif" width="20" height="20" alt="<s:text name="igeriv.scegli.destinatari" />" border="0" title="<s:text name="igeriv.scegli.destinatari" />" style="cursor:pointer"/>
						</s:if>
						<s:else>
							&nbsp;
						</s:else>
					</div>
					<div style="float:right; width:465px;"><s:textfield id="destinatari" name="destinatari" cssStyle="width: 465px" cssClass="tableFields" disabled="true"/></div>				
				</div>			 
				<div style="width:400px; heigth:300px; margin-top:30px">
					<div id="innerDiv"></div>											
				</div>	
				<div style="width: 600px;">
					<div class="required" style="height:25px">
						<div style="float:left; width:100px;"><s:text name="igeriv.allegato" /></div>
						<div style="float:left; width:220px;"><s:file name="attachment1" label="File" /></div>			
						<div style="float:right; width:220px; text-align:right; vertical-align:middle;"><font color="red"><span id="err_attachment1"><s:fielderror><s:param>attachment1</s:param></s:fielderror></span></font></div>		
					</div>	
					<div class="required" style="width:600px; height:25px; text-align:center; margin-top:30px">
						<input type="button" value="<s:text name='igeriv.invia'/>" name="igeriv.invia" id="invia" class="tableFields" style="width:100px; text-align:center" onclick="javascript: return (synchEditor() && submitForm('email_sendEmailRivendita.action'));"/>		
					</div>
				</div>										
			</div>
		</div>	
	</div>
	<s:hidden name="emailsToSend" id="emailsToSend"/>
	<s:hidden name="message" id="message"/>
</s:form>