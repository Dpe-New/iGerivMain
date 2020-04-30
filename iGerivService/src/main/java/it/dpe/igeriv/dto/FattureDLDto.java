package it.dpe.igeriv.dto;

import java.sql.Date;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.FattureEdicolaPdfVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false, of={"dataFattura", "numeroFattura"})
public class FattureDLDto extends BaseDto{
	private static final long serialVersionUID = 1L;
	private Date dataFattura;
	private Integer numeroFattura;
	private String nomeFile;
	
	public FattureDLDto() {
		this(null);
	}
	public FattureDLDto(FattureEdicolaPdfVo pdfVO) {
		if (pdfVO!=null) {
			this.dataFattura=pdfVO.getPk().getDataFattura();
			this.numeroFattura=pdfVO.getPk().getNumeroFattura();
			this.nomeFile=pdfVO.getNomeFile();
		}
	}	
	
	public String getDataNumeroFatturaStr() {
		try {
			return dataFattura==null ? null : String.format("%td-%<tm-%<tY N. %d", dataFattura, numeroFattura);
		} catch (Exception e) {
			return null;
		}
	}

	public String getKey() {
		try {
			return String.format("%td-%<tm-%<tY|%d", dataFattura, numeroFattura);
		} catch (Exception e) {
			return null;
		}
	}
	
	
}
