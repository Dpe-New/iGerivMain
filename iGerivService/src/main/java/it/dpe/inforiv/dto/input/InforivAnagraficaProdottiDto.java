package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.text.MessageFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Component("InforivAnagraficaProdottiDto")
@Scope("prototype")
public class InforivAnagraficaProdottiDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 156;
	private boolean isAnagraficaProdotto = true;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private String idProdotto;
	private Integer codiceTestata;
	private String numeroCopertina;
	private Date dataUsicta;
	private String titolo;
	private String sottotitolo;
	private String periodicita;
	private Float prezzoCopertina;
	private Float prezzoNetto;
	private Integer codiceTestataInforete;
	private Integer varianteProdottoInforete;
	private String numeroInforeteOAddon;
	private String barcode;
	private String tipoProdotto;
	private Integer segmentoMercato;
	private Integer codiceEditore;
	private Integer numeroUsciteOpera;
	private Integer giorniPermanenzaEdicola;
	private String scontoParticolare;

	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} else if (getCodFiegDl().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.codice.dl.nullo"));
		} else if (getIdProdotto().trim().equals("")) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.idtn.nullo"));
		} else if (getCodiceTestata().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.cpu.nullo"));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof InforivAnagraficaProdottiDto) {
			InforivAnagraficaProdottiDto dto = (InforivAnagraficaProdottiDto) obj;
			if (isAnagraficaProdotto()) {
				return this.getCodFiegDl().equals(dto.getCodFiegDl()) && this.getCodiceTestata().equals(dto.getCodiceTestata());
			} else {
				return this.getCodFiegDl().equals(dto.getCodFiegDl()) && this.getIdProdotto().equals(dto.getIdProdotto());
			}
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + ((getCodFiegDl() == null) ? 0 : getCodFiegDl());
		if (isAnagraficaProdotto()) {
			hash = 31 * hash + ((getCodiceTestata() == null) ? 0 : getCodiceTestata());
		} else {
			hash = 31 * hash + ((getIdProdotto() == null) ? 0 : getIdProdotto().hashCode());
		}
		return hash;
	}

}
