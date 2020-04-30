package it.dpe.igeriv.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Strings;

/**
 * La classe fornisce dei metodi di utilità per la gestione delle stringhe.
 */
public class StringUtility {
	private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
	private static HashMap<String, String> htmlEntities;
	private static DualHashBidiMap asciiEntities;
	static {
		htmlEntities = new HashMap<String, String>();
		asciiEntities = new DualHashBidiMap();
		htmlEntities.put("&lt;", "<");
		htmlEntities.put("&gt;", ">");
		htmlEntities.put("&amp;", "&");
		htmlEntities.put("&quot;", "\"");
		htmlEntities.put("&agrave;", "à");
		htmlEntities.put("&Agrave;", "À");
		htmlEntities.put("&acirc;", "â");
		htmlEntities.put("&auml;", "ä");
		htmlEntities.put("&Auml;", "Ä");
		htmlEntities.put("&Acirc;", "Â");
		htmlEntities.put("&aring;", "å");
		htmlEntities.put("&Aring;", "Å");
		htmlEntities.put("&aelig;", "æ");
		htmlEntities.put("&AElig;", "Æ");
		htmlEntities.put("&ccedil;", "ç");
		htmlEntities.put("&Ccedil;", "Ç");
		htmlEntities.put("&eacute;", "é");
		htmlEntities.put("&Eacute;", "É");
		htmlEntities.put("&egrave;", "è");
		htmlEntities.put("&Egrave;", "È");
		htmlEntities.put("&ecirc;", "ê");
		htmlEntities.put("&Ecirc;", "Ê");
		htmlEntities.put("&euml;", "ë");
		htmlEntities.put("&Euml;", "Ë");
		htmlEntities.put("&iuml;", "ï");
		htmlEntities.put("&Iuml;", "Ï");
		htmlEntities.put("&ocirc;", "ô");
		htmlEntities.put("&Ocirc;", "Ô");
		htmlEntities.put("&ouml;", "ö");
		htmlEntities.put("&Ouml;", "Ö");
		htmlEntities.put("&oslash;", "ø");
		htmlEntities.put("&Oslash;", "Ø");
		htmlEntities.put("&szlig;", "ß");
		htmlEntities.put("&ugrave;", "ù");
		htmlEntities.put("&Ugrave;", "Ù");
		htmlEntities.put("&ucirc;", "û");
		htmlEntities.put("&Ucirc;", "Û");
		htmlEntities.put("&uuml;", "ü");
		htmlEntities.put("&Uuml;", "Ü");
		htmlEntities.put("&nbsp;", " ");
		htmlEntities.put("&copy;", "\u00a9");
		htmlEntities.put("&reg;", "\u00ae");
		htmlEntities.put("&euro;", "\u20a0");
		htmlEntities.put("&deg;", "°");

		// asciiEntities.put(" ","&#032;");
		asciiEntities.put("!", "&#033;");
		asciiEntities.put("\"", "&#034;");
		asciiEntities.put("#", "&#035;");
		asciiEntities.put("$", "&#036;");
		asciiEntities.put("%", "&#037;");
		asciiEntities.put("&", "&#038;");
		asciiEntities.put("‘", "&#039;");
		asciiEntities.put("(", "&#040;");
		asciiEntities.put(")", "&#041;");
		asciiEntities.put("*", "&#042;");
		asciiEntities.put("+", "&#043;");
		asciiEntities.put(",", "&#044;");
		asciiEntities.put("-", "&#045;");
		asciiEntities.put("÷", "&#247;");
		asciiEntities.put(".", "&#046;");
		asciiEntities.put("/", "&#047;");
		asciiEntities.put(":", "&#058;");
		asciiEntities.put(";", "&#059;");
		asciiEntities.put("<", "&#060;");
		asciiEntities.put("=", "&#061;");
		asciiEntities.put(">", "&#062;");
		asciiEntities.put("?", "&#063;");
		asciiEntities.put("@", "&#064;");
		asciiEntities.put("[", "&#091;");
		asciiEntities.put("\\", "&#092;");
		asciiEntities.put("]", "&#093;");
		asciiEntities.put("^", "&#094;");
		asciiEntities.put("_", "&#095;");
		asciiEntities.put("`", "&#096;");
		asciiEntities.put("{", "&#123;");
		asciiEntities.put("|", "&#124;");
		asciiEntities.put("}", "&#125;");
		asciiEntities.put("~", "&#126;");
		asciiEntities.put("^?", "&#127;");
		asciiEntities.put("ˆ", "&#136;");
		asciiEntities.put("‰", "&#137;");
		asciiEntities.put("Š", "&#138;");
		asciiEntities.put("‹", "&#139;");
		asciiEntities.put("‘", "&#145;");
		asciiEntities.put("’", "&#146;");
		asciiEntities.put("“", "&#147;");
		asciiEntities.put("”", "&#148;");
		asciiEntities.put("•", "&#149;");
		asciiEntities.put("–", "&#150;");
		asciiEntities.put("—", "&#151;");
		asciiEntities.put("˜˜ ", "&#152;");
		asciiEntities.put("™", "&#153;");
		asciiEntities.put("¡", "&#161;");
		asciiEntities.put("¢", "&#162;");
		asciiEntities.put("£", "&#163;");
		asciiEntities.put("¥", "&#165;");
		asciiEntities.put("¦", "&#166;");
		asciiEntities.put("§", "&#167;");
		asciiEntities.put("©", "&#169;");
		asciiEntities.put("¬", "&#172;");
		asciiEntities.put("¬", "&#173;");
		asciiEntities.put("®", "&#174;");
		asciiEntities.put("°", "&#176;");
		asciiEntities.put("±", "&#177;");
		asciiEntities.put("µ", "&#181;");
		asciiEntities.put("¶", "&#182;");
		asciiEntities.put("¿", "&#191;");
		asciiEntities.put("À", "&#192;");
		asciiEntities.put("Á", "&#193;");
		asciiEntities.put("Â", "&#194;");
		asciiEntities.put("Ã", "&#195;");
		asciiEntities.put("Ä", "&#196;");
		asciiEntities.put("Å", "&#197;");
		asciiEntities.put("Æ", "&#198;");
		asciiEntities.put("Ç", "&#199;");
		asciiEntities.put("È", "&#200;");
		asciiEntities.put("É", "&#201;");
		asciiEntities.put("Ê", "&#202;");
		asciiEntities.put("Ë", "&#203;");
		asciiEntities.put("Ì", "&#204;");
		asciiEntities.put("Í", "&#205;");
		asciiEntities.put("Î", "&#206;");
		asciiEntities.put("Ï", "&#207;");
		asciiEntities.put("Ñ", "&#209;");
		asciiEntities.put("ß", "&#223;");
		asciiEntities.put("à", "&#224;");
		asciiEntities.put("á", "&#225;");
		asciiEntities.put("â", "&#226;");
		asciiEntities.put("ã", "&#227;");
		asciiEntities.put("ä", "&#228;");
		asciiEntities.put("å", "&#229;");
		asciiEntities.put("æ", "&#230;");
		asciiEntities.put("ç", "&#231;");
		asciiEntities.put("è", "&#232;");
		asciiEntities.put("é", "&#233;");
		asciiEntities.put("ê", "&#234;");
		asciiEntities.put("ë", "&#235;");
		asciiEntities.put("ñ", "&#241;");
	}

