package it.dpe.igeriv.dto;

import it.dpe.igeriv.vo.pk.RichiestaClientePk;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RichiestaClienteReportEtichetteDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private RichiestaClientePk pk;
	private String titolo;
	private String sottoTitolo;
	private String nome;
	private String cognome;
	private String numeroCopertina;
	private Integer quantitaRichiesta;
	private String telefono;
	
	private String titolo1;
	private String sottoTitolo1;
	private String nome1;
	private String cognome1;
	private String numeroCopertina1;
	private Integer quantitaRichiesta1;
	private String telefono1;
	
	private String titolo2;
	private String sottoTitolo2;
	private String nome2;
	private String cognome2;
	private String numeroCopertina2;
	private Integer quantitaRichiesta2;
	private String telefono2;
	
	private String titolo3;
	private String sottoTitolo3;
	private String nome3;
	private String cognome3;
	private String numeroCopertina3;
	private Integer quantitaRichiesta3;
	private String telefono3;
}
