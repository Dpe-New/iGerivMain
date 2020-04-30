<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.listPromemoria != null && !#request.listPromemoria.isEmpty()}">
	<s:form id="MessaggiForm" action="promemoria_showListPromemoria.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="MessaggiTab" items="listPromemoria"
				var="listPromemoria" action="promemoria_showListPromemoria.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.PromemoriaAction"
				styleClass="extremeTableFields"
				style="height:370px; table-layout:fixed" form="MessaggiForm"
				theme="eXtremeTable bollaScrollDiv" showPagination="false"
				id="MessaggiDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="promemoria.pdf" tooltip="plg.esporta.pdf"
					headerTitle="${reportTitle}" headerColor="black"
					headerBackgroundColor="#b6c2da" isLandscape="true" />
				<ec:exportXls fileName="promemoria.xls" tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" interceptor="marker"
					style="height:30px; cursor:pointer"
					href="#?codPromemoria=codPromemoria">
					<ec:column property="dataMessaggio" cell="date" format="dd/MM/yyyy"
						width="20%" title="igeriv.data" filterable="false" />
					<ec:column property="messaggioShort" width="40%"
						title="dpe.contact.form.message" filterable="false" />
					<ec:column property="lettoDesc" width="10%"
						title="igeriv.messaggio.letto" style="text-align:center"
						filterable="false" />
				</dpe:row>
			</dpe:table>
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>

