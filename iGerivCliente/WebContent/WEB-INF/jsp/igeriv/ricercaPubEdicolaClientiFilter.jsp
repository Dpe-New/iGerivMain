<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
	div#filter { 
	   height:200px;		  
	  } 
	  
	div#content1 { 
		height:500px;
	  } 
	  
	.required li{ 
		list-style: none; 
	}
</style> 
<s:set var="filterTitle" value="%{tableTitle}" />
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="RicercaPubEdicolaForm" action="pubblicazioniClienti_showPubblicazioni.action" namespace="%{#context['struts.actionMapping'].namespace}" onsubmit="return (ray.ajax())" method="POST" theme="simple" validate="false">	
	<div id="pubMainDiv" style="width:100%;">
	<div style="width:100%; text-align:left">
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px; margin-top:-10px"><font color='red'><s:actionerror /></font></div>
		<fieldset class="filterBolla" style="margin-top:0px; top:0px; width:750px; text-align:left"><legend style="font-size:100%">${filterTitle} &nbsp;<b><s:property value="nomeCliente"/></b></legend>
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:100px;"><s:text name="igeriv.titolo"/></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="titolo" id="titolo" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;"><s:text name="igeriv.sottotitolo" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="sottotitolo" id="sottotitolo" cssClass="required" cssStyle="width:250px;"/></div>
			</div>
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:100px;"><s:text name="igeriv.argomento" /></div>
				<div style="float:left; width:250px;">				
					<select name="argomento" id="argomento" class="required" style="width:250px;">						
						<option value=""></option>	
						<s:iterator value="%{#session['listArgomento']}" status="status" var="arg">								
							<option value="<s:property value='pk'/>" <s:if test="pk.toString() eq argomento">selected</s:if>><s:property value='dataBollaFormat'/><s:property value='descrizione'/></option>
						</s:iterator>
					</select>	
				</div>
				<div style="float:left; width:100px;"><s:text name="igeriv.periodicita" /></div>
				<div style="float:left; width:250px;">
					<select name="periodicita" id="periodicita" class="required" style="width:250px;">
						<option value=""></option>	
						<s:iterator value="%{#session['listPeriodicita']}" status="status">
							<option value="<s:property value='pk'/>" <s:if test="pk.toString() eq periodicita">selected</s:if>><s:property value='dataBollaFormat'/><s:property value='descrizione'/></option>
						</s:iterator>
					</select>	
				</div>
			</div>
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:100px;"><s:text name="igeriv.prezzo" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="prezzo" id="prezzo" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;">Cod. Pubb.</div>
				<div style="float:left; width:250px;"><s:textfield label="" name="codPubblicazione" id="codPubblicazione" cssClass="required" cssStyle="width:250px;"/></div>
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:100px;">Cod. Barre</div>
				<div style="float:left; width:250px;"><s:textfield label="" name="codBarre" id="codBarre" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:100px;">&nbsp;</div>
				<div style="float:left; width:250px;">&nbsp;</div>
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:750px; align:center; text-align:center;">					
					<input type="button" name="dpe.contact.form.submit" id="ricerca" value="<s:text name='dpe.contact.form.ricerca'/>" align="center" class="required" style="text-align:center; width:150px" onclick="submitForm();"/>
				</div>				
			</div>			
		</fieldset>
	</div>
	</div>
</s:form>