	/**
	 * Il metodo aggiunge a sinistra della stringa di input un numero n di
	 * caratteri dati
	 * 
	 * @param stringa
	 *            - la stringa di input
	 * @param len
	 *            - il numero di caratteri da aggiungere
	 * @param fillChar
	 *            - il carattere da aggiungere
	 * @return
	 */
	public static String fillSx(String stringa, int len, char fillChar) {
		int lenString = stringa.length();

		for (int i = lenString; i < len; i++) {
			stringa = fillChar + stringa;
		}

		return stringa.substring(0, len);
	}

	/**
	 * Il metodo aggiunge a destra della stringa di input un numero n di
	 * caratteri dati
	 * 
	 * @param stringa
	 *            - la stringa di input
	 * @param len
	 *            - il numero di caratteri da aggiungere
	 * @param fillChar
	 *            - il carattere da aggiungere
	 * @return
	 */
	public static String fillDx(String stringa, int len, char fillChar) {
		int lenString = stringa.length();

		for (int i = lenString; i < len; i++) {
			stringa = stringa + fillChar;
		}

		return stringa.substring(0, len);
	}

	/**
	 * Il metodo prepara una stringa con apici nella forma corretta da passare
	 * al data base:sostituisce il singolo apice ( ' ) con due singoli apici (
	 * '' ),
	 * 
	 * @param str
	 * @return
	 */
	public static String dbString(String str) {
		// sostituisce ' con ''
		str = str.replaceAll("'", "''");

		return str;
	}

