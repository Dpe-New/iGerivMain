<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.ordini}">
	<s:form id="BollaOrdiniForm" name="BollaOrdiniForm"
		action="bollaRivendita_showOrdini.action" method="POST" theme="simple"
		validate="false" onsubmit="return (ray.ajax())">
		<div id="mainDiv" style="width: 100%">
			<ec:table tableId="BollaOrdiniTab" items="ordini" var="ordine"
				action="bollaRivendita_showOrdini.action"
				imagePath="/app_img/table/*.gif" title="${tableTitle}"
				style="height:100px; width:100%" view="buttonsOnBottom"
				rowsDisplayed="1000" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFieldsLarger" form="BollaOrdiniForm"
				theme="eXtremeTable configBarraVenditeScrollDiv"
				showTooltips="false" showExports="true" showPagination="false"
				showStatusBar="false" filterable="false" width="100%">
				<dpe:exportPdf fileName="ordini_clienti.pdf"
					tooltip="plg.esporta.pdf" headerTitle="igeriv.nuovi.ordini.clienti"
					headerColor="black" headerBackgroundColor="#b6c2da" />
				<ec:exportXls fileName="ordini_clienti.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row interceptor="marker"
					href="#?pk=pk&codCliente=codCliente&evaso=quantitaDaEvadere&quantitaDaEvadere=quantitaDaEvadere">
					<ec:column property="titolo" width="15%" title="igeriv.titolo"
						filterable="false" sortable="false" style="text-align:left" />
					<ec:column property="numeroCopertina" width="5%"
						title="igeriv.numero" filterable="false" sortable="false"
						style="text-align:center" />
					<ec:column property="nome" width="8%" title="dpe.nome"
						filterable="false" sortable="false" style="text-align:left" />
					<ec:column property="cognome" width="8%" title="dpe.cognome"
						filterable="false" sortable="false" style="text-align:left" />
					<dpe:column property="dataOrdine" cell="date" format="dd/MM/yyyy"
						width="8%" title="igeriv.richiesta.rifornimenti.data.inserimento"
						filterable="false" sortable="false" style="text-align:center" />
					<ec:column property="quantitaDaEvadere" width="8%"
						title="igeriv.richiesta.rifornimenti.quantita.da.evadere"
						filterable="false" sortable="false" style="text-align:center" />
					<ec:column property="statoEvasioneDesc" width="8%"
						title="igeriv.richiesta.rifornimenti.stato.evasione"
						filterable="false" sortable="false" style="text-align:center" />
					<ec:column property="provenienzaDesc" width="8%"
						title="igeriv.richiesta.rifornimenti.provenienza.richiesta"
						filterable="false" sortable="false" style="text-align:center" />
				</dpe:row>
			</ec:table>
		</div>
		<s:if
			test="#request.listCodClienti != null && #request.listCodClienti != ''">
			<div style="width: 100%;">
				<div
					style="text-align: center; margin-left: auto; margin-right: auto; width: 200px; height: 30px;">
					<input type="button" value="<s:text name='igeriv.evadi.ordini'/>"
						name="evadiOrdini" id="evadiOrdini" class="tableFields"
						style="width: 150px; text-align: center"
						onclick="javascript:evadi();" />
				</div>
			</div>
		</s:if>
	</s:form>
	<s:form id="EvasioneOrdiniBollaForm" name="EvasioneOrdiniBollaForm"
		action="gestioneClientiJ_saveEvasione.action" method="POST">
		<s:hidden id="listCodClienti" name="listCodClienti"
			value="%{#request.listCodClienti}" />
		<s:hidden id="inviaEmail" name="inviaEmail" value="true" />
		<s:hidden id="titolo" name="titolo" value="%{#request.titolo}" />
		<s:hidden id="pk" name="pk" />
		<s:hidden id="evaso" name="evaso" />
		<s:hidden id="qtaDaEvadere" name="qtaDaEvadere" />
		<s:hidden id="spunte" name="spunte" />
		<s:hidden id="ultimaRisposta" name="ultimaRisposta" />
		<s:hidden id="messLibero" name="messLibero" />
	</s:form>

</s:if>
