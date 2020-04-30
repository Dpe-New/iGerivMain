package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;

public interface InforivDto {
	public String getTipoRecord();
	
	public Integer getCodEdicola();
	
	public Integer getCodFiegDl();
	
	public void validate(String riga) throws InvalidRecordException;
}
