<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:if test="%{#request.clienti != null && !#request.clienti.isEmpty()}">
<s:set name="keys" value="#session.listCSVNumber0to100" />
<s:set name="values" value="#session.listCSVNumber0to100" />
<s:form id="EstrattoContoClientiForm" action="estrattoContoClientiEdicola_showClientiEstrattoConto.action" method="POST" theme="simple" validate="false">		
	<div id="mainDiv" style="width:100%">						
		<dpe:table
			tableId="EstrattoContoClientiTab" 
			items="clienti"
			var="clienti" 
			action="${pageContext.request.contextPath}/estrattoContoClientiEdicola_showClientiEstrattoConto.action"						
			imagePath="/app_img/table/*.gif"				
			style="height:60px;"		
			view="buttonsOnBottom"
			locale="${localeString}"
			state="it.dpe.igeriv.web.actions.EstrattoContoClientiEdicolaAction"
			styleClass="extremeTableFields"	
			form="EstrattoContoClientiForm"		
			theme="eXtremeTable bollaScrollDivSmall"			
			showPagination="false"
			id="EstrattoContoScrollDiv"
			toolbarClass="eXtremeTable"
			showStatusBar="false"
			showTitle="false"
			showTooltips="false"
			footerStyle="height:30px;"
			filterable="false"
			autoIncludeParameters="false"
			>
			<dpe:exportPdf         
				fileName="estrattoContoClienti_${strDataCompetenza}.pdf"         
				tooltip="plg.esporta.pdf" 
				headerTitle="igeriv.emissione.estratto.conto.clienti" 
				headerColor="black"         
				headerBackgroundColor="#b6c2da"    
				isLandscape="false"
			/>
			<ec:exportXls     
				fileName="estrattoContoClienti_${strDataCompetenza}.xls"      
				tooltip="plg.esporta.excel"/>		
			<dpe:row highlightRow="true" interceptor="marker" href="#?idCliente=codCliente" style="height:30px;">
				<dpe:column property="fake" width="4%" title=" " cell="selectedSpunta" headerCell="buttonSpuntaAutomaticaOrdiniClienti" pkName="codCliente" hasHiddenPkField="true" style="text-align:center" filterable="false" sortable="false"/>  
				<ec:column property="nome" width="13%" title="dpe.nome" filterable="false"/>         
				<ec:column property="cognome" width="13%" title="dpe.cognome" filterable="false"/> 
				<ec:column property="localitaDesc" width="13%" title="dpe.localita" filterable="false"/>                    
				<ec:column property="provinciaDesc" width="5%" title="dpe.provincia" filterable="false" headerStyle="text-align:center" style="text-align:center"/>                 
				<ec:column property="cap" width="5%" title="dpe.cap" filterable="false" headerStyle="text-align:center" style="text-align:center"/>   
				<ec:column property="telefono" width="10%" title="dpe.telefono" filterable="false" headerStyle="text-align:center" style="text-align:center"/>
				<ec:column property="email" width="15%" title="dpe.email" filterable="false" headerStyle="text-align:center" style="text-align:center"/>
				<dpe:column property="totaleEstrattoContoPubb" cell="currency" width="7%" title="igeriv.importo.pubb" format="###.###.##0,00" hasCurrencySign="true" filterable="false" headerStyle="text-align:right" style="text-align:right"/>
				<dpe:column property="totaleEstrattoContoPne" cell="currency" width="7%" title="igeriv.importo.prod" format="###.###.##0,00" hasCurrencySign="true" filterable="false" headerStyle="text-align:right" style="text-align:right"/>
				<dpe:column property="sconto" cell="dpeECScontoCombo" width="7%" title="igeriv.sconto.perc.pub" filterable="false" hasEmptyOption="false" style="font-size:12px; width:80px; text-align:center" styleClass="extremeTableFieldsLarger" viewsDenied="pdf,xls" sortable="false"/>
				<dpe:column property="scontoPne" cell="dpeECScontoComboPne" width="7%" title="igeriv.sconto.perc.prod" filterable="false" hasEmptyOption="false" style="font-size:12px; width:80px; text-align:center" styleClass="extremeTableFieldsLarger" viewsDenied="pdf,xls" sortable="false"/>
			</dpe:row>
		</dpe:table>
		<div style="text-align:center; margin-top:15px;">
			<s:text name="igeriv.data.documento" />:&nbsp;&nbsp;<s:textfield name="strDataDocumento" id="strDataDocumento" cssStyle="width:90px;" disabled="false"/>&nbsp;&nbsp;&nbsp;&nbsp;
			<s:text name="igeriv.sconto" />&nbsp;<s:text name="igeriv.pubblicazioni"/>&nbsp;(<s:text name="igeriv.default"/>):&nbsp;&nbsp;
				<s:select label=""
				    name="scontoPerc"
				    id="scontoPerc" 
				    listKey="keyInt"
				    listValue="value"
				    list="%{#session['listNumberVo0to100']}"
				    emptyOption="false" 
				    cssStyle="width: 60px"
				    />%&nbsp;&nbsp;&nbsp;&nbsp;
			<s:text name="igeriv.sconto" />&nbsp;<s:text name="igeriv.prodotti.vari"/>&nbsp;(<s:text name="igeriv.default"/>):&nbsp;&nbsp;
				<s:select label=""
				    name="scontoPercPne"
				    id="scontoPercPne" 
				    listKey="keyInt"
				    listValue="value"
				    list="%{#session['listNumberVo0to100']}"
				    emptyOption="false" 
				    cssStyle="width: 60px"
				    />%&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div style="text-align:center; margin-top:15px;">
			<input type="button" value="" name="emettiEC" id="emettiEC" class="tableFields" style="width:200px; text-align:center; background: #ffffcc" onclick="javascript: return emettiEstrattoConto();"/>
		</div>
	</div>
	<s:hidden name="strDataComp" id="strDataComp"/>
	<s:hidden name="tipiEstrattoConto" />
	<s:hidden name="tipoDocumento" id="tipoDocumento" />
</s:form>
</s:if>
<s:else>
	<div class="tableFields" style="width:100%; align:center; text-align:center; margin-top: 50px">
		<s:text name="igeriv.nessun.risultato"/>
	</div>
</s:else>
<div id="one"></div> 

