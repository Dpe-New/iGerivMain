package it.dpe.igeriv.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * Classe di costanti per Rtae.
 * 
 * @author romanom
 *
 */
public class RtaeConstants {
	public static final String ID = "RtaeConstants";
	private static Map<String, Object> nameToValueMap = createNameToValueMap();
	public static final String ID_APP_GAZZETTA_MINICARD = "PLG_GAZZETTA_PARMA";
	public static final String COD_GAZZETTA = "9999";
	public static final String STATO_CONSEGNA_ATTIVA = " ";
	public static final String STATO_CONSEGNA_SOSPESA = "S";
	public static final String STATO_CONSEGNA_ANNULLATA = "A";
	public static final int COPIE_CONSEGNATE_DEFAULT = 1;
	public static final String FINE_RECORD = "*";
	public static final String COD_FLUSSO_ANAGRAFICA_MINICARD = "COD_FLUSSO_ANAGRAFICA_MINICARD"; // TODO 
	public static final String RICARICA_PAGAMENTO = "P";
	public static final String RICARICA_OMAGGIO = "O";
	public static final String RICARICA_RIMBORSO = "R";
	public static final String STATO_TESSERA_ATTIVA = " ";
	public static final String RITIRO_PRESSO_RIVENDITA_PREFERITA_NON_OBBLIGATORIO = " ";
	public static final String STATO_TESSERA_ANNULLATA = "A";
	public static final String STATO_TESSERA_EMESSA_NON_ATTIVA = "E";
	public static final String STATO_TESSERA_SOSPESA = "S";
	public static final String STATO_TESSERA_SOSTITUITA = "X";
	public static final String COD_AUTORIZZAZIONE_RICARICA_TUTTI_TEXT = "Tutti";
	public static final String COD_AUTORIZZAZIONE_RICARICA_EDITORE_TEXT = "Editore";
	public static final int MAX_TABLE_HEIGHT = 350;
	public static final int TABLE_ROW_HEIGHT = 60;
	public static final int COD_ABILITATO = 1;
	public static final int COD_NON_ABILITATO = 0;
	public static final int COD_AUTORIZZAZIONE_RICARICA_TUTTI = 0;
	public static final int COD_AUTORIZZAZIONE_RICARICA_EDITORE = 1;
	public static final String EXPORT_DL_FILE_NAME_MODEL = "%s%s%s%s";
	public static final String EXPORT_ED_FILE_NAME_MODEL = "%s%s%s";
	public static final String USER_NAME_FORMAT = "%s%s%s";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_RTAE_DL = "ROLE_RTAE_DL";
	public static final String ROLE_RTAE_ED = "ROLE_RTAE_ED";
	public static final String ROLE_RTAE_CS = "ROLE_RTAE_CS";
	public static final String ROLE_RTAE_RIV = "ROLE_RTAE_RIV";
	public static final String ROLE_RTAE_UTENTE = "ROLE_RTAE_UTENTE";
	public static final String ROLE_REST = "ROLE_REST";
	public static final String ROLE_CRON = "ROLE_CRON";
	public static final String ONE_WAY_ENCRIPTED_PWD = "owe";
	public static final String ROLE_RTAE_ED_PRESSDI_CUST = "ROLE_RTAE_ED_PRESSDI_CUST";
	public static final String ROLE_RTAE_ED_PRESSDI_ADM = "ROLE_RTAE_ED_PRESSDI_ADM";
	public static final String ROLE_RTAE_ED_PRESSDI_MKT = "ROLE_RTAE_ED_PRESSDI_MKT";
	public static final String ROLE_RTAE_ED_PRESSDI_FULL = "ROLE_RTAE_ED_PRESSDI_FULL";
	
