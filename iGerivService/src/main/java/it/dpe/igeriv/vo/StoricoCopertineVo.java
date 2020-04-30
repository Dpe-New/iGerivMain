package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.StoricoCopertinePk;

import java.math.BigDecimal;
import java.sql.Date;
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

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@Entity
@Table(name = "tbl_9607", schema = "")
@FilterDefs({
	@FilterDef(name="FornitoFilter", parameters={@ParamDef( name="codEdicola", type="integer"), @ParamDef( name="codEdicola2", type="integer" )}),
	@FilterDef(name="StoricoFilter", parameters=@ParamDef( name="dataStorico", type="timestamp" ) ),
	@FilterDef(name="GruppoScontoFilter", parameters=@ParamDef( name="gruppoSconto", type="integer" ) )
})
public class StoricoCopertineVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private StoricoCopertinePk pk;
	@Column(name = "cpu9607")
	private Integer codicePubblicazione;
	@Column(name = "cddt9607")
	private Integer complementoDistribuzione;
	@Column(name = "num9607")
	private String numeroCopertina;
	@Column(name = "datausc9607")
	private Timestamp dataUscita;
	@Column(name = "sottot9607")
	private String sottoTitolo;
	@Column(name = "barcode9607")
	private String codiceBarre;
	@Column(name = "dare9607")
	private Timestamp dataRichiamoResa;
	@Column(name = "tire9607")
	private Integer tipoRichiamoResa;
	@Column(name = "codinf9607")
	private Integer codiceInforete;
	@Column(name = "numinf9607")
	private String numeroCopertinaInforete;
	@Column(name = "prezzo9607")
	private BigDecimal prezzoCopertina;
	@Column(name = "priva9607")
	private BigDecimal percIva;
	@Column(name = "compl9607")
	private BigDecimal compensoCompiegamento;
	@Column(name = "icom9607")
	private Integer indComponentePaccotto;
	@Column(name = "mult9607")
	private Integer multiplo;
	@Column(name = "idtnr9607")
	private Integer idtnr;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "barcode9607", updatable = false, insertable = false, referencedColumnName = "barcode9700")
	private ImmaginePubblicazioneVo immagine;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9607", updatable = false, insertable = false, referencedColumnName = "coddl9606"),
		@JoinColumn(name = "cpu9607", updatable = false, insertable = false, referencedColumnName = "cpu9606")
	})	
	private AnagraficaPubblicazioniVo anagraficaPubblicazioniVo;
	@Formula(value = "(select nvl(sum(t2.quant9611), 0) from tbl_9611 t2 where t2.coddl9611 = coddl9607 and t2.crivw9611 in (:FornitoFilter.codEdicola, :FornitoFilter.codEdicola2) and t2.idtn9611 = idtn9607 and t2.ivalo9611 <> " + IGerivConstants.INDICATORE_NON_VALORIZZARE + " and t2.datbc9611 > :StoricoFilter.dataStorico)")
	@Basic(fetch = FetchType.LAZY)
	private Integer fornitoBolla;
	@Formula(value = "(select nvl(sum(t3.quant9612), 0) from tbl_9612 t3 where t3.coddl9612 = coddl9607 and t3.crivw9612 in (:FornitoFilter.codEdicola, :FornitoFilter.codEdicola2) and t3.idtn9612 = idtn9607 and t3.trfon9612 in (" + IGerivConstants.LIST_COD_TIPO_FONDO_BOLLA + ")  and t3.datbc9612 > :StoricoFilter.dataStorico)")
	@Basic(fetch = FetchType.LAZY)
	private Integer fornitoFondoBolla;
	@Formula(value = "(select nvl(sum(t4.quaev9131), 0) from tbl_9131 t4 where t4.coddl9131 = coddl9607 and t4.crivw9131 in (:FornitoFilter.codEdicola, :FornitoFilter.codEdicola2) and t4.idtn9131 = idtn9607 and t4.flstat9131 in (" + IGerivConstants.COD_SI + "))")
	@Basic(fetch = FetchType.LAZY)
	private Integer fornitoEstrattoConto;
	@Formula(value = "(select nvl(t6.qforn9638,0) from tbl_9638 t6 where t6.coddl9638 = coddl9607 and t6.crivw9638 in (:FornitoFilter.codEdicola, :FornitoFilter.codEdicola2) and t6.idtn9638 = idtn9607)")
	@Basic(fetch = FetchType.LAZY)
	private Integer fornitoStorico;
	@Formula(value = "(select t5.prnet9617 from tbl_9617 t5 where t5.coddl9617 = coddl9607 and t5.idtn9617 = idtn9607 and t5.gs9617 = :GruppoScontoFilter.gruppoSconto)")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal prezzoEdicola;
	@Formula(value = "(select t1.quant9618 from tbl_9618 t1 where t1.coddl9618 = coddl9607 and t1.crivw9618 in (:FornitoFilter.codEdicola, :FornitoFilter.codEdicola2) and t1.idtn9618 = idtn9607)")
	@Basic(fetch = FetchType.LAZY)
	private Integer quantitaCopieContoDeposito;
	@Column(name = "giac9607")
	private Integer giancezaPressoDl;
	@Column(name = "datgiac9607")
	private Timestamp dataUltimoAggiornamentoGiacenzaPressoDl;
	@Column(name = "dfpcd9607")
	private Timestamp dataFatturazionePrevista;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9607", updatable = false, insertable = false, referencedColumnName = "coddl9623"),
		@JoinColumn(name = "resp9607", updatable = false, insertable = false, referencedColumnName = "resp9623")
	})
	private MotivoResaRespintaVo motivoResaRespinta;
	@Column(name = "dtabar9607")
	private Timestamp dataCorrezioneBarcode;
	@Column(name = "cribar9607")
	private Integer codEdicolaCorrezioneBarcode;
	@Column(name = "barcodep9607")
	private String codiceBarrePrecedente;
	@Column(name = "scontoinf9607")
	private BigDecimal scontoInforete;
	@Column(name = "cdeposinf9607")
	private Boolean contoDepositoInforete;
	@Formula(value = "P_BOLLA.GET_GIACENZA(coddl9607, :FornitoFilter.codEdicola, idtn9607, :StoricoFilter.dataStorico)")
	@Basic(fetch = FetchType.LAZY)
	private Long giacenza;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9607", updatable = false, insertable = false, referencedColumnName = "coddl9642"),
		@JoinColumn(name = "idtn9607", updatable = false, insertable = false, referencedColumnName = "idtn9642")
	})
	private MessaggioIdtnVo messaggioIdtnVo;
	@Column(name = "cpconf9607")
	private Integer copiePerConfezione;
	@Column(name = "cesta9607")
	private String cesta;
}
