package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ArretratiDto extends BaseDto {
	private static final long serialVersionUID = 1340372161945378068L;
	private Integer codiceArretrato;
	private Timestamp dtBolla;
	private Integer indicatoreTrasmessoDl;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private Timestamp dataUscita;
	private Timestamp dataEvasione;
	private Timestamp dataSpedizioneConferma;
	private Integer quantitaEvasa;
	private Integer quantita;
	private String note;
	private Boolean confermaSi;
	private Boolean confermaNo;
}
