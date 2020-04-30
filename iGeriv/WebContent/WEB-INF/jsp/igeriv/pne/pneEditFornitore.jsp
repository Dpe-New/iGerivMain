<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="FornitoriEditForm"
	action="pneFornitori_saveFornitore.action" namespace="/" method="POST"
	validate="false" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">
				<s:text name="igeriv.inserisci.aggiorna.fornitore" />
			</legend>
			<div class="tableFields">
				<table width="850px" cellspacing="3">
					<tr>
						<td><s:text name="dpe.rag.sociale" />&nbsp;<font
							color="#000"><em>*</em></font></td>
						<td><s:textfield label="" name="fornitore.nome"
								id="fornitore.nome" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
						<td><s:text name="dpe.rag.sociale.2" />&nbsp;<font
							color="#000"><em>*</em></font></td>
						<td><s:textfield label="" name="fornitore.cognome"
								id="fornitore.cognome" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
					</tr>
					<tr>
						<td><s:text name="dpe.indirizzo" /></td>
						<td><s:select label=""
								name="fornitore.tipoLocalita.tipoLocalita" id="tipoLocalita"
								listKey="tipoLocalita" listValue="descrizione"
								list="tipiLocalita" emptyOption="false" cssStyle="width: 60px"
								cssClass="tableFields" />
							<s:textfield label="" name="fornitore.indirizzo"
								id="fornitore.indirizzo" cssStyle="width: 135px"
								cssClass="tableFields" /></td>
						<td><s:text name="igeriv.numero.civico" /></td>
						<td><s:textfield label="" name="fornitore.numeroCivico"
								id="fornitore.numeroCivico" cssStyle="width: 60px"
								cssClass="tableFields" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text
								name="igeriv.estensione" />&nbsp;&nbsp;&nbsp;<s:textfield
								label="" name="fornitore.estensione" id="fornitore.estensione"
								cssStyle="width: 60px" cssClass="tableFields" /></td>
					</tr>
					<tr>
						<td><s:text name="dpe.localita" /></em></td>
						<td><s:textfield id="autocomplete"
								name="fornitore.localita.descrizione" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
						<td><s:text name="dpe.provincia" /></td>
						<td style="float: left; width: 100px;"><s:select label=""
								name="fornitore.provincia.codProvincia" id="fornitore.provincia"
								listKey="codProvincia" listValue="descrizione" list="province"
								emptyOption="true" cssStyle="width: 200px"
								cssClass="tableFields" />
						</td>
					</tr>
					<tr>
						<td><s:text name="dpe.nazione" /></td>
						<td><s:textfield label="" name="descPaese" id="descPaese"
								cssStyle="width: 200px" cssClass="tableFields" value="ITALIA"
								disabled="true" /></td>
						<td><s:text name="dpe.cap" /></td>
						<td><s:textfield label="" name="fornitore.cap"
								id="fornitore.cap" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
					</tr>

					<tr>
						<td><s:text name="dpe.codice.fiscale" /></td>
						<td><s:textfield label="" name="fornitore.codiceFiscale"
								id="fornitore.codiceFiscale" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
						<td><s:text name="dpe.piva" /></td>
						<td><s:textfield label="" name="fornitore.piva"
								id="fornitore.piva" cssStyle="width: 200px"
								cssClass="tableFields" />
					</tr>
					<tr>
						<td><s:text name="dpe.telefono" /></td>
						<td><s:textfield label="" name="fornitore.telefono"
								id="fornitore.telefono" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
						<td><s:text name="dpe.fax" /></td>
						<td><s:textfield label="" name="fornitore.fax"
								id="fornitore.fax" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
					</tr>
					<tr>
						<td><s:text name="dpe.email" /></td>
						<td><s:textfield label="" name="fornitore.email"
								id="fornitore.email" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
						<td><s:text name="dpe.url" /></td>
						<td><s:textfield label="" name="fornitore.url"
								id="fornitore.url" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
					</tr>
				</table>
			</div>
		</fieldset>
		<div style="width: 100%; margin-top: 20px;">
			<s:if test='isNew != true'>
				<div
					style="text-align: center; width: 280px; margin-left: auto; margin-right: auto;">
					<div style="float: left; text-align: center; width: 100px;">
						<input type="button" name="igeriv.memorizza" id="memorizza"
							value="<s:text name='igeriv.memorizza'/>" align="center"
							class="tableFields" style="text-align: center; width: 100px"
							onclick="javascript: return (validateFieldsFornitore(true) && setFormAction('FornitoriEditForm','pneFornitori_saveFornitore.action', '', 'messageDiv', false, '', function() {if (typeof(afterSuccessSaveFromBollaCarico) === 'function') {afterSuccessSaveFromBollaCarico($('#fornitore\\.nome').val());} else {$('#ricerca').trigger('click');}}));" />
					</div>
					<div style="float: left; text-align: center; width: 150px;">
						<input type="button" name="cancella" id="cancella"
							value="<s:text name='dpe.contact.form.elimina.fornitore'/>"
							align="center" class="tableFields"
							style="text-align: center; width: 150px"
							onclick="javascript: doDelete();" />
					</div>
				</div>
			</s:if>
			<s:else>
				<div
					style="text-align: center; width: 100px; margin-left: auto; margin-right: auto;">
					<div style="float: left; text-align: center; width: 100px;">
						<input type="button" name="igeriv.memorizza" id="memorizza"
							value="<s:text name='igeriv.memorizza'/>" align="center"
							class="tableFields" style="text-align: center; width: 100px"
							onclick="javascript: return (validateFieldsFornitore(true) && setFormAction('FornitoriEditForm','pneFornitori_saveFornitore.action', '', 'messageDiv', false, '', function() {if (typeof(afterSuccessSaveFromBollaCarico) === 'function') {afterSuccessSaveFromBollaCarico($('#fornitore\\.nome').val());} else {$('#ricerca').trigger('click');}}));" />
					</div>
				</div>
			</s:else>
		</div>
	</div>
	<s:hidden name="fornitore.pk.codFornitore" />
	<s:hidden id="codLocalita" name="fornitore.localita.codLocalita" />
	<s:hidden id="codPaese" name="fornitore.paese.codPaese" value="1" />
</s:form>