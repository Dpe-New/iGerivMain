package it.dpe.igeriv.security;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.MenuModuloVo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Classe utente del sistema.
 * Instanziata durante il login e salvata nella session del'utente autenticato. 
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
public class UserAbbonato extends User {
	private static final long serialVersionUID = 1L;
	private Calendar cal = Calendar.getInstance();
	private Integer id;
	private Integer[] arrId;
	private String codUtente;
	private String ragioneSocialeDl;
	private String ragioneSocialeEdicola;
	private String indirizzoEdicolaPrimaRiga;
	private String indirizzoAgenziaPrimaRiga;
	private String localitaEdicolaPrimaRiga;
	private String provinciaEdicola;
	private String capEdicola;
	private String localitaAgenziaPrimaRiga;
	private String provinciaAgenzia;
	private String capAgenzia;
	private String pivaAgenzia;
	private String nome;
	private String cognome;
	private String email;
	private String emailDl;
	private boolean admin;
	private boolean statoAttivo;
	private Integer codEdicolaDl;
	private Integer codFiegDl;
	private Integer codFiegDlSecondario;
	private Integer codFiegDlMaster;
	private Integer codEdicolaMaster;
	private Integer[] arrCodFiegDl;
	private Integer codDpeWebEdicola;
	private Integer codDpeWebEdicolaSecodaria;
	private List<List<List<MenuModuloVo>>> moduli;
	private boolean changePassword;
	private String imgFolder;
	private Integer numMaxCpuResaDimenticata;
	private boolean moduleCustomizable;
	@SuppressWarnings("unused")
	private String userSalt;
	private Integer tipoUtente;
	private boolean pwdCriptata;
	private String ftpServerGestionaleAddress;
	private String ftpServerGestionaleUser;
	private String ftpServerGestionalePwd;
	private String ftpServerGestionaleDir;
	private Integer gruppoSconto;
	private Boolean visualizzaSemaforoGiacenza;
	private boolean rtaeAccessEnabled;
	private Timestamp dtSospensioneEdicola;
	private Timestamp dtAttivazioneEdicola;
	private List<Integer> giroTipo;
	private List<Integer> giri;
	private List<Integer> zonaTipo;
	private List<Integer> zone;
	private Long codContabileCliente;
	private String codiceFiscale;
	private String piva;
	private String imgLogo;
	private String urlDL;
	private Boolean ricercaProdottiVari;
	private Boolean edicolaTest;
	private boolean isMultiDl;
	private boolean emailReplyToInstantMessages;
	private boolean dlInforiv;
	private Timestamp dataStorico;
	private boolean emailValido;
	private Integer giornoSettimanaPermessaResaDimenticata;
	private Integer tipoControlloPubblicazioniRespinte;
	private Boolean abilitataCorrezioneBarcode;
	private Boolean hasEdicoleAutorizzateAggiornaBarcode;
	private List<AnagraficaAgenziaVo> listDl;
	private Boolean hasButtonCopiaDifferenze;
	private Boolean hasResaAnticipata;
	private boolean edicolaInGruppoScontoBase;
	private Boolean edicolaStarter;
	private Boolean richiestaProva;
	private Boolean returnReceiptTo;
	private Date dataInizioEstrattoContoPdf;
	private Boolean hasBolle;
	private Boolean hasVendite;
	private Boolean hasClienti;
	private Boolean hasProdottiVari;
	private Boolean hasPrenotazioni;
	private Boolean edicolaTestPerModifiche;
	private Boolean campagnaInvitaColleghiAbilitata;
	private Boolean gestioneAnagraficaRivenditaObbligatoria;
	private Boolean anagraficaCompilata;
	private Boolean condizioniUsoAccettate;
	private Boolean visualizzaResoRiscontratoStatistica;
	private Boolean permetteInserimentoRichiesteRifornimentoFuture;
	private Boolean spuntaObbligatoriaBollaConsegna;
	private Boolean suddivisioneQuotidianiPeriodiciReportVenduto;
	private Boolean prenotazioniEvasioneQuantitaEvasaEmpty;
	private Boolean edicoleVedonoMessaggiDpe;
	private Boolean fotoEdicolaInserita;
	private Integer codGruppo;
	private Boolean venditeEsauritoControlloGiacenzaDL;
	private Boolean hasPopupConfermaSuMemorizzaInviaBolle;
	private String httpProxyServer;
	private Integer httpProxyPort;
	private Boolean hasMessaggioDocumentoDisponibile;
	private boolean isEdicolaPromo;
	private Boolean hasLivellamenti;
	//Gestione Profili
	private Boolean viewImageByProfile;
	
