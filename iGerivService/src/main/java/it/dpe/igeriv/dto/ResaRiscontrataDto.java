package it.dpe.igeriv.dto;

import it.dpe.igeriv.vo.BollaResaRiassunto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ResaRiscontrataDto extends BaseDto implements BollaResaRiassunto {
	private static final long serialVersionUID = 1L;
	private Timestamp dtBolla;
	private String tipoBolla;

	@Override
	public Integer getGruppoSconto() {
		return 1;
	}

	@Override
	public Integer getBollaTrasmessaDl() {
		return null;
	}

}
