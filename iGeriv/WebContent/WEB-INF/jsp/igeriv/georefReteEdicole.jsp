<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 230px;
}

div#footer {
	margin-top: 10px;
}
</style>


<div style="text-align: center;">
	<s:form id="comunicazioniConfermaIndirizzoGeoref" method="POST"
		theme="simple" validate="false">
		<fieldset class="filterBolla"
			style="width: 80%; -moz-border-radius: 15px; border-radius: 15px; border: 2px solid #9ad0ef; padding: 10px;">
			<table id="anagraficaTable1" width="100%" cellspacing="3" border="0"
				align="center">
				<tr>
					<td style="width: 30%; text-align: right;"><font color="#000"><b>Rivendita:
						</b></font></td>
					<td style="width: 70%; text-align: left;"><font color="#000"><div
								id="ragSocRivendita" /></font></td>
				</tr>
				<tr>
					<td style="width: 30%; text-align: right;"><font color="#000"><b>Indirizzo:
						</b></font></td>
					<td style="width: 70%; text-align: left;"><font color="#000"><s:property
								value="anagraficaEdicola.indirizzoLocalita" /></font></td>
				</tr>
				<tr>
					<td style="width: 30%; text-align: right;"><font color="#000"><b>Distanza
								Km : </b></font></td>
					<td style="width: 70%; text-align: left;"><font color="#000"><div
								id="amount"
								style="color: #f6931f; padding: 0px 0px; margin: 0px 0 0;"></div></font></td>
				</tr>
				<tr>
					<td style="width: 30%; text-align: right;"></td>
					<td style="width: 70%; text-align: left;"><div
							id="slider-range-max"></div></td>
				</tr>

				<tr>
					<td colspan="2"><input type="button" id="button_ricerca"
						value="Ricerca"
						onclick="javascript: setTimeout('initialize();', 100);"> <input
						type="button" id="button_conferma"
						value="Conferma Gruppo Rete Edicole"></td>
				</tr>
				<tr>
					<td colspan="2"><div id="resultSearch" style="color: #f6931f;"></td>
				</tr>
			</table>
		</fieldset>

		<input type="hidden" id="indirizzo" name="indirizzo" value="" />
		<input type="hidden" id="lat" name="lat" value="" />
		<input type="hidden" id="lng" name="lng" value="" />
		<input type="hidden" id="km" name="km" value="" />
		<input type="hidden" id="arrayCrivwGeoref" name="arrayCrivwGeoref"
			value="" />


	</s:form>
</div>
<div style="height: 2px"></div>
<div style="text-align: center;">
	<fieldset class="filterBolla"
		style="-moz-border-radius: 15px; border-radius: 15px; border: 2px solid #9ad0ef;">
		<!-- width:80%;padding: 10px; -->
		<table id="legenda" width="100%" cellspacing="1" border="0"
			align="center">
			<tr>
				<td style="width: 10%; text-align: right;"><img
					src="https://maps.google.com/mapfiles/kml/shapes/schools_maps.png" /></td>
				<td style="width: 20%; text-align: left;"><font color="#000">Mia
						posizione</font></td>
				<td style="width: 10%; text-align: right;"><img
					src="https://maps.gstatic.com/mapfiles/ms2/micons/red-dot.png" /></td>
				<td style="width: 20%; text-align: left;"><font color="#000">Rivendite
						vicine</font></td>
				<td style="width: 10%; text-align: right;"><img
					src="https://maps.gstatic.com/mapfiles/ms2/micons/green-dot.png" /></td>
				<td style="width: 20%; text-align: left;"><font color="#000">Rivendite
						del mio gruppo</font></td>
			</tr>
		</table>
	</fieldset>
</div>


