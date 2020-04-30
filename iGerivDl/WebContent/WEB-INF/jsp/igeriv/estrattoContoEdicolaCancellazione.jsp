<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { height:170px; }
</style>
<s:form id="ECForm" action="edicole_execDeleteEc.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
	<div style="width:100%; text-align:center">
		<fieldset class="filterBolla" style="width:620px; height:170px;"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div class="required" style="float:left; width:610px; height:50px">
				<div style="float:left; width:200px;"><s:text name="igeriv.cod.rivendita.o.rag.soc" /><br><em>*</em>&nbsp;<span style="font-size:70%; font-style:italic"><s:text name="dpe.primi.tre.caratteri" /></span></div>
				<div style="float:left; width:380px; text-align:left">
					<s:textfield id="edicolaLabel" name="edicolaLabel" cssStyle="width: 300px;" cssClass="tableFields"/>
				</div>
			</div>
			<div class="required" style="float:left; width:610px; height:50px">
				<div style="float:left; width:200px;"><s:text name="igeriv.data.estratto.conto.limite" /></div>
				<div style="float:left; width:380px; text-align:left">
					<s:textfield name="strDataEC" id="strDataEC" cssClass="tableFields" cssStyle="width:90px;" disabled="false"/>						
				</div>
			</div> 
			<div class="required" style="width:610px; height:40px">		
				<div style="width:100%; margin-top:10px;">
					<input type="button" value="<s:text name='dpe.contact.form.reset'/>" name="igeriv.delete" id="delete" class="tableFields" style="width:100px; text-align:center" onclick="javascript: setTimeout(function() {return (checkFields() && askForConfirm());}, 10);"/>
				</div>
			</div>
		</fieldset>	
	</div>
	<s:hidden name="codEdicolaDl" id="codEdicolaDl"/>
	<s:hidden name="actionName" id="actionName"/>
</s:form>
