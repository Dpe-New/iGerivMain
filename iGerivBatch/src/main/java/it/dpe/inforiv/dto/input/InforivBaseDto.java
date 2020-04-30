package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.exception.InvalidRecordException;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class InforivBaseDto extends BaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private String record; 
	
	@Field(offset = 1, length = 2)
	public String getTipoRecord() {
		return tipoRecord != null ? tipoRecord.trim() : tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	
	@Field(offset = 3, length = 3)
	public Integer getCodFiegDl() {
		return codFiegDl;
	}

	public void setCodFiegDl(Integer codFiegDl) {
		this.codFiegDl = codFiegDl;
	}
	
	@Field(offset = 6, length = 4)
	public Integer getCodEdicola() {
		return codEdicola;
	}

	public void setCodEdicola(Integer codEdicola) {
		this.codEdicola = codEdicola;
	}
	
	public String getRecord() {
		return record != null ? record.trim() : record;
	}

	public void setRecord(String record) {
		this.record = record;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 != null) {
			return ((InforivBaseDto)arg0).getTipoRecord().equals(this.getTipoRecord());
		}
		return super.equals(arg0);
	}

	@Override
	public void validate(String riga) throws InvalidRecordException {
		setRecord(riga);
	}

}
