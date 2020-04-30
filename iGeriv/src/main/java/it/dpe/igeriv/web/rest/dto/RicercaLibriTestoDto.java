package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RicercaLibriTestoDto implements Serializable{

	private static final long serialVersionUID = 1L;
	public enum TYPE {
		SEARCH, SEARCH_CACHED, VIEW_DETAIL_BOOK
	}
	
	public TYPE 	typeOp;
	public Integer 	limite;
	public Integer 	disponibilita;
	public Integer 	tipoRicerca;
	public String 	testoRicerca;
	public String 	sku;

	
	public Integer totRecord;
	public String  guid;
	public List<TestoScolasticoDto> TestoScolasticoDto;
	
	public String jsonPutOrdine;
	
	public String numOrdine;
	public String numOrdineTxt;
	
}
