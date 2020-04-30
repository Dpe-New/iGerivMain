package it.dpe.igeriv.dto;

import it.dpe.igeriv.exchange.adapter.DateTimeAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "record")
public class ItemResaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "idtn")
	private Integer idtn;
	
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "data-ora-lavorazione")
	private Date dataOraLavorazione;
	
	@XmlElement(name = "copie")
	private Integer copie;
	
	@XmlElement(name = "file")
	private String nomeFile;
	
}
