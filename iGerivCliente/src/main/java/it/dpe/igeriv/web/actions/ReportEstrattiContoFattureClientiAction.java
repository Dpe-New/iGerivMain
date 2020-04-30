package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.EstrattoContoFatturaClientiDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
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
 * Classe action per i report degli estratti conto e fatture per i clienti edicola.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({"rawtypes"}) 
@Scope("prototype")
@Component("reportEstrattiContoFattureClientiAction")
public class ReportEstrattiContoFattureClientiAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State, 
		RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ClientiService<EstrattoContoFatturaClientiDto> clientiService;
	private List<EstrattoContoFatturaClientiDto> listEcFattura;
	private String filterTitle;
	private String dataDaStr;
	private String dataAStr;
	private Timestamp dataDa;
	private Timestamp dataA;
	private Integer tipo;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		filterTitle = getText("igeriv.report.ec.fatture");
		try {
			if (!Strings.isNullOrEmpty(dataDaStr)) {
				try {
					dataDa = Strings.isNullOrEmpty(dataDaStr) ? null : DateUtilities.floorDay(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDaStr));
					return;
				}
			}
			if (!Strings.isNullOrEmpty(dataAStr)) {
				try {
					dataA = Strings.isNullOrEmpty(dataAStr) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataAStr));
					return;
				}
			}
			if (dataDa == null && dataA == null) {
				dataDaStr = DateUtilities.getTimestampAsString(DateUtilities.togliGiorni(new Timestamp(new Date().getTime()), 15), DateUtilities.FORMATO_DATA_SLASH);
				dataAStr = DateUtilities.getTimestampAsString(new Timestamp(new Date().getTime()), DateUtilities.FORMATO_DATA_SLASH);
				dataDa = DateUtilities.floorDay(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH));
				dataA = DateUtilities.ceilDay(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH));
			}
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return;
		}
	}
	
	@SkipValidation
	public String showFilter() throws Exception {
		filterTitle = getText("igeriv.report.ec.fatture");
		return SUCCESS;
	}
	
	public String show() throws Exception {
		filterTitle = getText("igeriv.report.ec.fatture");
		listEcFattura = clientiService.getListEstrattiContoFattureClienti(new Long(getAuthUser().getId()), dataDa, dataA, tipo);
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see org.extremecomponents.table.state.State#getParameters(org.extremecomponents.table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/* (non-Javadoc)
	 * @see org.extremecomponents.table.state.State#saveParameters(org.extremecomponents.table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}
	
	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.report.ec.fatture");
	}

	public List<EstrattoContoFatturaClientiDto> getListEcFattura() {
		return listEcFattura;
	}

	public void setListEcFattura(List<EstrattoContoFatturaClientiDto> listEcFattura) {
		this.listEcFattura = listEcFattura;
	}

	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}
	
	public String getReportTitle() {
		return MessageFormat.format(getText("igeriv.report.ec.fatture.title"), dataDaStr, dataAStr);
	}

	public String getDataDaStr() {
		return dataDaStr;
	}

	public void setDataDaStr(String dataDaStr) {
		this.dataDaStr = dataDaStr;
	}

	public String getDataAStr() {
		return dataAStr;
	}

	public void setDataAStr(String dataAStr) {
		this.dataAStr = dataAStr;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	
	public ClientiService<EstrattoContoFatturaClientiDto> getClientiService() {
		return clientiService;
	}

	public void setClientiService(ClientiService<EstrattoContoFatturaClientiDto> clientiService) {
		this.clientiService = clientiService;
	}

	public List<KeyValueDto> getListTipoDocumento() {
		List<KeyValueDto> list = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKeyInt(-1);
		dto1.setValue(getText("igeriv.tutti"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKeyInt(IGerivConstants.ESTRATTO_CONTO);
		dto2.setValue(getText("igeriv.data.estratto.conto"));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKeyInt(IGerivConstants.FATTURA);
		dto3.setValue(getText("igeriv.fattura"));
		list.add(dto1);
		list.add(dto2);
		list.add(dto3);
		return list;
	}
	
}
