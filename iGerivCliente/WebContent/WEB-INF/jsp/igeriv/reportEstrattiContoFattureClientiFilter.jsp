<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:form id="filterForm" action="reportEcFatture_show.action" method="POST" theme="simple" onsubmit="return (ray.ajax())" validate="false">	
	<div style="width:100%; text-align:center">
		<div class="required" id="errorDiv" style="width:100%; align:center; text-align:center; height:20px;"><font color='red'><s:actionerror /></font></div>
		<fieldset class="filterBolla" style="width:700px; height:145px;"><legend style="font-size:100%"><s:property value="filterTitle" /></legend>
			<div class="required">
				<div style="float:left; width:200px;"><s:text name="igeriv.data.documento" /></div>
				<div style="float:left; width:400px; text-align:left">
					<s:text name="igeriv.da" />&nbsp;&nbsp;<s:textfield name="dataDaStr" id="dataDaStr" cssClass="tableFields" cssStyle="width:90px;" disabled="false"/>						
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="dataAStr" id="dataAStr" cssClass="tableFields" cssStyle="width:90px;" disabled="false"/>						
				</div>
			</div>
			<div class="required">
				<div style="float:left; width:200px;"><s:text name="igeriv.tipo.documento" /></div>
				<div style="float:left; width:400px; text-align:left; margin-left:25px">
					<s:select name="tipo" list="listTipoDocumento" listKey="keyInt" listValue="value" cssClass="tableFields" cssStyle="width:150px;"/>
				</div>
			</div>
			<div class="required">		
				<div style="float:left; width:620px; margin-top:10px">
					<input type="button" name="ricerca" id="ricerca" value="<s:text name="dpe.contact.form.ricerca"/>" align="center" style="align:center; width:80px" onclick="javascript:doRicerca();"/>
				</div>
			</div>
		</fieldset>			
	</div> 
</s:form>
