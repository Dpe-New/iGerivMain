package it.dpe.igeriv.vo;

import it.dpe.igeriv.dto.VisitorDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiBollaDettaglioPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

/**
 * Tabella Bolla Dettaglio di Consegna dei Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries( {
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_SOMMA_VENDITE_PRODOTTI_VARI_INTERVALLO, query = "select sum(vo.prezzo * vo.quantita) from ProdottiNonEditorialiBollaDettaglioVo vo where vo.magazzinoDa = " + IGerivConstants.COD_MAGAZZINO_INTERNO + " and vo.magazzinoA = " + IGerivConstants.COD_MAGAZZINO_ESTERNO + " and (vo.deleted = false or vo.deleted is null) and vo.bolla.edicola.codEdicola = :codEdicola and (TRUNC(vo.bolla.dataRegistrazione) between :dataVenditaIni and :dataVenditaFine ) and vo.bolla.deleted = false"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_RESA_TEMP, query = "update ProdottiNonEditorialiBollaDettaglioVo vo set vo.statoEsportazione = :statoEsportazione, vo.dataEsportazioneVersoDl = :dataEsportazioneVersoDl where vo.pk.idDocumento = :idDocumento and vo.pk.progressivo = :progressivo and vo.causale.codiceCausale = " + IGerivConstants.CODICE_CAUSALE_RESA)
})
@Table(name = "tbl_9546", schema = "")
public class ProdottiNonEditorialiBollaDettaglioVo extends BaseVo implements IVenditaDettaglio {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiBollaDettaglioPk pk;
	@ManyToOne
	@JoinColumn(name = "id9546", updatable = false, insertable = false, referencedColumnName = "id9545")
	private ProdottiNonEditorialiBollaVo bolla;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "crivw9546", updatable = true, insertable = true, referencedColumnName = "crivw9506"),
		@JoinColumn(name = "cpro9546", updatable = true, insertable = true, referencedColumnName = "cpro9506")
	})
	private ProdottiNonEditorialiVo prodotto;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "caus9546", updatable = true, insertable = true, referencedColumnName = "cau9550")
	private ProdottiNonEditorialiCausaliMagazzinoVo causale;
	@Column(name = "da9546")
	private Integer magazzinoDa;
	@Column(name = "a9546")
	private Integer magazzinoA;
	@Column(name = "qt9546")
	private Integer quantita;
	@Column(name = "prezzo9546")
	private Float prezzo;
	@Column(name = "scontov9546")
	private Float scontoValore;
	@Column(name = "sconto9546")
	private Float scontoPercentuale;
	@Formula(value = "(select t1.cprofo9540 from tbl_9540 t1 where t1.crivw9540 = crivw9546 and t1.cpro9540 = cpro9546)")
	@Basic(fetch = FetchType.LAZY)
	private String codiceProdottoFornitore;
	@Column(name = "delet9546")
	private Boolean deleted;
	@Column(name = "stesp9546")
	private Integer statoEsportazione;
	@Column(name = "dtesp9546")
	private Timestamp dataEsportazioneVersoDl;
	@Column(name = "corid9546")
	private String correlationId;
	
	public Float getImporto() {
		return (getQuantita() != null && getPrezzo() != null) ? getQuantita() * getPrezzo() : 0; 
	}
	
	@Override
	public String toString() {
		return getPk() != null ? getPk().toString() : "";
	}
	
	public String getFake() {
		return "";
	}

	@Override
	public Integer getIdtn() {
		return null;
	}

	@Override
	public BigDecimal getPrezzoCopertina() {
		return new BigDecimal(getPrezzo());
	}

	@Override
	public String getTitolo() {
		return ((prodotto != null) ? prodotto.getDescrizioneProdottoA() : "");
	}

	@Override
	public String getSottoTitolo() {
		return ((prodotto != null) ? prodotto.getDescrizioneProdottoB() : "");
	}

	@Override
	public String getNumeroCopertina() {
		return "";
	}

	@Override
	public BigDecimal getImportoTotale() {
		return (getPrezzo() != null && getQuantita() != null) ? new BigDecimal(getPrezzo() * getQuantita()) : null;
	}

	@Override
	public Integer getProgressivo() {
		return getPk().getProgressivo();
	}

	@Override
	public String getImportoFormat() {
		return (getImportoTotale() != null) ? NumberUtils.formatNumber(getImportoTotale()) : "";
	}

	@Override
	public Timestamp getDataUscita() {
		return null;
	}

	@Override
	public Boolean isProdottoNonEditoriale() {
		return true;
	}

	@Override
	public Long getIdProdotto() {
		return (getProdotto() != null) ? getProdotto().getCodProdottoInterno() : null;
	}

	@Override
	public String getBarcode() {
		return (getProdotto() != null) ? getProdotto().getBarcode() : "";
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
	
}
