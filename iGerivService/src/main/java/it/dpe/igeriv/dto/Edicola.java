package it.dpe.igeriv.dto;

import java.sql.Timestamp;

public interface Edicola {

	Integer getCodEdicolaWeb();

	void setCodEdicolaWeb(Integer codEdicolaWeb);

	Integer getCodEdicolaDl();

	void setCodEdicolaDl(Integer codEdicolaDl);

	Integer getCodEdicolaDl2();

	void setCodEdicolaDl2(Integer codEdicolaDl2);

	Integer getCodEdicolaDl3();

	void setCodEdicolaDl3(Integer codEdicolaDl3);

	Integer getCodDl();

	void setCodDl(Integer codDl);

	Integer getCodDl2();

	void setCodDl2(Integer codDl2);

	Integer getCodDl3();

	void setCodDl3(Integer codDl3);

	String getRagioneSociale();

	String getRagioneSociale1();

	void setRagioneSociale1(String ragioneSociale1);

	String getRagioneSociale2();

	void setRagioneSociale2(String ragioneSociale2);

	String getIndirizzo();

	void setIndirizzo(String indirizzo);

	String getLocalita();

	void setLocalita(String localita);

	String getProvincia();

	void setProvincia(String provincia);

	Timestamp getDataInserimento();

	void setDataInserimento(Timestamp dataInserimento);

	Timestamp getDataSospensione();

	void setDataSospensione(Timestamp dataSospensione);

	String getPassword();

	void setPassword(String password);

	String getEmail();

	void setEmail(String email);

	Integer getProfilo();

	void setProfilo(Integer profilo);

	String getPk();

	String getDescProfilo();

	void setDescProfilo(String descProfilo);

	String getFtpUser();

	void setFtpUser(String ftpUser);

	String getFtpPwd();

	void setFtpPwd(String ftpPwd);

	String getFtpHost();

	void setFtpHost(String ftpHost);

	String getCap();

	void setCap(String cap);

	String getTelefono();

	void setTelefono(String telefono);

	String getFax();

	void setFax(String fax);

	String getPiva();

	void setPiva(String piva);

	String getCodFiscale();

	void setCodFiscale(String codFiscale);

	Boolean getEdicolaTest();

	void setEdicolaTest(Boolean edicolaTest);

	Boolean getEdicolaPlus();

	void setEdicolaPlus(Boolean edicolaPlus);

	Long getCodiceContabileCliente();

	void setCodiceContabileCliente(Long codiceContabileCliente);

	Timestamp getDataInizioiGerivPlus();

	void setDataInizioiGerivPlus(Timestamp dataInizioiGerivPlus);

	Timestamp getDataFineiGerivPlus();

	void setDataFineiGerivPlus(Timestamp dataFineiGerivPlus);

	Boolean getEdicolaIGerivInforiv();

	void setEdicolaIGerivInforiv(Boolean edicolaIGerivInforiv);

	Boolean getEdicolaIGerivNet();

	void setEdicolaIGerivNet(Boolean edicolaIGerivNet);
	
	Double getLatitudine();
	
	void setLatitudine(Double latitudine);
	
	Double getLongitudine();
	
	void setLongitudine(Double longitudine);
	
	String getFake();
	
	String getFake2();
	
	String getStrEdicolaTest();

	void setStrEdicolaTest(Boolean edicolaTest);

}