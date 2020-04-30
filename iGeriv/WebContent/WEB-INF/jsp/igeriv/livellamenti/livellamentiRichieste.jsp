<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>

<s:set var="href"
	value="%{'?listIdsLivellamenti=idRichiestaLivellamento'}" />


<div id="contentDiv" style="margin-top: 30px">
	<s:if
		test="%{#request.richieste != null && !#request.richieste.isEmpty()}">
		<s:form id="RichiesteReteEdicolaReportForm"
			action="livellamenti_showRichieste.action" method="POST"
			theme="simple" validate="false" onsubmit="return (ray.ajax())">
			<div style="width: 100%">
				<dpe:table tableId="RichiesteReteEdicolaReportTab" items="richieste"
					var="richieste" action="livellamenti_showRichieste.action"
					imagePath="/app_img/table/*.gif"
					style="height:371px; table-layout:auto" view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.LivellamentiAction"
					styleClass="extremeTableFields"
					form="RichiesteReteEdicolaReportForm"
					theme="eXtremeTable bollaScrollDiv" showPagination="false"
					id="VenditeReportScrollDiv" toolbarClass="eXtremeTable"
					footerStyle="height:30px;" filterable="false">
					<dpe:exportPdf
						fileName="richieste_rete_edicola_${strDataDa}_${strDataA}.pdf"
						tooltip="plg.esporta.pdf"
						headerTitle="Report Richieste Rete Edicola - Da: ${strDataDa} A ${strDataA}}"
						headerColor="black" headerBackgroundColor="#b6c2da"
						isLandscape="true" />
					<ec:exportXls
						fileName="richieste_rete_edicola_${strDataDa}_${strDataA}.xls"
						tooltip="plg.esporta.excel" />

					<dpe:row highlightRow="true" interceptor="marker"
						style="height:30px; cursor:pointer" href="${href}">
						<ec:column property="dataRichiesta" width="10%"
							title="igeriv.data.richiesta" cell="date"
							format="dd/MM/yyyy HH:mm:ss" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<ec:column property="idRichiestaLivellamento" width="8%"
							title="igeriv.richiesta.rifornimenti.numero.ordine"
							filterable="false" style="text-align:center"
							headerStyle="text-align:center" />
						<ec:column
							property="storicoCopertineVo.anagraficaPubblicazioniVo.titolo"
							width="15%" title="igeriv.titolo" filterable="false"
							style="text-align:left" />
						<ec:column property="storicoCopertineVo.numeroCopertina"
							width="8%" title="igeriv.numero" filterable="false"
							style="text-align:center" headerStyle="text-align:center" />
						<dpe:column property="storicoCopertineVo.dataUscita" width="8%"
							title="igeriv.data.uscita" cell="date" format="dd/MM/yyyy"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="quantitaRichiesta" width="8%"
							title="plg.quantita" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="totRichiesteInviate" width="8%"
							title="plg.quantita.richieste.inviate" style="text-align:center"
							headerStyle="text-align:center" exportStyle="text-align:center" />
						<dpe:column property="totRichiesteEsaminate" width="8%"
							title="plg.quantita.richieste.visualizzate"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />
						<dpe:column property="totRichiesteAccettate" width="8%"
							title="plg.quantita.richieste.accettate"
							style="text-align:center" headerStyle="text-align:center"
							exportStyle="text-align:center" />

					</dpe:row>
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