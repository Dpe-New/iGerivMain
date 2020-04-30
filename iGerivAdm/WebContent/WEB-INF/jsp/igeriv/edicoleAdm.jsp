<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.edicole != null && !#request.edicole.isEmpty()}">
<s:set name="an" value="%{actionName}"/>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:set name="keys" value="%{selectKeys}" />
<s:set name="values" value="%{selectValues}" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:actionerror/>
<s:form id="EdicoleForm" action="edicoleAdm_showEdicole.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">	
	<dpe:table
		tableId="EdicoleTab"
		items="edicole"
		var="edicole" 
		action="${pageContext.request.contextPath}${ap}/edicoleAdm_showEdicole.action"
		imagePath="/app_img/table/*.gif"
		rowsDisplayed="1000"			
		view="buttonsOnBottom"
		locale="${localeString}"
		state="it.dpe.igeriv.web.actions.EdicoleAdmAction"
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
		<dpe:row style="height:30px">					
			<ec:column property="codEdicolaDl" width="3%" title="dpe.login.dl.fieg.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>  
			<ec:column property="codEdicolaWeb" width="3%" title="dpe.login.dl.web.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<ec:column property="ragioneSociale" width="10%" title="dpe.rag.sociale" filterable="false"/> 
			<ec:column property="indirizzo" width="12%" title="dpe.indirizzo" filterable="false"/>                    
			<ec:column property="localita" width="7%" title="dpe.localita" filterable="false"/>
			<ec:column property="provincia" width="3%" title="dpe.provincia" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<dpe:column property="dataInserimento" width="7%" maxlength="10" size="10" title="igeriv.richiesta.rifornimenti.data.inserimento" cell="textDifferenza" dateFormat="dd/MM/yyyy" sessionVarName="dataInserimento" pkName="pk" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" style="font-size:10px; text-align:left" exportStyle="text-align:center"/>
			<dpe:column property="dataSospensione" width="7%" maxlength="10" size="10" title="igeriv.data.sospensione" cell="textDifferenza" dateFormat="dd/MM/yyyy" sessionVarName="dataSospensione" pkName="pk" hasHiddenPkField="true" styleClass="extremeTableFieldsLarger" filterable="false" sortable="false" style="font-size:10px; text-align:left" exportStyle="text-align:center"/>
			<dpe:column property="edicolaTest" width="3%" title="igeriv.edicola.test" filterable="false" sortable="false" cell="edicolaTestCheck" pkName="pk" hasHiddenPkField="true" style="text-align:center"/>
			<dpe:column property="edicolaPromo" width="15%" title="igeriv.edicola.promo" filterable="false" sortable="false" cell="edicolaPromoCheck" pkName="pk" hasHiddenPkField="true" style="text-align:center"/>
			<dpe:column property="fake" width="14%" title="igeriv.edicola.plus" filterable="false" sortable="false" cell="edicolaPlusCheck" pkName="pk" hasHiddenPkField="true" style="text-align:center"/>
			<dpe:column property="profilo" cell="dpeCombo" width="15%" title="igeriv.profilo.utente" filterable="false" sessionVarName="profilo" optionKeys="${keys}" optionValues="${values}" pkName="pk" fieldName="profilo" hasHiddenPkField="true" style="font-size:12px; width:200px" styleClass="extremeTableFieldsLarger"/>
		</dpe:row>
	</dpe:table>
	<div style="width:100%;">						
		<div style="text-align:center;"><input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" style="width:100px; text-align:center" onclick="javascript: afterSuccessSave = function() {setFieldsToSave(false)}; (setFieldsToSave(true) && setFormAction('EdicoleForm','${pageContext.request.contextPath}${ap}/edicoleAdm_saveEdicole.action', '', 'messageDiv'));"/></div>
	</div>			
</div>	
<s:hidden name="edicolaPlusdtIniHidden" id="edicolaPlusdtIniHidden"/>
<s:hidden name="edicolaPlusdtFinHidden" id="edicolaPlusdtFinHidden"/>	
<s:hidden name="edicolaPromodtIniHidden" id="edicolaPromodtIniHidden"/>
<s:hidden name="edicolaPromodtFinHidden" id="edicolaPromodtFinHidden"/>	
</s:form>
</s:if>


