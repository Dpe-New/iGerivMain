<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div>
<s:if test="%{#request.agenzie != null && !#request.agenzie.isEmpty()}">
<s:set name="an" value="%{actionName}"/>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:actionerror/>
<s:form id="AgenzieForm" action="agenzia_showAgenzie.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">	
	<div style="width:100%">
			<div style="text-align:center;"><input type="button" value="<s:text name='igeriv.nuovoDl'/>" name="igeriv.nuovoDL" id="nuovoDL" style="width:100px; text-align:center" onclick="javascript: (showAgenzia());"/></div>  
	</div>
	<div style="width:100%">	
	<dpe:table
		tableId="AgenzieTab"
		items="agenzie"
		var="agenzie" 
		action="${pageContext.request.contextPath}${ap}/${an}"
		imagePath="/app_img/table/*.gif"
		rowsDisplayed="1000"			
		view="buttonsOnBottom"
		locale="${localeString}"
		state="it.dpe.igeriv.web.actions.AnagraficaAgenziaAction"
		styleClass="extremeTableFields" 
		form="AgenzieForm"
		theme="eXtremeTable bollaScrollDivTall"			
		showPagination="false"
		id="AgenzieDiv"
		toolbarClass="eXtremeTable"
		footerStyle="height:30px;"
		filterable="false"
		>
		<dpe:exportPdf         
			fileName="agenzie.pdf"         
			tooltip="plg.esporta.pdf" 
			headerTitle="igeriv.agenzie" 
			headerColor="black"         
			headerBackgroundColor="#b6c2da"    
			isLandscape="true"
		/>
		<ec:exportXls     
			fileName="agenzie.xls"      
			tooltip="plg.esporta.excel"/>		
		<dpe:row highlightRow="true" interceptor="marker" style="height:30px" href="#?codFiegDl=codFiegDl">					
			<ec:column property="codFiegDl" width="8%" title="dpe.login.cod.fieg.dl.code.short" filterable="false"/>  
			<ec:column property="ragioneSocialeDlPrimaRiga" width="20%" title="dpe.rag.sociale" filterable="false"/> 
			<ec:column property="indirizzoDlPrimaRiga" width="15%" title="dpe.indirizzo" filterable="false"/>                    
			<ec:column property="localitaDlPrimaRiga" width="10%" title="dpe.localita" filterable="false"/>
			<ec:column property="siglaProvincia" width="5%" title="dpe.provincia" filterable="false"/>
			<ec:column property="codDpeWebDl" width="8%" title="igeriv.statistiche.utilizzo.codice.dpe.web.dl" filterable="false"/>  
			<ec:column property="dlInforiv" cell="boolean" width="8%" title="igeriv.agenzia.inforiv" filterable="false"/>
			<dpe:column property="dataUltimaModfica" cell="date" dateFormat="dd/MM/yyyy HH:mm:ss" width="10%" title="igeriv.data.ultima.modifica" filterable="false"/>
		</dpe:row>
	</dpe:table>
	</div>		
</div>		
</s:form>
</s:if>
</div>


