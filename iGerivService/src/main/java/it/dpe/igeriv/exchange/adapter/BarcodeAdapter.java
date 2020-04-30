package it.dpe.igeriv.exchange.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BarcodeAdapter extends XmlAdapter<String, Long> {
	
	public Long unmarshal(String number) throws Exception {
		return (number != null && !number.equals("")) ? new Long(number) : new Long(0);
	}

	public String marshal(Long number) throws Exception {
		return (number == null || number.equals(0l)) ? "0" : (number.toString().length() >= 13) ? number.toString().substring(0, 13) : number.toString().substring(0, number.toString().length());
	}

}
