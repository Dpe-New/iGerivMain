package it.dpe.igeriv.dto;

import it.dpe.igeriv.exchange.adapter.DateAdapter;
import it.dpe.igeriv.exchange.adapter.IntegerAdapter;
import it.dpe.igeriv.exchange.adapter.StringAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "header")
public class EstrattoContoHeaderFooterXmlDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date dataCreazioneFile;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String oraCreazioneFile;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String logoFileName;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String title;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String title1;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String titoloLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String prezzoLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String copieLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String importoLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String totaleLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String controllareDatiLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String pagamentoLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String edicola;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String intestazioneCliente;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String pieDiPagina;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String valoreMarcaBollo;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String fileName;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String ggDataDocLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String dataScadenzaPagamentoLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String importoScontatoLabel;
	
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private Integer scontoPerc;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String marcaBolloLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String scontoLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String scontoPneLabel;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String datiEdicola;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String datiEdicolaInt;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String numeroEstrattoContoLabel;
	
	public Date getDataCreazioneFile() {
		return dataCreazioneFile;
	}

	public void setDataCreazioneFile(Date dataCreazioneFile) {
		this.dataCreazioneFile = dataCreazioneFile;
	}

	public String getOraCreazioneFile() {
		return oraCreazioneFile;
	}

	public void setOraCreazioneFile(String oraCreazioneFile) {
		this.oraCreazioneFile = oraCreazioneFile;
	}

	public String getValoreMarcaBollo() {
		return valoreMarcaBollo;
	}

	public void setValoreMarcaBollo(String valoreMarcaBollo) {
		this.valoreMarcaBollo = valoreMarcaBollo;
	}

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getTitoloLabel() {
		return titoloLabel;
	}

	public void setTitoloLabel(String titoloLabel) {
		this.titoloLabel = titoloLabel;
	}

	public String getPrezzoLabel() {
		return prezzoLabel;
	}

	public void setPrezzoLabel(String prezzoLabel) {
		this.prezzoLabel = prezzoLabel;
	}

	public String getCopieLabel() {
		return copieLabel;
	}

	public void setCopieLabel(String copieLabel) {
		this.copieLabel = copieLabel;
	}

	public String getImportoLabel() {
		return importoLabel;
	}

	public void setImportoLabel(String importoLabel) {
		this.importoLabel = importoLabel;
	}

	public String getTotaleLabel() {
		return totaleLabel;
	}

	public void setTotaleLabel(String totaleLabel) {
		this.totaleLabel = totaleLabel;
	}

	public String getControllareDatiLabel() {
		return controllareDatiLabel;
	}

	public void setControllareDatiLabel(String controllareDatiLabel) {
		this.controllareDatiLabel = controllareDatiLabel;
	}

	public String getPagamentoLabel() {
		return pagamentoLabel;
	}

	public void setPagamentoLabel(String pagamentoLabel) {
		this.pagamentoLabel = pagamentoLabel;
	}

	public String getEdicola() {
		return edicola;
	}

	public void setEdicola(String edicola) {
		this.edicola = edicola;
	}

	public String getIntestazioneCliente() {
		return intestazioneCliente;
	}

	public void setIntestazioneCliente(String intestazioneCliente) {
		this.intestazioneCliente = intestazioneCliente;
	}

	public String getPieDiPagina() {
		return pieDiPagina;
	}

	public void setPieDiPagina(String pieDiPagina) {
		this.pieDiPagina = pieDiPagina;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getGgDataDocLabel() {
		return ggDataDocLabel;
	}

	public void setGgDataDocLabel(String ggDataDocLabel) {
		this.ggDataDocLabel = ggDataDocLabel;
	}

	public String getDataScadenzaPagamentoLabel() {
		return dataScadenzaPagamentoLabel;
	}

	public void setDataScadenzaPagamentoLabel(String dataScadenzaPagamentoLabel) {
		this.dataScadenzaPagamentoLabel = dataScadenzaPagamentoLabel;
	}

	public String getImportoScontatoLabel() {
		return importoScontatoLabel;
	}

	public void setImportoScontatoLabel(String importoScontatoLabel) {
		this.importoScontatoLabel = importoScontatoLabel;
	}

	public Integer getScontoPerc() {
		return scontoPerc;
	}

	public void setScontoPerc(Integer scontoPerc) {
		this.scontoPerc = scontoPerc;
	}

	public String getMarcaBolloLabel() {
		return marcaBolloLabel;
	}

	public void setMarcaBolloLabel(String marcaBolloLabel) {
		this.marcaBolloLabel = marcaBolloLabel;
	}

	public String getScontoLabel() {
		return scontoLabel;
	}

	public void setScontoLabel(String scontoLabel) {
		this.scontoLabel = scontoLabel;
	}

	public String getScontoPneLabel() {
		return scontoPneLabel;
	}

	public void setScontoPneLabel(String scontoPneLabel) {
		this.scontoPneLabel = scontoPneLabel;
	}

	public String getDatiEdicola() {
		return datiEdicola;
	}

	public void setDatiEdicola(String datiEdicola) {
		this.datiEdicola = datiEdicola;
	}

	public String getDatiEdicolaInt() {
		return datiEdicolaInt;
	}

	public void setDatiEdicolaInt(String datiEdicolaInt) {
		this.datiEdicolaInt = datiEdicolaInt;
	}

	public String getNumeroEstrattoContoLabel() {
		return numeroEstrattoContoLabel;
	}

	public void setNumeroEstrattoContoLabel(String numeroEstrattoContoLabel) {
		this.numeroEstrattoContoLabel = numeroEstrattoContoLabel;
	}

}
