<%@ page contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
div#filter {
	height: 220px;
}
</style>
<s:form id="filterForm" action="%{actionName}" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div style="width: 100%; text-align: center">
		<div class="required" id="errorDiv"
			style="width: 100%; align: center; text-align: center; height: 20px; margin-top: -10px">
			<font color='red'><s:actionerror /></font>
		</div>
		<s:if
			test="actionName != null && actionName.contains('gestioneClienti_showClientiViewPrenotazioni')">
			<fieldset class="filterBolla" style="height: 230px; width: 750px">
				<legend style="font-size: 100%">${tableTitle}</legend>
				<div class="required">
					<div style="float: left; width: 280px;">
						<s:text name="igeriv.tipo.ricerca" />
					</div>
					<div style="float: left; width: 400px; text-align: left">
						<input type="radio" name="tipoRicerca" id="tipoRicerca1" value='0'
							<s:if test="tipoRicerca == null || tipoRicerca eq 0">checked</s:if>>&nbsp;&nbsp;
						<s:text name="igeriv.per.cliente" />
						&nbsp; <input type="radio" name="tipoRicerca" id="tipoRicerca2"
							value='1' <s:if test="1 eq tipoRicerca">checked</s:if>>&nbsp;&nbsp;
						<s:text name="igeriv.per.pubblicazione" />
						&nbsp;
					</div>
				</div>
		</s:if>
		<s:else>
			<fieldset class="filterBolla" style="height: 180px; width: 750px">
				<legend style="font-size: 100%">${tableTitle}</legend>
		</s:else>
		<div id="ricercaCliente">
			<div class="required">
				<div style="float: left; width: 300px;">
					<s:text name="dpe.nome" />
				</div>
				<div style="float: left; width: 200px;">
					<s:textfield label="" name="nome" id="nome" />
				</div>
				<div style="float: right; width: 200px;">
					<font color="red" size="1"><span id="err_nome"><s:fielderror>
								<s:param>nome</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 300px;">
					<s:text name="dpe.cognome" />
				</div>
				<div style="float: left; width: 200px;">
					<s:textfield label="" name="cognome" id="cognome" />
				</div>
				<div style="float: right; width: 200px;">
					<font color="red" size="1"><span id="err_cognome"><s:fielderror>
								<s:param>cognome</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 300px;">
					<s:text name="dpe.codice.fiscale" />
				</div>
				<div style="float: left; width: 200px;">
					<s:textfield label="" name="codiceFiscale" id="codiceFiscale" />
				</div>
				<div style="float: right; width: 200px;">
					<font color="red" size="1"><span id="err_codiceFiscale"><s:fielderror>
								<s:param>codiceFiscale</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 300px;">
					<s:text name="dpe.piva" />
				</div>
				<div style="float: left; width: 200px;">
					<s:textfield label="" name="piva" id="piva" />
				</div>
				<div style="float: right; width: 200px;">
					<font color="red" size="1"><span id="err_piva"><s:fielderror>
								<s:param>piva</s:param>
							</s:fielderror></span></font>
				</div>
			</div>
			<div class="required">
				<div style="float: left; width: 750px; margin-top: 10px">
					<s:submit name="ricerca" id="ricerca"
						key="dpe.contact.form.ricerca" align="center"
						cssStyle="align:center; width:80px" />
					&nbsp;&nbsp;
					<s:if
						test="actionName != null && actionName.equals('gestioneClienti_showClienti.action')">
						<input type="button" id="butNuovo"
							value="<s:text name='plg.inserisci.nuovo'/>" align="center"
							cssStyle="align:center; width:80px" />
					</s:if>
				</div>
			</div>
		</div>
		<s:if
			test="actionName != null && actionName.contains('gestioneClienti_showClientiViewPrenotazioni')">
			<div id="ricercaPubblicazione">
				<div class="required"
					style="width: 750px; align: left; text-align: center">
					<div style="float: left; width: 100px;">
						<s:text name="igeriv.titolo" />
					</div>
					<div style="float: left; width: 250px;">
						<s:textfield label="" name="titolo" id="titolo"
							cssClass="required" cssStyle="width:250px;" />
					</div>
					<div style="float: left; width: 100px;">
						<s:text name="igeriv.sottotitolo" />
					</div>
					<div style="float: left; width: 250px;">
						<s:textfield label="" name="sottotitolo" id="sottotitolo"
							cssClass="required" cssStyle="width:250px;" />
					</div>
				</div>
				<div class="required"
					style="width: 750px; align: left; text-align: center">
					<div style="float: left; width: 100px;">
						<s:text name="igeriv.argomento" />
					</div>
					<div style="float: left; width: 250px;">
						<select name="argomento" id="argomento" class="required"
							style="width: 250px;">
							<option value=""></option>
							<s:iterator
								value="%{#application['listArgomento'][authUser.codFiegDl]}"
								status="status" var="arg">
								<option value="<s:property value='pk'/>"
									<s:if test="pk.toString() eq argomento">selected</s:if>><s:property
										value='dataBollaFormat' /><s:property value='descrizione' /></option>
							</s:iterator>
						</select>
					</div>
					<div style="float: left; width: 100px;">
						<s:text name="igeriv.periodicita" />
					</div>
					<div style="float: left; width: 250px;">
						<select name="periodicita" id="periodicita" class="required"
							style="width: 250px;">
							<option value=""></option>
							<s:iterator value="%{#application['listPeriodicita']}"
								status="status">
								<option value="<s:property value='pk'/>"
									<s:if test="pk.toString() eq periodicita">selected</s:if>><s:property
										value='dataBollaFormat' /><s:property value='descrizione' /></option>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="required"
					style="width: 750px; align: left; text-align: center">
					<div style="float: left; width: 100px;">
						<s:text name="igeriv.prezzo" />
					</div>
					<div style="float: left; width: 250px;">
						<s:textfield label="" name="prezzo" id="prezzo"
							cssClass="required" cssStyle="width:250px;" />
					</div>
					<div style="float: left; width: 100px;">
						<s:text name="igeriv.codice.pubblicazione.short" />
					</div>
					<div style="float: left; width: 250px;">
						<s:textfield label="" name="codPubblicazione"
							id="codPubblicazione" cssClass="required" cssStyle="width:250px;" />
					</div>
				</div>
				<div class="required"
					style="width: 750px; align: left; text-align: center;">
					<div style="float: left; width: 100px;">
						<s:text name="igeriv.codice.barre.short" />
					</div>
					<div style="float: left; width: 250px;">
						<s:textfield label="" name="codBarre" id="codBarre"
							cssClass="required" cssStyle="width:250px;" />
					</div>
					<div style="float: left; width: 200px; text-align: left">
						&nbsp;&nbsp;&nbsp;
						<s:text name="igeriv.includi.prenotazioni.fisse" />
					</div>
					<div style="float: left; width: 150px;">
						<s:checkbox name="conPrenotazioniFisse" id="conPrenotazioniFisse"
							cssClass="tableFields" />
					</div>
				</div>
				<div class="required">
					<div style="float: left; width: 750px; margin-top: 10px">
						<s:submit name="ricerca" id="ricerca"
							key="dpe.contact.form.ricerca" align="center"
							cssStyle="align:center; width:80px" />
						&nbsp;&nbsp;
						<s:if
							test="actionName != null && actionName.contains('gestioneClienti_showClienti')">
							<input type="button" id="butNuovo"
								value="<s:text name='plg.inserisci.nuovo'/>" align="center"
								cssStyle="align:center; width:80px" />
						</s:if>
					</div>
				</div>
			</div>
		</s:if>
		</fieldset>
	</div>
</s:form>
