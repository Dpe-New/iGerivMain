<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.fatturazione != null && !#request.fatturazione.isEmpty()}">
<s:set name="an" value="%{actionName}"/>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="FatturazioneForm" action="fatturazione_showFatturazione.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
<div style="width:100%">	
	<dpe:table
		tableId="FatturazioneTab"
		items="fatturazione"
		var="fatturazione" 
		action="${pageContext.request.contextPath}${ap}/fatturazione_showFatturazione.action"
		imagePath="/app_img/table/*.gif"
		title=""			
		rowsDisplayed="1000"			
		view="buttonsOnBottom"
		locale="${localeString}"
		state="it.dpe.igeriv.web.actions.FatturazioneEdicoleAction"
		styleClass="extremeTableFields" 
		form="FatturazioneForm"
		theme="eXtremeTable bollaScrollDivTall"			
		showPagination="false"
		id="EdicoleDiv"
		toolbarClass="eXtremeTable"
		footerStyle="height:30px;"
		filterable="false"
		>
		<dpe:exportPdf         
			fileName="fatturazione.pdf"         
			tooltip="plg.esporta.pdf" 
			headerTitle="${title}" 
			headerColor="black"         
			headerBackgroundColor="#b6c2da"    
			isLandscape="true"
		/>
		<ec:exportXls     
			fileName="fatturazione.xls"      
			tooltip="plg.esporta.excel"/>		
		<dpe:row style="height:30px">					
			<dpe:column property="coddl" width="4%" title="username.dl" filterable="false" style="text-align:center" headerStyle="text-align:center"/>  
			<dpe:column property="nomeDl" width="12%" title="dpe.nome.dl" filterable="false" style="text-align:left" headerStyle="text-align:left"/>
			<dpe:column property="dtAttivazioneEdicola" width="8%" title="dpe.data.attivazione.edicola" cell="date" format="dd/MM/yyyy" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<dpe:column property="codEdicolaDl" width="4%" title="dpe.login.dl.fieg.code.short" filterable="false" style="text-align:center" headerStyle="text-align:center"/>              
			<dpe:column property="nomeEdicola" width="14%" title="dpe.nome.edicola" filterable="false" style="text-align:left" headerStyle="text-align:left" />
			<dpe:column property="giorni" width="5%" title="igeriv.giorni" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<dpe:column property="dtSospensioneEdicola" width="8%" title="igeriv.data.sospensione" cell="date" format="dd/MM/yyyy" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<dpe:column property="roleName" width="8%" title="igeriv.tipo.profilo" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
			<dpe:column property="importoiGeriv" width="8%" title="igeriv.importo.igeriv" calc="total" calcTitle="column.calc.total" hasCurrencySign="true" totalFormat="###.###.##0,00" totalCellStyle="text-align:right" cell="currency" format="###.###.##0,00" filterable="false" style="text-align:right" headerStyle="text-align:right"/> 
			<dpe:column property="importoiGerivPlus" width="8%" title="igeriv.importo.igeriv.plus" calc="total" hasCurrencySign="true" totalFormat="###.###.##0,00" totalCellStyle="text-align:right" cell="currency" format="###.###.##0,00" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
			<dpe:column property="importoiGerivPromo" width="8%" title="igeriv.importo.igeriv.promo" calc="total" hasCurrencySign="true" totalFormat="###.###.##0,00" totalCellStyle="text-align:right" cell="currency" format="###.###.##0,00" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
			<dpe:column property="importo" width="8%" title="igeriv.importo" calc="total" hasCurrencySign="true" totalFormat="###.###.##0,00" totalCellStyle="text-align:right" cell="currency" format="###.###.##0,00" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
		</dpe:row>
	</dpe:table>
</div>		
</s:form>
</s:if>


