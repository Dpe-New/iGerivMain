<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.variazioni != null && !#request.variazioni.isEmpty()}">
	<s:form id="VariazioniDettaglioForm"
		action="viewVariazioni_showVariazioniInserite.action" method="POST"
		theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div id="mainDiv" style="width: 100%">
			<dpe:table tableId="VariazioniTab" items="variazioni"
				var="variazioni"
				action="viewVariazioni_showVariazioniInserite.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFieldsSmaller"
				form="VariazioniDettaglioForm"
				style="height:320px; table-layout:fixed;"
				theme="eXtremeTable bollaScrollDiv" showPagination="false"
				id="VariazioniScrollDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="bolla_controllo.pdf"
					tooltip="plg.esporta.pdf" headerTitle="${title}"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" />
				<ec:exportXls fileName="bolla_controllo.xls"
					tooltip="plg.esporta.excel" />
				<ec:row style="height:30px">
					<dpe:column property="dataRichiesta" width="8%"
						title="igeriv.data.ordine" cell="date" format="dd/MM/yyyy"
						style="text-align:center" headerStyle="text-align:center"
						filterable="false" />
					<ec:column property="codicePubblicazione" width="8%"
						title="igeriv.codice.pubblicazione" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
					<ec:column property="titolo" width="25%" title="igeriv.titolo"
						filterable="false" />
					<ec:column property="argomento" width="13%"
						title="igeriv.argomento" filterable="false" />
					<ec:column property="periodicita" width="13%"
						title="igeriv.periodicita" filterable="false" />
					<ec:column property="quantitaRichiesta" width="8%"
						title="igeriv.richiesta.rifornimenti.quantita.richiesta"
						style="text-align:center" headerStyle="text-align:center"
						filterable="false" />
					<ec:column property="motivoRichiesta" width="15%"
						title="igeriv.motivo" filterable="false" />
					<ec:column property="statoDesc" width="8%"
						title="igeriv.richiesta.rifornimenti.quantita.stato"
						style="text-align:center" headerStyle="text-align:center"
						filterable="false" />
				</ec:row>
			</dpe:table>
		</div>
	</s:form>
	<div id="one"></div>
</s:if>
<s:else>
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>