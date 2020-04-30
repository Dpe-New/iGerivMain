package it.dpe.igeriv.vo;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.IndexColumn;

/**
 * Tabella Bolle di Consegna dei Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9545", schema = "")
@FilterDef(name = "NotDeletedFilter")
public class ProdottiNonEditorialiBollaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id9545")
	private Long idDocumento;
	@Column(name = "idre9545")
	private Integer indicatoreEmessoRicevuto;
	@Column(name = "cforn9545")
	private Integer codiceFornitore;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "crivw9545", updatable = false, insertable = false, referencedColumnName = "crivw9500"),
		@JoinColumn(name = "cforn9545", insertable = false, updatable = false, referencedColumnName = "cforn9500")
	})
	private ProdottiNonEditorialiFornitoreVo fornitore;
	@Column(name = "ddocf9545")
	private Timestamp dataDocumento;
	@Column(name = "ndocf9545")
	private String numeroDocumento;
	@Column(name = "datar9545")
	private Timestamp dataRegistrazione;
	@Column(name = "nor9545")
	private String numeroOrdine;
	@Column(name = "dmod9545")
	private Timestamp dataUltimaModifica;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9545", updatable = true, insertable = true, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola; 
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true, targetEntity = ProdottiNonEditorialiBollaDettaglioVo.class, mappedBy = "bolla")
	@Filter(name = "NotDeletedFilter", condition = "(delet9546 is null or delet9546 = 0)")
	@IndexColumn(name = "prog9546")
	private List<ProdottiNonEditorialiBollaDettaglioVo> dettagli;
	@Column(name = "codut9545")
	private String codUtente;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "caus9545", updatable = true, insertable = true, referencedColumnName = "cau9550")
	private ProdottiNonEditorialiCausaliMagazzinoVo causale;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "id9545", updatable = false, insertable = false, referencedColumnName = "idnoned9625")
	private VenditaVo vendita;
	@Column(name = "delet9545")
	private Boolean deleted;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "id9545", updatable = false, insertable = false, referencedColumnName = "iddoe9551"),
		@JoinColumn(name = "cforn9545", updatable = false, insertable = false, referencedColumnName = "cforn9551")
	})
	private ProdottiNonEditorialiDocumentiEmessiVo documentoResa;
	@Column(name = "invdl9545")
	private Boolean inviatoAlDl;

	public String getTipoDocumento() {
		if (getIndicatoreEmessoRicevuto() != null) {
			if (getIndicatoreEmessoRicevuto().equals(IGerivConstants.INDICATORE_DOCUMENTO_EMESSO)) {
				return IGerivMessageBundle.get("igeriv.documento.emesso");
			} else if (getIndicatoreEmessoRicevuto().equals(IGerivConstants.INDICATORE_DOCUMENTO_RICEVUTO)) {
				return IGerivMessageBundle.get("igeriv.documento.ricevuto");
			} else if (getIndicatoreEmessoRicevuto().equals(IGerivConstants.INDICATORE_DOCUMENTO_INTERNO)) {
				return IGerivMessageBundle.get("igeriv.documento.interno");
			}
		}
		return "";
	}
	
	public String getStatoDocumento() {
		return documentoResa != null && documentoResa.getIdReport() != null && Hibernate.isInitialized(documentoResa) ? IGerivMessageBundle.get("igeriv.inviato") : IGerivMessageBundle.get("igeriv.inserito");
	}
	
	@Override
	public String toString() {
		return getIdDocumento() != null ? getIdDocumento().toString() : "";
	}
	
	public String getNomeFornitore() {
		return (getFornitore() != null) ? getFornitore().getNome() : ""; 
	}
}
