package it.dpe.service.mail.impl;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.service.mail.MailingListService;

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
	private final String deployName;
	
	@Autowired
	MailingListServiceImpl(JavaMailSender javaMailSender, @Value("${server.email.address}") String serverEmail,@Value("${igeriv.env.deploy.name}") String deployName) {
		this.javaMailSender = javaMailSender;
		this.serverEmail = serverEmail;
		this.deployName = deployName;
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
		
		//String deployName = SpringContextManager.getSpringContext().getBean(ExposablePropertyPaceholderConfigurer.class).getResolvedProps().get("igeriv.env.deploy.name");
		
		igerivMessage.setSubject(deployName+" - "+subject);
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
		//sendEmailWithAttachments(email, subject, message, attachments, isHtml, replyTo, appendToConfiEmails, label, null);
		Date now = new Date();
		SimpleMailMessage igerivMessage = SpringContextManager.getSpringContext().getBean("IGerivMessage", SimpleMailMessage.class);
		buildSentToAddress(email, appendToConfiEmails, igerivMessage);
		sendWithAttachment(subject, message, now, igerivMessage, attachments, isHtml, replyTo, label, returnReceiptToAddress, null);
	
	
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
	private void sendWithAttachment(final String subject, final String message, final Date now, final SimpleMailMessage plgMessage1, 
			final File[] attachment, final Boolean isHtml, final String replyTo, final String label, final String returnReceiptToAddress, 
			final String fromLabel) throws MessagingException, UnsupportedEncodingException {
		MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws MessagingException, UnsupportedEncodingException {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, (attachment != null) ? true : false, Charsets.UTF_8.name());
				helper.setTo(plgMessage1.getTo());
				helper.setFrom(plgMessage1.getFrom());
				if (!Strings.isNullOrEmpty(fromLabel)) {
					helper.setFrom(new InternetAddress(serverEmail, fromLabel));
				}
				if (!Strings.isNullOrEmpty(replyTo)) {
					helper.setReplyTo(new InternetAddress(replyTo, !Strings.isNullOrEmpty(label) ? label : null, Charsets.UTF_8.name()));
				}
				if (!Strings.isNullOrEmpty(returnReceiptToAddress)) {
					mimeMessage.addHeader("Disposition-Notification-To", returnReceiptToAddress);
				}
				helper.setSubject(subject);
				helper.setText(message, isHtml);
				helper.setSentDate(now);
				if (attachment != null) {
					for (File f : attachment) {
						helper.addAttachment(new FileSystemResource(f).getFilename(), f);
					}
				}
			}
		};
		
		javaMailSender.send(mimeMessagePreparator);
	}
	
	
	
}
