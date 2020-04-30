package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EstrattoContoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Date dataEstrattoConto;
	private Integer numEstrattoConto;
	private Integer tipo;
	private String nomeFile;
	
	public String getDataEstrattoContoStr() {
		return (dataEstrattoConto != null) ? DateUtilities.getTimestampAsString(dataEstrattoConto, DateUtilities.FORMATO_DATA) : null;
	}

	public String getKey() {
		return getDataEstrattoContoStr() + "|" + getTipo();
	}
	
}
