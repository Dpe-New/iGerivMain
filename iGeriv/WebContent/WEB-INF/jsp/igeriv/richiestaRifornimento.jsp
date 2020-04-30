<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>

<style>
<!--
#element_to_pop_up { display:none; }
-->
</style>

    

<s:if test="%{#request.richiesteRifornimento}">
	<s:form id="RichiestaRifornimentoForm"
		action="sonoInoltreUscite_showRichiesteRifornimenti.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div id="mainDiv" style="width: 100%">
			<dpe:table tableId="RichiestaRifornimentoTab"
				items="richiesteRifornimento" var="richiesteRifornimento"
				action="sonoInoltreUscite_showRichiesteRifornimenti.action"
				imagePath="/app_img/table/*.gif" title="${title}"
				style="height:100px;" rowsDisplayed="1000" view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFieldsLarger"
				form="RichiestaRifornimentoForm"
				theme="eXtremeTable bollaScrollDivSmall" showPagination="false"
				id="BollaScrollDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
				<ec:exportPdf fileName="richieste_rifornimento.pdf"
					tooltip="plg.esporta.pdf" headerTitle="${title}"
					headerColor="black" headerBackgroundColor="#b6c2da" />
				<ec:exportXls fileName="richieste_rifornimento.xls"
					tooltip="plg.esporta.excel" />
				<dpe:row interceptor="marker"
					href="#?livellamentoEditable=livellamentoEditable">
					<dpe:column property="sottoTitolo" width="15%"
						title="igeriv.sottotitolo" styleClass="extremeTableFieldsLarger"
						filterable="false" sortable="false" />
					<dpe:column property="numeroCopertina" width="5%"
						title="igeriv.numero" styleClass="extremeTableFieldsLarger"
						filterable="false" sortable="false" />
					<dpe:column property="prezzoCopertina" cell="currency" width="5%"
						title="igeriv.prezzo.lordo" styleClass="extremeTableFieldsLarger"
						filterable="false" sortable="false" />
					<dpe:column property="dataUscita" width="6%"
						title="igeriv.data.uscita" cell="date" format="dd/MM/yyyy"
						filterable="false" sortable="false" />
					<dpe:column property="giorniValiditaRichiesteRifornimento"
						width="6%" maxlength="3" size="3"
						title="igeriv.giorni.scadenza.richiesta"
						cell="textGiorniValiditaRifornimento"
						styleClass="extremeTableFieldsLarger" filterable="false"
						sortable="false" style="text-align:left"
						exportStyle="text-align:center" />
					<dpe:column property="quantitaRichiesta" validateIsNumeric="true"
						maxlength="3" size="3" width="5%"
						title="igeriv.richiesta.rifornimenti.quantita.richiesta"
						cell="richiestaRifornimentoTextCell"
						sessionVarName="quantitaRifornimentoMap" pkName="pk"
						hasHiddenPkField="true" enabledConditionFields="enabled"
						enabledConditionValues="true" filterable="false" sortable="false"
						style="text-align:left" exportStyle="text-align:center" />
					<dpe:column property="noteVendita" maxlength="250" size="35"
						width="25%" title="igeriv.richiesta.rifornimenti.quantita.note"
						cell="textNoteRifornimento" sessionVarName="noteVenditaMap"
						styleClass="extremeTableFieldsLarger" filterable="false"
						sortable="false" />
					<dpe:column property="fornito" width="5%" title="igeriv.fornito"
						styleClass="extremeTableFieldsLarger" filterable="false"
						sortable="false" style="text-align:center" />
					<dpe:column property="giacenza" width="5%"
						title="igeriv.giacienza.ext" filterable="false" sortable="false"
						style="text-align:center" />
					<ec:column property="fake" width="5%" title="${titleSemaforo}"
						filterable="false" sortable="false" viewsDenied="pdf,xls"
						style="text-align:center" />
					<dpe:column property="immagine" title="igeriv.img"
						filterable="false" viewsDenied="pdf,xls" width="3%"
						sortable="false"
						cell="viewImageCell" >
