package it.dpe.mail.impl;

import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.mail.MailingListService;

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
import org.springframework.beans.factory.annotation.Value;
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
	private final JavaMailSender javaMailSender;
	private final String serverEmail;
	
	@Autowired
	MailingListServiceImpl(JavaMailSender javaMailSender, @Value("${server.email.address}") String serverEmail) {
		this.javaMailSender = javaMailSender;
		this.serverEmail = serverEmail;
	}
	
	@Override
	public void sendEmail(String email, String subject, String message) {
		sendEmail(new String[]{email}, subject, message);
	}
	
	@Override
	public void sendEmail(String[] email, String subject, String message) {
		sendEmail(email, subject, message, false);
	}
	
	@Override
	public void sendEmail(String subject, String message) {
		Date now = new Date();
		SimpleMailMessage igerivMessage = SpringContextManager.getSpringContext().getBean("IGerivMessage", SimpleMailMessage.class);
		igerivMessage.setSubject(subject);
		send(message, now, igerivMessage);
	}
	
	@Override
	public void sendEmail(String[] email, String subject, String message, boolean appendToConfigEmails) {
		Date now = new Date();
		SimpleMailMessage igerivMessage = SpringContextManager.getSpringContext().getBean("IGerivMessage", SimpleMailMessage.class);
		buildSentToAddress(email, appendToConfigEmails, igerivMessage);
		igerivMessage.setSubject(subject);
		send(message, now, igerivMessage);
	}

	private void send(String message, Date now, SimpleMailMessage plgMessage1) {
		plgMessage1.setSentDate(now);
		plgMessage1.setText(message);
		javaMailSender.send(plgMessage1);
	}
	
	@Override
	public void sendEmailWithAttachment(String[] email, String subject, String message, File attachment, Boolean isHtml, String replyTo, boolean appendToConfiEmails, String label) throws MessagingException, UnsupportedEncodingException {
		sendEmailWithAttachment(email, subject, message, attachment, isHtml, replyTo, appendToConfiEmails, label, null, null);
	}
	
	@Override
	public void sendEmailWithAttachment(String[] email, String subject, String message, File attachment, Boolean isHtml, String replyTo, boolean appendToConfiEmails, String label, String returnReceiptToAddress, String fromLabel) throws MessagingException, UnsupportedEncodingException {
		Date now = new Date();
		SimpleMailMessage igerivMessage = SpringContextManager.getSpringContext().getBean("IGerivMessage", SimpleMailMessage.class);
		buildSentToAddress(email, appendToConfiEmails, igerivMessage);
		sendWithAttachment(subject, message, now, igerivMessage, attachment != null ? new File[]{attachment} : null, isHtml, replyTo, label, returnReceiptToAddress, fromLabel);
	}
	
	@Override
	public void sendEmailWithAttachments(String[] email, String subject, String message, File[] attachments, Boolean isHtml, String replyTo, boolean appendToConfiEmails, String label, String returnReceiptToAddress) throws MessagingException, UnsupportedEncodingException {
		sendEmailWithAttachments(email, subject, message, attachments, isHtml, replyTo, appendToConfiEmails, label, null);
	}
	
	@Override
	public void sendEmailWithAttachments(String[] email, String subject, String message, File[] attachments, Boolean isHtml, String replyTo, boolean appendToConfiEmails, String label, String returnReceiptToAddress, String fromLabel)
			throws MessagingException, UnsupportedEncodingException {
		Date now = new Date();
		SimpleMailMessage igerivMessage = SpringContextManager.getSpringContext().getBean("IGerivMessage", SimpleMailMessage.class);
		buildSentToAddress(email, appendToConfiEmails, igerivMessage);
		sendWithAttachment(subject, message, now, igerivMessage, attachments, isHtml, replyTo, label, returnReceiptToAddress, fromLabel);
	}
	
	private void buildSentToAddress(String[] email, boolean appendToConfigEmails, SimpleMailMessage igerivMessage) {
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
	 * @param fromLabel 
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private void sendWithAttachment(String subject, String message, Date now, SimpleMailMessage plgMessage1, File[] attachment, Boolean isHtml, String replyTo, String label, String returnReceiptToAddress, String fromLabel) throws MessagingException, UnsupportedEncodingException {
		MimeMessage createMimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(createMimeMessage, (attachment != null) ? true : false);
		helper.setTo(plgMessage1.getTo());
		helper.setFrom(plgMessage1.getFrom());
		if (!Strings.isNullOrEmpty(fromLabel)) {
			createMimeMessage.setFrom(new InternetAddress(serverEmail, fromLabel));
		}
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
		if (!Strings.isNullOrEmpty(returnReceiptToAddress)) {
			createMimeMessage.addHeader("Disposition-Notification-To", returnReceiptToAddress);
		}
		createMimeMessage.setSubject(subject);
		helper.setText(message, isHtml);
		helper.setSentDate(now);
		if (attachment != null) {
			for (File f : attachment) {
				FileSystemResource file = new FileSystemResource(f);
				helper.addAttachment(file.getFilename(), f);
			}
		}
		javaMailSender.send(createMimeMessage);
	}
	
}
