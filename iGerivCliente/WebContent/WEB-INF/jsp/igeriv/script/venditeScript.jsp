<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<script>
	var regCassaApplet = null;
	var positionSizeDtoExists = false;
	var positionSizeDtoTop = 0;
	var positionSizeDtoLeft = 0;
	var positionSizeDtoWidth = 0;
	var positionSizeDtoHeight = 0;
	var maxValueMultiplier = 100;
	var idScontrino = '';
	var dataScontrino = '';
	var clienteConEstrattoConto = false;
	var chiudiLabel = '<s:text name="igeriv.chiudi"/>';
	var numMsg = '<s:text name="igeriv.numero.symbol"/>';
	var refreshMsg = '<s:text name="igeriv.refresh"/>';
	var pubNonDisponibileMsg = '<s:text name="msg.pubblicazione.non.uscita.non.disponibile"/>';
	var azzeraContoMsg = '<s:text name="label.main_frame.Reset_Bill_Message"/>';
	var nessunContoApertoMsg = '<s:text name="label.input_command.No_Bill"/>';
	var chiudiContoMsg = '<s:text name="label.main_frame.Close_Bill_Message"/>';
	var ricercaMinCaratteriMsg = '<s:text name="error.ricerca.per.titolo.almeno.due.caratteri"/>';
	var importoMinoreTotaleMsg = '<s:text name="label.main_frame.Cash_Less_Than_Total"/>';
	var impossibileEditareMsg = '<s:text name="impossibile.editare.vendita.gia.trasmessa"/>';
	var impossibileEditareContoInFatturaEmessaMsg = '<s:text name="impossibile.editare.vendita.in.fattura.emessa"/>';
	var impossibileEditareStornoScontrinoMsg = '<s:text name="impossibile.editare.storno.scontrino"/>';
	var impossibileEditareEstrattoContoChiusoMsg = '<s:text name="impossibile.editare.vendita.in.estratto.conto.chiuso"/>';
	var cancellaVenditaConfirmMsg = '<s:text name="dpe.conferma.cancella.vendita"/>';
	var sendErrorToDpeConfirmMsg = '<s:text name="dpe.conferma.invia.errore.applet.dpe"/>';
	var attenzioneMsg = '<s:text name="msg.alert.attenzione"/>';
	var informazioneMsg = '<s:text name="informazione"/>';
	var datiCliente = '<s:text name="gp.dati.cliente"/>';
	var ragSocialeClienteIndirizzoDatiFiscali = '<s:text name="dpe.rag.sociale.indirizzo.dati.fiscali"/>';
	var insertBarcodeMsg = '<s:text name="label.subscription_input_module_listener.Insert_Barcode_Message"/>';
	var scegliClienteMsg = '<s:text name="igeriv.scegli.il.cliente"/>';
	var alertInfoMsg = '<s:text name="msg.alert.info"/>';
	var insertBarcodeMsg = '<s:text name="label.subscription_input_module_listener.Insert_Barcode_Message"/>';
	var cardRechargeMsg = '<s:text name="label.subscription_input_module_listener.Card_Recharge"/>';
	var availableRechargesMsg = '<s:text name="label.subscription_input_module_listener.Available_Recharges"/>';
	var manageBackMsg = '<s:text name="label.subscription_input_module_listener.Manage_Back_Title"/>';
	var insertBackMsg = '<s:text name="label.subscription_input_module_listener.Insert_Back_Barcode_Title"/>';
	var confermaRicaricaMsg = '<s:text name="label.plg_barcode_input_module_listener.Conferma_Effetuare_Ricarica"/>';
	var prodIconWidth = <s:text name="prodottiVariIconWidth"/>;
	var prodIconHeight = <s:text name="prodottiVariconHeight"/>;
	var msgMostraCategorie = '<s:text name="igeriv.mostra.categorie"/>';
	var msgMostraSottocategorie = '<s:text name="igeriv.mostra.sottocategorie"/>';
	var msgInserisciPrezzo = '<s:text name="plg.inserisci.prezzo.unitario"/>';
	var msgInserisciTitolo = '<s:text name="plg.inserisci.titolo"/>';
	var msgVenditaProdottiVariReparto = '<s:text name="plg.vendita.prodotti.vari.reaparto"/>';
	var msgAggiornamentoBarcode = '<s:text name="plg.vendita.aggiornamento.barcode"/>';
	var moltiplicatoreInvalidoMsg = '<s:text name="igeriv.moltiplicatore.invalido"/>';
	var msgQuantita = '<s:text name="plg.quantita"/>';
	var msgPrezzoUnitario = '<s:text name="plg.prezzo.unitario"/>';
	var msgNascondiBarraProdottiVari = '<s:text name="igeriv.nascondi.barra.scelta.rapida.prodotti.vari"/>';
	var msgMostraBarraProdottiVari = '<s:text name="igeriv.mostra.barra.scelta.rapida.prodotti.vari"/>';
	var msgNascondiBarraPubblicazioni = '<s:text name="igeriv.nascondi.barra.scelta.rapida.pubblicazioni"/>';
	var msgMostraBarraPubblicazioni = '<s:text name="igeriv.mostra.barra.scelta.rapida.pubblicazioni"/>';
	var msgVenditaValore = '<s:text name="label.various.print.Value_Sell_Label"/>';	
	var msgProdottoSenzaGiacenza = '<s:text name="igeriv.prodotto.senza.giacenza"/>';
	var msgConfirmAssociazioneBarcode = '<s:text name="igeriv.confirm.associazione.barcode"/>';
	var msgConfirmInviaRichiestaAssociazioneBarcode = '<s:text name="igeriv.confirm.invia.richiesta.associazione.barcode"/>';
	var msgConfirmAggiornareAssociazioneBarcode = '<s:text name="igeriv.confirm.aggiornamento.barcode"/>';
	var msgConfirmRichiestaAggiornareAssociazioneBarcode = '<s:text name="igeriv.confirm.richiesta.aggiornamento.barcode"/>';
	var msgConfirmAggiungiTotaleAlContoCliente = '<s:text name="label.main_frame.Close_Bill_Message.Conto.Cliente.Confirm"/>';
	var msgConfirmRegCassaInstallPath = '<s:text name="igeriv.confirm.reg.cassa.install.path"/>';
	var msgConfirmRegCassaScontriniPath = '<s:text name="igeriv.confirm.reg.cassa.scontrini.path"/>';
	var msgConfirmStampareBollettinaConsegna = '<s:text name="igeriv.confirm.stampare.bollettina.consegna"/>';
	var msgConfirmStampareRicevutaFiscale = '<s:text name="igeriv.confirm.stampare.ricevuta.fiscale"/>';
	var msgConfirmStornoManualeRicevutaFiscale = '<s:text name="confirm.storno.manuale.scontrino"/>';
	var msgConfirmStornoManualeFattura = '<s:text name="confirm.storno.manuale.fattura"/>';
	var msgRegCassaInstallPath = '<s:text name="igeriv.reg.cassa.install.path"/>';
	var msgRegCassaScontriniPath = '<s:text name="igeriv.reg.cassa.scontrini.path"/>';
	var msgErrorUpdateRegCassaInstallPath = '<s:text name="igeriv.error.update.reg.cassa.install.path"/>';
	var msgRicevutaEmessa = '<s:text name="igeriv.ricevuta.emessa"/>';
	var msgMissingRegCassaActiveProcess = '<s:text name="igeriv.missing.reg.cassa.active.process"/>';
	var msgNonVisualizzarePiuMessaggio = '<s:text name="igeriv.msg.non.visualizzare.piu.questo.messaggio"/>';
	var unsupportedOS = '<s:text name="igeriv.usupported.os"/>';
	var quotaExceededConfirmMsg = '<s:text name="msg.raggiunto.limite.spazio.cache"/>';
	var localStorageAlreadyUpdatedConfirmMsg = '<s:text name="msg.cache.gia.aggiornata.pulire.si.no"/>';
	var ricevutaFiscale = '<s:text name="igeriv.ricevuta.fiscale"/>';
	var fattura = '<s:text name="igeriv.fattura"/>';
	var bollettinaConsegna = '<s:text name="igeriv.bollettina.consegna"/>';
	var moltiplicatoreMoltoGrandeMsg = '<s:text name="igeriv.moltiplicatore.molto.alto"/>';
	var msgScegliTipoDocumento = '<s:text name="igeriv.scegli.tipo.documento"/>';
	var msgTipoDocumento = '<s:text name="igeriv.tipo.documento"/>';
	var showLeftSideBar = <s:if test="%{#session.paramEdicola11.paramValue == null}">true</s:if><s:else><s:property value="%{#session.paramEdicola11.paramValue}"/></s:else>;
	var showRightSideBar = <s:if test="%{#session.paramEdicola19.paramValue == null}">true</s:if><s:else><s:property value="%{#session.paramEdicola19.paramValue}"/></s:else>;
	var blockVenditaUntillCallback = <s:if test="%{#session.paramEdicola9.paramValue == null}">false</s:if><s:else><s:property value="%{#session.paramEdicola9.paramValue}"/></s:else>;
	var useLocalStorage = <s:if test="%{#session.paramEdicola10.paramValue == null}">false</s:if><s:else><s:property value="%{#session.paramEdicola10.paramValue}"/></s:else>;
	var registratoreCassa = '<s:if test="%{#session.paramEdicola12.paramValue == null}"></s:if><s:else><s:property value="%{#session.paramEdicola12.paramValue}"/></s:else>';
	var prodottiVariTitlePrefix = '<s:text name="igeriv.prodotti.vari"/>' + ": ";
	var idContoCliente = '<s:property value="idConto"/>';
	var venditeIconWidth = '<s:text name="venditeIconWidth"/>';
	var venditeIconWidth = '<s:text name="venditeIconWidth"/>';
	var clientiEdicola = '<s:text name="#request.clientiEdicola"/>';
	var contoDelCliente = '<s:text name="igeriv.conto.del.cliente.tessera.o.nome"/>';
	var msgPreferiti = '<s:text name="igeriv.preferiti"/>';
	var msgSuggerimenti = '<s:text name="igeriv.suggerimenti"/>';
	var smartCardEdicola = '<s:text name="plg.vendita.igeriv.card"/>';
	var indirizzo = '<s:text name="dpe.indirizzo"/>';
	var localita = '<s:text name="dpe.localita"/>';
	var codFiscale = '<s:text name="dpe.codice.fiscale"/>';
	var piva = '<s:text name="dpe.piva"/>';
	var RICEVUTA_FISCALE = <s:text name="constants.RICEVUTA_FISCALE"/>;
	var FATTURA = <s:text name="constants.FATTURA"/>;
	var STORNO_FATTURA = <s:text name="constants.STORNO_FATTURA"/>;
	var BOLLETTINA_CONSEGNA = <s:text name="constants.BOLLETTINA_CONSEGNA"/>;
	var scrollbarWidth = 25;
	var codCliente = '<s:property value="idCliente"/>';
	var contoCliente = '<s:property value="contoCliente"/>';
	var venditeTabIdField = "progressivo";
	var pubblicazioniTabIdField = "idtn";
	var pubblicazioniBarcodeTabIdField = "idtn";
	var pubblicazioniRichiediBarcodeTabIdField = "idtn";
	var ricaricheTabIdField = "codTipologiaMinicard";
	var rowIndex = 0;
	var hasRegistratoreCassa = <s:if test="registratoreCassa != null">true</s:if><s:else>false</s:else>;
	var msgRepartiRegistratoreCassaVisto = <s:property value="msgRepartiRegistratoreCassaVisto"/>;
	var codRegCassa = <s:if test="registratoreCassa != null">${registratoreCassa.codRegCassa}</s:if><s:else>0</s:else>;
	
	<s:if test="positionSizeDto != null">
		positionSizeDtoExists = true;
		positionSizeDtoTop = isNaN(<s:property value="positionSizeDto.top"/>) ? 0 : Number(<s:property value="positionSizeDto.top"/>);
		positionSizeDtoLeft = isNaN(<s:property value="positionSizeDto.left"/>) ? 0 : Number(<s:property value="positionSizeDto.left"/>);
		positionSizeDtoWidth = isNaN(<s:property value="positionSizeDto.width"/>) ? 0 : Number(<s:property value="positionSizeDto.width"/>);
		positionSizeDtoHeight = isNaN(<s:property value="positionSizeDto.heigth"/>) ? 0 : Number(<s:property value="positionSizeDto.heigth"/>);
	</s:if>
	var smdUrl = "${smdUrl}";
	var arrCat = new Object();
	var arrScat = new Object();
	var arrProd = new Object();
	var arrAliquoteReparti = new Object();
	var mapGiacenze = {};
	
	<s:iterator value="listCategorie" status="status">
		<s:if test="pk.codCategoria != null">
			arrCat['<s:property value="pk.codCategoria"/>'] = '<s:property value="immagine"/>|<s:property value="descrizione"/>';
		</s:if>
	</s:iterator>
	<s:iterator value="listSottocatgorie" status="status">
		<s:if test="pk.codCategoria != null && pk.codSottoCategoria != null">
			arrScat['<s:property value="pk.codCategoria"/>_<s:property value="pk.codSottoCategoria"/>'] = '<s:property value="immagine"/>|<s:property value="descrizione"/>';
		</s:if>
	</s:iterator>
	<s:iterator value="listProdottiEdicola" status="status">
		<s:if test="codProdottoInterno != null">
			arrProd['<s:property value="codCategoria"/>_<s:property value="codSubCategoria"/>_<s:property value="codProdottoInterno"/>'] = '<s:property value="nomeImmagine"/>|<s:property value="descrizione"/>|<s:property value="barcode"/>|<s:property value="prezzo"/>|<s:property value="descrizioneB"/>|<s:property value="aliquota"/>';
			mapGiacenze.prod_<s:property value="codProdottoInterno"/> = '<s:property value="giacenzaProdotto"/>';
		</s:if>
	</s:iterator>
	<s:iterator value="listAliquoteReparti" status="status">
		arrAliquoteReparti['<s:property value="aliquota"/>'] = '<s:property value="reparto"/>';
	</s:iterator>
	if (screen.width > 1440) {
		$("#content").css({"width":"62%"});
	}
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js-transient/vendite-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/redmond/jquery-ui-1.8.18.custom_<s:text name="igeriv.version.timestamp"/>.css" />
<link rel="stylesheet" type="text/css" href="/app_js/dojo-release-1.7.2/dijit/themes/claro/claro.css" />
<link rel="stylesheet" type="text/css" href="/app_js/dojo-release-1.7.2/dojox/grid/resources/Grid.css" />
<link rel="stylesheet" type="text/css" href="/app_js/dojo-release-1.7.2/dojox/grid/resources/claroGrid.css" />
<style type="text/css">
	#makeMeScrollable {
		width: 60%;
		height: 50px;
		position: relative;
	}
	
	#makeMeScrollable div.scrollableArea * {
		position: relative;
		float: left;
		border: 1.5px solid #000;
	}
</style>