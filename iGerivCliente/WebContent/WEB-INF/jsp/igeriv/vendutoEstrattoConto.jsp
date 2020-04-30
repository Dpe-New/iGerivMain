<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.listVendutoEstrattoConto != null && !#request.listVendutoEstrattoConto.isEmpty()}">
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="VendutoEstrattoConto" action="venduto_showVenduto.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width:100%">	
		<dpe:table
			tableId="EstrattoContoTab"
			items="listVendutoEstrattoConto"
			var="listVendutoEstrattoConto" 
			action="${pageContext.request.contextPath}${ap}/venduto_showVenduto.action"
			imagePath="/app_img/table/*.gif"
			title="${tableTitle}"			
			rowsDisplayed="1000"			
			view="buttonsOnTopAndBottom"
			extraToolButton='<img src="/app_img/chart.gif" width="28px" height="28px" id="imgChart" alt="${requestScope.mostraGrafico}" border="0" title="${requestScope.mostraGrafico}" style="cursor:pointer" onclick="javascript: openChart()"/>' 
			extraToolButtonStyle="width:100%; text-align:center;"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.MancanzeAction"
			styleClass="extremeTableFields" 
			form="VendutoEstrattoConto"
			theme="eXtremeTable bollaScrollDivTall"			
			showPagination="false"
			id="EstrattoContoScrollDiv"
			toolbarClass="eXtremeTable"
			footerStyle="height:30px;"
			filterable="false"
			>
			<dpe:exportPdf         
				fileName="venduto_estratto_conto_${dataDaStr}_${dataAStr}.pdf"         
				tooltip="plg.esporta.pdf" 
				headerTitle="<fo:block text-align='center'>${title}</fo:block><br><fo:block text-align-last='justify'>${intestazioneAg}<fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${codAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>"
				headerColor="black"         
				headerBackgroundColor="#b6c2da"    
				isLandscape="true"
				regionBeforeExtentInches="1.5"
				marginTopInches="1.2"
			/>
			<ec:exportXls     
				fileName="venduto_estratto_conto_${dataDaStr}_${dataAStr}.xls"      
				tooltip="plg.esporta.excel"/>		
			<ec:row style="height:25px">
				<dpe:column property="dataEstrattoConto" cell="date" dateFormat="dd/MM/yyyy" width="12%" title="igeriv.data.tipo.estratto.conto" filterable="false" style="text-align:center" headerStyle="text-align:center"/>
				<dpe:column property="fornitoQuotidiani" width="12%" cell="currency" hasCurrencySign="true" calc="total" calcTitle="column.calc.total" totalCellStyle="text-align:right" title="igeriv.fornito.quotidiani" filterable="false" style="text-align:right" headerStyle="text-align:right"/> 
				<dpe:column property="resoQuotidiani" width="12%" cell="currency" hasCurrencySign="true" calc="total" totalCellStyle="text-align:right" title="igeriv.reso.quotidiani" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
				<dpe:column property="vendutoQuotidiani" width="12%" cell="currency" hasCurrencySign="true" calc="total" totalCellStyle="text-align:right" title="igeriv.venduto.quotidiani" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
				<dpe:column property="fornitoPeriodici" width="12%" cell="currency" hasCurrencySign="true" calc="total" totalCellStyle="text-align:right" title="igeriv.fornito.periodici" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
				<dpe:column property="resoPeriodici" width="12%" cell="currency" hasCurrencySign="true" calc="total" totalCellStyle="text-align:right" title="igeriv.reso.periodici" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
				<dpe:column property="vendutoPeriodici" width="12%" cell="currency" hasCurrencySign="true" calc="total" totalCellStyle="text-align:right" title="igeriv.venduto.periodici" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
				<dpe:column property="venduto" width="12%" cell="currency" hasCurrencySign="true" calc="total" totalCellStyle="text-align:right" title="igeriv.venduto" filterable="false" style="text-align:right" headerStyle="text-align:right"/>
			</ec:row>
		</dpe:table>
	</div>		
</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato"/>
	</div>
</s:else>


