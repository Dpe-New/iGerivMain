package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.BollaResaFuoriVocePk;
import it.dpe.igeriv.vo.pk.PeriodicitaPk;

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
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

/**
 * Tabella dati di dettaglio del richiamo fuori voce.
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_RICHIAMO_FUORI_VOCE_BOLLA_RESA_BY_CPU, query = "from BollaResaFuoriVoceVo vo join fetch vo.storicoCopertineVo sc join fetch sc.anagraficaPubblicazioniVo ap join fetch ap.periodicitaVo per where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.cpuDl = :cpuDl order by vo.pk.posizioneRiga asc"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RESA_DETTAGLIO_FUORI_VOCE, query = "update BollaResaFuoriVoceVo vo set vo.reso = :reso where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.pk.posizioneRiga = :posizioneRiga"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_LAST_POSIZIONE_RIGA_BOLLA_FUORI_RESA, query = "select max(vo.pk.posizioneRiga) from BollaResaFuoriVoceVo vo where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_CPUS_RESA_DIMETICATA_NOT_IN_BOLLA_RESA, query = "select distinct(vo.cpuDl) from BollaResaFuoriVoceVo vo where vo.pk.codFiegDl = ? and vo.pk.codEdicola = ? and vo.pk.dtBolla = ? and vo.pk.tipoBolla = ? and vo.reso > 0 and (vo.cpuDl not in (select distinct(vo1.cpuDl) from BollaResaVo vo1 where vo.pk.codFiegDl = vo1.pk.codFiegDl and vo.pk.dtBolla = vo1.pk.dtBolla and vo.pk.tipoBolla = vo1.pk.tipoBolla) or vo.cpuDl not in (select distinct(vo2.cpuDl) from BollaVo vo2 where vo.pk.codFiegDl = vo2.pk.codFiegDl and vo.pk.dtBolla = vo2.pk.dtBolla and vo.pk.tipoBolla = vo2.pk.tipoBolla))")
}) 
@Table(name = "tbl_9622B", schema = "")
public class BollaResaFuoriVoceVo extends BaseVo implements BollaResa {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaResaFuoriVocePk pk;
	@Column(name = "idtn9622B")
	private Integer idtn;
	@Column(name = "cpu9622B")
	private Integer cpuDl;
	@Column(name = "num9622B")
	@Getter(AccessLevel.NONE)
	private String numeroPubblicazione;
	@Column(name = "titolo9622B")
	private String titolo;
	@Column(name = "sottot9622B")
	private String sottoTitolo;
	@Column(name = "plor9622B")
	private BigDecimal prezzoLordo;
	@Column(name = "prne9622B")
	private BigDecimal prezzoNetto;
	@Column(name = "qdist9622B")
	private Long distribuito;
	@Column(name = "qreso9622B")
	private Integer reso;
	@Column(name = "cesta9622B")
	private String cesta;
	@Column(name = "qresor9622B")
	@Getter(AccessLevel.NONE)
	private Integer resoRiscontrato;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9622B", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9622B", updatable = false, insertable = false, referencedColumnName = "idtn9607") 
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Formula(value = "P_BOLLA.GET_GIACENZA(coddl9622B, crivw9622B, idtn9622B, null)")
	private Long giacenza;
	@Transient
	private String nomeImmagine;
	@Transient
	private boolean permetteContoDeposito;
	@Transient
	private boolean pubblicazionePresenteNelleSuccessiveBolleResa = false;
	@Transient
	private Integer quantitaCopieContoDeposito;
	@Transient
	private Timestamp dataUscita;
	@Transient
	private String barcodeStr;
	@Column(name = "resocd9622B")
	private Boolean resoInContoDeposito;
	@Transient
	@Getter(AccessLevel.NONE)
	private Boolean numeroInContoDeposito;
	@Formula(value = "(select t1.idtni9605 from tbl_9605 t1 where t1.coddl9605 = coddl9622B and t1.idtn9605 = idtn9622B)")
	@Basic(fetch = FetchType.LAZY)
	private String idtnTrascodifica;
	@Transient
	private String motivoResaRespinta;
	@Transient
	private Integer codMotivoRespinto;
	@Column(name = "flant9622B")
	private Boolean resaAnticipata;
	@Transient
	private Timestamp dataRichiamoResa;
	@Transient
	@Getter(AccessLevel.NONE)
	private PeriodicitaPk periodicita;
	@Transient
	@Getter(AccessLevel.NONE)
	private Boolean richiamoPersonalizzato;
	
	public String getNumeroPubblicazione() {
		return numeroPubblicazione != null ? numeroPubblicazione.trim() : numeroPubblicazione;
	}

	@Transient
	public BigDecimal getImportoLordo() {
		return (getPrezzoLordo() != null && reso != null) ? getPrezzoLordo().multiply(new BigDecimal(reso)) : BigDecimal.ZERO;
	}
	
	@Transient
	public BigDecimal getImportoNetto() {
		return (getPrezzoNetto() != null && reso != null) ? getPrezzoNetto().multiply(new BigDecimal(reso)) : BigDecimal.ZERO;
	}
	
	public Integer getResoRiscontrato() {
		return (resoRiscontrato == null) ? 0 : resoRiscontrato;
	}

	@Transient
	public Timestamp getDataUscita() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getDataUscita() : dataUscita;
	}
	
	@Transient
	public Integer getRiga() {
		return pk.getPosizioneRiga();
	}
	
	@Transient
	public String getImmagine() {
		return (storicoCopertineVo != null && storicoCopertineVo.getImmagine() != null) ? storicoCopertineVo.getImmagine().getNome() : "";
	}
	
	@Transient
	public String getBarcode() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getCodiceBarre() : null;
	}
	
	public Boolean getNumeroInContoDeposito() {
		return (numeroInContoDeposito == null) ? false : numeroInContoDeposito;
	}
	
	public PeriodicitaPk getPeriodicita() {
		return getStoricoCopertineVo() != null && getStoricoCopertineVo().getAnagraficaPubblicazioniVo() != null && getStoricoCopertineVo().getAnagraficaPubblicazioniVo().getPeriodicitaVo() != null ? getStoricoCopertineVo().getAnagraficaPubblicazioniVo().getPeriodicitaVo().getPk() : periodicita;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (obj != null && obj instanceof BollaResaFuoriVoceVo) {
			BollaResaFuoriVoceVo vo = (BollaResaFuoriVoceVo) obj;
			if (getPk().equals(vo.getPk()) && getNumeroPubblicazione().equals(vo.getNumeroPubblicazione())) {
				equal = true;
			}
		}
		return equal;
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
	    hash = hash * 31 + getPk().hashCode();
	    return hash;
	}

	@Override
	@Transient
	public String getTipoRichiamo() {
		return null;
	}
	
	public Boolean getRichiamoPersonalizzato() {
		return richiamoPersonalizzato == null ? false : richiamoPersonalizzato;
	}
	
}
