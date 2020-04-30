package it.dpe.igeriv.web.struts.converter;

import java.util.Date;
import java.util.Map;

public class TimestampConverter extends UtilDateConverter {
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		Date date = (Date) super.convertFromString(context, values, toClass);
		return new java.sql.Timestamp(date.getTime());
	}
}