	public static final String BLANK = "";
	public static final String SECURITY_ACCESS_VALUE = "disabled";
	public static final String HASH = "#";
	public static final String STRING_TRUE = "true";
	public static final String EMPTY_SPACE_STRING = " ";
	public static final String ALL_SYMBOL = "*";
	public static final String SELECTED = "on";
	public static final String UNSELECTED = "off";
	public static final String SLASH = "/";
	public static final String QUESTION_MARK = "?";
	public static final String SEMI_COLON = ";";
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final String EURO_SIGN_ASCII = "\u20AC";
	public static final String EURO_SIGN_HTML_CHAR_ENTITY = "&#8364;";
	public static final String EURO_SIGN_HTML = "&euro;";
	public static final String UNDERSCORE = "_";
	public static final String IT = "it";
	public static final String PT = "pt";
	public static final String EN = "en";
	public static final String ES = "es";
	public static final String EURO_SIGN = "\u20AC";
	public static final String J_SPRING_SECURITY_CHECK = "j_spring_security_check";
	public static final String REDIRECT = "redirect";
	public static final String ACTION_ORDINI = "ordini";
	public static final String ACTION_RICHIESTE_RIFORNIMENTI = "richiesteRifornimenti";
	public static final String ACTION_RICHIESTE_RIFORNIMENTI_EDIT = "richiesteRifornimentiEdit";
	public static final String ACTION_VIEW_RICHIESTE_RIFORNIMENTI = "viewRichiesteRifornimenti";
	public static final String ACTION_NUOVA_RIVENDITA = "nuovaRivendita";
	public static final String ACTION_NUOVO_UTENTE_EDICOLA = "nuovoUtenteEdicola";
	public static final String ACTION_SUCCESS_LOCALITA = "successLocalita";
	public static final String ACTION_SUCCESS_ABBONATO = "successAbbonato";
	public static final String ACTION_SUCCESS_PRODOTTI = "successProdotti";
	public static final String ACTION_SUCCESS_PRODOTTI_REPORT_RICARICHE = "successProdottiReportRicariche";
	public static final String ACTION_SUCCESS_TIPI_TESSERA = "successTipiTessera";
	public static final String ACTION_NUOVO_FUORI_VOCE = "successNuovoFuoriVoce";
	public static final String ACTION_BOLLA_RESA_FUORI_VOCE = "showBollaResaFuoriVoce";
	public static final String ACTION_PRENOTAZIONE_CLIENTI = "prenotazioniClienteEdicola";
	public static final String ACTION_VIEW_PRENOTAZIONI_INSERITE_CLIENTI_EDICOLA = "viewPrenotazioniInseriteClienteEdicola";
	public static final String ACTION_EVASIONE_PRENOTAZIONI_CLIENTI_EDICOLA = "viewEvasionePrenotazioniClienteEdicola";
	public static final String ACTION_RESULT_EVASIONE_PRENOTAZIONI_CLIENTI_EDICOLA = "viewResultEvasionePrenotazioniClienteEdicola";
	public static final String ACTION_INPUT_PUBBLICAZIONI = "inputPubblicazioni";
	public static final String ACTION_NUMERI_PRECEDENTI = "numeriPrecedenti";
	public static final String ACTION_NEW_MESSAGE = "newMessage";
	public static final String ACTION_SHOW_MESSAGE = "showMessage";
	public static final String ACTION_SHOW_CONFERME_LETTURA = "showConfermeLettura";
	public static final String ACTION_RITIRI_REPORT_PDF = "ritiriReportPdf";
	public static final String ACTION_RITIRI_PRODOTTO_REPORT_PDF = "ritiriProdottoReportPdf";
	public static final String ACTION_RICARICHE_RIEP_REPORT_PDF = "ritiriRiepReportPdf";
	public static final String ACTION_RICARICHE_REPORT_PDF = "ricaricheReportPdf";
	
	public static final String ACTION_REPORT_CONTO_DEPOSITO = "reportContoDeposito";
	
	public static final String METHOD_BASE_PK_NAME = "pk";
	public static final String METHOD_BASE_NAME_DIFFERENZE = "differenze";
	public static final String METHOD_BASE_NAME_SPUNTA = "spunta";
	public static final String METHOD_BASE_NAME_RESO = "reso";
	
