<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top:30px">
<s:set name="keys" value="%{risposteClientiSelectKeys}" />
<s:set name="values" value="%{risposteClientiSelectValues}" />
<s:if test="%{#request.richiesteRifornimento != null && !#request.richiesteRifornimento.isEmpty()}">
	<s:form id="EvasioneClienteEdicolaForm" action="gestioneClienti_showResultEvasionePrenotazioniClientiEdicola.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
	<div style="width:100%; text-align:center; color: #444444; font-weight: bold; font-size: 15px; vertical-align: middle;">	
		<s:text name='igeriv.evasione.conclusa'/>
	</div>	
	<div id="mainDiv" style="width:100%">	
		<dpe:table
			tableId="RichiesteRifornimentiClienteEdicola"
			items="richiesteRifornimento"
			var="richiesteRifornimento" 
			action="${pageContext.request.contextPath}/gestioneClienti_showResultEvasionePrenotazioniClientiEdicola.action"
			imagePath="/app_img/table/*.gif"
			extraToolButton='<img src="/app_img/print_48x48.png" id="print" alt="${requestScope.stampaRicevuta}" border="0" title="${requestScope.stampaRicevuta}" style="cursor:pointer" onclick="javascript: printRicevuta();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
			extraToolButtonStyle="width:80%; text-align:right;"
			style="height:${tableHeight}"
			rowsDisplayed="1000" 
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.GestioneClientiAction"
			styleClass="extremeTableFields"
			form="EvasioneClienteEdicolaForm"
			theme="eXtremeTable bollaScrollDivSmall"			
			showPagination="false"
			id="BollaScrollDiv"
			toolbarClass="eXtremeTable"
			footerStyle="height:30px;"
			filterable="false"
			>
			<dpe:exportPdf         
				fileName="evasione_ordini.pdf"         
				tooltip="plg.esporta.pdf" 
				headerTitle="<fo:block text-align='center'>${consegnaPrenotazioni}</fo:block><br><fo:block text-align-last='justify'>${codEdicola}<fo:leader leader-pattern='space'/>${cliente}</fo:block><fo:block text-align-last='justify'>${ragSocEdicola}<fo:leader leader-pattern='space'/>${ragSocCliente}</fo:block><fo:block text-align-last='justify'>${indirizzoEdicola}<fo:leader leader-pattern='space'/>${indirizzoCliente}</fo:block><fo:block text-align-last='justify'>${cittaEdicola}<fo:leader leader-pattern='space'/>${cittaCliente}</fo:block>" 
				headerColor="black"         
				headerBackgroundColor="#b6c2da"    
				isLandscape="false"
				regionBeforeExtentInches="1.5"
				marginTopInches="1.2"
			/>
			<ec:exportXls     
				fileName="evasione_ordini.xls"       
				tooltip="plg.esporta.excel"/>		
			<dpe:row style="height:30px" interceptor="marker" href="#?idtn=pk.idtn&codCliente=pk.codCliente">
				<ec:column property="nomeCognomeCliente" width="15%" title="username.cliente" filterable="false"/>
				<dpe:column property="dataOrdine" width="7%" title="igeriv.data.ordine" cell="date" format="dd/MM/yyyy" styleClass="extremeTableFields" style="text-align:center" headerStyle="text-align:center" filterable="false" sortable="false"/>			
				<dpe:column property="titolo" width="15%" title="igeriv.titolo" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>
				<dpe:column property="sottoTitolo" width="15%" title="igeriv.sottotitolo" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>
				<dpe:column property="numeroCopertina" width="7%" title="igeriv.numero" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false"/>
				<dpe:column property="quantitaConsegnare" maxlength="3" size="4" width="5%" title="igeriv.richiesta.rifornimenti.quantita.evasa" filterable="false" styleClass="extremeTableFieldsSmaller" sortable="false" style="text-align:center"/>
				<dpe:column property="prezzoCopertina" cell="currency" format="###.###.##0,00" style="text-align:right" headerStyle="text-align:right" width="8%" title="igeriv.prezzo.lordo" filterable="false" sortable="false"/>
				<dpe:column property="importoConsegnare" cell="currency" format="###.###.##0,00" style="text-align:right" headerStyle="text-align:right" width="8%" title="igeriv.importo" filterable="false" sortable="false"/>
				<ec:column property="fake" width="2%" title=" " filterable="false" sortable="false" viewsDenied="pdf,xls"/>
			</dpe:row>
		</dpe:table>
	</div>		
	<div style="width:100%; text-align:center">	
		<input type="button" value="<s:text name='igeriv.torna.evasione.clienti'/>" name="torna" id="torna" class="tableFields" style="width:180px; text-align:center" onclick="javascript: window.location = '${pageContext.request.contextPath}/gestioneClienti_showClientiEvasionePrenotazioni.action'"/>
		<s:if test="idCliente != null && idCliente != ''">
			&nbsp;&nbsp;<input type="button" value="<s:text name='igeriv.aggiungi.altre.vendite'/>" name="torna" id="torna" class="tableFields" style="width:180px; text-align:center" onclick="javascript: creaContoCliente()"/>
		</s:if>
	</div>
	<s:hidden id="qtaDaEvadere" name="qtaDaEvadere"/>
	<s:hidden id="idConto" name="idConto"/>
</s:form>
<form id="ContoClienteForm" action="${pageContext.request.contextPath}/vendite_showConto.action" method="POST" target="_top">	
	<s:hidden name="idCliente" id="idCliente"/>
	<s:hidden name="idConto" id="idConto1"/>
	<s:hidden name="contoCliente" id="contoCliente"/>
</form>
<form id="ReportVenditeForm" action="${pageContext.request.contextPath}/report_reportVenditePdf.action" method="POST">	
</form>
<div id="one"></div> 
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato"/>
	</div>
</s:else>
</div>