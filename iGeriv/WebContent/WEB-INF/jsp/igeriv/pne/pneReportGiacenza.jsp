<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if
	test="%{#request.reportGiacenza != null && !#request.reportGiacenza.isEmpty()}">
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>
	<s:form id="ReportGiacenzaForm"
		action="pneReportGiacenza_showGiacenza.action" method="POST"
		theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<dpe:table tableId="ReportGiacenzaTab" items="reportGiacenza"
				var="reportGiacenza"
				action="${pageContext.request.contextPath}${ap}/pneGiacenza_showGiacenza.action"
				imagePath="/app_img/table/*.gif" rowsDisplayed="1000"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ProdottiNonEditorialiGiacenzaAction"
				styleClass="extremeTableFields" form="ReportGiacenzaForm"
				theme="eXtremeTable bollaScrollDiv" showPagination="false"
				id="ReportGiacenzaDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="report_giacenza.pdf"
					tooltip="plg.esporta.pdf" headerTitle="igeriv.report.giacenza"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="true" />
				<ec:exportXls fileName="report_giacenza.xls"
					tooltip="plg.esporta.excel" />
				<ec:row style="height:25px;">
					<dpe:column property="descrizione" width="40%"
						title="igeriv.prodotto" filterable="false" />
					<dpe:column property="giacenza" width="20%"
						title="igeriv.giacienza.ext" allowZeros="true" filterable="false"
						style="text-align:center" headerStyle="text-align:center" />
<%-- 					<dpe:column property="prezzounitario" cell="currency" --%>
<%-- 						hasCurrencySign="true" format="###,###,##0.0000" width="20%" --%>
<%-- 						title="igeriv.prezzo.unitario.ext" filterable="false" --%>
<%-- 						style="text-align:right" headerStyle="text-align:right" /> --%>
<%-- 					<dpe:column property="prezzototale" cell="currency" --%>
<%-- 						hasCurrencySign="true" format="###,###,##0.0000" width="20%" --%>
<%-- 						title="igeriv.prezzo.totale.ext" filterable="false" --%>
<%-- 						style="text-align:right" headerStyle="text-align:right" /> --%>
						
								
				</ec:row>
			</dpe:table>
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
