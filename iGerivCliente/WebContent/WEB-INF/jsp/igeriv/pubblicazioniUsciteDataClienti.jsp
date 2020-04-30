<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<s:if test="%{#request.listPubblicazioni != null && !#request.listPubblicazioni.isEmpty()}">
	<s:form id="PubblicazioniDetailForm" action="%{actionName}" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<div style="width:100%">					
			<dpe:table
				tableId="PubblicazioniTab" 
				items="listPubblicazioni"
				var="listPubblicazioni" 
				action=""						
				imagePath="/app_img/table/*.gif"				
				style="table-layout:auto; height: 200px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.PubblicazioniAction"
				styleClass="extremeTableFields"	
				form="PubblicazioniDetailForm"		
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
					fileName="pubblicazioni.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="igeriv.pubblicazioni" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
				/> 
				<ec:exportXls     
					fileName="pubblicazioni.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row highlightRow="true" interceptor="marker" style="height:30px; cursor:pointer" href="${href}">				
					<ec:column property="argomento" width="20%" title="igeriv.argomento" filterable="false"/>
					<ec:column property="titolo" width="25%" title="igeriv.titolo" filterable="false"/>         
					<ec:column property="sottoTitolo" width="25%" title="igeriv.sottotitolo" filterable="false"/> 
					<ec:column property="numeroCopertina" width="6%" title="igeriv.numero" filterable="false" style="text-align:center"/>
					<ec:column property="prezzoCopertina" width="7%" title="igeriv.prezzo" cell="currency" filterable="false" style="text-align:right"/>
					<ec:column property="periodicita" width="14%" title="igeriv.periodicita" filterable="false"/>
					<ec:column property="immagine" title="igeriv.img" filterable="false" viewsDenied="pdf,xls" width="3%" sortable="false">
						<a href="/immagini/${pageScope.listPubblicazioni.immagine}" rel="thumbnail"><img src="/app_img/camera.gif" width="15px" height="15px" border="0" style="border-style:none"/></a>
					</ec:column>   	
				</dpe:row>
			</dpe:table>
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato" />
	</div>
</s:else>
</div>