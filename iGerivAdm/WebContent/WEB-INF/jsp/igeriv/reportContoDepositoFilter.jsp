<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { 
   		height:120px;		  
  	} 
</style> 
<s:form id="ContoDepositoFilterForm" action="infoPubblicazioni_reportContoDeposito.action" onsubmit="return (ray.ajax())" method="POST" theme="simple" validate="false">
	<div style="width:100%; text-align:center;">		
		<fieldset class="filterBolla" style="margin-top:0px; top:0px; width:800px; text-align:center"><legend style="font-size:100%"><s:text name="tableTitle"/></legend>
			<div class="required" style="align:center; text-align:center; width:550px">
				<div style="float:left; width:200px;">&nbsp;</div>
				<div style="float:left; width:100px;"><s:text name="igeriv.titolo" /></div>
				<div style="float:left; width:200px;"><s:textfield label="" name="titolo" id="titolo" cssClass="required" cssStyle="width:200px;"/></div>				
			</div>	
			<div class="required" style="align:center; text-align:center; width:550px">
				<div style="float:left; width:200px;">&nbsp;</div>
				<div style="float:left; width:100px;"><s:text name="igeriv.codice.barre" /></div>
				<div style="float:left; width:200px;"><s:textfield label="" name="codBarre" id="codBarre" cssClass="required" cssStyle="width:200px;"/></div>				
			</div>		
			<div class="required" style="align:center; text-align:center;">
				<div style="float:left; width:800px; align:center; text-align:center;">					
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:150px" />
				</div>				
			</div>			
		</fieldset>
	</div>
</s:form>