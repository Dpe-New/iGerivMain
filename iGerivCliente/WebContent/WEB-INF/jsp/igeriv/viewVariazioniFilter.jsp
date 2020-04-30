<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { height:120px; } 
	div#content1 {  top:50px; } 
</style>
<s:form id="RichiestaRifornimentoForm" action="viewVariazioni_showVariazioniInserite.action" onsubmit="return (ray.ajax())" method="POST" theme="simple" validate="false">
	<div style="width:100%; text-align:center;">		
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px;"><font color='red'><s:actionerror /></font></div>
		<fieldset class="filterBolla" style="margin-top:0px; top:0px; width:90%; text-align:center"><legend style="font-size:100%"><s:text name="filterTitle"/></legend>
			<div class="required">
				<div style="float:left; width:180px;">&nbsp;</div>
				<div style="float:left; width:120px;"><s:text name="igeriv.intervallo.date" /></div>
				<div style="float:left; width:300px; text-align:left">
					<s:text name="igeriv.da" />&nbsp;&nbsp;<s:textfield name="dataDa" id="dataDa" cssClass="required" cssStyle="width:80px;" disabled="false"/>						
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="dataA" id="dataA" cssClass="required" cssStyle="width:80px;" disabled="false"/>						
				</div>
			</div>
			<div class="required" style="align:center; text-align:center; width:550px">
				<div style="float:left; width:200px;">&nbsp;</div>
				<div style="float:left; width:100px;"><s:text name="igeriv.titolo" /></div>
				<div style="float:left; width:200px;"><s:textfield label="" name="titolo" id="titolo" cssClass="required" cssStyle="width:230px;"/></div>				
			</div>			
			<div class="required" style="align:center; text-align:center; width:550px">
				<div style="float:left; width:200px;">&nbsp;</div>
				<div style="float:left; width:100px;"><s:text name="igeriv.richiesta.rifornimenti.quantita.stato" /></div>
				<div style="float:left; width:200px;">
					<s:select label=""
					    name="stato"
					    id="stato" 
					    listKey="key" 
					    listValue="value"
					    list="%{#session['statiPrenotazioniPubblicazioniVariazioni']}"
					    emptyOption="true" 
					    cssStyle="width: 230px"
					    cssClass="required"
				    />
				</div>				 
			</div>	
			<div class="required" style="align:center; text-align:center;">
				<div style="float:left; width:90%; align:center; text-align:center;">					
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:150px" />
				</div>				
			</div>			
		</fieldset>
	</div>
	<s:hidden name="idCliente"/>
	<s:hidden name="nomeCliente"/>
</s:form>