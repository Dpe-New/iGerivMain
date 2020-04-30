<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top: 30px">
	<s:if
		test="%{#request.venditeDettaglio != null && !#request.venditeDettaglio.isEmpty()}">
		<s:form id="VenditeReportDetailForm"
			action="reportVendite_showReport.action" method="POST" theme="simple"
			validate="false" onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:table tableId="VenditeReportDetailTab" items="venditeDettaglio"
					var="venditeDettaglio" action="reportVendite_showReport.action"
					imagePath="/app_img/table/*.gif"
					style="height:371px; table-layout:auto" view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.VenditeCardAction"
					styleClass="extremeTableFieldsSmaller"
					form="VenditeReportDetailForm" theme="eXtremeTable bollaScrollDiv"
					showPagination="false" id="VenditeReportScrollDiv"
					toolbarClass="eXtremeTable" footerStyle="height:30px;"
					filterable="false">
					<dpe:exportPdf fileName="vendite_dettaglio_${strData}.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="Report Vendite Dettaglio - Data: ${strData}"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls fileName="vendite_dettaglio_${strData}.xls"
						tooltip="plg.esporta.excel" />
					<ec:row style="height:25px">
						<ec:column property="oraVendita" escapeAutoFormat="true"
							width="8%" title="igeriv.ora.vendita" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<ec:column property="ragioneSocialeCliente" width="20%"
							title="igeriv.rag.soc.cliente" filterable="false"
							style="text-align:left" />
						<ec:column property="titolo" width="18%" title="igeriv.titolo"
							filterable="false" style="text-align:left" />
						<ec:column property="sottoTitolo" width="18%"
							title="igeriv.sottotitolo" filterable="false"
							style="text-align:left" />
						<ec:column property="numeroCopertina" width="5%"
							title="igeriv.numero" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="prezzoCopertina" width="10%"
							title="igeriv.prezzo" cell="currency" hasCurrencySign="true"
							format="###,###,##0.00" totalFormat="###,###,##0.00"
							filterable="false" style="text-align:right"
							headerStyle="text-align:right" />
						<dpe:column property="quantita" calc="total"
							calcTitle="column.calc.total" width="10%" title="igeriv.quantita"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center"
							totalCellStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="totale" calc="total" hasCurrencySign="true"
							cell="currency" format="###,###,##0.00"
							totalFormat="###,###,##0.00" style="text-align:right"
							headerStyle="text-align:right" width="10%"
							title="label.print.Table.Total" filterable="false"
							totalCellStyle="text-align:right" exportStyle="text-align:right" />
					</ec:row>
				</dpe:table>
			</div>
		</s:form>
	</s:if>
	<s:elseif
		test="%{#request.venditeRiepilogo != null && !#request.venditeRiepilogo.isEmpty()}">
		<s:form id="VenditeReportDetailForm"
			action="reportVendite_showReport.action" method="POST" theme="simple"
			validate="false" onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:table tableId="VenditeReportDetailTab" items="venditeRiepilogo"
					var="venditeRiepilogo" action="reportVendite_showReport.action"
					imagePath="/app_img/table/*.gif"
					style="table-layout:fixed; height: 371px" view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.VenditeCardAction"
					styleClass="extremeTableFields" form="VenditeReportDetailForm"
					theme="eXtremeTable bollaScrollDiv" showPagination="false"
					id="VenditeReportScrollDiv" toolbarClass="eXtremeTable"
					showStatusBar="true" showTitle="false" showTooltips="false"
					footerStyle="height:30px;" filterable="false" showExports="true">
					<dpe:exportPdf fileName="vendite_riepilogativo.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="Report Vendite Riepilogativo - Dal: ${strDataDa} Al ${strDataA}"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls fileName="vendite_dettaglio_${strData}.xls"
						tooltip="plg.esporta.excel" />
					<ec:row style="height:25px">
						<ec:column property="raggruppamento" width="30%"
							title="${raggruppamento}" filterable="false"
							style="text-align:left" />
						<dpe:column property="copie" calc="total"
							calcTitle="column.calc.total" width="10%" title="igeriv.quantita"
							filterable="false" format="###,###,##0.00"
							totalFormat="###,###,##0.00" style="text-align:center"
							headerStyle="text-align:center"
							totalCellStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="importo" calc="total" hasCurrencySign="true"
							cell="currency" width="20%" title="igeriv.vendita.totale.lordo"
							format="###,###,##0.00" totalFormat="###,###,##0.00"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center"
							totalCellStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="importoNetto" calc="total" hasCurrencySign="true"
							cell="currency" width="20%" title="igeriv.vendita.totale.netto"
							format="###,###,##0.00" totalFormat="###,###,##0.00"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center"
							totalCellStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="aggio" calc="total" hasCurrencySign="true"
							cell="currency" width="20%" title="igeriv.vendita.totale.aggio"
							format="###,###,##0.00" totalFormat="###,###,##0.00"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center"
							totalCellStyle="text-align:center"
							exportStyle="text-align:center" />
					</ec:row>
				</dpe:table>
			</div>
		</s:form>
	</s:elseif>
	<s:else>
		<div class="tableFields"
			style="width: 100%; align: center; text-align: center; margin-top: 50px">
			<s:text name="gp.nessun.risultato" />
		</div>
	</s:else>
</div>