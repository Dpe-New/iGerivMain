package it.dpe.igeriv.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IGerivCardResultDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private List<VenditaDettaglioDto> ultimiAcquisti;
	private List<VenditaDettaglioDto> suggerimentiVendita;
	private List<String> promozioni;
	private String contoNome;
	private Long codCliente;
}
