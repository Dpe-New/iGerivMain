<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:form id="filterForm" action="%{actionName}" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
	<div style="width:100%; text-align:center">
		<fieldset class="filterBolla" style="height:120px;"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div class="required">
				<div style="float:left; width:200px;"><s:text name="dpe.utente" /></div>
				<div style="float:left; width:100px;"><s:textfield label="" name="codUtente" id="codUtente"/></div>
				<div style="float:right; width:100px;"><font color="red" size="1"><span id="err_codUtente"><s:fielderror><s:param>codUtente</s:param></s:fielderror></span></font></div>
			</div>
			<div class="required">
				<div style="float:left; width:200px;"><s:text name="dpe.nome" /></div>
				<div style="float:left; width:100px;"><s:textfield label="" name="nomeUtente" id="nomeUtente"/></div>
				<div style="float:right; width:100px;"><font color="red" size="1"><span id="err_nomeUtente"><s:fielderror><s:param>nomeUtente</s:param></s:fielderror></span></font></div>
			</div>
			<div class="required">		
				<div style="float:left; width:450px; margin-top:10px">
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:80px" />&nbsp;&nbsp;
					<input type="button" id="butNuovo" value="<s:text name='plg.inserisci.nuovo'/>" align="center" cssStyle="align:center; width:80px"/>
				</div>
			</div>
		</fieldset>	
	</div>
</s:form>