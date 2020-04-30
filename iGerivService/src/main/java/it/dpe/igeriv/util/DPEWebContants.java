package it.dpe.igeriv.util;

/**
 * Classe di costanti web.
 * 
 * @author romanom
 *
 */
public class DPEWebContants {
	public static final String PAGE = "page";
	public static final String SUCCESS = "SUCCESS";
	public static final String REQUEST_LOCALE_PARAM = "request_locale";
	public static final String UPLOAD_DIR = "C:/temp/cv";
	public static final String BLANK = "";
	public static final String CV_EMAIL_SUBJECT = "Curriculum Vitae cadidato {0} {1} - Sito Web DPE";
	public static final String CV_EMAIL_BODY = "Nome: {0} <br>Cognome: {1} <br>Data de Nascita: {2}/{3}/{4} <br>Sesso: {5} <br>Nazionalitï¿½: {6} <br>Residenza (prov.): {7} <br>Residenza (comune): {8} <br>Domicilio (prov.) {9} <br>Domicilio (comune): {10} <br>Telefono: {11} <br>Cellulare: {12} <br>Email: {13} <br>Categoria Protetta: {14} <br>Educazione: {15} <br>Laurea: {16} <br>Area di Interessa 1: {17} <br>Area di Interessa 2: {18}";
	public static final String CV_CONFIRM_EMAIL_SUBJECT = "Candidatura Ricevuta - DPE Data Processing Engineering";
	public static final String CV_CONFIRM_EMAIL_BODY = "Gent. {0} {1} {2}<br><br>La ringraziamo per averci inviato la sua candidatura.<br><br>Le confermiamo l''avvenuta registrazione dei suoi dati nel nostro database; esamineremo il suo curriculum vitae nel più breve tempo possibile, e La contatteremo qualora il suo profilo risulti in linea con le posizioni disponibili al nostro interno.<br><br>La ringraziamo per l''interesse dimostrato verso la nostra società.<br><br><br>Cordiali saluti<br>Human Resources - DPE Data Processing Engineering S.r.l<br>Via Vallarsa, 10<br>Milano<br>http://www.dpe.it";
	public static final String CONTACT_EMAIL_SUBJECT = "Messaggio Sito Web DPE - Sig.(ra) {0} - Azienda {1}";
	public static final String CONTACT_EMAIL_BODY = "Nome: {0} <br>Azienda: {1} <br>Telefono: ({2}){3}<br>Cellulare: ({4}){5} <br>Email: {6} <br>Sito: {7} <br>Motivo: {8} <br>Messaggio:<br>{9}";
	public static final String EMAIL_SENDER_ADDRESS_CV_EXT = "Human Resources - DPE<dpe@dpe.it>";
	public static final String EMAIL_SENDER_ADDRESS_CV = "CV Sito DPE<dpe@dpe.it>";
	public static final String EMAIL_SENDER_ADDRESS_CONTACTS = "contatti_sito_dpe@dpe.it";
	public static final String EMAIL_RECEPIENTS_ADDRESS = "dpe@dpe.it";
	public static final String EMAIL_SERVER = "10.1.1.10";
	public static final Integer EMAIL_SMTP_PORT = 25;
	public static final String SECURITY_ACCESS_VALUE = "disabled";
	public static final String HASH = "#";
	public static final String STRING_TRUE = "true";
	public static final String EMPTY_SPACE_STRING = " ";
	public static final String ALL_SYMBOL = "*";
	public static final String SELECTED = "on";
	public static final String UNSELECTED = "off";
	public static final String SESSION_VAR_SPUNTE = "spunte";
	public static final String SESSION_VAR_DIFFERENZE = "differenze";
	public static final String SESSION_VAR_RESA = "resa";
	public static final String SESSION_VAR_COPIE = "copie";
	public static final String SESSION_VAR_NOTE = "note";
	public static final String SESSION_VAR_PRENOTAZIONE = "prenotazione";
	public static final String SESSION_VAR_PRENOTAZIONI_COPIE = "prenotazioniCopie";
	public static final String SESSION_VAR_PRENOTAZIONI_COPIE_FISSE = "prenotazioniCopieFisse";
	public static final String SESSION_VAR_EVASO = "evaso";
	public static final String SESSION_VAR_MESSAGGIO_CODIFICATO = "messaggioCodificato";
	public static final String SESSION_VAR_MESSAGGIO_LIBERO = "messaggioLibero";
	public static final String DPE_CHECKBOX_NAME = "dpeChkbx_";
	public static final String DPE_INPUT_TEXT_NAME_DIFFERENZE = "differenze_";
	public static final String DPE_INPUT_TEXT_NAME_RESA = "resa_";
	public static final String DPE_INPUT_TEXT_NAME_COPIE = "copie_";
	public static final String DPE_INPUT_TEXT_NAME_NOTE = "note_";
	public static final String DPE_INPUT_TEXT_NAME_PRENOTAZIONE = "prenotazione_";
	public static final String PK_FIELD_NAME = "codice";
	public static final String FIELD_TITOLO = "titolo";
	public static final String STYLE_CLASS_TEXT = "tableFields";
	public static final String DPE_TEXTFIELD_DEFAULT_SIZE = "4";
	public static final String J_SPRING_SECURITY_CHECK = "j_spring_security_check";
	public static final String LOGIN_DL_INITIALS = "dl";
	public static final String LOGIN_ED_INITIALS = "ed";
	public static final String LOGIN_ADM_INITIALS = "adm";
	public static final String LOGIN_DL = "LoginDL";
	public static final String LOGIN_ED = "LoginEd";
	public static final String LOGIN_ADM = "LoginAdm";
	public static final String SLASH = "/";
	public static final String AUTH_FAILED_PARAM = "authfailed=true";
	public static final String QUESTION_MARK = "?";
	public static final String SEMI_COLON = ";";
	public static final String J_USERNAME = "j_username";
	public static final String DL_HOME = "dl/DLHome";
	public static final String ED_HOME = "ed/EdHome";
	public static final String ADM_HOME = "adm/AdmHome";
	public static final String SPRING_SECURITY_LAST_USERNAME = "SPRING_SECURITY_LAST_USERNAME";
	public static final String DPE_BUTTON_SPUNTA_AUTOMATICA_NAME = "butSpuntaAuto";
	public static final String PROC_GET_ITENS_BOLLA_RESA = "getItensBollaResa";
	public static final String PROC_GET_ITENS_BOLLA_RESA_DB_NAME = "get_itens_bolla_resa";
	public static final String PROC_GET_ITENS_BOLLA_CONTROLLO = "getItensBollaControllo";
	public static final String PROC_GET_ITENS_BOLLA_CONTROLLO_DB_NAME = "get_itens_bolla_controllo";
	public static final String MAX_LENGTH_TEXT_FIELD = "3";
	public static final String QUERY_UPDATE_DIFFERENZE_BOLLA_CONTROLLO = "update BollaControlloVO br set br.differenza=? where br.codice=?";
	public static final String QUERY_UPDATE_SPUNTE_BOLLA_CONTROLLO = "update BollaControlloVO br set br.spunta=? where br.codice=?";
	public static final String QUERY_UPDATE_BOLLA_RESA = "update BollaResaVO br set br.resa=? where br.codice=?";
	public static final String QUERY_UPDATE_RIFORNIMENTI_COPIE = "update BollaControlloVO br set br.copieRifornimenti=? where br.codice=?";
	public static final String QUERY_UPDATE_RIFORNIMENTI_NOTE = "update BollaControlloVO br set br.noteRifornimenti=? where br.codice=?";
	public static final String QUERY_UPDATE_RIFORNIMENTI_PRENOTAZIONE = "update BollaControlloVO br set br.prenotazioniRifornimenti=? where br.codice=?";
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String UNDERSCORE = "_";
	public static final String DPE = "DPE";
	public static final String IT = "it";
	public static final String PT = "pt";
	public static final String EN = "en";
	public static final String ES = "es";
	public static final String ACTION_NEWS_UPDATE = "NewsUpdate";
	public static final String ACTION_NEWS_UPDATE_DPE = "NewsUpdateDpe";
}