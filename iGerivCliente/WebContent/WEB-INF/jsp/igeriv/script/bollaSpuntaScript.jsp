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
	var msgCambioBolla = "<s:text name='igeriv.msg.cambio.bolla'/>";
	var msgDifferenzeNonConfermate = "<s:text name='igeriv.differenze.non.confermate'/>";
	var msgCampoNumerico = "<s:text name='msg.formato.campo.numerico'/>";
	var msgConfermaDifferenze = "<s:text name='conferma.differenze'/>";
	var msgBollaNonModificabile = "<s:text name='impossibile.modificare.bolla.gia.trasmessa'/>";
	var msgConfirmCopiaDifferenze = "<s:text name='msg.confirm.copia.differenze'/>";
	var pubNonInBollaMsg = '<s:text name="msg.pubblicazione.non.in.bolla"/>';
	var conditionItensBolla = <s:if test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">true</s:if><s:else>false</s:else>;
	var conditionDisableForm = <s:if test="%{disableForm}">true</s:if><s:else>false</s:else>;
	var conditionDlReadOnly = false;
	var conditionMessaggiBolla = <s:if test="%{#request.messaggiBolla != null && !#request.messaggiBolla.isEmpty()}">true</s:if><s:else>false</s:else>;
	var totaleBollaFormat = "<s:text name='#request.totaleBollaFormat'/>";
	var totaleFondoBollaFormat = "<s:text name='#request.totaleFondoBollaFormat'/>";
	var totaleBollaLordoFormat = "<s:text name='#request.totaleBollaLordoFormat'/>";
	var totaleFondoBollaLordoFormat = "<s:text name='#request.totaleFondoBollaLordoFormat'/>";
	var msgCpu = "<s:text name='igeriv.cpu'/>";
	var msgSottotitolo = "<s:text name='igeriv.sottotitolo'/>";
	var msgTitolo = "<s:text name='igeriv.titolo'/>";
	var msgNumero = "<s:text name='igeriv.numero'/>";
	var msgPrezzoLordo = "<s:text name='igeriv.prezzo.lordo'/>";
	var msgCopieBolla = "<s:text name='igeriv.copie.bolla'/>";
	var msgCopieLette = "<s:text name='igeriv.copie.lette'/>";
	var msgDifferenze = "<s:text name='igeriv.differenze'/>";
	var attenzioneMsg = '<s:text name="msg.alert.attenzione"/>';
	var modalita = 0;
	var MODALITA_DEFAULT = 0;
	var MODALITA_RICERCA = 1;
	var $lastTr;
</script>	
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/bollaSpunta-min_<s:text name="igeriv.version.timestamp"/>.js"></script>