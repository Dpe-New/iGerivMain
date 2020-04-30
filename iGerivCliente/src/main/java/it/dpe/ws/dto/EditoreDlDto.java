package it.dpe.ws.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditoreDlDto implements Serializable {
	private static final long serialVersionUID = 7262636397015059865L;
	private Integer idEditoreDl;
	private String nomeEditoreDl;
	private String nomeFileRegolamento;

}
