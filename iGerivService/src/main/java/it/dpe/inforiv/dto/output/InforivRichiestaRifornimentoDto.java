package it.dpe.inforiv.dto.output;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

/**
 * @author romanom
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Component("InforivRichiestaRifornimentoDto")
@Scope("prototype")
public class InforivRichiestaRifornimentoDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Integer codiceRichiesta;
	private String tipoMovimento;
	private Integer numeroSollecito;
	@Getter(AccessLevel.NONE)
	private String idProdotto;
	private Integer fornitoTotale;
	private Integer resaEsuberanzaExtra;
	private Integer giacenzaAttuale;
	private Integer copieRichieste;
	private String note;
	private Integer idtnTrascodifica;
	
	public String getIdProdotto() {
		return (idtnTrascodifica != null) ? idtnTrascodifica.toString() : idProdotto;
	}
	
}
