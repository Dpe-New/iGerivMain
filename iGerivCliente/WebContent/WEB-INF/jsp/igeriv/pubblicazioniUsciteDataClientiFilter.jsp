<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { height:180px;} 
</style>
<form id="PubblicazioniUsciteDataForm" action="pubblicazioniClientiUsciteData_showPubblicazioniUsciteDataClienti.action" onsubmit="return (ray.ajax())" method="POST">
	<div style="width:100%; text-align:center;">		
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px;"><font color='red'><s:actionerror /></font></div>
		<fieldset class="filterBolla" style="margin-top:0px; top:0px; width:40%; text-align:center"><legend style="font-size:100%"><s:text name="tableTitle"/></legend>
			<div class="required">
				<div style="float:left; width:100%; text-align:center">
					<s:text name="igeriv.data" />&nbsp;&nbsp;<s:textfield name="data" id="data" cssClass="required" cssStyle="width:80px;" disabled="false"/>						
				</div>
			</div>
			<div class="required" style="align:center; text-align:center;">
				<div style="float:left; width:100%; align:center; text-align:center;">					
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:150px" />
				</div>				
			</div>			
		</fieldset>
	</div>
</form>