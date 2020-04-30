<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<s:set name="templateSuffix" value="'ftl'" />
<s:form id="EditUtenteForm" action="gestioneUtenti_saveUtente.action"
	namespace="/" method="POST" theme="igeriv" validate="false"
	onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 600px">
		<fieldset class="filterBolla" style="text-align: left;">
			<legend style="font-size: 100%">
				<s:property value="tableTitle" />
			</legend>
			<div class="tableFields">
				<table cellspacing="3">
					<tr>
						<td width="150px"><s:text name="dpe.nome" /></td>
						<td><s:textfield label="" name="utente.nomeUtente"
								id="utente.nomeUtente" cssStyle="width: 200px"
								cssClass="tableFields" /></td>
						<td><font color="red" size="1"><span
								id="err_utente.nomeUtente"><s:fielderror>
										<s:param>utente.nomeUtente</s:param>
									</s:fielderror></span></font></td>
					</tr>
					<tr>
						<td><s:text name="dpe.email" /></td>
						<td><s:textfield label="" name="utente.email"
								id="utente.email" cssStyle="width: 200px" cssClass="tableFields" /></td>
						<td><font color="red" size="1"><span
								id="err_utente.email"><s:fielderror>
										<s:param>utente.email</s:param>
									</s:fielderror></span></font></td>
					</tr>
					<tr>
						<td><s:text name="igeriv.profilo.utente" /></td>
						<td><s:select label=""
								name="utente.dlGruppoModuliVo.pk.codGruppo"
								id="utente.dlGruppoModuliVo.gruppoModuli" listKey="id"
								listValue="titolo" list="gruppiModuli" emptyOption="false"
								cssStyle="width: 200px" cssClass="tableFields" /></td>
						<td><font color="red" size="1"><span
								id="err_igeriv.profilo.utente">&nbsp;</span></font></td>
					</tr>
					<td><s:text name="igeriv.abilitato" /></td>
					<td><s:select label="" name="utente.abilitato"
							id="utente.abilitato"
							list="#{'1':getText('igeriv.si'),'0':getText('igeriv.no')}"
							emptyOption="false" cssStyle="width: 200px"
							cssClass="tableFields" /></td>
					<td><font color="red" size="1"><span
							id="err_utente.abilitato"><s:fielderror>
									<s:param>utente.abilitato</s:param>
								</s:fielderror></span></font></td>
					</tr>
					<s:if test="newUser eq false">
						<tr>
							<td colspan="2"><s:text
									name="dpe.invia.email.credenziali.rigenera.password" /></td>
							<td>&nbsp;&nbsp;<s:checkbox name="inviaEmail"
									id="inviaEmail" cssClass="tableFields" value="true" /></td>
						</tr>
					</s:if>
				</table>
			</div>
		</fieldset>
		<div style="width: 100%; margin-top: 50px;">
			<div
				style="text-align: center; width: 250px; margin-left: auto; margin-right: auto">
				<div style="float: left; width: 100px;">
					<input type="button" name="igeriv.memorizza" id="memorizza"
						value="<s:text name='igeriv.memorizza'/>" align="center"
						class="tableFields" style="text-align: center; width: 100px"
						onclick="javascript: setTimeout(function() {return (validateFieldsUtenteEdicola(true) && setFormAction('EditUtenteForm','gestioneUtenti_saveUtente.action', '', 'messageDiv'));},10);" />
				</div>
				<div style="float: left; width: 100px; margin-left: 20px;">
					<input type="button" name="cancella" id="cancella"
						value="<s:text name='dpe.contact.form.reset'/>" align="center"
						class="tableFields" style="text-align: center; width: 100px"
						onclick="javascript:doDelete();" />
				</div>
			</div>
		</div>
	</div>
	<s:hidden name="utente.codUtente" />
	<s:hidden name="utente.abbinamentoEdicolaDlVo.codDpeWebEdicola" />
	<s:hidden name="utente.abbinamentoEdicolaDlVo.codEdicolaDl" />
	<s:hidden name="utente.dlGruppoModuliVo.pk.codDl" />
	<s:hidden name="utente.changePassword" />
	<s:hidden name="newUser" />
</s:form>
<div id="one">
	</td>