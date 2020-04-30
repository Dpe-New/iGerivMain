<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.listContoDeposito != null && !#request.listContoDeposito.isEmpty()}">
	<s:form id="ContoDepositoForm" action="infoPubblicazioni_reportContoDeposito.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div style="width:100%">					
			<dpe:table
				tableId="ContoDepositoTab" 
				items="listContoDeposito"
				var="listContoDeposito" 
				action="${pageContext.request.contextPath}/infoPubblicazioni_reportContoDeposito.action"						
				imagePath="/app_img/table/*.gif"				
				style="table-layout:fixed; height: 350px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.PubblicazioniAction"
				styleClass="extremeTableFields"	
				form="ContoDepositoForm"		
				theme="eXtremeTable bollaScrollDiv"			
				showPagination="false"
				id="PubblicazioniScrollDiv"
				toolbarClass="eXtremeTable"
				showStatusBar="false"
				showTitle="false"
				showTooltips="false"
				footerStyle="height:30px;"
				filterable="false"
				showExports="true"
				>
				<dpe:exportPdf         
					fileName="conto_deposito.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="igeriv.pubblicazioni.conto.deposito" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
				/> 
				<ec:exportXls     
					fileName="conto_deposito.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row highlightRow="true" style="height:30px">	
					<dpe:column property="dataUscita" width="7%" title="igeriv.data.uscita" cell="date" dateFormat="dd/MM/yyyy" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="dataFatturazionePrevista" width="7%" title="igeriv.data.fatturazione.prevista" cell="date" dateFormat="dd/MM/yyyy" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="codicePubblicazione" width="4%" title="igeriv.cpu" filterable="false" style="text-align:center" headerStyle="text-align:center"/>			
					<ec:column property="numeroCopertina" width="5%" title="igeriv.numero" filterable="false" escapeAutoFormat="true" style="text-align:center" headerStyle="text-align:center"/>     
					<ec:column property="titolo" width="27%" title="igeriv.titolo" filterable="false"/>
					<dpe:column property="quantitaContoDeposito" calc="total" calcTitle="column.calc.total" totalCellStyle="text-align:center" width="7%" title="igeriv.fornito.conto.deposito" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="quantitaReso" calc="total" totalCellStyle="text-align:center" width="7%" title="igeriv.reso" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="venduto" calc="total" totalCellStyle="text-align:center" width="7%" title="igeriv.venduto.conto.deposito" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="prezzoCopertina" width="8%" title="igeriv.prezzo.copertina" filterable="false" cell="currency" format="###.###.##0,0000" style="text-align:right" headerStyle="text-align:right" />
					<dpe:column property="prezzoEdicola" width="8%" title="igeriv.prezzo.netto" filterable="false" cell="currency" format="###.###.##0,0000" style="text-align:right" headerStyle="text-align:right" />              
					<dpe:column property="importo" calc="total" totalCellStyle="text-align:right" totalFormat="###.###.##0,00" width="7%" title="igeriv.importo" filterable="false" cell="currency" format="###.###.##0,0000" style="text-align:right" headerStyle="text-align:right" />
					<ec:column property="immagine" title="igeriv.img" filterable="false" viewsDenied="pdf,xls" sortable="false" width="5%" style="text-align:center">
						<a href="/immagini/${pageScope.listContoDeposito.immagine}" rel="thumbnail"><img src="/app_img/camera.gif" width="15px" height="15px" border="0" style="border-style:none"/></a>
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
