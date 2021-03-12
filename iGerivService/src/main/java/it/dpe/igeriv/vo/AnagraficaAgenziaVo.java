package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import java.math.BigDecimal;
import java.sql.Date;
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Anagrafica Agenzia
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_ID_JOINS, query = "from AnagraficaAgenziaVo vo join fetch vo.dlGruppoModuliVo join fetch vo.dlGruppoModuliVo.gruppoModuli join fetch vo.dlGruppoModuliVo.gruppoModuli.moduli where vo.codFiegDl = :codFiegDl"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_ID, query = "from AnagraficaAgenziaVo vo where vo.codFiegDl = :codFiegDl"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_ID_WEB, query = "from AnagraficaAgenziaVo vo where vo.codDpeWebDl = :codDpeWebDl"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_EMAIL_JOINS, query = "from AnagraficaAgenziaVo vo join fetch vo.dlGruppoModuliVo join fetch vo.dlGruppoModuliVo.gruppoModuli join fetch vo.dlGruppoModuliVo.gruppoModuli.moduli where vo.email = :email")
})
@Table(name = "tbl_9107", schema = "")
public class AnagraficaAgenziaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "coddl9107")
	private Integer codFiegDl;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddlalt9107", updatable = true, insertable = true, referencedColumnName = "coddl9107")
	})
	private AnagraficaAgenziaVo agenziaSecondaria;
	@Column(name = "coddlmst9107")
	private Integer codFiegDlMaster;
	@Column(name = "passwdl9107")
	private String passwordDl;
	@Column(name = "nomedla9107")
	private String ragioneSocialeDlPrimaRiga;
	@Column(name = "nomedlb9107")
	private String ragioneSocialeDlSecondaRiga;
	@Column(name = "viadla9107")
	private String indirizzoDlPrimaRiga;
	@Column(name = "viadlb9107")
	private String indirizzoDlSecondaRiga;
	@Column(name = "locadla9107")
	private String localitaDlPrimaRiga;
	@Column(name = "locadlb9107")
	private String localitaDlSecondaRiga;
	@Column(name = "paesedl9107")
	private Integer codNazione;
	@Column(name = "provdl9107")
	private String siglaProvincia;
	@Column(name = "capdl9107")
	private String cap;
	@Column(name = "teledl9107")
	private String telefono;
	@Column(name = "faxdl9107")
	private String fax;
	@Column(name = "emaildl9107")
	private String email;
	@Column(name = "urldl9107")
	private String url;
	@Column(name = "coddlweb9107")
	private Integer codDpeWebDl;
	@Column(name = "maxrdim9107")
	private Integer numMaxCpuResaDimeticata;
	@Column(name = "renuovo9107")
	private String creaRigaResaNuovoPerPeriodicita;
	@Column(name = "impmriv9107")
	private BigDecimal importoMensileDaFatturareDl;
	@Column(name = "impmrivl9107")
	private BigDecimal importoMensileDaFatturareDlLite;
	@Column(name = "impmrivpl9107")
	private BigDecimal importoMensileDaFatturareDliGerivPlus;
	@Column(name = "ftpserver9107")
	private String ftpServer;
	@Column(name = "ftpuser9107")
	private String ftpServerUser;
	@Column(name = "ftpassw9107")
	private String ftpServerPwd;
	@Column(name = "uploadf9107")
	private Integer frequenzaMinutiInvioDatiVendita;
	@Column(name = "pathin9107")
	private String pathInDatiVenditaServer;
	@Column(name = "pathout9107")
	private String pathOutDatiVenditaServer;
	@Column(name = "serverabb9107")
	private String ftpServerAbbonati;
	@Column(name = "capwd9107")
	private Integer changePassword;
	@Column(name = "codgr9107")
	private Integer codGruppoModuliVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "codgr9107", updatable = false, insertable = false, referencedColumnName = "codgr9212"),
		@JoinColumn(name = "coddl9107", updatable = false, insertable = false, referencedColumnName = "coddl9212")
	})
	private DlGruppoModuliVo dlGruppoModuliVo;
	@Column(name = "permod9107")
	private String modulesCustomizable;
	@Column(name = "gftpserver9107")
	private String ftpServerGestionaleAddress;
	@Column(name = "gftpuser9107")
	private String ftpServerGestionaleUser;
	@Column(name = "gftpassw9107")
	private String ftpServerGestionalePwd;
	@Column(name = "gftpdir9107")
	private String ftpServerGestionaleDir;
	@Column(name = "semagia9107")
	private Boolean visualizzaSemaforoGiacenza;
	@Column(name = "rtaea9107")
	private boolean rtaeAccessEnabled;
	@Column(name = "imglogo9107")
	private String imgLogo;
	@Column(name = "dlinfori9107")
	@Getter(AccessLevel.NONE)
	private Boolean dlInforiv;
	@Column(name = "emarep9107")
	@Getter(AccessLevel.NONE)
	private Boolean emailReplyToInstantMessages;
	@Column(name = "gsrdim9107")
	private Integer giornoSettimanaPermessaResaDimenticata;
	@Column(name = "resp9107")
	private Integer tipoControlloPubblicazioniRespinte;
	@Column(name = "copdif9107")
	@Getter(AccessLevel.NONE)
	private Boolean hasButtonCopiaDifferenze;
	@Column(name = "resant9107")
	@Getter(AccessLevel.NONE)
	private Boolean hasResaAnticipata;
	@Column(name = "gsbase9107")
	private Integer gruppoScontoBase;
	@Column(name = "rrto9107")
	@Getter(AccessLevel.NONE)
	private Boolean returnReceiptTo;
	@Column(name = "dtulmo9107")
	private Timestamp dataUltimaModfica;
	@Column(name = "utulmo9107")
	private Integer codUtenteUltimaModfica;
	@Column(name = "dtipdf9107")
	private Date dataInizioEstrattoContoPdf;
	@Column(name = "piva9107")
	private String piva;
	@Column(name = "gesana9107")
	private Boolean gestioneAnagraficaRivenditaObbligatoria;
	@Column(name = "visresris9107")
	private Boolean visualizzaResoRiscontratoStatistica;
	@Column(name = "insririf9107")
	private Boolean permetteInserimentoRichiesteRifornimentoFuture;
	@Column(name = "spobmem9107")
	private Boolean spuntaObbligatoriaBollaConsegna;
	@Column(name = "sudqprepv9107")
	private Boolean suddivisioneQuotidianiPeriodiciReportVenduto;
	@Column(name = "prqtaevem9107")
	private Boolean prenotazioniEvasioneQuantitaEvasaEmpty;
	@Column(name = "hasmsgdpe9107")
	private Boolean edicoleVedonoMessaggiDpe;
	@Column(name = "vendgiadl9107")
	private Boolean venditeEsauritoControlloGiacenzaDL;
	@Column(name = "coinvbodl9107")
	private Boolean hasPopupConfermaSuMemorizzaInviaBolle;
	@Column(name = "httpproxyserver9107")
	private String httpProxyServer;
	@Column(name = "httpproxyport9107")
	private Integer httpProxyPort;
	@Column(name = "msgboldocdisp9107")
	private Boolean hasMessaggioDocumentoDisponibile;
	@Column(name = "livel9107")
	@Getter(AccessLevel.NONE)
	private Boolean hasLivellamenti;
	@Column(name = "tiboresval9107")
	private String tiboresval;
	@Column(name = "FDASOS9107")
	private String flagDataSospensione;
	@Column(name = "tiporesnord9107")
	private String tipoResaNoRifornimentoDimenticato;
	@Column(name = "flgmodftp9107")
	@Getter(AccessLevel.NONE)
	private Integer nuovaModalitaftp;

	@Column(name = "tiporesnocd9107")
	private String tipoResaNoContoDeposito;

	@Column(name = "dlwsftpuser9107")
	private String dlWsFtpUser;
	
	@Column(name = "dlwsftppass9107")
	private String dlWsFtpPass;
	
	@Column(name = "dtpartsecci9107")
	private Timestamp dtPartSecondaCintura;
	
	public Boolean getDlInforiv() {
		return (dlInforiv == null) ? false : dlInforiv;
	}
	
	public Boolean getEmailReplyToInstantMessages() {
		return (emailReplyToInstantMessages == null) ? false : emailReplyToInstantMessages;
	}
	
	public Boolean getHasButtonCopiaDifferenze() {
		return (hasButtonCopiaDifferenze == null) ? false : hasButtonCopiaDifferenze;
	}

	public Boolean getHasResaAnticipata() {
		return (hasResaAnticipata == null) ? false : hasResaAnticipata;
	}
	
	public Boolean isReturnReceiptTo() {
		return returnReceiptTo == null ? false : returnReceiptTo;
	}

	public Boolean getHasLivellamenti() {
		return hasLivellamenti == null ? false : hasLivellamenti;
	}
	
	public Boolean isValidDataSospensione(){
		if(flagDataSospensione==null || flagDataSospensione.equals("0"))return false;
			return true;
	}
	public String getTtipoResaNoRifornimentoDimenticato(){
		return (tipoResaNoRifornimentoDimenticato!=null)?tipoResaNoRifornimentoDimenticato:"";
	}
	
	public Integer getNuovaModalitaftp() {
		return (nuovaModalitaftp == null) ? new Integer(0) : nuovaModalitaftp;
	}
	
	public String getTipoResaNoContoDeposito(){
		return (tipoResaNoContoDeposito!=null)?tipoResaNoContoDeposito:"";
	}
	
}
