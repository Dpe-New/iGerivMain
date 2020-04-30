package it.dpe.igeriv.web.struts.converter;

import it.dpe.igeriv.util.DateUtilities;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

import com.google.common.base.Strings;

@SuppressWarnings("rawtypes")
public class UtilDateConverter extends StrutsTypeConverter {
	private final Logger log = Logger.getLogger(getClass());
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (values != null && values.length > 0 && !Strings.isNullOrEmpty(values[0])) {
			try {
				return DateUtilities.parseDate(values[0], DateUtilities.FORMATO_DATA_SLASH_HHMMSS);
			} catch (ParseException e) {
				try {
					return DateUtilities.parseDate(values[0], DateUtilities.FORMATO_DATA_SLASH);
				} catch (ParseException e1) {
					log.error("Errore di conversione data da stringa a oggetto", e1);
					return values[0];
				}
	        }
		}
		return null;
	}

	@Override
	public String convertToString(Map context, Object o) {
		if (o != null) {
			try {
				return DateUtilities.getTimestampAsString((Date) o, DateUtilities.FORMATO_DATA_SLASH_HHMMSS);
			} catch (Throwable e) {
				try {
					return DateUtilities.getTimestampAsString((Date) o, DateUtilities.FORMATO_DATA_SLASH);
				} catch (Throwable e1) {
					log.error("Errore di conversione data da oggetto a stringa", e1);
					return null;
				}	
			}
		}
		return null;
	}

}
