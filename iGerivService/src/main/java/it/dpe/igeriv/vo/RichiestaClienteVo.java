package it.dpe.igeriv.vo;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;

import java.sql.Timestamp;

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

import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_RICHIESTA_CLIENTE_EDICOLA, query = "update RichiestaClienteVo vo set vo.statoEvasione = :statoEvasione, vo.quantitaEvasa = vo.quantitaEvasa + :quantitaEvasa, vo.codiceUltimaRisposta = :codiceUltimaRisposta, vo.descrizioneUltimaRisposta = :descrizioneUltimaRisposta where vo.pk.codEdicola = :codEdicola and vo.pk.codCliente = :codCliente and vo.pk.provenienza = :provenienza and vo.pk.dataInserimento = :dataInserimento and vo.pk.codDl = :codDl and vo.pk.idtn = :idtn")
})
@Table(name = "tbl_9310", schema = "")
public class RichiestaClienteVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private RichiestaClientePk pk;
	@Column(name = "quar9310")
	private Integer quantitaRichiesta;
	@Column(name = "quae9310")
	private Integer quantitaEvasa;
	@Column(name = "stat9310")
	private Integer statoEvasione;
	@Column(name = "irdl9310")
	private Integer richiedereDifferenzaDl;
	@Column(name = "dari9310")
	private Timestamp dataUltimaRisposta;
	@Column(name = "codr9310")
	private Integer codiceUltimaRisposta;
	@Column(name = "desr9310")
	private String descrizioneUltimaRisposta;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ccli9310", updatable = false, insertable = false, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo clienteEdicolaVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "codl9310", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9310", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Transient
	private boolean enabled = true;
	@Column(name = "flnot9310")
	private Boolean notificareOrdineRivendita;
	
	public Integer getQuantitaDaEvadere() {
		return (quantitaRichiesta != null && quantitaEvasa != null) ? quantitaRichiesta - quantitaEvasa : 0;
	}
	
	public String getStatoEvasioneDesc() {
		if (statoEvasione.equals(IGerivConstants.COD_STATO_EVASIONE_COMPLETA)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_COMPLETA);
		} else if (statoEvasione.equals(IGerivConstants.COD_STATO_EVASIONE_INSERITO)) {
			return  IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_INSERITO);
		} else if (statoEvasione.equals(IGerivConstants.COD_STATO_EVASIONE_PARZIALE)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_PARZIALE);
		}
		return null;
	}
	
	public String getTitolo() {
		return (storicoCopertineVo != null && storicoCopertineVo.getAnagraficaPubblicazioniVo() != null) ? storicoCopertineVo.getAnagraficaPubblicazioniVo().getTitolo() : "";
	}
	
	public String getSottoTitolo() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getSottoTitolo() : "";
	}
	
	public String getNumeroCopertina() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getNumeroCopertina() : "";
	}
	
	public java.util.Date getDataOrdine() {
		return pk.getDataInserimento();
	}
	
	public String getProvenienzaDesc() {
		if (getPk().getProvenienza().equals(IGerivConstants.COD_PROVENIENZA_RICHIESTA_AUTOMATICA)) {
			return IGerivMessageBundle.get(IGerivConstants.PROVENIENZA_RICHIESTA_AUTOMATICA);
		} else if (getPk().getProvenienza().equals(IGerivConstants.COD_PROVENIENZA_RICHIESTA_CLIENTE)) {
			return IGerivMessageBundle.get(IGerivConstants.PROVENIENZA_RICHIESTA_CLIENTE);
		} else if (getPk().getProvenienza().equals(IGerivConstants.COD_PROVENIENZA_RICHIESTA_EDICOLA)) {
			return IGerivMessageBundle.get(IGerivConstants.PROVENIENZA_RICHIESTA_EDICOLA);
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		return getPk().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof RichiestaClienteVo) {
			return getPk().equals(((RichiestaClienteVo)obj).getPk());
		}
		return super.equals(obj);
	}

	
}
