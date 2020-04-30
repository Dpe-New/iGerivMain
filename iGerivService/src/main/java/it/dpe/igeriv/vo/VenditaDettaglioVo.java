package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.vo.pk.VenditaDettaglioPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * Tabella dati delle vendite.
 * 	
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries( { 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_CONTO_VENDITA_DETTAGLI, query = "from VenditaDettaglioVo vo join fetch vo.pk.venditaVo where vo.codFiegDl = :codFiegDl and vo.codEdicola = :codEdicola and vo.pk.venditaVo.dataVendita between :dataVenditaIni and :dataVenditaFine and vo.deleted = false order by vo.pk.venditaVo.dataVendita desc"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_SOMMA_VENDITE_INTERVALLO, query = "select sum(vo.prezzoCopertina * vo.quantita) from VenditaDettaglioVo vo where vo.pk.venditaVo.codFiegDl = :codFiegDl and vo.pk.venditaVo.codEdicola = :codEdicola and vo.pk.venditaVo.dataVendita between :dataVenditaIni and :dataVenditaFine and vo.deleted = false"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_SOMMA_VENDITE_BY_IDTN, query = "select count(vo.pk.progressivo) from VenditaDettaglioVo vo where vo.pk.venditaVo.codFiegDl = :codFiegDl and vo.pk.venditaVo.codEdicola = :codEdicola and vo.idtn = :idtn and vo.deleted = false")
})
@Table(name = "tbl_9626", schema = "")
public class VenditaDettaglioVo extends BaseVo implements IVenditaDettaglio {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private VenditaDettaglioPk pk;
	@Column(name = "idtn9626")
	private Integer idtn;
	@Column(name = "coddl9626")
	private Integer codFiegDl;
	@Column(name = "crivw9626")
	private Integer codEdicola;
	@Column(name = "impve9626")
	private BigDecimal prezzoCopertina;
	@Column(name = "titolo9626")
	private String titolo;
	@Column(name = "sottot9626")
	private String sottoTitolo;
	@Column(name = "numcop9626")
	private String numeroCopertina;
	@Column(name = "quanti9626")
	private Integer quantita;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9626", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9626", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idven9626", insertable = false, updatable = false, referencedColumnName = "idven9625")
	private VenditaVo venditaVo;
	@Column(name = "imptot9626")
	private BigDecimal importoTotale;
	@Formula(value = "(select t1.idtni9605 from tbl_9605 t1 where t1.coddl9605 = coddl9626 and t1.idtn9605 = idtn9626)")
	@Basic(fetch = FetchType.LAZY)
	private String idtnTrascodifica;
	@Column(name = "delet9626")
	private Boolean deleted;
	@Column(name = "idtrg9626")
	private Boolean trasferitaGestionale;
	@Column(name = "datrg9626")
	private Timestamp dataTrasfGestionale;
	
	public Integer getProgressivo() {
		return getPk().getProgressivo();
	}
	
	public String getPrezzoCopertinaFormat() {
		return (prezzoCopertina != null) ? NumberUtils.formatNumber(prezzoCopertina) : "";
	}
	
	public String getImportoFormat() {
		return (importoTotale != null) ? NumberUtils.formatNumber(importoTotale) : "";
	}
	
	public BigDecimal getTotale() {
		return (getQuantita() != null && getPrezzoCopertina() != null) ? getPrezzoCopertina().multiply(new BigDecimal(getQuantita())) : null;
	}
	
	public String getOraVendita() {
		Timestamp dataVendita = getPk().getVenditaVo().getDataVendita();
		String timestampAsString = DateUtilities.getTimestampAsString(dataVendita, "HH:mm:ss");
		return timestampAsString;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (obj != null && obj instanceof VenditaDettaglioVo) {
			VenditaDettaglioVo vo = (VenditaDettaglioVo) obj;
			if (getPk().equals(vo.getPk())) {
				equal = true;
			}
		}
		return equal;
	}

	@Override
	public Timestamp getDataUscita() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getDataUscita() : null;
	}

	@Override
	public Boolean isProdottoNonEditoriale() {
		return false;
	}

	@Override
	public Long getIdProdotto() {
		return null;
	}

	@Override
	public String getBarcode() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getCodiceBarre() : null;
	}
}
