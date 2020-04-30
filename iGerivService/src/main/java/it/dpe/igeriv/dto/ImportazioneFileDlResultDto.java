package it.dpe.igeriv.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ImportazioneFileDlResultDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private List<String> logContent;
	private List<String> snagContent;
	private String snagFileName;
	private Long status;
}
