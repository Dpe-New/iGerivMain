package it.dpe.igeriv.job;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.greaterThan;
import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.dto.BollaRiassuntoDto;
import it.dpe.igeriv.exception.ConstraintViolationException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BollaRiassunto;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import it.dpe.mail.MailingListService;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.common.base.Strings;

/**
 * Classe job schedulata che estrae le bolle memorizzate ma non inviate del giorno prima
 * e invia un messaggio immediato a ciscuna edicola
 * 
 * @author romanom
 */
@Getter
@Setter
public class IGerivControlloBolleInviateJob extends QuartzJobBean {
	private static final String HTML_MESSAGE_TEMPLATE = "<div align=\"center\"><font color=\"#CC0000\" size=\"5\"><font size=\"6\"><b>ATTENZIONE !</b></font><br></font></div><br><div align=\"center\"><font size=\"5\"><b><font color=\"#000099\">Non hai ancora inviato la {0} del giorno {1}.<br>Ti preghiamo di aprire la pagina della bolla, verificare i dati</font></b></font><b><font color=\"#000099\"><font size=\"4\"><font size=\"5\"><b><font color=\"#000099\"> inseriti e infine cliccare </font></b>sul bottone \"<font color=\"#CC0000\">Memorizza e Invia</font>\"<br><br>Grazie<br></font></font></font></b></div>";
	private final Logger log = Logger.getLogger(getClass());
	private Integer timeout;
	private BolleService bolleService;
	private MailingListService mailingListService;
	private DateFormat formatter = new SimpleDateFormat(DateUtilities.FORMATO_DATA_SLASH + ", " + DateUtilities.FORMATO_GIORNO_SETTIMANA);
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			Timestamp sysdate = bolleService.getSysdate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(sysdate);
			int day = cal.get(Calendar.DAY_OF_WEEK);
			Timestamp[] dateBolla = (day == Calendar.MONDAY ? new Timestamp[]{DateUtilities.floorDay(DateUtilities.togliGiorni(sysdate, 2)), DateUtilities.floorDay(DateUtilities.togliGiorni(sysdate, 1))} : new Timestamp[]{DateUtilities.floorDay(DateUtilities.togliGiorni(sysdate, 1))});
			List<BollaRiassuntoDto> bolleR = select(bolleService.getBolleResaRiassuntoNonInviate(dateBolla, "A", IGerivConstants.COD_FIEG_MENTA), having(on(BollaRiassuntoDto.class).getTotaleCopieBollaResaDettaglio(), greaterThan(0)));
			
			String nameBollaR = IGerivMessageBundle.get("igeriv.bolla.resa");
			for (BollaRiassuntoDto bolla : bolleR) {
			
				if(!bolla.isEdicolaInFerie())
				{
					MessaggioVo vo = buildMessaggio(bolla, nameBollaR);
					bolleService.saveBaseVo(vo);
				}
			
			}
		} 
		catch(org.hibernate.exception.ConstraintViolationException constraintViolationException )
		{
			//catch senza fare nulla, perchè il batch potrebbe girare più volte e tentare di inserire un messaggio già salvato
		}		
		catch (Throwable e) {
			String subject = MessageFormat.format(IGerivMessageBundle.get("igeriv.email.errore.batch.subject"), "IGerivControlloBolleInviateJob");
			log.error(subject, e);
			try {
				String emailMsg = MessageFormat.format(IGerivMessageBundle.get("igeriv.email.errore.batch.body"), "IGerivControlloBolleInviateJob", (Strings.isNullOrEmpty(e.getMessage()) ? "" : e.getMessage()), StringUtility.getStackTrace(e)); 
				mailingListService.sendEmailWithAttachment(null, subject, emailMsg, null, true, null, true, null);
			} catch (Throwable e1) {
    			log.error(IGerivMessageBundle.get("msg.email.dpe.errore.invio.mail"), e1);
    		}
		}
	}

	/**
	 * @param bolla
	 * @param dtBollaFmt 
	 * @param nameBolla 
	 * @return
	 */
	private MessaggioVo buildMessaggio(BollaRiassunto bolla, String nameBolla) {
		MessaggioVo vo = new MessaggioVo();
		MessaggioPk pk = new MessaggioPk();
		pk.setCodFiegDl(bolla.getCodFiegDl());
		pk.setDestinatarioA(bolla.getCodEdicola());
		pk.setDestinatarioB(0);
		pk.setDtMessaggio(new Timestamp(new Date().getTime()));
		pk.setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
		vo.setPk(pk);
		String dtBollaFmt = formatter.format(bolla.getDtBolla());
		String messaggio = MessageFormat.format(HTML_MESSAGE_TEMPLATE, nameBolla, dtBollaFmt);
		vo.setMessaggio(messaggio);
		vo.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
		vo.setTipoMessaggio(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO);
		return vo;
	}

}
