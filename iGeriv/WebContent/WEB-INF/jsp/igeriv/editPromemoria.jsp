<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/rte_<s:text name="igeriv.version.timestamp"/>.css" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>
<s:form id="MessageForm" name="MessageForm"
	action="promemoria_savePromemoria.action" method="POST"
	validate="false" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla"
			style="text-align: left; width: 630px; margin-top: 30px;">
			<legend style="font-size: 100%">
				<s:text name="igeriv.nuovo.promemoria" />
			</legend>
			<div style="width: 100%">
				<div
					style="width: 270px; margin-left: auto; margin-right: auto; height: 30px">
					<div style="float: left; width: 50px;">
						<s:text name="igeriv.data" />
					</div>
					<div style="float: left; width: 200px;">
						<s:textfield name="promemoria.dataMessaggio" id="dataMessaggio"
							cssClass="tableFields" cssStyle="width:90px;" disabled="false">
							<s:param name="value">
								<s:date name="promemoria.dataMessaggio" format="dd/MM/yyyy" />
							</s:param>
						</s:textfield>
					</div>
				</div>
			</div>
			<div style="width: 540px;">
				<div id="innerDiv"></div>
			</div>
		</fieldset>
		<div style="width: 100%; text-align: center">
			<input type="button" value="<s:text name='igeriv.memorizza'/>"
				name="igeriv.memorizza" id="memorizza" class="tableFields"
				style="width: 100px; text-align: center"
				onclick="javascript: return (synchEditor() && validateSave() && setFormAction('MessageForm','promemoria_savePromemoria.action', '', '', false, '', function() {$('#ricerca').trigger('click');}));" />
			<input type="button" value="<s:text name='dpe.contact.form.reset'/>"
				name="dpe.contact.form.reset" id="cancella" class="tableFields"
				style="width: 150px; text-align: center"
				onclick="javascript: if (window.confirm('<s:text name="gp.cancellare.dati"/>')) { setFormAction('MessageForm','promemoria_deletePromemoria.action', '', '') };" />
		</div>
	</div>
	<s:hidden name="promemoria.codPromemoria" />
	<s:hidden name="promemoria.letto" />
	<input type="hidden" name="promemoria.messaggio" id="message"
		value="<s:property value="messageContent"/>" />
</s:form>