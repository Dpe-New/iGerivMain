<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.edicole != null && !#request.edicole.isEmpty()}">
<s:set name="an" value="%{actionName}"/>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:actionerror/>
<s:form id="EdicoleForm" action="edicole_showEdicole.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">	
	<s:if test="%{#session['codFiegDl'] eq #application['CDL_CODE']}">
		<dpe:table
			tableId="EdicoleTab"
			items="edicole"
			var="edicole" 
			action="${pageContext.request.contextPath}${ap}/${an}"
			imagePath="/app_img/table/*.gif"
			rowsDisplayed="1000"			
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.EdicoleAction"
			styleClass="extremeTableFields" 
			form="EdicoleForm"
			theme="eXtremeTable bollaScrollDivTall"			
			showPagination="false"
			id="EdicoleDiv"
			toolbarClass="eXtremeTable"
			footerStyle="height:30px;"
			filterable="false"
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
			<dpe:row style="height:30px" highlightRow="true" interceptor="marker" href="#?codEdicolaWeb=codEdicolaWeb">					
				<ec:column property="codEdicolaDl" width="5%" title="dpe.login.dl.fieg.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>  
				<ec:column property="password" width="15%" title="Password" filterable="false"/> 
				<ec:column property="codEdicolaWeb" width="5%" title="dpe.login.dl.web.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
				<ec:column property="ragioneSociale" width="20%" title="dpe.rag.sociale" filterable="false"/> 
				<ec:column property="indirizzo" width="15%" title="dpe.indirizzo" filterable="false"/>                    
				<ec:column property="localita" width="15%" title="dpe.localita" filterable="false"/>
				<ec:column property="provincia" width="5%" title="dpe.provincia" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
				<dpe:column property="dataInserimento" cell="date" dateFormat="dd/MM/yyyy" width="8%" title="igeriv.richiesta.rifornimenti.data.inserimento" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
				<dpe:column property="dataSospensione" maxlength="10" size="10" width="8%" dateFormat="dd/MM/yyyy" title="igeriv.data.sospensione" sessionVarName="dateSospensione" hasHiddenPkField="true" filterable="false" cell="textDifferenza" pkName="codEdicolaDl" styleClass="extremeTableFields" sortable="true" exportStyle="text-align:center" style="text-align:center" headerStyle="text-align:center"/>
			</dpe:row>
		</dpe:table>
	</s:if>
	<s:else>
		<dpe:table
			tableId="EdicoleTab"
			items="edicole"
			var="edicole" 
			action="${pageContext.request.contextPath}${ap}/${an}"
			imagePath="/app_img/table/*.gif"
			rowsDisplayed="1000"			
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.EdicoleAction"
			styleClass="extremeTableFields" 
			form="EdicoleForm"
			theme="eXtremeTable bollaScrollDivTall"			
			showPagination="false"
			id="EdicoleDiv"
			toolbarClass="eXtremeTable"
			footerStyle="height:30px;"
			filterable="false"
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
			<dpe:row highlightRow="true" interceptor="marker" style="height:30px" href="#?codEdicolaWeb=codEdicolaWeb">					
				<ec:column property="codEdicolaWeb" width="8%" title="dpe.login.dl.web.code.short" filterable="false"/>  
				<ec:column property="codEdicolaDl" width="8%" title="dpe.login.dl.fieg.code.short" filterable="false"/>
				<ec:column property="descProfilo" width="12%" title="igeriv.profilo.utente" filterable="false"/>
				<ec:column property="ragioneSociale" width="20%" title="dpe.rag.sociale" filterable="false"/> 
				<ec:column property="indirizzo" width="15%" title="dpe.indirizzo" filterable="false"/>                    
				<ec:column property="localita" width="10%" title="dpe.localita" filterable="false"/>
				<ec:column property="provincia" width="5%" title="dpe.provincia" filterable="false"/>
				<dpe:column property="dataInserimento" cell="date" dateFormat="dd/MM/yyyy" width="10%" title="igeriv.richiesta.rifornimenti.data.inserimento" filterable="false"/>
				<dpe:column property="dataSospensione" cell="date" dateFormat="dd/MM/yyyy" width="10%" title="igeriv.data.sospensione" filterable="false"/>
			</dpe:row>
		</dpe:table>
	</s:else>	
	<div style="width:100%;">						
		<div style="text-align:center;"><input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" style="width:100px; text-align:center" onclick="javascript: (setFieldsToSave() && setFormAction('EdicoleForm','${pageContext.request.contextPath}${ap}/edicole_saveEdicole.action', '', 'messageDiv'));"/></div>
	</div>			
</div>		
</s:form>
</s:if>


