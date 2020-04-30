package it.dpe.igeriv.web.actions;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.campagna.CampagnaService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.CampagnaEdicoleVo;
import it.dpe.igeriv.vo.CampagnaVo;
import it.dpe.service.mail.MailingListService;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per gli arretrati.
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("campagnaAction")
public class CampagnaAction extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final String crumbName = getText("comunicazioni.ferie.title");
	private String titleComunicazioniFerie;
	private final CampagnaService service;
	public CampagnaEdicoleVo campagnaEdicola;
	private final AgenzieService agenzieService;
	private final MailingListService mailingListService;
	public CampagnaVo campagna;
	
	private final EdicoleService  serviceEdicole;
	
	private Map<String, String> anagraficaEdicola;
	
	private Map<String, String> campagnaTurno1;
	private Map<String, String> campagnaTurno2;
	
	
	
	String S1_CheckBox;
	String S2_CheckBox;
	String S3_CheckBox;
	@Getter
	@Setter
	String S1_Op1DataDal;
	@Getter
	@Setter
	String S1_Op1DataAl;
	@Getter
	@Setter
	String S2_Op1DataDal;
	@Getter
	@Setter
	String S2_Op1DataAl;
	@Getter
	@Setter
	String S3_Op1DataDal;
	@Getter
	@Setter
	String S3_Op1DataAl;
	@Getter
	@Setter
	Integer crivw;
	@Getter
	@Setter
	String msgSave;
	//private final ArretratiService<ArretratiDto> service;
	private String filterTitle;

	private String dataConferma;
	private String dataOp1Dal;
	private String dataOp1Al;
	private int totGiorniOp1;
	private String dataOp2Dal;
	private String dataOp2Al;
	private int totGiorniOp2;
			
	public CampagnaAction() {
		this.service = null;
		this.serviceEdicole = null;
		this.agenzieService = null;
		this.mailingListService = null;
	}
	
	@Autowired
	public CampagnaAction(CampagnaService service,EdicoleService serviceEdicole,AgenzieService agenzieService,MailingListService mailingListService) {
		this.service = service;
		this.serviceEdicole = serviceEdicole;
		this.agenzieService = agenzieService;
		this.mailingListService = mailingListService;
		
		this.anagraficaEdicola = new LinkedHashMap<>();
		this.campagnaTurno1 = new LinkedHashMap<String, String>();
		this.campagnaTurno2 = new LinkedHashMap<String, String>();
		this.campagnaEdicola = new CampagnaEdicoleVo();
		this.msgSave = "";
		this.dataConferma = "";
		this.dataOp1Dal= "";
		this.dataOp1Al= "";
		this.totGiorniOp1= 0;
		this.dataOp2Dal= "";
		this.dataOp2Al= "";
		this.totGiorniOp2= 0;
	}
	
	@Override
	public void prepare() throws Exception {
		titleComunicazioniFerie = getText("comunicazioni.ferie.title");
		super.prepare();
	}
	
	@SkipValidation
	public String showFerie() {
		
		AnagraficaEdicolaVo anagraficaEdicolaVo = serviceEdicole.getAnagraficaEdicola(getAuthUser().getCodDpeWebEdicola());
		if(anagraficaEdicolaVo!=null){
			anagraficaEdicola.put("ragioneSocialeEdicola", anagraficaEdicolaVo.getRagioneSocialeEdicolaPrimaRiga() );
			anagraficaEdicola.put("indirizzoEdicola", anagraficaEdicolaVo.getIndirizzoEdicolaPrimaRiga());
			anagraficaEdicola.put("localitaEdicola",anagraficaEdicolaVo.getLocalitaEdicolaPrimaRiga());
		}
		
		
		campagnaEdicola = service.getEdicoleByCrivw(getAuthUser().getCodDpeWebEdicola());
		if(campagnaEdicola!=null){
			requestMap.put("stato", campagnaEdicola.getFlgstato9227());
			requestMap.put("flgAperto", campagnaEdicola.getFlgaperto9227());
						
			if(campagnaEdicola.getCampagnaVo()!=null){
				String s1op1_dal = DateUtilities.getTimestampAsString(campagnaEdicola.getCampagnaVo().getTr1Op1Dal9226(), DateUtilities.FORMATO_DATA_SLASH);
				campagnaTurno1.put("S1_Op1DataDal",(s1op1_dal!=null)?s1op1_dal:"");
				
				String s1op1_al = DateUtilities.getTimestampAsString(campagnaEdicola.getCampagnaVo().getTr1Op1Al9226(), DateUtilities.FORMATO_DATA_SLASH);
				campagnaTurno1.put("S1_Op1DataAl",(s1op1_al!=null)?s1op1_al:"");
				
				String s2op1_dal = DateUtilities.getTimestampAsString(campagnaEdicola.getCampagnaVo().getTr1Op2Dal9226(), DateUtilities.FORMATO_DATA_SLASH);
				campagnaTurno1.put("S2_Op1DataDal",(s2op1_dal!=null)?s2op1_dal:"");
				
				String s2op1_al = DateUtilities.getTimestampAsString(campagnaEdicola.getCampagnaVo().getTr1Op2Al9226(), DateUtilities.FORMATO_DATA_SLASH);
				campagnaTurno1.put("S2_Op1DataAl",(s2op1_al!=null)?s2op1_al:"");
			}
			if(campagnaEdicola.getCampagnaVo()!=null){
				String s1op1_dal = DateUtilities.getTimestampAsString(campagnaEdicola.getCampagnaVo().getTr2Op1Dal9226(), DateUtilities.FORMATO_DATA_SLASH);
				campagnaTurno2.put("S1_Op1DataDal",(s1op1_dal!=null)?s1op1_dal:"");
				
				String s1op1_al = DateUtilities.getTimestampAsString(campagnaEdicola.getCampagnaVo().getTr2Op1Al9226(), DateUtilities.FORMATO_DATA_SLASH);
				campagnaTurno2.put("S1_Op1DataAl",(s1op1_al!=null)?s1op1_al:"");
				
				String s2op1_dal = DateUtilities.getTimestampAsString(campagnaEdicola.getCampagnaVo().getTr2Op2Dal9226(), DateUtilities.FORMATO_DATA_SLASH);
				campagnaTurno2.put("S2_Op1DataDal",(s2op1_dal!=null)?s2op1_dal:"");
				
				String s2op1_al =DateUtilities.getTimestampAsString(campagnaEdicola.getCampagnaVo().getTr2Op2Al9226(), DateUtilities.FORMATO_DATA_SLASH);
				campagnaTurno2.put("S2_Op1DataAl",(s2op1_al!=null)?s2op1_al:"");
			}
			if(campagnaEdicola.getDtconferma9227()!=null){
				this.dataConferma = DateUtilities.getTimestampAsString(campagnaEdicola.getDtconferma9227(), DateUtilities.FORMATO_DATA_SLASH);
				
				this.dataOp1Dal = (campagnaEdicola.getDtOp1Dal9227()!=null)?DateUtilities.getTimestampAsString(campagnaEdicola.getDtOp1Dal9227(), DateUtilities.FORMATO_DATA_SLASH):"";
				this.dataOp1Al = (campagnaEdicola.getDtOp1Al9227()!=null)?DateUtilities.getTimestampAsString(campagnaEdicola.getDtOp1Al9227(), DateUtilities.FORMATO_DATA_SLASH):"";
				this.dataOp2Dal = (campagnaEdicola.getDtOp2Dal9227()!=null)?DateUtilities.getTimestampAsString(campagnaEdicola.getDtOp2Dal9227(), DateUtilities.FORMATO_DATA_SLASH):"";
				this.dataOp2Al = (campagnaEdicola.getDtOp2Al9227()!=null)?DateUtilities.getTimestampAsString(campagnaEdicola.getDtOp2Al9227(), DateUtilities.FORMATO_DATA_SLASH):"";
				
				if(campagnaEdicola.getDtOp1Dal9227()!=null && campagnaEdicola.getDtOp1Al9227()!=null){
					this.totGiorniOp1 = DateUtilities.countDay(campagnaEdicola.getDtOp1Dal9227(),campagnaEdicola.getDtOp1Al9227());
				}
				if(campagnaEdicola.getDtOp2Dal9227()!=null && campagnaEdicola.getDtOp2Al9227()!=null){
					this.totGiorniOp2 = DateUtilities.countDay(campagnaEdicola.getDtOp2Dal9227(),campagnaEdicola.getDtOp2Al9227());
				}
				
			}
		}else{
			requestMap.put("stato", "2");
		}
		
		
		requestMap.put("memorizzaInvia", getText("igeriv.memorizza.invia"));
		return SUCCESS;
	}
	
	@SkipValidation
	public String showReportComunicazioni() {
		titleComunicazioniFerie = getText("comunicazioni.ferie.title");
		return "report";
	}
	
	@SkipValidation
	public String saveComunicazioni() throws ParseException, UnsupportedEncodingException, MessagingException {
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		CampagnaEdicoleVo tmpCampagnaEdicola = service.getEdicoleByCrivw(getAuthUser().getCodDpeWebEdicola());
		
		boolean invioMailRiepilogo = false;
		
		
		if(campagnaEdicola.getFlgaperto9227()==1){
			//Edicola sempre aperta
			tmpCampagnaEdicola.setDtconferma9227(new Date());
			tmpCampagnaEdicola.setFlgaperto9227(new Integer(1));
			tmpCampagnaEdicola.setTotup9227(tmpCampagnaEdicola.getTotup9227().intValue()+1);
			tmpCampagnaEdicola.setFlgstato9227(new Integer(1));
			service.updateCampagnaEdicole(tmpCampagnaEdicola);
			//Send mail - ticket:0000232
			//this.sendEmailToDL(getAuthUser().getCodEdicolaDl().toString(), "Che il mio punto vendita non effettuerà nessun periodo di ferie", "");
			
		}else if(campagnaEdicola.getFlgaperto9227()==0){
			
			String testoMail ="";
			//Edicola in ferie
			if(S1_CheckBox!=null && S1_CheckBox.equals("on")){
				tmpCampagnaEdicola.setDtOp1Dal9227(formatter.parse(S1_Op1DataDal));
				testoMail+= "Dal "+S1_Op1DataDal;
				tmpCampagnaEdicola.setDtOp1Al9227(formatter.parse(S1_Op1DataAl));
				testoMail+= " al "+S1_Op1DataAl;
				
				int countTotGiorni1 = DateUtilities.countDay(tmpCampagnaEdicola.getDtOp1Dal9227(),tmpCampagnaEdicola.getDtOp1Al9227());
				testoMail+= "   Totale giorni :  "+countTotGiorni1+"<br><br>";
				
			}else if(S2_CheckBox!=null && S2_CheckBox.equals("on")){
				tmpCampagnaEdicola.setDtOp1Dal9227(formatter.parse(S2_Op1DataDal));
				testoMail+= "Dal "+S2_Op1DataDal;
				tmpCampagnaEdicola.setDtOp1Al9227(formatter.parse(S2_Op1DataAl));
				testoMail+= " al "+S2_Op1DataAl;
				
				int countTotGiorni2 = DateUtilities.countDay(tmpCampagnaEdicola.getDtOp1Dal9227(),tmpCampagnaEdicola.getDtOp1Al9227());
				testoMail+= "   Totale giorni :  "+countTotGiorni2+"<br><br>";
				
				if(S3_CheckBox!=null && S3_CheckBox.equals("on")){
					tmpCampagnaEdicola.setDtOp2Dal9227(formatter.parse(S3_Op1DataDal));
					testoMail+= "Dal "+S3_Op1DataDal;
					tmpCampagnaEdicola.setDtOp2Al9227(formatter.parse(S3_Op1DataAl));
					testoMail+= " al "+S3_Op1DataAl;
					
					int countTotGiorni3 = DateUtilities.countDay(tmpCampagnaEdicola.getDtOp2Dal9227(),tmpCampagnaEdicola.getDtOp2Al9227());
					testoMail+= "   Totale giorni :  "+countTotGiorni3+"<br><br>";
					invioMailRiepilogo = true;
				}
			}else{
				//Errore
				this.msgSave = "Errore nel processo di Conferma ferie";
				return showFerie();
			}	
			//campagnaEdicola.setTotup9227(campagnaEdicola.getTotup9227().intValue()+1);
			tmpCampagnaEdicola.setDtconferma9227(new Date());
			tmpCampagnaEdicola.setTurno9227(campagnaEdicola.getTurno9227());
			tmpCampagnaEdicola.setTotup9227(tmpCampagnaEdicola.getTotup9227().intValue()+1);
			tmpCampagnaEdicola.setFlgstato9227(new Integer(1));
			service.updateCampagnaEdicole(tmpCampagnaEdicola);
			
			//Send mail - ticket:0000232
			if(invioMailRiepilogo){
				if(getAuthUser().getCodFiegDl() == IGerivConstants.MENTA_CODE)
					this.sendEmailToDL(getAuthUser().getCodEdicolaDl().toString(), "Che il punto vendita effettuerà il periodo di ferie come previsto dal turno", testoMail);
			}
		}else{
			//Errore
			this.msgSave = "Errore nel processo di Conferma ferie";
			return showFerie();
		}
		this.msgSave = "Conferma ferie avvenuta con successo";
		return showFerie();
	}
	
	
	private void sendEmailToDL(String oggettoMail,String titolo, String testo) throws MessagingException, UnsupportedEncodingException {
		AnagraficaAgenziaVo agenzia = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
		String[] emailAgenzia = new String[1];
		if(agenzia!=null){
			//emailAgenzia[0] = (agenzia.getEmail()!=null && !agenzia.getEmail().equals(""))?agenzia.getEmail():"";
			emailAgenzia[0] = "diffusioneclienti@adgmenta.it";
		}
		String subject = "CAMPAGNA FERIE - AGGIORNAMENTO - Edicola: "+oggettoMail;
		String message = "<br><br><i>Dichiaro</i>: " + titolo + "<br><br> " + testo;
		mailingListService.sendEmailWithAttachment(emailAgenzia, subject, message, null, true, null, false, null);
	}

	
	
	
	@Override
	public void saveParameters(Context context, String tableId, @SuppressWarnings("rawtypes") Map parameterMap) {
		
	}

	@Override
	public Map<String, String> getParameters(Context context, String tableId, String stateAttr) {
		return null;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.menu.801");
	}

	public String getS1_CheckBox() {
		return S1_CheckBox;
	}

	public void setS1_CheckBox(String s1_CheckBox) {
		S1_CheckBox = s1_CheckBox;
	}

	public String getS2_CheckBox() {
		return S2_CheckBox;
	}

	public void setS2_CheckBox(String s2_CheckBox) {
		S2_CheckBox = s2_CheckBox;
	}

	public String getS3_CheckBox() {
		return S3_CheckBox;
	}

	public void setS3_CheckBox(String s3_CheckBox) {
		S3_CheckBox = s3_CheckBox;
	}

	
}
