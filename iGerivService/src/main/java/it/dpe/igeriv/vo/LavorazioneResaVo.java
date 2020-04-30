package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella con le lavorazioni delle rese delle edicole dal redic.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NamedQueries({
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_LAVORAZIONE_RESA_BY_ID, query = "from LavorazioneResaVo vo where vo.nomeFile = :nomeFile") 
})
@Table(name = "tbl_9705", schema = "")
public class LavorazioneResaVo extends BaseVo implements BollaResaRiassunto {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "nomfil9705")
	private String nomeFile;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9705", updatable = true, insertable = true, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo anagraficaEdicolaVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "coddl9705", updatable = true, insertable = true, referencedColumnName = "coddl9107")	
	private AnagraficaAgenziaVo anagraficaAgenziaVo;
	@Column(name = "datres9705")
	private Date dataResa;
	@Column(name = "tipres9705")
	private String tipoResa;
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true, targetEntity = LavorazioneResaImmagineVo.class, mappedBy = "pk.lavorazioneResaVo")
	private List<LavorazioneResaImmagineVo> listLavorazioneResaImmagineVo;

	@Override
	public Timestamp getDtBolla() {
		return new Timestamp(getDataResa().getTime());
	}

	@Override
	public String getTipoBolla() {
		return getTipoResa();
	}

	@Override
	public Integer getGruppoSconto() {
		return -1;
	}

	@Override
	public Integer getBollaTrasmessaDl() {
		return -1;
	}
}
