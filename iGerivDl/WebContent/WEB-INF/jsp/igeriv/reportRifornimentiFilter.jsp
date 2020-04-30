<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { height:240px; }
</style>
<s:set name="templateSuffix" value="'ftl'" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="filterForm" action="%{actionName}" method="POST" theme="simple" validate="false" onsubmit="return (prepareFieldValues() && doSubmit() && ray.ajax())">	
	<div style="width:100%; text-align:center">
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px; margin-top:-10px"><font color='red'><s:actionerror /></font></div>
		<fieldset class="filterBolla" style="width:520px; height:230px;"><legend style="font-size:100%"><s:property value="filterTitle" /></legend>
			<div class="required" style="width:520px;">
				<div style="float:left; width:200px;"><s:text name="igeriv.data.richiesta" /></div>
				<div style="float:left; width:100px; text-align:left"><s:textfield name="strDataDa" id="strDataDa" cssStyle="width:78px;" disabled="false"/></div>	
				<div style="float:left; width:200px; text-align:left"><s:textfield name="strDataA" id="strDataA" cssStyle="width:78px;" disabled="false"/></div>
			</div>
			<div class="required" style="width:520px;"> 
				<div style="float:left; width:200px;"><s:text name="igeriv.cod.rivendita.dl.o.rag.soc" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
				<div style="float:left; width:300px;"><s:textfield id="autocompleteRiv" name="autocompleteRiv" cssStyle="width: 300px" cssClass="tableFields"/></div>
			</div>
			<div class="required" style="width:520px; margin-top:80px"> 
				<div style="float:left; width:200px;"><s:text name="igeriv.cod.pubblicazione.o.titolo" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
				<div style="float:left; width:300px;"><s:textfield id="autocompletePub" name="autocompletePub" cssStyle="width: 300px" cssClass="tableFields"/></div>
			</div>
			<div class="required" style="width:520px;  margin-top:40px;">
				<div style="float:left; width:200px;"><s:text name="igeriv.cod.numero.copertina" /></div>
				<div style="float:left; width:300px; text-align:left">
					<select name="numCopertina" id="numCopertina" style="width:300px">
					</select>
				</div>	
			</div>
			<div class="required">		
				<div style="float:left; width:520px; margin-top:10px">
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:80px" />
				</div>
			</div>
		</fieldset>	
	</div> 
	<s:hidden name="codEdicola" id="codEdicola"/>
	<s:hidden name="codPubblicazione" id="codPubblicazione"/>
</s:form>