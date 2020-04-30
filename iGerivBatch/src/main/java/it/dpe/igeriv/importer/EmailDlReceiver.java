package it.dpe.igeriv.importer;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.MimeMessageContentNotSupportedException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
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
@Component("emailDlReceiver")
public class EmailDlReceiver { 
	
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private IGerivBatchService iGerivBo;
	
	@Autowired
	private MailingListService mailingListService;
	
	@Value("${upload.path.dir}")
	private String uploadPathDir;
	
	@Value("${igeriv.env.deploy.name}")
	private String env;
	
	public void receive(javax.mail.internet.MimeMessage mimeMessage) {
		log.info("Received message: " + mimeMessage.toString());
		try { 
			String subject = mimeMessage.getSubject();
			log.info("Message subject: " + subject);
			if (subject != null && !Strings.isNullOrEmpty(subject) && subject.contains("<") && subject.contains(">") && StringUtils.countMatches(subject, "<") == 2 && StringUtils.countMatches(subject, ">") == 2) {
				List<File> attachments = new ArrayList<File>();
				String[] split = subject.substring(StringUtils.ordinalIndexOf(subject, "<", 2) + 1, StringUtils.ordinalIndexOf(subject, ">", 2)).split("\\|");
				if (NumberUtils.isNumber(split[0].trim()) && NumberUtils.isNumber(split[1].trim())) {
					Object content = mimeMessage.getContent();
					Integer codiceDl = new Integer(split[0].trim());
					Integer codWebEdicola = new Integer(split[1].trim());
					String body = null;
					if (content instanceof InputStream) {
						log.info("content type InputStream");
						InputStream is = (InputStream) content;
						StringWriter writer = new StringWriter();
						IOUtils.copy(is, writer, Charsets.UTF_8.name());
						body = writer.toString();
					} else if (content instanceof String) {
						log.info("content type String");
						body = content.toString();
					} else if (content instanceof MimeMultipart) {
						log.info("content type MimeMultipart");
						MimeMultipart mimeMultipart = (MimeMultipart) content;
						manageAttachments(attachments, codiceDl, mimeMultipart);  
						for (int i = 0; i < mimeMultipart.getCount(); i++) {
							BodyPart bodyPart = mimeMultipart.getBodyPart(i);
							log.info("is null "+ bodyPart != null + " " + mimeMessage);
							if(bodyPart != null) {
								log.info("is content null "+ bodyPart.getContentType()  + " " + mimeMessage);
							}
							if (bodyPart != null && bodyPart.getContentType() != null) {
								if (bodyPart.getContentType().contains("text/html") || bodyPart.getContentType().contains("text/plain")) {
									body = bodyPart.getContent().toString();
									log.info("body "+ body  + " " + mimeMessage);
									break;
								} else {
									MimeMultipart a =(MimeMultipart)bodyPart.getContent();
									BodyPart bodyPart1 = a.getBodyPart(0);
									if (bodyPart1 != null && (bodyPart1.getContentType().contains("text/html") || bodyPart1.getContentType().contains("text/plain"))) {
										body = bodyPart1.getContent().toString();
										break;
									}
								}
							}
						}
					} else {
						throw new MimeMessageContentNotSupportedException(MessageFormat.format(IGerivMessageBundle.get("msg.errore.invio.email.dl.edicole.content.not.supported"), content.toString()));
					}
					
					if (!Strings.isNullOrEmpty(body)) {
						if (!Strings.isNullOrEmpty(mimeMessage.getContentType())) {
							String linkTextConfermaLettura = IGerivMessageBundle.get("msg.email.send.read.receipt.link.body");
							if (body.contains(linkTextConfermaLettura)) {
								Document parse = Jsoup.parse(body);
								Elements elementsByTag = parse.getElementsByTag("a");
								boolean hasLink = false;
								for (Iterator<Element> it = elementsByTag.iterator(); it.hasNext();) {
									Element el = it.next();
									if (el.html().contains(linkTextConfermaLettura)) {
										el.remove();
										hasLink = true;
										break;
									}
								}
								if (hasLink) {
									body = parse.html();
								} else if (body.contains(linkTextConfermaLettura)) {
									body = body.substring(0, body.indexOf(linkTextConfermaLettura));
								}
							}
							if (mimeMessage.getContentType().contains("text/plain")) {
								log.info("content type is text/plain");
								log.info("body before splitMessage=" + body);
								body = splitMessage(body);
							} else if (content instanceof MimeMultipart) {
								String text = Jsoup.parse(body).text().replaceAll(" ", "").trim();
								String trim = body.replaceAll("\r\n", "").replaceAll(" ", "").trim();
								log.info("Jsoup.parse(body)=" + text);
								log.info("body test compare=" + trim);
								boolean isHtml = !text.equals(trim);
								if (isHtml) {
									log.info("content type is MimeMultipart and body is isHtml");
									log.info("body before escaping=" + body);
									body = StringUtility.escapeHTML(body, false);
								} else {
									log.info("content type is MimeMultipart and body is text");
									log.info("body before splitMessage=" + body);
									body = splitMessage(body);
								}
							}
						}
						log.info("body=" + body);
						MessaggioVo vo = buildMessaggio(codiceDl, codWebEdicola, body, attachments);
						byte[] bytes = null;
						if (body.length() >= 4000) {
							log.info("Message with blob text");
							bytes = body.getBytes();
						}
						log.info("Saving message: " + vo);
						iGerivBo.saveMessaggioWithBlob(vo, bytes);
					}
				}
			}
		} catch (MimeMessageContentNotSupportedException e) {
			log.error("Errore di Content Not Supported in EmailDlReceiver.receive()", e);
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.invio.email.dl.edicole.subject"), 
					MessageFormat.format(IGerivMessageBundle.get("msg.subject.errore.invio.email.dl.edicole"), new Object[]{e.getMessage()}), true);
		} catch (Exception e) {
			log.error("Errore imprevisto in EmailDlReceiver.receive()", e);
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.invio.email.dl.edicole.subject"), 
					MessageFormat.format(IGerivMessageBundle.get("msg.subject.errore.invio.email.dl.edicole"), new Object[]{StringUtility.getStackTrace(e)}), true);
		}
	}

	/**
	 * Costruisce una stringa html dividendo la parte superiore del messaggio dal messaggio precedente 
	 * 
	 * @param body
	 * @return
	 */
	private String splitMessage(String body) {
		Map<String, String> map = new HashMap<String, String>();
		map = buildBodyReplyMap(body, "\n");
		body = addPreviousMsg(body, map);
		return body;
	}

	/**
	 * @param String body
	 * @param Map<String, String> map
	 * @return String
	 */
	private String addPreviousMsg(String body, Map<String, String> map) {
		if (map != null && !map.isEmpty() && map.get("prevMsg") != null && !Strings.isNullOrEmpty(body)) {
			body = map.get("body") + "<br><br><hr><br><br>" 
				+ IGerivMessageBundle.get("igeriv.messsaggio.originale")
				+ "<br><br>" 
				+ "<div class=\"messaggioOriginaleDiv\">" + map.get("prevMsg") + "</div>";
		}
		return body;
	}

	/**
	 * @param attachments
	 * @param codiceDl
	 * @param mimeMultipart
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void manageAttachments(List<File> attachments, Integer codiceDl, MimeMultipart mimeMultipart) throws MessagingException, IOException {
		for (int i = 0; i < mimeMultipart.getCount(); i++) {  
			BodyPart bodyPart = mimeMultipart.getBodyPart(i); 
			if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT) && isAllowedMimeType(bodyPart)) {
				InputStream is = bodyPart.getInputStream();      
				File file = new File(uploadPathDir + System.getProperty("file.separator") + codiceDl + System.getProperty("file.separator") + StringUtility.buildAttachmentFileNameDl(bodyPart.getFileName(), codiceDl));
				FileUtils.buildFileFromInputStream(is, file);
				attachments.add(file);  
			}
		}
	}

	/**
	 * @param bodyPart
	 * @return
	 */
	private boolean isAllowedMimeType(BodyPart bodyPart) {
		List<String> allowedMimeTypes = Arrays.asList(IGerivConstants.ALLOWED_MIME_TYPES.split(","));
		for (String mimeType : allowedMimeTypes) {
			try {
				if (bodyPart.isMimeType(mimeType)) {
					return true;
				}
			} catch (MessagingException e) {}
		}
		return false;
	}

	/**
	 * 
	 * @param body
	 * @param lineBreak
	 * @return
	 */
	private Map<String, String> buildBodyReplyMap(String body, String lineBreak) {
		Map<String, String> map = new HashMap<String, String>();
		String atEmail = env.equalsIgnoreCase("DPE") ? IGerivConstants.AT_EMAIL_IGERIV : (env.equalsIgnoreCase("CDL") ? IGerivConstants.AT_EMAIL_CDL : IGerivConstants.AT_EMAIL_IGERIV);
		if (body.contains(atEmail)) {
			int indexOf = body.indexOf(atEmail);
			String temp = body.substring(0, indexOf);
			int offset = temp.lastIndexOf(lineBreak); 
			String text = body.substring(0, offset);
			text = WordUtils.wrap(text, 66, "\r\n", true);
			map.put("body", StringUtility.escapeHTML(text, true).replaceAll("\r\n", "<br>"));
			String text1 = body.substring(offset);
			text1 = WordUtils.wrap(text1, 80, "\r\n", true);
			map.put("prevMsg", StringUtility.escapeHTML(text1, true).replaceAll("\r\n", "<br>"));
		}
		return map;
	}
	
	/**
	 * Costruisce il messaggio istantaneo per l'edicola
	 * 
	 * @param codiceDl
	 * @param codWebEdicola
	 * @param messaggio
	 * @param attachments 
	 * @return
	 */
	private MessaggioVo buildMessaggio(Integer codiceDl, Integer codWebEdicola, String messaggio, List<File> attachments) {
		MessaggioVo vo = new MessaggioVo();
		MessaggioPk pk = new MessaggioPk();
		pk.setCodFiegDl(codiceDl);
		pk.setDestinatarioA(codWebEdicola);
		pk.setDestinatarioB(0);
		pk.setDtMessaggio(new Timestamp(new Date().getTime()));
		pk.setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
		vo.setPk(pk);
		if (messaggio.length() < 4000) {
			vo.setMessaggio(messaggio);
		}
		vo.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
		vo.setTipoMessaggio(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO);
		vo.setAttachmentName1(attachments.size() > 0 ? attachments.get(0).getName() : null);
		vo.setAttachmentName2(attachments.size() > 1 ? attachments.get(1).getName() : null);
		vo.setAttachmentName3(attachments.size() > 2 ? attachments.get(2).getName() : null);
		return vo;
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

	public String getUploadPathDir() {
		return uploadPathDir;
	}

	public void setUploadPathDir(String uploadPathDir) {
		this.uploadPathDir = uploadPathDir;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}
	
}
