<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
	div#filter { height:180px;}
</style>
<s:if test="%{#request.richiesteRifornimento != null && !#request.richiesteRifornimento.isEmpty()}">
	<s:set name="an" value="%{actionName1}"/>
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>	
	<s:form id="RichiesteRifornimentiClienteEdicolaForm" action="%{actionName1}" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
	<div id="mainDiv" style="width:100%">		
			<dpe:table
				tableId="RichiesteRifornimentiClienteEdicola"
				items="richiesteRifornimento"
				var="richiesteRifornimento" 
				action="${pageContext.request.contextPath}${ap}/${an}"
				imagePath="/app_img/table/*.gif"
				style="height:${tableHeight}"
				rowsDisplayed="1000" 
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ClientiEdicolaAction"
				styleClass="extremeTableFields"
				form="RichiesteRifornimentiClienteEdicolaForm"
				theme="eXtremeTable bollaScrollDivTall"			
				showPagination="false"
				id="BollaScrollDiv"
				toolbarClass="eXtremeTable"
				footerStyle="height:30px;"
				filterable="false"
				>
				<dpe:exportPdf         
					fileName="richieste_rifornimento.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="<fo:block text-align='center'>${titoloRichiesteRifornimento}</fo:block><br><fo:block text-align-last='left'>${cliente}</fo:block>" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="false"
				/>
				<ec:exportXls     
					fileName="richieste_rifornimento.xls"       
					tooltip="plg.esporta.excel"/>		
				<ec:row style="height:30px">			
					<dpe:column property="fake" width="3%" title=" " cell="prenotazioniClienteSpunta" pkName="pk" hasHiddenPkField="true" style="text-align:center" filterable="false" sortable="false" viewsDenied="pdf,xls"/>
					<dpe:column property="titolo" width="15%" title="igeriv.titolo" styleClass="extremeTableFieldsLarger" filterable="false"/>
					<dpe:column property="sottoTitolo" width="15%" title="igeriv.sottotitolo" styleClass="extremeTableFieldsLarger" filterable="false"/> 
					<dpe:column property="numeroCopertina" width="5%" title="igeriv.numero" styleClass="extremeTableFieldsLarger" filterable="false"/>                    
					<dpe:column property="dataOrdine" width="8%" title="igeriv.data.ordine" cell="date" format="dd/MM/yyyy" styleClass="extremeTableFields" style="text-align:center" headerStyle="text-align:center" filterable="false"/>              
					<ec:column property="quantitaRichiesta" width="6%" title="igeriv.richiesta.rifornimenti.quantita.richiesta" styleClass="extremeTableFields" style="text-align:center" headerStyle="text-align:center" filterable="false"/>
					<ec:column property="quantitaEvasa" width="5%" title="igeriv.richiesta.rifornimenti.quantita.evasa" styleClass="extremeTableFields" style="text-align:center" headerStyle="text-align:center" filterable="false"/>
					<ec:column property="statoEvasioneDesc" width="9%" title="igeriv.richiesta.rifornimenti.quantita.stato" styleClass="extremeTableFields" style="text-align:center" headerStyle="text-align:center" filterable="false"/>								
					<ec:column property="dataUltimaRisposta" width="8%" title="igeriv.data.ultima.evasione" cell="date" format="dd/MM/yyyy" styleClass="extremeTableFields" style="text-align:center" headerStyle="text-align:center" filterable="false"/>
					<ec:column property="provenienzaDesc" width="8%" title="igeriv.richiesta.rifornimenti.provenienza.richiesta" styleClass="extremeTableFields" style="text-align:center" headerStyle="text-align:center" filterable="false"/>
				</ec:row>
			</dpe:table> 												
	</div>	
	<div style="width:100%;">
		<div style="width:350px; height:50px; margin-top:10px; margin-right:auto; margin-left:auto">
			<input type="button" value="<s:text name='igeriv.cancella.prenotazioni.selezionate'/>" name="aaaa" id="cancellaSelezione" class="tableFields" style="width:260px; text-align:center" onclick="javascript: deleteSelected()"/>
		</div>
	</div>
</s:form>
<div id="one"></div> 
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato"/>
	</div>
</s:else>