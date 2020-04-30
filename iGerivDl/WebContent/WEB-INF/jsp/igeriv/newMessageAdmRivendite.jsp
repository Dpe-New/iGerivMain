<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="MessageForm" action="msgDpeRivendite_saveMessage.action" method="POST" enctype="multipart/form-data" theme="igeriv" validate="false" onsubmit="return (ray.ajax())">			
	<div id="mainDiv" style="width:100%">	
		<fieldset class="filterBolla" style="text-align:left; width:700px"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div style="width:650px;">	
				<div class="required">
					<div style="float:left; width:200px;"><s:text name="igeriv.destinatario" /></div>
					<div style="float:left; width:420px;">
						<input type="radio" name="destinatario" id="destinatario1" value='0' <s:if test="0 eq destinatario">checked</s:if>>&nbsp;&nbsp;<s:text name="igeriv.tutte.rivendite"/>&nbsp;												
						<input type="radio" name="destinatario" id="destinatario2" value='1' <s:if test="1 eq destinatario">checked</s:if>>&nbsp;&nbsp;<s:text name="igeriv.rivendita.singola"/>&nbsp;
					</div>				
				</div>		 
				<div id="rivenditeDiv" class="required">
					<div style="float:left; width:220px;"><s:text name="igeriv.cod.rivendita.o.rag.soc" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
					<div style="float:right; width:400px; margin-top:20px; height:50px;"><s:textfield id="edicolaLabel" name="messaggio.edicolaLabel" cssStyle="width: 350px" cssClass="tableFields" /></div>				
				</div>	
				<div style="width:640px; heigth:250px">
					<div id="innerDiv"></div>											
				</div>	
				<div style="width:640px;">
					<s:if test="%{disableForm eq false}">
						<s:if test="messaggio.attachmentName1 == null || messaggio.attachmentName1 == ''">
							<div class="required" style="height:30px">
								<div style="float:left; width:100px;"><s:text name="igeriv.allegato1" /></div>
								<div style="float:left; width:220px;" id="attch1"><s:file name="attachment1" id="attachment1" label="File" /></div>			
								<div id="delete1" style="float:left; width:220px; text-align:right; vertical-align:middle;"><font color="red"><span id="err_attachment1"><s:fielderror><s:param>attachment1</s:param></s:fielderror></span></font></div>		
							</div>	
						</s:if>	
						<s:else>
							<div class="required" style="height:30px">
								<div style="float:left; width:100px;"><s:text name="igeriv.allegato1" /></div>
								<div style="float:left; width:220px;" id="attch1"><a href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggio.attachmentName1" />" target="new" class="textLinks"><s:text name="messaggio.attachmentName1" /></a></div>
								<div id="delete1" style="float:left; width:20px; margin-left:200px; text-align:left; cursor: pointer"><img src="/app_img/remove.jpg" width="16" height="16" title="<s:text name="dpe.contact.form.reset" />" style="border-style:none"/></div>					
							</div>	
						</s:else>
					</s:if>
				</div>										
			</div>
		</fieldset>	
		<div style="width:100%; text-align:center">	
			<s:if test="%{disableForm eq false}">
				<input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" class="tableFields" style="width:100px; text-align:center" onclick="javascript: return (synchEditor() && setFormAction('MessageForm','msgDpeRivendite_saveMessage.action', '', 'messageDiv'));"/>
				&nbsp;&nbsp;
				<input type="button" value="<s:text name='igeriv.memorizza.invia'/>" name="igeriv.memorizza.invia" id="memorizzaInvia" class="tableFields" style="width:150px; text-align:center" onclick="javascript: return (synchEditor() && setFormAction('MessageForm','msgDpeRivendite_saveAndSendMessage.action', '', 'messageDiv'));"/>			
				&nbsp;&nbsp;
				<input type="button" value="<s:text name='dpe.contact.form.reset'/>" name="dpe.contact.form.reset" id="cancella" class="tableFields" style="width:150px; text-align:center" onclick="javascript: doDelete();"/>
			</s:if>				
		</div>
		<div id="messageDiv" style="float:right; width:200px; height:50px; font-size:14;"></div>
	</div>		
	<input type="hidden" name="messaggio.messaggio" id="message" value="<s:property value="messageContent"/>"/>
	<s:hidden name="messaggio.pk.codFiegDl"/>
	<s:hidden name="coddl" id="coddl"/>
	<s:hidden name="messaggio.pk.dtMessaggio"/>
	<s:hidden name="messaggio.dtMessaggio"/>
	<s:hidden name="messaggio.tipoDestinatario"/>
	<s:hidden name="messaggio.destinatarioA" id="strCodEdicolaDl"/>
	<s:hidden name="messaggio.codFiegDl"/>
	<s:hidden name="messaggio.attachmentName1" id="attachmentName1"/>
</s:form>