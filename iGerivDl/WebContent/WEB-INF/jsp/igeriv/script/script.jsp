<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/combined-min-stable_<s:text name="igeriv.version.timestamp"/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/combined-min_<s:text name="igeriv.version.timestamp"/>.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/jquery.cookiebar_<s:text name="igeriv.version.timestamp"/>.js"></script>

<s:include value="/WEB-INF/jsp/igeriv/common/dojo_include.jsp"></s:include>
<script type="text/javascript">
	dojo.require("dojo.io.iframe");
	var attivaMemorizzazioneAutomatica = <s:if test="%{#session.paramEdicola14 == null}">true</s:if><s:else><s:property value="%{#session.paramEdicola14.paramValue}"/></s:else>;
	var msgDisabledMenuStarter = "<s:text name='igeriv.msg.disabled.menu.starter'/>";
	var spuntaBollaConsegna = "<s:text name='igeriv.spunta.bolla.consegna'/>"; 
	var userType = "<s:text name='authUser.tipoUtente'/>";
	var hasProfiloStarter = "<s:text name='#session.hasProfiloStarter'/>";
	var richiestaProva = "<s:text name='authUser.richiestaProva'/>";
	var userOfflineMsg = '<s:text name="gp.verificare.la.connessione"/>'.replace('{0}','<s:text name='#session["appName"]'/>');
	var errorMessage = "<s:text name='gp.errore'/>"; 
	var errorMessageDelete = "<s:text name='gp.errore.delete'/>";
	var okMessage = "<s:text name='gp.dati.memorizzati'/>";
	var bollaInTrasmissione = "<s:text name='gp.bolla.in.trasmissione'/>";
	var okMessageDelete = "<s:text name='gp.dati.memorizzati.delete'/>";
	var wrongDataType = "<s:text name='dpe.validation.msg.wrong.datatype'/>";
	var nessunaPubblicazione = "<s:text name='igeriv.nessuna.publicazione'/>";
	var chiudiLabel = "<s:text name='igeriv.chiudi'/>";
	var browserNotSupported = "<s:text name='msg.browser.not.supported'/>";
	var browserVersionNotSupported = "<s:text name='msg.browser.version.not.supported'/>";
	var cookiesDisabled = "<s:text name='msg.cookies.disabled'/>";
	var numeroNonValido = "<s:text name='igeriv.numero.non.valido'/>";
	var autoSpuntaMessage = "<s:text name='igeriv.message.spuntare.tutte.righe'/>";
	var alertTimeout = "<s:text name='igeriv.alert.timeout'/>";
	var memorizzaConfirm = "<s:text name='igeriv.memorizza.confirm'/>";
	var memorizzaInviaConfirm = "<s:text name='igeriv.memorizza.invia.confirm'/>";
	var sessionScadutaRedirectLoginMsg = "<s:text name='sessione.scaduta.redirect.login.msg'/>";
	var sessionScadutaAlert = "<s:text name='sessione.scaduta.alert'/>";
	var attenzioneMsg = "<s:text name='msg.alert.attenzione'/>";
	var infoMsg = "<s:text name='msg.alert.info'/>";
	var differenzaMoltoGrandeConfirm = "<s:text name='msg.confirm.differenza.molto.grande'/>";
	var confirmResaInseritaMoltoGrande = "<s:text name='igeriv.confirm.qta.resa.inserita.molto.grande'/>";
	var codiceProdotto = "<s:text name='igeriv.codice.prodotto'/>";
	var descrizione = "<s:text name='igeriv.descrizione'/>";
	var barcode = "<s:text name='igeriv.codice.barre'/>";
	var campoObbligatorio = "<s:text name='error.campo.x.obbligatorio'/>";
	var campoAlmenoDeveContenereAlmenoXCaratteri = "<s:text name='error.campo.deve.contenere.x.caratteri'/>";
	var prezzo = "<s:text name='igeriv.prezzo'/>";
	var prezzoInvalido = "<s:text name='msg.prezzo.invalido'/>";
	var nomeFileInvalido = "<s:text name='msg.nome.file.invalido'/>";
	var dataFormatoInvalido1 = "<s:text name='msg.formato.data.invalido.1'/>";
	var msgBarcodeAssociatoAltriProdotti = "<s:text name='msg.prodotto.barcode.utilizzato.altri.prodotti'/>";
	var privacyMessageEdicola = '<s:text name="igeriv.msg.sottoscrivi.privacy.edicola"/>';
	var privacyMessageLettore = '<s:text name="igeriv.msg.sottoscrivi.privacy.lettore"/>';
	var email = '<s:text name="dpe.contact.form.email"/>';
	var msgAccettoPrivacy = '<s:text name="igeriv.message.accetto.condizioni.privacy"/>';
	var msgInvalidEmail = '<s:text name="dpe.validation.msg.invalid.email"/>';
	var msgInvioEmailVerifica = "<s:text name='msg.avviso.invio.email.verifica'/>";
	var msgErroreInvioRichiesta = "<s:text name='msg.errore.invio.richiesta'/>";
	var msgEmailNonInserito = '<s:text name="igeriv.msg.email.non.inserito"/>';
	var msgEmailNonInseritoCliente = '<s:text name="igeriv.msg.email.non.inserito.cliente"/>';
	var msgPrivacyNonAccetata = '<s:text name="igeriv.msg.privacy.non.accettata"/>';
	var msgPrivacyNonAccetataCliente = '<s:text name="igeriv.msg.privacy.non.accettata.cliente"/>';
	var msgNuoviMessaggi = '<s:text name="igeriv.nuovi.messaggi"/>';
	var msgVisualizzaMsgImportantiDl = '<s:text name="igeriv.visualizza.messaggi.importanti.dl"/>';
	var msgPromoInvitaUnCollega = '<s:text name="igeriv.campagna.invita.un.collega"/>';
	var msgCliccaPerSaperneDiPiu = '<s:text name="igeriv.clicca.per.saperne.di.piu"/>';
	var helpArgumentsTitle = '<s:text name="igeriv.argomenti.aiuto"/>';
	var msgNovita = '<s:text name="igeriv.messaggi.novita"/>';
	var msgNuoviOrdiniClienti = '<s:text name="igeriv.nuovi.ordini.clienti"/>';
	var msgAttivaPeriodoProva = '<s:text name="igeriv.attiva.periodo.prova"/>';
	var msgAttivazionePeridoProvaEseguita = "<s:text name='igeriv.attivazione.periodo.prova.eseguita'/>";
	var attivaPeriodoProvaConfirm = '<s:text name="igeriv.attiva.periodo.prova.confirm"/>';
	var giorniProvaPerStarter = '<s:property value="giorniProvaPerIGerivStarter"/>';
	var msgNovitaSistema = '<s:text name="igeriv.messaggi.novita.sistema"/>';
	var msgNotificheOrdiniClienti = '<s:text name="igeriv.arrivati.nuovi.ordini.clienti"/>';
	var msgHelp = '<s:text name="help"/>';
	var msgCategoria = '<s:text name="igeriv.categoria"/>';
	var msgSottoCategoria = '<s:text name="igeriv.sotto.categoria"/>';
	var ragioneSocialeLabel = '<s:text name="dpe.rag.sociale"/>';
	var nomeLabel = '<s:text name="dpe.nome"/>';
	var dtNascLabel = '<s:text name="dpe.nato.a"/>';
	var giornoLabel = '<s:text name="dpe.gg"/>';
	var meseLabel = '<s:text name="dpe.mm"/>';
	var annoLabel = '<s:text name="dpe.aaaa"/>';
	var emailLabel = '<s:text name="dpe.email"/>';
	var numCivicoMsg = '<s:text name="msg.formato.campo.numerico"/>';
	var numCivicoLabel = '<s:text name="igeriv.numero" />';
	var localitaLabel = '<s:text name="dpe.localita" />';
	var provinciaLabel = '<s:text name="dpe.provincia" />';
	var capLabel = '<s:text name="dpe.cap" />';
	var indirizzoLabel = '<s:text name="dpe.indirizzo" />';
	var tipoEdicolaLabel = '<s:text name="dpe.tipo.edicola" />';
	var telefonoLabel = '<s:text name="dpe.telefono" />';
	var cellulareLabel = '<s:text name="dpe.contact.form.cell" />';
	var nomeLabel = '<s:text name="dpe.contact.form.name"/>';
	var cognomeLabel = '<s:text name="dpe.cognome"/>'
	var msgEmailValidato = '<s:text name="msg.email.validato"/>';
	var msgEmailValidatoCliente = '<s:text name="msg.email.validato.cliente"/>';
	var msgAvviso = '<s:text name="msg.avviso"/>'; 
	var actionName = '<s:text name="actionName"/>';
	var msgCambioDl = '<s:text name="msg.cambio.dl"/>';
	var msgNoteFisse = '<s:text name="msg.note.fisse"/>';
	var testoPrivacyEdicolaStarter1 = '<s:text name="msg.privacy.edicola.starter.1"/>';
	var testoPrivacyEdicolaStarter2 = '<s:text name="msg.privacy.edicola.starter.2"/>';
	var codTipoImmagineMiniatura = <s:text name="constants.COD_TIPO_IMMAGINE_MINIATURA"/>;
	var codTipoImmagineMiniaturaEdicola = <s:text name="constants.COD_TIPO_IMMAGINE_MINIATURA_EDICOLA"/>;
	var codTipoImmagineMiniaturaProdottiEdicola = <s:text name="constants.COD_TIPO_IMMAGINE_MINIATURA_PNE_EDICOLA"/>;
	var CDL_CODE = 226;
	var codDl = "<s:text name='#session["codFiegDl"]'/>";
	var timeout = "<s:text name='#session["timeout"]'/>";
	var networkDetectionInterval = "<s:text name='#session["networkDetectionIntervalMills"]'/>";
	var autoSaveBolleInterval = "<s:text name='#session["autoSaveBolleIntervalMills"]'/>";
	var highPriorityMessagesCheckInterval = "<s:text name='#session["highPriorityMessagesCheckInterval"]'/>";
	var hasMessaggiPrioritaMedia = <s:if test='#context["struts.actionMapping"].namespace == "/" && authUser.tipoUtente == "1" && messaggioNonLettoDto != null && messaggioNonLettoDto.priorita == 1'>true;</s:if><s:else>false;</s:else>
	var hasMessaggiPrioritaAlta = <s:if test='#context["struts.actionMapping"].namespace == "/" && authUser.tipoUtente == "1" && messaggioNonLettoDto != null && messaggioNonLettoDto.priorita == 2'>true;</s:if><s:else>false;</s:else>
	var hasArgomentiAiuto = <s:if test="help != null && !help.equals('')">true;</s:if><s:else>false;</s:else>
	var hasMessaggiDpe = <s:if test='#context["struts.actionMapping"].namespace == "/" && authUser.tipoUtente == "1" && hasMessaggiDpe == true'>true;</s:if><s:else>false;</s:else>
	var hasNotificheOrdiniClienti = <s:if test='hasNotificheOrdiniClienti == true'>true;</s:if><s:else>false;</s:else>
	var checkedEmail = <s:if test="checkedEmail eq true">true;</s:if><s:else>false;</s:else>
	var abilitataCorrezioneBarcode = "<s:text name='#session["abilitataCorrezioneBarcode"]'/>";
	var messaggioNonLetto = '<s:text name="messaggioNonLettoDto.pk"/>';
	var barcodeFinalizza = '<s:text name="constants.BARCODE_FINALIZZA"/>';
	var msgIGerivCard = '<s:text name="plg.vendita.igeriv.card"/>';	
	var msgInsertBarcodeIGerivCard = '<s:text name="label.pubblication_input_module_listener.IGeriv_Card_Insert_Barcode"/>';
	var informazioneMsg = '<s:text name="informazione"/>';
	var generalRpcActionUrl = '<s:url action="GlobalRpc.action"/>';
	var helpList = '';

	//Associazione barcode
	var msg_associazioneNuoviBarcodeConfirm 	= "<s:text name='igeriv.messaggio.associazione.nuovi.barcode.confirm'/>"; 			// Confirm
	var msg_associazioneNuoviBarcodeSaveNotOk	= "<s:text name='igeriv.messaggio.associazione.nuovi.barcode.save.not.ok'/>"; 		// Salvataggio non effettuato
	var msg_associazioneNuoviBarcodeSaveOk		= "<s:text name='igeriv.messaggio.associazione.nuovi.barcode.save.ok'/>"; 			// Salvataggio effettuato

	
	
	<s:if test="help != null && !help.equals('')">
		helpList = '<div class="helpmenu">';
		helpList += '<ul id="helpMenuId">';
		<s:iterator value="listHelpVideo" var="list" status="status">
		helpList += '<li><a href="javascript:openDiv(\'popup_name\',720,480,\'${pageContext.request.contextPath}/helpVideo_execute.action?param=<s:text name="#list.nomeFile"/>\',\'#fff\')"><s:text name="#list.titolo"/></a></li>';
		</s:iterator>
		helpList += '</ul>';
		helpList += '</div>';
	</s:if>
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/script-min_<s:text name="igeriv.version.timestamp"/>.js"></script>