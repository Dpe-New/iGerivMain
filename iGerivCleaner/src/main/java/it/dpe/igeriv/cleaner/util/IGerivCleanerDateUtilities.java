package it.dpe.igeriv.cleaner.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * La classe fornisce una serie di metodi di utilità per la gestione delle date.
 */
public class IGerivCleanerDateUtilities {
    public static final String FORMATO_DATA = "dd-MM-yyyy";
    public static final String FORMATO_DATA_SHORT = "dd/MM/yy";
    public static final String FORMATO_DATA_SLASH = "dd/MM/yyyy";
    public static final String FORMATO_DATA_SLASH_SHORT = "MM/yyyy";
    public static final String FORMATO_DATA_SLASH_HHMMSS = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMATO_DATA_HHMMSS = "dd-MM-yyyy HH:mm:ss";
    public static final String FORMATO_DATA_YYYYMMDD = "yyyyMMdd";
    public static final String FORMATO_DATA_YYMMDD = "yyMMdd";
    public static final String FORMATO_DATA_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMATO_DATA_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATO_DATA_COMPETENZA = "MM-yyyy";
    public static final String FORMATO_DATA_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String FORMATO_DATA_YYMMDDHHMMSS = "yyMMddHHmmss";
    public static final String FORMATO_ORA_HHMMSS = "HHmmss";
    public static final String FORMATO_GIORNO_SETTIMANA = "EEEE";
    public static int DIR_FORWARD = 1;
    public static int DIR_BACKWARD = -1;
    private static long MILLISEC_A_DAY = 86400000; // 24 x 60 x 60 x 100

    public static enum Egiorni {
	LUN(1, "Lunedì"),
	MAR(2, "Martedì"),
	MER(3, "Mercoledì"),
	GIO(4, "Giovedì"),
	VEN(5, "Venerdì"),
	SAB(6, "Sabato"),
	DOM(7, "Domenica");

	private int nGiorno;
	private String desc;

	private Egiorni(int nGiorno, String desc) {
	    this.nGiorno = nGiorno;
	    this.desc = desc;
	}

	public static Egiorni getEGiorno(int nGiorno) {
	    for (Egiorni giorno : Egiorni.values()) {
		if (giorno.getNGiorno() == nGiorno)
		    return giorno;
	    }
	    return null;
	}

	public int getNGiorno() {
	    return nGiorno;
	}

	public String toString() {
	    return desc;
	}

    }

    private IGerivCleanerDateUtilities() {}

    /**
     * Il metodo ritorna una stringa che rappresenta l'anno nel formato YYYY
     * estraendolo dalla data di ingresso.
     * @param dataStr
     * @return
     * @throws java.lang.Exception
     * @deprecated
     */
    public static String findYearFromString(String dataStr) throws Exception {
	SimpleDateFormat sData = new SimpleDateFormat(FORMATO_DATA);
	sData.setLenient(false);
	SimpleDateFormat sAnno = new SimpleDateFormat("yyyy");
	sAnno.setLenient(false);
	String annoStr = null;

	try {
	    Date data = sData.parse(dataStr);
	    annoStr = sAnno.format(data);
	}
	catch (ParseException e) {
	    throw e;
	}
	return annoStr;
    }

    /**
     * Il metodo aggiunge il numero di giorni dato alla data in input.
     * @param day
     * @param numDays
     * @return
     */
    public static Timestamp aggiungiGiorni(Timestamp day, int numDays) {
	return rollDay(day, numDays, DIR_FORWARD);
    }


    public static Timestamp togliAnni(Timestamp day, int numAnni) {
	return rollDay(day, numAnni * 365, DIR_BACKWARD);
    }


    /**
     * Il metodo toglie il numero di giorni dato dalla data in input.
     * @param day
     * @param numDays
     * @return
     */
    public static Timestamp togliGiorni(Timestamp day, int numDays) {
	return rollDay(day, numDays, DIR_BACKWARD);
    }

    private static Timestamp rollDay(Timestamp day, int numDays, int direction) {
	long millisecToMove = MILLISEC_A_DAY * numDays * direction;
	Timestamp dayRolled = new Timestamp(day.getTime() + millisecToMove);

	return dayRolled;
    }