	public static final String SESSION_VAR_LOGIN = "loginType";
	public static final String SESSION_VAR_SPUNTE = "spunte";
	public static final String SESSION_VAR_DIFFERENZE = "differenze";
	public static final String SESSION_VAR_RESA = "resa";
	public static final String SESSION_VAR_COPIE = "copie";
	public static final String SESSION_VAR_NOTE = "note";
	public static final String SESSION_VAR_PRENOTAZIONE = "prenotazione";
	public static final String SESSION_VAR_DATA_SCADENZA_RICHIESTA = "dataScadenzaRichiesta";
	public static final String SESSION_VAR_PRENOTAZIONI_COPIE = "prenotazioniCopie";
	public static final String SESSION_VAR_PRENOTAZIONI_COPIE_FISSE = "prenotazioniCopieFisse";
	public static final String SESSION_VAR_EVASO = "evaso";
	public static final String SESSION_VAR_MESSAGGIO_CODIFICATO = "messaggioCodificato";
	public static final String SESSION_VAR_MESSAGGIO_LIBERO = "messaggioLibero";
	public static final String SESSION_VAR_USER_DETAILS = "ud";
	public static final String SESSION_VAR_EDITORI = "editori";
	public static final String SESSION_VAR_DL = "dl";
	public static final String SESSION_VAR_PRODOTTI = "prodotti";
	public static final String SESSION_VAR_PROVINCE = "province";
	public static final String SESSION_VAR_TUTTI_PRODOTTI = "tutti_prodotti";
	public static final String SESSION_VAR_PRODOTTI_DTO = "prodotti_dto";
	public static final String SESSION_VAR_TIPI_TESSERA = "tipiTessera";
	public static final String SESSION_VAR_COD_FIEG_DL = "codFiegDl";
	public static final String SESSION_VAR_STATI_TESSERA = "statiTessera";
	public static final String SESSION_VAR_LIST_PRODOTTI = "listProdotti";
	public static final String SESSION_VAR_MAP_PRIVACY = "mapPrivacy";
	public static final String SESSION_VAR_ABBONAMENTI = "abbonamenti";
	public static final String COD_TIPO_TESSERA_ABBONATO = "A01";
	public static final String COD_TIPO_TESSERA_ABBONATO_OMAGGIO = "OM1";
	public static final String COD_TIPO_TESSERA_FIDELITY_CARD = "F01";
	public static final String COD_TIPO_TESSERA_PASSEPARTOUT = "P01";
	public static final String COD_TIPO_TESSERA_RICARICABILE_A_COPIE = "R01";
	public static final String COD_TIPO_TESSERA_RICARICABILE_A_VALORE = "R02";
	public static final String COD_TIPO_TESSERA_RICARICABILE_OMAGGIO = "OM2";
	public static final String COD_TIPO_TESSERA_CONSEGNA_SPECIALE = "S01";
	public static final String DESC_TIPO_TESSERA_ABBONATO = "Abbonato";
	public static final String DESC_TIPO_TESSERA_FIDELITY_CARD = "Fidelity card";
	public static final String DESC_TIPO_TESSERA_PASSEPARTOUT = "Passepartout";
	public static final String DESC_TIPO_TESSERA_RICARICABILE = "Ricaricabile";
	public static final String DESC_TIPO_TESSERA_CONSEGNA_SPECIALE = "Consegna speciale(scuole)";
	
	public static final String MAP_KEY_ABBONAMENTO = "ABB0";
	public static final String MAP_KEY_MINICARD = "MINI";
	public static final String MAP_KEY_CONSEGNA_MINICARD_COPIE = "MINC";
	public static final String MAP_KEY_CONSEGNA_MINICARD_VALORE = "MINV";
	public static final String MAP_KEY_STATO_TESSERA = "STAT";
	public static final String MAP_KEY_LISTINO_RICARICHE_COPIA = "LISC";
	public static final String MAP_KEY_LISTINO_RICARICHE_VALORE = "LISV";
	public static final String MAP_KEY_RICARICA_VALORE = "RICA";
	public static final String MAP_KEY_RICARICA_COPIE = "RICC";
	public static final String MAP_KEY_CONFERMA_RICARICA = "CORI";
	
