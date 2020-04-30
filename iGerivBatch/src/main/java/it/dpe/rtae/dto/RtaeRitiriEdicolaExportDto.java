package it.dpe.rtae.dto;


public class RtaeRitiriEdicolaExportDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long barcode;
	private String codiceEdizione;
	private Integer codFiegDl;
	private Integer codiceRivenditaDl;
	private Long copie;
	private Integer idEsportazione;

	public Long getBarcode() {
		return barcode;
	}

	public void setBarcode(Long barcode) {
		this.barcode = barcode;
	}
	
	public String getCodiceEdizione() {
		return codiceEdizione;
	}

	public void setCodiceEdizione(String codiceEdizione) {
		this.codiceEdizione = codiceEdizione;
	}

	public Integer getCodFiegDl() {
		return codFiegDl;
	}

	public void setCodFiegDl(Integer codFiegDl) {
		this.codFiegDl = codFiegDl;
	}

	public Integer getCodiceRivenditaDl() {
		return codiceRivenditaDl;
	}

	public void setCodiceRivenditaDl(Integer codiceRivenditaDl) {
		this.codiceRivenditaDl = codiceRivenditaDl;
	}

	public Long getCopie() {
		return copie;
	}

	public void setCopie(Long copie) {
		this.copie = copie;
	}

	public Integer getIdEsportazione() {
		return idEsportazione;
	}

	public void setIdEsportazione(Integer idEsportazione) {
		this.idEsportazione = idEsportazione;
	}
	
}
