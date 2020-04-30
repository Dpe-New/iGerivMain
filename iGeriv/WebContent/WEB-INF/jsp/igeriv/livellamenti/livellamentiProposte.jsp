<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top: 30px">
	<s:if
		test="%{#request.proposte != null && !#request.proposte.isEmpty()}">
		<s:form id="ProposteReteEdicolaReportForm"
			action="livellamenti_showProposte.action" method="POST"
			theme="simple" validate="false" onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:table tableId="ProposteReteEdicolaReportTab" items="proposte"
					var="proposte" action="livellamenti_showProposte.action"
					imagePath="/app_img/table/*.gif"
					style="height:371px; table-layout:auto" view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.LivellamentiAction"
					styleClass="extremeTableFields"
					form="ProposteReteEdicolaReportForm"
					theme="eXtremeTable bollaScrollDiv" showPagination="false"
					id="VenditeReportScrollDiv" toolbarClass="eXtremeTable"
					footerStyle="height:30px;" filterable="false">
					<dpe:exportPdf
						fileName="proposte_rete_edicola_${strDataDa}_${strDataA}.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="Report Proposte Rete Edicola - Da: ${strDataDa} A ${strDataA}}"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls
						fileName="proposte_rete_edicola_${strDataDa}_${strDataA}.xls"
						tooltip="plg.esporta.excel" />
					<ec:row style="height:25px">
						<dpe:column property="richiesta.dataRichiesta" width="10%"
							title="igeriv.data.richiesta" cell="date"
							format="dd/MM/yyyy HH:mm:ss" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column
							property="richiesta.storicoCopertineVo.anagraficaPubblicazioniVo.titolo"
							width="15%" title="igeriv.titolo" filterable="false"
							style="text-align:left" />
						<dpe:column
							property="richiesta.storicoCopertineVo.numeroCopertina"
							width="10%" title="igeriv.numero" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="richiesta.storicoCopertineVo.dataUscita"
							width="10%" title="igeriv.data.uscita" cell="date"
							format="dd/MM/yyyy" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="richiesta.quantitaRichiesta" width="10%"
							title="plg.quantita" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column
							property="richiesta.edicolaRichiedente.ragioneSocialeEdicolaPrimaRiga"
							width="15%" title="igeriv.edicola.richiedente" />
						<dpe:column
							property="richiesta.edicolaRichiedente.indirizzoViaNumeroCitta"
							width="29%" title="dpe.indirizzo" />
					</ec:row>
				</dpe:table>
			</div>
		</s:form>
	</s:if>
	<s:else>
		<div class="tableFields"
			style="width: 100%; align: center; text-align: center; margin-top: 50px">
			<s:text name="gp.nessun.risultato" />
		</div>
	</s:else>
</div>