	private Integer roleIdProfile;
	private String roleNameProfile;
	
	private GruppoModuliVo gruppoModuliVo;

	private Boolean checkConsegneGazzetta;
	private Boolean accettoResaCD;

	public UserAbbonato(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities)
			throws IllegalArgumentException {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		cal.set(2000, 0, 1);
	}

	public String getEmail() {
		return email == null ? "" : email;
	}

	public String getRagioneSocialeDl() {
		return ragioneSocialeDl == null ? "" : ragioneSocialeDl;
	}

	public String getRagioneSocialeEdicola() {
		return ragioneSocialeEdicola == null ? "" : ragioneSocialeEdicola;
	}

	public String getIndirizzoEdicolaPrimaRiga() {
		return indirizzoEdicolaPrimaRiga == null ? "" : indirizzoEdicolaPrimaRiga;
	}

	public String getIndirizzoAgenziaPrimaRiga() {
		return indirizzoAgenziaPrimaRiga == null ? "" : indirizzoAgenziaPrimaRiga;
	}

	public String getLocalitaEdicolaPrimaRiga() {
		return localitaEdicolaPrimaRiga == null ? "" : localitaEdicolaPrimaRiga;
	}

	public String getProvinciaEdicola() {
		return provinciaEdicola  == null ? "" : provinciaEdicola;
	}
	
	public String getCapEdicola() {
		return capEdicola == null ? "" : capEdicola;
	}

	public String getLocalitaAgenziaPrimaRiga() {
		return localitaAgenziaPrimaRiga == null ? "" : localitaAgenziaPrimaRiga;
	}

	public String getProvinciaAgenzia() {
		return provinciaAgenzia == null ? "" : provinciaAgenzia;
	}

	public String getCapAgenzia() {
		return capAgenzia == null ? "" : capAgenzia;
	}

	public Integer getNumMaxCpuResaDimenticata() {
		return (numMaxCpuResaDimenticata == null) ? 0 : numMaxCpuResaDimenticata;
	}

	public String getUserSalt() {
		return (tipoUtente != null && tipoUtente.equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) ? ("" + ((id + NumberUtils.getOnlyNumerics(codUtente)) * IGerivConstants.ENCODE_FACTOR)) : (codFiegDl.toString() + codEdicolaDl.toString());
	}

	public Boolean getVisualizzaSemaforoGiacenza() {
		return visualizzaSemaforoGiacenza == null ? false : visualizzaSemaforoGiacenza;
	}

	public Timestamp getDataStorico() {
		if (dataStorico == null) {
			return new Timestamp(cal.getTimeInMillis());
		} else {
			return dataStorico;
		}
	}

	public Boolean getAbilitataCorrezioneBarcode() {
		return abilitataCorrezioneBarcode == null ? false : abilitataCorrezioneBarcode;
	}

	public Boolean getHasEdicoleAutorizzateAggiornaBarcode() {
		return hasEdicoleAutorizzateAggiornaBarcode == null ? false : hasEdicoleAutorizzateAggiornaBarcode;
	}
	
	public Boolean getEdicolaDeviettiTodis() {
		return codFiegDl != null 
				&& (codFiegDl.equals(IGerivConstants.COD_FIEG_DL_DEVIETTI) || codFiegDl.equals(IGerivConstants.COD_FIEG_DL_TODIS));
	}
	
