<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<div id="contentDiv">
<s:if test="%{#request.listEcFattura != null && !#request.listEcFattura.isEmpty()}">
	<s:form id="ECFattureClientiForm" action="reportEcFatture_show.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div style="width:100%">					
			<dpe:table
				tableId="ECFattureClientiTab" 
				items="listEcFattura"
				var="listEcFattura" 
				action="${pageContext.request.contextPath}${ap}/reportEcFatture_show.action"						
				imagePath="/app_img/table/*.gif"				
				style="table-layout:auto; height: 350px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ReportEstrattiContoFattureClientiAction"
				styleClass="extremeTableFields"	
				form="ECFattureClientiForm"		
				theme="eXtremeTable bollaScrollSmallerDiv"			
				showPagination="false"
				id="FattureEmesseScrollDiv"
				toolbarClass="eXtremeTable"
				showStatusBar="false"
				showTitle="false"
				showTooltips="false"
				footerStyle="height:30px;"
				filterable="false"
				showExports="true"
				>
				<dpe:exportPdf         
					fileName="estratti_conto_fatture.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="${reportTitle}" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"      
				/> 
				<ec:exportXls     
					fileName="estratti_conto_fatture.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row style="height:25px" interceptor="marker" href="tipo=tipo&codCliente=codCliente&fileName=fileName">
					<ec:column property="dataDocumento" width="15%" title="igeriv.data.documento" cell="date" format="dd/MM/yyyy" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="tipoDocumentoDesc" width="15%" title="igeriv.tipo.documento" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="dataCompetenza" width="15%" title="igeriv.data.competenza" cell="date" format="dd/MM/yyyy" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="numeroDocumento" width="15%" title="igeriv.numero.documento" filterable="false" style="text-align:center" headerStyle="text-align:center"/>				
					<dpe:column property="importoTotale" calc="total" calcTitle="column.calc.total" totalCellStyle="text-align:right" cell="currency" hasCurrencySign="true" format="###.###.##0,00" width="15%" title="igeriv.importo" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
					<ec:column property="fileTxt" title="igeriv.documento" filterable="false" viewsDenied="pdf,xls" sortable="false" width="24%" style="text-align:center" headerStyle="text-align:center">
						<img src="/app_img/pdf_download_20.png" id="imgPdf" name="imgPdf" border="0px" style="border-style: none; cursor:pointer" title="<s:text name="igeriv.download.fattura"/>" alt="<s:text name="igeriv.download.fattura"/>"/>
					</ec:column> 	
				</dpe:row>
			</dpe:table>	 	
		</div>
	</s:form>
	<s:form name="EstrattoContoClientiForm" id="EstrattoContoClientiForm" action="report_emettiEstrattoContoSingoloClienteEdicola.action" target="_blank">
		<s:hidden name="dataEstrattoConto" id="dataEstrattoConto"/>
		<s:hidden name="codCliente" id="codCliente"/>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato"/>
	</div>
</s:else>
</div>