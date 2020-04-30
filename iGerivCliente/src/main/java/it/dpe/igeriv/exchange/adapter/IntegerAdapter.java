package it.dpe.igeriv.exchange.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String, Integer> {

	public Integer unmarshal(String number) throws Exception {
		return (number != null && !number.equals("")) ? new Integer(number) : new Integer(0);
	}

	public String marshal(Integer number) throws Exception {
		return (number == null || number.equals(0)) ? "0" : number.toString();
	}

}
