package it.dpe.igeriv.cleaner.mail.impl;

import it.dpe.igeriv.cleaner.mail.MailingListService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Classe che implementa la logica per l'invio di emails.
 * Utilizza la classe Spring JavaMailSender.
 * 
 * @author romanom
 *
 */
@Scope("prototype")
@Component("MailingListService")
public class MailingListServiceImpl implements MailingListService {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private SimpleMailMessage igerivMessage;
	
	@Override
	public synchronized void sendEmail(String email, String subject, String message) {
		sendEmail(new String[]{email}, subject, message);
	}
	
	@Override
	public synchronized void sendEmail(String[] email, String subject, String message) {
		sendEmail(email, subject, message, false);
	}
	
	@Override
	public synchronized void sendEmail(String subject, String message) {
		Date now = new Date();
		igerivMessage.setSubject(subject);
		send(message, now, igerivMessage);
	}
	
	@Override
	public synchronized void sendEmail(String[] email, String subject, String message, boolean appendToConfigEmails) {
		String[] toOrig = buildSentToAddress(email, appendToConfigEmails);
		sendEmail(subject, message);
		igerivMessage.setTo(toOrig);
		
	}

	private void send(String message, Date now, SimpleMailMessage plgMessage1) {
		plgMessage1.setSentDate(now);
		plgMessage1.setText(message);
		javaMailSender.send(plgMessage1);
	}
	
	@Override
	public synchronized void sendEmailWithAttachment(String[] email, String subject, String message, File attachment, Boolean isHtml, String replyTo, boolean appendToConfiEmails, String label) throws MessagingException, UnsupportedEncodingException {
		Date now = new Date();
		String[] toOrig = buildSentToAddress(email, appendToConfiEmails);
		sendWithAttachment(subject, message, now, igerivMessage, attachment, isHtml, replyTo, label);
		igerivMessage.setTo(toOrig);
	}
	
	private String[] buildSentToAddress(String[] email, boolean appendToConfigEmails) {
		String[] toOrig = igerivMessage.getTo();
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
		igerivMessage.setTo(to.toArray(new String[0]));
		return toOrig;
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
		javaMailSender.send(createMimeMessage);
	}
	
	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public SimpleMailMessage getIgerivMessage() {
		return igerivMessage;
	}

	public void setIgerivMessage(SimpleMailMessage igerivMessage) {
		this.igerivMessage = igerivMessage;
	}
	
}
