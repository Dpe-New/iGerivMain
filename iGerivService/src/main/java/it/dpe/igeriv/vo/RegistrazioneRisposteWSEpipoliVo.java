package it.dpe.igeriv.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.dpe.igeriv.util.StringUtility;
import lombok.Getter;
import lombok.Setter;

/**
 * Tabella 
 * REGISTRAZIONE DELLE RISPOSTE RICEVUTE DAL WEB SERVICES EPIPOLI INERENTI
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9660", schema = "")
public class RegistrazioneRisposteWSEpipoliVo extends BasetTextVo {

	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idRichiesta9660")
	private Integer idRichiesta;
	@Column(name = "coddl9660")
	private Integer codFiegDl;
	@Column(name = "crivw9660")
	private Integer codEdicola;
	@Column(name = "idTrans9660")
	private String idTrans;
	@Column(name = "dtTrans9660")
	private Timestamp dtTransazione;
	@Column(name = "esito9660")
	private String esito;
	@Column(name = "errCod9660")
	private String errCodice;
	@Column(name = "errDesc9660")
	private String errDescrizione;
	@Column(name = "ean9660")
	private String ean;
	@Column(name = "serNum9660")
	private String serialNumber;
	@Column(name = "pin9660")
	private String pin;
	@Column(name = "valore9660")
	private Double valore;
	@Column(name = "nomProd9660")
	private String nomeProdotto;
	@Column(name = "istruz9660")
	private String istruzioniAttivazione;
	@Column(name = "dtScadAtt9660")
	private Timestamp dtScadenzaAttivazione;
	@Column(name = "scaMesi9660")
	private Integer scadenzaMesiUtilizzo;
	
	
	public String getCodiceRichiesta(){
		String idRicRet = "ERR";
		if(this.esito.equals("OK")){
			idRicRet = this.idRichiesta.toString();
		}else{
			idRicRet = "Errore attivazione codice "+this.idRichiesta.toString();
		}
		return idRicRet;
	}
	
	
	public String getSerialNumber(){
		return (this.esito.equals("OK"))?this.serialNumber:"";
	}
	
	public String getPin(){
		return (this.esito.equals("OK"))?this.pin:"";
	}
	
	public String getValoreReport(){
		DecimalFormat df = new DecimalFormat("#0.00");
		String prezzoFormat = df.format((this.getValore()!=null)?this.getValore():new BigDecimal(0))+" &euro; " ;
		return prezzoFormat;
	}
	
	public String getNomeProdotto(){
		return (this.esito.equals("OK"))?this.nomeProdotto:"";
	}
	
	public String getIstruzioniAttivazione(){
		return (this.esito.equals("OK"))?this.istruzioniAttivazione:this.errCodice+" - "+this.errDescrizione;
	}

	public String getDtScadenzaAttivazioneReport(){
		if(this.getDtScadenzaAttivazione()!=null)
			return new SimpleDateFormat("dd/MM/yyyy").format(this.getDtScadenzaAttivazione());
		else
			return "";
	}

	
				
}				