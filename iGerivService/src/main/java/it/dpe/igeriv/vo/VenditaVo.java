package it.dpe.igeriv.vo;

import it.dpe.igeriv.dto.ConsumaCodiceB2CResponse;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivQueryContants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.IndexColumn;

@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_CONTO_VENDITA_CON_DETTAGLI, query = "from VenditaVo vo left join fetch vo.listVenditaDettaglio left join fetch vo.contoProdottiVari left join fetch vo.contoProdottiVari.dettagli where vo.codFiegDl = :codFiegDl and vo.codEdicola = :codEdicola and vo.codUtente = :codUtente and vo.dataVendita between :dataVenditaIni and :dataVenditaFine order by vo.dataVendita desc"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_CONTO_VENDITA_BY_PK, query = "from VenditaVo vo left join fetch vo.listVenditaDettaglio left join fetch vo.contoProdottiVari left join fetch vo.contoProdottiVari.dettagli where vo.codVendita = :codVendita"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_CONTO_VENDITA_BY_ID_PRODOTTI, query = "from VenditaVo vo left join fetch vo.listVenditaDettaglio left join fetch vo.contoProdottiVari left join fetch vo.contoProdottiVari.dettagli where vo.idDocumentoProdottiVari = :idDocumentoProdottiVari")
})
@Table(name = "tbl_9625", schema = "")
public class VenditaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idven9625")
	private Long codVendita;
	@Column(name = "coddl9625")
	private Integer codFiegDl;
	@Column(name = "crivw9625")
	private Integer codEdicola;
	@Transient
	private Integer codEdicolaDl;
	@Column(name = "codut9625")
	private String codUtente;
	@Column(name = "datve9625")
	private Timestamp dataVendita;
	@Column(name = "idtrg9625")
	private Integer trasferitaGestionale;
	@Column(name = "datrg9625")
	private Timestamp dataTrasfGestionale;
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true, fetch=FetchType.LAZY, targetEntity = VenditaDettaglioVo.class, mappedBy = "pk.venditaVo")
	@IndexColumn(name = "progv9626")
	private List<VenditaDettaglioVo> listVenditaDettaglio;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idnoned9625", insertable = false, updatable = false, referencedColumnName = "id9545")
	private ProdottiNonEditorialiBollaVo contoProdottiVari;
	@Column(name = "idnoned9625")
	private Long idDocumentoProdottiVari;
	@Column(name = "ccli9625")
	private Long codCliente;
	@Column(name = "imptot9625")
	private BigDecimal importoTotale;
	@Column(name = "scontr9625")
	private Boolean contoScontrinato;
	@Column(name = "dataec9625")
	private Timestamp dataEstrattoConto;
	@Column(name = "pagato9625")
	private Boolean pagato;
	@Column(name = "idScon9625")
	private String idScontrino;
	@Column(name = "dtScon9625")
	private Timestamp dataScontrino;
	@Column(name = "fateme9625")
	@Getter(AccessLevel.NONE)
	private Boolean fatturaEmessa;
	@Column(name = "fatcon9625")
	@Getter(AccessLevel.NONE)
	private Boolean fatturaContoUnico;
	@Column(name = "dtcoec9625")
	private Timestamp dataCompetenzaEstrattoContoClienti;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ccli9625", insertable = false, updatable = false, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo cliente;
	@Column(name = "provco9625")
	private Integer provenienzaConto;
	@Column(name = "tippec9625")
	private Integer tipoProdottiInEstrattoConto;
	@Formula(value = "(select t1.dafat9309 from tbl_9309 t1 where t1.idfat9309 = idfat9625)")
	@Basic(fetch = FetchType.LAZY)
	private Timestamp dataFattura;
	@Column(name = "idfat9625")
	private Integer idFattura;
	@Column(name = "delet9625")
	@Getter(AccessLevel.NONE)
	private Boolean deleted;
	@Column(name = "corid9625")
	private String correlationId;
	
	@Transient
	private String enumEsitoVendita;
	@Transient
	private String wsEpipoliMessaggioPopup;
	@Transient
	private List<ConsumaCodiceB2CResponse> responseWS;
	
	
	public Boolean getFatturaEmessa() {
		return fatturaEmessa == null ? false : fatturaEmessa;
	}
	
	public Boolean getFatturaContoUnico() {
		return fatturaContoUnico == null ? false : fatturaContoUnico;
	}
	
	public Boolean getDeleted() {
		return deleted == null ? false : deleted;
	}

	public BigDecimal getSommaVenduto() {
		BigDecimal somma = new BigDecimal(0);
		for (IVenditaDettaglio vo : getDettagliVendita()) {
			somma = somma.add(vo.getPrezzoCopertina());
		}
		return somma;
	}
	
	public String getDataVenditaFormat() {
		return (dataVendita != null) ? DateUtilities.getTimestampAsString(dataVendita, DateUtilities.FORMATO_DATA_SLASH_HHMMSS) : "";
	}
	
	public List<IVenditaDettaglio> getDettagliVendita() {
		List<IVenditaDettaglio> dettagli = new ArrayList<IVenditaDettaglio>();
		dettagli.addAll(listVenditaDettaglio);
		if (contoProdottiVari != null && contoProdottiVari.getDettagli() != null && !contoProdottiVari.getDettagli().isEmpty()) {
			dettagli.addAll(contoProdottiVari.getDettagli());
		}
		return dettagli;
	}
	
	@Override
	public void accept(VisitorVo visitor) {
		visitor.visit(this);
	}
	
	
	
}
