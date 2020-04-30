<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script>	 
	var mapSubmitsVals = new Object();
	var lastFieldFocusId = "";
	var dataTipoBolla = "<s:property value='dataTipoBolla'/>";
	var msgDataTipoBolla = "<s:property value='dataTipoBolla'/>";
	var msgBollaGiaInviata = "<s:text name='msg.bolla.gia.inviata.dl'/>";
	var msgConfirmSaveBolla = "<s:text name='msg.confirm.save.bolla'/>";
	var msgInserisciModificaResaFuoriVoce = "<s:text name='plg.inserisci.modifica.resa.fuori.voce'/>";
	var attenzioneMsg = "<s:text name='msg.alert.attenzione'/>";
	var msgResoSuperioreGiacenza = "<s:text name='igeriv.message.reso.superiore.giacenza'/>";
	var msgNoteRivendita = "<s:text name='igeriv.note.rivendita'/>";
	var msgNoteRivenditaPerPubblicazioneNumero = "<s:text name='igeriv.note.rivendita.per.pubblicazione.numero'/>";
	var msgBollaTrasmessa = "<s:text name='igeriv.trasmessa'/>";
	var msgBollaInTrasmissione = "<s:text name='igeriv.in.trasmissione'/>";
	var msgBollaNonTrasmessa = "<s:text name='igeriv.non.trasmessa'/>";
	var msgConfirmNumeroResaRespinto = "<s:text name='confirm.numero.resa.respinto'/>";
	var contoDepositoIniziali = "<s:text name='igeriv.conto.deposito.iniziali'/>";
	var msgPubblicazionePresenteNelleSuccessiveBolleResa = "<s:text name='pubblicazione.presente.nelle.successive.bolle.resa'/>";
	var msgResaContoDepositoNonPermessa = '<s:text name="pubblicazione.in.conto.deposito"/>';
	var confirmResaContoDeposito = "<s:text name='confirm.resa.pubblicazione.in.conto.deposito'/>";
	var confirmResaContoDeposito1 = "<s:text name='confirm.resa.pubblicazione.in.conto.deposito.1'/>";
	var alertResaContoDeposito = "<s:text name='alert.resa.in.conto.deposito'/>";
	var conditionItensBolla = <s:if test="%{authUser.dlInforiv eq false && (#request.itensBolla == null || #request.itensBolla.isEmpty())}">false</s:if><s:else>true</s:else>;
	var conditionDisableForm = true;
	var conditionDlReadOnly = false;
	var conditionDisableFormAndNotReadonly = false;
	var msgVisualizzaStatistiche = "<s:text name='igeriv.view.statistica.pubblicazioni'/>";
	var showDialogResoSuperioreGiacenza = false;
	var showDialogConfirmNumeroResaRespinto = false;
	var showDialogConfirmResaInContoDeposito = false;
	var showAlertNonPermettiResaInContoDeposito = false;
	var showDialogPubblicazionePresenteNelleSuccessiveBolleResa = false;
	var objToFocus = null;
	var $tit = '';
	var $num = '';
	var $ncd = null;
	var bypassValidateGiacenza = false;
	var bypassValidatePubblicazioneRespinta = false;
	var bypassValidatePubblicazioneContoDeposito = false;
	
	var isMenta = <s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA">true</s:if><s:else>false</s:else>; 
	var viewImageByProfile_isDisabledPopupRifornimenti = <s:if test="%{#session['isProfileBollaResaReadOnly'] eq true}">true</s:if><s:else>false</s:else>; 
	var isCDL = <s:if test="authUser.codFiegDl eq constants.COD_FIEG_CDL">true</s:if><s:else>false</s:else>; 
	// add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili)
	var isEnabledExportXLS 		= <s:if test="%{#session['isEnabledExportXLS'] eq true}">false</s:if><s:else>true</s:else>; 
	var isEnabledPKInExportXLS 	= <s:if test="%{#session['isEnabledPKInExportXLS'] eq true}">false</s:if><s:else>true</s:else>;
	var isEnabledExportPDF 		= <s:if test="%{#session['isEnabledExportPDF'] eq true}">false</s:if><s:else>true</s:else>;
	var isEnabledPKInExportPDF 	= <s:if test="%{#session['isEnabledPKInExportPDF'] eq true}">false</s:if><s:else>true</s:else>;
	
	
	
	<s:if test="hasActionErrors()">
		var msg = '';
		<s:iterator value="actionErrors">
			msg += '<s:property escape="false" />';
		</s:iterator>
		$.alerts.dialogClass = "style_1";
		jAlert(msg, attenzioneMsg.toUpperCase(), function() {
			$.alerts.dialogClass = null;
		});
	</s:if>
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/bollaResa-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
