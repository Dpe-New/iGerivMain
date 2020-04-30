<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<s:form id="filterForm" action="fatture_showFatture.action"
	method="POST" theme="simple" onsubmit="return (ray.ajax())"
	validate="false">
	<div style="width: 100%; text-align: center">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 30px;">
			<font color='red'><s:actionerror /></font>
		</div>
		<fieldset class="filterBolla" style="width: 700px; height: 145px;">
			<legend style="font-size: 100%">
				<s:property value="filterTitle" />
			</legend>
			<div class="required">
				<div style="float: left; width: 200px;">
					<s:text name="igeriv.data.fattura" />
				</div>
				<div style="float: left; width: 400px; text-align: left">
					<s:text name="igeriv.da" />
					&nbsp;&nbsp;
					<s:textfield name="dataDaStr" id="dataDaStr" cssClass="tableFields"
						cssStyle="width:90px;" disabled="false" />
					&nbsp;&nbsp;
					<s:text name="igeriv.a" />
					&nbsp;&nbsp;
					<s:textfield name="dataAStr" id="dataAStr" cssClass="tableFields"
						cssStyle="width:90px;" disabled="false" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 100px;">
					<s:text name="dpe.nome" />
				</div>
				<div style="float: left; width: 200px;">
					<s:textfield label="" name="nome" id="nome" cssClass="tableFields"
						cssStyle="width:210px" />
				</div>
				<div style="float: left; width: 100px;">
					<s:text name="dpe.cognome" />
				</div>
				<div style="float: left; width: 200px;">
					<s:textfield label="" name="cognome" id="cognome"
						cssClass="tableFields" cssStyle="width:210px" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 100px;">
					<s:text name="dpe.codice.fiscale" />
				</div>
				<div style="float: left; width: 200px;">
					<s:textfield label="" name="codiceFiscale" id="codiceFiscale"
						cssClass="tableFields" cssStyle="width:210px" />
				</div>
				<div style="float: left; width: 100px;">
					<s:text name="dpe.piva" />
				</div>
				<div style="float: left; width: 200px;">
					<s:textfield label="" name="piva" id="piva" cssClass="tableFields"
						cssStyle="width:210px" />
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 620px; margin-top: 10px">
					<s:submit name="ricerca" id="ricerca"
						key="dpe.contact.form.ricerca" align="center"
						cssStyle="align:center; width:80px" />
				</div>
			</div>
		</fieldset>
	</div>
</s:form>