    public static Timestamp getCurrentYearJanuary() {
	GregorianCalendar gregorianCalendar = new GregorianCalendar();
	gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
	gregorianCalendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
	gregorianCalendar.set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY);
	gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
	gregorianCalendar.set(GregorianCalendar.MINUTE, 0);
	gregorianCalendar.set(GregorianCalendar.SECOND, 0);
	gregorianCalendar.set(GregorianCalendar.MILLISECOND, 0);
	return new Timestamp(gregorianCalendar.getTimeInMillis());
    }

    public static Timestamp getCurrentYearDecember() {
	GregorianCalendar gregorianCalendar = new GregorianCalendar();
	gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
	gregorianCalendar.set(GregorianCalendar.DAY_OF_MONTH, 31);
	gregorianCalendar.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
	gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
	gregorianCalendar.set(GregorianCalendar.MINUTE, 0);
	gregorianCalendar.set(GregorianCalendar.SECOND, 0);
	gregorianCalendar.set(GregorianCalendar.MILLISECOND, 0);
	return new Timestamp(gregorianCalendar.getTimeInMillis());
    }

    //i mesi vanno da 0 a 11
    public static int getNumGiorniFromMesi(int meseIniziale, int meseFinale) {

	Calendar cal = Calendar.getInstance();
	int numGiorni = 0;
	for (int i = meseIniziale; i < meseFinale + 1; i++) {
	    cal.set(Calendar.MONTH, i);
	    numGiorni = numGiorni + cal.getMaximum(Calendar.DAY_OF_MONTH);
	}

	return numGiorni;

    }

    public static Timestamp parseDataCompetenzaFineMese(String dataCompetenza) throws ParseException {
	GregorianCalendar gregorianCalendar = new GregorianCalendar();
	SimpleDateFormat simpleDateFormat = dataCompetenza.indexOf("-") != dataCompetenza.lastIndexOf("-") ?
					    new SimpleDateFormat(FORMATO_DATA) :
					    new SimpleDateFormat(FORMATO_DATA_COMPETENZA);
	simpleDateFormat.setLenient(false);
	gregorianCalendar.setTimeInMillis(simpleDateFormat.parse(dataCompetenza).getTime());
	gregorianCalendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
	gregorianCalendar.add(GregorianCalendar.MONTH, 1);
	gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -1);
	return new Timestamp(gregorianCalendar.getTimeInMillis());
    }

    /**
     * @deprecated
     */
    public static Timestamp parseDataCompetenza(String dataCompetenza) {
	GregorianCalendar gregorianCalendar = new GregorianCalendar();
	SimpleDateFormat simpleDateFormat = dataCompetenza.indexOf("-") != dataCompetenza.lastIndexOf("-") ?
					    new SimpleDateFormat(FORMATO_DATA) :
					    new SimpleDateFormat(FORMATO_DATA_COMPETENZA);
	simpleDateFormat.setLenient(false);
	try {
	    gregorianCalendar.setTimeInMillis(simpleDateFormat.parse(dataCompetenza).getTime());
	}
	catch (ParseException pe) {}
	return new Timestamp(gregorianCalendar.getTimeInMillis());
    }

    /**
     * @deprecated
     */
    public static Timestamp parseDate(String date) {
	GregorianCalendar gregorianCalendar = new GregorianCalendar();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DATA);
	simpleDateFormat.setLenient(false);
	try {
	    gregorianCalendar.setTimeInMillis(simpleDateFormat.parse(date).getTime());
	}
	catch (ParseException pe) {
	    return null;
	}
	return new Timestamp(gregorianCalendar.getTimeInMillis());
    }

    public static Timestamp parseDate(String date, String format) throws ParseException {
    	if (date.lastIndexOf(".") == date.length() - 2) {
    		date = date.substring(0, date.lastIndexOf("."));
    	}
    	if (date.length() != format.length()) {
    		throw new ParseException("", 0);
    	}
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setLenient(false);
        gregorianCalendar.setTimeInMillis(simpleDateFormat.parse(date).getTime());
        return new Timestamp(gregorianCalendar.getTimeInMillis());
    }

    /**
     * @deprecated
     */
    public static Timestamp parseDateYYYYMMDD(String date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DATA_YYYYMMDD);
        simpleDateFormat.setLenient(false);
        try {
            gregorianCalendar.setTimeInMillis(simpleDateFormat.parse(date).getTime());
        }
        catch (ParseException pe) {}
        return new Timestamp(gregorianCalendar.getTimeInMillis());
    }

    public static Timestamp aggiungiMesi(Timestamp day, int numMonths) {
	Calendar calendar = new GregorianCalendar();
	calendar.setTimeInMillis(day.getTime());
	calendar.add(Calendar.MONTH, numMonths);
	return new Timestamp(calendar.getTimeInMillis());
    }


	public static Calendar getPasqua(int anno) throws Exception {
		if ((anno < 1573) || (anno > 2499))
			throw new Exception( "Anno non corretto" );

		int a = anno % 19;
		int b = anno % 4;
		int c = anno % 7;

		int m = 0;
		int n = 0;

		if ((anno >= 1583) && (anno <= 1699)) {
			m = 22;
			n = 2;
		}
		if ((anno >= 1700) && (anno <= 1799)) {
			m = 23;
			n = 3;
		}
		if ((anno >= 1800) && (anno <= 1899)) {
			m = 23;
			n = 4;
		}
		if ((anno >= 1900) && (anno <= 2099)) {
			m = 24;
			n = 5;
		}
		if ((anno >= 2100) && (anno <= 2199)) {
			m = 24;
			n = 6;
		}
		if ((anno >= 2200) && (anno <= 2299)) {
			m = 25;
			n = 0;
		}
		if ((anno >= 2300) && (anno <= 2399)) {
			m = 26;
			n = 1;
		}
		if ((anno >= 2400) && (anno <= 2499)) {
			m = 25;
			n = 1;
		}

		int d = (19 * a + m) % 30;
		int e = (2 * b + 4 * c + 6 * d + n) % 7;

		Calendar calendar = new GregorianCalendar(anno,Calendar.MARCH,1);
		int mese = Calendar.MARCH;
		int giorno = d + e + 22;

		if (d + e >= 10) {
			mese =Calendar.APRIL;
			giorno = d + e - 9;
			//Eccezioni 25 e 26 aprile
			if (giorno == 26)
				giorno-=7;
			if (giorno == 25 && d ==28 && e == 6 && a > 10)
				giorno-=7;
		}
		calendar.set(Calendar.MONTH, mese);
		calendar.set(Calendar.DAY_OF_MONTH, giorno);

		return calendar;
	}

	public static Calendar getLunediDellAngelo(int anno) throws Exception {
		Calendar cal = getPasqua(anno);
		if ( cal != null)
			cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal;
	}
	
	public static Calendar buildCalendarOnStartDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	public static Calendar buildCalendarOnEndDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	public static String getTimestampAsString(Date date, String format) {
		String strDate = null;
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			strDate = sdf.format(date.getTime());
		}
		return strDate;
	}
	
	public static Timestamp buildTimestampOnLastDayOfMonth(Date date) {
		Timestamp dateOnLastDayOfMonth = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance(); 
			calendar.setTime(date);
			int lastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); 
			calendar.set(Calendar.DAY_OF_MONTH, lastDate);
			dateOnLastDayOfMonth = new Timestamp(calendar.getTime().getTime());
		}
		return dateOnLastDayOfMonth;
	}
	
	/**
	 * Ritorna il massimo valore possibile del Timestamp toDate 
	 * nello stesso giorno.
	 *  
	 * @param toDate
	 * @return Timestamp
	 */
	public static Timestamp ceilDay(Date toDate) {
		Timestamp ceil = null;
		Calendar to = Calendar.getInstance();
		to.setTime(toDate);
		to.set(Calendar.HOUR_OF_DAY, 23);
		to.set(Calendar.MINUTE, 59);
		to.set(Calendar.SECOND, 59);
		to.set(Calendar.SECOND, 59);
		to.set(Calendar.MILLISECOND, 999);
		ceil = new Timestamp(to.getTime().getTime());
		return ceil;
	}

	/**
	 * Ritorna il minimo valore possibile del Timestamp toDate 
	 * nello stesso giorno.
	 * 
	 * @param fromDate
	 * @return Timestamp
	 */
	public static Timestamp floorDay(Date fromDate) {
		Timestamp floor = null;
		Calendar from = Calendar.getInstance();
		from.setTime(fromDate);
		from.set(Calendar.HOUR_OF_DAY, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		floor = new Timestamp(from.getTime().getTime());
		return floor;
	}
	
	public static String getTimestampAsStringExport(Date date, String format) {
		String str = IGerivCleanerDateUtilities.getTimestampAsString(date, format);
		if (str == null) {
			str = IGerivCleanerStringUtility.getStringFixedLength(" ", format.length());
		}
		return str;
	}
	
	public static long getDifference(Calendar a, Calendar b, TimeUnit units) {
	    return units.convert(b.getTimeInMillis() - a.getTimeInMillis(), TimeUnit.MILLISECONDS);
	 }

	
}
