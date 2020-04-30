<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
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
	var msgResaContoDepositoNonPermessa = '<s:text name="pubblicazione.in.conto.deposito"/>';
	var confirmResaContoDeposito = "<s:text name='confirm.resa.pubblicazione.in.conto.deposito'/>";
	var confirmResaContoDeposito1 = "<s:text name='confirm.resa.pubblicazione.in.conto.deposito.1'/>";
	var alertResaContoDeposito = "<s:text name='alert.resa.in.conto.deposito'/>";
	var conditionItensBolla = <s:if test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">true</s:if><s:else>false</s:else>;
	var conditionDisableForm = <s:if test="%{disableForm}">true</s:if><s:else>false</s:else>;
	var conditionDlReadOnly = false;
	var conditionDisableFormAndNotReadonly = <s:if test="%{disableForm == false}">true</s:if><s:else>false</s:else>;
	var msgVisualizzaStatistiche = "<s:text name='igeriv.view.statistica.pubblicazioni'/>";
	var showDialogResoSuperioreGiacenza = false;
	var showDialogConfirmNumeroResaRespinto = false;
	var showDialogConfirmResaInContoDeposito = false;
	var showAlertNonPermettiResaInContoDeposito = false;
	var objToFocus = null;
	var $tit = '';
	var $num = '';
	var $ncd = null;
	var bypassValidateGiacenza = false;
	var bypassValidatePubblicazioneRespinta = false;
	var bypassValidatePubblicazioneContoDeposito = false;
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/bollaResa-min_<s:text name="igeriv.version.timestamp"/>.js"></script>