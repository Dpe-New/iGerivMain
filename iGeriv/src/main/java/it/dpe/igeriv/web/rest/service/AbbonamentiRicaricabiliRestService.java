package it.dpe.igeriv.web.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.web.rest.factory.RestRequest;
import it.dpe.igeriv.web.rest.factory.RestRequestFactory;
import it.dpe.ws.dto.HttpJsonResponse;

/**
 * Chiama il servizio rest per la consegna/ricarica delle tessere minicard
 * 
 * @author mromano
 *
 */
@Component("AbbonamentiRicaricabiliRestService")
class AbbonamentiRicaricabiliRestService extends BaseRestService<HttpJsonResponse, VenditeParamDto> implements RestService<HttpJsonResponse, VenditeParamDto> {
	private static final String	LISTA_PUBBLICAZIONI = "/lista_pubblicazioni/tessera/%s/operation/CONSEGNA";
	private static final String	CONSEGNA = "/consegna/tessera/%s/barcode/%s/operation/CONSEGNA";
	private static final String	EDIZIONE = "/edizione/%s";
	private static final String	LISTINO_RICARICHE_COPIA = "/lista_ricariche/tessera/%s/operation/LISTINO_RICARICHE_COPIA";
	private static final String	LISTINO_RICARICHE_VALORE = "/lista_ricariche/tessera/%s/operation/LISTINO_RICARICHE_VALORE";
	private static final String	RICARICA_COPIE = "/ricarica/tessera/%s/idProdotto/%s/idEditore/%s/codTipologiaMinicard/%s/operation/RICARICA_COPIE";
	private static final String	RICARICA_VALORE = "/ricarica/tessera/%s/idProdotto/%s/idEditore/%s/codTipologiaMinicard/%s/operation/RICARICA_VALORE";
	private static final String	LETTURA_STATO = "/stato/tessera/%s/operation/LETTURA_STATO";
	private static final String	EDITORI = "/editori";
	private static final String	PRODOTTI = "/prodotti";
	private static final String	TIPI_TESSERA = "/tipi_tessera";
	private final String env; 
	private final String rtaeRestUrl;
	
	@Autowired
	AbbonamentiRicaricabiliRestService(@Value("${igeriv.rtae.rest.url}") String rtaeRestUrl, @Value("${igeriv.env.deploy.name}") String env) {
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
		if (operation.equals("EDITORI")) {
			op = EDITORI;
		} else if (operation.equals("PRODOTTI")) {
			op = PRODOTTI;
		} else if (operation.equals("TIPI_TESSERA")) {
			op = TIPI_TESSERA;
		} else if (!Strings.isNullOrEmpty(operation) && !Strings.isNullOrEmpty(inputText)) {
			if (operation.equals("CONSEGNA")) {
				String edizione = Strings.isNullOrEmpty(params.getEdizione()) ? "" : String.format(EDIZIONE, params.getEdizione());
				String tpl = Strings.isNullOrEmpty(params.getBarcode()) ? String.format(LISTA_PUBBLICAZIONI, inputText) : String.format(CONSEGNA, inputText, params.getBarcode());
				op = tpl + edizione;
			} else if (operation.equals("LISTINO_RICARICHE_COPIA")) {
				op = String.format(LISTINO_RICARICHE_COPIA, inputText);
			} else if (operation.equals("LISTINO_RICARICHE_VALORE")) {
				op = String.format(LISTINO_RICARICHE_VALORE, inputText);
			} else {
				if (operation.equals("RICARICA_COPIE")) {
					op = String.format(RICARICA_COPIE, inputText, params.getIdProdotto(), params.getIdEditore(), params.getCodTipologiaMinicard());
				} else if (operation.equals("RICARICA_VALORE")) {
					op = String.format(RICARICA_VALORE, inputText, params.getIdProdotto(), params.getIdEditore(), params.getCodTipologiaMinicard());
				} else if (operation.equals("LETTURA_STATO")) {
					op = String.format(LETTURA_STATO, inputText);
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
