package it.dpe.igeriv.job;


import it.dpe.igeriv.bo.campagna.CampagnaService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.CampagnaDto;
import it.dpe.igeriv.dto.CampagnaEdicoleDto;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.CampagnaEdicoleVo;
import it.dpe.mail.MailingListService;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;



/**
 * Classe job schedulata che controlla la liste delle edicole che non hanno ancora confermato la 
 * campagna ferie inviando un messaggio di notifica.
 * 
 * @version 0.0.1 - 17/07/2014
 */
public class IGerivControlloCampagnaFerieJob extends QuartzJobBean{

	private final int STATE_CAMPAGNA_NON_ATTIVA	= 0;
	private final int STATE_CAMPAGNA 			= 1;
	private final int STATE_SOLLECITO 			= 2;
	private final int STATE_FUORI_CAMPAGNA 		= 3;
	
	private final Integer STATE_EDICOLA_DA_CONFERMARE	= 0;
	private final Integer STATE_EDICOLA_CONFERMATA		= 1;
	private final Integer STATE_EDICOLA_FUORI_CAMPAGNA	= 2;

	private final Logger log = Logger.getLogger(getClass());
	private MailingListService mailingListService;
	private CampagnaService campagnaService;
	private MessaggiService messaggiService;
	private EdicoleService edicoleService;
	
	
	private static final String HTML_MESSAGE_TEMPLATE = "<div align=\"center\"><font color=\"#CC0000\" size=\"4\"><font size=\"4\"><b>ATTENZIONE !</b></font><br></font></div><br>"
			+ "<div align=\"center\">"
			+ "<font size=\"4\"><b><font color=\"#000099\">"
			+ " Non hai ancora confermato la scelta delle ferie.<br/>Ti preghiamo di aprire la pagina delle Comunicazioni Ferie Estive,"
			+ "<br/>inserire il proprio turno di appartenenza di ferie o selezionare <br/>di non effettuare ferie</font></b></font>"
			+ "<b><font color=\"#000099\"><font size=\"4\">"
			+ "<font size=\"4\"><b><font color=\"#000099\"> ed infine confermare cliccando il tasto <font color=\"#CC0000\">Memorizza</font> </font></b>"
			+ "<br><br>Grazie<br></font></font></font></b></div>";
	private static final String HTML_MESSAGE_TEMPLATE_SOLLECITO = "<div align=\"center\"><font color=\"#CC0000\" size=\"5\"><font size=\"6\"><b>ATTENZIONE !</b></font><br></font></div><br>"
			+ "<div align=\"center\">"
			+ "<font size=\"5\"><b><font color=\"#000099\">"
			+ " Non hai ancora confermato la scelta delle ferie.<br/>Ti preghiamo di aprire la pagina delle Comunicazioni Ferie Estive,"
			+ "<br/>inserire il proprio turno di appartenenza di ferie o selezionare <br/>di non effettuare ferie</font></b></font>"
			+ "<b><font color=\"#000099\"><font size=\"4\">"
			+ "<font size=\"5\"><b><font color=\"#000099\"> ed infine confermare cliccando il tasto <font color=\"#CC0000\">Memorizza</font> </font></b>"
			+ "<br><br>Grazie<br></font></font></font></b></div>";
	
	
	@Override
	protected void executeInternal(JobExecutionContext context)throws JobExecutionException {
		
		//List<CampagnaDto> listCampagna = campagnaService.getCampagnaByCodiceDl(new Integer("45"));
		List<CampagnaDto> listCampagna = campagnaService.getCampagnaIsActive();
		if(listCampagna!=null && listCampagna.size()>0){
			log.info("IGerivControlloCampagnaFerieJob - List Campagne : "+ listCampagna.size());
			
			for (CampagnaDto campagnaDto : listCampagna) {
				//Per ogni campagna viene verificata se si tratta di campagna o di sollecito o se siamo fuori periodo
				int statoCampagna = verificaCampagna(campagnaDto.getDtinizio9226(),campagnaDto.getDtfine9226(),campagnaDto.getDtfinesoll9226());
				
				switch (statoCampagna) {
				case 0:
					log.info("IGerivControlloCampagnaFerieJob - CAMPAGNA : "+ campagnaDto.getId9226()+" CAMPAGNA NON ATTIVA");
					break;
				case 1:
					List<CampagnaEdicoleDto> listEdicole = campagnaService.getEdicoleByIdCampagnaStato(campagnaDto.getId9226(),this.STATE_EDICOLA_DA_CONFERMARE);
					log.info("IGerivControlloCampagnaFerieJob - CAMPAGNA : "+ campagnaDto.getId9226()+" CAMPAGNA ATTIVA");
					log.info("IGerivControlloCampagnaFerieJob - LISTA EDICOLE : "+ listEdicole.size());
				
					MessaggioDto msgDto = null;
										
					for (CampagnaEdicoleDto edicola : listEdicole) {
						msgDto = new MessaggioDto();
						msgDto.setDtMessaggio(new Timestamp(new java.util.Date().getTime()));
						msgDto.setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
						msgDto.setCodFiegDl(campagnaDto.getCoddl9226());
						msgDto.setDestinatarioA(edicola.getCrivw9227());
						msgDto.setTipoMessaggio(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO);
						msgDto.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
						msgDto.setMessaggio(HTML_MESSAGE_TEMPLATE);
						
						AbbinamentoEdicolaDlVo abbEdicola = edicoleService.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(edicola.getCrivw9227());
						msgDto.setEdicolaLabel(abbEdicola.getCodEdicolaDl() + " - " + abbEdicola.getAnagraficaEdicolaVo().getRagioneSocialeEdicolaPrimaRiga() + " - " + abbEdicola.getAnagraficaEdicolaVo().getLocalitaEdicolaPrimaRiga());
						messaggiService.saveMessaggioRivenditaDpe(msgDto);
					}
					
					break;
				case 2:
					List<CampagnaEdicoleDto> listEdicoleSoll = campagnaService.getEdicoleByIdCampagnaStato(campagnaDto.getId9226(),this.STATE_EDICOLA_DA_CONFERMARE);
					log.info("IGerivControlloCampagnaFerieJob - CAMPAGNA : "+ campagnaDto.getId9226()+" CAMPAGNA ATTIVA");
					log.info("IGerivControlloCampagnaFerieJob - LISTA EDICOLE SOLLECITO: "+ listEdicoleSoll.size());
				
					MessaggioDto msgDtoSoll = null;
					
					for (CampagnaEdicoleDto edicola : listEdicoleSoll) {
						msgDtoSoll = new MessaggioDto();
						msgDtoSoll.setDtMessaggio(new Timestamp(new java.util.Date().getTime()));
						msgDtoSoll.setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
						msgDtoSoll.setCodFiegDl(campagnaDto.getCoddl9226());
						msgDtoSoll.setDestinatarioA(edicola.getCrivw9227());
						msgDtoSoll.setTipoMessaggio(IGerivConstants.TIPO_MESSAGGIO_UREGENTISSIMO);
						msgDtoSoll.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
						msgDtoSoll.setMessaggio(HTML_MESSAGE_TEMPLATE_SOLLECITO);
						
						AbbinamentoEdicolaDlVo abbEdicola = edicoleService.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(edicola.getCrivw9227());
						msgDtoSoll.setEdicolaLabel(abbEdicola.getCodEdicolaDl() + " - " + abbEdicola.getAnagraficaEdicolaVo().getRagioneSocialeEdicolaPrimaRiga() + " - " + abbEdicola.getAnagraficaEdicolaVo().getLocalitaEdicolaPrimaRiga());
						messaggiService.saveMessaggioRivenditaDpe(msgDtoSoll);
					}
						
					break;
				case 3:
					List<CampagnaEdicoleDto> listEdicoleOut = campagnaService.getEdicoleByIdCampagnaStato(campagnaDto.getId9226(),this.STATE_EDICOLA_DA_CONFERMARE);
					log.info("IGerivControlloCampagnaFerieJob - CAMPAGNA : "+ campagnaDto.getId9226()+" CAMPAGNA ATTIVA");
					log.info("IGerivControlloCampagnaFerieJob - LISTA EDICOLE FUORI CAMPAGNA: "+ listEdicoleOut.size());
				
					for (CampagnaEdicoleDto edicola : listEdicoleOut) {
						CampagnaEdicoleVo campEdicola =   campagnaService.getEdicoleByCrivw(edicola.getCrivw9227());
						campEdicola.setFlgstato9227(this.STATE_EDICOLA_FUORI_CAMPAGNA);
						campagnaService.updateCampagnaEdicole(campEdicola);
					}
					break;
				default:
					break;
				}
			}

		}else{
			//E-Mail per avvertire che per non ci sono edicole in campagna ferie per il CODDL lavorato
		}
		
	}

	
	private int verificaCampagna(Date dtInizioCamp, Date dtFineCampagna, Date dtFineSollecito){
		int resState = 0;
		
		Date sysDate = new Date(System.currentTimeMillis());
		if(sysDate.before(dtInizioCamp) && !sysDate.equals(dtInizioCamp)){
			resState = this.STATE_CAMPAGNA_NON_ATTIVA;
		}else if(sysDate.equals(dtInizioCamp) || sysDate.after(dtInizioCamp) && sysDate.equals(dtFineCampagna) || sysDate.before(dtFineCampagna)){
			resState = this.STATE_CAMPAGNA;
		}else if(sysDate.after(dtInizioCamp) && sysDate.after(dtFineCampagna) && sysDate.before(dtFineSollecito) || sysDate.equals(dtFineSollecito)){
			resState = this.STATE_SOLLECITO;
		}else if(sysDate.after(dtFineSollecito)){
			resState = this.STATE_FUORI_CAMPAGNA;
		}
		
		return resState;
	}


	public CampagnaService getCampagnaService() {
		return campagnaService;
	}

	public void setCampagnaService(CampagnaService campagnaService) {
		this.campagnaService = campagnaService;
	}

	public MessaggiService getMessaggiService() {
		return messaggiService;
	}

	public void setMessaggiService(MessaggiService messaggiService) {
		this.messaggiService = messaggiService;
	}

	public EdicoleService getEdicoleService() {
		return edicoleService;
	}

	public void setEdicoleService(EdicoleService edicoleService) {
		this.edicoleService = edicoleService;
	}
	
	
	
	
	
}