	public static final String CHECKBOX_NAME = "dpeChkbx_";
	public static final String INPUT_TEXT_NAME_DIFFERENZE = "differenze";
	public static final String INPUT_TEXT_NAME_RESA = "reso";
	public static final String INPUT_TEXT_NAME_RESA_FUORI_VOCE = "resoFuoriVoce";
	public static final String INPUT_TEXT_NAME_COPIE = "quantitaRifornimento";
	public static final String INPUT_TEXT_NAME_NOTE = "noteVendita";
	public static final String INPUT_TEXT_NAME_PRENOTAZIONE = "prenotazione";
	public static final String INPUT_TEXT_NAME_DATA_SCADENZA_RICHIESTA = "dataScadenzaRichiesta";
	public static final String TIPO = "Tipo";
	public static final String IMG_FOLDER_NAME = "img";
	public static final String PDF_FOLDER_NAME = "pdf";
	public static final String SI = "igeriv.si";
	public static final String NO = "igeriv.no";
	public static final String STATO_PRONTO_PER_INVIO_SIGLA = "P";
	public static final String STATO_INVIATO_DL_SIGLA = "I";
	public static final String STATO_INSERITO = "igeriv.inserito";
	public static final String STATO_PRONTO_PER_INVIO = "igeriv.pronto.per.invio";
	public static final String STATO_INVIATO_DL = "igeriv.inviato";
	public static final String STATO_RECORD_ANNULLATO = "X";
	public static final int TIPO_OPERAZIONE_GESDIS = 1;
	public static final int INDICATORE_VALORIZZARE = 0;
	public static final int INDICATORE_NON_VALORIZZARE = 1;
	public static final int INDICATORE_CONTO_DEPOSITO = 2;
	public static final String VALORIZZARE = "igeriv.valorizzare";
	public static final String NON_VALORIZZARE = "igeriv.non.valorizzare";
	public static final String CONTO_DEPOSITO = "igeriv.conto.deposito";
	public static final int COD_STATO_EVASIONE_INSERITO = 0;
	public static final int COD_STATO_EVASIONE_PARZIALE = 1;
	public static final int COD_STATO_EVASIONE_COMPLETA = 2;
	public static final int COD_STATO_EVASIONE_PREOTAZIONE_FISSA = 3;
	public static final String STATO_EVASIONE_INSERITO = "igeriv.inserito";
	public static final String STATO_EVASIONE_PARZIALE = "igeriv.evasione.parziale";
	public static final String STATO_EVASIONE_COMPLETA = "igeriv.evasione.completa";
	public static final String STATO_PRENOTAZIONE_FISSA = "igeriv.prenotazione.fissa";
	public static final int COD_PROVENIENZA_RICHIESTA_CLIENTE = 1;
	public static final int COD_PROVENIENZA_RICHIESTA_EDICOLA = 2;
	public static final int COD_PROVENIENZA_RICHIESTA_AUTOMATICA = 3;
	public static final String PROVENIENZA_RICHIESTA_CLIENTE = "igeriv.provenienza.evasione.cliente";
	public static final String PROVENIENZA_RICHIESTA_EDICOLA = "igeriv.provenienza.evasione.edicola";
	public static final String PROVENIENZA_RICHIESTA_AUTOMATICA = "igeriv.provenienza.evasione.automatica";
	public static final int COD_TIPO_PAGAMENTO_IN_EDICOLA = 1;
	public static final int COD_TIPO_PAGAMENTO_ANTICIPATO = 2;
	public static final int COD_TIPO_PAGAMENTO_CONTRO_ASSEGNO = 3;
	public static final String LIST_COD_TIPO_FONDO_BOLLA = "1,2,8,10,11";
	public static final String TIPO_PAGAMENTO_IN_EDICOLA = "igeriv.tipo.pgto.edicola";
	public static final String TIPO_PAGAMENTO_ANTICIPATO = "igeriv.tipo.pgto.anticipato";
	public static final String TIPO_PAGAMENTO_CONTRO_ASSEGNO = "igeriv.tipo.pgto.contro.assegno";
	public static final int INDICATORE_RECORD_TRASMESSO = 1;
	public static final int INDICATORE_RECORD_TRASMESSO_TEMP = 5;
	public static final int INDICATORE_RECORD_NON_TRASMESSO_DL = 0;
	public static final int INDICATORE_RECORD_IN_TRASMISSIONE_DL = 1;
	public static final int INDICATORE_RECORD_TRASMESSO_DL = 2;
	public static final int INDICATORE_BOLLA_NON_TRASMESSA = 0;
	public static final int INDICATORE_BOLLA_DA_TRASMETTERE = 1;
	public static final int INDICATORE_BOLLA_IN_TRASMISSIONE = 2;
	public static final int INDICATORE_BOLLA_TRASMESSA = 3;
	public static final String BOLLA_NON_TRASMESSA = "igeriv.non.trasmessa";
	public static final String BOLLA_DA_TRASMETTERE = "igeriv.da.trasmettere";
	public static final String BOLLA_IN_TRASMISSIONE = "igeriv.in.trasmissione";
	public static final String BOLLA_TRASMESSA = "igeriv.trasmessa";
	public static final int COD_SI = 1;
	public static final int COD_NO = 0;
	public static final int SPUNTA_SI = 1;
	public static final int SPUNTA_NO = 0;
	public static final String SEQ_CLIENTI_EDICOLA = "SEQUENZA_9305";
	public static final String SEQ_UTENTI_EDICOLA = "SEQUENZA_9207";
	public static final String SEQ_VENDITE = "SEQUENZA_9625";
	public static final String SEQ_TESSERE = "seq_9518";
	public static final String SEQ_ABBONATI = "seq_9515";
	public static final String SEQ_TIPOLOGIA_MINICARD = "seq_9851";
	public static final String RTAE_SUCCESS = "success";
	public static final String RTAE_EXCEPTION = "rtaeException";
	public static final String START_EXCEPTION_TAG = "<" + RTAE_EXCEPTION + ">";
	public static final String END_EXCEPTION_TAG = "</" + RTAE_EXCEPTION + ">";
	public static final String ID_TABLE_TD_TOTAL = "totalCell";
	public static final String EDICOLA = "edicola";
	public static final String CLIENTE = "cliente";
	public static final String DL = "dl";
	public static final int TIPO_BOLLA_RESA = 0;
	public static final int TIPO_BOLLA_RESA_FUORI_VOCE = 1;
	public static final int TIPO_BOLLA_RESA_RICHIAMO_PERSONALIZZATO = 2;
	public static final int TIPO_FONDO_BOLLA = 3;
	public static final int TIPO_BOLLA_DETTAGLIO = 4;
	public static final String URI_CENTRO_SERVIZI = "cs";
	public static final String URI_RIVENDITA = "riv";
	public static final String URI_DL = "dl";
	public static final String URI_EDITORE = "ed";
	public static final String URI_EDICOLIAMO = "edicoliamo";
	public static final String USER_ID = "userId";
	public static final String DECIMAL_FORMAT_PATTERN = "###.###.##0,00";
	public static final String DEFAULT_DECIMAL_FORMAT_PATTERN = "#0.00";
	public static final int TIPO_UTENTE_EDICOLA = 1;
	public static final int TIPO_UTENTE_EDITORE = 2;
	public static final int TIPO_UTENTE_DL = 3;
	public static final int TIPO_UTENTE_CS = 4;
	public static final int TIPO_UTENTE_VENDITE_CARD = 5;
	public static final int TIPO_UTENTE_REST = 6;
	public static final int TIPO_UTENTE_CLIENTE = 7;
	public static final double ENCODE_FACTOR = 38.6782;
	public static final int STATO_MESSAGGIO_NUOVO = 0;
	public static final int STATO_MESSAGGIO_INVIATO = 1;
	public static final int TIPO_MESSAGGIO_NORMALE = 0;
	public static final int TIPO_MESSAGGIO_CON_AVVISO = 1;
	public static final int TIPO_MESSAGGIO_IMMEDIATO = 2;
	public static final int COD_MESSAGGIO_NON_LETTO = 0;
	public static final int COD_MESSAGGIO_LETTO = 1;
	public static final int COD_TUTTE_LE_EDICOLE = 0;
	public static final String THOUSANDS_SPERATOR_IT = ".";
	public static final String DECIMAL_SPERATOR_IT = ",";
	public static final int STATIC_CONTENT_EXPIRE_DAYS = 1;
	public static final String VENDITE_SUFFIX = "V";
	public static final int COD_DL_MENTA = 1;
	public static final int COD_GIORNO_FESTIVITA = 8;
	public static final int COD_CAUSALE_ABBONAMENTI = 1;
	public static final int COD_RICARICA_EDITORE_TESSERA_NUOVA = 0;
	public static final int COD_RICARICA_EDITORE = 1;
	public static final int COD_RICARICA_RIVENDITA = 2;
	
