package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;

public class RispostaRichiestaRifornimentoDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 75;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Integer codiceRichiesta;
	private String tipoMovimento;
	private String idProdotto;
	private Date dataRipetizioneRichiesta;
	private Integer causaleMancataEvasione;
	private String note;
	
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
	
	@Field(offset = 10, length = 9, paddingChar = '0')
	public Integer getCodiceRichiesta() {
		return codiceRichiesta;
	}

	public void setCodiceRichiesta(Integer codiceRichiesta) {
		this.codiceRichiesta = codiceRichiesta;
	}
	
	@Field(offset = 19, length = 1)
	public String getTipoMovimento() {
		return tipoMovimento != null ? tipoMovimento.trim() : tipoMovimento;
	}

	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	@Field(offset = 20, length = 18)
	public String getIdProdotto() {
		return idProdotto != null ? idProdotto.trim() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 38, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataRipetizioneRichiesta() {
		return dataRipetizioneRichiesta;
	}

	public void setDataRipetizioneRichiesta(Date dataRipetizioneRichiesta) {
		this.dataRipetizioneRichiesta = dataRipetizioneRichiesta;
	}
	
	@Field(offset = 46, length = 1)
	public Integer getCausaleMancataEvasione() {
		return causaleMancataEvasione;
	}

	public void setCausaleMancataEvasione(Integer causaleMancataEvasione) {
		this.causaleMancataEvasione = causaleMancataEvasione;
	}
	
	@Field(offset = 47, length = 30)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		/*if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} else*/ if (getCodFiegDl().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.codice.dl.nullo"));
		} 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof RispostaRichiestaRifornimentoDto) {
			RispostaRichiestaRifornimentoDto dto = (RispostaRichiestaRifornimentoDto) obj;
			return this.getCodFiegDl().equals(dto.getCodFiegDl()) && this.getIdProdotto().equals(dto.getIdProdotto()) && this.getCodiceRichiesta().equals(dto.getCodiceRichiesta());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + ((getCodFiegDl() == null) ? 0 : getCodFiegDl());
		hash = 31 * hash + ((getIdProdotto() == null) ? 0 : getIdProdotto().hashCode());
		hash = 31 * hash + ((getCodiceRichiesta() == null) ? 0 : getCodiceRichiesta().hashCode());
		return hash;
	}
}
