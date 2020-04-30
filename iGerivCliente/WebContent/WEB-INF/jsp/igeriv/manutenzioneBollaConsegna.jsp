<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.bolleRiassunto != null && !#request.bolleRiassunto.isEmpty()}">
<s:set name="an" value="%{actionName}"/>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:set name="keys" value="%{statoSelectKeys}" />
<s:set name="values" value="%{statoSelectValues}" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="ManutenzioneBollaConsegnaForm" action="edicole_showEdicole.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">
<s:if test="(strData != null && strData != '') && (codEdicola == null || codEdicola == '')">
	<dpe:table
		tableId="ManutenzioneBollaConsegnaTab"
		items="bolleRiassunto"
		var="bolleRiassunto" 
		action="${pageContext.request.contextPath}${ap}/${an}"
		imagePath="/app_img/table/*.gif"		
		rowsDisplayed="1000"			
		view="buttonsOnBottom"
		locale="${localeString}"
		state="it.dpe.igeriv.web.actions.ManutenzioneBolleAction"
		styleClass="extremeTableFieldsLarger" 
		form="ManutenzioneBollaConsegnaForm"
		theme="eXtremeTable bollaScrollDivLarge"			
		showPagination="false"
		id="ManutenzioneBollaConsegnaDiv"
		toolbarClass="eXtremeTable"
		footerStyle="height:30px;"
		filterable="false"
		>
		<dpe:exportPdf         
			fileName="mantenzione_bolla_consegna.pdf"         
			tooltip="plg.esporta.pdf" 
			headerTitle="<fo:block text-align='center'>${titoloManutenzione}</fo:block><br><fo:block text-align-last='center'>${edicola}</fo:block>"
			headerColor="black"         
			headerBackgroundColor="#b6c2da"    
			isLandscape="false"
		/>
		<ec:exportXls     
			fileName="mantenzione_bolla_consegna.xls"      
			tooltip="plg.esporta.excel"/>		
		<ec:row style="height:20px">
			<ec:column property="abbinamentoEdicolaDlVo.codEdicolaDl" width="5%" title="dpe.login.dl.fieg.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<ec:column property="abbinamentoEdicolaDlVo.anagraficaEdicolaVo.ragioneSocialeDlPrimaRiga" width="20%" title="dpe.rag.sociale" filterable="false"/>					
			<dpe:column property="pk.dtBolla" alias="dataBolla" cell="date" dateFormat="dd/MM/yyyy" width="10%" title="igeriv.data.bolla" filterable="false" style="text-align:center"/>  
			<ec:column property="pk.tipoBolla" alias="tipoBolla" width="10%" title="igeriv.tipo.bolla" filterable="false" style="text-align:center"/>
			<dpe:column property="bollaTrasmessaDl" alias="stato" cell="dpeCombo"  width="20%" title="igeriv.richiesta.rifornimenti.quantita.stato" filterable="false" sessionVarName="stato" optionKeys="${keys}" optionValues="${values}" pkName="pk" fieldName="bollaTrasmessaDl" hasHiddenPkField="true" style="font-size:12px; width:200px" styleClass="extremeTableFieldsLarger"/>
			<dpe:column property="dtTrasmissione" alias="dataTrasmissione" cell="date" dateFormat="dd/MM/yyyy HH:mm:ss" width="10%" title="igeriv.data.trasmissione" filterable="false" style="text-align:center"/>  
		</ec:row>
	</dpe:table>
</s:if>
<s:else>
	<dpe:table
		tableId="ManutenzioneBollaConsegnaTab"
		items="bolleRiassunto"
		var="bolleRiassunto" 
		action="${pageContext.request.contextPath}${ap}/${an}"
		imagePath="/app_img/table/*.gif"		
		rowsDisplayed="1000"			
		view="buttonsOnBottom"
		locale="${localeString}"
		state="it.dpe.igeriv.web.actions.ManutenzioneBolleAction"
		styleClass="extremeTableFieldsLarger" 
		form="ManutenzioneBollaConsegnaForm"
		theme="eXtremeTable bollaScrollDivLarge"			
		showPagination="false"
		id="ManutenzioneBollaConsegnaDiv"
		toolbarClass="eXtremeTable"
		footerStyle="height:30px;"
		filterable="false"
		>
		<dpe:exportPdf         
			fileName="mantenzione_bolla_consegna.pdf"         
			tooltip="plg.esporta.pdf" 
			headerTitle="<fo:block text-align='center'>${titoloManutenzione}</fo:block><br><fo:block text-align-last='center'>${edicola}</fo:block>"
			headerColor="black"         
			headerBackgroundColor="#b6c2da"    
			isLandscape="false"
		/>
		<ec:exportXls     
			fileName="mantenzione_bolla_consegna.xls"      
			tooltip="plg.esporta.excel"/>		
		<ec:row style="height:20px">					
			<dpe:column property="pk.dtBolla" alias="dataBolla" cell="date" dateFormat="dd/MM/yyyy" width="10%" title="igeriv.data.bolla" filterable="false" style="text-align:center"/>  
			<ec:column property="pk.tipoBolla" alias="tipoBolla" width="10%" title="igeriv.tipo.bolla" filterable="false" style="text-align:center"/>
			<dpe:column property="bollaTrasmessaDl" alias="stato" cell="dpeCombo"  width="20%" title="igeriv.richiesta.rifornimenti.quantita.stato" filterable="false" sessionVarName="stato" optionKeys="${keys}" optionValues="${values}" pkName="pk" fieldName="bollaTrasmessaDl" hasHiddenPkField="true" style="font-size:12px; width:200px" styleClass="extremeTableFieldsLarger"/>
			<dpe:column property="dtTrasmissione" alias="dataTrasmissione" cell="date" dateFormat="dd/MM/yyyy HH:mm:ss" width="10%" title="igeriv.data.trasmissione" filterable="false" style="text-align:center"/>  
		</ec:row>
	</dpe:table>
</s:else>
<div style="width:100%;">						
	<div style="text-align:center;"><input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" style="width:100px; text-align:center" onclick="javascript: afterSuccessSave = function() {setFieldsToSave(false)}; return (setFieldsToSave(true) && setFormAction('ManutenzioneBollaConsegnaForm','${pageContext.request.contextPath}${ap}/manutenzioneBollaConsegna_saveBolleRiassunto.action', '', 'messageDiv'));"/></div>
</div>
</div>
</s:form>
</s:if>


