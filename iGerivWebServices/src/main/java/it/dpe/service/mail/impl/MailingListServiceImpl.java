package it.dpe.service.mail.impl;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.service.mail.MailingListService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe che implementa la logica per l'invio di emails.
 * Utilizza la classe Spring JavaMailSender.
 * 
 * @author romanom
 *
 */
@Component("MailingListService")
public class MailingListServiceImpl implements MailingListService {
	private final Logger log = Logger.getLogger(getClass());
	private final JavaMailSender javaMailSender;
	
	@Autowired
	MailingListServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	@Override
	public void sendEmail(String email, String subject, String message, String from) {
		sendEmail(new String[]{email}, subject, message, false, from);
	}
	
	@Override
	public void sendEmail(String[] email, String subject, String message, boolean appendToConfigEmails, String from) {
		SimpleMailMessage rtaeMessage = SpringContextManager.getSpringContext().getBean("IGerivMessage", SimpleMailMessage.class);
		if (!Strings.isNullOrEmpty(from)) {
			rtaeMessage.setFrom(from);
		}
		rtaeMessage.setSubject(subject);
		setToAddress(email, appendToConfigEmails, rtaeMessage);
		send(message, new Date(), rtaeMessage);
	}
	
	@Override
	public void sendEmailWithAttachment(String[] email, String subject, String message, File attachment, Boolean isHtml, String replyTo, boolean appendToConfiEmails, String label, String from) throws MessagingException, UnsupportedEncodingException {
		SimpleMailMessage rtaeMessage = SpringContextManager.getSpringContext().getBean("IGerivMessage", SimpleMailMessage.class);
		if (!Strings.isNullOrEmpty(from)) {
			rtaeMessage.setFrom(from);
		}
		setToAddress(email, appendToConfiEmails, rtaeMessage);
		sendWithAttachment(subject, message, new Date(), rtaeMessage, attachment, isHtml, replyTo, label);
	}
	
	private void send(String message, Date now, SimpleMailMessage plgMessage1) {
		plgMessage1.setSentDate(now);
		plgMessage1.setText(message);
		try {
			javaMailSender.send(plgMessage1);
		} catch (Throwable e) {
			log.error(IGerivMessageBundle.get("rtae.mail.email.error.subject"), e);
		}
	}
	
	private void setToAddress(String[] email, boolean appendToConfigEmails, SimpleMailMessage rtaeMessage) {
		String[] toOrig = rtaeMessage.getTo();
		List<String> to = new ArrayList<String>();
		if (appendToConfigEmails) {
			to = new ArrayList<String>(Arrays.asList(toOrig));
		}
		if (email != null && email.length > 0) {
			List<String> asList = new ArrayList<String>();
			for (String e : email) {
				if (e != null && !e.trim().equals("") && !e.trim().equals(";")) {
					if (!to.contains(e)) {
						asList.add(e);
					}
				}
			}
			to.addAll(asList);
		}
		rtaeMessage.setTo(to.toArray(new String[0]));
	}	
	
	/**
	 * @param subject
	 * @param message
	 * @param now
	 * @param plgMessage1
	 * @param attachment
	 * @param isHtml
	 * @param replyTo
	 * @param label
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private void sendWithAttachment(String subject, String message, Date now, SimpleMailMessage plgMessage1, File attachment, Boolean isHtml, String replyTo, String label) throws MessagingException, UnsupportedEncodingException {
		MimeMessage createMimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(createMimeMessage, (attachment != null) ? true : false);
		helper.setTo(plgMessage1.getTo());
		helper.setFrom(plgMessage1.getFrom());
		if (replyTo != null) {
			if (label != null) {
				InternetAddress rt = new InternetAddress();
				rt.setPersonal(label);
				rt.setAddress(replyTo);
				helper.setReplyTo(rt);
			} else {
				helper.setReplyTo(replyTo);
			}
		}
		createMimeMessage.setSubject(subject);
		helper.setText(message, isHtml);
		helper.setSentDate(now);
		if (attachment != null) {
			FileSystemResource file = new FileSystemResource(attachment);
			helper.addAttachment(file.getFilename(), file);
		}
		try {
			javaMailSender.send(createMimeMessage);
		} catch (Throwable e) {
			log.error(IGerivMessageBundle.get("rtae.mail.email.error.subject"), e);
		}
	}
	
}
