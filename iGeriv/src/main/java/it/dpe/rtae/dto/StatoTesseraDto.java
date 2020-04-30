package it.dpe.rtae.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name = "StatoTessera", namespace="http://it.dpe.rtae/ricaricabili/schemas") 
@XmlAccessorType(XmlAccessType.FIELD)
public class StatoTesseraDto extends BaseDto {
	private static final long serialVersionUID = -7226208710536750870L;
	private String creditoResiduo;
	private String dataInizioAbbonamento;
	private String dataFineAbbonamento;
	private Integer copieResidue; 
	private String titolo;
	private String stato;
	private Integer idProdotto;
	private String tipoTessera;
	private Boolean showCopie;
}
