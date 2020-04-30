<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<style>
	div#filter { height:100px; } 
</style>
<s:form id="filterForm" action="menu_showMenus.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
	<div style="width:100%; text-align:center">
		<fieldset class="filterBolla" style="height:120px;"><legend style="font-size:100%"><s:property value="tableTitle" /></legend>
			<div class="required">
				<s:select label="" 
				    name="idProfilo"
				    id="idProfilo" 
				    listKey="id" 
				    listValue="roleName"
				    list="profili"
				    emptyOption="false" 
				    cssStyle="width: 300px;"
				    cssClass="tableFields"
				    />
			</div>
			<div class="required" style="margin-top:10px;">
				<div style="float:left; width:100%;"><s:submit name="ricerca" id="ricerca" key="dpe.contact.form.ricerca" align="center" cssStyle="align:center; width:150px" /></div>
			</div>
		</fieldset>	
	</div>
</s:form>

