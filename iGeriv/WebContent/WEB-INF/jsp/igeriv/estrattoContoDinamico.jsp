<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top: 30px">
	<s:if
		test="%{#request.listEstrattoContoDinamico != null && !#request.listEstrattoContoDinamico.isEmpty()}">
		<s:form id="EstrattoContoDinamicoForm"
			action="estrattoContoDinamico_show.action" method="POST"
			theme="simple" validate="false" onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:ec_table tableId="EstrattoContoDinamicoTab"
					items="listEstrattoContoDinamico" var="listEstrattoContoDinamico"
					action="estrattoContoDinamico_show.action"
					imagePath="/app_img/table/*.gif"
					style="height:60px; font-size:13px;" view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.EstrattoContoDinamicoAction"
					styleClass="extremeTableFields" form="EstrattoContoDinamicoForm"
					theme="eXtremeTable bollaScrollDivTall" showPagination="false"
					id="EstrattoContoDinamicoScrollDiv" toolbarClass="eXtremeTable"
					showStatusBar="false" showTitle="false" showTooltips="false"
					footerStyle="height:30px;" filterable="false"
					filterRowsCallback="it.dpe.igeriv.web.extremecomponents.filter.EstrattoContoDinamicoFilterRows">
					<dpe:exportPdf fileName="estrattoConto_${data}.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="<fo:block text-align='center'>${title}</fo:block><br><fo:block text-align-last='justify'>${intestazioneAg}<fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${codAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block><fo:block text-align-last='justify'>${pivaAgenzia}<fo:leader leader-pattern='space'/>${pivaEdicola}</fo:block>"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" regionBeforeExtentInches="1.5"
						marginTopInches="1.3" logoImage="/app_img/rodis.gif" />
					<ec:exportXls fileName="estrattoConto_${data}.xls"
						tooltip="plg.esporta.excel" />
					<dpe:row interceptor="marker" style="height:25px">
						<ec:column property="dataMovimento" cell="date"
							format="dd/MM/yyyy" width="10%" title="igeriv.data.movimento"
							filterable="false" sortable="false" style="text-align:center"
							headerStyle="text-align:center" />
						<dpe:column property="descrizione" width="31%"
							title="igeriv.descrizione" filterable="false" sortable="false" />
						<dpe:column property="importoDare" cell="currency" calc="total"
							calcTitle="column.calc.total" totalFormat="###,##0.00"
							totalCellStyle="text-align:right" hasCurrencySign="true"
							width="28%" title="igeriv.dare" filterable="false"
							sortable="false" style="text-align:right"
							headerStyle="text-align:right" format="###,###,##0.00" />
						<dpe:column property="importoAvere" cell="currency" calc="total"
							totalFormat="###,##0.00" totalCellStyle="text-align:right"
							hasCurrencySign="true" width="28%" title="igeriv.avere"
							filterable="false" sortable="false" style="text-align:right"
							headerStyle="text-align:right" format="###,###,##0.00" />
						<ec:column property="fake" width="2%" title=" " filterable="false"
							sortable="false" viewsDenied="pdf,xls" />
					</dpe:row>
				</dpe:ec_table>
			</div>
		</s:form>
	</s:if>
	<s:else>
		<div class="tableFields"
			style="align: center; text-align: center; margin-top: 50px">
			<s:text name="igeriv.nessun.risultato" />
		</div>
	</s:else>
</div>