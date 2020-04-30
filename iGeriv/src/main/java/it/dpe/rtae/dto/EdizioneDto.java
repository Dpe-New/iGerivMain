package it.dpe.rtae.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name = "Edizione", namespace="http://it.dpe.rtae/ricaricabili/schemas") 
@XmlAccessorType(XmlAccessType.FIELD)
public class EdizioneDto extends BaseDto {
	private static final long serialVersionUID = -1420625351371213304L;
	private final String edizione;
	private final String descrizioneEdizione;
	private Integer idCopertina;
	
	public EdizioneDto() {
		this.edizione = null;
		this.descrizioneEdizione = null;
	}
	
	public EdizioneDto(String edizione, String descrizioneEdizione) {
		this.edizione = edizione;
		this.descrizioneEdizione = descrizioneEdizione;
	}
}
