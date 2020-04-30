package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.StringUtility;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

/**
 * Tabella Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9506", schema = "")
public class ProdottiNonEditorialiVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "cpro9506")
	private Long codProdottoInterno;
	@Column(name = "cproe9506")
	private String codProdottoEsterno;
	@Column(name = "desc9506")
	private String descrizioneProdottoA;
	@Column(name = "descb9506")
	private String descrizioneProdottoB;
	@Column(name = "cat9506")
	private Long codCategoria;
	@Column(name = "scat9506")
	private Long codSottoCategoria;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ 
		@JoinColumn(name = "cat9506", updatable = false, insertable = false, referencedColumnName = "cat9523"),
		@JoinColumn(name = "crivw9506", updatable = false, insertable = false, referencedColumnName = "crivw9523")
	})
	private ProdottiNonEditorialiCategoriaEdicolaVo categoria;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ 
		@JoinColumn(name = "cat9506", updatable = false, insertable = false, referencedColumnName = "cat9524"), 
		@JoinColumn(name = "scat9506", updatable = false, insertable = false, referencedColumnName = "scat9524"),
		@JoinColumn(name = "crivw9506", updatable = false, insertable = false, referencedColumnName = "crivw9524")
	})
	private ProdottiNonEditorialiSottoCategoriaEdicolaVo sottocategoria;
	@Column(name = "barco9506")
	private String barcode;
	@Column(name = "um9506")
	private String unitaMisura;
	@Column(name = "aliq9506")
	@Getter(AccessLevel.NONE)
	private Integer aliquota;
	@Column(name = "unmin9506")
	private Float unitaMinimaIncremento;
	@OneToOne
	@JoinColumn(name = "crivw9506", insertable = true, updatable = true, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
	@Column(name = "nomimg9506")
	@Getter(AccessLevel.NONE)
	private String nomeImmagine;
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true, targetEntity = ProdottiNonEditorialiPrezzoVenditaVo.class, mappedBy = "prodotto")
	private List<ProdottiNonEditorialiPrezzoVenditaVo> prezzi;
	@Column(name = "posiz9506")
	private Integer posizione;
	@Formula(value = "(select t1.cprofo9540 from tbl_9540 t1 where t1.crivw9540 = crivw9506 and t1.cpro9540 = cpro9506)")
	@Basic(fetch = FetchType.LAZY)
	private String codiceProdottoFornitore;
	@Formula(value = "(select t1.pda9540 from tbl_9540 t1 where t1.crivw9540 = crivw9506 and t1.cpro9540 = cpro9506)")
	@Basic(fetch = FetchType.LAZY)
	private Float ultimoPrezzoAcquisto;
	@Formula(value = "(select sum(t1.qt9546 * decode(t1.da9546, 1, -1, 1)) from tbl_9546 t1 where t1.crivw9546 = crivw9506 and t1.cpro9546 = cpro9506)")
	@Basic(fetch = FetchType.LAZY)
	@Getter(AccessLevel.NONE)
	private Integer giacenzaProdotto;
	@Formula(value = "(select sum(t1.qt9546) from tbl_9546 t1 where t1.da9546 = 0 and t1.a9546 = 1 and t1.caus9546 = 1 and t1.crivw9546 = crivw9506 and t1.cpro9546 = cpro9506)")
	@Basic(fetch = FetchType.LAZY)
	@Getter(AccessLevel.NONE)
	private Integer giacenzaInizialeProdotto;
	@Formula(value = "(select t1.prezzo9508 from tbl_9508 t1 where t1.crivw9508 = crivw9506 and t1.cpro9508 = cpro9506 and trunc(sysdate) between t1.dvi9508 and t1.dvf9508)")
	@Basic(fetch = FetchType.LAZY)
	private Float prezzo;
	@Formula(value = "(select t1.scontov9508 from tbl_9508 t1 where t1.crivw9508 = crivw9506 and t1.cpro9508 = cpro9506 and trunc(sysdate) between t1.dvi9508 and t1.dvf9508)")
	@Basic(fetch = FetchType.LAZY)
	private Float sconto;
	@Column(name = "fprodl9506")
	private Boolean prodottoDl;
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "tipo9506", insertable = true, updatable = true, referencedColumnName = "tipo9509"),
		@JoinColumn(name = "cccg9506", insertable = true, updatable = true, referencedColumnName = "cccg9509")
	})
	private ProdottiNonEditorialiCausaliContabilitaVo causaleIva;
	@Column(name = "obso9506")
	private Boolean obsoleto;
	@Column(name = "fp9506")
	private Integer formazionePacco;
	@Column(name = "perre9506")
	private Float percentualeResaSuDistribuito;
	@Column(name = "escve9506")
	private Boolean escludiDalleVendite;
	
	//GIFT CARD EPIPOLI
	@Column(name = "fprodig9506")
	private String isProdottoDigitale;
	@Column(name = "cforn9506")
	private Integer codiceFornitore;
	
	
	
	
	public Integer getAliquota() {
		return aliquota == null ? 0 : aliquota;
	}
	
	public String getNomeImmagine() {
		return nomeImmagine == null ? "" : nomeImmagine;
	}

	public Integer getGiacenzaProdotto() {
		return giacenzaProdotto == null ? 0 : giacenzaProdotto;
	}
	
	public Integer getGiacenzaInizialeProdotto() {
		return giacenzaInizialeProdotto == null ? 0 : giacenzaInizialeProdotto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			Long cpi = null;
			if (obj instanceof ProdottiNonEditorialiVo) {
				cpi = ((ProdottiNonEditorialiVo) obj).getCodProdottoInterno();
			} else {
				cpi = ((ProdottiNonEditorialiGenericaVo) obj).getCodProdottoInterno();
			}
			return getCodProdottoInterno().equals(cpi);
		}
		return super.equals(obj);
	}
	
	public String getDescrizioneProdottoHtml() {
		return descrizioneProdottoA != null ? StringUtility.escapeHTML(descrizioneProdottoA, false) : descrizioneProdottoA;
	}
	
}
