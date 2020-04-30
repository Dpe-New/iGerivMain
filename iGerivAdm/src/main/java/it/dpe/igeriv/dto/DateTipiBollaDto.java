package it.dpe.igeriv.dto;



public class DateTipiBollaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer idDataTipoBolla;
	private String dataBollaFormat;
	private String tipoBolla;
	private Boolean readonly;
	private Integer gruppoSconto;
	private Integer bollaTrasmessaDl;

	public Integer getIdDataTipoBolla() {
		return idDataTipoBolla;
	}

	public void setIdDataTipoBolla(Integer idDataTipoBolla) {
		this.idDataTipoBolla = idDataTipoBolla;
	}
	
	public String getDataBollaFormat() {
		return dataBollaFormat;
	}

	public void setDataBollaFormat(String dataBollaFormat) {
		this.dataBollaFormat = dataBollaFormat;
	}

	public String getTipoBolla() {
		return tipoBolla;
	}

	public void setTipoBolla(String tipoBolla) {
		this.tipoBolla = tipoBolla;
	}

	public Boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public Integer getGruppoSconto() {
		return gruppoSconto;
	}

	public void setGruppoSconto(Integer gruppoSconto) {
		this.gruppoSconto = gruppoSconto;
	}

	public void setBollaTrasmessaDl(Integer bollaTrasmessaDl) {
		this.bollaTrasmessaDl = bollaTrasmessaDl;
	}

	public Integer getBollaTrasmessaDl() {
		return bollaTrasmessaDl;
	}
	
}
