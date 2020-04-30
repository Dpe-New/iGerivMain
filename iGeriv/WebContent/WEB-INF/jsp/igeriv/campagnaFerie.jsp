<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
div#filter {
	height: 80px;
}

div#footer {
	margin-top: 10px;
}
</style>
<s:if test="%{#request.stato == 0 }">
	<s:form id="comunicazioniForm" method="POST" theme="simple"
		validate="false">
		<div style="text-align: center">
			<fieldset class="filterBolla" style="width: 750px;">
				<div style="width: 100%; font-weight: bold;">
					<s:property value="titleComunicazioniFerie" />
				</div>

				<table id="anagraficaTable1" width="100%" cellspacing="3" border="1"
					align="center">
					<table id="anagraficaTable2" width="95%" cellspacing="3">
						<tr>
							<td style="width: 10%; text-align: right;"><font
								color="#000"><b>Rivendita: </b></font></td>
							<td style="width: 90%; text-align: left;"><font color="#000"><s:property
										value="anagraficaEdicola.ragioneSocialeEdicola" /></font></td>
						</tr>
						<tr>
							<td style="width: 10%; text-align: right;"><font
								color="#000"><b>Via: </b></font></td>
							<td style="width: 90%; text-align: left;"><font color="#000"><s:property
										value="anagraficaEdicola.indirizzoEdicola" /></font></td>
						</tr>
						<tr>
							<td style="width: 10%; text-align: right;"><font
								color="#000"><b>Comune: </b></font></td>
							<td style="width: 90%; text-align: left;"><font color="#000"><s:property
										value="anagraficaEdicola.localitaEdicola" /></font></td>
						</tr>
					</table>
				</table>
				<div style="width: 100%; font-weight: bold;">DICHIARO</div>
				<br />
				<table id="anagraficaTable3" width="100%" cellspacing="3">
					<tr>
						<td style="width: 100%; text-align: left;"><input
							type="radio" id="campagnaEdicola.flgaperto9227"
							name="campagnaEdicola.flgaperto9227" value="1" checked="checked">&nbsp;&nbsp;
							<font color="#000"><s:text
									name="campagna.label.edicola.noferie" /></font></td>
					</tr>
					<tr>
						<td style="width: 100%; text-align: left;"><input
							type="radio" id="campagnaEdicola.flgaperto9227"
							name="campagnaEdicola.flgaperto9227" value="0">&nbsp;&nbsp;
							<font color="#000"><s:text
									name="campagna.label.edicola.ferie" /></font></td>
					</tr>
				</table>
				<br />
				<table id="anagraficaTable4" width="100%" cellspacing="3">
					<tr>
						<td style="width: 10%; text-align: right;"><font color="#000"><b>Turno:
							</b></font></td>
						<td style="width: 90%; text-align: left;"><input type="radio"
							name="campagnaEdicola.turno9227" value="1"
							<s:if test="campagnaEdicola.turno9227 !=null && campagnaEdicola.turno9227 eq 1">checked</s:if>>&nbsp;&nbsp;<font
							color="#000"><s:text name="campagna.turno1" /></font> <input
							type="radio" name="campagnaEdicola.turno9227" value="2"
							<s:if test="campagnaEdicola.turno9227 !=null && campagnaEdicola.turno9227 eq 2">checked</s:if>>&nbsp;&nbsp;<font
							color="#000"><s:text name="campagna.turno2" /></font></td>
					</tr>
				</table>

				<table id="anagraficaTable5" width="100%" cellspacing="3">
					<tr>
						<td style="width: 10%; text-align: center;"><input
							type="checkBox" id="S1_CheckBox" name="S1_CheckBox" /></td>
						<td style="width: 40%; text-align: left;"><s:text
								name="igeriv.da" />&nbsp;&nbsp;<s:textfield
								name="S1_Op1DataDal" id="S1_Op1DataDal" cssClass="tableFields"
								cssStyle="width:90px;" disabled="false" />&nbsp;&nbsp; <s:text
								name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="S1_Op1DataAl"
								id="S1_Op1DataAl" cssClass="tableFields" cssStyle="width:90px;"
								disabled="false" /></td>
						<td style="width: 50%; text-align: left;">per compessivi
							giorni <s:textfield id="S1_TotGiorni" name="S1_TotGiorni"
								value="0" disabled="disabled" />
						</td>
					</tr>
					<tr>
						<td style="width: 100%; text-align: center;" colspan="3"><b>OPPURE</b></td>
					</tr>
					<tr>
						<td style="width: 10%; text-align: center;"><input
							type="checkBox" id="S2_CheckBox" name="S2_CheckBox" /></td>
						<td style="width: 40%; text-align: left;"><s:text
								name="igeriv.da" />&nbsp;&nbsp;<s:textfield
								name="S2_Op1DataDal" id="S2_Op1DataDal" cssClass="tableFields"
								cssStyle="width:90px;" disabled="false" />&nbsp;&nbsp; <s:text
								name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="S2_Op1DataAl"
								id="S2_Op1DataAl" cssClass="tableFields" cssStyle="width:90px;"
								disabled="false" /></td>
						<td style="width: 50%; text-align: left;">per compessivi
							giorni <s:textfield id="S2_TotGiorni" name="S2_TotGiorni"
								value="0" disabled="disabled" />
						</td>
					</tr>

				</table>
				<br /> <br />
				<table id="anagraficaTable6" width="100%" cellspacing="3"
					disabled="disabled">
					<tr>
						<td style="width: 100%; text-align: LEFT;" colspan="3"><b>DA
								COMPILARE SOLO SE SI EFFETTUA LA 3° SETTIMANA DI FERIE IN
								PERIODI DIVERSI DA QUELLI FISSATI ENTRO L'ANNO</b></td>
					</tr>
					<tr>
						<td style="width: 10%; text-align: center;"><input
							type="checkBox" id="S3_CheckBox" name="S3_CheckBox" /></td>
						<td style="width: 40%; text-align: left;"><s:text
								name="igeriv.da" />&nbsp;&nbsp;<s:textfield
								name="S3_Op1DataDal" id="S3_Op1DataDal" cssClass="tableFields"
								cssStyle="width:90px;" disabled="false" />&nbsp;&nbsp; <s:text
								name="igeriv.a" />&nbsp;&nbsp;<s:textfield name="S3_Op1DataAl"
								id="S3_Op1DataAl" cssClass="tableFields" cssStyle="width:90px;"
								disabled="false" /></td>
						<td style="width: 50%; text-align: left;">per compessivi
							giorni <s:textfield id="S3_TotGiorni" name="S3_TotGiorni"
								value="0" disabled="disabled" />
						</td>
					</tr>
				</table>
				<br /> <br />

			</fieldset>

			<div style="width: 100%; margin-top: 20px; text-align: center">
				<input type="button" value="<s:text name='igeriv.memorizza'/>"
					align="center" cssClass="tableFields"
					cssStyle="width:150px; text-align:center"
					onclick="return validateFormConfermaFerie();" />
			</div>
		</div>


	</s:form>
