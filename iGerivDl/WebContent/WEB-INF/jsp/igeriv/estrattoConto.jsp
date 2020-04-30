<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
	div#filter { height:100px;}
</style>
<div id="contentDiv" style="margin-top:30px">
<s:if test="%{#request.listEstrattoContoDettaglio != null && !#request.listEstrattoContoDettaglio.isEmpty()}">
	<s:set name="an" value="%{actionName}"/>
	<s:form id="EstrattoContoForm" action="%{actionName}" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
		<div style="width:100%">					
			<dpe:table
				tableId="EstrattoContoTab" 
				items="listEstrattoContoDettaglio"
				var="listEstrattoContoDettaglio" 
				action="${pageContext.request.contextPath}/${an}"						
				imagePath="/app_img/table/*.gif"				
				style="height:60px;"		
				view="buttonsOnBottom"
				locale="${localeString}"
				state="it.dpe.igeriv.web.actions.EstrattoContoAction"
				styleClass="extremeTableFields"	
				form="EstrattoContoForm"		
				theme="eXtremeTable bollaScrollDivTall"			
				showPagination="false"
				id="EstrattoContoScrollDiv"
				toolbarClass="eXtremeTable"
				showStatusBar="false"
				showTitle="false"
				showTooltips="false"
				footerStyle="height:30px;"
				filterable="false"
				>
				<dpe:exportPdf         
					fileName="estrattoConto_${data}_Num_${numero}.pdf"         
					tooltip="plg.esporta.pdf" 
					headerTitle="<fo:block text-align='center'>${title}</fo:block><br><fo:block text-align-last='justify'>${intestazioneAg}<fo:leader leader-pattern='space'/>${intestazioneRiv}</fo:block><fo:block text-align-last='justify'>${codAgenzia}<fo:leader leader-pattern='space'/>${codEdicola}</fo:block><fo:block text-align-last='justify'>${ragSocAgenzia}<fo:leader leader-pattern='space'/>${ragSocEdicola}</fo:block><fo:block text-align-last='justify'>${indirizzoAgenzia}<fo:leader leader-pattern='space'/>${indirizzoEdicola}</fo:block>"
					headerColor="black"         
					headerBackgroundColor="#b6c2da"    
					isLandscape="true"
					regionBeforeExtentInches="1.5"
					marginTopInches="1.2"
					logoImage="/app_img/rodis.gif"
				/> 
				<ec:exportXls     
					fileName="estrattoConto_${data}_Num_${numero}.xls"      
					tooltip="plg.esporta.excel"/>		
				<dpe:row interceptor="marker" style="height:25px">	
					<dpe:column property="dataMovimento" width="10%" title="igeriv.data.tipo.estratto.conto" cell="date" dateFormat="dd/MM/yyyy" filterable="false" sortable="false" style="text-align:center" headerStyle="text-align:center"/>			
					<dpe:column property="note" width="40%" title="igeriv.descrizione" filterable="false" sortable="false" />         
					<dpe:column property="importoDare" cell="currency" hasCurrencySign="true" width="20%" title="igeriv.dare" filterable="false" sortable="false" style="text-align:right" headerStyle="text-align:right" format="###.###.##0,00" /> 
					<dpe:column property="importoAvere" cell="currency" hasCurrencySign="true" width="20%" title="igeriv.avere" filterable="false" sortable="false" style="text-align:right" headerStyle="text-align:right" format="###.###.##0,00" />
					<ec:column property="fake" width="5%" title=" " filterable="false" sortable="false" viewsDenied="pdf,xls"/>
				</dpe:row>
			</dpe:table>	 	
		</div>
	</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:property value="nessunRisultato"/>
	</div>
</s:else>
</div>