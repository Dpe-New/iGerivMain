<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 200px;
}
</style>
<s:form id="filterForm" action="sentMessagesClienti_showMessages.action"
	method="POST" theme="simple" onsubmit="return (ray.ajax())"
	validate="false">
	<div style="width: 100%; text-align: center">
		<fieldset class="filterBolla" style="width: 750px; height: 200px;">
			<legend style="font-size: 100%">
				<s:property value="filterTitle" />
			</legend>
			<div style="float: left; width: 100%; height: 40px;">
				<div style="float: left; width: 330px;">
					<s:text name="igeriv.data.messaggio" />
				</div>
				<div style="float: left; width: 380px; text-align: left">
					<s:text name="igeriv.da" />
					&nbsp;&nbsp;
					<s:textfield name="dataDaStr" id="strDataMessaggioDa"
						cssClass="tableFields" cssStyle="width:90px;" disabled="false" />
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />
					&nbsp;&nbsp;
					<s:textfield name="dataAStr" id="strDataMessaggioA"
						cssClass="tableFields" cssStyle="width:90px;" disabled="false" />
				</div>
			</div>
			<div style="float: left; width: 100%; height: 40px;">
				<div style="float: left; width: 125px;">
					<s:text name="dpe.nome" />
				</div>
				<div style="float: left; width: 230px;">
					<s:textfield label="" name="nome" id="nome" cssClass="tableFields"
						cssStyle="width:200px" />
				</div>
				<div style="float: left; width: 125px;">
					<s:text name="dpe.cognome" />
				</div>
				<div style="float: left; width: 230px;">
					<s:textfield label="" name="cognome" id="cognome"
						cssClass="tableFields" cssStyle="width:200px" />
				</div>
			</div>
			<div style="float: left; width: 100%; height: 40px;">
				<div style="float: left; width: 125px;">
					<s:text name="dpe.codice.fiscale" />
				</div>
				<div style="float: left; width: 230px;">
					<s:textfield label="" name="codiceFiscale" id="codiceFiscale"
						cssClass="tableFields" cssStyle="width:200px" />
				</div>
				<div style="float: left; width: 125px;">
					<s:text name="dpe.piva" />
				</div>
				<div style="float: left; width: 230px;">
					<s:textfield label="" name="piva" id="piva" cssClass="tableFields"
						cssStyle="width:200px" />
				</div>
			</div>
			<div style="align: center; text-align: center; margin-top: 20px;">
				<div
					style="float: left; width: 730px; align: center; text-align: center;">
					<input type="button" name="ricerca" id="ricerca"
						value="<s:text name="dpe.contact.form.ricerca"/>" align="center"
						style="align: center; width: 150px" onclick="doSubmit();" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
