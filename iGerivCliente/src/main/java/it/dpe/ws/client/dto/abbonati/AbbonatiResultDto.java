package it.dpe.ws.client.dto.abbonati;


import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author romanom
 *
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="AbbonatiResponse", namespace="http://it.dpe.rtae/abbonati/schemas")
public class AbbonatiResultDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String type;
	private PubblicazioneDto pubblicazione;
	@XmlElement(name = "Ricarica")
	private List<AnagraficaTipologiaMinicardDto> listRicariche;
	private Integer idEditore;
	private Integer idProdotto;
	private String edizione;
	private String message;
	private String barcode;
	
	public static enum ResultType {
        VENDITE_BARCODE,
        VENDITE_PREZZO,
        VENDITE_TITOLO,
        VENDITE_ABBONAMENTO,
        VENDUTO_GIORNALIERO,
        VENDUTO_CONTO,
        CONTO_VENDITE,
        RICARICA_MINICARD,
        STATO_MINICARD,
        PUBBLICAZIONI_MULTIPLE,
        CONFIRM_ESEGUI_RICARICA,
        GESTIONE_ARRETRATI,
        EXCEPTION;
        
        public String getString() {     
        	return this.name(); 
        } 
    }
	
	public static enum OperationType {
        CONSEGNA,
        RICARICA,
        MODALITA_RICARICA,
        LETTURA_STATO;
        
        public String getString() {     
        	return this.name(); 
        } 
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PubblicazioneDto getPubblicazione() {
		return pubblicazione;
	}

	public void setPubblicazione(PubblicazioneDto pubblicazione) {
		this.pubblicazione = pubblicazione;
	}
	
	public List<AnagraficaTipologiaMinicardDto> getListRicariche() {
		return listRicariche;
	}

	public void setListRicariche(List<AnagraficaTipologiaMinicardDto> listRicariche) {
		this.listRicariche = listRicariche;
	}

	public Integer getIdEditore() {
		return idEditore;
	}

	public void setIdEditore(Integer idEditore) {
		this.idEditore = idEditore;
	}
	
	public Integer getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Integer idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getEdizione() {
		return edizione;
	}

	public void setEdizione(String edizione) {
		this.edizione = edizione;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}	
