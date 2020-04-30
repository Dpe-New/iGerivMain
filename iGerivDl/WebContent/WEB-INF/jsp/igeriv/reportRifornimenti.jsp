<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
	<s:if test="%{#request.richiesteRifornimento != null && !#request.richiesteRifornimento.isEmpty()}">
		<s:set name="an" value="%{actionName}"/>
		<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
		<s:if test='#context["struts.actionMapping"].namespace == "/"'>
			<s:set name="ap" value="" />
		</s:if>	
		<s:form id="ReportRifornimentiForm" action="%{actionName}" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
			<div style="width:100%">
				<dpe:table
					tableId="ReportRifornimentiTab"
					items="richiesteRifornimento"
					var="richiesteRifornimento" 
					action="${pageContext.request.contextPath}${ap}/${an}"
					imagePath="/app_img/table/*.gif"		
					rowsDisplayed="1000"			
					view="buttonsOnBottom"
					locale="${localeString}"
					state="it.dpe.igeriv.web.actions.ReportRifornimentiAction"
					styleClass="extremeTableFieldsLarger" 
					form="ReportRifornimentiForm"
					theme="eXtremeTable bollaScrollDivLarge"			
					showPagination="false"
					id="ReportRifornimentiDiv"
					toolbarClass="eXtremeTable"
					footerStyle="height:30px;"
					filterable="false"
					>
					<dpe:exportPdf
						fileName="report_rifornimenti.pdf"         
						tooltip="plg.esporta.pdf" 
						headerTitle="igeriv.report.richieste.rifornimento"
						headerColor="black"         
						headerBackgroundColor="#b6c2da"    
						isLandscape="true"
					/>
					<ec:exportXls
						fileName="report_rifornimenti.xls"      
						tooltip="plg.esporta.excel"/>
					<ec:row style="height:20px">
						<ec:column property="codicePubblicazione" width="5%" title="igeriv.codice.pubblicazione.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
						<ec:column property="titolo" width="14%" title="igeriv.titolo" filterable="false" style="text-align:left" headerStyle="text-align:left"/>
						<ec:column property="numeroCopertina" width="7%" title="igeriv.cod.numero.copertina" filterable="false" style="text-align:left" headerStyle="text-align:left"/>
						<ec:column property="codRivendita" width="5%" title="dpe.login.dl.fieg.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
						<ec:column property="ragioneSocialeRivendita" width="14%" title="dpe.rag.sociale" filterable="false" style="text-align:left" headerStyle="text-align:left"/>
						<dpe:column property="dataOrdine" cell="date" dateFormat="dd/MM/yyyy" width="8%" title="igeriv.data.richiesta" filterable="false" style="text-align:center" headerStyle="text-align:center"/>  
						<dpe:column property="dataScadenza" cell="date" dateFormat="dd/MM/yyyy" width="8%" title="dpe.data.scadenza" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
						<dpe:column property="quantitaRichiesta" width="5%" title="igeriv.richiesta.rifornimenti.quantita.richiesta" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
						<ec:column property="statoDesc" width="9%" title="igeriv.richiesta.rifornimenti.stato.evasione" filterable="false" style="text-align:left" headerStyle="text-align:left"/>
						<dpe:column property="quantitaEvasa" width="5%" title="igeriv.richiesta.rifornimenti.quantita.evasa" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
						<dpe:column property="dataRispostaDl" cell="date" dateFormat="dd/MM/yyyy" width="8%" title="igeriv.richiesta.rifornimenti.data.risposta.dl" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
						<ec:column property="descCausaleNonEvadibilita" width="10%" title="igeriv.richiesta.rifornimenti.risposta.dl" filterable="false" style="text-align:left" headerStyle="text-align:left"/>
					</ec:row>
				</dpe:table>
			</div>
		</s:form>
	</s:if>
	<s:else>
		<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
			<s:property value="nessunRisultato"/>
		</div>
	</s:else>
</div>


