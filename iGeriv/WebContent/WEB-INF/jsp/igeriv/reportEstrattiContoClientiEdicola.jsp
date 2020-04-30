<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.clienti != null && !#request.clienti.isEmpty()}">
	<s:form id="EstrattoContoClientiForm"
		action="estrattoContoClientiEdicola_showClientiReportEstrattoConto.action"
		method="POST" theme="simple" validate="false">
		<div id="mainDiv" style="width: 100%">
			<dpe:table tableId="GestioneClientiTab" items="clienti" var="clienti"
				action="${pageContext.request.contextPath}/estrattoContoClientiEdicola_showClientiReportEstrattoConto.action"
				imagePath="/app_img/table/*.gif" style="height:60px;"
				view="buttonsOnBottom" locale="${localeString}"
				state="it.dpe.igeriv.web.actions.EstrattoContoClientiEdicolaAction"
				styleClass="extremeTableFields" form="EstrattoContoClientiForm"
				theme="eXtremeTable bollaScrollDivSmall" showPagination="false"
				id="EstrattoContoScrollDiv" toolbarClass="eXtremeTable"
				showStatusBar="false" showTitle="false" showTooltips="false"
				footerStyle="height:30px;" filterable="false">
				<dpe:exportPdf fileName="report_estratti_conto_clienti.pdf"
					tooltip="plg.esporta.pdf"
					headerTitle="igeriv.emissione.estratto.conto.clienti"
					headerColor="black" headerBackgroundColor="#b6c2da"
					isLandscape="false" />
				<ec:exportXls fileName="report_estratti_conto_clienti.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row highlightRow="true" interceptor="marker"
					href="#?idCliente=codCliente" style="cursor:pointer;height:30px;">
					<ec:column property="nome" width="12%" title="dpe.nome"
						filterable="false" />
					<ec:column property="cognome" width="12%" title="dpe.cognome"
						filterable="false" />
					<ec:column property="localitaDesc" width="15%" title="dpe.localita"
						filterable="false" />
					<ec:column property="provinciaDesc" width="10%"
						title="dpe.provincia" filterable="false" />
					<ec:column property="cap" width="5%" title="dpe.cap"
						filterable="false" />
					<ec:column property="telefono" width="8%" title="dpe.telefono"
						filterable="false" />
					<ec:column property="email" width="15%" title="dpe.email"
						filterable="false" />
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
<div id="one"></div>

