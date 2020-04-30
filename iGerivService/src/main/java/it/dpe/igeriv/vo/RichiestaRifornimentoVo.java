package it.dpe.igeriv.vo;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.RichiestaRifornimentoPk;

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
@Table(name = "tbl_9131", schema = "")
public class RichiestaRifornimentoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private RichiestaRifornimentoPk pk;
	@Column(name = "datscad9131")
	private Timestamp dataScadenzaRichiesta;
	@Column(name = "quaric9131")
	private Integer quantitaRichiesta;
	@Column(name = "quaev9131")
	private Integer quantitaEvasa;
	@Column(name = "stato9131")
	private String stato;
	@Column(name = "datarisp9131")
	private Timestamp dataRispostaDl;
	@Column(name = "acausne9131")
	private String descCausaleNonEvadibilita;
	@Column(name = "datainvio9131")
	private Timestamp dataInvioRichiesta;
	@Column(name = "quagcl9131")
	private Integer quantitaRichiestaGestioneClienti;
	@Column(name = "note9131")
	private String noteVendita;
	@Column(name = "isosp9131")
	private Integer richiestaSospesa;
	@Transient
	private String richiestaSospesaDesc;
	@Column(name = "prove9131")
	private Integer provenienza;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9131", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9131", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Transient
	private Long fornito;
	@Transient
	private Integer prenotazione;
	@Column(name = "ngval9131")
	private Integer giorniValiditaRichiesteRifornimento;
	@Column(name = "flstat9131")
	private Integer flagStato;
	@Column(name = "ordine9131")
	private Integer ordine;
	@Transient
	private boolean enabled = true;
	@Formula(value = "(select t1.idtni9605 from tbl_9605 t1 where t1.coddl9605 = coddl9131 and t1.idtn9605 = idtn9131)")
	@Basic(fetch = FetchType.LAZY)
	private String idtnTrascodifica;
	@Column(name = "dboladd9131")
	private Timestamp dataBollaAddebito;
	
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
		return (getStoricoCopertineVo() != null && getStoricoCopertineVo().getAnagraficaPubblicazioniVo() != null) ? getStoricoCopertineVo().getAnagraficaPubblicazioniVo().getTitolo() : "";
	}
	
	public String getSottoTitolo() {
		return getStoricoCopertineVo() != null ? getStoricoCopertineVo().getSottoTitolo() : "";
	}
	
	public String getNumeroCopertina() {
		return getStoricoCopertineVo() != null ? getStoricoCopertineVo().getNumeroCopertina() : "";
	}
	
	public BigDecimal getPrezzoCopertina() {
		return getStoricoCopertineVo() != null ? getStoricoCopertineVo().getPrezzoCopertina() : BigDecimal.ZERO;
	}
	
	public Timestamp getDataOrdine() {
		return getPk().getDataOrdine();
	}
	
	@Override
	public int hashCode() {
		return getPk().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof RichiestaRifornimentoVo) {
			RichiestaRifornimentoVo obj2 = (RichiestaRifornimentoVo) obj;
			return this.getPk().equals(obj2.getPk());
		}
		return super.equals(obj);
	}
	
}	
