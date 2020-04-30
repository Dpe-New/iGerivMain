package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.text.MessageFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	
	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} else if (getCodFiegDl().equals(0)) {
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
