package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;

import com.ancientprogramming.fixedformat4j.annotation.Field;

public class InforivEditoreDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 38;
	private String tipoRecord;
	private Integer codEditore;
	private String descrizione;
	
	@Field(offset = 1, length = 2)
	public String getTipoRecord() {
		return tipoRecord != null ? tipoRecord.trim() : tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	
	@Field(offset = 3, length = 7)
	public Integer getCodEditore() {
		return codEditore;
	}

	public void setCodEditore(Integer codEditore) {
		this.codEditore = codEditore;
	}
	
	@Field(offset = 9, length = 30)
	public String getDescrizione() {
		return descrizione != null ? descrizione.trim() : descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		/*if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} */
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
