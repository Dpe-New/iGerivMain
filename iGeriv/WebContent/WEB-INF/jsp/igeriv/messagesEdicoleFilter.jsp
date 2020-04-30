<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 150px;
}
</style>
<s:form id="filterForm" action="messages_showMessagesEdicole.action"
	method="POST" theme="simple" onsubmit="return (ray.ajax())"
	validate="false">
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla" style="width: 620px; height: 120px;">
			<legend style="font-size: 100%">
				<s:property value="filterTitle" />
			</legend>
			<div>
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.data.messaggio" />
				</div>
				<div style="float: left; width: 400px; text-align: left">
					<s:text name="igeriv.da" />
					&nbsp;&nbsp;
					<s:textfield name="strDataMessaggioDa" id="strDataMessaggioDa"
						cssClass="tableFields" cssStyle="width:90px;" disabled="false" />
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />
					&nbsp;&nbsp;
					<s:textfield name="strDataMessaggioA" id="strDataMessaggioA"
						cssClass="tableFields" cssStyle="width:90px;" disabled="false" />
				</div>
			</div>
			<div style="align: center; text-align: center; margin-top: 20px;">
				<div
					style="float: left; width: 610px; align: center; text-align: center;">
					<input type="button" name="ricerca" id="ricerca"
						value="<s:text name="dpe.contact.form.ricerca"/>" align="center"
						style="align: center; width: 150px" onclick="doSubmit();" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
