<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %> 
<style>
	div#filter { 
   		height:120px;		  
  	} 
</style>
<s:form id="PubblicazioniNonUsciteForm" action="pubblicazioniNonUscite_showElencoPubblicazioni.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
	<div style="text-align:center">
		<fieldset class="filterBolla" ><legend style="font-size:100%;"><s:property value="filterTitle" /></legend>
			<div class="required" style="float:left">
				<div style="float:left; text-align:left; width:150px">
					<s:if test="#an != ''">
						<s:text name="igeriv.data.tipo.bolla" />	
					</s:if>
				</div>
				<div style="float:left; width:300px">
					<s:select list="listaBolle" name="dataTipoBolla"
					listKey="dataTipoBolla"
					listValue="dtAndTipoBolla"
					/>				
				</div>
			</div>
			<div style="align:center; text-align:center; margin-top:20px;">
				<div style="float:left; width:610px; align:center; text-align:center;">					
					<input type="button" name="ricerca" id="ricerca" value="<s:text name="dpe.contact.form.ricerca"/>" align="center" style="align:center; width:150px" onclick="doSubmit();"/>
				</div>				
			</div>	
		</fieldset>
	</div>
</s:form>