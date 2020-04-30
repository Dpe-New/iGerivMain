package it.dpe.igeriv.vo;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivQueryContants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ParamDef;

import com.google.common.base.Strings;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_USER_CLIENTE_EDICOLA_BY_CODICE, query = "from ClienteEdicolaVo vo where vo.codCliente = :codCliente"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_USER_CLIENTE_EDICOLA_BY_CODICE_JOINS, query = "from ClienteEdicolaVo vo join fetch vo.edicola join fetch vo.edicola.anagraficaAgenziaVo join fetch vo.edicola.anagraficaEdicolaVo join fetch vo.gruppoModuliVo join fetch vo.gruppoModuliVo.moduli where vo.codCliente = :codCliente"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_USER_CLIENTE_EDICOLA_BY_EMAIL_JOINS, query = "from ClienteEdicolaVo vo join fetch vo.edicola join fetch vo.edicola.anagraficaAgenziaVo join fetch vo.edicola.anagraficaEdicolaVo join fetch vo.gruppoModuliVo join fetch vo.gruppoModuliVo.moduli where vo.email = :email"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_CLIENT_USER_AGENT, query = "update ClienteEdicolaVo vo set vo.userAgent = :userAgent where vo.codEdicola = :codEdicola and vo.codCliente = :codCliente")
})
@Table(name = "tbl_9305", schema = "")
@FilterDefs({
	@FilterDef(name="DtCompetenzaFilter", parameters=@ParamDef( name="dtCompetenza", type="timestamp" ) )
})
public class ClienteEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ccli9305")
	private Long codCliente;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="ccli9400")
	private List<OrdineLibriVo> ordini;
	
	@Column(name = "crivw9305")
	private Integer codEdicola;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9305", updatable = false, insertable = false, referencedColumnName = "crivw9206")	
	private AbbinamentoEdicolaDlVo edicola;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codgr9305", updatable = false, insertable = true, referencedColumnName = "codgr9209")
	private GruppoModuliVo gruppoModuliVo;
	@Column(name = "nomea9305")
	private String nome;
	@Column(name = "nomeb9305")
	private String cognome;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "tiplo9305", updatable = true, insertable = true, referencedColumnName = "tiplo9221")
	})
	private TipoLocalitaVo tipoLocalita;
	@Column(name = "viaa9305")
	private String indirizzo;	
	@Column(name = "numci9305")
	private Integer numeroCivico;
	@Column(name = "compl9305")
	private String estensione;	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cloc9305", updatable = true, insertable = true, referencedColumnName = "local9219")
	private LocalitaVo localita;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "paese9305", updatable = true, insertable = true, referencedColumnName = "paesi9218")
	private PaeseVo paese;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cprov9305", updatable = true, insertable = true, nullable = true, referencedColumnName = "provi9220")
	private ProvinciaVo provincia;
	@Column(name = "cap9305")
	private String cap;
	@Column(name = "cofi9305")
	private String codiceFiscale;
	@Column(name = "piva9305")
	private String piva;
	@Column(name = "tele9305")
	private String telefono;
	@Column(name = "cell9305")
	private Long cellulare;
	@Column(name = "fax9305")
	private String fax;
	@Column(name = "email9305")
	private String email;
	@Column(name = "url9305")
	private String url;
	@Column(name = "ccred9305")
	private Long numCartaCredito;
	@Column(name = "datasccr9305")
	private Timestamp dtScadenzaCartaCredito;
	@Column(name = "datacons9305")
	private Timestamp dtInserimentoConsenso;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "tipopag9305", updatable = true, insertable = true, nullable = true, referencedColumnName = "tipopag9308")
	private MetodoPagamentoClienteVo tipoPagamento;
	@Column(name = "passwc9305")
	private String password;
	@Transient
	private String tipoPagamentoDesc;
	@Column(name = "capwd9305")
	private Integer changePassword;
	@Column(name = "pwdcr9305")
	private Integer pwdCriptata;
	@Column(name = "usrag9305")
	private String userAgent;
	@Getter(AccessLevel.NONE)
	@Column(name = "privso9305")
	private Boolean privacySottoscritta;
	@Getter(AccessLevel.NONE)
	@Column(name = "emailv9305")
	private Boolean emailValido;
	@Column(name = "daulmo9305")
	private Timestamp dtUltimoAggiornamento;
	@Column(name = "datnas9305")
	private Date dataNascita;
	@Column(name = "sesso9305")
	private String sesso;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codprof9305", updatable = true, insertable = true, nullable = true, referencedColumnName = "codprof9351")
	private ProfessioneVo professione;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codist9305", updatable = true, insertable = true, nullable = true, referencedColumnName = "codist9350")
	private IstruzioneVo istruzione;;
	@Column(name = "ipautpr9305")
	private String indirizzoIpRichiesta;
	@Column(name = "tipesco9305")
	private Integer tipoEstrattoConto;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codban9305", updatable = true, insertable = true, nullable = true, referencedColumnName = "codban9304")
	private AnagraficaBancaVo banca;
	@Column(name = "concor9305")
	private String contoCorrente;
	@Column(name = "iban9305")
	private String iban;
	@Column(name = "ggscad9305")
	private Integer giorniScadenzaPagamento;
	@Formula(value = "(select sum(t1.imptot9625) from tbl_9625 t1 inner join tbl_9305 t3 on t1.ccli9625 = t3.ccli9305 where trunc(t1.dtcoec9625) <= :DtCompetenzaFilter.dtCompetenza and t1.crivw9625 = crivw9305 and t1.ccli9625 = ccli9305 and t1.dataec9625 is null and (t1.pagato9625 = 0 or t1.pagato9625 is null) and (t1.fateme9625 = 0 or t1.fateme9625 is null) and (t1.delet9625 is null or t1.delet9625 = 0))")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal totaleEstrattoConto;
	@Formula(value = "(select sum(t3.prezzo9546 * t3.qt9546) from tbl_9625 t1 inner join tbl_9545 t2 on t1.idnoned9625 = t2.id9545 and (t2.delet9545 is null or t2.delet9545 = 0) inner join tbl_9546 t3 on t2.id9545 = t3.id9546 and (t3.delet9546 is null or t3.delet9546 = 0) inner join tbl_9305 t4 on t1.ccli9625 = t4.ccli9305 where trunc(t1.dtcoec9625) <= :DtCompetenzaFilter.dtCompetenza and t1.crivw9625 = crivw9305 and t1.ccli9625 = ccli9305 and t1.dataec9625 is null and (t1.pagato9625 = 0 or t1.pagato9625 is null) and (t1.fateme9625 = 0 or t1.fateme9625 is null) and (t1.delet9625 is null or t1.delet9625 = 0))")
	@Basic(fetch = FetchType.LAZY)
	private BigDecimal totaleEstrattoContoPne;
	@Column(name = "dtsopreda9305")
	private Timestamp dtSospensionePrenotazioniDa;
	@Column(name = "dtsoprea9305")
	private Timestamp dtSospensionePrenotazioniA;
	@Column(name = "note9305")
	private String note;
	
	public String getProvinciaDesc() {
		return (provincia != null) ? provincia.getDescrizione() : null;
	}
	
	public String getLocalitaDesc() {
		return (localita != null) ? localita.getDescrizione() : null;
	}
	
	public String getNomeCognome() {
		return getNome() + (Strings.isNullOrEmpty(getCognome()) ? "" : (" " + getCognome()));
	}
	
	public String getIndirizzoCompleto() {
		String tipoLoc = getTipoLocalita() == null || Strings.isNullOrEmpty(getTipoLocalita().getDescrizione()) ? "" : getTipoLocalita().getDescrizione().trim() + " ";
		String indirizzo = Strings.isNullOrEmpty(getIndirizzo()) ? "" : getIndirizzo().trim() + " ";
		String ext = Strings.isNullOrEmpty(getEstensione()) ? "" : " / " + getEstensione() + " ";
		String num = getNumeroCivico() == null ? "" : ", " + getNumeroCivico() + ext + " ";
		String cap = Strings.isNullOrEmpty(getCap()) ? "" : getCap().trim() + " ";
		String localita = Strings.isNullOrEmpty(getLocalitaDesc()) ? "" : (Strings.isNullOrEmpty(cap) ? "" : " - ") + getLocalitaDesc().trim() + " ";
		String prov = Strings.isNullOrEmpty(getProvinciaDesc()) ? "" : " (" + getProvinciaDesc().trim() + ") ";
		return (Strings.isNullOrEmpty(indirizzo) ? "" : tipoLoc + " " + indirizzo + num + " \n") + cap + localita + prov;
	}
	
	public String getViaNumero() {
		String tipoLoc = getTipoLocalita() == null || Strings.isNullOrEmpty(getTipoLocalita().getDescrizione()) ? "" : getTipoLocalita().getDescrizione().trim() + " ";
		String indirizzo = Strings.isNullOrEmpty(getIndirizzo()) ? "" : getIndirizzo().trim() + " ";
		String num = getNumeroCivico() == null ? "" : getNumeroCivico() + " ";
		String ext = Strings.isNullOrEmpty(getEstensione()) ? "" : getEstensione().trim() + " ";
		return tipoLoc + " " + indirizzo + " " + num + (Strings.isNullOrEmpty(ext) ? "" : " / " + ext);
	}
	
	public String getCapCittaProv() {
		String localita = Strings.isNullOrEmpty(getLocalitaDesc()) ? "" : getLocalitaDesc().trim() + " ";
		String cap = Strings.isNullOrEmpty(getCap()) ? "" : getCap().trim() + " ";
		String prov = Strings.isNullOrEmpty(getProvinciaDesc()) ? "" : "(" + getProvinciaDesc().trim() + ") ";
		return (Strings.isNullOrEmpty(cap) ? "" : cap + " - ") + localita + " " + prov;
	}
	
	public String getDatiFiscali() {
		return Strings.isNullOrEmpty(getPiva()) ? Strings.isNullOrEmpty(getCodiceFiscale()) ? "" : getCodiceFiscale() : getPiva();
	}
	
	public String getNumeroCivicoReport() {
		return (getNumeroCivico() == null) ? "" : "N. " + getNumeroCivico();
	}
	
	public String getDatiCliente() {
		String piva = (Strings.isNullOrEmpty(getPiva()) ? "" : "\n" + IGerivMessageBundle.get("dpe.piva") + ": " + getPiva());
		String cf = (Strings.isNullOrEmpty(getCodiceFiscale()) ? "" : "\n" + IGerivMessageBundle.get("dpe.codice.fiscale.3") + ": " + getCodiceFiscale());
		return getNomeCognome() + "\n" + getIndirizzoCompleto() + piva + cf;
	}

	public Boolean getEmailValido() {
		return emailValido == null ? false : emailValido;
	}

	public Boolean getPrivacySottoscritta() {
		return privacySottoscritta == null ? false : privacySottoscritta;
	}
	
	
}
