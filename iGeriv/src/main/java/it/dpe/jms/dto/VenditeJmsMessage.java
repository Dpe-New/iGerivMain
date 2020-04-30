package it.dpe.jms.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VenditeJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = -3529805648703357546L;
	private Calendar calendar = Calendar.getInstance();
	private Integer idtn;
	private Timestamp dataVendita;
	private BigDecimal totaleImporto;
	private Integer copieProdotto;
	private BigDecimal prezzoProdotto;
	private String descrizione;
	
	public VenditeJmsMessage(Integer idtn, Timestamp dataVendita, BigDecimal totaleImporto, Integer copieProdotto, BigDecimal prezzoProdotto, String descrizione) {
		this.idtn = idtn;
		this.dataVendita = dataVendita;
		this.totaleImporto = totaleImporto;
		this.copieProdotto = copieProdotto;
		this.prezzoProdotto = prezzoProdotto;
		this.descrizione = descrizione;
	}
	
	public Date getDataVenditaTrunc() {
		return getDataVendita() != null ? new Date(getDataVendita().getTime()) : null;
	}
	
	public Integer getOraVendita() {
		calendar.setTime(getDataVendita());
		String hours = "" + calendar.get(Calendar.HOUR_OF_DAY);
		String minutes = "" +  calendar.get(Calendar.MINUTE);
		return getDataVendita() != null ? new Integer(hours + minutes) : null;
	}
}
