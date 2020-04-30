<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<s:if test="%{#request.listProdotti != null && !#request.listProdotti.isEmpty()}">
	<s:form id="ProdottiDetailForm" action="prodotti_showProdotti.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div style="width:100%">					
			<dpe:table
				tableId="ProdottiTab" 
				items="listProdotti"
				var="listProdotti" 
				action="${pageContext.request.contextPath}/prodotti_showProdotti.action"						
				imagePath="/app_img/table/*.gif"				
				style="table-layout:auto; height: 350px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.ProdottiAction"
				styleClass="extremeTableFields"	
				form="ProdottiDetailForm"		
				theme="eXtremeTable bollaScrollDiv"			
				showPagination="false"
				id="ProdottiScrollDiv"
				toolbarClass="eXtremeTable"
				showStatusBar="false"
				showTitle="false"
				showTooltips="false"
				footerStyle="height:30px;"
				filterable="false"
				showExports="true"
				>
				<dpe:exportPdf         
					fileName="Prodotti.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="igeriv.prodotti.non.editoriali" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
				/> 
				<ec:exportXls     
					fileName="prodotti.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row interceptor="marker" style="height:25px;" href="#?pk=codProdottoInterno">
					<dpe:column property="codiceProdottoFornitore" width="4%" title="igeriv.codice.prod.forn" filterable="false" style="text-align:center" headerStyle="text-align:center" exportStyle="text-align:center"/>
					<dpe:column property="descrizione" width="26%" title="igeriv.prodotto" filterable="false" style="text-align:left" headerStyle="text-align:left"/>         
					<dpe:column property="categoria" width="19%" title="igeriv.categoria" filterable="false" style="text-align:center" headerStyle="text-align:center" exportStyle="text-align:center"/>
					<dpe:column property="sottocategoria" width="17%" title="igeriv.sotto.categoria" filterable="false" style="text-align:center" headerStyle="text-align:center" exportStyle="text-align:center"/>
					<dpe:column property="ultimoPrezzoAcquisto" width="8%" title="igeriv.imponibile" cell="currency" hasCurrencySign="true" filterable="false" style="text-align:right" headerStyle="text-align:right" exportStyle="text-align:right"/>
					<dpe:column property="aliquota" width="8%" title="igeriv.aliquota.iva" filterable="false" style="text-align:right" headerStyle="text-align:right" exportStyle="text-align:right"/>
					<dpe:column property="prezzoConAliquota" width="8%" title="igeriv.prezzo" cell="currency" hasCurrencySign="true" filterable="false" style="text-align:right" headerStyle="text-align:right" exportStyle="text-align:right"/>
					<dpe:column property="giacenza" width="6%" title="igeriv.disponibilita" filterable="false" sortable="false" viewsDenied="pdf,xls" style="text-align:center" exportStyle="text-align:center"/>
					<ec:column property="immagine" title="igeriv.img" filterable="false" viewsDenied="pdf,xls" sortable="false" width="3%" style="text-align:center;" headerStyle="text-align:center">
						<a href="/immagini_prodotti_vari_dl/${pageScope.listProdotti.immagine}" rel="thumbnail"><img src="/app_img/camera.gif" width="15px" height="15px" border="0" style="border-style:none; text-align:center"/></a>
					</ec:column>   	              
				</dpe:row>
			</dpe:table>	 	
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato"/>
	</div>
</s:else>
</div>