package it.dpe.igeriv.exchange.adapter;

import java.math.BigDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {
	
	public BigDecimal unmarshal(String number) throws Exception {
		return (number != null && !number.equals("")) ? new BigDecimal(number) : new BigDecimal(0);
	}

	public String marshal(BigDecimal number) throws Exception {
		return (number == null || number.equals(new BigDecimal(0))) ? "0" : number.toString();
	}

}
