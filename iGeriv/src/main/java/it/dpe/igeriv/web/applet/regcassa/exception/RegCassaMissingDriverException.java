package it.dpe.igeriv.web.applet.regcassa.exception;

public class RegCassaMissingDriverException extends Exception {
	private static final long serialVersionUID = 1L;
	private String driverName;
	
	public RegCassaMissingDriverException(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverName() {
		return driverName;
	}
	
}
