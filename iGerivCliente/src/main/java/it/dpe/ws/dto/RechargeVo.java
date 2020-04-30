package it.dpe.ws.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.Strings;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RechargeVo implements Serializable {
	private static final long serialVersionUID = -6342895430679584414L;
	private Integer codTipologiaMinicard;
	private String tipologiaMinicardEditore;
	private String descrizioneTipologia;
	private Integer codAutorizzazioneRicarica;
	private String codAutorizzazioneRicaricaDesc;
	private Double importoRicarica;
	private String importoRicaricaFormat;
	private Integer copieRicarica;
	private Integer durataGiorniRicarica;
	private String titoloPrimaParte;
	private String stato;
	private String statoDesc;
	private String statoConsegna;
	private String statoConsegnaDesc;
	private String tipoRicarica;
	private String tipoRicaricaDesc;
	private Double aggioRivendita;
	private Double aggioAgenzia;
	private Integer copieGiornaliere;
	private Integer copieMaxRiportare;
	private Integer copieResidueLimite;
	private Integer codUtente;
	private Long dataUltimaModifica;
	private Integer idProdotto;
	private Integer idEditore;
	
	public String getDescrizioneTipologiaPerListino() {
		return (Strings.isNullOrEmpty(titoloPrimaParte) ? "" : titoloPrimaParte) + (Strings.isNullOrEmpty(descrizioneTipologia) ? "" : " " + descrizioneTipologia);
	}
}
