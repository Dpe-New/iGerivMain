<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<div class="eXtremeTable">
	<div style="width:100%; text-align:center" class="title">
		<s:property value="tableTitle"/>
	</div>
</div>
<s:form id="RichiestaRifornimentoForm" action="richiesteProdotti_saveRichiestaRifornimentoProdotto.action" method="POST" theme="simple" validate="false" onsubmit="return (ray.ajax())">		
	<div id="mainDiv" style="width:100%; margin-top:20px; text-align:center;">		
			<div class="tableFields" style="text-align:center; margin-left:auto; margin-right:auto; width:420px; font-size:12px; font-weight:bold;">
				<div style="width:450px; height:30px">
					<div style="float:left; width:150px">
						<s:text name="igeriv.richiesta.rifornimenti.prodotti.quantita.richiesta"/>
					</div>
					<div style="float:left; width:250px">	
						<s:textfield name="richiestaRifornimentoProdotto.quatitaRichiesta" id="richiestaRifornimentoProdotto.quatitaRichiesta" cssClass="extremeTableFields" cssStyle="width:200px;" disabled="%{prenotazioneDisabled}"/>
					</div>
				</div>		
				<div style="width:420px; height:30px">
					<div style="float:left; width:150px">
						<s:text name="igeriv.data.ultima.richiesta"/>
					</div>
					<div style="float:left; width:250px">
						<s:textfield name="richiestaRifornimentoProdotto.dataRichiesta" id="richiestaRifornimentoProdotto.dataRichiesta" cssClass="extremeTableFields" cssStyle="width:200px;" disabled="true">
							<s:param name="value">
								<s:date name="richiestaRifornimentoProdotto.dataRichiesta" format="dd/MM/yyyy HH:mm:ss"/>
							</s:param>
						</s:textfield>
					</div>
				</div>
				<div style="width:420px; height:30px">
					<div style="float:left; width:150px">
						<s:text name="igeriv.ultima.qta.richiesta"/>
					</div>
					<div style="float:left; width:250px">
						<s:textfield name="ultimaQuantitaRichiesta" id="ultimaQuantitaRichiesta" cssClass="extremeTableFields" cssStyle="width:200px;" disabled="true" />
					</div>
				</div>
				<div style="width:420px; height:30px">
					<div style="float:left; width:150px">
						<s:text name="igeriv.richiesta.rifornimenti.risposta.dl"/>
					</div>
					<div style="float:left; width:250px">
						<s:textfield name="richiestaRifornimentoProdotto.rispostaDl" id="richiestaRifornimentoProdotto.rispostaDl" cssClass="extremeTableFields" cssStyle="width:200px;" disabled="true"/>
					</div>
				</div>
				<div style="width:420px; height:30px">
					<div style="float:left; width:150px">
						<s:text name="igeriv.richiesta.rifornimenti.quantita.note.dl"/>
					</div>
					<div style="float:left; width:250px">
						<s:textfield name="richiestaRifornimentoProdotto.note" id="richiestaRifornimentoProdotto.note" cssClass="extremeTableFields" cssStyle="width:200px;" disabled="true"/>
					</div>
				</div>
				<div style="width:420px; height:30px">
					<div style="float:left; width:150px">
						<s:text name="igeriv.data.ultima.trasmissione.dl"/>
					</div>
					<div style="float:left; width:250px">
						<s:textfield name="richiestaRifornimentoProdotto.dataInvioRichiestaDl" id="richiestaRifornimentoProdotto.dataInvioRichiestaDl" cssClass="extremeTableFields" cssStyle="width:200px;" disabled="true">
							<s:param name="value">
								<s:date name="richiestaRifornimentoProdotto.dataInvioRichiestaDl" format="dd/MM/yyyy"/>
							</s:param>
						</s:textfield>	
					</div>
				</div>
			</div>			
	</div>	
	<s:hidden name="codRichiestaRifornimento" id="codRichiestaRifornimento"/>	
	<s:hidden name="richiestaRifornimentoProdotto.stato" id="richiestaRifornimentoProdotto.stato"/>				
	<s:hidden name="codice"/>	
</s:form>
<div style="width:100%; margin-top:50px;">
	<div style="text-align:center; margin-left:auto; margin-right:auto; width:200px; height:50px; margin-top:10px;">
		<input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" class="tableFields" style="width:100px; text-align:center" onclick="javascript: setFormAction('RichiestaRifornimentoForm','richiesteProdotti_saveRichiestaRifornimentoProdotto.action', '', 'messageDiv');"/>
	</div>
</div>