	public static final String PRINT_XSL_DIR = "xsl";
	public static final String BILL_XSL = PRINT_XSL_DIR + "bill.xsl";
	public static final int COD_RAGGRUPPAMENTO_DATA_PRODOTTO = 1;
	public static final int COD_RAGGRUPPAMENTO_DATA_PRODOTTO_DL = 2;
	public static final int COD_RAGGRUPPAMENTO_DATA_PRODOTTO_DL_RIVENDITA = 3;
	public static final int COD_RAGGRUPPAMENTO_DATA_PRODOTTO_RIVENDITA = 4;
	public static final int COD_RAGGRUPPAMENTO_DATA_EDITORE = 5;
	public static final int COD_RAGGRUPPAMENTO_DATA_EDITORE_PRODOTTO_TIPO_TESSERA = 6;
	public static final int COD_RAGGRUPPAMENTO_DATA_EDITORE_PRODOTTO_RIVENDITA = 7;
	public static final int COD_RAGGRUPPAMENTO_DATA_EDITORE_PRODOTTO_ABBONATO = 8;
	public static final int COD_RAGGRUPPAMENTO_DATA_EDITORE_TIPO_TESSERA = 9;
	public static final String HEADER_USER_AGENT = "User-Agent";
	public static final int COD_MESSAGGIO_CODIFICATO_PUBBLICAZIONE_ESAURITA = 2;
	public static final int DEFAULT_GIORNI_SCADENZA_RICHIESTA = 60;
	public static final String SUFFIX_ROLE_ADV = "_ADV";
	public static final String MAP_PARAM_KEY_ID_TESSERA = "idTessera";
	public static final String MAP_PARAM_KEY_ID_PRODOTTO = "idProdotto";
	public static final String MAP_PARAM_KEY_DATA = "dateNow";
	public static final String MAP_PARAM_KEY_BARCODE = "barcode";
	public static final String MAP_PARAM_KEY_TIPOLOGIA_MINICARD = "tipologiaMinicard";
	public static final String MAP_PARAM_KEY_ID_EDITORE = "idEditore";
	public static final String MAP_PARAM_KEY_ID_RIVENDITA = "idRivendita";
	public static final String MAP_PUOI_RICARICARE_FLAG = "puoiRicaricare";
	public static final String MAP_MODALITA = "modalita";
	public static final String MAP_PARAM_KEY_IMPORTO = "importo";
	public static final String MAP_PARAM_KEY_COPIE = "copie";
	public static final String MAP_PARAM_KEY_LIST_IDTN = "list_idtn";
	public static final String MAP_PARAM_KEY_PUBBLICAZIONE = "pubblicazione";
	public static final String MAP_PARAM_KEY_CORRISPONDENZA = "corrispondenza";
	public static final String MAP_PARAM_KEY_LIST_CORRISPONDENZE = "list_corrispondenze";
	public static final String MAP_PARAM_KEY_SET_EDITORI_SENZA_ACCETTAZIONE_PRIVACY = "list_nomi_editori_senza_accettazione_privacy";
	public static final String MAP_PARAM_KEY_ID_EDITORI_SET = "set_id_editori";
	public static final String MAP_PARAM_KEY_COPIE_CONSEGNARE = "copie_consegnare";
	public static final String MAP_PARAM_KEY_DATA_USCITA = "data_uscita";
	public static final String MAP_PARAM_KEY_NUOVO_ABBONAMENTO = "nuovoAbbonamento";
	public static final String MAP_PARAM_KEY_IMPORTO_RICARICA = "importoRicarica";
	public static final String MAP_PARAM_KEY_COPIE_ACQUISTATE = "copieAcquistate";
	public static final String MAP_PARAM_KEY_TITOLO = "titolo";
	public static final String MAP_PARAM_KEY_STATO = "stato";
	public static final String MAP_PARAM_KEY_CREDITO_RESIDUO = "creditoResiduo";
	public static final String MAP_PARAM_KEY_COPIE_RESIDUE = "copieResidue";
	public static final String MAP_PARAM_KEY_DATA_INIZIO_ABBONAMENTO = "dataInizioAbbonamento";
	public static final String MAP_PARAM_KEY_DATA_FINE_ABBONAMENTO = "dataFineAbbonamento";
	public static final String MAP_PARAM_KEY_EMAIL_ABBONATO = "emailAbbonato";
	public static final String MAP_PARAM_KEY_COPIE_TOTALI = "copieTotali";
	public static final String MAP_PARAM_KEY_DL_HAS_ACCETTAZIONE_PRIVACY = "hasAccettazionePrivacy";
	
