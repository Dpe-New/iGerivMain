package it.dpe.jms.dto;


/**
 * Fornitori dell'Edicola di Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
public class ProdottiNonEditorialiFornitoreJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = 1173900155137420008L;
	private String ragioneSociale1;
	private String ragioneSociale2;
	private String indirizzo;
	private String localita;
	private String provincia;
	private String cap;
	private String codiceFiscale;
	private String piva;
	private String telefono;
	private String fax;

	public String getRagioneSociale1() {
		return ragioneSociale1;
	}

	public void setRagioneSociale1(String ragioneSociale1) {
		this.ragioneSociale1 = ragioneSociale1;
	}

	public String getRagioneSociale2() {
		return ragioneSociale2;
	}

	public void setRagioneSociale2(String ragioneSociale2) {
		this.ragioneSociale2 = ragioneSociale2;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public String getLocalita() {
		return localita != null ? localita.trim() : localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}
	
	public String getProvincia() {
		return provincia != null ? provincia.trim() : provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ragioneSociale1 = " + ragioneSociale1 + "\t");
		sb.append("ragioneSociale2 = " + ragioneSociale2 + "\t");
		sb.append("indirizzo = " + indirizzo + "\t");
		sb.append("localita = " + localita + "\t");
		sb.append("provincia = " + provincia + "\t");
		sb.append("cap = " + cap + "\t");
		sb.append("codiceFiscale = " + codiceFiscale + "\t");
		sb.append("piva = " + piva + "\t");
		sb.append("telefono = " + telefono + "\t");
		sb.append("fax = " + fax + "\t");
		return sb.toString();
	}

}
