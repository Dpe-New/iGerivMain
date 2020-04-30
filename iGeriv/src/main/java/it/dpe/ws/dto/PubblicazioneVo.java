package it.dpe.ws.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import it.dpe.igeriv.util.NumberUtils;
import it.dpe.ws.converter.CustomJsonDateDeserializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PubblicazioneVo {
	private Integer	idCopertina;
	private Integer	idProdotto;
	private String	titolo;
	private String	sottoTitolo;
	private String	codiceEdizione;
	private String	numeroCopertina;
	private String	periodicita;
	private Double	prezzoCopertina;
	private Integer	quantita;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date	dataConsegna;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date	dataUscita;
	private String	codiceBarre;
	private String	dl;
	private String	edicola;
	private String	addon;
	private Boolean	disabilitata;
	private String	edizione;
	private Integer	codRivDl;
	private Integer	codRivWeb;
	private String	prezzoCopertinaFormat;
	private Integer coddl;
	
	public String getImportoFormat() {
		return (quantita != null && prezzoCopertina != null) ? NumberUtils.formatNumber(new BigDecimal(0)) : "";
	}
}
