<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style>
.ui-menu-item {
	width: 400px;
	font-size: 14px;
	color: black;
	background: #fff;
}
</style>
<script>
	var userId = '<s:text name="%{#session['userId']}"/>';
	var msgConfirmAzzerareVociBolla = '<s:text name="msg.confirm.azzerare.voci.bolla"/>';
	var msgConfirmAggiungereNuovoProdotto = '<s:text name="msg.confirm.aggiungere.nuovo.prodotto"/>';
	var msgConfirmMemorizzaInviaBolla = '<s:text name="msg.confirm.memorizza.invia.bolla.resa.prodotti.vari"/>';
	var msgAggiungiProdotto = '<s:text name="igeriv.aggiungi.prodotto"/>';
	var msgCancellareBolla = '<s:text name="gp.vuoi.cancellare.bolla"/>';
	var msgCampoObbligatorio = '<s:text name="error.campo.x.obbligatorio"/>';
	var msgFornitore = '<s:text name="igeriv.fornitore"/>';
	var msgDataDocumento = '<s:text name="igeriv.data.documento"/>';
	var msgAttenzione = '<s:text name="msg.alert.attenzione"/>';
	var msgFormatoDataInvalido = '<s:text name="msg.formato.data.invalido"/>';
	var msgNumeroDocumento = '<s:text name="igeriv.numero.documento"/>';
	var msgCausale = '<s:text name="igeriv.causale"/>';
	var msgQuantita = '<s:text name="igeriv.quantita"/>';
	var msgPrezzo = '<s:text name="igeriv.prezzo"/>';
	var msgProdotto = '<s:text name="igeriv.prodotto"/>';
	var msgProdottiDuplicatiBolla = "<s:text name='gp.errore.prodotti.duplicati.bolla.carico.prodotti.vari'/>";
	var msgConfirmEditareProdotto = "<s:text name='igeriv.confirm.editare.iva.prodotto.non.editoriale'/>";
	var msgConfirmCloseDialog = "<s:text name='igeriv.confirm.chiudere.bolla.resa'/>";
	var msgErrorProdotto = "<s:text name='igeriv.nessuno.prodotto.selezionato'/>";
	var numbersStr = "${keys}";
	var magazzinoInterno = <s:text name='constants.COD_MAGAZZINO_INTERNO'/>;
	var magazzinoEsterno = <s:text name='constants.COD_MAGAZZINO_ESTERNO'/>;
	var byPassAliquotaCheck = false;
	var byPassCodiceProdottoFornitoreCheck = false;
	var codCausale = '<s:property value="bolla.causale.codiceCausale"/>';
	var numeroNonValido = '<s:text name="igeriv.numero.non.valido"/>';
	var msgDataDocumentoInvalida = '<s:text name="error.data.documento.bolla.resa.prodotti.vari.non.valida"/>';
	var msgQtaResoInvalida = '<s:text name="error.qta.resa.prodotto.non.valida"/>';
	var msgResaNonConsentita = '<s:text name="error.prodotto.senza.resa"/>';
	var dataDocumento = '<s:if test="#request.dataDocumentoEdit != null"><s:text name="#request.dataDocumentoEdit"/></s:if>';
	var isNew = <s:if test="isNew eq true">true</s:if><s:else>false</s:else>;
	var bollaInviata = <s:if test="bollaInviata eq true">true</s:if><s:else>false</s:else>;
	var codFornitore = <s:if test="codFornitore != null">'<s:text name="codFornitore"/>'</s:if><s:else>null</s:else>;
	var hasMemorizzaInvia = <s:if test="hasMemorizzaInvia eq true">true</s:if><s:else>false</s:else>;
	var codFiegDl = <s:text name="authUser.codFiegDl"/>;
	var codEdicolaMaster = <s:text name="authUser.codEdicolaMaster"/>;
	var bollaMemorizzata = false;
	var memInvBollaResaPressed = false;
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/pneBollaResaEdit-min_<s:text name="igeriv.version.timestamp"/>.js"></script>