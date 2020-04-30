<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 25px;
}

div#content1 {
	height: 480px;
}
</style>
<s:if
	test="%{#request.arretrati != null && !#request.arretrati.isEmpty()}">
	<s:form id="arretratiForm"
		action="arretrati_showReportArretrati.action" method="POST"
		theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div style="text-align: center">
			<fieldset class="filterBolla">
				<div
					style="margin-left: 100px; width: 300px; text-align: center; margin-top: 12px; font-weight: bold;">
					<s:property value="titleArretrati" />
				</div>
			</fieldset>
		</div>
		<div>
			<dpe:table tableId="arretratiTab" items="arretrati" var="arretrati"
				action="arretrati_showReportArretrati.action"
				imagePath="/app_img/table/*.gif" style="table-layout:fixed;"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ArretratiAction"
				styleClass="extremeTableFields" form="arretratiForm"
				theme="eXtremeTable bollaScrollDivTall" showPagination="true"
				rowsDisplayed="100" id="BollaScrollDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false"
				autoIncludeParameters="false">
				<dpe:exportPdf fileName="report_arretrati.pdf"
					tooltip="plg.esporta.pdf" headerTitle="${titleArretrati}"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" />
				<ec:exportXls fileName="report_arretrati.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" style="height:25px;">
					<dpe:column property="dataEvasione" width="8%"
						title="igeriv.richiesta.rifornimenti.data.risposta" cell="date"
						dateFormat="dd/MM/yyyy" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="dataSpedizioneConferma" width="8%"
						title="igeriv.data.spedizione.conferma" cell="date"
						dateFormat="dd/MM/yyyy" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="titolo" width="18%" title="igeriv.titolo"
						filterable="false" />
					<ec:column property="sottoTitolo" width="15%"
						title="igeriv.sottotitolo" filterable="false" />
					<dpe:column property="numeroCopertina" width="8%"
						title="igeriv.numero" filterable="false" escapeAutoFormat="true"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="dataUscita" cell="date"
						dateFormat="dd/MM/yyyy" style="text-align:center"
						headerStyle="text-align:center" width="8%"
						title="igeriv.data.uscita" filterable="false" />
					<dpe:column property="quantita" style="text-align:center"
						headerStyle="text-align:center" width="8%" title="igeriv.copie"
						filterable="false" />
					<dpe:column property="quantitaEvasa" style="text-align:center"
						headerStyle="text-align:center" width="8%"
						title="igeriv.richiesta.rifornimenti.quantita.evasa"
						filterable="false" />
					<ec:column property="note" title="igeriv.fondo.bolla"
						filterable="false" width="18%" style="text-align:center;"
						headerStyle="text-align:center" />
				</dpe:row>
			</dpe:table>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
