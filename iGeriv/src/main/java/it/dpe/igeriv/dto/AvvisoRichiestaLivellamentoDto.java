package it.dpe.igeriv.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvvisoRichiestaLivellamentoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private List<Long> idLivellamenti;
	
	public AvvisoRichiestaLivellamentoDto(List<Long> idLivellamenti) {
		super();
		this.idLivellamenti = idLivellamenti;
	}
	
}
