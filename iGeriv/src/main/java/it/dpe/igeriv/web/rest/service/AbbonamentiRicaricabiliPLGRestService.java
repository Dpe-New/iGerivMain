package it.dpe.igeriv.web.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.web.rest.factory.RestRequest;
import it.dpe.igeriv.web.rest.factory.RestRequestFactory;
import it.dpe.ws.dto.HttpJsonResponse;

/**
 * Chiama il servizio rest per la consegna/ricarica/reports delle tessere abbonati/minicard della Gazzetta di Parma
 * 
 * @author mromano
 *
 */
@Component("AbbonamentiRicaricabiliPLGRestService")
class AbbonamentiRicaricabiliPLGRestService extends BaseRestService<HttpJsonResponse, VenditeParamDto> implements RestService<HttpJsonResponse, VenditeParamDto> {
	private static final String	CONSEGNA_MINICARD = "/consegna_minicard/tessera/%s";
	private static final String	CONSEGNA_ABBONATO = "/consegna_abbonato/tessera/%s";
	private static final String	CONSEGNA_ABBONATO_BARCODE = "/consegna_abbonato/tessera/%s/barcode/%s";
	private static final String	CONSEGNA_COLLATERALE = "/consegna_collaterale/tessera/%s/barcode/%s";
	private static final String	LISTINO_RICARICHE_COPIA = "/lista_ricariche/tessera/%s";
	private static final String	RICARICA_COPIE = "/ricarica/tessera/%s/codTipologiaMinicard/%s";
	private static final String	LETTURA_STATO = "/stato/tessera/%s";
	private static final String	VENDITE_ABBONATO_RIV_PERIODO = "/get_vendite_abbonato_riv_periodo/datIn/%s/datFi/%s";
	private static final String	VENDITE_MINICARD_RIV_PERIODO = "/get_vendite_minicard_riv_periodo/datIn/%s/datFi/%s";
	private static final String	RICARICHE_MINICARD_RIV_PERIODO = "/get_ricariche_minicard_riv_periodo/datIn/%s/datFi/%s";
	private final String env; 
	private final String rtaeRestUrl;
	
	@Autowired
	AbbonamentiRicaricabiliPLGRestService(@Value("${igeriv.plg.rest.url}") String rtaeRestUrl, @Value("${igeriv.env.deploy.name}") String env) {
		this.rtaeRestUrl = rtaeRestUrl;
		this.env = env;
	}
	
	@Override
	public ResponseEntity<HttpJsonResponse> getEntity(VenditeParamDto params) {
		RestRequest<ResponseEntity<HttpJsonResponse>, VenditeParamDto> restRequest = RestRequestFactory.getAbbonatiRicaricabiliRestRequest();
		return executeRestRequest(restRequest, params, getUrl(params), HttpMethod.POST, HttpJsonResponse.class, env);
	}

	/**
	 * @param params
	 * @return
	 */
	private String getUrl(VenditeParamDto params) {
		String op = null;
		String operation = params.getOperation();
		String inputText = params.getInputText();
		String barcode = params.getBarcode();
		if (!Strings.isNullOrEmpty(operation)) {
			if (operation.equals(VenditeCardResultDto.ResultType.VENDITE_ABBONATO_RIV_PERIODO.toString())
					&& !Strings.isNullOrEmpty(params.getDataDa()) && !Strings.isNullOrEmpty(params.getDataA())) {
				op = String.format(VENDITE_ABBONATO_RIV_PERIODO, params.getDataDa(), params.getDataA());
			} else if (operation.equals(VenditeCardResultDto.ResultType.VENDITE_MINICARD_RIV_PERIODO.toString())
					&& !Strings.isNullOrEmpty(params.getDataDa()) && !Strings.isNullOrEmpty(params.getDataA())) {
				op = String.format(VENDITE_MINICARD_RIV_PERIODO, params.getDataDa(), params.getDataA());
			} else if (operation.equals(VenditeCardResultDto.ResultType.RICARICHE_MINICARD_RIV_PERIODO.toString())
					&& !Strings.isNullOrEmpty(params.getDataDa()) && !Strings.isNullOrEmpty(params.getDataA())) {
				op = String.format(RICARICHE_MINICARD_RIV_PERIODO, params.getDataDa(), params.getDataA());
			}
			if (!Strings.isNullOrEmpty(inputText)) {
				if (operation.equals("CONSEGNA")) { 
					if (inputText.length() == 10) {
						if (inputText.startsWith("10")) {
							op = String.format(CONSEGNA_MINICARD, inputText);
						} else if (!inputText.startsWith("10") && !Strings.isNullOrEmpty(barcode)) {
							op = String.format(CONSEGNA_ABBONATO_BARCODE, inputText, barcode);
						} else if (!inputText.startsWith("10")) {
							op = String.format(CONSEGNA_ABBONATO, inputText);
						}
					}
				} else if (operation.equals("CONSEGNA_COLLATERALE")) {
					op = String.format(CONSEGNA_COLLATERALE, inputText, barcode);
				} else if (operation.equals("LISTINO_RICARICHE_COPIA")) {
					op = String.format(LISTINO_RICARICHE_COPIA, inputText);
				} else {
					if (operation.equals("RICARICA_COPIE")) {
						op = String.format(RICARICA_COPIE, inputText, params.getCodTipologiaMinicard());
					} else if (operation.equals("LETTURA_STATO")) {
						op = String.format(LETTURA_STATO, inputText);
					}
				}
			}
		}
		String url = rtaeRestUrl + "/rest" + op;
		return url;
	}
	
	@Override
	public ResponseEntity<HttpJsonResponse> putEntity(VenditeParamDto entity) {
		return null;
	}

}
