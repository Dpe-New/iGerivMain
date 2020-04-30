package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.BollaDettaglioPk;

import java.math.BigDecimal;

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
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_DIFFERENZE_SPUNTE, query = "update BollaDettaglioVo vo set vo.differenze = :differenze, vo.spunta = :spunta where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.pk.posizioneRiga = :posizioneRiga and not exists (from vo.bolla b where b.pubblicazioneNonUscita = true)"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_DIFFERENZE_COPIE_SPUNTATE, query = "update BollaDettaglioVo vo set vo.differenze = :differenze, vo.quantitaSpuntata = :qtaSpunta, vo.spunta = :spunta where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.pk.posizioneRiga = :posizioneRiga")
})
@Table(name = "tbl_9611", schema = "")
public class BollaDettaglioVo extends BaseVo implements BollaDettaglio {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaDettaglioPk pk;
	@Column(name = "idtn9611")
	private Integer idtn;
	@Column(name = "plorl9611")
	private BigDecimal prezzoLordo;
	@Column(name = "prnel9611")
	private BigDecimal prezzoNetto;
	@Column(name = "ivalo9611")
	private Integer indicatoreValorizzare;
	@Column(name = "indpv9611")
	private String indicatorePrezzoVariato;
	@Column(name = "scont9611")
	private BigDecimal sconto;
	@Column(name = "quant9611")
	private Integer quantitaConsegnata;
	@Column(name = "quans9611")
	private Integer quantitaSpuntata;
	@Column(name = "diffe9611")
	private Integer differenze;
	@Column(name = "spunt9611")
	private Integer spunta;
	@Column(name = "agfco9611")
	private Boolean aggiuntaFuoriCompetenza;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9611", updatable = false, insertable = false, referencedColumnName = "coddl9609"),
		@JoinColumn(name = "datbc9611", updatable = false, insertable = false, referencedColumnName = "datbc9609"),
		@JoinColumn(name = "tipbc9611", updatable = false, insertable = false, referencedColumnName = "tipbc9609"),
		@JoinColumn(name = "posiz9611", updatable = false, insertable = false, referencedColumnName = "posiz9609")
	})
	private BollaVo bolla;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9611", updatable = false, insertable = false, referencedColumnName = "coddl9612B"),
		@JoinColumn(name = "crivw9611", updatable = false, insertable = false, referencedColumnName = "crivw9612B"),
		@JoinColumn(name = "datbc9611", updatable = false, insertable = false, referencedColumnName = "datbc9612B"),
		@JoinColumn(name = "tipbc9611", updatable = false, insertable = false, referencedColumnName = "tipbc9612B"),
		@JoinColumn(name = "idtn9611", updatable = false, insertable = false, referencedColumnName = "idtn9612B")
	})
	private VariazioneNotturnaVo variazione;
	@Transient
	private BigDecimal importo;
	@Formula(value = "(select sum(t1.quar9310-t1.quae9310) from tbl_9310 t1 inner join tbl_9305 t4 on t1.ccli9310 = t4.ccli9305 where t1.codl9310 = coddl9611 and t1.crivw9310 = crivw9611 and t1.idtn9310 = idtn9611 and ((t4.dtsopreda9305 is null and t4.dtsopreda9305 is null) or (sysdate not between t4.dtsopreda9305 and t4.dtsoprea9305)))")
	@Basic(fetch = FetchType.LAZY)
	private Integer ordini;
	@Formula(value = "(select t2.noter9640 from tbl_9640 t2 where t2.idtn9640 = idtn9611 and t2.crivw9640 = crivw9611)")
	@Basic(fetch = FetchType.LAZY)
	private String note;
	@Formula(value = "(select t2.noter9641 from tbl_9641 t2 where t2.cpu9641 = (select t3.cpu9607 from tbl_9607 t3 where t3.coddl9607 = coddl9611 and t3.idtn9607 = idtn9611) and t2.crivw9641 = crivw9611)")
	@Basic(fetch = FetchType.LAZY)
	private String noteByCpu;
	@Formula(value = "(select t3.idtni9605 from tbl_9605 t3 where t3.coddl9605 = coddl9611 and t3.idtn9605 = idtn9611)")
	@Basic(fetch = FetchType.LAZY)
	private String idtnTrascodifica;
	@Transient
	private String tipoFondoBolla;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9611", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9611", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Formula(value = "(P_BOLLA.GET_FORNITO(coddl9611,crivw9611,idtn9611))")
	@Basic(fetch = FetchType.LAZY)
	private Long fornito;
	@Formula(value = "(P_BOLLA.GET_RESO(coddl9611,crivw9611,idtn9611))")
	@Basic(fetch = FetchType.LAZY)
	private Long reso;
	@Formula(value = "(select 1 from tbl_9618 t1 where t1.coddl9618 = coddl9611 and t1.crivw9618 = crivw9611 and t1.idtn9618 = idtn9611 and t1.quant9618 > 0)")
	@Basic(fetch = FetchType.LAZY)
	private Boolean contoDeposito;
	
	public BigDecimal getImporto() {
		BigDecimal bigDecimal = importo = (indicatoreValorizzare == IGerivConstants.INDICATORE_VALORIZZARE) ? prezzoNetto.multiply(new BigDecimal(quantitaConsegnata)) : new BigDecimal(0);
		return bigDecimal;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	
	public String getBarcode() {
		return (bolla.getStoricoCopertineVo() != null) ? bolla.getStoricoCopertineVo().getCodiceBarre() : null;
	}
	
	public String getImmagine() {
		return (bolla.getStoricoCopertineVo() != null && bolla.getStoricoCopertineVo().getImmagine() != null) ? bolla.getStoricoCopertineVo().getImmagine().getNome() : "";
	}
	
	public String getTipoFondoBolla() {
		return tipoFondoBolla;
	}

	public void setTipoFondoBolla(String tipoFondoBolla) {
		this.tipoFondoBolla = tipoFondoBolla;
	}
	
	public String getTitolo() {
		return bolla.getTitolo();
	}

	public String getSottoTitolo() {
		return bolla.getSottoTitolo();
	}

	public String getNumeroPubblicazione() {
		return bolla.getNumeroPubblicazione() != null ? bolla.getNumeroPubblicazione().trim() : bolla.getNumeroPubblicazione();
	}

	public BigDecimal getPercentualeIva() {
		return bolla.getPercentualeIva();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (obj != null && obj instanceof BollaDettaglioVo) {
			BollaDettaglio vo = (BollaDettaglio) obj;
			if (getPk().equals(vo.getPk())) {
				equal = true;
			}
		}
		return equal;
	}
	
}
