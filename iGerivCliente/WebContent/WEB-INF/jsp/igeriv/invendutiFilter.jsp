<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { 
   		height:180px;		  
  	} 
</style> 
<s:form id="PubblicazioniInveduteFilterForm" action="invenduti_show.action" onsubmit="return (ray.ajax())" method="POST" theme="simple" validate="false">
	<div style="width:100%; text-align:center;">		
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px; margin-top:-10px"><font color='red'><s:actionerror /></font></div>
		<fieldset class="filterBolla" style="width:610px;"><legend style="font-size:100%"><s:text name="tableTitle"/></legend>
			<div class="required" style="width:550px;">
				<div style="float:left; width:200px;"><s:text name="igeriv.data.uscita" /></div>
				<div style="float:left; width:330px;">
					<s:text name="igeriv.da" />&nbsp;&nbsp;<s:textfield name="strDataDa" id="strDataDa" cssStyle="width:78px;" disabled="false"/>						
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="strDataA" id="strDataA" cssStyle="width:78px;" disabled="false"/>						
				</div>				
			</div>
			<div class="required" style="align:center; width:550px">
				<div style="float:left; width:225px;"><s:text name="igeriv.titolo" /></div>
				<div style="float:left; width:305px;"><s:textfield label="" name="titolo" id="titolo" cssClass="required" cssStyle="width:200px;"/></div>				
			</div>	
			<div class="required" style="align:center; width:550px">
				<div style="float:left; width:225px;"><s:text name="igeriv.basato.su" /></div>
				<div style="float:left; width:305px;">
					<s:select label=""
					    name="baseCalcolo"
					    id="baseCalcolo" 
					    listKey="keyInt" 
					    listValue="value"
					    list="listBaseCalcolo"
					    emptyOption="false" 
					    />
				</div>				
			</div>
			<div class="required" style="align:center; width:550px">
				<div style="float:left; width:225px;"><s:text name="igeriv.escludi.quotidiani" /></div>
				<div style="float:left; width:305px;"><s:checkbox label="" name="escludiQuotidiani" id="escludiQuotidiani" cssClass="required" /></div>				
			</div>	
			<div class="required" style="align:center; text-align:center;">
				<div style="float:left; width:610px; align:center; text-align:center;">					
					<input type="button" name="ricerca" id="ricerca" value="<s:text name="dpe.contact.form.ricerca"/>" align="center" style="align:center; width:150px" />
				</div>				
			</div>			
		</fieldset>
	</div>
</s:form>