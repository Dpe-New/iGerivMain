package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivQueryContants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Tabella utenti
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries( {
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_COD_DL_EDICOLA, query = "select vo.codEdicolaDl from AbbinamentoEdicolaDlVo vo where vo.codDpeWebEdicola = :codDpeWebEdicola"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_COD_EDICOLA_WEB, query = "select vo.codDpeWebEdicola from AbbinamentoEdicolaDlVo vo where vo.codEdicolaDl = :codiceRivenditaDl and vo.anagraficaAgenziaVo.codFiegDl = :codFiegDl"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ABBINAMENTO_EDICOLA_DL, query = "from AbbinamentoEdicolaDlVo vo where vo.codEdicolaDl = :codiceRivenditaDl and vo.anagraficaAgenziaVo.codFiegDl = :codFiegDl"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_DATA_SOSPENSIONE_EDICOLA, query = "update AbbinamentoEdicolaDlVo vo set vo.dtSospensioneEdicola = :dtSospensioneEdicola,vo.checkConsegneGazzetta = :checkConsegneGazzetta where vo.anagraficaAgenziaVo.codFiegDl = :codFiegDl and vo.codEdicolaDl = :codEdicolaDl")
})
@Table(name = "tbl_9206", schema = "")
public class AbbinamentoEdicolaDlVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "crivw9206")
	private Integer codDpeWebEdicola;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9206", updatable = false, insertable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo anagraficaEdicolaVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "coddl9206", updatable = true, insertable = true, referencedColumnName = "coddl9107")	
	private AnagraficaAgenziaVo anagraficaAgenziaVo;
	@Column(name = "crivdl9206")
	private Integer codEdicolaDl;
	@Column(name = "agenz9206")
	private Integer agenziaFatturazione;
	@Column(name = "gs9206")
	private Integer gruppoSconto;
	@Column(name = "datat9206")
	private Timestamp dtAttivazioneEdicola;
	@Column(name = "dasos9206")
	private Timestamp dtSospensioneEdicola;
	@Column(name = "coso9206")
	private Long codiceContabileCliente;
	@Column(name = "test9206")
	private Boolean edicolaTest;
	@Column(name = "hostftp9206")
	private String hostFtp;
	@Column(name = "userftp9206")
	private String userFtp;
	@Column(name = "pwdftp9206")
	private String pwdFtp;
	@Column(name = "odirftp9206")
	private String remoteFtpDir;
	@Column(name = "datsto9206")
	private Timestamp dataCreazioneStatistica;
	@Column(name = "privso9206")
	@Getter(AccessLevel.NONE)
	private Boolean privacySottoscritta;
	@Column(name = "emailv9206")
	@Getter(AccessLevel.NONE)
	private Boolean emailValido;
	@Column(name = "daulmo9206")
	private Timestamp dtUltimoAggiornamento;
	@Column(name = "ipautpr9206")
	private String indirizzoIpRichiesta;
	@Column(name = "corbar9206")
	private Boolean abilitataCorrezioneBarcode;
	@Column(name = "crivwm9206")
	private Integer codEdicolaMaster;
	@Column(name = "start9206")
	@Getter(AccessLevel.NONE)
	private Boolean edicolaStarter;
	@Column(name = "ristart9206")
	@Getter(AccessLevel.NONE)
	private Boolean richiestaProva;
	@Column(name = "dainplu9206")
	private Timestamp dataInizioiGerivPlus;
	@Column(name = "dafiplu9206")
	private Timestamp dataFineiGerivPlus;
	@Column(name = "ediinfo9206")
	private Boolean edicolaIGerivInforiv;
	@Column(name = "grvnet9206")
	private Boolean edicolaIGerivNet;
	@Column(name = "testmo9206")
	private Boolean edicolaTestPerModifiche;
	@Column(name = "anacomp9206")
	private Boolean anagraficaCompilata;
	@Column(name = "condusoacc9206")
	private Boolean condizioniUsoAccettate;
	@Column(name = "agprodvar9206")
	private Boolean aggiornataProdottiVariDl;
	@Column(name = "dainprm9206")
	private Timestamp dataInizioiGerivPromo;
	@Column(name = "dafiprm9206")
	private Timestamp dataFineiGerivPromo;
	@Column(name = "deinffi9206")
	private Boolean deleteInforivFtpFileAfterDownload;
	@Column(name = "ediinfdl9206")
	private Boolean edicolaIGerivInforivDl;
	@Column(name = "cocgz9206")
	private Boolean checkConsegneGazzetta;
	@Column(name = "accde9206")
	private Boolean accettoResaCD;
	// Vittorio 26/08/2020
	@Column(name = "gessep9206")
	private Boolean gesSepDevTod;

	// Vittorio 26/01/2021
	@Column(name = "edseci9206")
	private Boolean edSecCintura;
	
	@Formula("to_char(crivw9206)") 
	private String codDpeWebEdicolaString;
	
	
	
	@Transient
	private String dataInserimento_format;
	@Transient
	private String dataSospensione_format;
	
	public String getDataInserimento_format(){
		return DateUtilities.getTimestampAsString(dtAttivazioneEdicola, DateUtilities.FORMATO_DATA_SLASH);
	}
	public String getDataSospensione_format(){
		return DateUtilities.getTimestampAsString(dtSospensioneEdicola, DateUtilities.FORMATO_DATA_SLASH);
	}
	
	
	public Boolean getPrivacySottoscritta() {
		return privacySottoscritta == null ? false : privacySottoscritta;
	}

	public Boolean getEmailValido() {
		return emailValido == null ? false : emailValido;
	}

	public Boolean getEdicolaStarter() {
		return edicolaStarter == null ? false : edicolaStarter;
	}
	
	public Boolean getRichiestaProva() {
		return richiestaProva == null ? false : richiestaProva;
	}

	public boolean getEdicolaiGerivPlus() {
		if (getDataInizioiGerivPlus() != null) {
			Date now = new Date();
			Date dataFine = getDataFineiGerivPlus() != null ? getDataFineiGerivPlus() : DateUtilities.END_OF_TIME;
			return getDataInizioiGerivPlus().before(now) && dataFine.after(now);
		} else {
			return false;
		}
	}

	public boolean getIsEdicolaPromo() {
		if (getDataInizioiGerivPromo() != null) {
			Date endDate = getDataFineiGerivPromo();
			if (endDate == null) {
				endDate = DateUtilities.END_OF_TIME;
			}
			Date now = new Date();
			return (getDataInizioiGerivPromo().before(now) && endDate.after(now));
		}
		return false;
	}
	
	
	
	
}
