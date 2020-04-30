package it.dpe.inforiv.dto.output;

import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Component("InforivMancanzeEccedenzeDto")
@Scope("prototype")
public class InforivMancanzeEccedenzeDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	@Getter(AccessLevel.NONE)
	private String tipoMovimento;
	@Getter(AccessLevel.NONE)
	private String idProdotto;
	private Integer fornito;
	@Getter(AccessLevel.NONE)
	private Integer copieMancantiEccedenti;
	private String note;
	private Integer differenze;
	private String idtnTrascodifica;
	
	public String getTipoMovimento() {
		if (getDifferenze() != null && getDifferenze() > 0) {
			return "E";
		} else if (getDifferenze() != null && getDifferenze() < 0) {
			return "M";
		}
		return tipoMovimento;
	}

	public String getIdProdotto() {
		return (idtnTrascodifica != null) ? idtnTrascodifica.toString() : idProdotto;
	}

	public Integer getCopieMancantiEccedenti() {
		return copieMancantiEccedenti = Math.abs(getDifferenze());
	}

}
