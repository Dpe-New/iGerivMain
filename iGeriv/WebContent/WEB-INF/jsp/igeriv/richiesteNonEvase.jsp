<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:form id="RichiesteRifornimentoNonEvaseForm"
	action="bollaRivendita_showPrenotazioniNonEvase.action" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<dpe:table tableId="RichiesteRifornimentoNonEvaseTab"
			items="richiesteRifornimento" var="richiesteRifornimento"
			action="bollaRivendita_showPrenotazioniNonEvase.action"
			imagePath="/app_img/table/*.gif" title="${filterTitle}"
			style="height:100px;" rowsDisplayed="1000" view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
			styleClass="extremeTableFieldsLarger"
			form="RichiesteRifornimentoNonEvaseForm"
			theme="eXtremeTable bollaScrollDivSmall" showPagination="false"
			id="BollaScrollDiv" toolbarClass="eXtremeTable"
			footerStyle="height:30px;" filterable="false">
			<ec:exportPdf fileName="copie_insufficienti_richieste_clienti.pdf"
				tooltip="plg.esporta.pdf" headerTitle="${filterTitle}"
				headerColor="black" headerBackgroundColor="#b6c2da" />
			<ec:exportXls fileName="copie_insufficienti_richieste_clienti.xls"
				tooltip="plg.esporta.excel" />
			<ec:row>
				<dpe:column property="titolo" width="25%" title="igeriv.titolo"
					styleClass="extremeTableFieldsLarger" filterable="false"
					sortable="false" />
				<dpe:column property="sottoTitolo" width="20%"
					title="igeriv.sottotitolo" styleClass="extremeTableFieldsLarger"
					filterable="false" sortable="false" />
				<dpe:column property="numero" width="10%" title="igeriv.numero"
					styleClass="extremeTableFieldsLarger" filterable="false"
					sortable="false" style="text-align:center"
					headerStyle="text-align:center" />
				<dpe:column property="quantitaRichiesta" width="10%"
					title="igeriv.richiesta.rifornimenti.quantita.richiesta.2"
					styleClass="extremeTableFieldsLarger" filterable="false"
					sortable="false" style="text-align:center"
					headerStyle="text-align:center" />
				<dpe:column property="quantitaInBolla" allowZeros="true" width="10%"
					title="igeriv.richiesta.rifornimenti.quantita.in.bolla"
					styleClass="extremeTableFieldsLarger" filterable="false"
					sortable="false" style="text-align:center"
					headerStyle="text-align:center" />
			</ec:row>
		</dpe:table>
	</div>
</s:form>