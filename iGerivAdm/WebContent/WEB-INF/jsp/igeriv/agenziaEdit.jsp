<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<style>
		div#filter { height:0px;} 
		div#content1 { height:580px;} 
</style> 
<s:form id="AnagraficaAgenziaEditForm" method="POST" validate="false"  onsubmit="return (ray.ajax())">			
	<div id="mainDiv" style="width:100%;">
		<div id="mainDiv1" style="width:80%; height:520px; overflow-x:hidden; overflow-y:scroll; margin-left:auto; margin-right:auto; ">	
			<fieldset style="text-align:left; height:760px; width:88%;"><legend style="font-size:100%"><s:text name="igeriv.params.agenzia"/></legend>
			<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="dpe.login.cod.fieg.dl.code.short" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.codFiegDl" id="codFiegDl" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
		 	<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.1" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ragioneSocialeDlPrimaRiga" id="paramAgenzia1" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.2" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ragioneSocialeDlSecondaRiga" id="paramAgenzia2" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.3" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.indirizzoDlPrimaRiga" id="paramAgenzia3" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.4" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.indirizzoDlSecondaRiga" id="paramAgenzia4" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.5" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.localitaDlPrimaRiga" id="paramAgenzia5" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
		  	<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.6" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.localitaDlSecondaRiga" id="paramAgenzia6" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.7" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.siglaProvincia" id="paramAgenzia7" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.8" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.cap" id="paramAgenzia8" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.9" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.telefono" id="paramAgenzia9" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.10" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.fax" id="paramAgenzia10" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.11" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.email" id="paramAgenzia11" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.22" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.url" id="paramAgenzia22" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="margin-top:5px; width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.12" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ftpServerGestionaleAddress" id="paramAgenzia12" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.13" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ftpServerGestionaleUser" id="paramAgenzia13" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.14" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ftpServerGestionalePwd" id="paramAgenzia14" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.15" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.ftpServerGestionaleDir" id="paramAgenzia15" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.16" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.numMaxCpuResaDimeticata" id="paramAgenzia16" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.24" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.codGruppoModuliVo" id="paramAgenzia24" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.19" /></div>
					<div style="float:left; width:48%"><s:textfield name="agenzia.giornoSettimanaPermessaResaDimenticata" id="paramAgenzia19" cssClass="tableFields" cssStyle="width:300px"/></div>
				</div> 
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.20" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.hasButtonCopiaDifferenze" id="paramAgenzia20" cssClass="tableFields"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.21" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.hasResaAnticipata" id="paramAgenzia21" cssClass="tableFields"/></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.17" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.visualizzaSemaforoGiacenza" id="paramEdicola17" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.18" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.emailReplyToInstantMessages" id="paramEdicola18" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.23" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.returnReceiptTo" id="paramEdicola23" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.25" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.edicoleVedonoMessaggiDpe" id="paramEdicola25" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.26" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.gestioneAnagraficaRivenditaObbligatoria" id="paramEdicola26" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.27" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.visualizzaResoRiscontratoStatistica" id="paramEdicola27" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.28" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.permetteInserimentoRichiesteRifornimentoFuture" id="paramEdicola28" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.29" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.spuntaObbligatoriaBollaConsegna" id="paramEdicola29" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.30" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.suddivisioneQuotidianiPeriodiciReportVenduto" id="paramEdicola30" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.31" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.prenotazioniEvasioneQuantitaEvasaEmpty" id="paramEdicola31" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.32" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.venditeEsauritoControlloGiacenzaDL" id="paramEdicola32" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.33" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.hasPopupConfermaSuMemorizzaInviaBolle" id="paramEdicola33" cssClass="tableFields" /></div>
				</div>
				<div style="width:100%">
					<div style="float:left; width:50%"><s:text name="igeriv.params.agenzia.34" /></div>
					<div style="float:left; width:48%"><s:checkbox name="agenzia.hasMessaggioDocumentoDisponibile" id="paramEdicola34" cssClass="tableFields" /></div>
				</div>
			</fieldset>		
		</div>  
		<div style="width:100%">
			<div style="text-align:center;"><input type="button" value="<s:text name='igeriv.memorizza'/>" name="igeriv.memorizza" id="memorizza" style="width:100px; text-align:center" onclick="javascript: (setFormAction('AnagraficaAgenziaEditForm','agenzia_saveAgenzia.action', '', 'messageDiv'));"/></div>  
		</div>	
	</div>	
	<s:hidden name="codDpeWebDl" />
	<s:hidden name="isNew" value="true"/>
</s:form>
