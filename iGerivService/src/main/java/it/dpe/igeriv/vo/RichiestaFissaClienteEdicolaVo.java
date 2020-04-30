package it.dpe.igeriv.vo;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.RichiestaFissaClienteEdicolaPk;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9313", schema = "")
public class RichiestaFissaClienteEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private RichiestaFissaClienteEdicolaPk pk;
	@Column(name = "quar9313")
	private Integer quantitaRichiesta;
	@Column(name = "itdl9313")
	private Integer indicatoreTrasmessoDl;
	@Column(name = "dari9313")
	private Date dataRichiesta;
	@Transient
	private boolean enabled = true;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "codl9313", updatable = false, insertable = false, referencedColumnName = "coddl9606"),
		@JoinColumn(name = "cpu9313", updatable = false, insertable = false, referencedColumnName = "cpu9606")
	})
	private AnagraficaPubblicazioniVo anagraficaPubblicazioniVo;
	@Column(name = "nvol9313")
	private Integer numeroVolte;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ccli9313", updatable = false, insertable = false, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo clienteEdicolaVo;
	
	public String getTitolo() {
		return (anagraficaPubblicazioniVo != null) ? anagraficaPubblicazioniVo.getTitolo() : "";
	}
	
	public String getSottoTitolo() {
		return "";
	}
	
	public String getNumeroCopertina() {
		return "";
	}
	
	public java.util.Date getDataOrdine() {
		return dataRichiesta;
	}
	
	public Integer getQuantitaEvasa() {
		return null;
	}
		
	public String getStatoEvasioneDesc() {
		return IGerivMessageBundle.get(IGerivConstants.STATO_PRENOTAZIONE_FISSA);
	}
	
	public String getProvenienzaDesc() {
		return IGerivMessageBundle.get(IGerivConstants.PROVENIENZA_RICHIESTA_CLIENTE);
	}
	
	public Integer getStatoEvasione() {
		return IGerivConstants.COD_STATO_EVASIONE_PREOTAZIONE_FISSA;
	}

	public Integer getProvenienza() {
		return IGerivConstants.COD_PROVENIENZA_RICHIESTA_CLIENTE;
	}
}
