package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RilevamentiDto extends BaseDto {
	private static final long serialVersionUID = 3042159093064304607L;
	private Integer codiceRilevamento;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Timestamp dtBolla;
	private String tipoBolla;
	private Integer posizioneRiga;
	private Integer idtn;
	private Integer distribuito;
	private Integer rilevamento;
	private Integer indicatoreTrasmessoDl;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private Timestamp dataUscita;
	private BigDecimal prezzoCopertina;
	private String nomeImmagine;
}
