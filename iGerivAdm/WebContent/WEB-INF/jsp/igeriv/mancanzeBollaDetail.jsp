<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top:30px">
	<dpe:table
		tableId="MancanzeBollaDettaglioTab" 
		items="mancanzeDettaglio"
		var="mancanzeDettaglio" 
		action="${pageContext.request.contextPath}/mancanzeBolla_showMancanzeDettaglioBolla.action"						
		imagePath="/app_img/table/*.gif"	
		title="${filterTitle}"			
		style="height:60px; width:350px"		
		view="buttonsOnBottom"
		locale="${localeString}"
		state="it.dpe.igeriv.web.actions.MancanzeAction"
		styleClass="extremeTableFields"	
		theme="eXtremeTable bollaScrollDivSmall"			
		showPagination="false"
		id="MancanzeBollaDettaglioScrollDiv"
		toolbarClass="eXtremeTable" 
		showStatusBar="false"
		showTitle="true"
		showTooltips="false"
		footerStyle="height:30px; width:100%"
		filterable="false"
		>
		<dpe:exportPdf         
			fileName="dettaglio_quadratura_mancanze.pdf"         
			tooltip="plg.esporta.pdf" 
			headerTitle="${filterTitle}" 
			headerColor="black"         
			headerBackgroundColor="#b6c2da"    
		/> 
		<ec:exportXls     
			fileName="dettaglio_quadratura_mancanze.xls"      
			tooltip="plg.esporta.excel"/>		
		<ec:row style="height:25px">
			<dpe:column property="dtBolla" width="25%" title="igeriv.data.bolla" cell="date" dateFormat="dd/MM/yyyy" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/>				
			<ec:column property="tipoBolla" width="25%" title="igeriv.tipo.bolla" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/>         
			<dpe:column property="mancanze" width="25%" title="igeriv.mancanze.accreditate" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/> 
		</ec:row>
	</dpe:table>
</div>