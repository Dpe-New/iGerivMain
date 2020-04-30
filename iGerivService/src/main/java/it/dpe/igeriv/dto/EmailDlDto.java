package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EmailDlDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codEmailVo;
	private String reparto;
	private String nome;
	private Integer codFiegDl;
	private String email;
}
