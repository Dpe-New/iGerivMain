package it.dpe.service.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;


/**
 * Interfaccia per l'invio di email.
 * 
 * @author romanom
 *
 */
public interface MailingListService {
	
	/**
	 * Invia un email a un indirizzo singolo. 
	 * 
	 * @param String email
	 * @param String subject
	 * @param String message
	 * @param String from
	 */
	public void sendEmail(String email, String subject, String message, String from);
	
	/**
	 * Invia un email a un array di indirizzi.
	 * 
	 * @param String[] email
	 * @param String subject
	 * @param String message
	 * @param boolean overrideExistingEmails Sovrascrivere gli email già definiti nella configurazione
	 * @param String from
	 */
	public void sendEmail(String[] email, String subject, String message, boolean appendToConfigEmails, String from);
	
	/**
	 * @param email
	 * @param subject
	 * @param message
	 * @param attachment
	 * @param isHtml
	 * @param replyTo
	 * @param appendToConfiEmails
	 * @param label
	 * @param from
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public void sendEmailWithAttachment(String[] email, String subject, String message, File attachment, Boolean isHtml, String replyTo, boolean appendToConfiEmails, String label, String from) throws MessagingException, UnsupportedEncodingException;
	
}
