package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public interface EstrattoContoClienti {

	Long getCodCliente();

	void setCodCliente(Long codCliente);

	String getNome();

	void setNome(String nome);

	String getCognome();

	void setCognome(String cognome);

	String getTitolo();

	void setTitolo(String titolo);

	String getTelefono();

	void setTelefono(String telefono);

	BigDecimal getPrezzo();

	void setPrezzo(BigDecimal prezzo);

	Long getCopie();

	void setCopie(Long copie);

	BigDecimal getImporto();
	
	BigDecimal getImportoPne();
	
	BigDecimal getImportoLordoPne();

	String getTipoLocalita();

	void setTipoLocalita(String tipoLocalita);

	String getIndirizzo();

	void setIndirizzo(String indirizzo);

	Integer getNumeroCivico();

	void setNumeroCivico(Integer numeroCivico);

	String getEstensione();

	void setEstensione(String estensione);
	
	String getLocalitaDesc();

	void setLocalitaDesc(String localitaDesc);

	String getCap();

	void setCap(String cap);

	String getProvinciaDesc();

	void setProvinciaDesc(String provinciaDesc);

	String getPiva();

	void setPiva(String piva);

	String getCodiceFiscale();

	void setCodiceFiscale(String codiceFiscale);

	Float getPrezzoF();

	void setPrezzoF(Float prezzoF);

	String getNomeCognome();

	String getIndirizzoCompleto();

	String getRagSocCliente();

	String getIndirizzoCliente();

	String getDatiFiscali();

	String getPartitaIva();

	String getOrder();

	Timestamp getDataCompetenzaEstrattoContoClienti();

	void setDataCompetenzaEstrattoContoClienti(Timestamp dataCompetenzaEstrattoContoClienti);

	String getBanca();

	void setBanca(String banca);

	String getContoCorrente();

	void setContoCorrente(String contoCorrente);

	String getIban();

	void setIban(String iban);

	String getDatiCliente();

	String getDatiBancari();

	String getTipoPagamento();

	void setTipoPagamento(String tipoPagamento);

	Integer getTipoProdotto();
	
	Integer getGiorniScadenzaPagamento();

	void setGiorniScadenzaPagamento(Integer giorniScadenzaPagamento);
	
	String getDataScadenzaPagamento();
	
	void setSconto(Integer sconto);
	
	Integer getSconto();
	
	void setScontoPne(Integer scontoPne);
	
	Integer getScontoPne();
	
	void setMarcaBollo(BigDecimal marcaBollo);
	
	BigDecimal getMarcaBollo();
	
	public void setAliquota(Integer aliquota);
	
	public Integer getAliquota();
	
	Integer getNumeroEstrattoConto();
	
	public void setNumeroEstrattoConto(Integer numeroEstrattoConto);
	
	public String getSortCriteria();
	
	public void setDataDocumento(Date dataDocumento);
	
	Date getDataDocumento();
	
	public String getDescrizioneCausaleIva();
	
	public void setDescrizioneCausaleIva(String descrizioneCausaleIva);
}