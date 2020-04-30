<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	top: 0px;
}
</style>
<s:form id="filterForm" action="emailInviati_showEmailInviati.action"
	method="POST" theme="simple" onsubmit="return (ray.ajax())"
	validate="false">
	<div style="width: 100%; text-align: center">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px;">
			<font color='red'><s:actionerror /></font>
		</div>
		<fieldset class="filterBolla" style="width: 550px; height: 120px;">
			<legend style="font-size: 100%">
				<s:property value="filterTitle" />
			</legend>
			<div class="required">
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.data.messaggio" />
				</div>
				<div style="float: left; width: 300px; text-align: left">
					<s:text name="igeriv.da" />
					&nbsp;&nbsp;
					<s:textfield name="dataDaStr" id="strDataMessaggioDa"
						cssStyle="width:80px;" disabled="false" />
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />
					&nbsp;&nbsp;
					<s:textfield name="dataAStr" id="strDataMessaggioA"
						cssStyle="width:80px;" disabled="false" />
				</div>
			</div>
			<div class="required">
				<div style="width: 550px; margin-top: 40px;">
					<s:submit name="ricerca" id="ricerca"
						key="dpe.contact.form.ricerca" align="center"
						cssStyle="align:center; width:80px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
