package it.dpe.igeriv.exchange.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class FloatAdapter extends XmlAdapter<String, Float> {
	
	public Float unmarshal(String number) throws Exception {
		return (number != null && !number.equals("")) ? new Float(number) : new Float(0);
	}

	public String marshal(Float number) throws Exception {
		return (number == null || number.equals(0f)) ? "0" : number.toString();
	}

}
