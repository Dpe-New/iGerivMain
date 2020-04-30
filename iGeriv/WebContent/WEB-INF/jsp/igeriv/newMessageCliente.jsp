<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 20px;
}

div#content1 {
	height: 550px;
}
</style>
<s:form id="MessageForm" action="messagesClienti_saveMessage.action"
	method="POST" enctype="multipart/form-data" theme="igeriv"
	validate="true" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla" style="text-align: left; width: 700px">
			<legend style="font-size: 100%">
				<s:property value="filterTitle" />
			</legend>
			<div style="width: 650px;">
				<div class="required">
					<div style="float: left; width: 150px;">
						<s:text name="igeriv.destinatario" />
					</div>
					<div style="float: left; width: 470px;">
						<input type="radio" name="destinatario" id="destinatario1"
							value='0'>&nbsp;&nbsp;
						<s:text name="igeriv.tutti.clienti" />
						&nbsp; <input type="radio" name="destinatario" id="destinatario2"
							value='1'>&nbsp;&nbsp;
						<s:text name="igeriv.con.estratto.conto" />
						&nbsp; <input type="radio" name="destinatario" id="destinatario3"
							value='2'>&nbsp;&nbsp;
						<s:text name="igeriv.senza.estratto.conto" />
						&nbsp; <input type="radio" name="destinatario" id="destinatario3"
							value='3'>&nbsp;&nbsp;
						<s:text name="plg.reimposta" />
						&nbsp;
					</div>
				</div>
				<div class="required">
					<div style="float: left; width: 150px;">&nbsp;</div>
					<div
						style="float: left; width: 470px; margin-top: 20px; height: 150px;">
						<div class="configBarraVenditeScrollDiv"
							style="width: 300px; height: 150px; border: 1px solid black;">
							<s:iterator value="clienti">
								<div style="float: left; width: 100%; white-space: nowrap;">
									<input type="checkbox" name="destinatari" id="destinatari"
										value="<s:property value="codCliente"/>"
										hasEC="<s:if test="tipoEstrattoConto != null && tipoEstrattoConto > 0">true</s:if><s:else>false</s:else>" />&nbsp;
									<s:property value="nomeCognome" escape="false" />
								</div>
							</s:iterator>
						</div>
					</div>
				</div>
				<div class="required">
					<div
						style="float: left; width: 150px; margin-top: 20px; height: 50px;">
						<s:text name="dpe.contact.form.reason" />
					</div>
					<div
						style="float: right; width: 470px; margin-top: 20px; height: 50px;">
						<s:textfield id="oggetto" name="messaggio.oggetto"
							cssStyle="width: 350px" cssClass="tableFields" />
					</div>
				</div>
				<div class="required">
					<div
						style="float: left; width: 150px; margin-top: 20px; height: 50px;">
						<s:text name="plg.template.modello" />
					</div>
					<div
						style="float: right; width: 470px; margin-top: 20px; height: 50px;">
						<s:select id="template" name="codTemplate" list="templates"
							listKey="codice" listValue="nome" cssStyle="width:350px"
							cssClass="tableFields" emptyOption="true" />
					</div>
				</div>
				<div style="float: left; width: 640px; height: 10px"></div>
				<div style="width: 640px; heigth: 300px">
					<div id="innerDiv"></div>
				</div>
				<div style="width: 640px;">
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
				</div>
			</div>
		</fieldset>
		<div style="width: 100%; text-align: center">
			<input type="button"
				value="<s:text name='igeriv.memorizza.template'/>"
				name="memorizzaTemplate" id="memorizzaTemplate" class="tableFields"
				style="width: 200px; text-align: center"
				onclick="javascript: saveTemplate();" /> &nbsp;&nbsp; <input
				type="button" value="<s:text name='igeriv.invia.email'/>"
				name="memorizzaInvia" id="memorizzaInvia" class="tableFields"
				style="width: 150px; text-align: center"
				onclick="javascript: jConfirm('<s:text name='igeriv.confirm.inviare.messaggio.clienti'/>', attenzioneMsg, function(r) {if (r) {setTimeout(function() {return (synchEditor() && formSubmitMultipartAjax('MessageForm', 'html', function() {showAlertMsgSent(); $('#reset').trigger('click');}, function() {return checkFields();}, false, uploadErrCallback));},10);} else {unBlockUI();}}, true, false);" />
			&nbsp;&nbsp; <input type="button"
				value="<s:text name='dpe.contact.form.reset'/>" name="reset"
				id="reset" class="tableFields"
				style="width: 150px; text-align: center"
				onclick="javascript: resetForm();" />
		</div>
		<div id="messageDiv"
			style="float: right; width: 200px; height: 50px; font-size: 14;"></div>
	</div>
	<s:hidden name="messaggio.messaggio" id="message" />
</s:form>
<s:form id="EmailTemplateForm"
	action="messagesClientiJ_saveEmailTemplateMessage.action" method="POST">
	<s:hidden name="template.nome" id="nome" />
	<s:hidden name="template.contenuto" id="messageTemplate" />
</s:form>
<ul id="templateMenu" class="contextMenu" style="width: 200px;">
	<li class="delete"><a href="#delete"><s:text
				name="dpe.contact.form.elimina" /></a></li>
</ul>