	/**
	 * il metodo restituisce una stringa vuota se l'input è null.
	 * 
	 * @param str
	 * @return
	 */
	public static String nval(String str) {
		// se null, ritorna ""; se no, ritorna str
		if (str == null) {
			return new String();
		}

		return str;
	}

	public static boolean contains(String[] arrayStr, String string) {
		for (int i = 0; i < arrayStr.length; i++) {
			if (arrayStr[i].equals(string)) {
				return true;
			}
		}

		return false;
	}

	public static int firstNoMatches(String str, char chrToDiscard) {
		char[] charArr = str.toCharArray();

		for (int i = 0; i < charArr.length; i++) {
			if (charArr[i] != chrToDiscard) {
				return i;
			}
		}

		return -1;
	}

	public static String cutValuesSx(String str, String strToCut, String strReplace) {
		while (str.startsWith(strToCut)) {
			str = str.replaceFirst(strToCut, strReplace);
		}

		return str;
	}

	public static String normalizeString(String strIn) {
		// Per adesso fa solo upper case da decidere se togliere anche gli spazi
		// e alcuni carattero
		if (strIn != null) {
			return strIn.toUpperCase();
		} else {
			return "";
		}
	}

	/**
	 * RESTITUISCE UNA STRINGA di valori separati da un carattere
	 * 
	 * @param valori
	 *            int [] Valori da inserire nella stringa
	 * @param separatore
	 *            String Carattere separatore
	 * @return String
	 */

	public static String stringUntokenizer(int[] valori, String separatore) {
		StringBuffer outBuff = new StringBuffer();
		for (int i = 0; i < valori.length; i++) {
			if (i > 0) {
				outBuff.append(separatore);
			}
			outBuff.append(valori[i]);
		}
		return outBuff.toString();
	}

	/**
	 * RESTITUISCE UNA STRINGA di valori separati da un carattere
	 * 
	 * @param valori
	 *            object [] Valori da inserire nella stringa
	 * @param separatore
	 *            String Carattere separatore
	 * @return String
	 */

	public static String stringUntokenizer(Object[] valori, String separatore) {
		StringBuffer outBuff = new StringBuffer();
		for (int i = 0; i < valori.length; i++) {
			if (i > 0) {
				outBuff.append(separatore);
			}
			outBuff.append(valori[i].toString());
		}
		return outBuff.toString();
	}

	public static String getSqlErr(SQLException ex) {
		StringBuffer msg = new StringBuffer(ex.getLocalizedMessage() + "\n");
		if (ex.getErrorCode() != 0) {
			switch (ex.getErrorCode()) {
			case 1:
				msg.insert(0, "Riga gia presente, inserimento o aggiornamento non consentito.\nDettagli:\n");
				break;
			}
			msg.append("Codice errore: " + ex.getErrorCode() + "\n");
		}
		if (ex.getSQLState() != null && !ex.getSQLState().isEmpty())
			msg.append("Stato: " + ex.getSQLState() + "\n");
		return msg.toString();
	}

