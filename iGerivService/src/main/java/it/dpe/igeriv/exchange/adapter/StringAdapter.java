package it.dpe.igeriv.exchange.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringAdapter extends XmlAdapter<String, String> {

	public String unmarshal(String str) throws Exception {
		return (str != null && str.trim().equals("")) ? "" : (str != null ? str.trim() : "");
	}

	public String marshal(String str) throws Exception {
		return (str == null) ? "" : str.trim();
	}

}
