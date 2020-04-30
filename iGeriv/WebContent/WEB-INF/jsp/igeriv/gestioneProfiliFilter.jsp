<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:form id="filterForm" action="%{actionName}" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla" style="height: 120px;">
			<legend style="font-size: 100%">
				<s:property value="filterTitle" />
			</legend>
			<div class="required">
				<s:select label="" name="profilo.id" id="profilo" listKey="id"
					listValue="roleName" list="%{#session['profili']}"
					emptyOption="false" cssStyle="width: 200px" cssClass="tableFields" />
			</div>
			<div class="required" style="margin-top: 10px">
				<div style="float: left; width: 450px; margin-top: 10px">
					<s:submit name="ricerca" id="ricerca" key="dpe.contact.form.mostra"
						align="center" cssStyle="align:center; width:80px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
