<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
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
	var msgVenditaProdottiVariReparto = '<s:text name="plg.vendita.prodotti.vari.reaparto"/>';
	var moltiplicatoreInvalidoMsg = '<s:text name="igeriv.moltiplicatore.invalido"/>';
	var msgQuantita = '<s:text name="plg.quantita"/>';
	var msgPrezzoUnitario = '<s:text name="plg.prezzo.unitario"/>';
	var msgNascondiBarraProdottiVari = '<s:text name="igeriv.nascondi.barra.scelta.rapida.prodotti.vari"/>';
	var msgMostraBarraProdottiVari = '<s:text name="igeriv.mostra.barra.scelta.rapida.prodotti.vari"/>';
	var msgNascondiBarraPubblicazioni = '<s:text name="igeriv.nascondi.barra.scelta.rapida.pubblicazioni"/>';
	var msgMostraBarraPubblicazioni = '<s:text name="igeriv.mostra.barra.scelta.rapida.pubblicazioni"/>';
	var msgVenditaValore = '<s:text name="label.various.print.Value_Sell_Label"/>';	
	var msgProdottoSenzaGiacenza = '<s:text name="igeriv.prodotto.senza.giacenza"/>';
	var msgProdottoDigitale = '<s:text name="igeriv.prodotto.digitale"/>';
	
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
	var msgConfirmRichiedereRifornimenti = '<s:text name="igeriv.confirm.richiedere.rifornimenti"/>';
	var msgRegCassaInstallPath = '<s:text name="igeriv.reg.cassa.install.path"/>';
	var msgRegCassaScontriniPath = '<s:text name="igeriv.reg.cassa.scontrini.path"/>';
	var msgErrorUpdateRegCassaInstallPath = '<s:text name="igeriv.error.update.reg.cassa.install.path"/>';
	var msgRicevutaEmessa = '<s:text name="igeriv.ricevuta.emessa"/>';
	var msgMissingRegCassaActiveProcess = '<s:text name="igeriv.missing.reg.cassa.active.process"/>';
	var msgRegCassaNotConnected = '<s:text name="igeriv.reg.cassa.not.connected"/>';
	var msgRegCassaDownloadResourceException = '<s:text name="igeriv.reg.cassa.download.resource.exception"/>';
	var msgNonVisualizzarePiuMessaggio = '<s:text name="igeriv.msg.non.visualizzare.piu.questo.messaggio"/>';
	var confirmDownloadDriver = '<s:text name="igeriv.confirm.install.driver.reg.cassa"/>';
	var msgInstallareDriver = '<s:text name="igeriv.missing.driver.reg.cassa"/>';
	
	var msgRiaperturaConto = '<s:text name="igeriv.riapertura.conto"/>';
	
	var unsupportedOS = '<s:text name="igeriv.usupported.os"/>';
	var quotaExceededConfirmMsg = '<s:text name="msg.raggiunto.limite.spazio.cache"/>';
	var localStorageAlreadyUpdatedConfirmMsg = '<s:text name="msg.cache.gia.aggiornata.pulire.si.no"/>';
	var ricevutaFiscale = '<s:text name="igeriv.ricevuta.fiscale"/>';
	var fattura = '<s:text name="igeriv.fattura"/>';
	var bollettinaConsegna = '<s:text name="igeriv.bollettina.consegna"/>';
	var moltiplicatoreMoltoGrandeMsg = '<s:text name="igeriv.moltiplicatore.molto.alto"/>';
	var msgScegliTipoDocumento = '<s:text name="igeriv.scegli.tipo.documento"/>';
	var msgTipoDocumento = '<s:text name="igeriv.tipo.documento"/>';
	var showLeftSideBar = <s:if test="%{#session.mapParamsEdicola.paramEdicola11.paramValue == null}">true</s:if><s:else><s:property value="%{#session.mapParamsEdicola.paramEdicola11.paramValue}"/></s:else>;
	var showRightSideBar = <s:if test="%{#session.mapParamsEdicola.paramEdicola19.paramValue == null}">true</s:if><s:else><s:property value="%{#session.mapParamsEdicola.paramEdicola19.paramValue}"/></s:else>;
	var blockVenditaUntillCallback = <s:if test="%{#session.mapParamsEdicola.paramEdicola9.paramValue == null}">false</s:if><s:else><s:property value="%{#session.mapParamsEdicola.paramEdicola9.paramValue}"/></s:else>;
	var useLocalStorage = <s:if test="%{#session.mapParamsEdicola.paramEdicola10.paramValue == null}">false</s:if><s:else><s:property value="%{#session.mapParamsEdicola.paramEdicola10.paramValue}"/></s:else>;
	var registratoreCassa = '<s:if test="%{#session.mapParamsEdicola.paramEdicola12.paramValue == null}"></s:if><s:else><s:property value="%{#session.mapParamsEdicola.paramEdicola12.paramValue}"/></s:else>';
	
	var paramRichiestaRifornimentoVendite = '<s:if test="%{#session.mapParamsEdicola.paramEdicola21.paramValue == null}"></s:if><s:else><s:property value="%{#session.mapParamsEdicola.paramEdicola21.paramValue}"/></s:else>';
	
	//Ticket 0000371
	var abilitaBeepVenditaBarcode = <s:if test="abilitaBeepVenditaBarcode == null">false</s:if><s:else><s:if test="abilitaBeepVenditaBarcode == 1">true</s:if><s:else>false</s:else></s:else>;
	
	
	
	var prodottiVariTitlePrefix = '<s:text name="igeriv.prodotti.vari"/>' + ": ";
	var idContoCliente = '<s:property value="idConto"/>';
	var venditeIconWidth = '<s:text name="venditeIconWidth"/>';
	var clientiEdicola = '<s:text name="#request.clientiEdicola"/>';
	var contoDelCliente = '<s:text name="igeriv.conto.del.cliente.tessera.o.nome"/>';
	var msgPreferiti = '<s:text name="igeriv.preferiti"/>';
	var msgSuggerimenti = '<s:text name="igeriv.suggerimenti"/>';
	var smartCardEdicola = '<s:text name="plg.vendita.igeriv.card"/>';
	var indirizzo = '<s:text name="dpe.indirizzo"/>';
	var localita = '<s:text name="dpe.localita"/>';
	var codFiscale = '<s:text name="dpe.codice.fiscale"/>';
	var aliquotaIvaNonValidaMsg = '<s:text name="gp.aliquota.prodotto.non.editoriale.non.valida"/>';	
	var piva = '<s:text name="dpe.piva"/>';
	var msgInserisciCodiceClienteReteEdicole = '<s:text name="igeriv.inserisci.codice.rete.edicole"/>';
	var msgCodiceClienteReteEdicole = '<s:text name="igeriv.codice.rete.edicole"/>';
	var msgVenditaLivellamentiEseguita = '<s:text name="igeriv.vendita.livellamenti.eseguita"/>';
	var RICEVUTA_FISCALE = <s:text name="constants.RICEVUTA_FISCALE"/>;
	var FATTURA = <s:text name="constants.FATTURA"/>;
	var STORNO_FATTURA = <s:text name="constants.STORNO_FATTURA"/>;
	var BOLLETTINA_CONSEGNA = <s:text name="constants.BOLLETTINA_CONSEGNA"/>;
	var scrollbarWidth = 25;
	var codCliente = '<s:property value="idCliente"/>';
	var contoCliente = '<s:property value="contoCliente"/>';
	var venditeTabIdField = "progressivo";
	var ricaricheTabIdField = "codTipologiaMinicard";
	var pubblicazioniBarcodeTabIdField = "idtn";
	var pubblicazioniRichiediBarcodeTabIdField = "idtn";
	var rowIndex = 0;
	var hasRegistratoreCassa = <s:if test="registratoreCassa != null">true</s:if><s:else>false</s:else>;
	var msgRepartiRegistratoreCassaVisto = <s:property value="msgRepartiRegistratoreCassaVisto"/>;
	var codRegCassa = <s:if test="registratoreCassa != null">${registratoreCassa.codRegCassa}</s:if><s:else>0</s:else>;
	var labelRicaricaCopie = '<s:text name="label.subscription_input_module_listener.Rechange_Copies"/>';
	var labelRicaricaValore = '<s:text name="label.subscription_input_module_listener.Rechange_Cash"/>';
	var labelInsertBarcode = '<s:text name="label.subscription_input_module_listener.Insert_Barcode_Message"/>';
	var labelInsertBarcodeCollaterale = '<s:text name="label.subscription_input_module_listener.Insert_Barcode_Collaterale_Message"/>';
	var labelInsertCollateraleBarcodeTitle = 'Codice a barre';
	var labelConsegnareCopia = '<s:text name="label.subscription_input_module_listener.Consegnare_Copia"/>';
	var labelCopie = '<s:text name="label.subscription_input_module_listener.Copie"/>';
	var labelCopia = '<s:text name="label.subscription_input_module_listener.Copia"/>';
	var labelConfirmConsegnaPubblicazioneValore = '<s:text name="igeriv.confirm.consegna.pubblicazione.valore"/>';
	var labelRiaricaDisponibile = '<s:text name="label.plg_barcode_input_module_listener.Puoi_Ricaricare_Tessera"/>';
	var labelTesseraRicaricata = '<s:text name="label.plg_barcode_input_module_listener.Abbonamento_Rinnovato"/>';
	var labelInfoStatoTesseraCopieSingola = '<s:text name="label.plg_barcode_input_module_listener.Info_Tessera"/>';
	var labelInfoStatoTesseraCopieSingolaSenzaCopieResidue = '<s:text name="label.plg_barcode_input_module_listener.Info_Tessera.Senza_Copie_Residue"/>';
	var labelInfoStatoTesseraValoreSingola = '<s:text name="label.plg_barcode_input_module_listener.Info_Tessera_Valore"/>';
	var labelInfoStatoTesseraValoreMultipla = '<s:text name="label.plg_barcode_input_module_listener.Info_Tessera_Valore_Line"/>';
	var labelInfoStatoTesseraCopieMultipla = '<s:text name="label.plg_barcode_input_module_listener.Info_Tessera_Line"/>';
	var labelInfoStatoTesseraCopieMultiplaSenzaCopieResidue = '<s:text name="label.plg_barcode_input_module_listener.Info_Tessera_Line.Senza_Copie_Residue"/>';
	var labelStatoTesserePerEditoriSenzaPrivacy = '<s:text name="stato.tessere.per.editori.senza.privacy.sottoscritta"/>';
	var labelInserisciCodiceBarreDiPubblicazione = '<s:text name="label.subscription_input_module_listener.Insert_Barcode_For_Pubblication_Message"/>';
	var labelInserisciCodiceBarre = '<s:text name="label.subscription_input_module_listener.Insert_Barcode_For_Multi_Pubblication_Message"/>';
	var labelTesseraNuovaDaRicaricare = '<s:text name="label.plg_barcode_input_module_listener.Tessera_Da_Ricaricare"/>';
	var labelConfirmEffettuareRicarica = '<s:text name="label.plg_barcode_input_module_listener.Effettuare_Ricarica"/>';
	var labelTesseraEsaurita = '<s:text name="label.plg_barcode_input_module_listener.Tessera_Esaurita"/>';
	var labelConfirmEffettuareRicarica = '<s:text name="label.plg_barcode_input_module_listener.Effettuare_Ricarica"/>';
	var labelCopiaGiaConsegnata = '<s:text name="label.subscription_input_module_listener.Title_Already_Delivered.html"/>';
	var labelConfirmEseguireGestioneArretrato = '<s:text name="label.subscription_input_module_listener.No_Copies.html"/>';
	var labelCopiaGiaConsegnata = '<s:text name="label.subscription_input_module_listener.Title_Already_Delivered.html"/>';
	var labelRivenditaNonAncoraAbilitataConsegna = '<s:text name="label.subscription_input_module_listener.RivenditaNotActive"/>';
	var labelContrattoNonPiuValido = '<s:text name="label.subscription_input_module_listener.AccountExpired"/>';
	var labelContrattoNonAncoraValido = '<s:text name="label.subscription_input_module_listener.AccountNotYetValid"/>';
	var labelTesseraSenzaRicaricaAValore = '<s:text name="igeriv.tessera.senza.ricarica.valore"/>';
	var labelListiniTesserePerEditoriSenzaPrivacy = '<s:text name="listini.per.editori.senza.privacy.sottoscritta"/>';
	var labelPubblicazioneNonUscita = '<s:text name="label.plg_barcode_input_module_listener.Giornale_Non_Uscito"/>';
	var labelPubblicazioneTroppoVecchia = '<s:text name="label.subscription_input_module_listener.Copy_Too_Old"/>';
	var labelNessunaPubblicazioneDaConsegnare = '<s:text name="label.subscription_input_module_listener.No_Copies_To_Be_Delivered_Card.1.html"/>';
	var labelTesseraNonRiconosciuta = '<s:text name="gp.codice.nessun.prodotto"/>';
	var labelOperazioneInvalida = '<s:text name="label.plg_barcode_input_module_listener.Invalid_Operation"/>';
	var labelTesseraInsesistente = '<s:text name="label.subscription_input_module_listener.Card_Not_Found"/>';
	var labelConsegnaPressoRivenditaErrata = '<s:text name="label.subscription_input_module_listener.DeliveryNotAllowed"/>';
	var labelTesseraSospesa = '<s:text name="label.subscription_input_module_listener.Card_Suspended"/>';
	var labelTesseraAnnullata = '<s:text name="label.subscription_input_module_listener.Card_Cancelled"/>';
	var labelTesseraEsaurita = '<s:text name="label.plg_barcode_input_module_listener.Tessera_Esaurita"/>';
	var labelTesseraInSostituzione = '<s:text name="igeriv.richiesta.sostituzione.tessera.pendente"/>';
	var labelCreditoInsufficiente = '<s:text name="igeriv.credito.tessera.non.sufficiente"/>';
	var labelCreditoEccessivo = '<s:text name="label.plg_barcode_input_module_listener.Credito_Eccessivo"/>';
	var labelPlafondSuperato = '<s:text name="label.plg_barcode_input_module_listener.Minicard_Superato_Plafond"/>';
	var labelProdottoNonCorrispondeAbbonamento = '<s:text name="igeriv.publicazione.non.corrisponde"/>';
	var labelCodiceBarreNonCorrisponde = '<s:text name="igeriv.publicazione.non.disponibile"/>';
	var labelPubblicazioneNonInPromozione = '<s:text name="igeriv.publicazione.non.in.promozione"/>';
	var labelNessunContratto = '<s:text name="error.tessera.nessun.abbonamento.ricarica"/>';
	var labelManageBackTitle = '<s:text name="label.subscription_input_module_listener.Manage_Back_Title"/>';
	var labelInsertBackBarcodeTitle = '<s:text name="label.subscription_input_module_listener.Insert_Back_Barcode_Title"/>';
	var labelCardRecharge = '<s:text name="label.subscription_input_module_listener.Card_Recharge"/>';
	var labelAvailableRecharges = '<s:text name="label.subscription_input_module_listener.Available_Recharges"/>';
	var labelChooseDesiredRecharge = '<s:text name="label.subscription_input_module_listener.Choose_Desired_Recharge"/>';
	var labelErroreImprevisto = '<s:text name="gp.errore.imprevisto"/>';
	var labelConfermaEffettuareRicaricaTitle = '<s:text name="label.subscription_input_module_listener.Recharge_Confirm"/>';
	var labelConfermaEffettuareRicarica = '<s:text name="label.plg_barcode_input_module_listener.Conferma_Effetuare_Ricarica"/>';
	var labelTitleVenditeAbbonatiDialog = '<s:text name="label.report.vendite.abbonati.gazz.parma"/>';
	var labelTitleVenditeMinicardDialog = '<s:text name="label.report.vendite.minicard.gazz.parma"/>';
	var labelTitleRicaricheDialog = '<s:text name="label.report.ricariche.minicard.gazz.parma"/>';
	var codTipologiaMinicardValore = '<s:text name="constants.COD_TIPOLOGIA_MINICARD_VALORE"/>';
	var codTipoScontrinoNormale = '<s:text name="constants.TIPO_SCONTRINO_NORMALE"/>';
	var codTipoScontrinoDettagliato = '<s:text name="constants.TIPO_SCONTRINO_DETTAGLIATO"/>';
	var clientiConEC = '<s:text name="igeriv.con.estratto.conto"/>';
	var clientiSenzaEC = '<s:text name="igeriv.senza.estratto.conto"/>';
	var abbonamentiPlg = '<s:text name="igeriv.plg"/>';
	var reportVenditeRicaricheMsg = '<s:text name="scegli.tipo.report.periodo.vendite.ricariche"/>';
	var reportVenditeRicaricheTitle = '<s:text name="mostra.report.vendite.ricariche"/>';
	var regExprMinicard = <s:if test="regExprMinicard neq null && regExprMinicard neq ''">/<s:property value="regExprMinicard"/>/</s:if><s:else>''</s:else>;
	var regExprMinicardPlg = <s:if test="regExprMinicardPlg neq null && regExprMinicardPlg neq ''">/<s:property value="regExprMinicardPlg"/>/</s:if><s:else>''</s:else>;
	var codFiegDl = '<s:text name="authUser.codFiegDl"/>';
	var codFiegMenta = '<s:text name="constants.COD_FIEG_MENTA"/>';
	var msgDataDa = '<s:text name="igeriv.da"/>';
	var msgDataA = '<s:text name="igeriv.a"/>';
	var msgVenditeAbbonato = '<s:text name="igeriv.consegne.abbonamenti.gazzetta.parma"/>';
	var msgVenditeMinicard = '<s:text name="igeriv.consegne.minicard.gazzetta.parma"/>';
	var msgRicaricheMinicard = '<s:text name="igeriv.ricariche.minicard.gazzetta.parma"/>';
	var barraProdottiVariVisible = <s:if test="positionSizeDto eq null || (positionSizeDto neq null && positionSizeDto.barraProdottiVariVisible eq true)">true</s:if><s:else>false</s:else>;
	
	var isMenta = <s:if test="authUser.codFiegDl eq constants.COD_FIEG_MENTA">true</s:if><s:else>false</s:else>; 
	var viewImageByProfile_isDisabledPopupRifornimenti = <s:if test="%{#session['isProfileBollaConsegnaReadOnly'] eq true}">true</s:if><s:else>false</s:else>; 
	// add 14/02/2017 - Gestione dell'esportazione in XLS e PDF (Gestione dei dati sensibili)
	var isEnabledExportXLS 		= <s:if test="%{#session['isEnabledExportXLS'] eq true}">false</s:if><s:else>true</s:else>; 
	var isEnabledPKInExportXLS 	= <s:if test="%{#session['isEnabledPKInExportXLS'] eq true}">false</s:if><s:else>true</s:else>;
	var isEnabledExportPDF 		= <s:if test="%{#session['isEnabledExportPDF'] eq true}">false</s:if><s:else>true</s:else>;
	var isEnabledPKInExportPDF 	= <s:if test="%{#session['isEnabledPKInExportPDF'] eq true}">false</s:if><s:else>true</s:else>;
	
	var isCDL = <s:if test="authUser.codFiegDl eq constants.COD_FIEG_CDL">true</s:if><s:else>false</s:else>; 
	
	
	var euroSign = "&#8364;";
	var BARCODE_PRODOTTO_EDITORIALE_MAX_LENGTH = 18;
	var mapLabelPrivacyMancante = new Object();
	mapLabelPrivacyMancante['PRIVACY_MANCANTE_21'] = '<s:text name="cliente.privacy.mancante.21"/>';
	
	var mapBeanTypeException = new Object();
	mapBeanTypeException['PUBBLICAZIONE_NON_USCITA'] = labelPubblicazioneNonUscita;
	mapBeanTypeException['DATA_USCITA_NON_VALIDA'] = labelPubblicazioneNonUscita;
	mapBeanTypeException['PUBBLICAZIONE_TROPPO_VECCHIA'] = labelPubblicazioneTroppoVecchia;
	mapBeanTypeException['NESSUNA_PUBBLICAZIONE_DA_CONSEGNARE'] = labelNessunaPubblicazioneDaConsegnare;
	mapBeanTypeException['TESSERA_NON_RICONOSCIUTA'] = labelTesseraNonRiconosciuta;
	mapBeanTypeException['OPERAZIONE_INVALIDA'] = labelOperazioneInvalida;
	mapBeanTypeException['TESSERA_INESISTENTE'] = labelTesseraInsesistente;
	mapBeanTypeException['RIVENDITA_ERRATA'] = labelConsegnaPressoRivenditaErrata;
	mapBeanTypeException['TESSERA_SOSPESA'] = labelTesseraSospesa;
	mapBeanTypeException['TESSERA_ANNULLATA'] = labelTesseraAnnullata;
	mapBeanTypeException['TESSERA_ESAURITA'] = labelTesseraEsaurita;
	mapBeanTypeException['TESSERA_IN_SOSTITUZIONE'] = labelTesseraInSostituzione;
	mapBeanTypeException['CREDITO_INSUFFICIENTE'] = labelCreditoInsufficiente;
	mapBeanTypeException['CREDITO_ECCESSIVO'] = labelCreditoEccessivo;
	mapBeanTypeException['PLAFOND_SUPERATO'] = labelPlafondSuperato;
	mapBeanTypeException['PRODOTTO_NON_CORRISPONDE_ABBONAMENTO'] = labelProdottoNonCorrispondeAbbonamento;
	mapBeanTypeException['NESSUNA_USCITA_PUBBLICAZIONE'] = labelCodiceBarreNonCorrisponde;
	mapBeanTypeException['PRODOTTO_NON_IN_PROMOZIONE'] = labelPubblicazioneNonInPromozione;
	mapBeanTypeException['NESSUN_CONTRATTO'] = labelNessunContratto;
	
	<s:if test="positionSizeDto != null">
		positionSizeDtoTop = isNaN(<s:property value="positionSizeDto.top"/>) ? 0 : Number(<s:property value="positionSizeDto.top"/>);
		positionSizeDtoLeft = isNaN(<s:property value="positionSizeDto.left"/>) ? 0 : Number(<s:property value="positionSizeDto.left"/>);
		positionSizeDtoWidth = isNaN(<s:property value="positionSizeDto.width"/>) ? 0 : Number(<s:property value="positionSizeDto.width"/>);
		positionSizeDtoHeight = isNaN(<s:property value="positionSizeDto.height"/>) ? 0 : Number(<s:property value="positionSizeDto.height"/>);
		positionSizeDtoExists = true;
	</s:if>
	var smdUrl = "${smdUrl}";
	var arrCat = new Object();
	var arrScat = new Object();
	var arrProd = new Object();
	var arrAliquoteReparti = new Object();
	var arrAliquoteDisabilitate = new Array();
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
			arrProd['<s:property value="codCategoria"/>_<s:property value="codSubCategoria"/>_<s:property value="codProdottoInterno"/>'] = '<s:property value="nomeImmagine"/>|<s:property value="descrizione"/>|<s:property value="barcode"/>|<s:property value="prezzo"/>|<s:property value="descrizioneB"/>|<s:property value="aliquota"/>|<s:property value="isProdottoDigitale"/>';
			mapGiacenze.prod_<s:property value="codProdottoInterno"/> = '<s:property value="giacenzaProdotto"/>';
		</s:if>
	</s:iterator>
	<s:iterator value="listAliquoteReparti" status="status">
		<s:if test="disabilitato eq true">
			arrAliquoteDisabilitate.push('<s:property value="aliquota"/>');
		</s:if>
		<s:else>
			arrAliquoteReparti['<s:property value="aliquota"/>'] = '<s:property value="reparto"/>';
		</s:else>
	</s:iterator>
	if (screen.width > 1440) {
		$("#content").css({"width":"62%"});
	}
</script>
<script type="text/javascript" src="/app_js/underscore-min.js"></script>
<script type="text/javascript" src="/app_js/backbone-min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js-transient/vendite-min_<s:text name="igeriv.version.timestamp"/>.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/redmond/jquery-ui-1.8.18.custom.css" />
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dijit/themes/claro/claro.css" />
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dojox/grid/resources/Grid.css" />
<link rel="stylesheet" type="text/css"
	href="/app_js/dojo-release-1.7.2/dojox/grid/resources/claroGrid.css" />
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

#closePneDiv {
	float: right;
	display: inline-block;
	padding: 2px 5px;
	background: #000;
	color: #fff;
	font-weight: bold;
}

#closePneDiv:hover {
	float: right;
	display: inline-block;
	padding: 2px 5px;
	background: #000;
	color: #FFBF00;
	font-weight: bold;
}
</style>