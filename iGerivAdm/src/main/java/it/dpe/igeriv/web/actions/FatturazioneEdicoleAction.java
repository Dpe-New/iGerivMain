package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.dto.FatturazioneDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action per la gestione della fatturazione delle edicole.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "rawtypes"})
@Scope("prototype")
@Component("fatturazioneEdicoleAction")
public class FatturazioneEdicoleAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ContabilitaService contabilitaService;
	private String tableTitle;
	private List<FatturazioneDto> fatturazione;
	private String actionName;
	private String dataStr;
	private String codDl;
	private Integer trimestre;
	private SimpleDateFormat sdf; 
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		tableTitle = getText("igeriv.fatturazione.edicole");
		if (codDl == null || codDl.equals("")) {
			addActionError(getText("plg.selezionare.dl"));
			return;
		}
		if (Strings.isNullOrEmpty(dataStr)) {
			sdf = new SimpleDateFormat(DateUtilities.FORMATO_DATA_SLASH);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dataStr = sdf.format(cal.getTime());
		} else {
			try {
				DateUtilities.parseDate(dataStr, DateUtilities.FORMATO_DATA_SLASH);
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataStr));
				return;
			}
		}
	}
	
	@SkipValidation
	public String showFilterFatturazione() {
		tableTitle = getText("igeriv.fatturazione.edicole");
		return SUCCESS;
	}
		
	public String showFatturazione() throws ParseException {
		tableTitle = getText("igeriv.fatturazione.edicole");
		Integer codDlInt = (codDl != null && !codDl.equals("")) ? Integer.parseInt(codDl) : null;
		Date dtFatturazione = DateUtilities.parseDate(dataStr, DateUtilities.FORMATO_DATA_SLASH);
		fatturazione = contabilitaService.getFatturazioneEdicole(codDlInt, dtFatturazione, trimestre);
		requestMap.put("fatturazione", fatturazione);
		String title = null;
		if (trimestre.equals(IGerivConstants.COD_TRIMETRE_PRECEDENTE)) {
			title = MessageFormat.format(getText("igeriv.report.title.trimestre.precedente"), DateUtilities.getTimestampAsString(dtFatturazione, DateUtilities.FORMATO_DATA_SLASH), DateUtilities.getTimestampAsString(dtFatturazione, DateUtilities.FORMATO_DATA_SLASH));
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dtFatturazione);
			cal.add(Calendar.MONTH, 3);
			Date dtFatturazionePiu3Mesi = cal.getTime();
			title = MessageFormat.format(getText("igeriv.report.title.trimestre.successivo"), DateUtilities.getTimestampAsString(dtFatturazione, DateUtilities.FORMATO_DATA_SLASH), DateUtilities.getTimestampAsString(dtFatturazionePiu3Mesi, DateUtilities.FORMATO_DATA_SLASH));
		}
		requestMap.put("title", title);
		return SUCCESS;
	}
	
	
	@SkipValidation
	public String showNewFilterFatturazione() {
		tableTitle = getText("igeriv.fatturazione.edicole");
		return SUCCESS;
	}
	
	public String showNewFatturazione() throws ParseException {
		tableTitle = getText("igeriv.fatturazione.edicole");
		Integer codDlInt = (codDl != null && !codDl.equals("")) ? Integer.parseInt(codDl) : null;
		Date dtFatturazione = DateUtilities.parseDate(dataStr, DateUtilities.FORMATO_DATA_SLASH);
		fatturazione = contabilitaService.getFatturazioneEdicole(codDlInt, dtFatturazione, trimestre);
		requestMap.put("fatturazione", fatturazione);
		String title = null;
		if (trimestre.equals(IGerivConstants.COD_TRIMETRE_PRECEDENTE)) {
			title = MessageFormat.format(getText("igeriv.report.title.trimestre.precedente"), DateUtilities.getTimestampAsString(dtFatturazione, DateUtilities.FORMATO_DATA_SLASH), DateUtilities.getTimestampAsString(dtFatturazione, DateUtilities.FORMATO_DATA_SLASH));
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dtFatturazione);
			cal.add(Calendar.MONTH, 3);
			Date dtFatturazionePiu3Mesi = cal.getTime();
			title = MessageFormat.format(getText("igeriv.report.title.trimestre.successivo"), DateUtilities.getTimestampAsString(dtFatturazione, DateUtilities.FORMATO_DATA_SLASH), DateUtilities.getTimestampAsString(dtFatturazionePiu3Mesi, DateUtilities.FORMATO_DATA_SLASH));
		}
		requestMap.put("title", title);
		return SUCCESS;
	}
	
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#getParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#saveParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.visualizza.edicole");
	}

	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	public ContabilitaService getContabilitaService() {
		return contabilitaService;
	}

	public void setContabilitaService(ContabilitaService contabilitaService) {
		this.contabilitaService = contabilitaService;
	}

	public List<FatturazioneDto> getFatturazione() {
		return fatturazione;
	}

	public void setFatturazione(List<FatturazioneDto> fatturazione) {
		this.fatturazione = fatturazione;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getDataStr() {
		return dataStr;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public String getCodDl() {
		return codDl;
	}

	public void setCodDl(String codDl) {
		this.codDl = codDl;
	}

	public Integer getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(Integer trimestre) {
		this.trimestre = trimestre;
	}
	
}