	// 02/11/2016 CAVAGLIA : crivdl > 1600
	public Boolean isCodiceDLCavaglia() {
		return (codEdicolaDl!=null && codEdicolaDl.intValue() > 1600);
	}
	

	// 16/11/2016 - Controllo effettuato per le edicole light chiminelli
	public Boolean getEdicolaIlChiosco() {
		return codFiegDl != null && (codFiegDl.equals(IGerivConstants.CHIMINELLI_CODE_1) || codFiegDl.equals(IGerivConstants.CHIMINELLI_CODE_2) 
				|| codFiegDl.equals(IGerivConstants.CHIMINELLI_CODE_3));
	}
	
	// 17/01/2017 
	public Boolean isEdicolaCDLBologna() {
		return codFiegDl != null && (codFiegDl.equals(IGerivConstants.CDL_CODE));
	}
	
	
	
	public Boolean getEdicolaDeviettiTodisCavaglia() {
		return codFiegDl != null && (codFiegDl.equals(IGerivConstants.COD_FIEG_DL_DEVIETTI) || codFiegDl.equals(IGerivConstants.COD_FIEG_DL_TODIS) 
				|| codFiegDl.equals(IGerivConstants.COD_FIEG_CAVAGLIA));
	}
	
	
	
	public Boolean getEdicolaTestPerModifiche() {
		return edicolaTestPerModifiche == null ? false : edicolaTestPerModifiche;
	}

	public String getPivaAgenzia() {
		return pivaAgenzia == null ? "" : pivaAgenzia;
	}

	public Boolean getSpuntaObbligatoriaBollaConsegna() {
		return spuntaObbligatoriaBollaConsegna == null ? false : spuntaObbligatoriaBollaConsegna;
	}

	public Boolean getSuddivisioneQuotidianiPeriodiciReportVenduto() {
		return suddivisioneQuotidianiPeriodiciReportVenduto == null ? false : suddivisioneQuotidianiPeriodiciReportVenduto;
	}
	
	public String getNome() {
		return nome == null ? "" : nome;
	}

	public String getCognome() {
		return cognome == null ? "" : cognome;
	}

	public boolean isEmailValido() {
		return getEdicolaDeviettiTodis() ? true : emailValido;
	}
	
	public Boolean getHasLivellamenti() {
		return hasLivellamenti == null ? false : hasLivellamenti;
	}
	
	@Override
	public int hashCode() {
		return getUsername().hashCode() + tipoUtente;
	}
	
	@Override
	public boolean equals(Object rhs) {
		int i = new Integer(getUsername()) + tipoUtente;
		int j = new Integer(((UserAbbonato)rhs).getUsername()) + ((UserAbbonato)rhs).tipoUtente;
		return i == j;
	}

	public Calendar getCal() {
		return cal;
	}

	public void setCal(Calendar cal) {
		this.cal = cal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer[] getArrId() {
		return arrId;
	}

	public void setArrId(Integer[] arrId) {
		this.arrId = arrId;
	}

	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = codUtente;
	}

	public String getEmailDl() {
		return emailDl;
	}

