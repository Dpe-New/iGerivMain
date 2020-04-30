package it.dpe.ws.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditionVo implements Serializable {
	private static final long	serialVersionUID	= -5560227431099951665L;
	private String				edizione;
	private String				descrizioneEdizione;
	private Integer				idCopertina;
}
