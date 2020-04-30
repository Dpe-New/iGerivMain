package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InventarioJsonDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String dataInventario;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private String dataUscitaFormat;
	private String prezzoEdicolaFormat;
	private String prezzoCopertinaFormat;
	private String quantita;
	private String importoTotale;
	private String isContoDeposito;
	private String isScaduta;
	private String giacenza;
}