	public void setEmailDl(String emailDl) {
		this.emailDl = emailDl;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isStatoAttivo() {
		return statoAttivo;
	}

	public void setStatoAttivo(boolean statoAttivo) {
		this.statoAttivo = statoAttivo;
	}

	public Integer getCodEdicolaDl() {
		return codEdicolaDl;
	}

	public void setCodEdicolaDl(Integer codEdicolaDl) {
		this.codEdicolaDl = codEdicolaDl;
	}

	public Integer getCodFiegDl() {
		return codFiegDl;
	}

	public void setCodFiegDl(Integer codFiegDl) {
		this.codFiegDl = codFiegDl;
	}

	public Integer getCodFiegDlSecondario() {
		return codFiegDlSecondario;
	}

	public void setCodFiegDlSecondario(Integer codFiegDlSecondario) {
		this.codFiegDlSecondario = codFiegDlSecondario;
	}

	public Integer getCodFiegDlMaster() {
		return codFiegDlMaster;
	}

	public void setCodFiegDlMaster(Integer codFiegDlMaster) {
		this.codFiegDlMaster = codFiegDlMaster;
	}

	public Integer getCodEdicolaMaster() {
		return codEdicolaMaster;
	}

	public void setCodEdicolaMaster(Integer codEdicolaMaster) {
		this.codEdicolaMaster = codEdicolaMaster;
	}

	public Integer[] getArrCodFiegDl() {
		return arrCodFiegDl;
	}

	public void setArrCodFiegDl(Integer[] arrCodFiegDl) {
		this.arrCodFiegDl = arrCodFiegDl;
	}

	public Integer getCodDpeWebEdicola() {
		return codDpeWebEdicola;
	}

	public void setCodDpeWebEdicola(Integer codDpeWebEdicola) {
		this.codDpeWebEdicola = codDpeWebEdicola;
	}

	public Integer getCodDpeWebEdicolaSecodaria() {
		return codDpeWebEdicolaSecodaria;
	}

	public void setCodDpeWebEdicolaSecodaria(Integer codDpeWebEdicolaSecodaria) {
		this.codDpeWebEdicolaSecodaria = codDpeWebEdicolaSecodaria;
	}

	public List<List<List<MenuModuloVo>>> getModuli() {
		return moduli;
	}

	public void setModuli(List<List<List<MenuModuloVo>>> moduli) {
		this.moduli = moduli;
	}

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	public String getImgFolder() {
		return imgFolder;
	}

	public void setImgFolder(String imgFolder) {
		this.imgFolder = imgFolder;
	}

	public boolean isModuleCustomizable() {
		return moduleCustomizable;
	}

	public void setModuleCustomizable(boolean moduleCustomizable) {
		this.moduleCustomizable = moduleCustomizable;
	}

	public Integer getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(Integer tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

	public boolean isPwdCriptata() {
		return pwdCriptata;
	}

	public void setPwdCriptata(boolean pwdCriptata) {
		this.pwdCriptata = pwdCriptata;
	}

	public String getFtpServerGestionaleAddress() {
		return ftpServerGestionaleAddress;
	}

	public void setFtpServerGestionaleAddress(String ftpServerGestionaleAddress) {
		this.ftpServerGestionaleAddress = ftpServerGestionaleAddress;
	}

	public String getFtpServerGestionaleUser() {
		return ftpServerGestionaleUser;
	}

	public void setFtpServerGestionaleUser(String ftpServerGestionaleUser) {
		this.ftpServerGestionaleUser = ftpServerGestionaleUser;
	}

	public String getFtpServerGestionalePwd() {
		return ftpServerGestionalePwd;
	}

	public void setFtpServerGestionalePwd(String ftpServerGestionalePwd) {
		this.ftpServerGestionalePwd = ftpServerGestionalePwd;
	}

	public String getFtpServerGestionaleDir() {
		return ftpServerGestionaleDir;
	}

	public void setFtpServerGestionaleDir(String ftpServerGestionaleDir) {
		this.ftpServerGestionaleDir = ftpServerGestionaleDir;
	}

	public Integer getGruppoSconto() {
		return gruppoSconto;
	}

	public void setGruppoSconto(Integer gruppoSconto) {
		this.gruppoSconto = gruppoSconto;
	}

	public boolean isRtaeAccessEnabled() {
		return rtaeAccessEnabled;
	}

	public void setRtaeAccessEnabled(boolean rtaeAccessEnabled) {
		this.rtaeAccessEnabled = rtaeAccessEnabled;
	}

	public Timestamp getDtSospensioneEdicola() {
		return dtSospensioneEdicola;
	}

	public void setDtSospensioneEdicola(Timestamp dtSospensioneEdicola) {
		this.dtSospensioneEdicola = dtSospensioneEdicola;
	}

	public Timestamp getDtAttivazioneEdicola() {
		return dtAttivazioneEdicola;
	}

	public void setDtAttivazioneEdicola(Timestamp dtAttivazioneEdicola) {
		this.dtAttivazioneEdicola = dtAttivazioneEdicola;
	}

	public List<Integer> getGiroTipo() {
		return giroTipo;
	}

	public void setGiroTipo(List<Integer> giroTipo) {
		this.giroTipo = giroTipo;
	}

	public List<Integer> getGiri() {
		return giri;
	}

	public void setGiri(List<Integer> giri) {
		this.giri = giri;
	}

	public List<Integer> getZonaTipo() {
		return zonaTipo;
	}

	public void setZonaTipo(List<Integer> zonaTipo) {
		this.zonaTipo = zonaTipo;
	}

	public List<Integer> getZone() {
		return zone;
	}

	public void setZone(List<Integer> zone) {
		this.zone = zone;
	}

	public Long getCodContabileCliente() {
		return codContabileCliente;
	}

	public void setCodContabileCliente(Long codContabileCliente) {
		this.codContabileCliente = codContabileCliente;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	public String getImgLogo() {
		return imgLogo;
	}

	public void setImgLogo(String imgLogo) {
		this.imgLogo = imgLogo;
	}

	public String getUrlDL() {
		return urlDL;
	}

	public void setUrlDL(String urlDL) {
		this.urlDL = urlDL;
	}

	public Boolean getRicercaProdottiVari() {
		return ricercaProdottiVari;
	}

	public void setRicercaProdottiVari(Boolean ricercaProdottiVari) {
		this.ricercaProdottiVari = ricercaProdottiVari;
	}

	public Boolean getEdicolaTest() {
		return edicolaTest;
	}

	public void setEdicolaTest(Boolean edicolaTest) {
		this.edicolaTest = edicolaTest;
	}

	public boolean isMultiDl() {
		return isMultiDl;
	}

	public void setMultiDl(boolean isMultiDl) {
		this.isMultiDl = isMultiDl;
	}

	public boolean isEmailReplyToInstantMessages() {
		return emailReplyToInstantMessages;
	}

	public void setEmailReplyToInstantMessages(boolean emailReplyToInstantMessages) {
		this.emailReplyToInstantMessages = emailReplyToInstantMessages;
	}

	public boolean isDlInforiv() {
		return dlInforiv;
	}

	public void setDlInforiv(boolean dlInforiv) {
		this.dlInforiv = dlInforiv;
	}

	public Integer getGiornoSettimanaPermessaResaDimenticata() {
		return giornoSettimanaPermessaResaDimenticata;
	}

	public void setGiornoSettimanaPermessaResaDimenticata(Integer giornoSettimanaPermessaResaDimenticata) {
		this.giornoSettimanaPermessaResaDimenticata = giornoSettimanaPermessaResaDimenticata;
	}

	public Integer getTipoControlloPubblicazioniRespinte() {
		return tipoControlloPubblicazioniRespinte;
	}

	public void setTipoControlloPubblicazioniRespinte(Integer tipoControlloPubblicazioniRespinte) {
		this.tipoControlloPubblicazioniRespinte = tipoControlloPubblicazioniRespinte;
	}

	public List<AnagraficaAgenziaVo> getListDl() {
		return listDl;
	}

	public void setListDl(List<AnagraficaAgenziaVo> listDl) {
		this.listDl = listDl;
	}

	public Boolean getHasButtonCopiaDifferenze() {
		return hasButtonCopiaDifferenze;
	}

	public void setHasButtonCopiaDifferenze(Boolean hasButtonCopiaDifferenze) {
		this.hasButtonCopiaDifferenze = hasButtonCopiaDifferenze;
	}

	public Boolean getHasResaAnticipata() {
		return hasResaAnticipata;
	}

	public void setHasResaAnticipata(Boolean hasResaAnticipata) {
		this.hasResaAnticipata = hasResaAnticipata;
	}

	public boolean isEdicolaInGruppoScontoBase() {
		return edicolaInGruppoScontoBase;
	}

	public void setEdicolaInGruppoScontoBase(boolean edicolaInGruppoScontoBase) {
		this.edicolaInGruppoScontoBase = edicolaInGruppoScontoBase;
	}

	public Boolean getEdicolaStarter() {
		return edicolaStarter;
	}

	public void setEdicolaStarter(Boolean edicolaStarter) {
		this.edicolaStarter = edicolaStarter;
	}

	public Boolean getRichiestaProva() {
		return richiestaProva;
	}

	public void setRichiestaProva(Boolean richiestaProva) {
		this.richiestaProva = richiestaProva;
	}

	public Boolean getReturnReceiptTo() {
		return returnReceiptTo;
	}

	public void setReturnReceiptTo(Boolean returnReceiptTo) {
		this.returnReceiptTo = returnReceiptTo;
	}

	public Date getDataInizioEstrattoContoPdf() {
		return dataInizioEstrattoContoPdf;
	}

	public void setDataInizioEstrattoContoPdf(Date dataInizioEstrattoContoPdf) {
		this.dataInizioEstrattoContoPdf = dataInizioEstrattoContoPdf;
	}

	public Boolean getHasBolle() {
		return hasBolle;
	}

	public void setHasBolle(Boolean hasBolle) {
		this.hasBolle = hasBolle;
	}

	public Boolean getHasVendite() {
		return hasVendite;
	}

	public void setHasVendite(Boolean hasVendite) {
		this.hasVendite = hasVendite;
	}

	public Boolean getHasClienti() {
		return hasClienti;
	}

	public void setHasClienti(Boolean hasClienti) {
		this.hasClienti = hasClienti;
	}

	public Boolean getHasProdottiVari() {
		return hasProdottiVari;
	}

	public void setHasProdottiVari(Boolean hasProdottiVari) {
		this.hasProdottiVari = hasProdottiVari;
	}

	public Boolean getHasPrenotazioni() {
		return hasPrenotazioni;
	}

	public void setHasPrenotazioni(Boolean hasPrenotazioni) {
		this.hasPrenotazioni = hasPrenotazioni;
	}

	public Boolean getCampagnaInvitaColleghiAbilitata() {
		return campagnaInvitaColleghiAbilitata;
	}

	public void setCampagnaInvitaColleghiAbilitata(Boolean campagnaInvitaColleghiAbilitata) {
		this.campagnaInvitaColleghiAbilitata = campagnaInvitaColleghiAbilitata;
	}

	public Boolean getGestioneAnagraficaRivenditaObbligatoria() {
		return gestioneAnagraficaRivenditaObbligatoria;
	}

	public void setGestioneAnagraficaRivenditaObbligatoria(Boolean gestioneAnagraficaRivenditaObbligatoria) {
		this.gestioneAnagraficaRivenditaObbligatoria = gestioneAnagraficaRivenditaObbligatoria;
	}

	public Boolean getAnagraficaCompilata() {
		return anagraficaCompilata;
	}

	public void setAnagraficaCompilata(Boolean anagraficaCompilata) {
		this.anagraficaCompilata = anagraficaCompilata;
	}

	public Boolean getCondizioniUsoAccettate() {
		return condizioniUsoAccettate;
	}

	public void setCondizioniUsoAccettate(Boolean condizioniUsoAccettate) {
		this.condizioniUsoAccettate = condizioniUsoAccettate;
	}

	public Boolean getVisualizzaResoRiscontratoStatistica() {
		return visualizzaResoRiscontratoStatistica;
	}

	public void setVisualizzaResoRiscontratoStatistica(Boolean visualizzaResoRiscontratoStatistica) {
		this.visualizzaResoRiscontratoStatistica = visualizzaResoRiscontratoStatistica;
	}

	public Boolean getPermetteInserimentoRichiesteRifornimentoFuture() {
		return permetteInserimentoRichiesteRifornimentoFuture;
	}

	public void setPermetteInserimentoRichiesteRifornimentoFuture(Boolean permetteInserimentoRichiesteRifornimentoFuture) {
		this.permetteInserimentoRichiesteRifornimentoFuture = permetteInserimentoRichiesteRifornimentoFuture;
	}

	public Boolean getPrenotazioniEvasioneQuantitaEvasaEmpty() {
		return prenotazioniEvasioneQuantitaEvasaEmpty;
	}

	public void setPrenotazioniEvasioneQuantitaEvasaEmpty(Boolean prenotazioniEvasioneQuantitaEvasaEmpty) {
		this.prenotazioniEvasioneQuantitaEvasaEmpty = prenotazioniEvasioneQuantitaEvasaEmpty;
	}

	public Boolean getEdicoleVedonoMessaggiDpe() {
		return edicoleVedonoMessaggiDpe;
	}

	public void setEdicoleVedonoMessaggiDpe(Boolean edicoleVedonoMessaggiDpe) {
		this.edicoleVedonoMessaggiDpe = edicoleVedonoMessaggiDpe;
	}

	public Boolean getFotoEdicolaInserita() {
		return fotoEdicolaInserita;
	}

	public void setFotoEdicolaInserita(Boolean fotoEdicolaInserita) {
		this.fotoEdicolaInserita = fotoEdicolaInserita;
	}

	public Integer getCodGruppo() {
		return codGruppo;
	}

	public void setCodGruppo(Integer codGruppo) {
		this.codGruppo = codGruppo;
	}

	public Boolean getVenditeEsauritoControlloGiacenzaDL() {
		return venditeEsauritoControlloGiacenzaDL;
	}

	public void setVenditeEsauritoControlloGiacenzaDL(Boolean venditeEsauritoControlloGiacenzaDL) {
		this.venditeEsauritoControlloGiacenzaDL = venditeEsauritoControlloGiacenzaDL;
	}

	public Boolean getHasPopupConfermaSuMemorizzaInviaBolle() {
		return hasPopupConfermaSuMemorizzaInviaBolle;
	}

	public void setHasPopupConfermaSuMemorizzaInviaBolle(Boolean hasPopupConfermaSuMemorizzaInviaBolle) {
		this.hasPopupConfermaSuMemorizzaInviaBolle = hasPopupConfermaSuMemorizzaInviaBolle;
	}

	public String getHttpProxyServer() {
		return httpProxyServer;
	}

	public void setHttpProxyServer(String httpProxyServer) {
		this.httpProxyServer = httpProxyServer;
	}

	public Integer getHttpProxyPort() {
		return httpProxyPort;
	}

	public void setHttpProxyPort(Integer httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
	}

	public Boolean getHasMessaggioDocumentoDisponibile() {
		return hasMessaggioDocumentoDisponibile;
	}

	public void setHasMessaggioDocumentoDisponibile(Boolean hasMessaggioDocumentoDisponibile) {
		this.hasMessaggioDocumentoDisponibile = hasMessaggioDocumentoDisponibile;
	}

	public boolean isEdicolaPromo() {
		return isEdicolaPromo;
	}

	public void setEdicolaPromo(boolean isEdicolaPromo) {
		this.isEdicolaPromo = isEdicolaPromo;
	}

	public Boolean getViewImageByProfile() {
		return viewImageByProfile;
	}

	public void setViewImageByProfile(Boolean viewImageByProfile) {
		this.viewImageByProfile = viewImageByProfile;
	}

	public Integer getRoleIdProfile() {
		return roleIdProfile;
	}

	public void setRoleIdProfile(Integer roleIdProfile) {
		this.roleIdProfile = roleIdProfile;
	}

	public String getRoleNameProfile() {
		return roleNameProfile;
	}

	public void setRoleNameProfile(String roleNameProfile) {
		this.roleNameProfile = roleNameProfile;
	}

	public GruppoModuliVo getGruppoModuliVo() {
		return gruppoModuliVo;
	}

	public void setGruppoModuliVo(GruppoModuliVo gruppoModuliVo) {
		this.gruppoModuliVo = gruppoModuliVo;
	}

	public Boolean getCheckConsegneGazzetta() {
		return checkConsegneGazzetta;
	}

	public void setCheckConsegneGazzetta(Boolean checkConsegneGazzetta) {
		this.checkConsegneGazzetta = checkConsegneGazzetta;
	}

	public Boolean getAccettoResaCD() {
		return accettoResaCD;
	}

	public void setAccettoResaCD(Boolean accettoResaCD) {
		this.accettoResaCD = accettoResaCD;
	}

	public void setRagioneSocialeDl(String ragioneSocialeDl) {
		this.ragioneSocialeDl = ragioneSocialeDl;
	}

	public void setRagioneSocialeEdicola(String ragioneSocialeEdicola) {
		this.ragioneSocialeEdicola = ragioneSocialeEdicola;
	}

	public void setIndirizzoEdicolaPrimaRiga(String indirizzoEdicolaPrimaRiga) {
		this.indirizzoEdicolaPrimaRiga = indirizzoEdicolaPrimaRiga;
	}

	public void setIndirizzoAgenziaPrimaRiga(String indirizzoAgenziaPrimaRiga) {
		this.indirizzoAgenziaPrimaRiga = indirizzoAgenziaPrimaRiga;
	}

	public void setLocalitaEdicolaPrimaRiga(String localitaEdicolaPrimaRiga) {
		this.localitaEdicolaPrimaRiga = localitaEdicolaPrimaRiga;
	}

	public void setProvinciaEdicola(String provinciaEdicola) {
		this.provinciaEdicola = provinciaEdicola;
	}

	public void setCapEdicola(String capEdicola) {
		this.capEdicola = capEdicola;
	}

	public void setLocalitaAgenziaPrimaRiga(String localitaAgenziaPrimaRiga) {
		this.localitaAgenziaPrimaRiga = localitaAgenziaPrimaRiga;
	}

	public void setProvinciaAgenzia(String provinciaAgenzia) {
		this.provinciaAgenzia = provinciaAgenzia;
	}

	public void setCapAgenzia(String capAgenzia) {
		this.capAgenzia = capAgenzia;
	}

	public void setPivaAgenzia(String pivaAgenzia) {
		this.pivaAgenzia = pivaAgenzia;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNumMaxCpuResaDimenticata(Integer numMaxCpuResaDimenticata) {
		this.numMaxCpuResaDimenticata = numMaxCpuResaDimenticata;
	}

	public void setUserSalt(String userSalt) {
		this.userSalt = userSalt;
	}

	public void setVisualizzaSemaforoGiacenza(Boolean visualizzaSemaforoGiacenza) {
		this.visualizzaSemaforoGiacenza = visualizzaSemaforoGiacenza;
	}

	public void setDataStorico(Timestamp dataStorico) {
		this.dataStorico = dataStorico;
	}

	public void setEmailValido(boolean emailValido) {
		this.emailValido = emailValido;
	}

	public void setAbilitataCorrezioneBarcode(Boolean abilitataCorrezioneBarcode) {
		this.abilitataCorrezioneBarcode = abilitataCorrezioneBarcode;
	}

	public void setHasEdicoleAutorizzateAggiornaBarcode(Boolean hasEdicoleAutorizzateAggiornaBarcode) {
		this.hasEdicoleAutorizzateAggiornaBarcode = hasEdicoleAutorizzateAggiornaBarcode;
	}

	public void setEdicolaTestPerModifiche(Boolean edicolaTestPerModifiche) {
		this.edicolaTestPerModifiche = edicolaTestPerModifiche;
	}

	public void setSpuntaObbligatoriaBollaConsegna(Boolean spuntaObbligatoriaBollaConsegna) {
		this.spuntaObbligatoriaBollaConsegna = spuntaObbligatoriaBollaConsegna;
	}

	public void setSuddivisioneQuotidianiPeriodiciReportVenduto(Boolean suddivisioneQuotidianiPeriodiciReportVenduto) {
		this.suddivisioneQuotidianiPeriodiciReportVenduto = suddivisioneQuotidianiPeriodiciReportVenduto;
	}

	public void setHasLivellamenti(Boolean hasLivellamenti) {
		this.hasLivellamenti = hasLivellamenti;
	}

}
