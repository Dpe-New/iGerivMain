package it.dpe.jms.dto;

import java.sql.Timestamp;

public class RichiestaRifornimentoJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = -2684186292250199506L;
	private String numeroDocumento;
	private Long codProdottoInterno;
	private Integer codiceProdottoFornitore;
	private Timestamp dataRichiesta;
	private Integer quatitaRichiesta;
	private String note;
	private Integer codEdicolaDl;
	private String stato;
	private String rispostaDl;
	private Timestamp dataUltAggiornamento;
	private Integer codFornitore;
	private Long codiceContabileCliente;
	private String correlationId;
	private Integer quantitaEvasa;
	private Integer statoOrdine;
	private Timestamp dataEvasione;
	private Integer formazionePacco;
	
	public Long getCodProdottoInterno() {
		return codProdottoInterno;
	}

	public void setCodProdottoInterno(Long codProdottoInterno) {
		this.codProdottoInterno = codProdottoInterno;
	}

	public Integer getCodiceProdottoFornitore() {
		return codiceProdottoFornitore;
	}

	public void setCodiceProdottoFornitore(Integer codiceProdottoFornitore) {
		this.codiceProdottoFornitore = codiceProdottoFornitore;
	}

	public Timestamp getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Timestamp dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}
	
	public Integer getQuatitaRichiesta() {
		return quatitaRichiesta == null ? 0 : quatitaRichiesta;
	}

	public void setQuatitaRichiesta(Integer quatitaRichiesta) {
		this.quatitaRichiesta = quatitaRichiesta;
	}
	
	public Integer getQuantitaEvasa() {
		return quantitaEvasa;
	}

	public void setQuantitaEvasa(Integer quantitaEvasa) {
		this.quantitaEvasa = quantitaEvasa;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getCodEdicolaDl() {
		return codEdicolaDl;
	}

	public void setCodEdicolaDl(Integer codEdicolaDl) {
		this.codEdicolaDl = codEdicolaDl;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getRispostaDl() {
		return rispostaDl;
	}

	public void setRispostaDl(String rispostaDl) {
		this.rispostaDl = rispostaDl;
	}

	public Timestamp getDataUltAggiornamento() {
		return dataUltAggiornamento;
	}

	public void setDataUltAggiornamento(Timestamp dataUltAggiornamento) {
		this.dataUltAggiornamento = dataUltAggiornamento;
	}

	public Integer getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(Integer codFornitore) {
		this.codFornitore = codFornitore;
	}

	public Long getCodiceContabileCliente() {
		return codiceContabileCliente;
	}

	public void setCodiceContabileCliente(Long codiceContabileCliente) {
		this.codiceContabileCliente = codiceContabileCliente;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public Integer getStatoOrdine() {
		return statoOrdine == null ? 0 : statoOrdine;
	}

	public void setStatoOrdine(Integer statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	public Timestamp getDataEvasione() {
		return dataEvasione;
	}

	public void setDataEvasione(Timestamp dataEvasione) {
		this.dataEvasione = dataEvasione;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	
	public Integer getFormazionePacco() {
		return formazionePacco;
	}

	public void setFormazionePacco(Integer formazionePacco) {
		this.formazionePacco = formazionePacco;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("numeroDocumento = " + numeroDocumento + "\t");
		sb.append("codProdottoInterno = " + codProdottoInterno + "\t");
		sb.append("codiceProdottoFornitore = " + codiceProdottoFornitore + "\t");
		sb.append("dataRichiesta = " + dataRichiesta + "\t");
		sb.append("quatitaRichiesta = " + quatitaRichiesta + "\t");
		sb.append("note = " + note + "\t");
		sb.append("codEdicolaDl = " + codEdicolaDl + "\t");
		sb.append("stato = " + stato + "\t");
		sb.append("rispostaDl = " + rispostaDl + "\t");
		sb.append("dataUltAggiornamento = " + dataUltAggiornamento + "\t");
		sb.append("codFornitore = " + codFornitore + "\t");
		sb.append("codiceContabileCliente = " + codiceContabileCliente + "\t");
		sb.append("correlationId = " + correlationId + "\t");
		sb.append("quantitaEvasa = " + quantitaEvasa + "\t");
		sb.append("statoOrdine = " + statoOrdine + "\t");
		sb.append("dataEvasione = " + dataEvasione + "\t");
		sb.append("formazionePacco = " + formazionePacco + "\t");
		return sb.toString();
	}

}
