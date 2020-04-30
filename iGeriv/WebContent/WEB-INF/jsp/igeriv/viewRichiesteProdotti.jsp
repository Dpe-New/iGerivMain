<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
	<s:if
		test="%{#request.listRichiesteProdotti != null && !#request.listRichiesteProdotti.isEmpty()}">
		<s:form id="ProdottiDetailForm"
			action="viewRichiesteProdotti_showViewRichiesteProdotti.action"
			method="POST" theme="simple" validate="false"
			onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:table tableId="ProdottiTab" items="listRichiesteProdotti"
					var="listRichiesteProdotti"
					action="${pageContext.request.contextPath}/viewRichiesteProdotti_showViewRichiesteProdotti.action"
					imagePath="/app_img/table/*.gif"
					style="table-layout:auto; height: 350px" view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.ProdottiAction"
					styleClass="extremeTableFields" form="ProdottiDetailForm"
					theme="eXtremeTable bollaScrollDiv" showPagination="false"
					id="ProdottiScrollDiv" toolbarClass="eXtremeTable"
					showStatusBar="false" showTitle="false" showTooltips="false"
					footerStyle="height:30px;" filterable="false" showExports="true">
					<dpe:exportPdf fileName="richieste_prodotti.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="igeriv.view.richieste.prodotti.non.editoriali"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls fileName="richieste_prodotti.xls"
						tooltip="plg.esporta.excel" />
					<dpe:row interceptor="marker" style="height:25px;"
						href="#?pk=codProdottoInterno">
						<ec:column property="descrizioneProdotto" width="28%"
							title="igeriv.prodotto" filterable="false"
							style="text-align:left" headerStyle="text-align:left" />
						<ec:column property="quatitaRichiesta" width="10%"
							title="igeriv.richiesta.rifornimenti.prodotti.quantita.richiesta"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
						<ec:column property="quatitaEvasa" width="10%"
							title="igeriv.richiesta.rifornimenti.quantita.evasa"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
						<ec:column property="dataRichiesta" width="12%"
							title="igeriv.data.richiesta" cell="date"
							format="dd/MM/yyyy HH:mm:ss" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<ec:column property="statoDesc" width="15%"
							title="igeriv.richiesta.rifornimenti.stato.evasione"
							filterable="false" style="text-align:left"
							headerStyle="text-align:left" />
						<ec:column property="note" width="23%"
							title="igeriv.richiesta.rifornimenti.quantita.note.dl"
							filterable="false" style="text-align:left"
							headerStyle="text-align:left" />
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
</div>