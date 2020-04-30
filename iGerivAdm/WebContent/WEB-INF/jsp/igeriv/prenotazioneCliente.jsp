<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.ordini}">	
	<s:form id="PrenotazioneClienteForm" action="bollaRivendita_showPrenotazioniClienteEdicola.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
	<div id="mainDiv" style="width:100%">		
			<dpe:table
				tableId="PrenotazioneCliente_table"
				items="ordini"
				var="ordini" 
				action="bollaRivendita_showPrenotazioniClienteEdicola.action"
				imagePath="/app_img/table/*.gif"
				title="${title}"
				style="height:100px;"
				rowsDisplayed="1000"
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaRivenditaAction"
				styleClass="extremeTableFieldsLarger"
				form="PrenotazioneClienteForm"
				theme="eXtremeTable bollaScrollDivSmall"			
				showPagination="false"
				id="BollaScrollDiv"
				toolbarClass="eXtremeTable"
				footerStyle="height:30px;"
				filterable="false"
				>
				<ec:exportPdf         
					fileName="prenotazioni_clienti.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="${title}" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
				/>
				<ec:exportXls     
					fileName="prenotazioni_clienti.xls"       
					tooltip="plg.esporta.excel"/>		
				<ec:row>			
					<dpe:column property="titolo" width="15%" title="igeriv.titolo" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>
					<dpe:column property="sottoTitolo" width="15%" title="igeriv.sottotitolo" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/> 
					<dpe:column property="numeroCopertina" width="5%" title="igeriv.numero" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>                    
					<dpe:column property="prezzoCopertina" cell="currency" width="7%" title="igeriv.prezzo.lordo" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" style="text-align:right"/>              
					<dpe:column property="dataUscita" width="7%" title="igeriv.data.uscita" cell="date" format="dd/MM/yyyy" filterable="false" sortable="false" style="text-align:center"/>
					<dpe:column property="quantitaRichiesta" validateIsNumeric="true" maxlength="3" size="4" width="7%" title="igeriv.richiesta.rifornimenti.quantita.richiesta" cell="textDifferenza" sessionVarName="quantitaRifornimento" pkName="pk" hasHiddenPkField="true" enabledConditionFields="enabled" enabledConditionValues="true" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" exportStyle="text-align:center"/>
					<dpe:column property="quantitaEvasa" validateIsNumeric="true" maxlength="3" size="4" width="7%" title="igeriv.richiesta.rifornimenti.quantita.evasa" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" exportStyle="text-align:center"/>
					<dpe:column property="statoEvasioneDesc" maxlength="3" size="4" width="7%" title="igeriv.richiesta.rifornimenti.stato.evasione" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" style="text-align:center"/>
				</ec:row>
			</dpe:table> 			
			<div class="tableFields" id="prenotazioneDiv" style="font-size:12px; width:100%; text-align:left; font-weight:bold; float:left;">
				<div style="float:left;"><s:text name="igeriv.prenotazione.fissa"/>&nbsp;&nbsp;&nbsp;<s:textfield name="richiestaFissaClienteEdicolaVo.quantitaRichiesta" id="richiestaFissaClienteEdicolaVo.quantitaRichiesta" cssClass="extremeTableFields" cssStyle="width:50px;"/></div>
				<div style="float:left;"><s:text name="igeriv.numero.volte"/>&nbsp;&nbsp;&nbsp;<s:select name="richiestaFissaClienteEdicolaVo.numeroVolte" id="richiestaFissaClienteEdicolaVo.numeroVolte" list="%{#session['listNumberVo1to100']}" listKey="keyInt" listValue="value" cssClass="extremeTableFields" cssStyle="width:80px;"/></div>								
				<div style="float:left;">														
					<s:hidden name="richiestaFissaClienteEdicolaVo.pk.codEdicola"/>				
					<s:hidden name="richiestaFissaClienteEdicolaVo.pk.codCliente"/>	
					<s:hidden name="richiestaFissaClienteEdicolaVo.pk.codDl"/>	
					<s:hidden name="richiestaFissaClienteEdicolaVo.pk.codicePubblicazione"/>	
				</div>
			</div>		
			<div style="width:450px; margin-top:50px; margin-left:380px;">
				<div style="float:left; width:200px; height:50px; margin-top:10px;">
					<input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" class="tableFields" style="width:100px; text-align:center" onclick="javascript: setTimeout(function() {return (validateFields('PrenotazioneClienteForm') && enableField() && setFormAction('PrenotazioneClienteForm','bollaRivendita_savePrenotazioniClienteEdicola.action', '', 'messageDiv'));},10);"/>
				</div>
			</div>
	</div>		
</s:form>
<div id="one"></div> 
</s:if>