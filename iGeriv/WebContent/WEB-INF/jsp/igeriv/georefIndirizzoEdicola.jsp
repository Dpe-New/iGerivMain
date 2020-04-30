<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 105px;
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
					<td style="width: 10%; text-align: right;"><font color="#000"><b>Rivendita:
						</b></font></td>
					<td style="width: 90%; text-align: left;"><font color="#000"><div
								id="ragSocRivendita" /></font></td>
				</tr>
				<tr>
					<td style="width: 10%; text-align: right;"><font color="#000"><b>Indirizzo:
						</b></font></td>
					<td style="width: 90%; text-align: left;"><font color="#000"><input
							id="address" type="text" size="100" value=""></font></td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" id="button_ricerca"
						value="Ricerca"
						onclick="javascript: setTimeout('initialize();', 100);">
						<input type="button" id="button_conferma"
						value="Conferma Posizione"></td>
				</tr>

			</table>
		</fieldset>

		<input type="hidden" id="indirizzo" name="indirizzo" value="" />
		<input type="hidden" id="lat" name="lat" value="" />
		<input type="hidden" id="lng" name="lng" value="" />
	</s:form>
</div>



