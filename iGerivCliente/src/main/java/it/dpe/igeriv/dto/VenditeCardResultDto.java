package it.dpe.igeriv.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author romanom
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class VenditeCardResultDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private ResultType type;
	private Object result;
	private String exceptionMessage;
	private String idConto;
	private String barcode;
	private String idEditore;
	private String idProdotto;
	private String contoNome;
	private Long codCliente;
	private List<?> listRicariche;
	private String token;
	private Object pubblicazione;
	private Map<String, String> msgParams;
	private List<?> listStatoTessera;
	private List<?> listEditoriSenzaPrivacy;
	private Boolean puoiRicaricare;
	private String tessera;
	private String operation;
	private List<?> ritiri;
	private List<?> editori;
	private List<?> prodotti;
	private List<?> tipiTessera;
	private List<?>	listVenditeAbbonatiRivPeriodo;
	private List<?>	listVenditeMinicardRivPeriodo;
	private List<?>	listRicaricheMinicardRivPeriodo;
	
	public static enum ResultType {
        VENDITE_BARCODE,
        VENDITE_IDTN,
        VENDITE_PREZZO,
        VENDITE_TITOLO,
        VENDITE_ABBONAMENTO,
        VENDUTO_GIORNALIERO,
        VENDUTO_CONTO,
        VENDITE_IGERIV_CARD,
        CONTO_CHIUSO,
        CONTO_VENDITE,
        MINICARD,
        RICARICA_MINICARD,
        STATO_MINICARD,
        CONFIRM_ESEGUI_RICARICA,
        GESTIONE_ARRETRATI,
        PUBBLICAZIONI_MULTIPLE,
        LISTA_TIPOLOGIE_MINICARD,
        EXCEPTION,
        CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE,
        CONFIRM_ABILITARE_IGERIV_CARD,
        CONFIRM_RIASSOCIARE_IGERIV_CARD,
        CONFIRM_ASSOCIA_BARCODE,
        UTENTE_NON_AUTENTICATO,
        VENDITE_VALORE,
        RICARICA_MINICARD_COPIE,
        RICARICA_MINICARD_VALORE,
        LISTA_PUBBLICAZIONI_MINICARD,
        TESSERA_NUOVA_CONFIRM_ESEGUI_RICARICA,
        OPERAZIONE_INVALIDA,
        TESSERA_ESAURITA_CONFIRM_ESEGUI_RICARICA,
        CONFIRM_PREZZO_PUBBLICAZIONE,
        COPIA_GIA_CONSEGNATA,
        PUBBLICAZIONE_NON_USCITA,
        PUBBLICAZIONE_SENZA_DATA_USCITA,
        DATA_USCITA_NON_VALIDA,
        NESSUNA_PUBBLICAZIONE_DA_CONSEGNARE,
        TESSERA_NON_RICONOSCIUTA,
        TESSERA_INESISTENTE,
        RIVENDITA_ERRATA,
        RIVENDITA_NON_ANCORA_ABILITATA,
        CONTRATTO_NON_PIU_VALIDO,
        CONTRATTO_NON_ANCORA_VALIDO,
        RICARICA_ANNULLATA,
        TESSERA_SOSPESA,
        TESSERA_ANNULLATA,
        TESSERA_ESAURITA,
        TESSERA_IN_SOSTITUZIONE,
        CREDITO_INSUFFICIENTE,
        CREDITO_ECCESSIVO,
        PLAFOND_SUPERATO,
        PRODOTTO_NON_CORRISPONDE_ABBONAMENTO,
        PRODOTTO_NON_IN_PROMOZIONE,
        NESSUNA_USCITA_PUBBLICAZIONE,
        NESSUN_CONTRATTO,
        TESSERA_SENZA_RICARICA_VALORE,
        PUBBLICAZIONE_TROPPO_VECCHIA,
        LISTINO_RICARICHE_COPIA,
        LISTINO_RICARICHE_VALORE,
        UNEXPECTED_EXCEPTION,
        PRIVACY_MANCANTE,
        PLG_CLIENT_MESSAGE,
        PLG_COPIA_GIA_CONSEGNATA,
        PLG_WARNING_CLIENT_MESSAGE,
        VENDITE_ABBONATO_RIV_PERIODO,
        VENDITE_MINICARD_RIV_PERIODO,
        RICARICHE_MINICARD_RIV_PERIODO,
        VENDITA_RETE_EDICOLE;
        
        public String getString() {     
        	return this.name(); 
        } 
    }
	
}	
