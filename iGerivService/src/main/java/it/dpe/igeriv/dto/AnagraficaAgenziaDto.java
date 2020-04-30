package it.dpe.igeriv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnagraficaAgenziaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codFiegDl;
	private String ragioneSocialeDlPrimaRiga;
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AnagraficaAgenziaDto) {
			return ((AnagraficaAgenziaDto) obj).getCodFiegDl().equals(this.getCodFiegDl());
		}
		return super.equals(obj);
	}
}
