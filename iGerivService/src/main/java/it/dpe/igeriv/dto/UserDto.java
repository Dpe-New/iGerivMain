package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String codUtente;
	private String nomeUtente;
	private String email;
	private String titolo;
	private Integer abilitatoInt;

	public Boolean getAbilitato() {
		return (abilitatoInt != null && abilitatoInt.equals(1)) ? true : false;
	}

	public String getAbilitatoDesc() {
		return getAbilitato() ? IGerivMessageBundle.get(IGerivConstants.SI) : IGerivMessageBundle.get(IGerivConstants.NO);
	}

}
