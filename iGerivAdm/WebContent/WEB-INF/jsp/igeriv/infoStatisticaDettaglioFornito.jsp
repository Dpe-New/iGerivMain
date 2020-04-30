<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv" style="margin-top:30px">
	<s:if test="tableTitle != null">
		<s:set var="title" value="%{tableTitle}" />
	</s:if>
	<div id="mainDiv" style="width:100%">
	<s:form id="StatisticaDettaglioForm" action="statisticaPubblicazioni_showStatisticaDettaglioFornito.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
		<s:if test="%{#request.listStatisticaDettaglio != null && !#request.listStatisticaDettaglio.isEmpty() && #request.listStatisticaDettaglio[0].tipo eq 0}">	
			<dpe:table
				tableId="StatisticaDettaglioTab" 
				items="listStatisticaDettaglio"
				var="listStatisticaDettaglio" 
				action="${pageContext.request.contextPath}/statisticaPubblicazioni_showStatisticaDettaglioFornito.action"						
				imagePath="/app_img/table/*.gif"	
				title="${title}"			
				style="height:60px; width:450px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.PubblicazioniAction"
				styleClass="extremeTableFields"	
				form="StatisticaDettaglioForm"		
				theme="eXtremeTable bollaScrollSmallerDiv"			
				showPagination="false"
				id="PubblicazioniScrollDiv"
				toolbarClass="eXtremeTable" 
				showStatusBar="false"
				showTitle="true"
				showTooltips="false"
				footerStyle="height:30px; width:100%"
				filterable="false"
				>
				<dpe:exportPdf         
					fileName="dettaglio_fornito_statistica.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="${title}" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
				/> 
				<ec:exportXls     
					fileName="dettaglio_fornito_statistica.xls"      
					tooltip="plg.esporta.excel"/>		
				<ec:row style="height:25px">
					<dpe:column property="data" width="19%" title="igeriv.data.bolla" cell="date" dateFormat="dd/MM/yyyy" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/>				
					<ec:column property="tipoBolla" width="19%" title="igeriv.tipo.bolla" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/>         
					<ec:column property="tipoFondoBolla" width="19%" title="igeriv.tipo.fondo.bolla" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="copie" width="19%" title="igeriv.copie" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/> 
				</ec:row>
			</dpe:table>
		</s:if>
		<s:else>
			<dpe:table
			tableId="StatisticaDettaglioTab" 
			items="listStatisticaDettaglio"
			var="listStatisticaDettaglio" 
			action="${pageContext.request.contextPath}/statisticaPubblicazioni_showStatisticaDettaglioFornito.action"						
			imagePath="/app_img/table/*.gif"	
			title="${title}"			
			style="height:60px; width:450px"		
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.PubblicazioniAction"
			styleClass="extremeTableFields"	
			form="StatisticaDettaglioForm"		
			theme="eXtremeTable bollaScrollSmallerDiv"			
			showPagination="false"
			id="PubblicazioniScrollDiv"
			toolbarClass="eXtremeTable" 
			showStatusBar="false"
			showTitle="true"
			showTooltips="false"
			footerStyle="height:30px; width:100%"
			filterable="false"
			>
			<dpe:exportPdf         
				fileName="dettaglio_fornito_statistica.pdf"         
				tooltip="plg.esporta.pdf" 
				headerTitle="${title}" 
				headerColor="black"         
				headerBackgroundColor="#b6c2da"    
			/> 
			<ec:exportXls     
				fileName="dettaglio_fornito_statistica.xls"      
				tooltip="plg.esporta.excel"/>		
			<ec:row style="height:25px">
				<dpe:column property="data" width="40%" title="igeriv.data.richiesta" cell="date" dateFormat="dd/MM/yyyy" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/>				
				<dpe:column property="copie" width="40%" title="igeriv.copie" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/> 
			</ec:row>
		</dpe:table>
		</s:else>	 	
	</s:form>
	</div>
</div>