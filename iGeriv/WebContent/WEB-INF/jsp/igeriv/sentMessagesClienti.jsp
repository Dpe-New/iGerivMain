<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.messaggi != null && !#request.messaggi.isEmpty()}">
	<s:form id="MessaggiForm"
		action="sentMessagesClienti_showMessages.action" method="POST"
		theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="MessaggiTab" items="messaggi" var="messaggi"
				action="sentMessagesClienti_showMessages.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.MessagesClientiAction"
				styleClass="extremeTableFields"
				style="height:370px; table-layout:fixed" form="MessaggiForm"
				theme="eXtremeTable bollaScrollDiv" showPagination="false"
				id="MessaggiDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="messaggi.pdf" tooltip="plg.esporta.pdf"
					headerTitle="${reportTitle}" headerColor="black"
					headerBackgroundColor="#b6c2da" isLandscape="true" />
				<ec:exportXls fileName="messaggi.xls" tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" interceptor="marker"
					style="height:30px; cursor:pointer" href="#?codice=codice">
					<ec:column property="dataMessaggio" cell="date"
						format="dd/MM/yyyy HH:mm:ss" width="9%"
						title="igeriv.data.messaggio" filterable="false" />
					<ec:column property="cliente.nomeCognome" width="15%"
						title="username.cliente" filterable="false" />
					<ec:column property="oggetto" width="15%"
						title="dpe.contact.form.reason" filterable="false" />
					<ec:column property="attachmentName" width="5%"
						title="igeriv.allegato1" filterable="false" sortable="false"
						viewsDenied="pdf,xls" />
					<ec:column property="attachmentName" width="5%"
						title="igeriv.allegato2" filterable="false" sortable="false"
						viewsDenied="pdf,xls" />
					<ec:column property="attachmentName" width="5%"
						title="igeriv.allegato3" filterable="false" sortable="false"
						viewsDenied="pdf,xls" />
				</dpe:row>
			</dpe:table>
		</div>
		<s:hidden name="messagePk" id="messagePk" />
	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>

