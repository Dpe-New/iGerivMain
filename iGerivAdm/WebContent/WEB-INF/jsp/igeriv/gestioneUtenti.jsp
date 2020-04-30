<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.utenti != null && !#request.utenti.isEmpty()}">
<s:form id="GestioneUtentiForm" action="gestioneUtenti_showUtenti.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">		
		<dpe:table
			tableId="GestioneUtentiTab"
			items="utenti"
			var="utenti" 
			action="${pageContext.request.contextPath}/gestioneUtenti_showUtenti.action"
			imagePath="/app_img/table/*.gif"
			rowsDisplayed="1000"			
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.UtentiAction"
			style="height:370px"
			styleClass="extremeTableFields" 
			form="GestioneUtentiForm"
			theme="eXtremeTable bollaScrollDivLarger"			
			showPagination="false"
			id="GestioneUtentiDiv"
			toolbarClass="eXtremeTable"
			footerStyle="height:30px;"
			filterable="false"
			>
			<dpe:exportPdf         
				fileName="utenti.pdf"         
				tooltip="plg.esporta.pdf" 
				headerTitle="igeriv.utenti.igeriv" 
				headerColor="black"         
				headerBackgroundColor="#b6c2da"    
				isLandscape="true"
			/>
			<ec:exportXls     
				fileName="utenti.xls"      
				tooltip="plg.esporta.excel"/>		
			<dpe:row highlightRow="true" interceptor="marker" href="#?codUtente=codUtente" style="cursor:pointer; height:30px">					
				<ec:column property="codUtente" width="10%" title="dpe.utente" filterable="false"/>  
				<ec:column property="nomeUtente" width="15%" title="dpe.nome" filterable="false"/>
				<ec:column property="email" width="15%" title="dpe.email" filterable="false"/> 
				<ec:column property="titolo" width="10%" title="igeriv.profilo.utente" filterable="false"/>
				<ec:column property="abilitatoDesc" width="15%" title="igeriv.abilitato" filterable="false"/>                    
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

