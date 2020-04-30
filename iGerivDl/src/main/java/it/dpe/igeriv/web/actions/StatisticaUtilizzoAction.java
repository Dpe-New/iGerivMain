package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.statistiche.StatisticheService;
import it.dpe.igeriv.dto.StatisticaUtilizzoDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.BaseVo;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action per il report sulle statistiche di utilizzo.
 * 
 * @author Vincenzo Mazzeo
 */
@Getter
@Setter
@Scope("prototype")
@Component("statisticaUtilizzoAction")
public class StatisticaUtilizzoAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final StatisticheService statisticheService;
	private final String crumbName = getText("igeriv.statistiche.utilizzo");
	private String tableTitle;
	private String dataIniziale;
	private String dataFinale;
	private String codRivendita;
	
	public StatisticaUtilizzoAction() {
		this(null);
	}
	
	@Autowired
	public StatisticaUtilizzoAction(StatisticheService statisticheService) {
		this.statisticheService = statisticheService;
	}
	
	@Override
	public void validate() {
		tableTitle = getText("igeriv.statistiche.utilizzo");
		if (dataIniziale == null || dataFinale == null || dataIniziale.equals("") || dataFinale.equals("")) {
			addActionError(getText("error.specificare.data.o.intervallo.date"));
		} else {
			try {
				DateUtilities.floorDay(DateUtilities.parseDate(dataIniziale,
						DateUtilities.FORMATO_DATA_SLASH));
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataIniziale));
				return;
			}
			try {
				DateUtilities.ceilDay(DateUtilities.parseDate(dataFinale,
						DateUtilities.FORMATO_DATA_SLASH));
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataFinale));
				return;
			}
		}
	}

	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.statistiche.utilizzo");
		return SUCCESS;
	}

	@BreadCrumb("%{crumbName}")
	public String showStatisticaUtilizzo() throws ParseException {
		tableTitle = getText("igeriv.statistiche.utilizzo");
		Timestamp dataIniziale = DateUtilities.parseDate(this.dataIniziale, DateUtilities.FORMATO_DATA_SLASH);
		Timestamp dataFinale = DateUtilities.parseDate(this.dataFinale, DateUtilities.FORMATO_DATA_SLASH);
		Integer codRivendita = this.codRivendita.equals("") ? null : Integer.valueOf(this.codRivendita);
		List<StatisticaUtilizzoDto> statisticheUtilizzo = statisticheService.getStatisticheUtilizzo(getAuthUser().getCodFiegDl(), dataIniziale, dataFinale, codRivendita);
		requestMap.put("statisticheUtilizzo", statisticheUtilizzo);
		return SUCCESS;
	}

	@SuppressWarnings("rawtypes")
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	@SuppressWarnings("rawtypes")
	public void saveParameters(Context context, String arg1, Map arg2) {
	}

}
