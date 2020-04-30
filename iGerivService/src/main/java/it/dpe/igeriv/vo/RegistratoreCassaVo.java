package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.springframework.security.crypto.codec.Base64;

import com.google.common.base.Strings;

@Getter
@Setter
@Entity
@Table(name = "tbl_9711", schema = "")
public class RegistratoreCassaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codreg9711")
	private Integer codRegCassa;
	@Column(name = "nomreg9711")
	private String modello;
	@Column(name = "desreg9711")
	private String descrizione;
	@Column(name = "patreg9711")
	private String pathLocaleFile;
	@Column(name = "fipref9711")
	private String fileNamePrefix;
	@Column(name = "fdiglf9711")
	private String fileNameDigitLength;
	@Column(name = "timeou9711")
	private Integer millsTaskTimeout;
	@Column(name = "nompro9711")
	private String nomeProcesso;
	@Column(name = "filini9711")
	private String contentFileIni;
	@Column(name = "manual9711")
	private String fileNameManualeInstallazione;
	@Column(name = "abilog9711")
	private Boolean abilitaLog;
	@Column(name = "mailfr9711")
	private String mailFrom;
	@Column(name = "mailto9711")
	private String mailTo;
	@Column(name = "logfin9711")
	private String logFileName;
	@Column(name = "logsiz9711")
	private Long logFileSize;
	@Column(name = "smtpho9711")
	private String smtpHost;
	@Column(name = "smtpus9711")
	private String smtpUser;
	@Column(name = "smtppw9711")
	private String smtpPwd;
	@Column(name = "ossupp9711")
	private String sistemiOperativiSupportati;
	@Column(name = "binnix9711")
	private String binaryUnixLinux;
	@Column(name = "binosx9711")
	private String binaryMac;
	@Column(name = "binsun9711")
	private String binarySolaris;
	@Column(name = "sfnfix9711")
	private Boolean isScontrinoFileNameFixed;
	
	public String getSmtpHostC() {
		if (!Strings.isNullOrEmpty(getSmtpHost())) {
			return new String(Base64.encode(getSmtpHost().getBytes()));
		}
		return "";
	}
	
	public String getSmtpUserC() {
		if (!Strings.isNullOrEmpty(getSmtpUser())) {
			return new String(Base64.encode(getSmtpUser().getBytes()));
		}
		return "";
	}
	
	public String getSmtpPwdC() {
		if (!Strings.isNullOrEmpty(getSmtpPwd())) {
			return new String(Base64.encode(getSmtpPwd().getBytes()));
		}
		return "";
	}
	
}
