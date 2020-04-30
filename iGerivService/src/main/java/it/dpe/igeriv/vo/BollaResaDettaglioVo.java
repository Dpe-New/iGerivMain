package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.BollaResaDettaglioPk;

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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

/**
 * Tabella dati di dettaglio della bolla di resa.
 * 	
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries( { 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RESA_DETTAGLIO, query = "update BollaResaDettaglioVo vo set vo.reso = :reso where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.pk.posizioneRiga = :posizioneRiga"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_LAST_POSIZIONE_RIGA_BOLLA_RESA, query = "select max(vo.pk.posizioneRiga) from BollaResaDettaglioVo vo where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla")
})
@Table(name = "tbl_9621", schema = "")
public class BollaResaDettaglioVo extends BaseVo implements BollaResa {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaResaDettaglioPk pk;
	@Column(name = "idtn9621")
	private Integer idtn;
	@Column(name = "prne9621")
	private BigDecimal prezzoNetto;
	@Column(name = "qreso9621")
	private Integer reso;
	@Column(name = "qresor9621")
	private Integer resoRiscontrato;
	@Column(name = "qdist9621")
	@Getter(AccessLevel.NONE)
	private Long distribuito;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9621", updatable = false, insertable = false, referencedColumnName = "coddl9619"),
		@JoinColumn(name = "datbr9621", updatable = false, insertable = false, referencedColumnName = "datbr9619"),
		@JoinColumn(name = "tipbr9621", updatable = false, insertable = false, referencedColumnName = "tipbr9619"),
		@JoinColumn(name = "posiz9621", updatable = false, insertable = false, referencedColumnName = "posiz9619")
	})
	private BollaResaVo bollaResa;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9621", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9621", updatable = false, insertable = false, referencedColumnName = "idtn9607") 
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Formula(value = "P_BOLLA.GET_GIACENZA(coddl9621, crivw9621, idtn9621, null)")
	@Basic(fetch = FetchType.LAZY)
	private Long giacenza;
	@Formula(value = "(select t1.noter9640 from tbl_9640 t1 where t1.idtn9640 = idtn9621 and t1.crivw9640 = crivw9621)")
	@Basic(fetch = FetchType.LAZY)
	private String note;
	@Formula(value = "(select t1.noter9641 from tbl_9641 t1 where t1.cpu9641 = (select t2.cpu9607 from tbl_9607 t2 where t2.coddl9607 = coddl9621 and t2.idtn9607 = idtn9621) and t1.crivw9641 = crivw9621)")
	@Basic(fetch = FetchType.LAZY)
	private String noteByCpu;
	@Formula(value = "(select t2.idtni9605 from tbl_9605 t2 where t2.coddl9605 = coddl9621 and t2.idtn9605 = idtn9621)")
	@Basic(fetch = FetchType.LAZY)
	private String idtnTrascodifica;
	@Formula(value = "(select sum(t1.quar9310-t1.quae9310) from tbl_9310 t1 inner join tbl_9305 t4 on t1.ccli9310 = t4.ccli9305 where t1.codl9310 = coddl9621 and t1.crivw9310 = crivw9621 and t1.idtn9310 = idtn9621 and ((t4.dtsopreda9305 is null and t4.dtsopreda9305 is null) or (sysdate not between t4.dtsopreda9305 and t4.dtsoprea9305)))")
	@Basic(fetch = FetchType.LAZY)
	private Integer ordini;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9621", updatable = false, insertable = false, referencedColumnName = "coddl9618"),
		@JoinColumn(name = "crivw9621", updatable = false, insertable = false, referencedColumnName = "crivw9618"),
		@JoinColumn(name = "idtn9621", updatable = false, insertable = false, referencedColumnName = "idtn9618") 
	})
	private ContoDepositoVo contoDeposito;
	
	public Long getDistribuito() {
		return (distribuito == null) ? 0 : distribuito;
	}
	
	public BigDecimal getImportoLordo() {
		return (getPrezzoLordo() != null && reso != null) ? getPrezzoLordo().multiply(new BigDecimal(reso)) : new BigDecimal(0);
	}
	
	public BigDecimal getImportoNetto() {
		return (getPrezzoNetto() != null && reso != null) ? getPrezzoNetto().multiply(new BigDecimal(reso)) : new BigDecimal(0);
	}

	public BigDecimal getPrezzoLordo() {
		return bollaResa.getPrezzoLordo();
	}
	
	public String getBarcode() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getCodiceBarre() : null;
	}
	
	public String getImmagine() {
		return (storicoCopertineVo != null && storicoCopertineVo.getImmagine() != null) ? storicoCopertineVo.getImmagine().getNome() : "";
	}
	
	public String getTitolo() {
		return bollaResa.getTitolo();
	}

	public String getSottoTitolo() {
		return bollaResa.getSottoTitolo();
	}

	public String getNumeroPubblicazione() {
		return bollaResa.getNumeroPubblicazione() != null ? bollaResa.getNumeroPubblicazione().trim() : bollaResa.getNumeroPubblicazione();
	}
	
	public Timestamp getDataUscita() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getDataUscita() : null;
	}

	public String getTipoRichiamo() {
		return null;
	}

	public Integer getRiga() {
		return pk.getPosizioneRiga();
	}
	
	public Integer getCpuDl() {
		return bollaResa.getCpuDl();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (obj != null && obj instanceof BollaResaDettaglioVo) {
			BollaResaDettaglioVo vo = (BollaResaDettaglioVo) obj;
			if (getPk().equals(vo.getPk())) {
				equal = true;
			}
		}
		return equal;
	}
}
