<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
		div#filter { 
		   height:200px;		  
		} 
</style> 
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="ProdottiForm" action="%{actionName}" namespace="%{#context['struts.actionMapping'].namespace}" onsubmit="return (ray.ajax())" method="POST" theme="simple" validate="false">	
	<div id="pubMainDiv" style="width:100%;">
	<div style="width:100%; text-align:left">
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px; margin-top:-10px"><font color='red'><s:actionerror /></font></div>
		<fieldset class="filterBolla" style="margin-top:0px; top:0px; width:850px; text-align:left"><legend style="font-size:100%"><s:property value="tableTitle"/></b></legend>
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:150px;"><s:text name="igeriv.codice"/></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="codice" id="codice" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:150px;"><s:text name="igeriv.prodotto" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="descrizione" id="descrizione" cssClass="required" cssStyle="width:250px;"/></div>
			</div>
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:150px;"><s:text name="igeriv.categoria" /></div>
				<div style="float:left; width:250px;">				
					<select name="categoria" id="categoria" class="required" style="width:250px;">						
						<option value=""></option>	
						<s:iterator value="%{#session['listCategorie']}" status="status" var="arg">								
							<option value="<s:property value='pk.codCategoria'/>" <s:if test="pk.codCategoria.toString() eq categoria">selected</s:if>><s:property value='descrizione'/></option>
						</s:iterator>
					</select>	
				</div>
				<div style="float:left; width:150px;"><s:text name="igeriv.sotto.categoria" /></div>
				<div style="float:left; width:250px;">
					<select name="sottocategoria" id="sottocategoria" class="required" style="width:250px;">
					</select>	
				</div>
			</div>
			<div class="required" style="align:left; text-align:center">
				<div style="float:left; width:150px;"><s:text name="igeriv.prezzo" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="prezzo" id="prezzo" cssClass="required" cssStyle="width:250px;"/></div>
				<div style="float:left; width:150px;"><s:text name="igeriv.codice.barre" /></div>
				<div style="float:left; width:250px;"><s:textfield label="" name="barcode" id="barcode" cssClass="required" cssStyle="width:250px;"/></div>
			</div>
			<div class="required" style="align:left; text-align:center;">
				<div style="float:left; width:850px; align:center; text-align:center; height:50px; margin-top:10px">					
					<s:submit name="ricerca" id="ricerca"  key="dpe.contact.form.ricerca" align="center" cssClass="required" cssStyle="text-align:center; width:150px" onclick="submitForm();"/>
				</div>				
			</div>			
		</fieldset>
	</div>
	</div>
</s:form>

