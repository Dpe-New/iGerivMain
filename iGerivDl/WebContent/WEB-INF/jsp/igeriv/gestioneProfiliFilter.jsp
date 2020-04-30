<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<s:form id="filterForm" action="gestioneProfili_showModuliProfilo.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">	
	<div style="width:100%; text-align:center">
		<fieldset class="filterBolla" style="height:120px;"><legend style="font-size:100%"><s:property value="filterTitle" />&nbsp;<span style="font-size:9px">(<s:text name="igeriv.click.dx.per.menu"/>)</span></legend>
			<div class="required">
				<s:select label="" 
				    name="idProfilo"
				    id="idProfilo" 
				    listKey="id" 
				    listValue="titolo"
				    list="profili"
				    emptyOption="false" 
				    cssStyle="width: 300px; font-size:14px;"
				    />
			</div>
			<div class="required" style="margin-top:10px">		
				<div style="float:left; width:450px; margin-top:10px">
					<input type="button" name="ricerca" id="ricerca" value="<s:text name="dpe.contact.form.mostra.menu.profilo"/>" align="center" style="align:center; width:150px" onclick='populateSelects($("#idProfilo").val());'/>
				</div>
			</div>
		</fieldset>	
	</div>
</s:form>
