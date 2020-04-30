package it.dpe.ws.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoTesseraVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tipoTessera;
	private String descrizioneTipoTessera;
}
