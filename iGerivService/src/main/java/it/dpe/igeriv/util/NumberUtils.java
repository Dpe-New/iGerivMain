package it.dpe.igeriv.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.google.common.base.Strings;

/**
 * La classe fornisce una serie di metodi di utilità per la gestione dei numeri.
 * 
 * @author romanom
 *
 */
public class NumberUtils {
	private static DecimalFormat df = null;
	
	static {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
		df = ((DecimalFormat) nf);
		df.applyPattern("###,###,##0.00");
		/*
		DecimalFormatSymbols fs = new DecimalFormatSymbols(Locale.getDefault());
		fs.setDecimalSeparator(',');
		fs.setGroupingSeparator('.'); 
		df.setDecimalFormatSymbols(fs);
		*/
	}
	
	public static String formatNumber(Number number) {
		return df.format(number);
	}
	
	public static Number unformatNumber(String number) throws ParseException {
		return df.parse(number);
	}
	
	public static String formatNumber(Number number, Locale locale) {
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		DecimalFormat df1 = ((DecimalFormat) nf);
		return df1.format(number);
	}
	
	public static String formatNumber(Number number, Locale locale, String pattern) {
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		DecimalFormat df1 = ((DecimalFormat) nf);
		if (!Strings.isNullOrEmpty(pattern)) {
			df1.applyPattern(pattern);
		}
		return df1.format(number);
	}
	
	public static String formatNumber(String number, Locale locale) {
		if (Strings.isNullOrEmpty(number) || !org.apache.commons.lang.math.NumberUtils.isNumber(number)) {
			return "";
		}
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		DecimalFormat df1 = ((DecimalFormat) nf);
		return df1.format(Double.parseDouble(number));
	}
	
	public static String formatNumber(String number, Locale locale, String pattern) {
		if (Strings.isNullOrEmpty(number) || !org.apache.commons.lang.math.NumberUtils.isNumber(number)) {
			return "";
		}
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
		DecimalFormat df1 = ((DecimalFormat) nf);
		if (!Strings.isNullOrEmpty(pattern)) {
			df1.applyPattern(pattern);
		}
		return df1.format(Double.parseDouble(number));
	}
	
	public static String formatNumber(Number number, String format) {
		df.applyLocalizedPattern(format);
		return df.format(number);
	}
	
	public static Number unformatNumber(String number, String format) throws ParseException {
		df.applyLocalizedPattern(format);
		return df.parse(number);
	}
	
	/**
	 * @deprecated
	 * 
	 * Formatta il numero construendo una maschera.
	 * 
	 * @param Number number Numero da formattare
	 * @param int size Lunghezza del numero
	 * @param int decimals Numero di decimali
	 * @param boolean deleteDecimalSeparator Se true elimina il separatore dei decimali
	 * @return String Numero formattato
	 */
	@Deprecated
	public static String formatNumber(Number number, int size, int decimals, boolean deleteDecimalSeparator) {
		String format = "";
		if (number == null) {
			number = 0;
		}
		StringBuffer mask = new StringBuffer("");
		boolean decimalPart = false;
		for (int i = 0; i < size; i++) {
			if (decimals != 0 && (i == (size - decimals))) {
				decimalPart = true;
				mask.append(".");
			}
			if (decimalPart) {
				mask.append("0");
			} else {
				mask.append("0");
			}
		}
		NumberFormat formatter = new DecimalFormat(mask.toString());
		format = formatter.format(number);
		if (deleteDecimalSeparator) {
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
			format = format.replaceAll(String.valueOf(dfs.getDecimalSeparator()), "");
		}
		return format;
	}
	
	/**
	 * @deprecated
	 * 
	 * @param number
	 * @param size
	 * @param decimals
	 * @param deleteDecimalSeparator
	 * @param deleteLeadingZero
	 * @return
	 */
	@Deprecated
	public static String formatNumber(Number number, int size, int decimals, boolean deleteDecimalSeparator, boolean deleteLeadingZero) {
		String formatNumber = formatNumber(number, size, decimals, deleteDecimalSeparator);
		if (deleteLeadingZero) {
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
			while (formatNumber.startsWith("0") && !formatNumber.startsWith("0" + dfs.getDecimalSeparator())) {
				formatNumber = formatNumber.replaceFirst("0", "");
			}
		}
		return formatNumber;
	}

	/**
	 * @deprecated
	 * 
	 * Converte il formato decimale italiano nel formato inglese,
	 * ritorna il numero formattato nel formato inglese.
	 * 
	 * @param String format
	 * @param String cellDisplay
	 * @param Locale locale
	 * @return String
	 */
	@Deprecated
	public static String defaultFormatNumber(String format, String cellDisplay, Locale locale) {
		try {
			Number number = org.springframework.util.NumberUtils.parseNumber(cellDisplay.replaceAll("\u20AC", "").trim(), Number.class, NumberFormat.getNumberInstance(locale));
	        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
	        DecimalFormat df1 = (DecimalFormat) nf;
	        String defaulFormat = format.replaceAll("\\" + IGerivConstants.THOUSANDS_SPERATOR_IT, IGerivConstants.DECIMAL_SPERATOR_IT);
	        int lastIndexOf = defaulFormat.lastIndexOf(IGerivConstants.DECIMAL_SPERATOR_IT);
	        defaulFormat = defaulFormat.substring(0, lastIndexOf) + IGerivConstants.THOUSANDS_SPERATOR_IT + defaulFormat.substring(lastIndexOf + 1);
			df1.applyLocalizedPattern(defaulFormat);
	        return df1.format(number);
		} catch (Exception e) {
			return cellDisplay;
		}
	}

	/**
	 * Ritorna la porzione numerica della stringa in input.
	 * 
	 * @param String str
	 * @return
	 */
	public static Long getOnlyNumerics(String str) {
		if (str == null) {
			return null;
		}
		StringBuffer strBuff = new StringBuffer();
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (Character.isDigit(c)) {
				strBuff.append(c);
			}
		}
		return new Long(strBuff.toString());
	}
	
	/**
	 * Formatta l'importo con il segno del'euro.
	 * 
	 * @param BigDecimal importo
	 * @return String
	 */
	public static String formatImporto(BigDecimal importo) {
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("00.00");
		String str = IGerivConstants.EURO_SIGN_HTML + " " + df.format(importo);
		return str;
	}
	
	/**
	 * @param arg0
	 * @return
	 */
	public static Integer getAsInteger(String strNum) {
		Integer codEdicola = null;
		try {
			codEdicola = (strNum != null && !strNum.equals("")) ? org.springframework.util.NumberUtils.parseNumber(strNum, Integer.class) : null;
		} catch (Exception e) {
			
		}
		return codEdicola;
	}
	
	/**
	 * @param arg0
	 * @return
	 */
	public static Long getAsLong(String strNum) {
		Long codEdicola = null;
		try {
			codEdicola = (strNum != null && !strNum.equals("")) ? org.springframework.util.NumberUtils.parseNumber(strNum, Long.class) : null;
		} catch (Exception e) {
			
		}
		return codEdicola;
	}
}
