<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:form id="EstrattoContoPopupClientiForm"
	action="estrattoContoClientiEdicola_showClientiReportEstrattoConto.action"
	method="POST" theme="simple" validate="false">
	<div id="mainDiv" style="width: 100%; text-align: center">
		<fieldset class="filterBolla" style="height: 100px">
			<legend style="font-size: 100%">
				<s:property value="tableTitle" escape="false" />
			</legend>
			<div class="required">
				<div
					style="margin-left: auto; margin-right: auto; text-align: center; width: 350px; white-space: nowrap;">
					<s:text name="username.cliente" />
					:&nbsp;&nbsp;
					<s:text name="nomeCliente" />
				</div>
			</div>
			<div class="required">
				<div
					style="margin-left: auto; margin-right: auto; text-align: center; width: 350px; white-space: nowrap;">
					<s:text name="igeriv.data.competenza.estratto.conto" />
					&nbsp;&nbsp;
					<s:select name="strDataA" id="strDataA" list="listEstrattiConto"
						listKey="key" listValue="value" cssStyle="width:130px"
						emptyOption="true"
						onchange="javascript: changeDataEstrattoConto(this)" />
				</div>
			</div>
		</fieldset>
	</div>
	<div id="mainDiv" style="width: 100%">
		<dpe:table tableId="EstrattoContoClientiTab"
			items="listEstrattoContoDettaglio" var="listEstrattoContoDettaglio"
			autoIncludeParameters="true"
			action="${pageContext.request.contextPath}/estrattoContoClientiEdicola_showClientiReportEstrattoConto.action"
			imagePath="/app_img/table/*.gif" style="height:60px;"
			view="buttonsOnBottom" locale="${localeString}"
			state="it.dpe.igeriv.web.actions.EstrattoContoAction"
			styleClass="extremeTableFields tablesorter"
			form="EstrattoContoPopupClientiForm"
			theme="eXtremeTable bollaScrollDivSmall" showPagination="false"
			id="EstrattoContoScrollDiv" toolbarClass="eXtremeTable"
			showStatusBar="false" showTitle="false" showTooltips="false"
			footerStyle="height:30px;" filterable="false">
			<dpe:row interceptor="marker" style="height:25px">
				<dpe:column property="titolo" width="55%" title="igeriv.titolo"
					filterable="false" sortable="false" />
				<dpe:column property="prezzo" cell="currency" hasCurrencySign="true"
					width="15%" title="igeriv.prezzo" filterable="false"
					sortable="false" style="text-align:center"
					headerStyle="text-align:center" format="###,###,##0.00"
					exportStyle="text-align:center" />
				<dpe:column property="quantita" width="15%" title="igeriv.quantita"
					cell="date" dateFormat="dd/MM/yyyy HH:mi:ss" filterable="false"
					sortable="false" style="text-align:center"
					headerStyle="text-align:center" exportStyle="text-align:center" />
				<dpe:column property="importo" cell="currency"
					hasCurrencySign="true" width="15%"
					title="igeriv.pne.report.magazzino.importo" filterable="false"
					sortable="false" style="text-align:center"
					headerStyle="text-align:center" format="###,###,##0.00"
					exportStyle="text-align:center" />
			</dpe:row>
		</dpe:table>
	</div>
	<div style="text-align: center; margin-top: 15px;">
		<s:text name="label.print.Table.Total" />
		:&nbsp;&nbsp;<span id="impTotale"
			style="font-size: 18px; font-weight: bold;"></span>
	</div>
	<div style="text-align: center; margin-top: 15px;">
		<a href="#" onclick="javascript:printEC();"><img id="imgPrintEC"
			src="/app_img/print_48x48.png" width="25" height="25"
			title="<s:text name="tooltip.main_frame.Print.Estratto.Conto"/>"
			alt="<s:text name="tooltip.main_frame.Print.Estratto.Conto"/>"
			border="0" /></a> <span id="deleteECSpan">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="#"
			onclick="javascript:deleteEC();"><img id="imgDeleteEC"
				src="/app_img/delete.gif" width="25" height="28"
				title="<s:text name="tooltip.main_frame.Delete.Estratto.Conto"/>"
				alt="<s:text name="tooltip.main_frame.Delete.Estratto.Conto"/>"
				border="0" /></a>
		</span>
	</div>
	<s:hidden name="dataEstrattoConto" id="dataEstrattoConto" />
</s:form>
<div id="one"></div>
