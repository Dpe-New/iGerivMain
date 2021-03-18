package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.selectDistinct;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.DateTipiBollaDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.PrenotazioneVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import lombok.Getter;
import lombok.Setter;


/**
 * Classe action per la bolla di consegna modalità spunta.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("bollaRivenditaSpuntaAction")
@SuppressWarnings({"unchecked","rawtypes"}) 
public class BollaRivenditaSpuntaAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final BolleService bolleService;
	private final RifornimentiService rifornimentiService;
	private final MessaggiService messaggiService;
	private final String crumbName = getText("igeriv.spunta.bolla.consegna");
	private String tipoOperazione;
	private String soloRigheSpuntare;
	private List<BollaRiassuntoVo> bolleRiassunto;
	private String dataTipoBolla;
	private List itensBolla;
	private List<MessaggiBollaVo> messaggiBolla;
	private List<RichiestaRifornimentoDto> richiesteRifornimento;
	private String idtn;
	private String periodicita;
	private String filterTitle;
	private String tableTitle;
	private String actionName;
	private String actionName1;
	private Integer tableHeight;
	private PrenotazioneVo prenotazioneVo;
	private String quantitaVariazioneServizio;
	private RichiestaFissaClienteEdicolaVo richiestaFissaClienteEdicolaVo;
	private String prenotazioneDisabled = "false";
	private boolean disableForm;
	private String differenze;
	private String spunte;
	private String pk;
	private String differenzeFondoBolla;
	private String spunteFondoBolla;
	private String pkFondoBolla;
	private String dataScadenzaRichiesta;
	private String quantitaRifornimento;
	private String noteVendita;
	private String dataScadenzaRichiestaAggiunte;
	private String quantitaRifornimentoAggiunte;
	private String noteVenditaAggiunte;
	private String pkAggiunte;
	private String titolo;
	private String stato;
	private String idCliente;
	private String nomeCliente;
	private String importoLordo;
	private String downloadToken;
	private String dataDa;
	private String dataA;
	private String BollaControlloTab_ev;
	private String cpu;
	
	public BollaRivenditaSpuntaAction() {
		this.bolleService = null;
		this.rifornimentiService = null;
		this.messaggiService = null;
	}
	
	@Autowired
	public BollaRivenditaSpuntaAction(BolleService bolleService, RifornimentiService rifornimentiService, MessaggiService messaggiService) {
		this.bolleService = bolleService;
		this.rifornimentiService = rifornimentiService;
		this.messaggiService = messaggiService;
	}
	
	@Override
	public void validate() {
		
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		filterTitle = getText("igeriv.spunta.bolla.consegna");
		requestMap.put("memorizza", getText("memorizza.differenze"));
		requestMap.put("memorizzaInvia", getText("igeriv.memorizza.invia"));
		requestMap.put("memorizzaInviaCtrlI", getText("igeriv.memorizza.invia.ctrl.i"));
		requestMap.put("esportaOrdini", getText("igeriv.esporta.ordini.clienti"));
		doShowFilter();
	}
	
	@SkipValidation
	public String showFilter() throws Exception {
		return SUCCESS;
	}
	
	@SkipValidation
	public String showBolla() throws Exception {
		if (dataTipoBolla == null || dataTipoBolla.equals("")) {
			return "input";
		}
		StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
		String strData = st.nextToken();
		String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
		Boolean enabled = Boolean.valueOf(st.nextToken().trim());
		Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);

		// Vittorio 26/08/2020
		//Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		//Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
		Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
		
		itensBolla = bolleService.getDettaglioBolla(arrCodFiegDl, arrId, dtBolla, tipo, false, getAuthUser().isMultiDl());
		List<?> dettagliFondoBolla = bolleService.getDettagliFondoBolla(arrCodFiegDl, arrId, dtBolla, tipo, false, true, getAuthUser().isMultiDl());
		messaggiBolla = messaggiService.getMessaggiBolla(arrCodFiegDl, arrId, dtBolla, tipo);
		itensBolla.addAll(dettagliFondoBolla);
		int count = 0;
		for (Object vo : itensBolla) {
			BeanUtils.setProperty(vo, "rownum", ++count);
			BeanUtils.setProperty(vo, "isSpuntaBolla", true);
		}
		requestMap.put("itensBolla", itensBolla);
		requestMap.put("messaggiBolla", messaggiBolla);
		requestMap.put("strData", strData);
		requestMap.put("tipo", tipo);
		
		if (BollaControlloTab_ev != null && (BollaControlloTab_ev.toUpperCase().equals("PDF") || BollaControlloTab_ev.toUpperCase().equals("XLS"))) {
			setAgenziaEdicolaExportTitle();
		}
		disableForm = !enabled;
		return SUCCESS;
	}
	
	public String save() {
		try {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			BollaRiassuntoVo bollaRiassunto = bolleService.getBollaRiassunto(getAuthUser().getCodFiegDl(), getAuthUser().getCodDpeWebEdicola(), dtBolla, tipo);
			if (bollaRiassunto.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("impossibile.salvare.bolla.gia.trasmessa") + IGerivConstants.END_EXCEPTION_TAG);
	    		throw new IGerivRuntimeException();
	    	}
//			// 30/09/2014
			LinkedHashMap<String, BollaRiassuntoVo> listBolleDistinct = new LinkedHashMap<String, BollaRiassuntoVo>();
			Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};

			String[] arrPks = pk.split(",");
			for (int i = 0; i < arrPks.length; i++) {
				String pkValue = arrPks[i].trim();
				String[] arrPkValue = pkValue.split("\\|");
				String coddl = arrPkValue[0].trim();
				if(coddl!=null && listBolleDistinct.get(coddl)==null){
					for (Integer idCrivw : arrId) {
						BollaRiassuntoVo bollaCoddl =  bolleService.getBollaRiassunto(new Integer(coddl), idCrivw, dtBolla, tipo);
						if(bollaCoddl!=null){
							listBolleDistinct.put(coddl, bollaCoddl);
							break;
						}
					}
				}
			}
			
			bolleService.saveSpuntaBollaRivendita(differenze, spunte, pk, null, null, null, getAuthUser().getCodDpeWebEdicola(), listBolleDistinct);
			//bolleService.saveSpuntaBollaRivendita(differenze, spunte, pk, null, null, null, getAuthUser().getCodDpeWebEdicola());
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	    return "save";
	}
	
	public String send() {
		try {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			BollaRiassuntoVo bollaRiassunto = bolleService.getBollaRiassunto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);
			if (bollaRiassunto.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("impossibile.salvare.bolla.gia.trasmessa") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			LinkedHashMap<String, BollaRiassuntoVo> listBolleDistinct = new LinkedHashMap<String, BollaRiassuntoVo>();
			Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};

			String[] arrPks = pk.split(",");
			for (int i = 0; i < arrPks.length; i++) {
				String pkValue = arrPks[i].trim();
				String[] arrPkValue = pkValue.split("\\|");
				String coddl = arrPkValue[0].trim();
				if(coddl!=null && listBolleDistinct.get(coddl)==null){
					for (Integer idCrivw : arrId) {
						BollaRiassuntoVo bollaCoddl =  bolleService.getBollaRiassunto(new Integer(coddl), idCrivw, dtBolla, tipo);
						if(bollaCoddl!=null){
							listBolleDistinct.put(coddl, bollaCoddl);
							break;
						}
					}
				}
			}
				
			
			
			bolleService.saveAndSendSpuntaBollaRivendita(differenze, spunte, pk, null, null, null, getAuthUser().getCodDpeWebEdicola(),listBolleDistinct);
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "save";
	}
	
	@SkipValidation
	public String showOrdini() throws Exception {
		if (idtn != null && !idtn.equals("")) {
			Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
			Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
			List<RichiestaClienteDto> ordini = rifornimentiService.getRichiesteClienteByIdtn(arrCodFiegDl, arrId, new Integer(idtn), null);
			requestMap.put("ordini", ordini);
		}
		requestMap.put("tableTitle", getText("igeriv.nuovi.ordini.clienti"));
		return IGerivConstants.ACTION_ORDINI;
	}
	
	private void doShowFilter() {
		// Vittorio 26/08/2020
		//Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		//Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
		Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};

		bolleRiassunto = bolleService.getBolleRiassunto(arrCodFiegDl, arrId, null);
		if (getAuthUser().isMultiDl()) {
			Collection<BollaRiassuntoVo> selectDistinct = selectDistinct(bolleRiassunto, new Comparator<BollaRiassuntoVo>() {
				@Override
				public int compare(BollaRiassuntoVo o1, BollaRiassuntoVo o2) {
					int i = 0;
					if (o1 != null && o2 != null) {
						i = -(o1.getPk().getDtBolla().compareTo(o2.getPk().getDtBolla()));
						if (i != 0) {
					    	return i;
					    }
						i = o1.getPk().getTipoBolla().compareTo(o2.getPk().getTipoBolla());
						if (i != 0) {
					    	return i;
					    }
					}
					return i;
				}
			});
			bolleRiassunto = new ArrayList<BollaRiassuntoVo>(selectDistinct);
		}
		List<KeyValueDto> listSoloRighaDaSpuntare = buildMapSoloRighaDaSpuntare();
		List<DateTipiBollaDto> listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		int count = 0;
		listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		for (BollaRiassuntoVo vo : bolleRiassunto) {
			DateTipiBollaDto dto = new DateTipiBollaDto();
			BollaRiassuntoVo brvo = (BollaRiassuntoVo) vo;
			dto.setIdDataTipoBolla(count);
			dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getPk().getDtBolla(), DateUtilities.FORMATO_DATA));
			dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getPk().getTipoBolla());
			dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) ? false : true);
			dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
			listDateTipiBolla.add(dto);
			count++;
		}
		requestMap.put("listDateTipiBolla", listDateTipiBolla);
		requestMap.put("listSoloRighaDaSpuntare", listSoloRighaDaSpuntare);
		if (tipoOperazione == null || tipoOperazione.equals("")) {
			tipoOperazione = "1";
		}
	}
	
	public List<BollaRiassuntoVo> getBolleRiassunto() {
		return bolleRiassunto;
	}

	public void setBolleRiassunto(List<BollaRiassuntoVo> bolleRiassunto) {
		this.bolleRiassunto = bolleRiassunto;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getSoloRigheSpuntare() {
		return soloRigheSpuntare;
	}

	public void setSoloRigheSpuntare(String soloRigheSpuntare) {
		this.soloRigheSpuntare = soloRigheSpuntare;
	}
	
	public List<T> getItensBolla() {
		return itensBolla;
	}

	public void setItensBolla(List<T> itensBolla) {
		this.itensBolla = itensBolla;
	}
	
	public List<RichiestaRifornimentoDto> getRichiesteRifornimento() {
		return richiesteRifornimento;
	}

	public void setRichiesteRifornimento(
			List<RichiestaRifornimentoDto> richiesteRifornimento) {
		this.richiesteRifornimento = richiesteRifornimento;
	}

	public String getDataTipoBolla() {
		return dataTipoBolla;
	}

	public void setDataTipoBolla(String dataTipoBolla) {
		this.dataTipoBolla = dataTipoBolla;
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
		return super.getTitle() + getText("igeriv.spunta.bolla.consegna");
	}
	
	public String getNomeCliente() {
		return (nomeCliente != null) ? nomeCliente.toUpperCase() : nomeCliente;
	}

	protected List<KeyValueDto> buildMapSoloRighaDaSpuntare() {
		List<KeyValueDto> listSoloRigheDaSpuntare = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("1");
		dto1.setValue(getText("igeriv.si"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("2");
		dto2.setValue(getText("igeriv.no"));
		listSoloRigheDaSpuntare.add(dto2);
		listSoloRigheDaSpuntare.add(dto1);
		return listSoloRigheDaSpuntare;
	}

}
