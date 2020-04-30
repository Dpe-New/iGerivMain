<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<s:if test="%{#request.fatture != null && !#request.fatture.isEmpty()}">
	<s:form id="FattureEmesseForm" action="fatture_showFatture.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div style="width:100%">					
			<dpe:table
				tableId="FattureEmesseTab" 
				items="fatture"
				var="fatture" 
				action="${pageContext.request.contextPath}/fatture_showFatture.action"						
				imagePath="/app_img/table/*.gif"				
				style="table-layout:auto; height: 350px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ReportFattureAction"
				styleClass="extremeTableFields"	
				form="FattureEmesseForm"		
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
					fileName="fatture_emesse.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="igeriv.report.fatture.emesse" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"      
				/> 
				<ec:exportXls     
					fileName="fatture_emesse.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row style="height:25px" interceptor="marker" href="fileName=fileName">
					<ec:column property="nomeCognome" width="15%" title="username.cliente" filterable="false" style="text-align:left" headerStyle="text-align:left"/>
					<ec:column property="data" width="15%" title="igeriv.data" cell="date" format="dd/MM/yyyy HH:mm:ss" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="numero" width="15%" title="igeriv.numero.documento" filterable="false" style="text-align:center" headerStyle="text-align:center"/>				
					<ec:column property="tipoDocumentoDesc" width="15%" title="igeriv.tipo.documento" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="fake" width="15%" cell="dpeImgStornoFatturaCell" title="igeriv.storno.fattura" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="fileTxt" title="igeriv.documento" filterable="false" viewsDenied="pdf,xls" sortable="false" width="15%" style="text-align:center" headerStyle="text-align:center">
						<img src="/app_img/pdf_download_20.png" id="imgPdf" name="imgPdf" border="0px" style="border-style: none; cursor:pointer" title="<s:text name="igeriv.download.fattura"/>" alt="<s:text name="igeriv.download.fattura"/>"/>
					</ec:column> 	
				</dpe:row>
			</dpe:table>	 	
		</div>
		<s:hidden name="codCliente" id="codCliente"/>
		<s:hidden name="numFattura" id="numFattura"/>
		<s:hidden name="tipoDocumento" id="tipoDocumento"/>
		<s:hidden name="tipoProdottiInEstrattoConto" id="tipoProdottiInEstrattoConto"/>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato"/>
	</div>
</s:else>
</div>