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
	<s:form id="arretratiForm" action="arretrati_showArretrati.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div style="text-align: center">
			<fieldset class="filterBolla">
				<div
					style="margin-left: 100px; width: 300px; text-align: center; margin-top: 10px;">
					<s:property value="titleArretratiMessaggio" />
				</div>
				<div
					style="margin-left: 100px; width: 300px; text-align: center; margin-top: 12px; font-weight: bold;">
					<s:property value="titleArretrati" />
				</div>
			</fieldset>
		</div>
		<div>
			<dpe:table tableId="arretratiTab" items="arretrati" var="arretrati"
				action="arretrati_showArretrati.action"
				imagePath="/app_img/table/*.gif" style="table-layout:fixed;"
				view="buttonsOnTopAndBottom"
				extraToolButton='<input type="button" value="${requestScope.memorizzaInvia}" name="igeriv.memorizza.invia" id="memorizzaInvia" class="tableFields" style="width:150px; text-align:center;" onclick="javascript: setTimeout(jConfirm(memorizzaArretratiConfirm, attenzioneMsg, function(r) { if (r) { return (setFormAction(\'arretratiForm\',\'arretrati_saveArretrati.action\', \'\', \'messageDiv\', false, \'\', function() { $(\'#arretratiForm\').attr(\'action\',\'arretrati_showArretrati.action\'); $.alerts.dialogClass = \'style_1\'; jAlert(okMessage, msgAvviso.toUpperCase(), function() { $.alerts.dialogClass = null; $(\'#arretratiForm\').submit(); })}, \'\', false, \'\'))} unBlockUI();}),10);"/>'
				extraToolButtonStyle="text-align:right;" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ArretratiAction"
				styleClass="extremeTableFields" form="arretratiForm"
				theme="eXtremeTable bollaScrollDivTall" showPagination="false"
				id="BollaScrollDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false"
				autoIncludeParameters="false">
				<dpe:exportPdf fileName="arretrati.pdf" tooltip="plg.esporta.pdf"
					headerTitle="${titleArretrati}" headerColor="black"
					headerBackgroundColor="#b6c2da" isLandscape="true" />
				<ec:exportXls fileName="arretrati.xls" tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" style="height:25px;">
					<dpe:column property="dtBolla" width="8%" title="igeriv.data"
						cell="date" dateFormat="dd/MM/yyyy" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="titolo" width="18%" title="igeriv.titolo"
						filterable="false" />
					<ec:column property="sottoTitolo" width="15%"
						title="igeriv.sottotitolo" filterable="false" />
					<dpe:column property="numeroCopertina" width="7%"
						title="igeriv.numero" filterable="false" escapeAutoFormat="true"
						style="text-align:center" headerStyle="text-align:center" />
					<dpe:column property="dataUscita" cell="date"
						dateFormat="dd/MM/yyyy" style="text-align:center"
						headerStyle="text-align:center" width="7%"
						title="igeriv.data.uscita" filterable="false" />
					<dpe:column property="quantita"
						style="text-align:center; font-size:12px; font-weight:bold;"
						headerStyle="text-align:center" width="5%" title="igeriv.copie"
						filterable="false" />
					<dpe:column property="conferma" width="18%" title="conferma.copie"
						filterable="false" cell="confermaArretratoCell" sortable="false"
						style="text-align:center" exportStyle="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="note" cell="noteArretratoCell"
						title="igeriv.fondo.bolla" filterable="false" width="20%"
						style="text-align:center;" headerStyle="text-align:center" />
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
