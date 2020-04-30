package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiFornitorePk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Fornitori dell'Edicola di Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9500", schema = "")
public class ProdottiNonEditorialiFornitoreVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiFornitorePk pk;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9500", updatable = false, insertable = false, referencedColumnName = "crivw9206")	
	private AbbinamentoEdicolaDlVo edicola;
	@Column(name = "nomea9500")
	private String nome;
	@Column(name = "nomeb9500")
	private String cognome;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "tiplo9500", updatable = true, insertable = true, referencedColumnName = "tiplo9221")
	})
	private TipoLocalitaVo tipoLocalita;
	@Column(name = "viaa9500")
	private String indirizzo;	
	@Column(name = "numci9500")
	private Integer numeroCivico;
	@Column(name = "compl9500")
	private String estensione;	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cloc9500", updatable = true, insertable = true, referencedColumnName = "local9219")
	private LocalitaVo localita;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "paese9500", updatable = true, insertable = true, referencedColumnName = "paesi9218")
	private PaeseVo paese;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cprov9500", updatable = true, insertable = true, referencedColumnName = "provi9220")
	private ProvinciaVo provincia;
	@Column(name = "cap9500")
	private String cap;
	@Column(name = "cofi9500")
	private String codiceFiscale;
	@Column(name = "piva9500")
	private String piva;
	@Column(name = "tele9500")
	private String telefono;
	@Column(name = "fax9500")
	private String fax;
	@Column(name = "email9500")
	private String email;
	@Column(name = "url9500")
	private String url;
	//GIFT CARD EPIPOLI
	@Column(name = "ws9500")
	private String webServices;
	
	
	
	public String getProvinciaDesc() {
		return (provincia != null) ? provincia.getDescrizione() : null;
	}
	
	public String getLocalitaDesc() {
		return (localita != null) ? localita.getDescrizione() : null;
	}
	
	public Integer getCodFornitore() {
		return getPk().getCodFornitore();
	}
}
