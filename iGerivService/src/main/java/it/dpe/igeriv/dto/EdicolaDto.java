package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class EdicolaDto extends BaseDto implements Edicola {
	private static final long serialVersionUID = 1L;
	private String codUtente;
	private Integer codEdicolaWeb;
	private Integer codEdicolaDl;
	private Integer codEdicolaDl2;
	private Integer codEdicolaDl3;
	private Integer codDl;
	private Integer codDl2;
	private Integer codDl3;
	private String ragioneSociale1;
	private String ragioneSociale2;
	private String indirizzo;
	private String localita;
	private String provincia;
	private Timestamp dataInserimento;
	private Timestamp dataSospensione;
	private String password;
	private String email;
	private Integer profilo;
	private String descProfilo;
	private String ftpUser;
	private String ftpPwd;
	private String ftpHost;
	private String cap;
	private String telefono;
	private String fax;
	private String piva;
	private String codFiscale;
	private Boolean edicolaTest;
	private Boolean edicolaPlus;
	@Getter(AccessLevel.NONE)
	private Long codiceContabileCliente;
	private Timestamp dataInizioiGerivPlus;
	private Timestamp dataFineiGerivPlus;
	@Getter(AccessLevel.NONE)
	private Boolean edicolaIGerivInforiv;
	@Getter(AccessLevel.NONE)
	private Boolean edicolaIGerivNet;
	private Double latitudine;
	private Double longitudine;
	private Boolean anagraficaCompilata;
	private Boolean condizioniUsoAccettate;
	private Timestamp dataInizioiGerivPromo;
	private Timestamp dataFineiGerivPromo;
	@Getter(AccessLevel.NONE)
	private Boolean deleteInforivFtpFileAfterDownload;
	private String remoteFtpDir;
	private String flggeo;
	private boolean isInMyGroup;
	
	private String strEdicolaTest;
	private Boolean checkConsegneGazzetta;	
	
	@Override
	public String getRagioneSociale() {
		return (ragioneSociale1 != null ? ragioneSociale1 : "") + " " + (ragioneSociale2 != null ? ragioneSociale2 : "");
	}

	@Override
	public String getPk() {
		return getCodEdicolaWeb().toString();
	}

	@Override
	public Long getCodiceContabileCliente() {
		return codiceContabileCliente == null ? -1 : codiceContabileCliente;
	}

	@Override
	public Boolean getEdicolaIGerivInforiv() {
		return edicolaIGerivInforiv == null ? false : edicolaIGerivInforiv;
	}

	@Override
	public Boolean getEdicolaIGerivNet() {
		return edicolaIGerivNet == null ? false : edicolaIGerivNet;
	}
	
	public Boolean getDeleteInforivFtpFileAfterDownload() {
		return deleteInforivFtpFileAfterDownload == null ? false : deleteInforivFtpFileAfterDownload;
	}
	
	@Override
	public String getFake() {
		return "";
	}
	
	@Override
	public String getFake2() {
		return "";
	}
	
	
	public String getDataSospensioneString() {
		String result = null;
		
		if (dataSospensione != null) {
			result = DateUtilities.getTimestampAsString(new Date(dataSospensione.getTime()), DateUtilities.FORMATO_DATA_SLASH);
		}
		
		return result;
	}
	
	public void setDataSospensioneString(String strDataSospensione) {
		if (strDataSospensione == null || strDataSospensione.isEmpty()) {
			dataSospensione = null;
		}
		else {
			try {
				dataSospensione = DateUtilities.parseDate(strDataSospensione, DateUtilities.FORMATO_DATA_SLASH);
			}
			catch (ParseException pe) {}
		}
	}
	
	@Override
	public void setStrEdicolaTest(Boolean edicolaTest) {
		if(edicolaTest==null || !edicolaTest){
			this.strEdicolaTest = " ";
		}else{
			this.strEdicolaTest = "SI";
		}
		
	}
	

}
