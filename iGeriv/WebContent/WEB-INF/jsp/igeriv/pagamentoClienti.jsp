<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.clienti != null && !#request.clienti.isEmpty()}">
	<s:form id="PagamentoClientiForm" action="pagamenti_showClienti.action"
		method="POST" theme="simple" validate="false">
		<div id="mainDiv" style="width: 100%">
			<s:if test='contiDaPagare eq "false"'>
				<dpe:table tableId="PagamentoClientiTab" items="clienti"
					var="clienti"
					action="${pageContext.request.contextPath}/pagamenti_showClienti.action"
					imagePath="/app_img/table/*.gif" style="height:60px;"
					view="buttonsOnBottom" locale="${localeString}"
					state="it.dpe.igeriv.web.actions.EstrattoContoClientiEdicolaAction"
					styleClass="extremeTableFields" form="PagamentoClientiForm"
					theme="eXtremeTable bollaScrollDivSmall" showPagination="false"
					id="EstrattoContoScrollDiv" toolbarClass="eXtremeTable"
					showStatusBar="false" showTitle="false" showTooltips="false"
					footerStyle="height:30px;" filterable="false"
					autoIncludeParameters="true">
					<dpe:exportPdf fileName="pagamento_clienti.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="igeriv.gestione.pagamenti.clienti"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls fileName="pagamento_clienti.xls"
						tooltip="plg.esporta.excel" />
					<dpe:row highlightRow="true" interceptor="marker"
						href="#?idCliente=codCliente" style="height:30px;">
						<dpe:column property="nomeCognome" width="17%" title="dpe.nome"
							filterable="false" />
						<dpe:column property="localitaDesc" width="10%"
							title="dpe.localita" filterable="false" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="provinciaDesc" width="6%"
							title="dpe.provincia" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="telefono" width="10%" title="dpe.telefono"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="email" width="15%" title="dpe.email"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="metodoPagamento" width="8%"
							title="igeriv.tipo.pagamento" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="tipoDocumentoDesc" width="10%"
							title="igeriv.tipo.documento" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="dataDocumento" width="8%" cell="date"
							format="dd/MM/yyyy" title="igeriv.data.documento"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="numeroDocumento" width="8%"
							title="igeriv.numero.documento" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="importoTotale" cell="currency"
							hasCurrencySign="true" width="8%"
							title="igeriv.pne.report.magazzino.importo" filterable="false"
							sortable="false" style="text-align:right"
							headerStyle="text-align:right" format="###,###,##0.00"
							exportStyle="text-align:right" />
					</dpe:row>
				</dpe:table>
			</s:if>
			<s:else>
				<dpe:table tableId="PagamentoClientiTab" items="clienti"
					var="clienti"
					action="${pageContext.request.contextPath}/pagamenti_showClienti.action"
					imagePath="/app_img/table/*.gif" style="height:60px;"
					view="buttonsOnBottom" locale="${localeString}"
					state="it.dpe.igeriv.web.actions.EstrattoContoClientiEdicolaAction"
					styleClass="extremeTableFields" form="PagamentoClientiForm"
					theme="eXtremeTable bollaScrollDivSmall" showPagination="false"
					id="EstrattoContoScrollDiv" toolbarClass="eXtremeTable"
					showStatusBar="false" showTitle="false" showTooltips="false"
					footerStyle="height:30px;" filterable="false"
					autoIncludeParameters="false">
					<dpe:exportPdf fileName="pagamento_clienti.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="igeriv.gestione.pagamenti.clienti"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls fileName="pagamento_clienti.xls"
						tooltip="plg.esporta.excel" />
					<dpe:row highlightRow="true" interceptor="marker"
						href="#?idCliente=codCliente" style="height:30px;">
						<dpe:column property="nomeCognome" width="15%" title="dpe.nome"
							filterable="false" />
						<dpe:column property="localitaDesc" width="9%"
							title="dpe.localita" filterable="false" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="provinciaDesc" width="6%"
							title="dpe.provincia" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="telefono" width="8%" title="dpe.telefono"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="email" width="15%" title="dpe.email"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="metodoPagamento" width="8%"
							title="igeriv.tipo.pagamento" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="tipoDocumentoDesc" width="8%"
							title="igeriv.tipo.documento" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="dataDocumento" width="8%" cell="date"
							format="dd/MM/yyyy" title="igeriv.data.documento"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="numeroDocumento" width="8%"
							title="igeriv.numero.documento" filterable="false"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="importoTotale" cell="currency"
							hasCurrencySign="true" width="8%"
							title="igeriv.pne.report.magazzino.importo" filterable="false"
							sortable="false" style="text-align:right"
							headerStyle="text-align:right" format="###,###,##0.00"
							exportStyle="text-align:right" />
						<dpe:column property="fake" alias="spunta" width="5%"
							title="igeriv.importo.pagato" cell="pgtoClientiSpunta"
							headerStyle="text-align:center" style="text-align:center"
							filterable="false" sortable="false" />
					</dpe:row>
				</dpe:table>
			</s:else>
		</div>
		<s:if test='contiDaPagare eq "true"'>
			<div style="width: 100%;">
				<div style="text-align: center;">
					<input type="button" value="<s:text name='igeriv.memorizza'/>"
						name="igeriv.memorizza" id="memorizza"
						style="width: 100px; text-align: center"
						onclick="javascript: setFormAction('PagamentoClientiForm','${pageContext.request.contextPath}/pagamenti_savePagamentoClienti.action', '', 'messageDiv', false, '', function() { doRicerca(); }, function() { return checkFields(); }, '', false, '');" />
				</div>
			</div>
		</s:if>
		<s:hidden name="contiDaPagare" />
		<s:hidden name="nome" />
		<s:hidden name="cognome" />
		<s:hidden name="codiceFiscale" />
		<s:hidden name="piva" />
		<s:hidden name="strDataCompetenzaDa" />
		<s:hidden name="strDataCompetenzaA" />
	</s:form>
</s:if>
<s:else>
	<div class="tableFields"
		style="width: 100%; align: center; text-align: center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
<div id="one"></div>

