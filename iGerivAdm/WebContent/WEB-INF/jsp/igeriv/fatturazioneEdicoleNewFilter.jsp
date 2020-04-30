agenzie<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { height:250px; }
</style>
<s:form id="filterForm" action="fatturazioneNew_showNewFatturazione.action" namespace="/" method="POST" theme="simple" validate="true" onsubmit="return (ray.ajax())">	
	<div style="width:100%; text-align:center">
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px;"><font color='red'><s:actionerror /></font></div>
		<fieldset class="filterBolla" style="width:800px; height:160px;"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div class="required" style="height:30px; margin-top:10px">
				<div style="float:left; width:200px;"><s:text name="dpe.nome.dl" /></div>	
				<div style="float:left; width:400px;">
					<s:select label=""
					    name="codDl"
					    id="codDl" 
					    listKey="key" 
					    listValue="value"
					    list="%{#session['dl']}"
					    emptyOption="true" 
					    cssStyle="width:400px"
					    cssClass="tableFields"
					    />	
				</div>
				<div style="float:left; width:150px;"><font color="red" size="1"><span id="err_codDl"><s:fielderror><s:param>codDl</s:param></s:fielderror></span></font></div>
			</div>
			<div class="required">
				<div style="float:left; width:200px;"><s:text name="igeriv.data.fatturazione" /></div>
				<div style="float:left; width:400px; text-align:left">
					<s:textfield name="dataStr" id="dataStr" cssClass="tableFields" cssStyle="width:90px;" disabled="false"/>						
				</div>
				<div style="float:left; width:150px;"><font color="red" size="1"><span id="err_dataStr"><s:fielderror><s:param>dataStr</s:param></s:fielderror></span></font></div>
			</div>
			<div class="required">
				<div style="float:left; width:200px;"><s:text name="igeriv.trimestre.fatturazione" /></div>
				<div style="float:left; width:400px; text-align:left">
						<input type="radio" name="trimestre" id="trimestre1" value='0' <s:if test="trimestre == null || trimestre eq 0">checked</s:if>>&nbsp;&nbsp;<s:text name="toolbar.text.prevPage"/>&nbsp;												
						<input type="radio" name="trimestre" id="trimestre2" value='1' <s:if test="1 eq trimestre">checked</s:if>>&nbsp;&nbsp;<s:text name="toolbar.text.successivo"/>&nbsp;
				</div>
				<div style="float:left; width:150px;">&nbsp;</div>
			</div>
			<div class="required" style="width:100%; text-align:center; margin-top:50px">
				<div style="width:75px; margin-left:auto; margin-right:auto;">		
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:80px" />
				</div>
			</div>
		</fieldset>	
	</div>
</s:form>
