<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div class="eXtremeTable">
	<div style="width: 100%; text-align: center" class="title">
		<s:text name="igeriv.bolla.variazioni" />
		&nbsp;
		<s:text name="igeriv.settimana" />
	</div>
</div>
<s:form id="RichiestaVariazioniSettimanaForm"
	action="sonoInoltreUscite_showVariazioniSettimana.action" method="POST"
	theme="simple" validate="false" onsubmit="return (ray.ajax())">
	<div id="mainDiv" style="width: 100%; margin-top: 50px;">
		<table style="width: 100%;">
			<tr>
				<td style="width: 30%;" />
				<td class="tableFields" style="width: 15%; text-align: center;"><s:text
						name="igeriv.prenotazione" /></td>
				<td class="tableFields" style="width: 25%; text-align: center;"><s:text
						name="igeriv.motivo" /></td>
				<td class="tableFields" style="width: 15%; text-align: center;"><s:text
						name="igeriv.data.richiesta" /></td>
				<td class="tableFields" style="width: 15%; text-align: center;"><s:text
						name="igeriv.data.ultima.trasmissione.dl" /></td>
			</tr>
			<s:iterator value="%{#request.prenotazioni}" var="v" status="s">
				<tr style="height: 30px;">
					<td style="text-align: left;"><s:text
							name="#v.pubblicazione.titolo" /></td>
					<td><s:textfield value="%{#v.quantitaRichiesta}"
							id="quantitaRichiesta%{#s.index}"
							name="quantitaRichiestaMap['%{#v.pubblicazione.pk.codicePubblicazione}']"
							cssClass="extremeTableFields onlyNumeric" cssStyle="width:50px;"
							disabled="%{prenotazioneDisabled}" /></td>
					<td><s:textfield value="%{#v.motivoRichiesta}"
							id="motivoRichiesta%{#s.index}"
							name="motivoRichiestaMap['%{#v.pubblicazione.pk.codicePubblicazione}']"
							cssClass="extremeTableFields" cssStyle="width:150px;"
							disabled="%{prenotazioneDisabled}" /></td>
					<td><s:textfield id="dataRichiesta%{#s.index}"
							name="dataRichiesta%{#s.index}" cssClass="extremeTableFields"
							cssStyle="width:80px;" disabled="true">
							<s:param name="value">
								<s:date name="#v.pk.dataRichiesta" format="dd/MM/yyyy" />
							</s:param>
						</s:textfield></td>
					<td><s:textfield id="dataUltimaTrasmissioneDl%{#s.index}"
							name="dataUltimaTrasmissioneDl%{#s.index}"
							cssClass="extremeTableFields" cssStyle="width:80px;"
							disabled="true">
							<s:param name="value">
								<s:date name="#v.dataUltimaTrasmissioneDl" format="dd/MM/yyyy" />
							</s:param>
						</s:textfield></td>
				</tr>
			</s:iterator>
		</table>
	</div>
	<div style="width: 100%; margin-top: 50px;">
		<input type="button" value="<s:text name='igeriv.memorizza'/>"
			name="igeriv.memorizza" id="memorizza" class="tableFields"
			style="width: 100px; text-align: center"
			onclick="javascript: setFormAction('RichiestaVariazioniSettimanaForm','sonoInoltreUscite_saveVariazioniSettimana.action', '', 'messageDiv');" />
	</div>
</s:form>
