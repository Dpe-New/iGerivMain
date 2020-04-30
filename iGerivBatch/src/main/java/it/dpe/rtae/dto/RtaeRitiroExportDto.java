package it.dpe.rtae.dto;

import java.sql.Timestamp;
import java.util.List;

public class RtaeRitiroExportDto extends BaseDto {
	private static final long serialVersionUID = -1351395438150542397l;
	private Integer idEsportazione;
	private Timestamp dataEsportazione;
	private Integer statoEsportazione;
	private String descrizioneErrore;
	private List<RtaeRitiriEdicolaExportDto> listGiornaleRitiriDto;
 
	public Integer getIdEsportazione() {
		return idEsportazione;
	}

	public void setIdEsportazione(Integer idEsportazione) {
		this.idEsportazione = idEsportazione;
	}

	public Timestamp getDataEsportazione() {
		return dataEsportazione;
	}

	public void setDataEsportazione(Timestamp dataEsportazione) {
		this.dataEsportazione = dataEsportazione;
	}

	public Integer getStatoEsportazione() {
		return statoEsportazione;
	}

	public void setStatoEsportazione(Integer statoEsportazione) {
		this.statoEsportazione = statoEsportazione;
	}

	public String getDescrizioneErrore() {
		return descrizioneErrore;
	}

	public void setDescrizioneErrore(String descrizioneErrore) {
		this.descrizioneErrore = descrizioneErrore;
	}

	public List<RtaeRitiriEdicolaExportDto> getListGiornaleRitiriDto() {
		return listGiornaleRitiriDto;
	}

	public void setListGiornaleRitiriDto(List<RtaeRitiriEdicolaExportDto> listGiornaleRitiriDto) {
		this.listGiornaleRitiriDto = listGiornaleRitiriDto;
	}

}
