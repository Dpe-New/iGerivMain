package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.text.MessageFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InforivEditoreDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 38;
	private String tipoRecord;
	private Integer codEditore;
	private String descrizione;
	
	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof InforivEditoreDto) {
			InforivEditoreDto dto = (InforivEditoreDto) obj;
			return this.getCodEditore().equals(dto.getCodEditore());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + ((getCodEditore() == null) ? 0 : getCodEditore());
		return hash;
	}
}
