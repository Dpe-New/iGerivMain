package it.dpe.igeriv.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EsportazioneDatiDlResultDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private List<String> fileContent;
	private String resultMsg;
}
