<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="an" value="%{actionName}"/>	
<s:if test="%{#request.clienti != null && !#request.clienti.isEmpty()}">
<s:form id="GestioneClientiForm" action="%{actionName}" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">		
		<dpe:table
			tableId="GestioneClientiTab"
			items="clienti"
			var="clienti" 
			action="${pageContext.request.contextPath}/${an}"
			imagePath="/app_img/table/*.gif"
			rowsDisplayed="1000"			
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.GestioneClientiAction"
			styleClass="extremeTableFields"
			form="GestioneClientiForm"
			theme="eXtremeTable bollaScrollDiv"			
			showPagination="false"
			id="BollaScrollDiv1"
			toolbarClass="eXtremeTable"
			footerStyle="height:30px;"
			filterable="false"
			>
			<dpe:exportPdf         
				fileName="clienti.pdf"         
				tooltip="plg.esporta.pdf" 
				headerTitle="gp.clienti.edicola" 
				headerColor="black"         
				headerBackgroundColor="#b6c2da"    
				isLandscape="true"
			/>
			<ec:exportXls     
				fileName="clienti.xls"      
				tooltip="plg.esporta.excel"/>		
			<dpe:row highlightRow="true" interceptor="marker" href="#?idCliente=codCliente" style="height:30px; cursor:pointer">
				<ec:column property="nome" width="15%" title="dpe.nome" filterable="false"/>         
				<ec:column property="cognome" width="15%" title="dpe.cognome" filterable="false"/> 
				<ec:column property="localitaDesc" width="15%" title="dpe.localita" filterable="false"/>                    
				<ec:column property="provinciaDesc" width="10%" title="dpe.provincia" filterable="false"/>              
				<ec:column property="cap" width="5%" title="dpe.cap" filterable="false"/>  
				<ec:column property="telefono" width="10%" title="dpe.telefono" filterable="false"/>                  
				<ec:column property="email" width="15%" title="dpe.email" filterable="false"/>              			         
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
