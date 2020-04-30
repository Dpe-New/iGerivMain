<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.richiesteRifornimento != null && !#request.richiesteRifornimento.isEmpty()}">
	<s:form id="RichiestaRifornimentoDettaglioForm"
		action="viewRifornimenti_showRichiesteRifornimenti.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div id="mainDiv" style="width: 100%">
			<dpe:table tableId="RichiestaRifornimentoTab"
				items="richiesteRifornimento" var="richiesteRifornimento"
				action="viewRifornimenti_showRichiesteRifornimenti.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFieldsSmaller"
				form="RichiestaRifornimentoDettaglioForm"
				style="height:320px; table-layout:fixed;"
				theme="eXtremeTable bollaScrollDiv" showPagination="false"
				id="RichiestaRifornimentoScrollDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="bolla_controllo.pdf"
					tooltip="plg.esporta.pdf" headerTitle="${title}"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" />
				<ec:exportXls fileName="bolla_controllo.xls"
					tooltip="plg.esporta.excel" />
				<ec:row style="height:30px" interceptor="marker">
					<dpe:column property="dataOrdine" width="7%"
						title="igeriv.data.ordine" cell="date" format="dd/MM/yyyy"
						style="text-align:center" headerStyle="text-align:center"
						filterable="false" />
					<ec:column property="codicePubblicazione" width="4%"
						title="igeriv.cpu" filterable="false" style="text-align:center"
						headerStyle="text-align:center" />
					<ec:column property="titolo" width="15%" title="igeriv.titolo"
						filterable="false" />
					<ec:column property="sottoTitolo" width="12%"
						title="igeriv.sottotitolo" filterable="false" />
					<ec:column property="numeroCopertina" width="5%"
						title="igeriv.numero" style="text-align:center"
						headerStyle="text-align:center" filterable="false" />
					<dpe:column property="dataScadenzaRichiesta" width="7%" size="5"
						title="igeriv.data.scadenza.richiesta" cell="date"
						format="dd/MM/yyyy" style="text-align:center"
						headerStyle="text-align:center" filterable="false" />
					<ec:column property="quantitaRichiesta" width="5%"
						title="igeriv.richiesta.rifornimenti.quantita.richiesta"
						style="text-align:center" headerStyle="text-align:center"
						filterable="false" />
					<ec:column property="quantitaRichiestaGestioneClienti" width="5%"
						title="igeriv.richiesta.rifornimenti.quantita.richiesta.clienti"
						style="text-align:center" headerStyle="text-align:center"
						filterable="false" />
					<ec:column property="quantitaEvasa" width="5%"
						title="igeriv.richiesta.rifornimenti.quantita.evasa"
						style="text-align:center" headerStyle="text-align:center"
						filterable="false" />
					<dpe:column property="dataBollaAddebito" width="7%"
						title="igeriv.data.bolla.addebito" cell="date" format="dd/MM/yyyy"
						style="text-align:center" headerStyle="text-align:center"
						filterable="false" />
					<dpe:column property="dataRispostaDl" width="7%"
						title="igeriv.richiesta.rifornimenti.data.risposta" cell="date"
						format="dd/MM/yyyy" style="text-align:center"
						headerStyle="text-align:center" filterable="false" />
					<ec:column property="descCausaleNonEvadibilita" width="10%"
						title="igeriv.richiesta.rifornimenti.quantita.note.dl"
						filterable="false" />
					<ec:column property="noteVendita" width="10%"
						title="igeriv.richiesta.rifornimenti.quantita.note"
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