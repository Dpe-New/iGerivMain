package it.dpe.igeriv.vo;

import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 *
 */
@Getter
@Setter
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_RICHIESTA_RIFORNIMENTO_LIVELLAMENTI_CON_DETTAGLI, query = "select vo from RichiestaRifornimentoLivellamentiVo vo join fetch vo.livellamenti li join fetch vo.edicolaRichiedente er join fetch vo.storicoCopertineVo sc where er.codEdicola in (:codEdicola) and sc.pk.codDl in (:codDl) and sc.pk.idtn = :idtn and vo.stato in (:stato)"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_RICHIESTA_RIFORNIMENTO_LIVELLAMENTI_CON_DETTAGLI_BY_EDICOLA_VENDITRICE, query = "select vo from RichiestaRifornimentoLivellamentiVo vo join fetch vo.livellamenti li join fetch vo.edicolaAccettataVenduto er join fetch vo.storicoCopertineVo sc where er.codEdicola = :codEdicola and sc.pk.codDl = :codDl and sc.pk.idtn = :idtn")
})
@Entity
@Table(name = "tbl_9131a", schema = "")
public class RichiestaRifornimentoLivellamentiVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idric9131a")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9131a", allocationSize = 1)
	private Long idRichiestaLivellamento;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9131a", updatable = true, insertable = true, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicolaRichiedente;
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true, fetch=FetchType.LAZY, targetEntity = LivellamentiVo.class, mappedBy = "richiesta")
	private List<LivellamentiVo> livellamenti;
	@Column(name = "coddl9131a")
	private Integer coddl;
	@Column(name = "idtn9131a")
	private Integer idtn;
	@Column(name = "copie9131a")
	private Integer quantitaRichiesta;
	@Column(name = "datri9131a")
	private Timestamp dataRichiesta;
	@Column(name = "stato9131a")
	private StatoRichiestaRifornimentoLivellamento stato;
	@Column(name = "itrdl9131a")
	@Getter(AccessLevel.NONE)
	private Boolean recordTrasferitoDl;
	@Column(name = "dtrdl9131a")
	private Timestamp dataTrasferitoDl;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9131a", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9131a", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivwv9131a", updatable = true, insertable = true, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicolaAccettataVenduto;
	
	@Formula(value = "(select count(distinct(t1.CRIVW9131B)) from tbl_9131b t1 where t1.IDRIC9131B = idric9131a)")
	@Basic(fetch = FetchType.LAZY)
	private Integer totRichiesteInviate;
	
	@Formula(value = "(select sum(count(distinct(t1.CRIVW9131B))) from tbl_9131b t1 where t1.IDRIC9131B = idric9131a and t1.STATOV9131B > 0 GROUP BY t1.CRIVW9131B)")
	@Basic(fetch = FetchType.LAZY)
	private Integer totRichiesteEsaminate;
	
	@Formula(value = "(select sum(count(distinct(t1.CRIVW9131B))) from tbl_9131b t1 where t1.IDRIC9131B = idric9131a and t1.STATOV9131B = 1 GROUP BY t1.CRIVW9131B)")
	@Basic(fetch = FetchType.LAZY)
	private Integer totRichiesteAccettate;

	
//	@Formula(value = "(select count(1) from tbl_9131b t1 where t1.coddl9605 = coddl9626 and t1.idtn9605 = idtn9626)")
//	@Basic(fetch = FetchType.LAZY)
//	private Integer totRichiesteInviate;
	
	
	public String getStatoDesc() {
		if (stato == null) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_INSERITO);
		} else if (stato.equals(IGerivConstants.STATO_INVIATO_DL_SIGLA)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_INVIATO_DL);
		} else if (stato.equals(IGerivConstants.STATO_PRONTO_PER_INVIO_SIGLA)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_PRONTO_PER_INVIO);
		}
		return null;
	}
	
	public String getTitolo() {
		return (getStoricoCopertineVo() != null && getStoricoCopertineVo().getAnagraficaPubblicazioniVo() != null) ? getStoricoCopertineVo().getAnagraficaPubblicazioniVo().getTitolo() : null;
	}
	
	public String getSottoTitolo() {
		return getStoricoCopertineVo() != null ? getStoricoCopertineVo().getSottoTitolo() : null;
	}
	
	public String getNumeroCopertina() {
		return getStoricoCopertineVo() != null ? getStoricoCopertineVo().getNumeroCopertina() : null;
	}
	
	public BigDecimal getPrezzoCopertina() {
		return getStoricoCopertineVo() != null ? getStoricoCopertineVo().getPrezzoCopertina() : null;
	}

	public Boolean getRecordTrasferitoDl() {
		return recordTrasferitoDl == null ? false : recordTrasferitoDl;
	}
	
}	
