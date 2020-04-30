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
	 */
	public void sendEmail(String email, String subject, String message);
	
	/**
	 * Invia un email a un array di indirizzi sovrascrivendo quelli già definiti nella configurazione.
	 * 
	 * @param String[] email
	 * @param String subject
	 * @param String message
	 */
	public void sendEmail(String[] email, String subject, String message);
	
	/**
	 * Invia un email con allegato a un array di indirizzi sovrascrivendo quelli già definiti nella configurazione.
	 * 
	 * @param String[] email
	 * @param String subject
	 * @param String message
	 * @param File attachment
	 */
	public void sendEmailWithAttachment(String[] email, String subject, String message, File attachment, Boolean isHtml, String replyTo, boolean appendToConfiEmails, String label) throws MessagingException, UnsupportedEncodingException;
	
	/**
	 * Invia un email a un array di indirizzi.
	 * 
	 * @param String[] email
	 * @param String subject
	 * @param String message
	 * @param boolean overrideExistingEmails Sovrascrivere gli email già definiti nella configurazione
	 */
	public void sendEmail(String[] email, String subject, String message, boolean appendToConfigEmails);
	
	/**
	 * Invia un email all'indirizzo definito nella configurazione.
	 * 
	 * @param String subject
	 * @param String message
	 */
	public void sendEmail(String subject, String message);
	
}
