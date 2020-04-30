package it.dpe.igeriv.importer;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Questa classe è configurata nel file integrationContext.xml
 * Riceve email dalla casella di posta dl.edicole@gmail.com dove i dl che hanno questo servzio
 * inviano gli email di risposta alle richieste delle edicole. 
 * Gli email di questa casella vengono trasformati in messaggi istantanei con priorità alta per ciascuna edicola.
 * 
 * @author romanom
 *
 */
@Component("emailReturnReceiptReceiver")
public class EmailReturnReceiptReceiver { 
	@Autowired
	private IGerivBatchService iGerivBo;
	@Autowired
	private MailingListService mailingListService;
	
	public void receive(javax.mail.internet.MimeMessage mimeMessage) {
		try { 
			String subject = mimeMessage.getSubject();
			if (subject != null && !Strings.isNullOrEmpty(subject) && subject.contains("<") && subject.contains(">")) {
				Integer idEmailRivendita = new Integer(subject.substring(subject.indexOf("<") + 1, subject.indexOf(">")).trim());
				EmailRivenditaVo vo = iGerivBo.getEmailRivenditaVo(idEmailRivendita);
				vo.setLetto(true);
				iGerivBo.saveBaseVo(vo);
			}
		} catch (Exception e) {
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.invio.email.dl.edicole.subject"), 
					MessageFormat.format(IGerivMessageBundle.get("msg.subject.errore.invio.email.dl.edicole"), new Object[]{StringUtility.getStackTrace(e)}), true);
		}
	}

	public IGerivBatchService getiGerivBo() {
		return iGerivBo;
	}

	public void setiGerivBo(IGerivBatchService iGerivBo) {
		this.iGerivBo = iGerivBo;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}
	
}