</s:if>
<s:elseif test="%{#request.stato == 1 }">
	<div style="text-align: center">
		<fieldset class="filterBolla" style="width: 750px;">
			<div style="width: 100%; font-weight: bold;">
				<s:property value="titleComunicazioniFerie" />
			</div>

			<table id="anagraficaTable1" width="100%" cellspacing="3" border="1"
				align="center">
				<table id="anagraficaTable2" width="95%" cellspacing="3">
					<tr>
						<td style="width: 10%; text-align: right;"><font color="#000"><b>Rivendita:
							</b></font></td>
						<td style="width: 90%; text-align: left;"><font color="#000"><s:property
									value="anagraficaEdicola.ragioneSocialeEdicola" /></font></td>
					</tr>
					<tr>
						<td style="width: 10%; text-align: right;"><font color="#000"><b>Via:
							</b></font></td>
						<td style="width: 90%; text-align: left;"><font color="#000"><s:property
									value="anagraficaEdicola.indirizzoEdicola" /></font></td>
					</tr>
					<tr>
						<td style="width: 10%; text-align: right;"><font color="#000"><b>Comune:
							</b></font></td>
						<td style="width: 90%; text-align: left;"><font color="#000"><s:property
									value="anagraficaEdicola.localitaEdicola" /></font></td>
					</tr>
				</table>
			</table>
			<div style="width: 100%; font-weight: bold;">DICHIARO</div>
			<br />
			<table id="anagraficaTable3" width="100%" cellspacing="3"
				disabled="disabled">
				<tr>
					<td style="width: 100%; text-align: left;"><input type="radio"
						name="campagnaEdicola.flgaperto9227" disabled="disabled" value="1"
						<s:if test="campagnaEdicola.flgaperto9227 eq 1">checked</s:if>>&nbsp;&nbsp;
						<font color="#000"><s:text
								name="campagna.label.edicola.noferie" /></font></td>
				</tr>
				<tr>
					<td style="width: 100%; text-align: left;"><input type="radio"
						name="campagnaEdicola.flgaperto9227" disabled="disabled" value="0"
						<s:if test="campagnaEdicola.flgaperto9227 eq 0">checked</s:if>>&nbsp;&nbsp;
						<font color="#000"><s:text
								name="campagna.label.edicola.ferie" /></font></td>
				</tr>
			</table>
			<br />
			<table id="anagraficaTable4" width="100%" cellspacing="3"
				disabled="disabled">
				<tr>
					<td style="width: 10%; text-align: right;"><font color="#000"><b>Turno:
						</b></font></td>
					<td style="width: 90%; text-align: left;"><input type="radio"
						name="campagnaEdicola.turno9227" disabled="disabled" value="1"
						<s:if test="campagnaEdicola.turno9227 !=null && campagnaEdicola.turno9227 eq 1">checked</s:if>>&nbsp;&nbsp;<font
						color="#000"><s:text name="campagna.turno1" /></font> <input
						type="radio" name="campagnaEdicola.turno9227" disabled="disabled"
						value="2"
						<s:if test="campagnaEdicola.turno9227 !=null && campagnaEdicola.turno9227 eq 2">checked</s:if>>&nbsp;&nbsp;<font
						color="#000"><s:text name="campagna.turno2" /></font></td>
				</tr>
			</table>

			<table id="anagraficaTable5" width="100%" cellspacing="3"
				disabled="disabled">
				<tr>
					<td style="width: 40%; text-align: left;"><s:text
							name="igeriv.da" />&nbsp;&nbsp;<s:textfield
							value="%{#request.dataOp1Dal}" cssClass="tableFields"
							cssStyle="width:90px;" disabled="true" />&nbsp;&nbsp; <s:text
							name="igeriv.a" />&nbsp;&nbsp;<s:textfield
							value="%{#request.dataOp1Al}" cssClass="tableFields"
							cssStyle="width:90px;" disabled="true" /></td>
					<td style="width: 50%; text-align: left;">per compessivi
						giorni <s:text name="totGiorniOp1" />
					</td>
				</tr>
				<tr>
					<td style="width: 40%; text-align: left;"><s:text
							name="igeriv.da" />&nbsp;&nbsp;<s:textfield
							value="%{#request.dataOp2Dal}" cssClass="tableFields"
							cssStyle="width:90px;" disabled="true" />&nbsp;&nbsp; <s:text
							name="igeriv.a" />&nbsp;&nbsp;<s:textfield
							value="%{#request.dataOp2Al}" cssClass="tableFields"
							cssStyle="width:90px;" disabled="true" /></td>
					<td style="width: 50%; text-align: left;">per compessivi
						giorni <s:text name="totGiorniOp2" />
					</td>
				</tr>
			</table>
			<br /> <br />
			<table id="anagraficaTable6" width="100%" cellspacing="3"
				disabled="disabled">
				<tr>
					<td style="width: 100%; text-align: center;" colspan="3"><b>FERIE
							CONFERMATE IN DATA &nbsp;&nbsp;<s:text name="dataConferma" />
					</b></td>
				</tr>
			</table>
			<br /> <br />

		</fieldset>

	</div>