	public static final String TIPO_PERIODICO = "P";
	public static final String TIPO_QUOTIDIANO = "Q";
	public static final String TIPO_PERIODICITA_MENSILE = "M";
	public static final String TIPO_PERIODICITA_QUOTIDIANO = "Q";
	public static final String TIPO_PERIODICITA_SETTIMANALE = "S";
	public static final String TIPO_PERIODICITA_ALTRO = "X";
	public static final String PREFIX_EXPORT_FILE_DL = "AC";
	public static final String PREFIX_EXPORT_FILE_RIEP_DL = "ACR";
	public static final String PREFIX_EXPORT_FILE_PRODOTTO_DL = "ACP";
	public static final String PREFIX_EXPORT_FILE_ED = "CM";
	public static final String PREFIX_EXPORT_FILE_STAMPA_TESSERE = "STAMPA_TESSERE";
	public static final String PREFIX_EXPORT_FILE_RICARICHE_DL = "RI";
	public static final String WEB_INF_DIR = "/WEB-INF";
	public static final String XSL_RITIRI = "ritiri.xsl";
	public static final String XSL_RICARICHE = "ricariche.xsl";
	public static final String XSL_RITIRI_EDITORE = "ritiri_editore.xsl";
	public static final int TIMEOUT_EXPORT_JOB = 50;
	public static final String SYSTEM_PROP_WEBINF_DIR = "webinf_dir";
	public static final int STATO_ESPORTAZIONE_INVIATO = 0;
	public static final int STATO_ESPORTAZIONE_CONCLUSO_SUCCESSO = 1;
	public static final int STATO_ESPORTAZIONE_CONCLUSO_ERRORE = 2;
	public static final int STATO_ESPORTAZIONE_CONCLUSO_WARNING = 3;
	public static final int STATO_IMPORTAZIONE_SPOSTAMENTO_CONCLUSO_SUCCESSO = 1;
	public static final int STATO_IMPORTAZIONE_SPOSTAMENTO_CONCLUSO_WARNING = 2;
	public static final int STATO_IMPORTAZIONE_SPOSTAMENTO_CONCLUSO_ERRORE = 3;
	public static final String DATA_VALIDITA_INIZIALE = "01-01-2000";
	public static final String DATA_VALIDITA_FINALE = "31-12-9999";
	public static final String RITIRO_OBBLIGATORIO_PRESSO_RIVENDITA_PREFERITA = "F";
	public static final String FILE_INPUT_PATTERN = "^CT([\\d]{2})([\\d]{2})([\\d]{2})([\\d]{4}).txt$";
	public static final int COD_OPERAZIONE_IMPORTAZIONE = 0;
	public static final int COD_OPERAZIONE_IMPORTAZIONE_BATCH_DETTAGLI = 1;
	public static final int COD_OPERAZIONE_MODIFICA_ONLINE = 2;
	public static final int TIPO_TESSERA_NORMALE = 0;
	public static final int TIPO_TESSERA_FUTURA = 1;
	public static final int TIPO_TESSERA_RICARICABILE = 2;
	public static final int TIPO_TESSERA_RICARICABILE_CLIENTE = 3;
	public static final int COD_TIPOLOGIA_MINICARD_VALORE = 99999999;
	public static final int COD_TIPOLOGIA_RIMBORSO = 99999998;
	public static final String HTML_LINE_BREAK = "<BR>";
	public static final String STAMPA_TESSERE_EXPORT_DIR = "stampaTessere";
	public static final String LEVEL_NORMAL = "0";
	public static final String LEVEL_WARNING = "1";
	public static final String LEVEL_ERROR = "2";
	public static final String LEVEL_FATAL = "3";
	public static final int COD_TIPO_OPERAZIONE_CONSEGNA = 0;
	public static final int COD_TIPO_OPERAZIONE_RICARICA = 1;
	public static final int TIPO_AGGIO_NORMALE = 0;
	public static final int TIPO_AGGIO_TESSERE_STAMPATE = 1;
	public static final int COD_MOTIVO_STAMPA_NUOVA_TESSERA = 1;
	public static final int COD_MOTIVO_RISTAMPA_TESSERA = 2;
	public static final int COD_MOTIVO_SOSTITUZIONE_TESSERA = 3;
	public static final int COD_TIPO_NOTIFICA_EMAIL = 0;
	public static final int COD_TIPO_NOTIFICA_SMS = 1;
	public static final int ONE_YEAR_DAYS = 365;
	public static final int COPIE_RESIDUE_LIMITE_DEFAULT_PER_MESSAGGIO_PUOI_RICARICARE = 3;
	public static final double RIMBORSO_COPIE_FACTOR = 0.80d;
	public static final int COD_FIEG_MENTA = 177;
	public static final int COD_FIEG_CDL = 226;
	
	
	public static final String OS = "os";
	public static final String OS_VERSION = "os_version";
	public static final String DEVICE_ID = "device_id";
	public static final String CLIENT_VERSION = "client_version";
	public static final String REST_AUTH_NAME = "rest_auth_name";
	public static final String APP_NAME = "app_name";
	public static final String TOKEN_AUTH_FROM_IGERIV = "5O!up1nw948&y?oQ40kef0)Z=l30<GiAoi572vu7";
	
