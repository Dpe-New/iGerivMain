package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.BollaResaRichiamoPersonalizzatoPk;

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
 * Tabella dati di dettaglio del richiamo personalizzato.
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_RICHIAMO_PERSONALIZZATO_BOLLA_RESA_BY_CPU, query = "from BollaResaRichiamoPersonalizzatoVo vo join fetch vo.storicoCopertineVo sc join fetch sc.anagraficaPubblicazioniVo ap join fetch ap.periodicitaVo per where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.cpuDl = :cpuDl order by vo.pk.posizioneRiga asc"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RESA_DETTAGLIO_RICHIAMO, query = "update BollaResaRichiamoPersonalizzatoVo vo set vo.reso = :reso where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.pk.posizioneRiga = :posizioneRiga"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_LAST_POSIZIONE_RIGA_BOLLA_RICHIAMO_PERSONALIZZATO, query = "select max(vo.pk.posizioneRiga) from BollaResaRichiamoPersonalizzatoVo vo where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla"),
})
@Table(name = "tbl_9622", schema = "")
public class BollaResaRichiamoPersonalizzatoVo extends BaseVo implements BollaResa {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaResaRichiamoPersonalizzatoPk pk;
	@Column(name = "tagr9622")
	private Integer aggiuntaTipo;
	@Column(name = "idtn9622")
	private Integer idtn;
	@Column(name = "cpu9622")
	private Integer cpuDl;
	@Column(name = "num9622")
	@Getter(AccessLevel.NONE)
	private String numeroPubblicazione;
	@Column(name = "titolo9622")
	private String titolo;
	@Column(name = "sottot9622")
	private String sottoTitolo;
	@Column(name = "plor9622")
	private BigDecimal prezzoLordo;
	@Column(name = "prne9622")
	private BigDecimal prezzoNetto;
	@Column(name = "qdist9622")
	private Long distribuito;
	@Column(name = "qreso9622")
	private Integer reso;
	@Column(name = "qresor9622")
	private Integer resoRiscontrato;
	@Column(name = "cesta9622")
	private String cesta;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9622", updatable = false, insertable = false, referencedColumnName = "coddl9624"),
		@JoinColumn(name = "tagr9622", updatable = false, insertable = false, referencedColumnName = "trr9624") 
	})
	private DecodificaRichiamiResaVo numeroRichiamo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9622", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9622", updatable = false, insertable = false, referencedColumnName = "idtn9607") 
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Formula(value = "P_BOLLA.GET_GIACENZA(coddl9622, crivw9622, idtn9622, null)")
	@Basic(fetch = FetchType.LAZY)
	private Long giacenza;
	@Formula(value = "(select t1.idtni9605 from tbl_9605 t1 where t1.coddl9605 = coddl9622 and t1.idtn9605 = idtn9622)")
	@Basic(fetch = FetchType.LAZY)
	private String idtnTrascodifica;
	@Transient
	private Integer quantitaCopieContoDeposito;
	@Transient
	@Getter(AccessLevel.NONE)
	private Boolean numeroInContoDeposito;
	@Transient
	private String motivoResaRespinta;
	@Transient
	private Integer codMotivoRespinto;
	@Transient
	private Timestamp dataRichiamoResa;
	@Transient
	private String barcodeStr;
	
	public String getNumeroPubblicazione() {
		return numeroPubblicazione != null ? numeroPubblicazione.trim() : numeroPubblicazione;
	}
	
	@Transient
	public BigDecimal getImportoLordo() {
		return (getPrezzoLordo() != null && reso != null) ? getPrezzoLordo().multiply(new BigDecimal(reso)) : new BigDecimal(0);
	}
	
	@Transient
	public BigDecimal getImportoNetto() {
		return (getPrezzoNetto() != null && reso != null) ? getPrezzoNetto().multiply(new BigDecimal(reso)) : new BigDecimal(0);
	}
	
	public Integer getResoRiscontrato() {
		return (resoRiscontrato == null) ? 0 : resoRiscontrato;
	}
	
	@Transient
	public Timestamp getDataUscita() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getDataUscita() : null;
	}

	@Transient
	public String getTipoRichiamo() {
		return (numeroRichiamo != null) ? numeroRichiamo.getDescrizione() : null;
	}

	@Transient
	public Integer getRiga() {
		return pk.getPosizioneRiga();
	}
	
	@Transient
	public String getImmagine() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getImmagine().getNome() : "";
	}
	
	@Transient
	public String getBarcode() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getCodiceBarre() : null;
	}
	
	public Boolean getNumeroInContoDeposito() {
		return (numeroInContoDeposito == null) ? false : numeroInContoDeposito;
	}
}
