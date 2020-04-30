package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class EdicolaDto implements Serializable {
	private static final long serialVersionUID = -3749440096041192779L;
	private Integer idRivendita;
	private Integer codiceRivenditaDl;
	private String ragioneSocialePrimaParte;
	private String ragioneSocialeSecondaParte;
	private String piva;
	private String indirizzo;
	private String localita;
	private String provincia;
	private Integer codProvincia;
	private String cap;
	private String telefono;
	private String cellulare;
	private String email;
	private Integer software; 
	private String userFtp;
	private String pwdFtp;
	private String coddl;
	private Integer coddl1;
	private Integer coddl2;
	private Integer coddl3;
	private Timestamp dataInserimento;
	private Date dataSospensione;
	private BigDecimal latitudine;
	private BigDecimal longitudine;
	private Integer provenienzaRegistrazione;
	
	public Integer getIdRivendita() {
		return idRivendita;
	}

	public void setIdRivendita(Integer idRivendita) {
		this.idRivendita = idRivendita;
	}

	public Integer getCodiceRivenditaDl() {
		return codiceRivenditaDl;
	}

	public void setCodiceRivenditaDl(Integer codiceRivenditaDl) {
		this.codiceRivenditaDl = codiceRivenditaDl;
	}

	public String getRagioneSocialePrimaParte() {
		return ragioneSocialePrimaParte == null ? "" : ragioneSocialePrimaParte;
	}

	public void setRagioneSocialePrimaParte(String ragioneSocialePrimaParte) {
		this.ragioneSocialePrimaParte = ragioneSocialePrimaParte;
	}

	public String getRagioneSocialeSecondaParte() {
		return ragioneSocialeSecondaParte == null ? "" : ragioneSocialeSecondaParte;
	}

	public void setRagioneSocialeSecondaParte(String ragioneSocialeSecondaParte) {
		this.ragioneSocialeSecondaParte = ragioneSocialeSecondaParte;
	}
	
	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	public String getIndirizzo() {
		return indirizzo == null ? "" : indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getLocalita() {
		return localita == null ? "" : localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	public Integer getCodProvincia() {
		return codProvincia;
	}

	public void setCodProvincia(Integer codProvincia) {
		this.codProvincia = codProvincia;
	}

	public Timestamp getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Timestamp dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Date getDataSospensione() {
		return dataSospensione;
	}

	public void setDataSospensione(Date dataSospensione) {
		this.dataSospensione = dataSospensione;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getCellulare() {
		return cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getSoftware() {
		return software;
	}

	public void setSoftware(Integer software) {
		this.software = software;
	}

	public String getUserFtp() {
		return userFtp;
	}

	public void setUserFtp(String userFtp) {
		this.userFtp = userFtp;
	}

	public String getPwdFtp() {
		return pwdFtp;
	}

	public void setPwdFtp(String pwdFtp) {
		this.pwdFtp = pwdFtp;
	}
	
	public String getCoddl() {
		return coddl;
	}

	public void setCoddl(String coddl) {
		this.coddl = coddl;
	}

	public Integer getCoddl1() {
		return coddl1;
	}

	public void setCoddl1(Integer coddl1) {
		this.coddl1 = coddl1;
	}

	public Integer getCoddl2() {
		return coddl2;
	}

	public void setCoddl2(Integer coddl2) {
		this.coddl2 = coddl2;
	}

	public Integer getCoddl3() {
		return coddl3;
	}

	public void setCoddl3(Integer coddl3) {
		this.coddl3 = coddl3;
	}

	public BigDecimal getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(BigDecimal latitudine) {
		this.latitudine = latitudine;
	}

	public BigDecimal getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(BigDecimal longitudine) {
		this.longitudine = longitudine;
	}
	
	public String getIndirizzoCompleto() {
		return getRagioneSocialePrimaParte().toUpperCase() + " " + getRagioneSocialeSecondaParte().toUpperCase() + ", " + getIndirizzo().toUpperCase() + ", " + getLocalita().toUpperCase();
	}

	public Integer getProvenienzaRegistrazione() {
		return provenienzaRegistrazione;
	}

	public void setProvenienzaRegistrazione(Integer provenienzaRegistrazione) {
		this.provenienzaRegistrazione = provenienzaRegistrazione;
	}
	
}