	public static enum StatoTessera {
        ATTIVA(STATO_TESSERA_ATTIVA),
        ANNULLATA(STATO_TESSERA_ANNULLATA),
        EMESSA_NON_ATTIVA(STATO_TESSERA_EMESSA_NON_ATTIVA),
        SOSPESA(STATO_TESSERA_SOSPESA);

        private String stato;

        private StatoTessera(String stato) {
            this.stato = stato;
        }

		public static StatoTessera getStatoTessera(String stato) {
        	StatoTessera statoTessera = null;
            StatoTessera[] responses = StatoTessera.values();
            for (StatoTessera st : responses) {
                if (st.stato.equalsIgnoreCase(stato)) {
                	statoTessera = st;
                	break;
                }
            }
            return statoTessera;
        }
    }
	
	/**
     * Puts all public static fields via introspection into the resulting Map.
     * Uses the name of the field as key to reference it's in the Map.
     *
     * @return a Map of field names to field values of
     *         all public static fields of this class
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
     */
	private static Map<String, Object> createNameToValueMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        Field[] publicFields = RtaeConstants.class.getFields();
        for (int i = 0; i < publicFields.length; i++) {
            Field field = publicFields[i];
            String name = field.getName();
            try {
                result.put(name, field.get(null));
            } catch (Exception e) {
                System.err.println("Initialization of Constants class failed!");
                e.printStackTrace(System.err);
            }
        }
        Class<?>[] classes = RtaeConstants.class.getClasses();
        for (Class<?> clazz : classes) {
	        Field[] pf = clazz.getFields();
	        for (int i = 0; i < pf.length; i++) {
	            Field field = pf[i];
	            String name = field.getName();
	            try {
	                result.put(name, field.get(null));
	            } catch (Exception e) {
	                System.err.println("Initialization of Constants class failed!");
	                e.printStackTrace(System.err);
	            }
	        }
        }
        return result;
    }
	
	/**
     * Gets the Map of all public static fields.
     * The field name is used as key for the value of the field itself.
     *
     * @return the Map of all public static fields
     */
    public static Map<String, Object> getNameToValueMap() {
        return nameToValueMap;
    }

}
