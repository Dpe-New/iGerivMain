<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec"%>
<%@ taglib uri="http://www.dpe.it" prefix="dpe"%>
<script>	
	codPagina = '<s:text name="constants.COD_PAGINA_BOLLA_CONSEGNA"/>';
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
	var msgCopertina = "<s:text name='igeriv.messaggio.copertina'/>";
	var msgConfirmInviaRichiestaAssociazioneBarcode = '<s:text name="igeriv.confirm.invia.richiesta.associazione.barcode"/>';
	var msgNascondiMessaggiBolla = '<s:text name="igeriv.nascondi.msg.bolla"/>';
	var msgMostraMessaggiBolla = '<s:text name="igeriv.mostra.msg.bolla"/>';
	var msgRigheNonSpuntate = "<s:text name='igeriv.message.non.hai.spuntato.tutte.righe'/>";
	var msgBollaNonLetti = "<s:text name='igeriv.message.non.hai.letto.messaggi.bolla'/>";
	var msgBarcodeFittizio = "<s:text name='igeriv.message.codice.barre.non.compatibile'/>";
	var msgPubbNonDisponibile = "<s:text name='msg.pubblicazione.non.uscita.non.disponibile'/>";
	var confirmEsportareClientiDiTutteLeBolle = "<s:text name='igeriv.confirm.esportare.clienti.tutte.le.bolle.stessa.data'/>";
	var conditionItensBolla = <s:if test="%{#request.itensBolla != null && !#request.itensBolla.isEmpty()}">true</s:if><s:else>false</s:else>;
	var conditionDisableForm = <s:if test="%{disableForm}">true</s:if><s:else>false</s:else>;
	var conditionDlReadOnly = false;
	var conditionMessaggiBolla = <s:if test="%{#request.messaggiBolla != null && !#request.messaggiBolla.isEmpty()}">true</s:if><s:else>false</s:else>;
	var totaleBollaFormat = "<s:text name='#request.totaleBollaFormat'/>";
	var totaleFondoBollaFormat = "<s:text name='#request.totaleFondoBollaFormat'/>";
	var totaleBollaLordoFormat = "<s:text name='#request.totaleBollaLordoFormat'/>";
	var totaleFondoBollaLordoFormat = "<s:text name='#request.totaleFondoBollaLordoFormat'/>";
	var prenotazioniFisseNonEvase = "<s:text name='igeriv.vedi.prenotazioni.non.evase'/>"
	var msgConfirmAggiornareAssociazioneBarcode = '<s:text name="igeriv.confirm.aggiornamento.barcode"/>';
	var msgConfirmRichiestaAggiornareAssociazioneBarcode = '<s:text name="igeriv.confirm.richiesta.aggiornamento.barcode"/>';
	var hasPrenotazioniFisseNonEvase = <s:if test="hasPrenotazioniFisseNonEvase eq true">true</s:if><s:else>false</s:else>;
	var hasEdicoleAutorizzateAggiornaBarcode = <s:if test="authUser.hasEdicoleAutorizzateAggiornaBarcode eq true">true</s:if><s:else>false</s:else>;
	var hasSpuntaObbligatoriaBollaConsegna = <s:if test="authUser.spuntaObbligatoriaBollaConsegna eq true">true</s:if><s:else>false</s:else>;
	var hasPopupConfermaSuMemorizzaInviaBolle = <s:if test="authUser.hasPopupConfermaSuMemorizzaInviaBolle eq true">true</s:if><s:else>false</s:else>;
	var hasMessaggioDocumentoDisponibile = <s:if test="authUser.hasMessaggioDocumentoDisponibile eq true">true</s:if><s:else>false</s:else>;
	var msgConfermaSpunte = "<s:text name='igeriv.confirm.spunte'/>";
	var msgConfermaMancanzeEccedenze = "<s:text name='igeriv.confirm.mancanze.eccedenze'/>";
	var msgDocumentoDisponibile = "<s:text name='igeriv.msg.bolla.consegna.documento.disponibile'/>";
	var isMenta = <s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA">true</s:if><s:else>false</s:else>; 
	var viewImageByProfile_isDisabledPopupRifornimenti = <s:if test="%{#session['isProfileBollaConsegnaReadOnly'] eq true}">true</s:if><s:else>false</s:else>; 
	
	// add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili)
	var isEnabledExportXLS 		= <s:if test="%{#session['isEnabledExportXLS'] eq true}">false</s:if><s:else>true</s:else>; 
	var isEnabledPKInExportXLS 	= <s:if test="%{#session['isEnabledPKInExportXLS'] eq true}">false</s:if><s:else>true</s:else>;
	var isEnabledExportPDF 		= <s:if test="%{#session['isEnabledExportPDF'] eq true}">false</s:if><s:else>true</s:else>;
	var isEnabledPKInExportPDF 	= <s:if test="%{#session['isEnabledPKInExportPDF'] eq true}">false</s:if><s:else>true</s:else>;
		
	var msgLetti = <s:if test="msgBollaLetti eq true">true</s:if><s:else>false</s:else>;
	var smdUrl = "${smdUrl}";
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
<s:if test="#request.modalitaInforiv eq true">
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js-transient/bollaRivenditaInf-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
</s:if>
<s:else>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js-transient/bollaRivendita-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
</s:else>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/bollaRivenditaBase-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<s:if test="#request.modalitaInforiv eq true">
	<script>
		function checkboxChanged($checkField) {
			var isChecked = $checkField.attr("checked");
			var $nextTd = $checkField.parent("td").next("td");
			var $diffTextField = $nextTd.find("input:text").first();
			var $fSpunta = $nextTd.find("input:hidden[name^=spunta]").first();
			var $fModificato = $fSpunta.next("input:hidden[name^=modificato]");
			$fSpunta.val(isChecked ? 1 : 0);
			$fModificato.val("true");
			$diffTextField.focus();
		}
	</script>
</s:if>
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dijit/themes/claro/claro.css" />
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dojox/grid/resources/Grid.css" />
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dojox/grid/resources/claroGrid.css" />
