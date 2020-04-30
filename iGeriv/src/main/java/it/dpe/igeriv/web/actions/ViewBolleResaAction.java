package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.dto.DateTipiBollaDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.BollaResa;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.BollaResaRiassunto;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import lombok.Getter;
import lombok.Setter;


/**
 * Classe action per la bolla di resa.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("viewBollaResaAction")
@SuppressWarnings({"unchecked", "rawtypes"}) 
public class ViewBolleResaAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final BolleService bolleService;
	private final String crumbName = getText("igeriv.view.bolla.resa");
	private String dataTipoBolla;
	private Timestamp dtBolla;
	private String tipoBolla;
	private Boolean bollaEnabled;
	private List itensBolla;
	private String filterTitle;
	private Integer tableHeight;
	private List<BollaResaFuoriVoceVo> itensBollaResaFuoriVoce;
	private String BollaResaTab_ev;
	private Boolean soloResoDaInserire;
	private Boolean soloResoConGiacenza;
	private AgenzieService agenzieService;
	
	public ViewBolleResaAction() {
		this.bolleService = null;
	}
	
	@Autowired
	public ViewBolleResaAction(BolleService bolleService,AgenzieService agenzieService) {
		this.bolleService = bolleService;
		this.agenzieService = agenzieService;
	}
	
	@Override
	public void prepare() {
		try {
			super.prepare();
			filterTitle = getText("igeriv.view.bolla.resa");
			requestMap.put("inserisciNuovo", getText("plg.inserisci.nuovo.fuori.voce"));
			requestMap.put("memorizza", getText("igeriv.memorizza"));
			requestMap.put("memorizzaInvia", getText("igeriv.memorizza.invia"));
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
		}
	}
	
	@Override
	public void validate() {
		
	}
	
	@SkipValidation
	public String showFilter() {
		Set<BollaResaRiassuntoVo> bolleResaRiassunto = bolleService.getBolleResaRiassunto(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, null);
		doShowFilter(bolleResaRiassunto);
		return SUCCESS;
	}
	
	public String showBollaResa() {
		try {
			if (dataTipoBolla == null || dataTipoBolla.equals("")) {
				return "input";
			}
			Set<BollaResaRiassuntoVo> bolleResaRiassunto = bolleService.getBolleResaRiassunto(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, null);
			doShowFilter(bolleResaRiassunto);
			setDataTipoBolla();
			itensBolla = bolleService.getDettaglioBollaResa(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, dtBolla, tipoBolla, soloResoDaInserire,soloResoConGiacenza);
			List dettaglioRichiamoPersonalizzato = bolleService.getDettaglioRichiamoPersonalizzato(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, dtBolla, tipoBolla, soloResoDaInserire);
			List dettaglioFuoriVoce = bolleService.getDettaglioFuoriVoce(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, dtBolla, tipoBolla, soloResoDaInserire);
			itensBolla.addAll(dettaglioRichiamoPersonalizzato);
			itensBolla = buildOrderedBollaResaDettagliList(itensBolla, dettaglioFuoriVoce);
			int count = 0;
			for (Object vo : itensBolla) {
				BeanUtils.setProperty(vo, "rownum", ++count);
			}
			
			requestMap.put("itensBolla", itensBolla);
			
			AnagraficaAgenziaVo agenziaVo = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
			
			if(agenziaVo.getTiboresval() != null && agenziaVo.getTiboresval().equals(tipoBolla))
			{
				requestMap.put("strOnSubmitFunction", "$('#exportOnlyResoValorizzato').val('false'); strToReplace; document.forms.BollaResaForm.ec_eti.value=''");
			}
			else
			{
				requestMap.put("strOnSubmitFunction", "jConfirmEsportaResoPdf('" + getText("gp.esportare.solo.reso.valorizzato") + "', attenzioneMsg, function(val) {if (val == 1) { $('#exportOnlyResoValorizzato').val('true'); strToReplace; document.forms.BollaResaForm.ec_eti.value=''} else if (val == 2) { $('#exportOnlyResoValorizzato').val('false'); strToReplace; document.forms.BollaResaForm.ec_eti.value=''} else {unBlockUI(); document.forms.BollaResaForm.ec_eti.value=''} })");
			}
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return SUCCESS;
	}
	
	/**
	 * @throws ParseException
	 */
	private void setDataTipoBolla() throws ParseException {
		if (!Strings.isNullOrEmpty(dataTipoBolla)) {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			tipoBolla = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			bollaEnabled = Boolean.valueOf(st.nextToken().trim());
			dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			requestMap.put("strData", strData);
			requestMap.put("tipo", tipoBolla);
			if (BollaResaTab_ev != null && (BollaResaTab_ev.toUpperCase().equals("PDF") || BollaResaTab_ev.toUpperCase().equals("XLS"))) {
				setAgenziaEdicolaExportTitle();
			}
		}
	}
	
	/**
	 * @param bolleRiassunto 
	 * 
	 */
	@SuppressWarnings("hiding")
	private <T extends BollaResaRiassunto> void doShowFilter(Collection<T> bolleRiassunto) {
		List<DateTipiBollaDto> listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		int count = 0;
		listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		for (BollaResaRiassunto brvo : bolleRiassunto) {
			DateTipiBollaDto dto = new DateTipiBollaDto();
			dto.setIdDataTipoBolla(count);
			dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getDtBolla(), DateUtilities.FORMATO_DATA));
			dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getTipoBolla());
			dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) ? false : true);
			dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
			dto.setGruppoSconto(brvo.getGruppoSconto());
			listDateTipiBolla.add(dto);
			count++;
		}
		requestMap.put("listDateTipiBolla", listDateTipiBolla);
	}
	
	/**
	 * Ritorna una lista di VOs BollaResa dove i vo dei dettagli fuori voce
	 * figurano dopo il richiamo bolla corrispondente. 
	 * 
	 * @param List<BollaResa> itensBolla
	 * @param List<BollaResa> dettaglioFuoriVoce
	 * @return List<BollaResa>
	 */
	private List<BollaResa> buildOrderedBollaResaDettagliList(List<BollaResa> itensBolla,
			List<BollaResa> dettaglioFuoriVoce) {
		List<BollaResa> list = new ArrayList<BollaResa>();
		for (BollaResa br : itensBolla) {
			list.add(br);
			Iterator<BollaResa> it = dettaglioFuoriVoce.iterator();
			while (it.hasNext()) {
				BollaResa br1 = (BollaResa) it.next();
				if (br1.getCpuDl().equals(br.getCpuDl())) {
					list.add(br1);		
					it.remove();
				}
			}
		}
		if (!dettaglioFuoriVoce.isEmpty()) {
			list.addAll(dettaglioFuoriVoce);
		}
		return list;
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
		return super.getTitle() + getText("igeriv.view.bolla.resa");
	}
	
	public String getDataBollaFolder() {
		return (dtBolla != null) ? DateUtilities.getTimestampAsStringExport(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDD) : null;
	}

}
