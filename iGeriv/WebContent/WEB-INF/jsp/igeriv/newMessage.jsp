<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 20px;
}

div#content1 {
	height: 550px;
}
</style>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="MessageForm" action="messages_saveMessage.action"
	method="POST" enctype="multipart/form-data" theme="igeriv"
	validate="true" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla" style="text-align: left; width: 700px">
			<legend style="font-size: 100%">
				<s:property value="tableTitle" />
			</legend>
			<div style="width: 650px;">
				<div class="required">
					<div style="float: left; width: 200px;">
						<s:text name="igeriv.destinatario" />
					</div>
					<div style="float: left; width: 420px;">
						<input type="radio" name="destinatario" id="destinatario1"
							value='0' <s:if test="0 eq destinatario">checked</s:if>>&nbsp;&nbsp;
						<s:text name="igeriv.tutte.rivendite" />
						&nbsp; <input type="radio" name="destinatario" id="destinatario2"
							value='1' <s:if test="1 eq destinatario">checked</s:if>>&nbsp;&nbsp;
						<s:text name="igeriv.rivendita.singola" />
						&nbsp; <input type="radio" name="destinatario" id="destinatario3"
							value='2' <s:if test="2 eq destinatario">checked</s:if>>&nbsp;&nbsp;
						<s:text name="igeriv.giri" />
						&nbsp; <input type="radio" name="destinatario" id="destinatario4"
							value='3' <s:if test="3 eq destinatario">checked</s:if>>&nbsp;&nbsp;
						<s:text name="igeriv.zone" />
					</div>
				</div>
				<div id="rivenditeDiv" class="required">
					<div style="float: left; width: 220px;">
						<s:text name="igeriv.cod.rivendita.o.rag.soc" />
						<br>
						<em>*</em>&nbsp;<span style="font-size: 70%; font-style: italic"><s:text
								name="dpe.primi.tre.caratteri" /></span>
					</div>
					<div
						style="float: right; width: 400px; margin-top: 20px; height: 50px;">
						<s:textfield id="edicolaLabel" name="messaggio.edicolaLabel"
							cssStyle="width: 350px" cssClass="tableFields" />
					</div>
				</div>
				<div id="giriDiv" class="required">
					<div style="float: left; width: 220px; height: 100px;">
						<div style="float: left; width: 80px;">
							<s:text name="igeriv.giro.tipo" />
						</div>
						<div style="float: right; width: 120px;">
							<s:textfield id="giroTipo" name="giroTipo" cssStyle="width:100px"
								cssClass="tableFields" />
						</div>
					</div>
					<div id="giriList"
						style="float: left; margin-left: 30px; width: 380px; height: 100px; overflow-y: scroll; text-align: center">

					</div>
				</div>
				<div id="zoneDiv" class="required">
					<div style="float: left; width: 220px; height: 100px;">
						<div style="float: left; width: 80px;">
							<s:text name="igeriv.zona.tipo" />
						</div>
						<div style="float: right; width: 120px;">
							<s:textfield id="zonaTipo" name="zonaTipo" cssStyle="width:100px"
								cssClass="tableFields" />
						</div>
					</div>
					<div id="zoneList"
						style="float: left; margin-left: 30px; width: 380px; height: 100px; overflow-y: scroll; text-align: center">

					</div>
				</div>
				<div class="required">
					<div
						style="float: left; width: 200px; margin-top: 20px; height: 50px;">
						<s:text name="igeriv.tipo.messaggio" />
					</div>
					<div style="float: left; width: 420px;">
						<s:select label="" name="messaggio.tipoMessaggio" id="messageType"
							listKey="key" listValue="value"
							list="%{#application['statiTipoMessaggi']}" emptyOption="false"
							cssStyle="width: 200px" />
					</div>
				</div>
				<div style="float: left; width: 640px; height: 10px"></div>
				<div style="width: 640px; heigth: 300px">
					<div id="innerDiv"></div>
				</div>
				<div style="width: 640px;">
					<s:if
						test="messaggio.attachmentName1 == null || messaggio.attachmentName1 == ''">
						<div class="required" style="height: 30px">
							<div style="float: left; width: 100px;">
								<s:text name="igeriv.allegato1" />
							</div>
							<div style="float: left; width: 220px;">
								<s:file name="attachment1" label="File" />
							</div>
							<div
								style="float: right; width: 220px; text-align: right; vertical-align: middle;">
								<font color="red"><span id="err_attachment1"><s:fielderror>
											<s:param>attachment1</s:param>
										</s:fielderror></span></font>
							</div>
						</div>
					</s:if>
					<s:else>
						<div class="required" style="height: 30px">
							<div style="float: left; width: 100px;">
								<s:text name="igeriv.allegato1" />
							</div>
							<div style="float: left; width: 220px;">
								<a
									href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggio.attachmentName1" />"
									target="new" class="textLinks"><s:text
										name="messaggio.attachmentName1" /></a>
							</div>
							<div id="delete1"
								style="float: right; width: 20px; margin-right: 200px; text-align: left; cursor: pointer">
								<img src="/app_img/remove.jpg" width="16" height="16"
									title="<s:text name="dpe.contact.form.reset" />"
									style="border-style: none" />
							</div>
						</div>
					</s:else>

					<s:if
						test="messaggio.attachmentName2 == null || messaggio.attachmentName2 == ''">
						<div class="required" style="height: 30px">
							<div style="float: left; width: 100px;">
								<s:text name="igeriv.allegato2" />
							</div>
							<div style="float: left; width: 220px;">
								<s:file name="attachment2" label="File1" />
							</div>
							<div style="float: right; width: 220px;">
								<font color="red"><span id="err_attachment2"><s:fielderror>
											<s:param>attachment2</s:param>
										</s:fielderror></span></font>
							</div>
						</div>
					</s:if>
					<s:else>
						<div class="required" style="height: 30px">
							<div style="float: left; width: 100px;">
								<s:text name="igeriv.allegato2" />
							</div>
							<div style="float: left; width: 220px;">
								<a
									href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggio.attachmentName2" />"
									target="new" class="textLinks"><s:text
										name="messaggio.attachmentName2" /></a>
							</div>
							<div id="delete2"
								style="float: right; width: 20px; margin-right: 200px; text-align: left; cursor: pointer;">
								<img src="/app_img/remove.jpg" width="16" height="16"
									title="<s:text name="dpe.contact.form.reset" />"
									style="border-style: none" />
							</div>
						</div>
					</s:else>

					<s:if
						test="messaggio.attachmentName3 == null || messaggio.attachmentName3 == ''">
						<div class="required" style="height: 30px">
							<div style="float: left; width: 100px;">
								<s:text name="igeriv.allegato3" />
							</div>
							<div style="float: left; width: 220px;">
								<s:file name="attachment3" label="File2" />
							</div>
							<div style="float: right; width: 220px;">
								<font color="red"><span id="err_attachment3"><s:fielderror>
											<s:param>attachment3</s:param>
										</s:fielderror></span></font>
							</div>
						</div>
					</s:if>
					<s:else>
						<div class="required" style="height: 30px">
							<div style="float: left; width: 100px;">
								<s:text name="igeriv.allegato3" />
							</div>
							<div style="float: left; width: 220px;">
								<a
									href="${pageContext.request.contextPath}${ap}/attachment.action?fileName=<s:text name="messaggio.attachmentName3" />"
									target="new" class="textLinks"><s:text
										name="messaggio.attachmentName3" /></a>
							</div>
							<div id="delete3"
								style="float: right; width: 20px; margin-right: 200px; text-align: left; cursor: pointer">
								<img src="/app_img/remove.jpg" width="16" height="16"
									title="<s:text name="dpe.contact.form.reset" />"
									style="border-style: none" />
							</div>
						</div>
					</s:else>
				</div>
			</div>
		</fieldset>
		<div style="width: 100%; text-align: center">
			<s:if test="%{disableForm eq false}">
				<input type="button" value="<s:text name='igeriv.memorizza'/>"
					name="igeriv.memorizza" id="memorizza" class="tableFields"
					style="width: 100px; text-align: center"
					onclick="javascript: setTimeout(function() {return (synchEditor() && checkFields(false) && submitForm('messages_saveMessage.action'));},10);" />
				&nbsp;&nbsp;
				<input type="button" value="<s:text name='igeriv.memorizza.invia'/>"
					name="igeriv.memorizza.invia" id="memorizzaInvia"
					class="tableFields" style="width: 150px; text-align: center"
					onclick="javascript: setTimeout(function() {return (synchEditor() && checkFields(false) && submitForm('messages_saveAndSendMessage.action'));},10);" />			
				&nbsp;&nbsp;
				<input type="button" value="<s:text name='dpe.contact.form.reset'/>"
					name="dpe.contact.form.reset" id="cancella" class="tableFields"
					style="width: 150px; text-align: center"
					onclick="javascript:doDelete();" />
			</s:if>
			<s:else>
				&nbsp;&nbsp;
				<input type="button"
					value="<s:text name='igeriv.mostra.conferme.lettura'/>"
					name="igeriv.mostra.conferme.lettura" id="confermeLettura"
					class="tableFields" style="width: 200px; text-align: center" />
			</s:else>
		</div>
		<div id="messageDiv"
			style="float: right; width: 200px; height: 50px; font-size: 14;"></div>
	</div>
	<s:hidden name="message" id="message" />
	<s:hidden name="messaggio.pk.codFiegDl" />
	<s:hidden name="strCodEdicolaDl" id="strCodEdicolaDl" />
	<s:hidden name="messaggio.pk.dtMessaggio" />
	<s:hidden name="messaggio.messaggio" id="messaggio" />
	<s:hidden name="messaggio.attachmentName1" id="attachmentName1" />
	<s:hidden name="messaggio.attachmentName2" id="attachmentName2" />
	<s:hidden name="messaggio.attachmentName3" id="attachmentName3" />
	<s:hidden name="giroTipoSelected" />
	<s:hidden name="zonaTipoSelected" />
	<s:hidden name="giroSelected" />
	<s:hidden name="zonaSelected" />
	<s:hidden name="destinatarioSelected" id="destinatarioSelected" />
</s:form>