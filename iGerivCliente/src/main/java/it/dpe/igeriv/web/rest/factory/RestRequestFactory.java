package it.dpe.igeriv.web.rest.factory;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.web.rest.dto.RicercaLibriTestoDto;
import it.dpe.ws.dto.HttpJsonResponse;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;

/**
 * @author mromano
 *
 */
public class RestRequestFactory {
	
	public static RestRequest<ResponseEntity<String>, VenditeParamDto> getTokenRestRequest() {
		RestRequest<ResponseEntity<String>, VenditeParamDto> requestRequest = new RestRequest<ResponseEntity<String>, VenditeParamDto>() {
			@Override
			public void prepareRequest(HttpHeaders requestHeaders, VenditeParamDto dto, String env) {
				Integer codFiegDl = dto.getCodFiegDl();
				Integer codEdicolaDl = dto.getCodEdicolaDl();
				String user = codFiegDl.toString() + "|" + codEdicolaDl.toString();
				requestHeaders.set(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, new String(Base64.encode(user.getBytes())));
				/*
				 * Visto che le edicole possono cambiarsi la pwd su iGeriv, rompendo cosi' l'integrazione con rtae,
				 * per adesso sostituisco la pwd con una costante, che viene gestita su rtae per autenticare le rivendite.
				 * TODO	Terminare il KAS Service: servizio REST centralizzato per l'autenticazione delle rivendite
				 */
				//requestHeaders.set(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY, new String(dto.getPwd().getBytes()));
				requestHeaders.set(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY, new String(IGerivConstants.TOKEN_AUTH_FROM_IGERIV_RTAE.getBytes()));
				requestHeaders.set(AbstractRememberMeServices.DEFAULT_PARAMETER, "true");
				requestHeaders.set("owe", "true");
				requestHeaders.set("os", "IGERIV_MENTA");
				requestHeaders.set("os_version", "1.0");
				requestHeaders.set("device_id", "");
				requestHeaders.set("client_version", "1.0");
				requestHeaders.set("app_name", "IGERIV_" + env.toUpperCase());
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
			}
		};
		return requestRequest;
	}
	
	public static RestRequest<ResponseEntity<HttpJsonResponse>, VenditeParamDto> getAbbonatiRicaricabiliRestRequest() {
		RestRequest<ResponseEntity<HttpJsonResponse>, VenditeParamDto> requestRequest = new RestRequest<ResponseEntity<HttpJsonResponse>, VenditeParamDto>() {
			@Override
			public void prepareRequest(HttpHeaders requestHeaders, VenditeParamDto dto, String env) {
				requestHeaders.set(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, dto.getToken());
				requestHeaders.set("owe", "true");
				requestHeaders.set("os", "IGERIV");
				requestHeaders.set("os_version", "1.0");
				requestHeaders.set("device_id", "");
				requestHeaders.set("client_version", "1.0");
				requestHeaders.set("app_name", "IGERIV_" + env.toUpperCase());
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
			}
		};
		return requestRequest;
	}
	
	public static RestRequest<ResponseEntity<EdicolaDto>, UserVo> getKasRestRequest() {
		RestRequest<ResponseEntity<EdicolaDto>, UserVo> requestRequest = new RestRequest<ResponseEntity<EdicolaDto>, UserVo>() {
			@Override
			public void prepareRequest(HttpHeaders requestHeaders, UserVo user, String env) {
				String codFiegDl = user.getAbbinamentoEdicolaDlVo().getAnagraficaAgenziaVo().getCodFiegDl().toString();
				requestHeaders.set("codFiegDl", codFiegDl);
				String crivDl = user.getAbbinamentoEdicolaDlVo().getCodEdicolaDl().toString();
				requestHeaders.set("crivDl", crivDl);
				requestHeaders.set("pwd", user.getPwd());
				requestHeaders.set("app_name", "IGERIV_KAS_" + env.toUpperCase());
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
			}
		};
		return requestRequest;
	}
	
	public static RestRequest<ResponseEntity<EdicolaDto>, UserVo> getKasPutRestRequest() {
		RestRequest<ResponseEntity<EdicolaDto>, UserVo> requestRequest = new RestRequest<ResponseEntity<EdicolaDto>, UserVo>() {
			@Override
			public void prepareRequest(HttpHeaders requestHeaders, UserVo user, String env) {
				requestHeaders.set("codFiegDl", user.getAbbinamentoEdicolaDlVo().getAnagraficaAgenziaVo().getCodFiegDl().toString());
				requestHeaders.set("crivDl", user.getAbbinamentoEdicolaDlVo().getCodEdicolaDl().toString());
				requestHeaders.set("pwd", user.getPwd());
				requestHeaders.set("app_name", "IGERIV_KAS_" + env.toUpperCase());
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
			}
		};
		return requestRequest;
	}

	
	public static RestRequest<ResponseEntity<HttpJsonResponse>, RicercaLibriTestoDto> getLibriScolasticiRestRequest() {
		RestRequest<ResponseEntity<HttpJsonResponse>, RicercaLibriTestoDto> requestRequest = new RestRequest<ResponseEntity<HttpJsonResponse>, RicercaLibriTestoDto>() {
			@Override
			public void prepareRequest(HttpHeaders requestHeaders, RicercaLibriTestoDto dto, String env) {
				
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
			}
		};
		return requestRequest;
	}
	
	
	
	
}
