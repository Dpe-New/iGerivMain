<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
		div#filter { height:200px; }
</style>
<s:form id="VenditeReportForm" action="reportVendite_showReport" method="POST" theme="simple" onsubmit="return (ray.ajax())" validate="false">
	<div id="pubMainDiv" style="width:100%;">
		<div style="width:100%; text-align:left">
			<div class="required" id="errorDiv" style="width:100%; height:15px; align:center; text-align:center"><font color='red'><s:actionerror /></font></div>
			<fieldset class="filterBolla" style="margin-top:0px; top:0px; width:650px; text-align:left"><legend style="font-size:100%"><s:text name="filterTitle"/></legend>
				<div style="width: 650px" id="filtroVendita1">
					<div class="required">
						<div style="float:left; width:307px; margin-top:10px"><s:text name="igeriv.data.vendita.det" /></div>
						<div style="float:left; width:300px; text-align:left; margin-top:10px">
							<s:textfield name="strDataVendita" id="strDataVendita" cssClass="extremeTableFields" cssStyle="width:80px;" disabled="false"/>&nbsp;&nbsp;
							<s:select
							    name="tipoProdotto"
							    id="tipoProdotto" 
							    listKey="keyInt" 
							    listValue="value"
							    list="tipiProdotto"
							    cssClass="extremeTableFields"
							    cssStyle="width:180px"
							 />							
						</div>
					</div>
				</div>
				<div style="width: 650px" id="filtroVendita2">						
					<div class="required">
						<div style="float:left; width:280px; margin-top:10px"><s:text name="igeriv.intervallo.data.vendita" /></div>
						<div style="float:left; width:350px; text-align:left; margin-top:10px">
							<s:text name="igeriv.da" />&nbsp;&nbsp;<s:textfield name="strDataMessaggioDa" id="strDataMessaggioDa" cssClass="extremeTableFields" cssStyle="width:80px;" disabled="false"/>						
							&nbsp;&nbsp;
							<s:text name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="strDataMessaggioA" id="strDataMessaggioA" cssClass="extremeTableFields" cssStyle="width:80px;" disabled="false"/>						
						</div>
					</div>
					<div class="required">
						<div style="float:left; width:280px; margin-top:10px;"><s:text name="igeriv.raggruppamento" /></div>
						<div style="float:left; width:350px; margin-top:10px">								
							<s:iterator value="%{#session['listRaggruppamento']}" status="status">	
								<input type="radio" name="raggruppamento" value='<s:text name="key"/>' <s:if test="key eq raggruppamento">checked</s:if>>&nbsp;&nbsp;<s:text name="value"/>&nbsp;												
							</s:iterator>								
						</div>
					</div>	
					<s:if test="authUser.admin == true">
						<div class="required">
							<div style="float:left; width:307px; margin-top:10px;"><s:text name="dpe.utente" /></div>
							<div style="float:left; width:300px; margin-top:10px">				
								<s:select
							    	name="codUtente"
								    id="codUtente" 
								    listKey="key" 
								    listValue="value"
								    list="utenti"
								    cssClass="extremeTableFields"
								    cssStyle="width:100px"
								 />					
							</div>
							<!--
							<div style="float:left; width:100px; margin-top:10px">	
								<s:if test="showGraficoProdottiVari == true">
									<img src="/app_img/chart.gif" width="28px" height="28px" id="imgChart" alt="${requestScope.mostraGrafico}" border="0" title="${requestScope.mostraGrafico}" style="cursor:pointer" onclick="javascript: openChart()"/> 
								</s:if>	
							</div>
							 -->
						</div>	
					</s:if>	
					<div class="required" style="align:left; text-align:left;">
						<div style="float:left; width:650px; align:center; text-align:center; margin-top:20px">					
							<s:submit name="submitRicerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssClass="required" cssStyle="text-align:center; width:150px"/>
						</div>				
					</div>	
				</div>
			</fieldset>
		</div>
	</div>
</s:form>