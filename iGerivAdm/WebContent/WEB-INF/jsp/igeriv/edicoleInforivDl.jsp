<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.edicole != null && !#request.edicole.isEmpty()}">
<s:actionerror/>
<s:form id="EdicoleForm" action="edicoleInforivDl_showEdicoleInforivDl.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">	
	<dpe:table
		tableId="EdicoleTab"
		items="edicole"
		var="edicole" 
		action="edicoleInforivDl_showEdicoleInforivDl.action"
		imagePath="/app_img/table/*.gif"
		rowsDisplayed="1000"			
		view="buttonsOnBottom"
		locale="${localeString}"
		state="it.dpe.igeriv.web.actions.EdicolaInforivDlAction"
		styleClass="extremeTableFields" 
		form="EdicoleForm"
		theme="eXtremeTable bollaScrollDivTall"			
		showPagination="false"
		id="EdicoleDiv"
		toolbarClass="eXtremeTable"
		footerStyle="height:30px;"
		filterable="false"
		autoIncludeParameters="false"
		>
		<dpe:exportPdf         
			fileName="edicole.pdf"         
			tooltip="plg.esporta.pdf" 
			headerTitle="igeriv.edicole" 
			headerColor="black"         
			headerBackgroundColor="#b6c2da"    
			isLandscape="true"
		/>
		<ec:exportXls     
			fileName="edicole.xls"      
			tooltip="plg.esporta.excel"/>		
		<dpe:row style="height:30px" interceptor="marker" highlightRow="true" href="#?codEdicolaWeb=codEdicolaWeb">					
			<ec:column property="codUtente" width="8%" title="dpe.utente" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<ec:column property="codEdicolaDl" width="8%" title="dpe.login.dl.fieg.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>  
			<ec:column property="codEdicolaWeb" width="8%" title="dpe.login.dl.web.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<ec:column property="ragioneSociale" width="20%" title="dpe.rag.sociale" filterable="false"/> 
			<ec:column property="indirizzo" width="18%" title="dpe.indirizzo" filterable="false"/>                    
			<ec:column property="localita" width="18%" title="dpe.localita" filterable="false"/>
			<ec:column property="provincia" width="5%" title="dpe.provincia" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<dpe:column property="dataInserimento" width="8%" title="igeriv.richiesta.rifornimenti.data.inserimento" dateFormat="dd/MM/yyyy" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" style="font-size:10px; text-align:left" exportStyle="text-align:center"/>
			<dpe:column property="dataSospensione" width="8%" title="igeriv.data.sospensione" dateFormat="dd/MM/yyyy" sessionVarName="dataSospensione" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" style="font-size:10px; text-align:left" exportStyle="text-align:center"/>
		</dpe:row>
	</dpe:table>
</div>	
</s:form>
</s:if>


