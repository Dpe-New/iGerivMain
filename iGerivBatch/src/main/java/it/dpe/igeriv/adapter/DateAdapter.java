package it.dpe.igeriv.adapter;

import it.dpe.igeriv.util.DateUtilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {
	DateFormat df = new SimpleDateFormat(DateUtilities.FORMATO_DATA_SLASH);

	public Date unmarshal(String date) throws Exception {
		return df.parse(date);
	}

	public String marshal(Date date) throws Exception {
		return df.format(date);
	}

}
