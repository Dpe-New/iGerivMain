package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.BollaResaRiassuntoPk;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_BOLLE_RESA_RIASSUNTO, query = "from BollaResaRiassuntoVo vo where vo.pk.codFiegDl in (:codFiegDl) and vo.pk.codEdicola in (:codEdicola) and vo.pk.dtBolla >= :dtBolla order by vo.pk.dtBolla desc"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_BOLLE_RESA_RIASSUNTO_EDICOLA_BY_DATE, query = "from BollaResaRiassuntoVo vo where vo.pk.codFiegDl in (:codFiegDl) and vo.pk.codEdicola in (:codEdicola) and vo.pk.dtBolla = :dtBolla order by vo.pk.dtBolla desc"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_BOLLE_RESA_RIASSUNTO_EDICOLE_BY_DATE, query = "from BollaResaRiassuntoVo vo join fetch vo.abbinamentoEdicolaDlVo join fetch vo.abbinamentoEdicolaDlVo.anagraficaEdicolaVo where vo.pk.codFiegDl in (:codFiegDl) and vo.pk.dtBolla = :dtBolla order by vo.abbinamentoEdicolaDlVo.codEdicolaDl desc"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_BOLLA_RESA_RIASSUNTO_BY_ID, query = "from BollaResaRiassuntoVo vo where vo.pk.codFiegDl in (:codFiegDl) and vo.pk.codEdicola in (:codEdicola) and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RESA_RIASSUNTO, query = "update BollaResaRiassuntoVo vo set vo.bollaTrasmessaDl = :bollaTrasmessaDl where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla")
})
@Table(name = "tbl_9620", schema = "")
public class BollaResaRiassuntoVo extends BaseVo implements BollaResaRiassunto {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaResaRiassuntoPk pk;
	@Column(name = "nvoci9620")
	private Integer numVoci;
	@Column(name = "vboll9620")	
	private BigDecimal valoreBolla;
	@Column(name = "grusc9620")	
	private Integer gruppoSconto;
	@Column(name = "iretr9620")	
	private Integer bollaTrasmessaDl;
	@Column(name = "datra9620")	
	private Timestamp dtTrasmissione;
	@Formula(value = "(select sum(t1.qreso9621) from tbl_9621 t1 where t1.coddl9621 = coddl9620 and t1.crivw9621 = crivw9620 and t1.datbr9621 = datbr9620 and t1.tipbr9621 = tipbr9620)")
	@Basic(fetch = FetchType.LAZY)
	private Integer totaleCopieBollaResaDettaglio;
	@Formula(value = "(select sum(t2.prne9621 * t2.qreso9621) from tbl_9621 t2 where t2.coddl9621 = coddl9620 and t2.crivw9621 = crivw9620 and t2.datbr9621 = datbr9620 and t2.tipbr9621 = tipbr9620)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal totaleBollaResaDettaglio;
	@Formula(value = "(select sum(t3.qreso9622b) from tbl_9622b t3 where t3.coddl9622b = coddl9620 and t3.crivw9622b = crivw9620 and t3.datbr9622b = datbr9620 and t3.tipbr9622b = tipbr9620)")
	@Basic(fetch = FetchType.LAZY)
	private Integer totaleCopieBollaFuoriResa;
	@Formula(value = "(select sum(t4.prne9622b * t4.qreso9622b) from tbl_9622b t4 where t4.coddl9622b = coddl9620 and t4.crivw9622b = crivw9620 and t4.datbr9622b = datbr9620 and t4.tipbr9622b = tipbr9620)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal totaleBollaFuoriResa;
	@Formula(value = "(select sum(t5.qreso9622) from tbl_9622 t5 where t5.coddl9622 = coddl9620 and t5.crivw9622 = crivw9620 and t5.datbr9622 = datbr9620 and t5.tipbr9622 = tipbr9620)")
	@Basic(fetch = FetchType.LAZY)
	private Integer totaleCopieBollaResaDimenticata;
	@Formula(value = "(select sum(t6.prne9622 * t6.qreso9622) from tbl_9622 t6 where t6.coddl9622 = coddl9620 and t6.crivw9622 = crivw9620 and t6.datbr9622 = datbr9620 and t6.tipbr9622 = tipbr9620)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal totaleBollaResaDimenticata;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9620", updatable = false, insertable = false, referencedColumnName = "crivw9206")	
	private AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVo;
	@Formula(value = "(select t7.crivw9228 from tbl_9228 t7 where t7.crivw9228 = crivw9620 and datbr9620 between t7.din9228 and t7.dif9228)")
	@Basic(fetch = FetchType.LAZY)
	private Integer edicolaInFerie;
	@Column(name = "dtmein9620")	
	private Timestamp dtMemorizzazioneInvio;
	
	@Formula(value = "(select count(1) from tbl_9621 t1 where t1.coddl9621 = coddl9620 and t1.crivw9621 = crivw9620 and t1.datbr9621 = datbr9620 and t1.tipbr9621 = tipbr9620 and t1.QDIST9621 > 0 )")
	@Basic(fetch = FetchType.LAZY)
	private Integer ver_totaleBolleResaDettaglio;
	@Formula(value = "(select count(1) from tbl_9622 t6 where t6.coddl9622 = coddl9620 and t6.crivw9622 = crivw9620 and t6.datbr9622 = datbr9620 and t6.tipbr9622 = tipbr9620)")
	@Basic(fetch = FetchType.LAZY)
	private Integer ver_totaleBollaResaDimenticata;
	
	
	
	
	
	@Transient
	public String getDtAndTipoBolla() {
		return DateUtilities.getTimestampAsString(getPk().getDtBolla(), DateUtilities.FORMATO_DATA) + " " + IGerivConstants.TIPO + " " + getPk().getTipoBolla();
	}
	
	@Transient
	public String getDescFerie()
	{
		if(edicolaInFerie!= null)
		{
			return "F";
		}
		return "";
	}

	@Override
	@Transient
	public Timestamp getDtBolla() {
		return getPk().getDtBolla();
	}

	@Override
	@Transient
	public String getTipoBolla() {
		return getPk().getTipoBolla();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BollaResaRiassuntoVo other = (BollaResaRiassuntoVo) obj;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		return true;
	}
	
	
	
}
