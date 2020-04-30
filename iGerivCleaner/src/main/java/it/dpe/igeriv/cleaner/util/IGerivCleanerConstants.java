package it.dpe.igeriv.cleaner.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe di costanti per iGeriv.
 * 
 * @author romanom
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class IGerivCleanerConstants {
    public static final String ID = "IGerivConstants";
    private static Map nameToValueMap = createNameToValueMap();
	public static final Integer MAX_TABLE_HEIGHT = 350;
	public static final Integer TABLE_ROW_HEIGHT = 60;
	public static final int PROVENIENZA_CLIENTE = 1;
	public static final int PROVENIENZA_EDICOLA_PER_CONTO_CLIENTE = 2;
	public static final int PROVENIENZA_AUTOMATICA = 3;
	public static final int OPERAZIONE_VARIAZIONE_WEB = 3;
	public static final int INDICATORE_DOCUMENTO_RICEVUTO = 0;
	public static final int INDICATORE_DOCUMENTO_EMESSO = 1;
	public static final int INDICATORE_DOCUMENTO_INTERNO = 2;
	public static final int INDICATORE_RICARICA_GAZZETTA_TESSERA_NUOVA = 0;
	public static final int INDICATORE_RICARICA_GAZZETTA = 1;
	public static final int INDICATORE_RICARICA_RIVENDITA = 2;
	public static final int INDICATORE_RECORD_TRASFERITO = 1;
	public static final int INDICATORE_RECORD_NON_TRASFERITO = 0;
	public static final int INDICATORE_RECORD_TRASFERITO_TEMP = 2;
	public static final int COD_AUTORIZZAZIONE_RICARICA_TUTTI = 0;
	public static final int COD_AUTORIZZAZIONE_RICARICA_GAZZETTA = 1;
	public static final int COD_RICHIEDERE_DIFFERENZA_DL_NO = 0;
	public static final int COD_RICHIEDERE_DIFFERENZA_DL_SI_RICHIEDERE = 1;
	public static final int COD_RICHIEDERE_DIFFERENZA_DL_SI_RICHIESTO = 2;
	public static final int COD_ABILITATO = 1;
	public static final int COD_NON_ABILITATO = 0;
	public static final int PERIODICITA_QUOTIDIANO = 1;
	public static final int PRENOTAZIONI_INSERITE_O_PARZIALMENTE_EVASE = 0;
	public static final int PRENOTAZIONI_INSERITE = 1;
	public static final int PRENOTAZIONI_EVASE = 2;
	public static final int PRENOTAZIONI_FISSE = 3;
	public static final int COD_AUTORIZZAZIONE_RICARICA_EDITORE = 1;
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int COD_SI = 1;
	public static final int COD_NO = 0;
	public static final int SPUNTA_SI = 1;
	public static final int SPUNTA_NO = 0;
	public static final int TIPO_OPERAZIONE_GESDIS = 1;
	public static final int TIPO_OPERAZIONE_INFORETE = 0;
	public static final int INDICATORE_VALORIZZARE = 0;
	public static final int INDICATORE_NON_VALORIZZARE = 1;
	public static final int INDICATORE_CONTO_DEPOSITO = 2;
	public static final int COD_STATO_CONTESTAZIONI_RESA_DA_LAVORARE = 0;
	public static final int COD_STATO_CONTESTAZIONI_RESA_APPROVATA = 1;
	public static final int COD_STATO_CONTESTAZIONI_RESA_RIFIUTATA = 2;
	public static final int COD_STATO_CONTESTAZIONI_RESA_ANNULLATA = 3;
	public static final int COD_STATO_EVASIONE_INSERITO = 0;
	public static final int COD_STATO_EVASIONE_PARZIALE = 1;
	public static final int COD_STATO_EVASIONE_COMPLETA = 2;
	public static final int COD_STATO_MANCANZE_INSERITO = 0;
	public static final int COD_STATO_MANCANZE_SOSPESO = 1;
	public static final int COD_STATO_MANCANZE_ACCREDITATA = 2;
	public static final int COD_STATO_MANCANZE_NON_ACCREDITATA = 3;
	public static final int COD_STATO_MANCANZE_PAREGGIATE = 5;
	public static final int COD_STATO_EVASIONE_PREOTAZIONE_FISSA = 3;
	public static final int COD_PROVENIENZA_RICHIESTA_CLIENTE = 1;
	public static final int COD_PROVENIENZA_RICHIESTA_EDICOLA = 2;
	public static final int COD_PROVENIENZA_RICHIESTA_AUTOMATICA = 3;
	public static final int COD_TIPO_PAGAMENTO_IN_EDICOLA = 1;
	public static final int COD_TIPO_PAGAMENTO_ANTICIPATO = 2;
	public static final int COD_TIPO_PAGAMENTO_CONTRO_ASSEGNO = 3;
	public static final int INDICATORE_RECORD_NON_TRASMESSO_DL = 0;
	public static final int INDICATORE_RECORD_IN_TRASMISSIONE_DL = 1;
	public static final int INDICATORE_RECORD_TRASMESSO_DL = 2;
	public static final int INDICATORE_BOLLA_NON_TRASMESSA = 0;
	public static final int INDICATORE_BOLLA_DA_TRASMETTERE = 1;
	public static final int INDICATORE_BOLLA_IN_TRASMISSIONE = 2;
	public static final int INDICATORE_BOLLA_TRASMESSA = 3;
	public static final int TIPO_BOLLA_RESA = 0;
	public static final int TIPO_BOLLA_RESA_FUORI_VOCE = 1;
	public static final int TIPO_BOLLA_RESA_RICHIAMO_PERSONALIZZATO = 2;
	public static final int TIPO_FONDO_BOLLA = 3;
	public static final int TIPO_BOLLA_DETTAGLIO = 4;
	public static final int TIPO_UTENTE_EDICOLA = 1;
	public static final int TIPO_UTENTE_CLIENTE_EDICOLA = 2;
	public static final int TIPO_UTENTE_DL = 3;
	public static final int TIPO_UTENTE_ADM_SITO = 4;
	public static final int STATO_MESSAGGIO_NUOVO = 0;
	public static final int STATO_MESSAGGIO_INVIATO = 1;
	public static final int TIPO_MESSAGGIO_NORMALE = 0;
	public static final int TIPO_MESSAGGIO_CON_AVVISO = 1;
	public static final int TIPO_MESSAGGIO_IMMEDIATO = 2;
	public static final int COD_MESSAGGIO_NON_LETTO = 0;
	public static final int COD_MESSAGGIO_LETTO = 1;
	public static final int COD_TUTTE_LE_EDICOLE = 0;
	public static final int COD_EDICOLA_SINGOLA = 1;
	public static final int COD_GIRO_TIPO = 2;
	public static final int COD_ZONA_TIPO = 3;
	public static final int STATIC_CONTENT_EXPIRE_DAYS = 1;
	public static final int COD_DL_MENTA = 1;
	public static final int COD_GIORNO_FESTIVITA = 8;
	public static final int STATO_ESPORTAZIONE_INVIATO = 0;
	public static final int STATO_ESPORTAZIONE_CONCLUSO_SUCCESSO = 1;
	public static final int STATO_ESPORTAZIONE_CONCLUSO_ERRORE = 2;
	public static final int COD_RAGGRUPPAMENTO_PERIODICITA = 1;
	public static final int COD_RAGGRUPPAMENTO_ARGOMENTO = 2;
	public static final int COD_MESSAGGIO_CODIFICATO_ATTENDO_RIFORNIMENTO_AGENZIA = 1;
	public static final int COD_MESSAGGIO_CODIFICATO_PUBBLICAZIONE_ESAURITA = 2;
	public static final int COD_MESSAGGIO_CODIFICATO_ARRETRATO = 3;
	public static final int COD_TIPO_IMMAGINE_RESA = 1;
	public static final int COD_TIPO_IMMAGINE_MINIATURA = 2;
	public static final int COD_TIPO_IMMAGINE_MINIATURA_EDICOLA = 3;
	public static final int COD_TIPO_IMMAGINE_MINIATURA_PNE_EDICOLA = 4;
	public static final int COD_TIPO_IMMAGINE_PRODOTTI_VARI_DL = 5;
	public static final int COD_TIPO_IMMAGINE_PDF = 6;
	public static final int COD_TIPO_PERIODICITA_QUOTIDIANO = 1;
	public static final int COD_PERIODICITA_QUOTIDIANO = 1;
	public static final int COD_TIPO_MOVIMENTO_FORNITURA_QUOTIDIANI = 16;
	public static final int COD_TIPO_MOVIMENTO_RESA_QUOTIDIANI = 17;
	public static final int COD_TIPO_MOVIMENTO_FORNITURA_PERIODICI = 3;
	public static final int COD_TIPO_MOVIMENTO_RESA_PERIODICI = 4;
	public static final int COD_TRIMETRE_PRECEDENTE = 0;
	public static final int COD_TRIMETRE_SUCCESSIVO = 1;
	public static final int TIPO_STATISTICA_DETTAGLIO_FORNITO = 0;
	public static final int TIPO_STATISTICA_DETTAGLIO_RIFORNIMENTI = 1;
	public static final int CDDT_MAX_VALUE = 99999999;
	public static final int COD_INIZIO_FORNITORI_NON_DL = 10000;
	public static final int NUM_MAX_COPERTINE_PRECEDENTI_PER_RIFORNIMENTI = 4;
	public static final int COD_PERIODICITA_ENCICLOPEDIA = 3;
	public static final int COD_PERIODICITA_NUMERO_UNICO = 0;
	public static final int COD_TIPO_FONDO_BOLLA_RIFORNIMENTI = 2;
	public static final int TIPO_RECORD_FONDO_BOLLA_RIFORNIMENTI = 2;
	public static final int COD_MAGAZZINO_ESTERNO = 0;
	public static final int COD_MAGAZZINO_INTERNO = 1;
	public static final int TIPO_RECORD_FONDO_BOLLA_CONTO_DEPOSITO = 9;
	public static final int DEFAULT_NUM_GIORNI_CANCELLA_PRODOTTI_VARI = 9999;
	
	public static final double ENCODE_FACTOR = 38.6782;
	
	public static final Integer DEFAULT_GIORNI_SCADENZA_RICHIESTA = 7;
	public static final Integer NUM_MAX_STATISTICA_VISUALIZZARE = 5;
	public static final Integer TIPO_PUBBLICAZIONE_QUOTIDIANO = 1;
	public static final Integer TIPO_PUBBLICAZIONE_PERIODICO = 2;
	public static final Integer CDL_CODE = 226;
	public static final Integer STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_ERRORE = -1;
	public static final Integer STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_SUCCESS = 0;
	public static final Integer STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_WARNING = 1;
	public static final Integer COD_NAZIONE_ITALIA = 1;
	public static final Integer CODICE_CAUSALE_ACQUISTO = 1;
	public static final Integer CODICE_CAUSALE_VENDITA = 2;
	public static final Integer COD_CLIENTE_ANONIMO = 0;
	public static final Integer COD_PROCESSO_INFORIV_IMPORT = 100;
	public static final Integer COD_PROCESSO_EMAIL_RECEIVER = 200;
	public static final Integer COD_PROCESSO_IMPORT_IMMAGINI = 300;
	public static final Integer COD_PARAMETRO_EDICOLA_BEEP = 1;
	public static final Integer COD_PARAMETRO_EDICOLA_GIORNI_STORICO_BOLLE_PRODOTTI_VARI = 2;
	public static final Integer COD_PARAMETRO_EDICOLA_ABILITA_RESTO = 3;
	public static final String LOGO_DPE_IMG_NAME = "logo_dpe.gif";
	public static final String COD_DPE = "9999";
	public static final String IGERIV_INFORIV_PWD = "grvrfv2965dpe2012";
	public static final String IGERIV_INFORIV_SALT = "720£9yt$";
	public static final String STATO_CONSEGNA_SOSPESA = "S";
	public static final String STATO_CONSEGNA_ANNULLATA = "A";
	public static final String FINE_RECORD = "*";
	public static final String RICARICA_PAGAMENTO = "P";
	public static final String RICARICA_OMAGGIO = "O";
	public static final String COD_AUTORIZZAZIONE_RICARICA_EDITORE_TEXT = "Editore";
	public static final String RICARICA_PAGAMENTO_TEXT = "A&nbsp;Pagamento";
	public static final String RICARICA_OMAGGIO_TEXT = "Omaggio";
	public static final String OPERAZIONE_IMPORTAZIONE_GAZZETTA_TEXT = "Importazione&nbsp;Gazzetta";
	public static final String OPERAZIONE_RICARICA_GAZZETTA_TEXT = "Ricarica&nbsp;Gazzetta";
	public static final String OPERAZIONE_RICARICA_RIVENDITA_TEXT = "Ricarica&nbsp;Rivendita";
	public static final String OPERAZIONE_VARIAZIONE_WEB_TEXT = "Variazione&nbsp;WEB";
	public static final String COD_AUTORIZZAZIONE_RICARICA_TUTTI_TEXT = "Tutti";
	public static final String COD_AUTORIZZAZIONE_RICARICA_GAZZETTA_TEXT = "Gazzetta";
	public static final String RICARICA_GAZZETTA_TESSERA_NUOVA_TEXT = "Ricarica&nbsp;Nuova&nbsp;Gazzettta";
	public static final String RICARICA_GAZZETTA_TEXT = "Ricarica&nbsp;Gazzettta";
	public static final String RICARICA_RIVENDITA_TEXT = "Ricarica&nbsp;Rivendita";
	public static final String EXPORT_FILE_NAME_MODEL = "%s%s%s.txt";
	public static final String BARCODE_VENDITE_DEFAULT = "9771590649016";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_POWER_USER = "ROLE_POWER_USER";
	public static final String ROLE_USER_EDICOLA = "ROLE_USER_EDICOLA";
	public static final String ROLE_IGERIV_BASE = "ROLE_IGERIV_BASE";
	public static final String ROLE_IGERIV_BASE_ADMIN = "ROLE_IGERIV_BASE_ADMIN";
	public static final String ROLE_IGERIV_DL = "ROLE_IGERIV_DL";
	public static final String ROLE_IGERIV_DL_ADV = "ROLE_IGERIV_DL_ADV";
	public static final String ROLE_IGERIV_LITE = "ROLE_IGERIV_LITE";
	public static final String ROLE_IGERIV_ADV_1 = "ROLE_IGERIV_ADV_1";
	public static final String ROLE_IGERIV_ADV_2 = "ROLE_IGERIV_ADV_2";
	public static final String ROLE_IGERIV_ADV_3 = "ROLE_IGERIV_ADV_3";
	public static final String ROLE_IGERIV_ADV_4 = "ROLE_IGERIV_ADV_4";
	public static final String ROLE_IGERIV_ADV_5 = "ROLE_IGERIV_ADV_5";
	public static final String ROLE_IGERIV_CLIENTE_EDICOLA = "ROLE_IGERIV_CLIENTE_EDICOLA";
	public static final String ROLE_CRON = "ROLE_CRON";
	public static final String BLANK = "";
	public static final String SECURITY_ACCESS_VALUE = "disabled";
	public static final String HASH = "#";
	public static final String STRING_TRUE = "true";
	public static final String EMPTY_SPACE_STRING = " ";
	public static final String STAR_SYMBOL = "*";
	public static final String SELECTED = "on";
	public static final String UNSELECTED = "off";
	public static final String SLASH = "/";
	public static final String QUESTION_MARK = "?";
	public static final String SEMI_COLON = ";";
	public static final String EURO_SIGN_ASCII = "\u20AC";
	public static final String EURO_SIGN_HTML = "&euro;";
	public static final String EURO_SIGN_DECIMAL = "&#8364;";
	public static final String PERIOD_ASCII = "\u002e"; 
	public static final String UNDERSCORE = "_";
	public static final String IT = "it";
	public static final String PT = "pt";
	public static final String EN = "en";
	public static final String ES = "es";
	public static final String EURO_SIGN = "\u20AC";
	public static final String EURO_SIGN_TEXT = "Euro";
	public static final String J_SPRING_SECURITY_CHECK = "j_spring_security_check";
	public static final String REDIRECT = "redirect";
	public static final String ACTION_ORDINI = "ordini";
	public static final String ACTION_RICHIESTE_RIFORNIMENTI = "richiesteRifornimenti";
	public static final String ACTION_RICHIESTE_VARIAZIONI = "richiesteVariazioni";
	public static final String ACTION_RICHIESTE_RIFORNIMENTI_EDIT = "richiesteRifornimentiEdit";
	public static final String ACTION_VIEW_RICHIESTE_RIFORNIMENTI = "viewRichiesteRifornimenti";
	public static final String ACTION_NUOVO_CLIENTE = "nuovoClienteEdicola";
	public static final String ACTION_NUOVO_UTENTE_EDICOLA = "nuovoUtenteEdicola";
	public static final String ACTION_SUCCESS_LOCALITA = "successLocalita";
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
	public static final String ACTION_VENDITE_REPORT_PDF = "venditeReportPdf";
	public static final String ACTION_ORDINI_CLIENTI_PDF = "ordiniClientiReportPdf";
	public static final String ACTION_REPORT_CONTO_DEPOSITO = "reportContoDeposito";
	public static final String ACTION_EMAIL_INVIATI = "emailInviati";
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
	public static final String SESSION_VAR_APP_NAME = "appName";
	public static final String SESSION_ID = "sessionId";
	public static final String SESSION_VAR_URL_PATH = "urlPath";
	public static final String SESSION_VAR_COD_FIEG_DL = "codFiegDl";
	public static final String SESSION_VAR_DL = "dl";
	public static final String SESSION_VAR_PROFILI_EDICOLA = "profiliEdicola";
	public static final String SESSION_VAR_EMAIL_DL = "emailDl";
	public static final String SESSION_VAR_EMAIL_DL_LIST = "emailDlList";
	public static final String SESSION_VAR_EMAIL_DL_JSON = "emailDlJson";
	public static final String SESSION_VAR_REQUEST_IP_ADDRESS = "requestIpAddress";
	public static final String SESSION_VAR_TIMEOUT_MINUTES = "timeout";
	public static final String SESSION_VAR_BROWSER_NAME = "browserName";
	public static final String SESSION_VAR_CDL_CODE = "CDL_CODE";
	public static final String SESSION_VAR_TIPO_CONTROLLO_PUBBLICAZIONI_RESPINTE = "tipoControlloPubblicazioniRespinte";
	public static final String SESSION_VAR_PARAMETRO_EDICOLA = "paramEdicola";
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
	public static final String SI = "igeriv.si";
	public static final String NO = "igeriv.no";
	public static final String VALORIZZARE = "igeriv.valorizzare";
	public static final String NON_VALORIZZARE = "igeriv.non.valorizzare";
	public static final String CONTO_DEPOSITO = "igeriv.conto.deposito";
	public static final String STATO_TESSERA_ATTIVA = " ";
	public static final String STATO_TESSERA_ANNULLATA = "A";
	public static final String STATO_TESSERA_EMESSA_NON_ATTIVA = "E";
	public static final String STATO_TESSERA_SOSPESA = "S";
	public static final String STATO_TESSERA_ATTIVA_TEXT = "Attiva";
	public static final String STATO_TESSERA_SOSPESA_TEXT = "Sospesa";
	public static final String STATO_TESSERA_EMESSA_NON_ATTIVA_TEXT = "Emessa&nbsp;Non&nbsp;Attiva";
	public static final String STATO_TESSERA_ANNULLATA_TEXT = "Annullata";
	public static final String STATO_PRONTO_PER_INVIO_SIGLA = "P";
	public static final String STATO_INVIATO_DL_SIGLA = "I";
	public static final String STATO_INSERITO = "igeriv.inserito";
	public static final String STATO_PRONTO_PER_INVIO = "igeriv.pronto.per.invio";
	public static final String STATO_INVIATO_DL = "igeriv.inviato";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INSERITO = "N";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_PRONTO_PER_INVIO = "P";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INVIATO = "I";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_NON_ANCORA_ELABORATA = "N";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA = "E";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_NON_EVADIBILE = "F";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INVIATO_AL_DL = "I";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA_PARZIALMENTE = "S";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_SCADUTA = "O";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_NON_ANCORA_ELABORATA_TEXT = "igeriv.richiesta.non.acora.elaborata";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA_TEXT = "igeriv.richiesta.evasa";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_NON_EVADIBILE_TEXT = "igeriv.richiesta.non.evadibile";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INVIATO_AL_DL_TEXT = "igeriv.richiesta.inviata.dl";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA_PARZIALMENTE_TEXT = "igeriv.richiesta.evasa.parzilemente";
	public static final String STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_SCADUTA_TEXT = "igeriv.richiesta.scaduta";
	public static final String STATO_EVASIONE_INSERITO = "igeriv.inserito";
	public static final String STATO_EVASIONE_PARZIALE = "igeriv.evasione.parziale";
	public static final String STATO_EVASIONE_COMPLETA = "igeriv.evasione.completa";
	public static final String STATO_PRENOTAZIONE_FISSA = "igeriv.prenotazione.fissa";
	public static final String STATO_MANCANZE_INSERITO_TEXT = "igeriv.inserito";
	public static final String STATO_MANCANZE_SOSPESO_TEXT = "igeriv.sospeso";
	public static final String STATO_MANCANZE_ACCREDITATA_TEXT = "igeriv.accreditata";
	public static final String STATO_MANCANZE_NON_ACCREDITATA_TEXT = "igeriv.non.accreditata";
	public static final String STATO_MANCANZE_PAREGGIATE_TEXT = "igeriv.pareggiata";
	public static final String STATO_CONTESTAZIONI_RESA_DA_LAVORARE_TEXT = "igeriv.da.lavorare";
	public static final String STATO_CONTESTAZIONI_RESA_APPROVATA_TEXT = "igeriv.approvata";
	public static final String STATO_CONTESTAZIONI_RESA_RIFIUTATA_TEXT = "igeriv.rifiutata";
	public static final String STATO_CONTESTAZIONI_RESA_ANNULLATA_TEXT = "igeriv.annullata";
	public static final String PROVENIENZA_RICHIESTA_CLIENTE = "igeriv.provenienza.evasione.cliente";
	public static final String PROVENIENZA_RICHIESTA_EDICOLA = "igeriv.provenienza.evasione.edicola";
	public static final String PROVENIENZA_RICHIESTA_AUTOMATICA = "igeriv.provenienza.evasione.automatica";
	public static final String LIST_COD_TIPO_FONDO_BOLLA = "1,2,8,10,11";
	public static final String TIPO_PAGAMENTO_IN_EDICOLA = "igeriv.tipo.pgto.edicola";
	public static final String TIPO_PAGAMENTO_ANTICIPATO = "igeriv.tipo.pgto.anticipato";
	public static final String TIPO_PAGAMENTO_CONTRO_ASSEGNO = "igeriv.tipo.pgto.contro.assegno";
	public static final String BOLLA_NON_TRASMESSA = "igeriv.non.trasmessa";
	public static final String BOLLA_DA_TRASMETTERE = "igeriv.da.trasmettere";
	public static final String BOLLA_IN_TRASMISSIONE = "igeriv.in.trasmissione";
	public static final String BOLLA_TRASMESSA = "igeriv.trasmessa";
	public static final String SEQ_CLIENTI_EDICOLA = "SEQUENZA_9305";
	public static final String SEQ_UTENTI_EDICOLA = "SEQUENZA_9207";
	public static final String SEQ_VENDITE = "SEQUENZA_9625";
	public static final String SEQ_RICHIESTE_RIFORNIMENTO = "SEQUENZA_9131";
	public static final String SEQ_CATEGORIE_EDICOLA = "SEQUENZA_9523";
	public static final String SEQ_SOTTOCATEGORIE_EDICOLA = "SEQUENZA_9524";
	public static final String SEQ_PRODOTTO_NON_EDITORIALE = "SEQUENZA_9506";
	public static final String SEQ_COD_DPE_WEB_DL = "SEQUENZA_9107";
	public static final String SEQ_STORICO_EMAIL = "SEQUENZA_9225";
	public static final String START_EXCEPTION_TAG = "<igerivException>";
	public static final String END_EXCEPTION_TAG = "</igerivException>";
	public static final String ID_TABLE_TD_TOTAL = "totalCell";
	public static final String EDICOLA = "edicola";
	public static final String CLIENTE = "cliente";
	public static final String DL = "dl";
	public static final String URI_CLIENTE_EDICOLA = "client";
	public static final String URI_ADMIN = "adm";
	public static final String URI_EDICOLA = "edicola";
	public static final String URI_DL = "dl";
	public static final String USER_ID = "userId";
	public static final String DECIMAL_FORMAT_PATTERN = "###.###.##0,00";
	public static final String DEFAULT_DECIMAL_FORMAT_PATTERN = "#0.00";
	public static final String THOUSANDS_SPERATOR_IT = ".";
	public static final String DECIMAL_SPERATOR_IT = ",";
	public static final String VENDITE_SUFFIX = "V";
	public static final String PRINT_XSL_DIR = "xsl";
	public static final String BILL_XSL = PRINT_XSL_DIR + "bill.xsl";
	public static final String HEADER_USER_AGENT = "User-Agent";
	public static final String FORMAT_IMAGE_JPEG = "JPEG";
	public static final String EMAIL_IGERIV = "noreply@geriv.it";
	public static final String ENCRYPTION_TOKEN = "dpetoken49iw!";
	public static final String GIACENZA_INIZIALE_RICHIAMO_CONTO = "---";
	public static final String SESSION_VAR_NETWORK_DETECTION_INTERVAL_MILLS = "networkDetectionIntervalMills";
	public static final String SESSION_VAR_SAVE_BOLLE_INTERVAL_MILLS = "autoSaveBolleIntervalMills";
	public static final String BROWSER_NAME_IE = "MSIE";
	public static final String BROWSER_NAME_CHROME = "CHROME";
	public static final String BROWSER_NAME_FIREFOX = "FIREFOX";
	public static final String BROWSER_NAME_SAFARI = "SAFARI";
	public static final String BROWSER_NAME_OPERA = "OPERA";
	public static final String BARCODE_FINALIZZA = "000000000000000001";
	public static final String PAGE_MONITOR_MAP = "pageMonitorMap";
	
	public static enum SQLType {
	    	ARRAY("ARRAY"),         
			BIGINT("BIGINT"),       
			BINARY("BINARY"),      
			BIT("BIT"),           
			BLOB("BLOB"),          
			BOOLEAN("BOOLEAN"),       
			CHAR("CHAR"),          
			CLOB("CLOB"),          
			DATALINK("DATALINK"),      
			DATE("DATE"),          
			DECIMAL("DECIMAL"),       
			DISTINCT("DISTINCT"),      
			DOUBLE("DOUBLE"),        
			FLOAT("FLOAT"),         
			INTEGER("INTEGER"),       
			JAVA_OBJECT("JAVA_OBJECT"),   
			LONGVARBINARY("LONGVARBINARY"), 
			LONGVARCHAR("LONGVARCHAR"),   
			NULL("NULL"),          
			NUMERIC("NUMERIC"),       
			OTHER("OTHER"),         
			REAL("REAL"),          
			REF("REF"),           
			SMALLINT("SMALLINT"),      
			STRUCT("STRUCT"),        
			TIME("TIME"),          
			TIMESTAMP("TIMESTAMP"),     
			TINYINT("TINYINT"),       
			VARBINARY("VARBINARY"),     
			VARCHAR("VARCHAR");

	        private String sqlType;

	        private SQLType(String sqlType) {
	            this.sqlType = sqlType;
	        }

			public static SQLType getSQLType(String type) {
				SQLType sqlType = null;
				SQLType[] responses = SQLType.values();
	            for (SQLType st : responses) {
	                if (st.sqlType.equalsIgnoreCase(type)) {
	                	sqlType = st;
	                	break;
	                }
	            }
	            return sqlType;
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
	private static Map createNameToValueMap() {
        Map result = new HashMap();
        Field[] publicFields = IGerivCleanerConstants.class.getFields();
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
        Class<?>[] classes = IGerivCleanerConstants.class.getClasses();
        for (Class clazz : classes) {
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
    public static Map getNameToValueMap() {
        return nameToValueMap;
    }
    
}
