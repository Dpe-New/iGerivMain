package models;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mromano
 *
 */
public class AbbonatoDto implements Serializable {
	private static final long serialVersionUID = 6749377174351383708L;
	private Integer idAbbonato;
	private Integer codiceInternoAbbonato;
	private String ragioneSocialePrimaParte;
	private String ragioneSocialeSecondaParte;
	private String indirizzo;
	private String localita;
	private String provincia;
	private Integer codLocalita;
	private Integer codProvincia;
	private String cap;
	private String telefono;
	private String telefono2;
	private String fax;
	private String email;
	private Date dataNascita;
	private String sesso;
	private Integer idProdotto;
	private String codRivendita;
	private Integer provenienzaRegistrazione;
	
	public AbbonatoDto() {
		// TODO Auto-generated constructor stub
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getLocalita() {
		return localita;
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
	
	public Integer getCodLocalita() {
		return codLocalita;
	}

	public void setCodLocalita(Integer codLocalita) {
		this.codLocalita = codLocalita;
	}

	public Integer getCodProvincia() {
		return codProvincia;
	}

	public void setCodProvincia(Integer codProvincia) {
		this.codProvincia = codProvincia;
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

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public Integer getIdAbbonato() {
		return idAbbonato;
	}

	public void setIdAbbonato(Integer idAbbonato) {
		this.idAbbonato = idAbbonato;
	}

	public Integer getCodiceInternoAbbonato() {
		return codiceInternoAbbonato;
	}

	public void setCodiceInternoAbbonato(Integer codiceInternoAbbonato) {
		this.codiceInternoAbbonato = codiceInternoAbbonato;
	}

	public String getRagioneSocialePrimaParte() {
		return ragioneSocialePrimaParte;
	}

	public void setRagioneSocialePrimaParte(String ragioneSocialePrimaParte) {
		this.ragioneSocialePrimaParte = ragioneSocialePrimaParte;
	}

	public String getRagioneSocialeSecondaParte() {
		return ragioneSocialeSecondaParte;
	}

	public void setRagioneSocialeSecondaParte(String ragioneSocialeSecondaParte) {
		this.ragioneSocialeSecondaParte = ragioneSocialeSecondaParte;
	}

	public Integer getIdProdotto() {
		return idProdotto;
	}
	
	public void setIdProdotto(Integer idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getCodRivendita() {
		return codRivendita;
	}

	public void setCodRivendita(String codRivendita) {
		this.codRivendita = codRivendita;
	}

	public Integer getProvenienzaRegistrazione() {
		return provenienzaRegistrazione;
	}

	public void setProvenienzaRegistrazione(Integer provenienzaRegistrazione) {
		this.provenienzaRegistrazione = provenienzaRegistrazione;
	}
	
}
