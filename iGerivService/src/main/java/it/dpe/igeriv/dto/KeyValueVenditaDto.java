package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class KeyValueVenditaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codVendita;
	private Integer progressivo;
	private Boolean isProdottoNonEditoriale;
}