<%-- 						<a href="/immagini/${pageScope.richiesteRifornimento.immagine}" --%>
<!-- 							rel="thumbnail"><img src="/app_img/camera.gif" width="15px" -->
<!-- 							height="15px" border="0" style="border-style: none" /></a> -->
					</dpe:column>
				</dpe:row>
			</dpe:table>
			<div class="tableFields" id="prenotazioneDiv"
				style="font-size: 12px; width: 100%; text-align: left; font-weight: bold; float: left;">
				<div style="float: left;">
					<s:text name="igeriv.prenotazione" />
					&nbsp;&nbsp;&nbsp;
					<s:textfield name="quantitaVariazioneServizio"
						id="quantitaVariazioneServizio" cssClass="extremeTableFields"
						cssStyle="width:50px;" disabled="%{prenotazioneDisabled}" />
				</div>
				<div style="float: left;">
					<s:text name="igeriv.motivo" />
					&nbsp;&nbsp;&nbsp;
					<s:textfield name="prenotazioneVo.motivoRichiesta"
						id="prenotazioneVo.motivoRichiesta" cssClass="extremeTableFields"
						cssStyle="width:150px;" disabled="%{prenotazioneDisabled}" />
				</div>
				<div style="float: left;">
					<s:text name="igeriv.data.richiesta" />
					&nbsp;&nbsp;&nbsp;
					<s:textfield name="prenotazioneVo.pk.dataRichiesta"
						id="prenotazioneVo.pk.dataRichiesta" cssClass="extremeTableFields"
						cssStyle="width:80px;" disabled="true">
						<s:param name="value">
							<s:date name="prenotazioneVo.pk.dataRichiesta"
								format="dd/MM/yyyy" />
						</s:param>
					</s:textfield>
				</div>
				<div style="float: left;">
					<s:text name="igeriv.data.ultima.trasmissione.dl" />
					&nbsp;&nbsp;&nbsp;
					<s:textfield name="prenotazioneVo.dataUltimaTrasmissioneDl"
						id="prenotazioneVo.dataUltimaTrasmissioneDl"
						cssClass="extremeTableFields" cssStyle="width:80px;"
						disabled="true">
						<s:param name="value">
							<s:date name="prenotazioneVo.dataUltimaTrasmissioneDl"
								format="dd/MM/yyyy" />
						</s:param>
					</s:textfield>
					<s:hidden name="prenotazioneVo.pk.codDl" />
					<s:hidden name="prenotazioneVo.pk.codicePubblicazione" />
					<s:hidden name="prenotazioneVo.pk.codEdicola" />
					<s:hidden name="prenotazioneVo.pk.dataRichiesta" />
					<s:hidden name="prenotazioneVo.dataUltimaTrasmissioneDl" />
					<s:hidden name="pkSel" id="pkSel" />
				</div>
			</div>
		</div>
		<div style="width: 450px; margin-top: 0px; margin-left: 330px;">
			<div
				style="float: left; width: 250px; height: 50px; margin-top: 10px;">
				<s:if test="%{isQuotidiano != null && isQuotidiano}">
					<input type="button" value="<s:text name='igeriv.settimana'/>"
						name="igeriv.settimana" id="settimana" class="tableFields"
						style="width: 100px; text-align: center" />
				</s:if>
				<input type="button" value="<s:text name='igeriv.memorizza'/>"
					name="igeriv.memorizza" id="memorizzaRifornimenti"
					class="tableFields" style="width: 100px; text-align: center"
					onclick="javascript: setTimeout(function() {return (validateFields('RichiestaRifornimentoForm') && setFormAction('RichiestaRifornimentoForm','sonoInoltreUscite_saveRichiestaRifornimento.action', '', 'messageDiv', false, '', '', '', false, function() {$('input:text[name*=quantitaRifornimentoMap]').not(':disabled').first().focus();}));},10);" />
			</div>
		</div>
	</s:form>
	</s:if>
	



<s:if test="%{#request.dettaglioEsitorichiesteRifornimento}"> 
	<s:form id="EsitoRichiestaRifornimentoForm"
		action="sonoInoltreUscite_showRichiesteRifornimenti.action"
		method="POST" theme="simple" validate="false"
		onsubmit="return (ray.ajax())">
		<div id="mainDiv" style="width: 100%">
			<dpe:table tableId="EsitoRichiestaRifornimentoTab"
				items="dettaglioEsitorichiesteRifornimento" var="dettaglioEsitorichiesteRifornimento"
				action="sonoInoltreUscite_showRichiesteRifornimenti.action"
				imagePath="/app_img/table/*.gif" title="${title_DettaglioEsitoRifornimenti}"
				style="height:100px;" rowsDisplayed="1000" view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFieldsLarger"
				form="EsitoRichiestaRifornimentoForm"
				theme="eXtremeTable bollaScrollDivSmall" showPagination="false"
				id="BollaScrollDiv" toolbarClass="eXtremeTable"
				footerStyle="height:30px;" filterable="false">
