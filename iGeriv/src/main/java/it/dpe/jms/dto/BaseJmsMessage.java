package it.dpe.jms.dto;

public class BaseJmsMessage implements JmsMessage {
	private static final long serialVersionUID = 8773737950026018075L;
	private String tipoMessaggio;
	private String msg;
	
	public BaseJmsMessage() {}
	
	public BaseJmsMessage(String msg) {
		this.msg = msg;
	}
	
	public String getTipoMessaggio() {
		return tipoMessaggio;
	}

	public void setTipoMessaggio(String tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
