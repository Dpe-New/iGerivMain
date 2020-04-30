<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div id="contentDiv">
<s:if test="%{#request.listQuadraturaResa != null && !#request.listQuadraturaResa.isEmpty()}">
	<s:set name="an" value="%{actionName}"/>
	<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
	<s:if test='#context["struts.actionMapping"].namespace == "/"'>
		<s:set name="ap" value="" />
	</s:if>	
	<s:form id="QuadraturaResaForm" action="bollaResa_" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div style="width:100%">					
			<dpe:table
				tableId="BollaResaTab" 
				items="listQuadraturaResa"
				var="listQuadraturaResa" 
				action="${pageContext.request.contextPath}${ap}/${an}"						
				imagePath="/app_img/table/*.gif"				
				style="table-layout:auto; height: 350px"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.BollaResaAction"
				styleClass="extremeTableFields"	
				form="QuadraturaResaForm"		
				theme="eXtremeTable bollaScrollDiv"			
				showPagination="false"
				id="QuadraturaResaScrollDiv"
				toolbarClass="eXtremeTable"
				showStatusBar="false"
				showTitle="false"
				showTooltips="false"
				footerStyle="height:30px;"
				filterable="false"
				showExports="true"
				>
				<dpe:exportPdf         
					fileName="report_quadratura_resa.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="<fo:block text-align='center'>${title} Data: ${strData} Tipo ${tipo}</fo:block><br><fo:block text-align-last='justify'>${intestazioneAg}<fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${codAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>" 
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
					regionBeforeExtentInches="1.5"
					marginTopInches="1.2" 
					repeatColumnHeaders="true"
				/> 
				<ec:exportXls     
					fileName="report_quadratura_resa.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row style="height:25px">
					<ec:column property="cpu" width="5%" title="igeriv.codice.pubblicazione.title" filterable="false" style="text-align:center" headerStyle="text-align:center"/>				
					<ec:column property="titolo" width="28%" title="igeriv.titolo" filterable="false"/> 
					<ec:column property="numero" width="5%" title="igeriv.numero" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="copieDichiarate" width="10%" title="igeriv.copie.dichiarate" filterable="false" style="text-align:center" headerStyle="text-align:center"/>              
					<ec:column property="copieRiscontrate" width="10%" title="igeriv.copie.riscontrate" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="differenza" width="10%" title="igeriv.differenza.ext" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<ec:column property="copieRespinte" width="10%" title="igeriv.copie.respinte" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
					<dpe:column property="motivo" width="20%" title="igeriv.motivo" filterable="false" style="text-align:left" headerStyle="text-align:left"/>                   
				</dpe:row>
			</dpe:table>	 	
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessuna.differenza.bolla.resa"/>
	</div>
</s:else>
</div>