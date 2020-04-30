package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CodDlEdicoleSecondarieDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codFiegDl;
	private Integer codDpeWebEdicola;
}
