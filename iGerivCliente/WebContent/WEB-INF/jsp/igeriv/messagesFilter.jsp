<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
		div#filter { top:20px; }
</style>
<s:set name="templateSuffix" value="'ftl'" />
<s:set name="ap" value="#context['struts.actionMapping'].namespace" />
<s:if test='#context["struts.actionMapping"].namespace == "/"'>
	<s:set name="ap" value="" />
</s:if>	
<s:form id="filterForm" action="%{actionName}" method="POST" theme="simple" onsubmit="return (doSubmit() && ray.ajax())" validate="false">	
	<div style="width:100%; text-align:center">
		<fieldset class="filterBolla" style="width:700px; height:132px;"><legend style="font-size:100%"><s:property value="filterTitle" /></legend>
			<div class="required">
				<div style="float:left; width:200px;"><s:text name="igeriv.data.messaggio" /></div>
				<div style="float:left; width:400px; text-align:left">
					<s:text name="igeriv.da" />&nbsp;&nbsp;<s:textfield name="strDataMessaggioDa" id="strDataMessaggioDa" cssClass="tableFields" cssStyle="width:90px;" disabled="false"/>						
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="strDataMessaggioA" id="strDataMessaggioA" cssClass="tableFields" cssStyle="width:90px;" disabled="false"/>						
				</div>
				<div style="float:right; width:100px;"><font color="red" size="1"><span id="err_autocomplete"><s:fielderror><s:param>autocomplete</s:param></s:fielderror></span></font></div>
			</div>
			<div class="required">
				<div style="float:left; width:200px;"><s:text name="igeriv.cod.rivendita.o.rag.soc" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
				<div style="float:left; width:300px;"><s:textfield id="edicolaLabel" name="edicolaLabel" cssStyle="width: 300px" cssClass="tableFields"/></div>
				<div style="float:right; width:120px;"><font color="red" size="1"><span id="err_autocomplete"><s:fielderror><s:param>autocomplete</s:param></s:fielderror></span></font></div>
			</div>
			<div class="required">		
				<div style="float:left; width:620px; margin-top:0px">
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:80px" />					
				</div>
			</div>
		</fieldset>			
	</div> 
	<s:hidden name="strCodEdicolaDl" id="strCodEdicolaDl"/>
</s:form>
