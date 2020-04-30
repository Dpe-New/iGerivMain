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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Classe utente del sistema.
 * Instanziata durante il login e salvata nella session del'utente autenticato. 
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
public class UserAbbonato extends User {
	private static final long serialVersionUID = 1L;
	private Calendar cal = Calendar.getInstance();
	private Integer id;
	private Integer[] arrId;
	private String codUtente;
	@Getter(AccessLevel.NONE)
	private String ragioneSocialeDl;
	@Getter(AccessLevel.NONE)
	private String ragioneSocialeEdicola;
	@Getter(AccessLevel.NONE)
	private String indirizzoEdicolaPrimaRiga;
	@Getter(AccessLevel.NONE)
	private String indirizzoAgenziaPrimaRiga;
	@Getter(AccessLevel.NONE)
	private String localitaEdicolaPrimaRiga;
	@Getter(AccessLevel.NONE)
	private String provinciaEdicola;
	@Getter(AccessLevel.NONE)
	private String capEdicola;
	@Getter(AccessLevel.NONE)
	private String localitaAgenziaPrimaRiga;
	@Getter(AccessLevel.NONE)
	private String provinciaAgenzia;
	@Getter(AccessLevel.NONE)
	private String capAgenzia;
	@Getter(AccessLevel.NONE)
	private String pivaAgenzia;
	@Getter(AccessLevel.NONE)
	private String nome;
	@Getter(AccessLevel.NONE)
	private String cognome;
	@Getter(AccessLevel.NONE)
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
	@Getter(AccessLevel.NONE)
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
	@Getter(AccessLevel.NONE)
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
	@Getter(AccessLevel.NONE)
	private Timestamp dataStorico;
	@Getter(AccessLevel.NONE)
	private boolean emailValido;
	private Integer giornoSettimanaPermessaResaDimenticata;
	private Integer tipoControlloPubblicazioniRespinte;
	@Getter(AccessLevel.NONE)
	private Boolean abilitataCorrezioneBarcode;
	@Getter(AccessLevel.NONE)
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
	@Getter(AccessLevel.NONE)
	private Boolean edicolaTestPerModifiche;
	private Boolean campagnaInvitaColleghiAbilitata;
	private Boolean gestioneAnagraficaRivenditaObbligatoria;
	private Boolean anagraficaCompilata;
	private Boolean condizioniUsoAccettate;
	private Boolean visualizzaResoRiscontratoStatistica;
	private Boolean permetteInserimentoRichiesteRifornimentoFuture;
	@Getter(AccessLevel.NONE)
	private Boolean spuntaObbligatoriaBollaConsegna;
	@Getter(AccessLevel.NONE)
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
	@Getter(AccessLevel.NONE)
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

}
