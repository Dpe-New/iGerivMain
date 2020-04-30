<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 200px;
}
</style>
<s:form id="filterForm" action="%{actionName}" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px; margin-top: -10px">
			<font color='red'><s:actionerror /></font>
		</div>
		<fieldset class="filterBolla" style="height: 170px; width: 780px;">
			<legend style="font-size: 100%">${tableTitle}</legend>
			<div id="ricercaCliente">
				<div class="required">
					<div style="float: left; width: 135px;">
						<s:text name="dpe.nome" />
					</div>
					<div style="float: left; width: 230px;">
						<s:textfield label="" name="nome" id="nome" cssClass="tableFields"
							cssStyle="width:240px" />
					</div>
					<div style="float: left; width: 135px;">
						<s:text name="dpe.cognome" />
					</div>
					<div style="float: left; width: 230px;">
						<s:textfield label="" name="cognome" id="cognome"
							cssClass="tableFields" cssStyle="width:240px" />
					</div>
				</div>
				<div class="required">
					<div style="float: left; width: 135px;">
						<s:text name="dpe.codice.fiscale" />
					</div>
					<div style="float: left; width: 230px;">
						<s:textfield label="" name="codiceFiscale" id="codiceFiscale"
							cssClass="tableFields" cssStyle="width:240px" />
					</div>
					<div style="float: left; width: 135px;">
						<s:text name="dpe.piva" />
					</div>
					<div style="float: left; width: 230px;">
						<s:textfield label="" name="piva" id="piva" cssClass="tableFields"
							cssStyle="width:240px" />
					</div>
				</div>
				<div class="required">
					<div style="float: left; width: 135px;">
						<s:text name="igeriv.data.competenza.ec" />
					</div>
					<div style="float: left; width: 230px;">
						<s:text name="igeriv.da" />
						&nbsp;&nbsp;
						<s:textfield name="strDataCompetenzaDa" id="strDataCompetenzaDa"
							cssClass="tableFields" cssStyle="width:70px;" disabled="false" />
						&nbsp;&nbsp;
						<s:text name="igeriv.a" />
						&nbsp;&nbsp;
						<s:textfield name="strDataCompetenzaA" id="strDataCompetenzaA"
							cssClass="tableFields" cssStyle="width:70px;" disabled="false" />
					</div>
					<div style="float: left; width: 135px;">
						<s:text name="igeriv.richiesta.rifornimenti.quantita.stato" />
					</div>
					<div style="float: left; width: 240px;">
						<s:select name="contiDaPagare" id="contiDaPagare" listKey="key"
							listValue="value" list="listContiDaPagare" cssClass="tableFields"
							cssStyle="font-size:10px; width:240px" />
					</div>
				</div>
				<div class="required">
					<div style="float: left; width: 750px; margin-top: 10px">
						<s:submit name="ricerca" id="ricerca"
							key="dpe.contact.form.ricerca" align="center"
							cssStyle="align:center; width:80px" />
						&nbsp;&nbsp;
					</div>
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
