package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.RichiestaAggiornamentoBarcodeVo;
import lombok.Getter;
import lombok.Setter;


/**
 * Classe action per il verifica eventi.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("pollingAction")
public class PollingAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	private final VenditeService venditeService;
	private final BolleService bolleService;
	private Integer codPagina;
	private String time;
	private String dataBolla;
	private String tipoBolla;
	private Map<String, Object> result = new HashMap<String, Object>();
	
	public PollingAction() {
		this.venditeService = null;
		this.bolleService = null;
	}
	
	@Autowired
	public PollingAction(VenditeService venditeService, BolleService bolleService) {
		this.venditeService = venditeService;
		this.bolleService = bolleService;
	}
	
	@SkipValidation
	public String execute() {
		if (getAuthUser().getHasEdicoleAutorizzateAggiornaBarcode() && codPagina != null) {
			Timestamp dtBolla = null;
			String tpBolla = null;
			if (!Strings.isNullOrEmpty(dataBolla) && codPagina.equals(IGerivConstants.COD_PAGINA_BOLLA_CONSEGNA) || codPagina.equals(IGerivConstants.COD_PAGINA_BOLLA_RESA)) {
				try {
					dtBolla = DateUtilities.parseDate(dataBolla, DateUtilities.FORMATO_DATA);
				} catch (ParseException e) {
					log.error("Parsing error in PollingAction for date: " + dataBolla, e);
				}
				tpBolla = tipoBolla;
			}
			Timestamp dateTime = null;
			if (!Strings.isNullOrEmpty(time) && !time.equals("undefined")) {
				try {
					dateTime = DateUtilities.parseDate(time, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
				} catch (ParseException e) {
					log.error("Parsing error in PollingAction for time:" + time, e);
				}
			}
			List<RichiestaAggiornamentoBarcodeVo> richiesteAgg = dateTime != null ? venditeService.getRichiesteEseguiteAggiornamentoBarcodeVo(getAuthUser().getCodFiegDl(), dateTime) : null;
			if (richiesteAgg != null && !richiesteAgg.isEmpty()) {
				result.put("time", DateUtilities.getTimestampAsString(venditeService.getSysdate(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
				Map<String, String> map = new HashMap<String, String>();
				for (RichiestaAggiornamentoBarcodeVo rab : richiesteAgg) {
					Integer progressivo = codPagina.equals(IGerivConstants.COD_PAGINA_BOLLA_CONSEGNA) ? bolleService.getProgressivoIdtnBollaConsegna(getAuthUser().getCodFiegDl(), getAuthUser().getCodDpeWebEdicola(), dtBolla, tpBolla, rab.getPk().getIdtn()) : bolleService.getProgressivoIdtnBollaResa(getAuthUser().getCodFiegDl(), getAuthUser().getCodDpeWebEdicola(), dtBolla, tpBolla, rab.getPk().getIdtn());
					String pk = getAuthUser().getCodFiegDl().toString() + "|" + getAuthUser().getCodDpeWebEdicola().toString() + "|" + DateUtilities.getTimestampAsString(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + tipoBolla + "|" + progressivo;
					map.put(rab.getPk().getCodiceBarre(), pk);
				}
				result.put("pubb", map);
			}
		}
		return SUCCESS;
	}
	
}