<%-- 				<ec:exportPdf fileName="richieste_rifornimento.pdf" --%>
<%-- 					tooltip="plg.esporta.pdf" headerTitle="${title}" --%>
<%-- 					headerColor="black" headerBackgroundColor="#b6c2da" /> --%>
<%-- 				<ec:exportXls fileName="richieste_rifornimento.xls" --%>
<%-- 					tooltip="plg.esporta.excel" /> --%>
				<dpe:row interceptor="marker"
					href="#?livellamentoEditable=livellamentoEditable">
					
					<dpe:column property="dataOrdine" width="15%"
						title="igeriv.data.ordine" cell="date" format="dd/MM/yyyy"
						filterable="false" sortable="false" />
					<dpe:column property="dataScadenza" width="15%"
						title="igeriv.data.scadenza.richiesta" cell="date" format="dd/MM/yyyy"
						filterable="false" sortable="false" />
					<dpe:column property="quantitaRichiesta" validateIsNumeric="true"
						maxlength="3" size="3" width="10%"
						title="igeriv.richiesta.rifornimenti.quantita.richiesta"
						filterable="false" sortable="false"/>
					<dpe:column property="quantitaEvasa" validateIsNumeric="true"
						maxlength="3" size="3" width="10%"
						title="igeriv.richiesta.rifornimenti.quantita.evasa"
						filterable="false" sortable="false"/>
					<dpe:column property="dataRispostaDl" width="15%"
						title="igeriv.richiesta.rifornimenti.data.risposta" cell="date" format="dd/MM/yyyy"
						filterable="false" sortable="false" />
					<dpe:column property="descCausaleNonEvadibilita" maxlength="250" size="35"
						width="35%" title="igeriv.richiesta.rifornimenti.risposta.dl"
						filterable="false" sortable="false" />
					
					
				</dpe:row>
			</dpe:table>
			
		</div>
		
	</s:form>
	</s:if>

	
	
	
	<s:if
		test="%{#request.statistica != null && !#request.statistica.isEmpty()}">
		<s:form id="StatisticaForm"
			action="statisticaPubblicazioni_showNumeriPubblicazioni.action"
			method="POST" theme="simple" validate="false"
			onsubmit="return (ray.ajax())">
			<div id="statisticaDiv" style="margin-top: 60px">
				<div style="width: 100%">
					<dpe:table tableId="StatisticaTab" items="statistica"
						var="statistica"
						action="${pageContext.request.contextPath}/statisticaPubblicazioni_showNumeriPubblicazioni.action"
						imagePath="/app_img/table/*.gif" title="${titleStatistica}"
						style="height:60px;" view="buttonsOnBottom"
						locale="${localeString}"
						state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
						styleClass="extremeTableFieldsLarger" form="StatisticaForm"
						theme="eXtremeTable bollaScrollMiniDiv" showPagination="false"
						id="StatisticaScrollDiv" toolbarClass="eXtremeTable"
						showStatusBar="false" showTitle="true" showTooltips="false"
						filterable="false" sortable="false">
						<ec:row style="height:20px">
							<ec:column property="numeroCopertina" width="15%"
								title="igeriv.numero" filterable="false" sortable="false" />
							<s:if test="%{authUser.visualizzaResoRiscontratoStatistica}">
								<ec:column property="sottoTitolo" width="15%"
									title="igeriv.sottotitolo" filterable="false" sortable="false" />
							</s:if>
							<s:else>
								<ec:column property="sottoTitolo" width="20%"
									title="igeriv.sottotitolo" filterable="false" sortable="false" />
							</s:else>
							<dpe:column property="dataUscita" width="10%"
								title="igeriv.data.uscita" cell="date" dateFormat="dd/MM/yyyy"
								filterable="false" sortable="false" />
							<ec:column property="prezzoCopertina" width="5%"
								title="igeriv.prezzo" cell="currency" filterable="false"
								sortable="false" style="text-align:right" />
							<ec:column property="fornito" width="5%" title="igeriv.fornito"
								filterable="false" sortable="false" style="text-align:center" />
							<s:if test="%{authUser.visualizzaResoRiscontratoStatistica}">
								<dpe:column property="reso" width="5%"
									title="igeriv.reso.dichiarato" allowZeros="true"
									filterable="false" sortable="false" style="text-align:center" />
								<ec:column property="resoRiscontrato" width="5%"
									title="igeriv.reso.riscontrato" filterable="false"
									sortable="false" style="cursor:pointer; text-align:center" />
							</s:if>
							<s:else>
								<dpe:column property="reso" width="5%" title="igeriv.reso"
									allowZeros="true" filterable="false" sortable="false"
									style="text-align:center" />
							</s:else>
							<dpe:column property="venduto" width="5%" title="igeriv.venduto"
								allowZeros="true" filterable="false" sortable="false"
								style="text-align:center" />
							<ec:column property="giacenza" width="5%"
								title="igeriv.giacienza.ext" filterable="false" sortable="false"
								style="text-align:center" />
							<ec:column property="vendite" width="5%"
								title="gp.report.vendite.minicard" filterable="false"
								sortable="false" style="cursor:pointer; text-align:center" />
						</ec:row>
					</dpe:table>
				</div>
			</div>
		</s:form>
	</s:if>
	
	
	
	 <div id="element_to_pop_up">Content of popup</div>