	/**
	 * Ritorna la stringa di errore con i riferimenti dpe
	 * 
	 * @param exception
	 *            eccezione sollevata
	 * @param html
	 *            true:formatta in html false:nessuna formattazione
	 * @return String
	 */
	public static String getMsgFatalError(Exception exception, boolean html) {

		String newLine = html ? "<BR/>" : "\n";
		StringBuffer msg = new StringBuffer();
		if (html)
			msg.append("<HTML><BODY><FONT  color=\"#000000\"><CENTER>");

		msg.append("Attenzione errore nella procedura" + newLine);
		if (html)
			msg.append("<P><FONT size=\"+1\" color=\"#FF0000\">");
		String[] errors = exception.getLocalizedMessage().trim().split(" ");
		for (int i = 0; i < errors.length; ++i) {
			msg.append(errors[i]);
			msg.append((i % 4 == 0 && i > 0 && i < errors.length - 1) ? newLine : " ");
		}

		// msg.append( exception.getLocalizedMessage() );
		if (html)
			msg.append("</FONT></P></CENTER>");
		msg.append(newLine + "Segnalare questa interruzione all'assistenza tecnica:");
		if (html)
			msg.append("<CENTER><P><FONT size=\"-1\" color=\"#00A040\">");
		msg.append(newLine + "DPE s.r.l.");
		msg.append(newLine + "Via Vallarsa, 10");
		msg.append(newLine + "20139 Milano");
		msg.append(newLine + "Telefono +39 02 5695770");
		msg.append(newLine + "Fax +39 02 5696968");
		msg.append(newLine + "E-mail dpe@dpe.it");
		if (html)
			msg.append("</FONT></P></CENTER>");
		msg.append(newLine + "Si consiglia di chiudere questa sessione");
		if (html)
			msg.append("</FONT></BODY></HTML>");
		return msg.toString();
	}

	/**
	 * @param s
	 * @param nChar
	 * @return
	 */
	public static String getStringFixedLength(String s, int nChar) {
		if (s == null) {
			s = " ";
		}
		StringBuilder sb = new StringBuilder(StringUtils.rightPad(s, nChar));
		sb.setLength(nChar);
		String string = sb.toString();
		return string;
	}

	/**
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();

	}

	public static final String unescapeHTML(String source) {
		int i, j;
		boolean continueLoop;
		int skip = 0;
		do {
			continueLoop = false;
			i = source.indexOf("&", skip);
			if (i > -1) {
				j = source.indexOf(";", i);
				if (j > i) {
					String entityToLookFor = source.substring(i, j + 1);
					String value = (String) htmlEntities.get(entityToLookFor);
					if (value != null) {
						source = source.substring(0, i) + value + source.substring(j + 1);
						continueLoop = true;
					} else if (value == null) {
						skip = i + 1;
						continueLoop = true;
					}
				}
			}
		} while (continueLoop);
		return source;
	}
	
	/**
	 * @param s
	 * @return
	 */
	public static final String escapeHTML(String s, boolean escapeHtml) {
		StringBuffer sb = new StringBuffer();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				if (escapeHtml)
					sb.append("&lt;");
				else
					sb.append(c);
				break;
			case '>':
				if (escapeHtml)
					sb.append("&gt;");
				else
					sb.append(c);
				break;
			case '&':
				if (escapeHtml)
					sb.append("&amp;");
				else
					sb.append(c);
				break;
			case '"':
				if (escapeHtml)
					sb.append("&quot;");
				else
					sb.append(c);
				break;
			case '\t':
				if (escapeHtml)
					sb.append("&nbsp; &nbsp; &nbsp;");
				else
					sb.append(c);
				break;
			case 'à':
				sb.append("&agrave;");
				break;
			case 'À':
				sb.append("&Agrave;");
				break;
			case 'ò':
				sb.append("&ograve;");
				break;
			case 'Ò':
				sb.append("&Ograve;");
				break;
			case 'ì':
				sb.append("&igrave;");
				break;
			case 'Ì':
				sb.append("&Igrave;");
				break;
			case 'â':
				sb.append("&acirc;");
				break;
			case 'Â':
				sb.append("&Acirc;");
				break;
			case 'ä':
				sb.append("&auml;");
				break;
			case 'Ä':
				sb.append("&Auml;");
				break;
			case 'å':
				sb.append("&aring;");
				break;
			case 'Å':
				sb.append("&Aring;");
				break;
			case 'æ':
				sb.append("&aelig;");
				break;
			case 'Æ':
				sb.append("&AElig;");
				break;
			case 'ç':
				sb.append("&ccedil;");
				break;
			case 'Ç':
				sb.append("&Ccedil;");
				break;
			case 'é':
				sb.append("&eacute;");
				break;
			case 'É':
				sb.append("&Eacute;");
				break;
			case 'è':
				sb.append("&egrave;");
				break;
			case 'È':
				sb.append("&Egrave;");
				break;
			case 'ê':
				sb.append("&ecirc;");
				break;
			case 'Ê':
				sb.append("&Ecirc;");
				break;
			case 'ë':
				sb.append("&euml;");
				break;
			case 'Ë':
				sb.append("&Euml;");
				break;
			case 'ï':
				sb.append("&iuml;");
				break;
			case 'Ï':
				sb.append("&Iuml;");
				break;
			case 'ô':
				sb.append("&ocirc;");
				break;
			case 'Ô':
				sb.append("&Ocirc;");
				break;
			case 'ö':
				sb.append("&ouml;");
				break;
			case 'Ö':
				sb.append("&Ouml;");
				break;
			case 'ø':
				sb.append("&oslash;");
				break;
			case 'Ø':
				sb.append("&Oslash;");
				break;
			case 'ß':
				sb.append("&szlig;");
				break;
			case 'ù':
				sb.append("&ugrave;");
				break;
			case 'Ù':
				sb.append("&Ugrave;");
				break;
			case 'û':
				sb.append("&ucirc;");
				break;
			case 'Û':
				sb.append("&Ucirc;");
				break;
			case 'ü':
				sb.append("&uuml;");
				break;
			case 'Ü':
				sb.append("&Uuml;");
				break;
			case '®':
				sb.append("&reg;");
				break;
			case '©':
				sb.append("&copy;");
				break;
			case '\u20AC':
				sb.append("&euro;");
				break;
			case '°':
				sb.append("&deg;");
				break;
			case '§':
				sb.append("&sect;");
				break;
			// be carefull with this one (non-breaking whitee space)
			case ' ':
				if (escapeHtml)
					sb.append("&nbsp;");
				else
					sb.append(c);
				break;

			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	public static String removeHTML(String htmlString) {
		// Remove HTML tag from java String
		String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");

		// Remove Carriage return from java String
		noHTMLString = noHTMLString.replaceAll("\r", "<br/>");

		// Remove New line from java string and replace html break
		noHTMLString = noHTMLString.replaceAll("\n", " ");
		noHTMLString = noHTMLString.replaceAll("\'", "&#39;");
		noHTMLString = noHTMLString.replaceAll("\"", "&quot;");
		return noHTMLString;
	}

	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}

