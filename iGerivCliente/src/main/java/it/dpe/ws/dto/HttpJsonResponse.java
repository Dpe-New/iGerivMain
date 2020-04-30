package it.dpe.ws.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpJsonResponse {
	private List<Link>	links;
	private Content		content;
	private String		status;

	@Getter
	@Setter
	public static class Link {
		private String	rel;
		private String	href;
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Content {
		public String						type;
		public Integer						level;
		public PubblicazioneVo				pubblicazione;
		public ArrayList<RechargeVo>		listRicariche;
		public ArrayList<EditionVo>			listEdizioni;
		public String						idEditore;
		public String						idProdotto;
		public String						edizione;
		public String						message;
		public String						barcode;
		public Map<String, String>			msgParams;
		public List<CardStatusVo>			listStatoTessera;
		public String						puoiRicaricare;
		public String						tessera;
		public String						operation;
		private List<RitiriDlExportDto> 	ritiri;
		private List<EditoreDlDto> 			editori;
		private List<AnagraficaProdottoDto> prodotti;
		private List<TipoTesseraVo> 		tipiTessera;
		private List<ReportVenditeAbbonatiRivPeriodoDto>	listVenditeAbbonatiRivPeriodo;
		private List<ReportVenditeMinicardRivPeriodoDto>	listVenditeMinicardRivPeriodo;
		private List<ReportRicaricheRivPeriodoDto>	listRicaricheMinicardRivPeriodo;
	}

}
