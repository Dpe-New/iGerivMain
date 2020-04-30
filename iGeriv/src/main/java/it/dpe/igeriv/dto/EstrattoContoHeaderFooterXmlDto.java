package it.dpe.igeriv.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import it.dpe.igeriv.exchange.adapter.DateAdapter;
import it.dpe.igeriv.exchange.adapter.IntegerAdapter;
import it.dpe.igeriv.exchange.adapter.StringAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
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

}
