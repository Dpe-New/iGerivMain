<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<s:if test="%{#request.ritiri != null && !#request.ritiri.isEmpty()}">
	<s:form id="RitiriForm" action="ritiri_show.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div style="width:100%">					
			<dpe:table
				tableId="RitiriTab" 
				items="ritiri"
				var="ritiri" 
				action="ritiri_show.action"						
				imagePath="/app_img/table/*.gif"				
				style="table-layout:auto; height: 350px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.RitiriAction"
				styleClass="extremeTableFields"	
				form="RitiriForm"		
				theme="eXtremeTable bollaScrollDiv"			
				showPagination="false"
				id="RitiriScrollDiv"
				toolbarClass="eXtremeTable"
				showStatusBar="false"
				showTitle="false"
				showTooltips="false"
				footerStyle="height:30px;"
				filterable="false"
				showExports="true"
				>
				<dpe:exportPdf         
					fileName="ritiri_${codCliente}_${strDataDa}_${strDataA}.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="${reportTitle}" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
				/> 
				<ec:exportXls     
					fileName="ritiri_${codCliente}_${strDataDa}_${strDataA}.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row>	
					<dpe:column property="fake" width="3%" title=" " cell="ritiriClienteSpunta" pkName="pk" hasHiddenPkField="true" style="text-align:center" filterable="false" sortable="false" viewsDenied="pdf,xls"/>			
					<ec:column property="dataVendita" width="10%" title="igeriv.data.ritiro" cell="date" format="dd/MM/yyyy HH:mm:ss" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="tipoRitiro" width="10%" title="igeriv.tipo.ritiro" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="dataECFattura" width="10%" title="igeriv.data.estratto.conto.o.fattura" cell="date" dateFormat="dd/MM/yyyy" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="titolo" width="16%" title="igeriv.titolo" filterable="false"/>         
					<ec:column property="sottoTitolo" width="16%" title="igeriv.sottotitolo" filterable="false"/> 
					<ec:column property="numeroCopertina" width="10%" title="igeriv.numero" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="prezzoCopertina" width="10%" title="igeriv.prezzo.lordo" hasCurrencySign="true" format="###.###.##0,00" cell="currency" filterable="false" style="text-align:right"/>
					<dpe:column property="quantita" width="8%" title="igeriv.quantita" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="importoTotale" width="10%" title="igeriv.importo" calc="total" calcTitle="column.calc.total" totalCellStyle="text-align:right" hasCurrencySign="true" format="###.###.##0,00" totalFormat="###.###.##0,00" cell="currency" filterable="false" style="text-align:right"/>
					<dpe:column property="immagine" title="igeriv.img" filterable="false" viewsDenied="pdf,xls" width="3%" sortable="false" style="text-align:center" headerStyle="text-align:center">
						<a href="/${pageScope.ritiri.dirAlias}/${pageScope.ritiri.immagine}" rel="thumbnail"><img src="/app_img/camera.gif" width="15px" height="15px" border="0" style="border-style:none"/></a>
					</dpe:column>   	
				</dpe:row>
			</dpe:table>
		</div>
		<s:if test="authUser.tipoUtente == 1">
			<div style="text-align:center; margin-top:15px;">
				<input type="button" value="<s:text name='igeriv.cancella.ritiri.selezionati'/>" name="butDeleteRitiri" id="butDeleteRitiri" class="tableFields" style="width:200px; text-align:center; background: #ffffcc" onclick="javascript: return deleteRitiri();"/>
			</div>
		</s:if>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:property value="nessunRisultato"/>
	</div>
</s:else>
</div>