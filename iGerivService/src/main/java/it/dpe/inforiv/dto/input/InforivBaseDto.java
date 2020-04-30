package it.dpe.inforiv.dto.input;

import lombok.Getter;
import lombok.Setter;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.exception.InvalidRecordException;

@Getter
@Setter
public class InforivBaseDto extends BaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private String record; 
	
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
