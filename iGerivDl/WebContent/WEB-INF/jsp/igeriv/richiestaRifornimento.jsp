<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.richiesteRifornimento}">	
	<s:form id="RichiestaRifornimentoForm" action="sonoInoltreUscite_showRichiesteRifornimenti.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div id="mainDiv" style="width:100%">		
				<dpe:table
					tableId="RichiestaRifornimentoTab"
					items="richiesteRifornimento"
					var="richiesteRifornimento" 
					action="sonoInoltreUscite_showRichiesteRifornimenti.action"
					imagePath="/app_img/table/*.gif"
					title="${title}"
					style="height:100px;"
					rowsDisplayed="1000"
					view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
					styleClass="extremeTableFieldsLarger"
					form="RichiestaRifornimentoForm"
					theme="eXtremeTable bollaScrollDivSmall"			
					showPagination="false"
					id="BollaScrollDiv"
					toolbarClass="eXtremeTable"
					footerStyle="height:30px;"
					filterable="false"
					>
					<ec:exportPdf         
						fileName="richieste_rifornimento.pdf"         
						tooltip="plg.esporta.pdf" 
						headerTitle="${title}" 
						headerColor="black"         
						headerBackgroundColor="#b6c2da"    
					/>
					<ec:exportXls     
						fileName="richieste_rifornimento.xls"       
						tooltip="plg.esporta.excel"/>		
					<ec:row>			
						<dpe:column property="sottoTitolo" width="15%" title="igeriv.sottotitolo" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/> 
						<dpe:column property="numeroCopertina" width="5%" title="igeriv.numero" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>                    
						<dpe:column property="prezzoCopertina" cell="currency" width="5%" title="igeriv.prezzo.lordo" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>              
						<dpe:column property="dataUscita" width="6%" title="igeriv.data.uscita" cell="date" format="dd/MM/yyyy" filterable="false" sortable="false"/>
						<dpe:column property="giorniValiditaRichiesteRifornimento" width="6%" maxlength="3" size="3" title="igeriv.giorni.scadenza.richiesta" cell="textDifferenza" sessionVarName="dataScadenzaRichiesta" pkName="pk" hasHiddenPkField="true" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" style="text-align:left" exportStyle="text-align:center"/>
						<dpe:column property="quantitaRichiesta" validateIsNumeric="true" maxlength="3" size="3" width="5%" title="igeriv.richiesta.rifornimenti.quantita.richiesta" cell="textDifferenza" sessionVarName="quantitaRifornimento" pkName="pk" hasHiddenPkField="false" enabledConditionFields="enabled" enabledConditionValues="true" filterable="false" sortable="false" style="text-align:left" exportStyle="text-align:center"/>
						<dpe:column property="noteVendita" maxlength="250" size="35" width="30%" title="igeriv.richiesta.rifornimenti.quantita.note" cell="textDifferenza" sessionVarName="noteVendita" pkName="pk" hasHiddenPkField="false" enabledConditionFields="enabled" enabledConditionValues="true" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>
						<dpe:column property="fornito" width="5%" title="igeriv.fornito" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" style="text-align:center"/>
						<dpe:column property="giacenza" width="5%" title="igeriv.giacienza.ext" filterable="false" sortable="false" style="text-align:center"/>
						<ec:column property="fake" width="5%" title="${titleSemaforo}" filterable="false" sortable="false" viewsDenied="pdf,xls" style="text-align:center"/>
					</ec:row>
				</dpe:table> 					
				<div class="tableFields" id="prenotazioneDiv" style="font-size:12px; width:100%; text-align:left; font-weight:bold; float:left;">
					<div style="float:left;"><s:text name="igeriv.prenotazione"/>&nbsp;&nbsp;&nbsp;<s:textfield name="quantitaVariazioneServizio" id="quantitaVariazioneServizio" cssClass="extremeTableFields" cssStyle="width:50px;" disabled="%{prenotazioneDisabled}"/></div>
					<div style="float:left;"><s:text name="igeriv.motivo"/>&nbsp;&nbsp;&nbsp;<s:textfield name="prenotazioneVo.motivoRichiesta" id="prenotazioneVo.motivoRichiesta" cssClass="extremeTableFields" cssStyle="width:150px;" disabled="%{prenotazioneDisabled}"/></div>
					<div style="float:left;">
						<s:text name="igeriv.data.richiesta"/>&nbsp;&nbsp;&nbsp;
						<s:textfield name="prenotazioneVo.pk.dataRichiesta" id="prenotazioneVo.pk.dataRichiesta" cssClass="extremeTableFields" cssStyle="width:80px;" disabled="true">
							<s:param name="value">
								<s:date name="prenotazioneVo.pk.dataRichiesta" format="dd/MM/yyyy"/>
							</s:param>
						</s:textfield>
					</div>
					<div style="float:left;">
						<s:text name="igeriv.data.ultima.trasmissione.dl"/>&nbsp;&nbsp;&nbsp;
						<s:textfield name="prenotazioneVo.dataUltimaTrasmissioneDl" id="prenotazioneVo.dataUltimaTrasmissioneDl" cssClass="extremeTableFields" cssStyle="width:80px;" disabled="true">
							<s:param name="value">
								<s:date name="prenotazioneVo.dataUltimaTrasmissioneDl" format="dd/MM/yyyy"/>
							</s:param>
						</s:textfield>	
						<s:hidden name="prenotazioneVo.pk.codDl"/>				
						<s:hidden name="prenotazioneVo.pk.codicePubblicazione"/>
						<s:hidden name="prenotazioneVo.pk.codEdicola"/>	
						<s:hidden name="prenotazioneVo.pk.dataRichiesta"/>													
						<s:hidden name="prenotazioneVo.dataUltimaTrasmissioneDl"/>
						<s:hidden name="pkSel" id="pkSel"/>
					</div>
				</div>			
		</div>	
		<div style="width:450px; margin-top:0px; margin-left:330px;">
			<div style="float:left; width:200px; height:50px; margin-top:10px; margin-left:60px">
				<input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizzaRifornimenti" class="tableFields" style="width:100px; text-align:center" onclick="javascript: setTimeout(function() {return (validateFields('RichiestaRifornimentoForm') && setFormAction('RichiestaRifornimentoForm','sonoInoltreUscite_saveRichiestaRifornimento.action', '', 'messageDiv', false, '', '', '', false));},10);"/>
			</div>
		</div>	
	</s:form>
	<s:if test="%{#request.statistica != null && !#request.statistica.isEmpty()}">
		<s:form id="StatisticaForm" action="statisticaPubblicazioni_showNumeriPubblicazioni.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
			<div id="statisticaDiv" style="margin-top:60px">
					<div style="width:100%">					
						<dpe:table
							tableId="StatisticaTab" 
							items="statistica"
							var="statistica" 
							action="${pageContext.request.contextPath}/statisticaPubblicazioni_showNumeriPubblicazioni.action"						
							imagePath="/app_img/table/*.gif"	
							title="${titleStatistica}"			
							style="height:60px;"		
							view="buttonsOnBottom"
							locale="${localeString}"
							state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
							styleClass="extremeTableFieldsLarger"	
							form="StatisticaForm"		
							theme="eXtremeTable bollaScrollMiniDiv"			
							showPagination="false"
							id="StatisticaScrollDiv"
							toolbarClass="eXtremeTable" 
							showStatusBar="false"
							showTitle="true"
							showTooltips="false"
							filterable="false"
							sortable="false"
							>
							<ec:row style="height:20px">				
								<ec:column property="numeroCopertina" width="15%" title="igeriv.numero" filterable="false" sortable="false"/>         
								<ec:column property="sottoTitolo" width="15%" title="igeriv.sottotitolo" filterable="false" sortable="false"/>
								<dpe:column property="dataUscita" width="10%" title="igeriv.data.uscita" cell="date" dateFormat="dd/MM/yyyy" filterable="false" sortable="false" />
								<ec:column property="prezzoCopertina" width="5%" title="igeriv.prezzo" cell="currency" filterable="false" sortable="false"  style="text-align:right"/>
								<ec:column property="fornito" width="5%" title="igeriv.fornito" filterable="false" sortable="false" style="text-align:center"/>
								<dpe:column property="reso" width="5%" title="igeriv.reso.dichiarato" allowZeros="true" filterable="false" sortable="false" style="text-align:center"/> 
								<ec:column property="resoRiscontrato" width="5%" title="igeriv.reso.riscontrato" filterable="false" sortable="false" style="cursor:pointer; text-align:center"/>
								<dpe:column property="venduto" width="5%" title="igeriv.venduto" allowZeros="true" filterable="false" sortable="false" style="text-align:center"/>
								<ec:column property="giacenza" width="5%" title="igeriv.giacienza.ext" filterable="false" sortable="false" style="text-align:center"/>
								<ec:column property="vendite" width="5%" title="gp.report.vendite.minicard" filterable="false" sortable="false" style="cursor:pointer; text-align:center"/>
							</ec:row>
						</dpe:table>	 	
					</div>
			</div>
		</s:form>
	</s:if>
</s:if>