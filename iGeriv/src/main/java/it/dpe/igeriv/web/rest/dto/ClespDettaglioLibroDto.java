package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ClespDettaglioLibroDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@SerializedName("ean")
	public String ean;			//Chiave primaria articolo
	@SerializedName("titolo")
	public String titolo;		//Titolo dell’articolo
	@SerializedName("autore")
	public String autore;		//Nome autore
	@SerializedName("editore")
	public String editore;		//Editore dell’articolo
	@SerializedName("volume")
	public String volume;		//Indicazione del volume dell’articolo
	@SerializedName("prezzo")
	public BigDecimal prezzo;		//prezzo espresso in Euro dell’articolo
	@SerializedName("copertinabile")
	public String copertinabile;	//Stato di attività Si/No
	@SerializedName("numero_tomi")
	public Integer numero_tomi;		//Numero di tomi effettivamente copertinabili del libro
	@SerializedName("disponibilita")
	public String disponibilita;	//Indica eventuali note circa la disponibilità del titolo alla prenotazione
	@SerializedName("prenotabile")
	public String prenotabile;		//Stato di attività Si/No
	@SerializedName("url_immagine")
	public String url_immagine;		//url pubblico per immagine del libro
	@SerializedName("classe_sconto")
	public Integer classe_sconto;	//url pubblico per immagine del libro

	
	
	
	
}
