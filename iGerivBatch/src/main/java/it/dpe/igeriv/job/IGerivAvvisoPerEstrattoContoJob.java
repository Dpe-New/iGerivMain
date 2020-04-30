package it.dpe.igeriv.job;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.google.common.base.Strings;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import it.dpe.mail.MailingListService;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe job schedulata per inoltrare agli edicolanti un messaggio di avviso 
 * 
 * MSG inviato :
 * ATTENZIONE! Si prega di verificare che tutte le bolle di resa 
 * e di consegna della settimana siano state trasmesse regolarmente, 
 * onde evitare problemi con il prossimo estratto conto. GRAZIE
 * @author romanom
 */
@Getter
@Setter
public class IGerivAvvisoPerEstrattoContoJob extends QuartzJobBean {
	
	private final Logger log = Logger.getLogger(getClass());
	private static final String HTML_MESSAGE_TEMPLATE = "<div align=\"center\"><font color=\"#CC0000\" size=\"5\"><font size=\"6\"><b>ATTENZIONE !</b></font><br></font></div><br><div align=\"center\"><font size=\"5\"><b><font color=\"#000099\">Si prega di verificare che tutte le bolle di resa e di consegna della settimana siano state trasmesse regolarmente, onde evitare problemi con il prossimo estratto conto.</font></b></font><b><font color=\"#000099\"><font size=\"4\"><br><br><br>Grazie<br></font></font></font></b></div>";
	private EdicoleService edicoleService;
	private MailingListService mailingListService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)throws JobExecutionException {
		
		
			log.info("IGerivAvvisoPerEstrattoContoJob.executeInternal - START");
			String pkInLavorazione = null;
			
			List<EdicolaDto> listEdicoleAttive = edicoleService.getEdicoleDlAttive(IGerivConstants.COD_FIEG_DL_CDL);
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7);
			log.info("Date = "+ cal.getTime());
			
			if(listEdicoleAttive!=null){
				for(EdicolaDto iterEdicola : listEdicoleAttive){
					if(iterEdicola.getCodEdicolaWeb().equals(new Integer("1240"))){
						try{
							MessaggioVo vo = buildMessaggio(iterEdicola);
							pkInLavorazione =  vo.getPk().toString();
							log.info("Invio messaggio edicola : "+pkInLavorazione);
							edicoleService.saveBaseVo(vo);
							Thread.sleep(1000);
						}catch(org.hibernate.exception.ConstraintViolationException constraintViolationException ){
							//catch senza fare nulla, perchè il batch potrebbe girare più volte e tentare di inserire un messaggio già salvato
						}catch (Throwable e) {
							String subject = MessageFormat.format(IGerivMessageBundle.get("igeriv.email.errore.batch.subject"), "IGerivAvvisoPerEstrattoContoJob");
							log.error(subject, e);
							try {
								String emailMsg = MessageFormat.format(IGerivMessageBundle.get("igeriv.email.errore.batch.body"), "IGerivAvvisoPerEstrattoContoJob", (Strings.isNullOrEmpty(e.getMessage()) ? "" : e.getMessage()), StringUtility.getStackTrace(e)); 
								// Inserito per qualificare la tipologia di msg che genera errori di SQLIntegrityConstraintViolationException 20/10/2015
								emailMsg += " PK IN LAVORAZIONE: "+pkInLavorazione;
								mailingListService.sendEmailWithAttachment(null, subject, emailMsg, null, true, null, true, null);
							} catch (Throwable e1) {
				    			log.error(IGerivMessageBundle.get("msg.email.dpe.errore.invio.mail"), e1);
				    		}
						}
		
					}
					
				}
			}	
			log.info("IGerivAvvisoPerEstrattoContoJob.executeInternal - END");			
	}
	
	

	/**
	 * @param EdicolaDto edicola
	 * @return MessaggioVo
	 */
	private MessaggioVo buildMessaggio(EdicolaDto edicola) {
		MessaggioVo vo = new MessaggioVo();
		MessaggioPk pk = new MessaggioPk();
		pk.setCodFiegDl(edicola.getCodDl());
		pk.setDestinatarioA(edicola.getCodEdicolaWeb());
		pk.setDestinatarioB(0);
		pk.setDtMessaggio(new Timestamp(new Date().getTime()));
		pk.setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
		vo.setPk(pk);
		vo.setMessaggio(HTML_MESSAGE_TEMPLATE);
		vo.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
		vo.setTipoMessaggio(IGerivConstants.TIPO_MESSAGGIO_UREGENTISSIMO);
		// Inserito per qualificare la tipologia di msg il 21/01/2016 
		vo.setCategoria(IGerivConstants.CATEGORIA_MESSAGGIO_IGERIV_BATCH_AVVISO_PRE_ESTRATTO_CONTO);
		return vo;
	}

}