	public static String stripPercentage(String name) {
		String fileName = null;
		if (name.startsWith("%")) {
			fileName = name.replaceFirst("%", "");
		}
		fileName = name.replaceAll("%\\.", ".").replaceAll("%", " ");
		return fileName;
	}

	public static final String escapeASCII(String source) {
		return asciiEntities.get(source).toString();
	}

	public static final String unescapeASCII(String source) {
		return asciiEntities.getKey(source).toString();
	}

	public static String checkSpecialChars(String s) {
		int n = s.length();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			String c = Character.toString(s.charAt(i));
			if (asciiEntities.containsKey(c)) {
				sb.append(asciiEntities.get(c).toString());
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String preparePlaceHolders(int length, String namedParam) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length;) {
			if (namedParam != null) {
				builder.append(":" + namedParam + i);
			} else {
				builder.append("?");
			}
			if (++i < length) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

	/**
	 * Validate the form of an email address.
	 * 
	 * <P>
	 * Return <tt>true</tt> only if
	 * <ul>
	 * <li> <tt>aEmailAddress</tt> can successfully construct an
	 * {@link javax.mail.internet.InternetAddress}
	 * <li>when parsed with "@" as delimiter, <tt>aEmailAddress</tt> contains
	 * two tokens which satisfy
	 * {@link hirondelle.web4j.util.Util#textHasContent}.
	 * </ul>
	 * 
	 * <P>
	 * The second condition arises since local email addresses, simply of the
	 * form "<tt>albert</tt>", for example, are valid for
	 * {@link javax.mail.internet.InternetAddress}, but almost always undesired.
	 */
	public static boolean isValidEmailAddress(String aEmailAddress) {
		if (aEmailAddress == null)
			return false;
		boolean result = true;
		try {
			new InternetAddress(aEmailAddress);
			if (!hasNameAndDomain(aEmailAddress)) {
				result = false;
			}
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	private static boolean hasNameAndDomain(String aEmailAddress) {
		String[] tokens = aEmailAddress.split("@");
		return tokens.length == 2 && textHasContent(tokens[0]) && textHasContent(tokens[1]);
	}

	/**
	 * Returns true if aText is non-null and has visible content.
	 * 
	 * This is a test which is often performed, and should probably be placed in
	 * a general utility class.
	 */
	public static boolean textHasContent(String aText) {
		String EMPTY_STRING = "";
		return (aText != null) && (!aText.trim().equals(EMPTY_STRING));
	}
	
	/**
	 * @param description
	 * @return
	 */
	public static String getStringInitials(String description, int length) {
		StringBuilder sbInitials = new StringBuilder();
		String[] nameParts = description.split("\\s");
		for (String part : nameParts) {
			sbInitials.append(Character.toUpperCase(part.charAt(0)));
		}
		if (sbInitials.length() < length) {
			Random r = new Random();
			double d = length - sbInitials.length();
			int randint = r.nextInt((int) Math.pow(10d, d));
			sbInitials.append(randint);
		}
		return sbInitials.toString();
	}
	
	/**
	 * @param header
	 * @return
	 */
	public static String getBrowserName(String header) {
		if (!Strings.isNullOrEmpty(header)) {
			if (header.toUpperCase().contains(IGerivConstants.BROWSER_NAME_IE)) {
				return IGerivConstants.BROWSER_NAME_IE;
			} else if (header.toUpperCase().contains(IGerivConstants.BROWSER_NAME_FIREFOX)) {
				return IGerivConstants.BROWSER_NAME_FIREFOX;
			} else if (header.toUpperCase().contains(IGerivConstants.BROWSER_NAME_CHROME)) {
				return IGerivConstants.BROWSER_NAME_CHROME;
			} else if (header.toUpperCase().contains(IGerivConstants.BROWSER_NAME_SAFARI)) {
				return IGerivConstants.BROWSER_NAME_SAFARI;
			} else if (header.toUpperCase().contains(IGerivConstants.BROWSER_NAME_OPERA)) {
				return IGerivConstants.BROWSER_NAME_OPERA;
			}
		}
		return "";
	}

	/**
	 * Ritorna un nome per i file in upload dell'utente
	 * 
	 * @return
	 */
	public static String buildAttachmentFileName(String fileName, Integer codDl, Integer codEdicola, String codUtente) {
		String ext = fileName.indexOf(".") != -1 ? "." + FilenameUtils.getExtension(fileName) : fileName;
		return (codDl == null ? "" : codDl) + "_" + (codEdicola == null ? "" : codEdicola) + "_" + (codUtente == null ? "" : codUtente) + "_"  + DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + ext;
	}
	
	/**
	 * Ritorna un nome per i file in upload del DL
	 * 
	 * @return
	 */
	public static String buildAttachmentFileNameDl(String fileName, Integer codDl) {
		String ext = fileName.indexOf(".") != -1 ? "." + FilenameUtils.getExtension(fileName) : fileName;
		return (codDl == null ? "" : codDl) + "_" + DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + ext;
	}
	
	/**
	 * Ritorna il testo senza Html
	 * 
	 * @param string
	 * @return
	 */
	public static String stripHtml(String string) {
		return string != null ? unescapeHTML(string.replaceAll("\\<.*?>","")) : "";
	}
	
	/**
	 * Ritorna il nome del file con l'estensione in minuscolo
	 * @param fileName
	 */
	public static String getFileName(String fileName) {
		String fn = "";
		String ext = FilenameUtils.getExtension(fileName);
		if (ext != null) {
			String baseName = flattenToAscii(FilenameUtils.getBaseName(fileName));
			fn = baseName + "." + ext.toLowerCase();
		}
		return fn;
	}
	
	/**
	 * Toglie tutti i caratteri accentati e speciali
	 * 
	 * @param string
	 * @return
	 */
	public static String flattenToAscii(String string) {
	    char[] out = new char[string.length()];
	    string = Normalizer.normalize(string, Normalizer.Form.NFD);
	    int j = 0;
	    for (int i = 0; i < string.length(); i++) {
	        char c = string.charAt(i);
	        if (c <= '\u007F') out[j++] = c;
	    }
	    return new String(out);
	}

}
