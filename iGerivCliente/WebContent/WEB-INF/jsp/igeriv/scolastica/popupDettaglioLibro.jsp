<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="popupDettaglioLibroForm"
	action="gestioneClienti_saveCliente.action" namespace="/" method="POST"
	theme="igeriv" validate="false" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%">
		<fieldset class="filterBolla" style="text-align: left; width: 100%">
			<legend style="font-size: 100%">${tableTitle}</legend>
			<div class="tableFields">

				<table id="dettagliolibrotable" width="810px" cellspacing="3">

					<tr>
						<td><s:text name="dpe.dettaglio.libro.sku" /></td>
						<td><s:textfield label="" name="SKU" id="SKU"
								value="%{#request.dettaglioLibroScolastico.SKU}"
								cssStyle="width: 400px" cssClass="tableFields" maxlength="25"
								disabled="false" /></td>
						<td rowspan="11"><img id="imgCaptcha"
							src="<s:property value='%{#request.dettaglioLibroScolastico.URL}'/>" /></td>
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.barcode" /></td>
						<td><s:textfield label="" name="BARCODE" id="BARCODE"
								value="%{#request.dettaglioLibroScolastico.BARCODE}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.titolo" /></td>
						<td><s:textfield label="" name="TITOLO" id="TITOLO"
								value="%{#request.dettaglioLibroScolastico.TITOLO}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.autori" /></td>
						<td><s:textfield label="" name="AUTORI" id="AUTORI"
								value="%{#request.dettaglioLibroScolastico.AUTORI}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.editore" /></td>
						<td><s:textfield label="" name="EDITORE" id="EDITORE"
								value="%{#request.dettaglioLibroScolastico.EDITORE}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.prezzo" /></td>
						<td><s:textfield label="" name="PREZZO" id="PREZZO"
								value="%{#request.dettaglioLibroScolastico.PREZZO}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.aliquota" /></td>
						<td><s:textfield label="" name="ALIQUOTA" id="ALIQUOTA"
								value="%{#request.dettaglioLibroScolastico.ALIQUOTA}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.disponibile" /></td>
						<td><s:textfield label="" name="DISPONIBILE" id="DISPONIBILE"
								value="%{#request.dettaglioLibroScolastico.DISPONIBILE}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.tomi" /></td>
						<td><s:textfield label="" name="TOMI" id="TOMI"
								value="%{#request.dettaglioLibroScolastico.TOMI}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.annopubb" /></td>
						<td><s:textfield label="" name="ANNAPUBBL" id="ANNAPUBBL"
								value="%{#request.dettaglioLibroScolastico.ANNAPUBBL}"
								cssStyle="width: 400px" cssClass="tableFields" disabled="false" />
					</tr>
					<tr>
						<td><s:text name="dpe.dettaglio.libro.abstract" /></td>
						<td><s:textarea name="ABSTRACT" id="ABSTRACT"
								value="%{#request.dettaglioLibroScolastico.ABSTRACT}"
								disabled="false" cols="25" rows="5" cssStyle="width:400px"
								maxlength="3999" /></td>
					</tr>

				</table>
			</div>
		</fieldset>
		<div style="width: 100%; margin-top: 0px;">
			<div style="float: left; width: 400px; margin-left: 320px">
				<div style="float: left; width: 100px; margin-top: 0px;">
					<input type="button" name="igeriv.aggiungi.carrello"
						id="aggiungicarrello"
						value="<s:text name='igeriv.aggiungi.carrello'/>" align="center"
						class="tableFields" style="text-align: center; width: 200px"
						onclick="javascript: addCart(function() {clienteEdicolaSuccessCallback();})" />
				</div>
			</div>
		</div>
	</div>
	<s:hidden name="idNumeroOrdine" id="idNumeroOrdine" value="%{#request.numeroOrdine}" />
	<s:hidden name="guid" id="guid" value="%{#request.ricParams.guid}" />
	<s:hidden name="sku" id="sku" value="%{#request.ricParams.sku}" />

</s:form>
<div id="one">
	</td>