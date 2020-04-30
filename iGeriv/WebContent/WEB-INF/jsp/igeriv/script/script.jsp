<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/combined-min-stable_<s:text name="igeriv.version.timestamp"/>.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/combined-min_<s:text name="igeriv.version.timestamp"/>.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/jquery.cookiebar_<s:text name="igeriv.version.timestamp"/>.js"></script>


	


<s:include value="/WEB-INF/jsp/igeriv/common/dojo_include.jsp"></s:include>
<script type="text/javascript">
	dojo.require("dojo.io.iframe");
	var attivaMemorizzazioneAutomatica = <s:if test="%{#session.mapParamsEdicola.paramEdicola14 == null}">true</s:if><s:else><s:property value="%{#session.mapParamsEdicola.paramEdicola14.paramValue}"/></s:else>;
	var barcodeScannerBeepEnabled = <s:if test="%{#session.mapParamsEdicola.paramEdicola22 != null && #session.mapParamsEdicola.paramEdicola22.paramValue == 'true' && #session.mapParamsEdicola.paramEdicola24 != null && #session.mapParamsEdicola.paramEdicola24.paramValue == 'true'}">true</s:if><s:else>false</s:else>;
	var msgDisabledMenuStarter = "<s:text name='igeriv.msg.disabled.menu.starter'/>";
	var spuntaBollaConsegna = "<s:text name='igeriv.spunta.bolla.consegna'/>";
	var isEdicolaPromo = <s:if test='authUser.edicolaPromo == true'>true;</s:if><s:else>false;</s:else>
	var userType = "<s:text name='authUser.tipoUtente'/>";
	var hasProfiloStarter = "<s:text name='#session.hasProfiloStarter'/>";
	var richiestaProva = "<s:text name='authUser.richiestaProva'/>";
	//var userOfflineMsg = '<s:text name="gp.verificare.la.connessione"/>'.replace('{0}','<s:text name='#session["appName"]'/>');
	var userOfflineMsg = '<s:text name="gp.verificare.la.connessione"/>';
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
	var autoSpuntaMessageOL = "<s:text name='igeriv.message.spuntare.tutt1.ordinilibri'/>";
	var alertTimeout = "<s:text name='igeriv.alert.timeout'/>";
	var memorizzaConfirm = "<s:text name='igeriv.memorizza.confirm'/>";
	var memorizzaInviaConfirm = "<s:text name='igeriv.memorizza.invia.confirm'/>";
	var sessionScadutaRedirectLoginMsg = "<s:text name='sessione.scaduta.redirect.login.msg'/>";
	var sessionScadutaAlert = "<s:text name='sessione.scaduta.alert'/>";
	var attenzioneMsg = "<s:text name='msg.alert.attenzione'/>";
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
	var igerivUri = "<s:property value='igerivUrl'/>";
	var privacyMessageEdicola = $.validator.format('<s:text name="igeriv.msg.sottoscrivi.privacy.edicola"/>', [igerivUri]);
	var privacyMessageLettore = $.validator.format('<s:text name="igeriv.msg.sottoscrivi.privacy.lettore"/>', [igerivUri]);
	var email = '<s:text name="dpe.contact.form.email"/>';
	var msgAccettoPrivacy = $.validator.format('<s:text name="igeriv.message.accetto.condizioni.privacy"/>', [igerivUri]);
	var msgInvalidEmail = '<s:text name="dpe.validation.msg.invalid.email"/>';
	var msgInvioEmailVerifica = "<s:text name='msg.avviso.invio.email.verifica'/>";
	var msgErroreInvioRichiesta = "<s:text name='msg.errore.invio.richiesta'/>";
	var msgEmailNonInserito = '<s:text name="igeriv.msg.email.non.inserito"/>';
	var msgEmailNonInseritoCliente = '<s:text name="igeriv.msg.email.non.inserito.cliente"/>';
	var msgPrivacyNonAccetata =  $.validator.format('<s:text name="igeriv.msg.privacy.non.accettata"/>', [igerivUri]);
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
	var error_localitaProviNuovoCliente = "<s:text name='nuovo.cliente.errore.localita'/>"; 
	
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
	var confermaProdottiDigitali = '<s:text name="conferma.prodotti.digitali"/>';
	var actionName = '<s:text name="actionName"/>';
	var msgCambioDl = '<s:text name="msg.cambio.dl"/>';
	var msgNoteFisse = '<s:text name="msg.note.fisse"/>';
	var testoPrivacyEdicolaStarter1 = '<s:text name="msg.privacy.edicola.starter.1"/>';
	var testoPrivacyEdicolaStarter2 = '<s:text name="msg.privacy.edicola.starter.2"/>';
	var codTipoImmagineMiniatura = <s:text name="constants.COD_TIPO_IMMAGINE_MINIATURA"/>;
	var codTipoImmagineMiniaturaEdicola = <s:text name="constants.COD_TIPO_IMMAGINE_MINIATURA_EDICOLA"/>;
	var codTipoImmagineMiniaturaProdottiEdicola = <s:text name="constants.COD_TIPO_IMMAGINE_MINIATURA_PNE_EDICOLA"/>;
	var CDL_CODE = <s:text name="#application['CDL_CODE']"/>;
	var codDl = "<s:text name='#session["codFiegDl"]'/>";

	var isMenta = <s:if test="%{#session['codFiegDl'] eq #application['MENTA_CODE']}">true</s:if><s:else>false</s:else>; 
	var isCDL = <s:if test="%{#session['codFiegDl'] eq #application['CDL_CODE']}">true</s:if><s:else>false</s:else>; 
	
	
	
	var timeout = "<s:text name='#session["timeout"]'/>";
	var networkDetectionInterval = "<s:text name='#application["networkDetectionIntervalMills"]'/>";
	var autoSaveBolleInterval = "<s:text name='#application["autoSaveBolleIntervalMills"]'/>";
	var highPriorityMessagesCheckInterval = "<s:text name='#application["highPriorityMessagesCheckInterval"]'/>";
	var hasMessaggiPrioritaMedia = <s:if test='#context["struts.actionMapping"].namespace == "/" && authUser.tipoUtente == "1" && messaggioNonLettoDto != null && messaggioNonLettoDto.priorita == 1'>true;</s:if><s:else>false;</s:else>
	var hasMessaggiPrioritaAlta = <s:if test='#context["struts.actionMapping"].namespace == "/" && authUser.tipoUtente == "1" && messaggioNonLettoDto != null && messaggioNonLettoDto.priorita == 2'>true;</s:if><s:else>false;</s:else>
	var hasMessaggiPrioritaMassima = <s:if test='#context["struts.actionMapping"].namespace == "/" && authUser.tipoUtente == "1" && messaggioNonLettoDto != null && messaggioNonLettoDto.priorita == 3'>true;</s:if><s:else>false;</s:else>
	
	var categoriaMessaggio = '<s:property value="messaggioNonLettoDto.categoria"/>';
	
	
	var priorita = <s:if test='#context["struts.actionMapping"].namespace == "/" && authUser.tipoUtente == "1" && messaggioNonLettoDto != null'><s:property value="messaggioNonLettoDto.priorita"/></s:if><s:else>''</s:else>;
	var hasEdicoleAutorizzateAggiornaBarcode = <s:if test="authUser.hasEdicoleAutorizzateAggiornaBarcode eq true">true;</s:if><s:else>false;</s:else>
	var hasArgomentiAiuto = <s:if test="help != null && !help.equals('')">true;</s:if><s:else>false;</s:else>
	var hasMessaggiDpe = <s:if test='#context["struts.actionMapping"].namespace == "/" && authUser.tipoUtente == "1" && hasMessaggiDpe == true'>true;</s:if><s:else>false;</s:else>
	var hasNotificheOrdiniClienti = <s:if test='hasNotificheOrdiniClienti == true'>true;</s:if><s:else>false;</s:else>
	var checkedEmail = <s:if test="checkedEmail eq true">true;</s:if><s:else>false;</s:else>
	var abilitataCorrezioneBarcode = "<s:text name='#session["abilitataCorrezioneBarcode"]'/>";
	var messaggioNonLetto = '<s:text name="messaggioNonLettoDto.pk"/>';
	var hasLivellamenti = <s:if test='authUser.hasLivellamenti == true'>true;</s:if><s:else>false;</s:else>
	var listIdLivellamenti = '<s:property value="listIdLivellamenti"/>';
	var listIdLivellamentiAccettati = '<s:property value="listIdLivellamentiAccettati"/>';
	var barcodeFinalizza = '<s:text name="constants.BARCODE_FINALIZZA"/>';
	var msgIGerivCard = '<s:text name="plg.vendita.igeriv.card"/>';	
	var msgInsertBarcodeIGerivCard = '<s:text name="label.pubblication_input_module_listener.IGeriv_Card_Insert_Barcode"/>';
	var informazioneMsg = '<s:text name="informazione"/>';
	var generalRpcActionUrl = '<s:url action="GlobalRpc.action"/>';
	var msgAggiornamentoBarcode = '<s:text name="plg.vendita.aggiornamento.barcode"/>';
	var msgInserisciTitolo = '<s:text name="plg.inserisci.titolo"/>';
	var msgConfirmAssociazioneBarcode = '<s:text name="igeriv.confirm.associazione.barcode"/>';
	var msgActionNotAllowed = '<s:text name="igeriv.action.not.allowed"/>';
	var COD_PAGINA_BOLLA_CONSEGNA = '<s:text name="constants.COD_PAGINA_BOLLA_CONSEGNA"/>';
	var COD_PAGINA_BOLLA_RESA = '<s:text name="constants.COD_PAGINA_BOLLA_RESA"/>';
	var ana = <s:if test="%{#parameters.ana[0] == 'true'}">true;</s:if><s:else>false;</s:else>
	var helpList = '';
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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/script-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<!-- test chat -->
<!--
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/atmosphere.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/chat.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/jquery.ui.chatbox.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.ui.chatbox.css" />
<script>
var box = $("#content").chatbox({
	id:"chat_div",
    user:{key : "value"},
    title : "test chat",
    hidden: false,
    offset: 0, // relative to right edge of the browser window
    width: 180,
    height: 150,
    messageSent : function(id, user, msg) {
        $("#content").chatbox("option", "boxManager").addMsg(id, msg);
    },
    boxClosed: function(id) {}, // called when the close icon is clicked
});
</script>
-->