</s:elseif>
<s:else>
	<div style="text-align: center">
		<fieldset class="filterBolla" style="width: 750px;">
			<div style="width: 100%; font-weight: bold;">
				<s:property value="titleComunicazioniFerie" />
			</div>

			<table id="anagraficaTable1" width="100%" cellspacing="3" border="1"
				align="center">
				<table id="anagraficaTable2" width="95%" cellspacing="3">
					<tr>
						<td style="width: 10%; text-align: right;"><font color="#000"><b>Rivendita:
							</b></font></td>
						<td style="width: 90%; text-align: left;"><font color="#000"><s:property
									value="anagraficaEdicola.ragioneSocialeEdicola" /></font></td>
					</tr>
					<tr>
						<td style="width: 10%; text-align: right;"><font color="#000"><b>Via:
							</b></font></td>
						<td style="width: 90%; text-align: left;"><font color="#000"><s:property
									value="anagraficaEdicola.indirizzoEdicola" /></font></td>
					</tr>
					<tr>
						<td style="width: 10%; text-align: right;"><font color="#000"><b>Comune:
							</b></font></td>
						<td style="width: 90%; text-align: left;"><font color="#000"><s:property
									value="anagraficaEdicola.localitaEdicola" /></font></td>
					</tr>
				</table>
			</table>

			<br /> <br />
			<table id="anagraficaTable6" width="100%" cellspacing="3"
				disabled="disabled">
				<tr>
					<td style="width: 100%; text-align: center;" colspan="3"><b>CAMPAGNA
							NON ATTIVA</b></td>
				</tr>
			</table>
			<br /> <br />

		</fieldset>

	</div>
</s:else>



