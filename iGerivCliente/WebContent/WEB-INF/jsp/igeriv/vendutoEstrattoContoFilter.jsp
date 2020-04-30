<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#content1 { height:550px; } 
</style>
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="VendutoEstrattoContoFilterForm" action="venduto_showVenduto.action" onsubmit="return (ray.ajax())" method="POST" theme="simple" validate="false">
	<div style="width:100%; text-align:center;">	
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px;"><font color='red'><s:actionerror /></font></div>	
		<fieldset class="filterBolla" style="width:450px; text-align:center"><legend style="font-size:100%"><s:text name="tableTitle"/></legend>
			<div class="required" style="margin-left:auto; margin-right:auto; align:center; text-align:center; width:450px">
				<div style="float:left; width:150px;"><s:text name="igeriv.data.tipo.estratto.conto" /></div>
				<div style="float:left; width:250px;">
					<s:text name="igeriv.da" />&nbsp;&nbsp;<s:textfield name="dataDaStr" id="dataDaStr" cssStyle="width:78px;" disabled="false"/>						
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="dataAStr" id="dataAStr" cssStyle="width:78px;" disabled="false"/>						
				</div>				
			</div>
			<div class="required" style="align:center; text-align:center;">
				<div style="float:left; margin-top:20px; width:450px; text-align:center;">					
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:150px" />
				</div>				
			</div>			
		</fieldset>
	</div>
</s:form>