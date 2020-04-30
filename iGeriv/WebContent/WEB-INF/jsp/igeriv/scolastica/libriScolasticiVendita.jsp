<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<s:set name="an" value="%{actionName}" />
<s:if test="%{#request.itemsOrdini != null && !#request.itemsOrdini.isEmpty()}">
	<s:form id="LibriScolasticiVenditaDettForm" action="%{actionName}"	method="POST" theme="simple" validate="false"	onsubmit="return (ray.ajax())">
		<div style="width: 100%">
			<div style="float:left">
     			<input type="button" value="Conferma consegna" name="Consegna" id="consegna" class="tableFields" onclick="doSubmitSave();"/>
				<input type="button" value="Ricevuta" name="Ricevuta" id="ricevuta" class="tableFields" onclick="doSubmitReport();">
			</div>
			<div style="float:center">
			     <h4>${cliente}</h4>
			</div>
			<div style="float:right" id="divTotale">
			   <p></p>
			</div>
		</div>
		<div style="width: 100%">
			<dpe:table 
				view="buttonsOnBottom"
				tableId="LibriScolasticiVenditaTab"
				items="itemsOrdini" 
				var="itemsOrdini"
				action="${pageContext.request.contextPath}/${an}"
				imagePath="/app_img/table/*.gif" 
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.LibriScolasticiVenditaAction"
				styleClass="extremeTableFields"
				form="LibriScolasticiVenditaForm"
				theme="eXtremeTable bollaScrollDiv" 
				showPagination="false"
				rowsDisplayed="15" 
				id="OrdiniScrollDiv1" 
				toolbarClass="eXtremeTable"
				footerStyle="height:30px;" 
				filterable="false"
				showStatusBar="false"
				autoIncludeParameters="false" sortable="false"
				beforeUnloadValidationScript="">
				<dpe:row style="height:30px; cursor:pointer">
					<dpe:column property="spunta" width="5%" title="igeriv.spunta"
							filterable="false" sortable="false" cell="selectedSpuntaOC"
							headerCell="buttonSpuntaAutomaticaOC" pkName="seqordine"
							hasHiddenPkField="false" style="text-align:center" />
					<ec:column property="barcode" width="15%"				title="dpe.dettaglio.libro.barcode" filterable="false" />
					<ec:column property="titolo" width="30%"				title="dpe.dettaglio.libro.titolo" filterable="false" />
					<ec:column property="autore" width="10%"				title="dpe.dettaglio.libro.autori" filterable="false" />
					<ec:column property="editore" width="10%"				title="dpe.dettaglio.libro.editore" filterable="false" />
					<dpe:column property="prezzoCopertinaCliente" cell="currency"		
								hasCurrencySign="true" width="15%" title="dpe.dettaglio.copertina.prezzo"
								filterable="false" sortable="false" style="text-align:right"
								headerStyle="text-align:right" format="###,##0.00" />
					<dpe:column property="prezzoCopertina" cell="currency"		
								hasCurrencySign="true" width="15%" title="dpe.dettaglio.libro.prezzo"
								filterable="false" sortable="false" style="text-align:right"
								headerStyle="text-align:right" format="###,##0.00" />			
				</dpe:row>
			</dpe:table>
			<s:hidden name="ordiniSelezionati" id="ordiniSelezionati" />
		</div>
		<s:hidden name="ordineFornitore"/>
	</s:form>
	<s:form id="ReportVenditaForm"
		action="report_reportVenditeLibriPdf.action" target="_blank"
		method="POST" theme="simple" validate="false">
	</s:form>
</s:if>
<s:elseif test="%{#request.itemsClient != null && !#request.itemsItemClienti.isEmpty()}">
	<s:form id="LibriScolasticiClientiDettForm" action="%{actionName}"	method="POST" theme="simple" validate="false"	onsubmit="return (ray.ajax())">
		
		<div style="width: 100%">
			<dpe:table 
				view="buttonsOnBottom"
				tableId="LibriScolasticiClientiTab"
				items="itemsClient" 
				var="itemsClient"
				action="${pageContext.request.contextPath}/${an}"
				imagePath="/app_img/table/*.gif" 
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.LibriScolasticiVenditaAction"
				styleClass="extremeTableFields"
				form="LibriScolasticiVenditaForm"
				theme="eXtremeTable bollaScrollDiv" 
				showPagination="false"
				rowsDisplayed="15" 
				id="OrdiniScrollDiv1" 
				toolbarClass="eXtremeTable"
				footerStyle="height:30px;" 
				filterable="false"
				showStatusBar="false"
				autoIncludeParameters="false" sortable="false"
				beforeUnloadValidationScript="">
				<dpe:row style="height:30px; cursor:pointer">
					<ec:column property="codCliente" width="15%"		title="Codice"  filterable="false" />
					<ec:column property="cognome" width="30%"			title="Cognome" filterable="false" />
					<ec:column property="nome" width="25%"				title="Nome"    filterable="false" />
				</dpe:row>
			</dpe:table>
		</div>
	</s:form>
	<s:form id="ReportVenditaForm"
		action="report_reportVenditeLibriPdf.action" target="_blank"
		method="POST" theme="simple" validate="false">
	</s:form>
</s:elseif>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:property value="nessunRisultato"/>
	</div>
</s:else>


</div>
