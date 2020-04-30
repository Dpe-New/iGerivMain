<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<script>	
	var dataTipoBolla = "<s:property value='dataTipoBolla'/>";
	var msgNoteRivendita = "<s:text name='igeriv.note.rivendita'/>";
	var msgNotePubblicazione = "<s:text name='igeriv.inserisci.note.pubblicazione'/>";
	var msgBollaGiaInviata = "<s:text name='msg.bolla.gia.inviata.dl'/>";
	var msgNonValorizzare = "<s:text name='igeriv.non.valorizzare'/>";
	var msgContoDeposito = "<s:text name='igeriv.conto.deposito'/>";
	var msgPrezzoVariato = "<s:text name='igeriv.prezzo.variato'/>";
	var msgDaRespingereInResa = "<s:text name='igeriv.copia.respinta.in.resa'/>";
	var msgConfirmSaveBolla = "<s:text name='msg.confirm.save.bolla'/>";
	var msgDistribuitoInferioreDirreferenza = "<s:text name='igeriv.message.distribuito.inferiore.differenza'/>";
	var msgBollaTrasmessa = "<s:text name='igeriv.trasmessa'/>";
	var msgBollaInTrasmissione = "<s:text name='igeriv.in.trasmissione'/>";
	var msgBollaNonTrasmessa = "<s:text name='igeriv.non.trasmessa'/>";
	var msgAliquotaInteger = "<s:text name='dpe.validation.msg.error.aliquota.iva.integer'/>";
	var msgVisualizzaStatistiche = "<s:text name='igeriv.view.statistica.pubblicazioni'/>";
	var msgDataPresuntoAddebito = "<s:text name='igeriv.data.presunto.addebito'/>";
	var msgFatturatoInContoDepositoDiversoDaVenduto = "<s:text name='igeriv.fatturata.cd.diverso.veduto'/>";
	var msgPubblicazioneCDInforeteNonInBolla = "<s:text name='igeriv.conto.deposito.inforete.diversa.bolla'/>";
	var msgPubblicazioneCDInBollaNonInforete = "<s:text name='igeriv.conto.deposito.bolla.diversa.inforete'/>";
	var msgScontoDiversoInforete = "<s:text name='igeriv.sconto.inforete.diverso'/>";
	var msgNessunOrdineClienteInBolla = "<s:text name='igeriv.nessun.ordine.cliente.in.bolla'/>";
	var confirmEsportareClientiDiTutteLeBolle = "<s:text name='igeriv.confirm.esportare.clienti.tutte.le.bolle.stessa.data'/>";
	var conditionItensBolla = <s:if test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">true</s:if><s:else>false</s:else>;
	var conditionDisableForm = <s:if test="%{disableForm}">true</s:if><s:else>false</s:else>;
	var conditionDlReadOnly = false;
	var conditionMessaggiBolla = <s:if test="%{#request.messaggiBolla != null && !#request.messaggiBolla.isEmpty()}">true</s:if><s:else>false</s:else>;
	var totaleBollaFormat = "<s:text name='#request.totaleBollaFormat'/>";
	var totaleFondoBollaFormat = "<s:text name='#request.totaleFondoBollaFormat'/>";
	var totaleBollaLordoFormat = "<s:text name='#request.totaleBollaLordoFormat'/>";
	var totaleFondoBollaLordoFormat = "<s:text name='#request.totaleFondoBollaLordoFormat'/>";
</script>	
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/bollaRivendita-min_<s:text name="igeriv.version.timestamp"/>.js"></script>