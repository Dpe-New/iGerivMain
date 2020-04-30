package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.NumberUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9401", schema = "")
public class OrdineLibriDettVo extends BasetTextVo {

	private static final long serialVersionUID = 1L;
	
	public enum ETrack {
		ND(-1,""),
		INS(0,"Nuovo"),
		DL(1,"Consegnato al DL"),
		RIV(2,"Consegnato alla rivendita"),
		CLI(3,"Consegnato al Cliente"),
		ANN(4,"Annullato"),
		RIO(5,"Riordinato"),
		RES(6,"Reso");
		
		
		private ETrack(int id, String desc) {
			this.id=id;
			this.desc=desc;
		}
		@Getter private final int id;
		@Getter private String desc;
		
		public static ETrack getTrackaing(Integer id) {
			for (ETrack track : ETrack.values()) {
				if (id!=null && id==track.id) {
					return track;
				}
			}
			return ETrack.ND;
		}
		
		
	}

	@Id
	@Column(name = "seqord9401")
	private Long seqordine;
	@Column(name = "numord9401")
	private Long numeroOrdine;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name = "numord9401", updatable = false, insertable = false, referencedColumnName = "numord9400")
	@ManyToOne
    @JoinColumn(name = "numord9401", updatable = false, insertable = false, referencedColumnName = "numord9400")
	private OrdineLibriVo ordine;
	@Column(name = "sku9401")
	private String sku;
	//Nuovo campo 16/09/2015
	@Column(name = "keynum9401")
	private Long keynum;
	
	@Column(name = "barcode9401")
	private String barcode;
	@Column(name = "titolo9401")
	private String titolo;
	@Column(name = "autori9401")
	private String autore;
	@Column(name = "editore9401")
	private String editore;
	@Column(name = "prezzo9401")
	private BigDecimal prezzoCopertina;
	@Column(name = "urlimg9401")
	private String urlImmagineCopertina;
	@Column(name = "quant9401")
	private Integer quantitaLibri;
	@Column(name = "stTrack9401")
	private String statoTracking;
	@Column(name = "stato9401")
	private Integer stato;
	@Column(name = "numcol9401")
	private Long numeroCollo;
	@Column(name = "datardl9401")
	private Timestamp dataArrivoDL;
	@Column(name = "datared9401")
	private Timestamp dataArrivoRivendita;
	@Column(name = "datcocl9401")
	private Timestamp dataArrivoCliente;
	//21-06-2016 campi aggiunti per la gestione del servizio di copertinatura
	@Column(name = "FLGCOPER9401")
	private Integer flagCopertina;
	@Column(name = "TOMI9401")
	private Integer tomi;
	@Column(name = "PRIGA9401")
	private String primaRigaCopertina;
	@Column(name = "SRIGA9401")
	private String secondaRigaCopertina;
	@Column(name = "TRIGA9401")
	private String terzaRigaCopertina;
	@Column(name = "IDLOGO9401")
	private Integer idLogoCopertina;
	
	//prezzi unitario 
	@Column(name = "PRUNCOPECLI9401")
	private BigDecimal prezzoUnitarioCopertinaCliente;
	@Column(name = "PRUNCOPERIV9401")
	private BigDecimal prezzoUnitarioCopertinaRivendita;
	@Column(name = "PRUNCOPEDL9401")
	private BigDecimal prezzoUnitarioCopertinaDl;
	
	//prezzi calcolati in base al numero di tomi
	@Column(name = "PREZCOPECLI9401")
	private BigDecimal prezzoCopertinaCliente;
	@Column(name = "PREZCOPERIV9401")
	private BigDecimal prezzoCopertinaRivendita;
	@Column(name = "PREZCOPEDL9401")
	private BigDecimal prezzoCopertinaDl;
	
	@Column(name = "tipotesto9401")
	private Integer tipotesto;
	
	@Column(name = "VOLUME9401")
	private String volume;
	
	@Transient
	private BigDecimal prezzoTotaleLibro;
	
	@Transient
	private String descrizioneStato;
	@Transient
	private Integer spunta;
	
	public String getDescStato() {
		return ETrack.getTrackaing(stato).getDesc();
	}
	
	
	
	
}

