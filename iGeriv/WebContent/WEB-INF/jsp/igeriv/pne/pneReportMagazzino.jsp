<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.reportMagazzino != null && !#request.reportMagazzino.isEmpty()}">
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>
	<s:if test="codiceCausale eq 1">
		<s:form id="ReportMagazzinoForm"
			action="pneReportMagazzino_showReport.action" method="POST"
			theme="simple" validate="false" onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:table tableId="ReportMagazzinoTab" items="reportMagazzino"
					var="reportMagazzino"
					action="${pageContext.request.contextPath}${ap}/pneReportMagazzino_showReport.action"
					imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
					view="buttonsOnBottom" locale="${localeString}"
					state="it.dpe.igeriv.web.actions.ProdottiNonEditorialiMagazzinoAction"
					styleClass="extremeTableFields" form="ReportMagazzinoForm"
					theme="eXtremeTable bollaScrollDiv" showPagination="false"
					id="ReportMagazzinoDiv" toolbarClass="eXtremeTable"
					footerStyle="height:30px;" filterable="false">
					<dpe:exportPdf fileName="report_magazzino.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="<fo:block text-align='center'>${tableTitle}</fo:block><br><fo:block text-align-last='center'>${dataIniziale} - ${dataFinale}</fo:block>"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls fileName="report_magazzino.xls"
						tooltip="plg.esporta.excel" />
					<ec:row style="height:25px;">
						<ec:column property="dataRegistrazione" width="10%"
							title="igeriv.pne.report.magazzino.data.registrazione"
							cell="date" format="dd/MM/yyyy HH:mm:ss" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="descrizioneProdotto" width="35%"
							title="igeriv.pne.report.magazzino.descrizione.prodotto"
							filterable="false" />
						<dpe:column property="quantitaProdotto" width="5%"
							title="igeriv.pne.report.magazzino.quantita" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="giacenzaProdotto" width="5%"
							title="igeriv.giacienza.ext" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="prezzoProdotto" width="5%"
							title="igeriv.pne.report.magazzino.prezzo" filterable="false"
							cell="currency" format="###,##0.00" hasCurrencySign="true"
							allowZeros="true" style="text-align:right"
							headerStyle="text-align:right" />
						<dpe:column property="importo" width="5%"
							title="igeriv.pne.report.magazzino.importo" filterable="false"
							cell="currency" format="###,##0.00" hasCurrencySign="true"
							allowZeros="true" style="text-align:right"
							headerStyle="text-align:right" />
						<dpe:column property="fornitore" width="20%"
							title="igeriv.pne.report.magazzino.conto" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="causale" width="15%"
							title="igeriv.pne.report.magazzino.causale" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
					</ec:row>
				</dpe:table>
				<div
					style="background-color: #F4F4F4; width: 90; height: 30px; font-weight: bold">
					<s:iterator value="totaliMap.entrySet()">
						<div
							style="width: 25%; float: left; text-align: left; font-weight: bold; font-size: 110%"
							class="extremeTableFieldsLarger">
							<s:text name="igeriv.pne.report.magazzino.totale"></s:text>
							&nbsp;
							<s:property value="%{key}" />
							:&nbsp;&nbsp;
							<s:property value="%{value}" />
						</div>
					</s:iterator>
				</div>
			</div>
		</s:form>
	</s:if>
	<s:else>
		<s:form id="ReportMagazzinoForm"
			action="pneReportMagazzino_showReport.action" method="POST"
			theme="simple" validate="false" onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:table tableId="ReportMagazzinoTab" items="reportMagazzino"
					var="reportMagazzino"
					action="${pageContext.request.contextPath}${ap}/pneReportMagazzino_showReport.action"
					imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
					view="buttonsOnBottom" locale="${localeString}"
					state="it.dpe.igeriv.web.actions.ProdottiNonEditorialiMagazzinoAction"
					styleClass="extremeTableFields" form="ReportMagazzinoForm"
					theme="eXtremeTable bollaScrollDiv" showPagination="false"
					id="ReportMagazzinoDiv" toolbarClass="eXtremeTable"
					footerStyle="height:30px;" filterable="false">
					<dpe:exportPdf fileName="report_magazzino.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="<fo:block text-align='center'>${tableTitle}</fo:block><br><fo:block text-align-last='center'>${dataIniziale} - ${dataFinale}</fo:block>"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls fileName="report_magazzino.xls"
						tooltip="plg.esporta.excel" />
					<ec:row style="height:25px;">
						<ec:column property="dataRegistrazione" width="10%"
							title="igeriv.pne.report.magazzino.data.registrazione"
							cell="date" format="dd/MM/yyyy HH:mm:ss" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="descrizioneProdotto" width="35%"
							title="igeriv.pne.report.magazzino.descrizione.prodotto"
							filterable="false" />
						<dpe:column property="quantitaProdotto" width="5%"
							title="igeriv.pne.report.magazzino.quantita" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="prezzoProdotto" width="5%"
							title="igeriv.pne.report.magazzino.prezzo" filterable="false"
							cell="currency" format="###,##0.00" hasCurrencySign="true"
							allowZeros="true" style="text-align:right"
							headerStyle="text-align:right" />
						<dpe:column property="importo" width="5%"
							title="igeriv.pne.report.magazzino.importo" filterable="false"
							cell="currency" format="###,##0.00" hasCurrencySign="true"
							allowZeros="true" style="text-align:right"
							headerStyle="text-align:right" />
						<dpe:column property="fornitore" width="20%"
							title="igeriv.pne.report.magazzino.conto" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="causale" width="15%"
							title="igeriv.pne.report.magazzino.causale" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
					</ec:row>
				</dpe:table>
				<div
					style="background-color: #F4F4F4; width: 90; height: 30px; font-weight: bold">
					<s:iterator value="totaliMap.entrySet()">
						<div
							style="width: 25%; float: left; text-align: left; font-weight: bold; font-size: 110%"
							class="extremeTableFieldsLarger">
							<s:text name="igeriv.pne.report.magazzino.totale"></s:text>
							&nbsp;
							<s:property value="%{key}" />
							:&nbsp;&nbsp;
							<s:property value="%{value}" />
						</div>
					</s:iterator>
				</div>
			</div>
		</s:form>
	</s:else>
</s:if>
<s:else>
	<div class="tableFields"
		style="align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
