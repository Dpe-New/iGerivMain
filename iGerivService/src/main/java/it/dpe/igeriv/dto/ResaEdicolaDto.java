package it.dpe.igeriv.dto;

import it.dpe.igeriv.exchange.adapter.DateAdapter;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "export-data")
public class ResaEdicolaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "codice-rivendita")
	private Integer codiceRivendita;
	
	@XmlElement(name = "codice-dl")
	private Integer codiceDl;
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	@XmlElement(name = "data-resa")
	private Date dataResa;
	
	@XmlElement(name = "tipo-resa")
	private String tipoResa;
	
	@XmlElement(name = "zip-file")
	private String zipFile;
	
	@XmlElementWrapper(name = "records")
	@XmlElement(name = "record")
	private List<ItemResaDto> listItensResa;
}
