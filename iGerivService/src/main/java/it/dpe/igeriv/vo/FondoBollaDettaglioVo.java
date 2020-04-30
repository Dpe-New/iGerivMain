package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.FondoBollaDettaglioPk;

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
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_DIFFERENZE_SPUNTE_FONDO_BOLLA, query 			= "update FondoBollaDettaglioVo vo set vo.differenze = :differenze, vo.spunta = :spunta 		  where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.pk.posizioneRiga = :posizioneRiga"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_DIFFERENZE_COPIE_SPUNTATE_FONDO_BOLLA, query 	= "update FondoBollaDettaglioVo vo set vo.differenze = :differenze, vo.quantitaSpuntata = :spunta where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.pk.posizioneRiga = :posizioneRiga"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_NOTE_FONDO_BOLLA, query = "update FondoBollaDettaglioVo vo set vo.note = :note where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla and vo.pk.posizioneRiga = :posizioneRiga")
})
@Table(name = "tbl_9612", schema = "")
public class FondoBollaDettaglioVo extends BaseVo implements BollaDettaglio {
	private static final long serialVersionUID = 1L;
	@Id
	private FondoBollaDettaglioPk pk;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "trfon9612", referencedColumnName = "trfon9614")
	private TipoFondoBollaVo tipoFondoBollaVo;
	@Column(name = "idtn9612")
	private Integer idtn;
	@Column(name = "titolo9612")
	private String titolo;
	@Column(name = "sottot9612")
	private String sottoTitolo;
	@Column(name = "plorl9612")
	private BigDecimal prezzoLordo;
	@Column(name = "prnel9612")
	private BigDecimal prezzoNetto;
	@Column(name = "quant9612")
	private Integer quantitaConsegnata;
	@Column(name = "quans9612")
	private Integer quantitaSpuntata;
	@Column(name = "diffe9612")
	private Integer differenze;
	@Column(name = "spunt9612")
	private Integer spunta;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9612", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9612", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Formula(value = "(select sum(t1.quar9310-t1.quae9310) from tbl_9310 t1 inner join tbl_9305 t4 on t1.ccli9310 = t4.ccli9305 where t1.codl9310 = coddl9612 and t1.crivw9310 = crivw9612 and t1.idtn9310 = idtn9612 and ((t4.dtsopreda9305 is null and t4.dtsopreda9305 is null) or (sysdate not between t4.dtsopreda9305 and t4.dtsoprea9305)))")
	@Basic(fetch = FetchType.LAZY)
	private Integer ordini;
	@Transient
	@Getter(AccessLevel.NONE)
	private BigDecimal importo;
	@Column(name = "noter9612")
	private String note;

	/*
	 * Vittorio 26/09/18 a cosa serve?
	 * 
	 * 
	@Formula(value = "(select sum(t2.quant9611) from tbl_9611 t2 where t2.coddl9611 = coddl9612 and t2.crivw9611 = crivw9612 and t2.idtn9611 = idtn9612 and t2.ivalo9611 = " + IGerivConstants.INDICATORE_CONTO_DEPOSITO + " and t2.datbc9611 < datbc9612)")
	@Basic(fetch = FetchType.LAZY)
	private Integer fornitoBollePrecedenti;
	@Formula(value = "(select sum(t3.quant9612) from tbl_9612 t3 where t3.coddl9612 = coddl9612 and t3.crivw9612 = crivw9612 and t3.idtn9612 = idtn9612 and t3.prnel9612 = 0 and t3.datbc9612 < datbc9612)")
	@Basic(fetch = FetchType.LAZY)
	private Integer mancanzeBollePrecedenti;
	@Formula(value = "(select sum(t4.qresor9635) from tbl_9635 t4 where t4.coddl9635 = coddl9612 and t4.crivw9635 = crivw9612 and t4.idtn9635 = idtn9612 and t4.prne9635 = 0 and t4.datbr9635 < datbc9612)")
	@Basic(fetch = FetchType.LAZY)
	private Integer resoDichiaratoBollePrecedenti;
	*/
	
	public String getNumeroPubblicazione() {
		return (storicoCopertineVo != null && storicoCopertineVo.getNumeroCopertina() != null) ? (storicoCopertineVo.getNumeroCopertina() != null ? storicoCopertineVo.getNumeroCopertina().trim() : storicoCopertineVo.getNumeroCopertina()) : null;
	}

	public BigDecimal getPercentualeIva() {
		return (storicoCopertineVo != null && storicoCopertineVo.getPercIva() != null) ? storicoCopertineVo.getPercIva() : null;
	}
	
	public String getImmagine() {
		return (storicoCopertineVo != null) ? storicoCopertineVo.getImmagine().getNome() : "";
	}
	
	public String getTipoFondoBolla() {
		return (tipoFondoBollaVo != null && tipoFondoBollaVo.getDescrizione() != null) ? tipoFondoBollaVo.getDescrizione() : null;
	}

	public BigDecimal getSconto() {
		return new BigDecimal(0);
	}
	
	public String getBarcode() {
		return storicoCopertineVo.getCodiceBarre();
	}

	public BigDecimal getImporto() {
		return importo = prezzoNetto.multiply(new BigDecimal(quantitaConsegnata));
	}

	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (obj != null && obj instanceof FondoBollaDettaglioVo) {
			FondoBollaDettaglioVo vo = (FondoBollaDettaglioVo) obj;
			if (getPk().equals(vo.getPk())) {
				equal = true;
			}
		}
		return equal;
	}

	public String getSortCriteria() {
		return String.format("%02d%s", getTipoFondoBollaVo().getTipoRecordFondoBolla(), getTitolo());
	}
}
