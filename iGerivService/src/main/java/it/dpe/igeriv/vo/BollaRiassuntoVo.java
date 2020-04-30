package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.BollaRiassuntoPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AccessLevel;
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
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_BOLLE_RIASSUNTO, query = "from BollaRiassuntoVo vo where vo.pk.codFiegDl in (:codFiegDl) and vo.pk.codEdicola in (:codEdicola) and vo.pk.dtBolla >= :dtBolla order by vo.pk.dtBolla desc"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_BOLLE_RIASSUNTO_EDICOLA_BY_DATE, query = "from BollaRiassuntoVo vo where vo.pk.codFiegDl in (:codFiegDl) and vo.pk.codEdicola in (:codEdicola) and vo.pk.dtBolla = :dtBolla order by vo.pk.dtBolla desc"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_BOLLE_RIASSUNTO_EDICOLE_BY_DATE, query = "from BollaRiassuntoVo vo join fetch vo.abbinamentoEdicolaDlVo join fetch vo.abbinamentoEdicolaDlVo.anagraficaEdicolaVo where vo.pk.codFiegDl in (:codFiegDl) and vo.pk.dtBolla = :dtBolla order by vo.abbinamentoEdicolaDlVo.codEdicolaDl desc"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_BOLLA_RIASSUNTO_BY_ID, query = "from BollaRiassuntoVo vo where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RIASSUNTO, query = "update BollaRiassuntoVo vo set vo.bollaTrasmessaDl = :bollaTrasmessaDl where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla")
})
@Table(name = "tbl_9610", schema = "")
public class BollaRiassuntoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaRiassuntoPk pk;
	@Column(name = "datbr9610")
	private Timestamp dtBollaResa;
	@Column(name = "tipbr9610")
	private String tipoBollaResa;
	@Column(name = "nvoci9610")
	private Integer numVoci;
	@Column(name = "vboll9610")	
	private BigDecimal valoreBolla;
	@Column(name = "grusc9610")
	private Integer gruppoSconto;
	@Column(name = "ibotr9610")	
	private Integer bollaTrasmessaDl;
	@Column(name = "datra9610")	
	private Timestamp dtTrasmissione;
	@Column(name = "norif9610")
	private Integer numeroOrdine;
	@Column(name = "zonam9610")	
	private Integer zonaStampaMessaggi;
	@Column(name = "msgbl9610")	
	@Getter(AccessLevel.NONE)
	private Boolean messaggiInBollaLetti;
	@Column(name = "dareg9610")
	private Timestamp dataRegistrazioneDocumento;
	@Formula(value = "(select sum(t1.quant9611) from tbl_9611 t1 where t1.coddl9611 = coddl9610 and t1.crivw9611 = crivw9610 and t1.datbc9611 = datbc9610 and t1.tipbc9611 = tipbc9610)")
	@Basic(fetch = FetchType.LAZY)
	private Long totaleCopieConsegnate;
	@Formula(value = "(select sum(t1.diffe9611) from tbl_9611 t1 where t1.coddl9611 = coddl9610 and t1.crivw9611 = crivw9610 and t1.datbc9611 = datbc9610 and t1.tipbc9611 = tipbc9610)")
	@Basic(fetch = FetchType.LAZY)
	private Long totaleDifferenze;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9610", updatable = false, insertable = false, referencedColumnName = "crivw9206")	
	private AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVo;
	
	public String getDtAndTipoBolla() {
		return DateUtilities.getTimestampAsString(getPk().getDtBolla(), DateUtilities.FORMATO_DATA) + " " + IGerivConstants.TIPO + " " + getPk().getTipoBolla();
	}

	public Boolean getMessaggiInBollaLetti() {
		return messaggiInBollaLetti == null ? false : messaggiInBollaLetti;
	}
	
	public String getDataRegistrazioneDocumentoFormat() {
		return getDataRegistrazioneDocumento() != null ? DateUtilities.getTimestampAsString(getDataRegistrazioneDocumento(), DateUtilities.FORMATO_DATA_SLASH) : "";
	}
	
	public String getOraRegistrazioneDocumentoFormat() {
		return getDataRegistrazioneDocumento() != null ? DateUtilities.getTimestampAsString(getDataRegistrazioneDocumento(), DateUtilities.FORMATO_ORA_HHMM_COLON) : "";
	}
	
}
