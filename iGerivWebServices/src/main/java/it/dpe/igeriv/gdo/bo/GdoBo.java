package it.dpe.igeriv.gdo.bo;

import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.gdo.dto.GdoBolReplyDto;
import it.dpe.igeriv.gdo.dto.GdoVenditaDto;
import it.dpe.igeriv.gdo.dto.GdoVenditaReplyDto;
import it.dpe.igeriv.security.UserAbbonato;

import java.util.Date;
import java.util.List;

/**
 * @author romanom
 * 
 */
public interface GdoBo {
	
	public GdoBolReplyDto getDatiBollaGdo(UserAbbonato user, Date dataBolla);
	
	public GdoVenditaReplyDto importDatiVenditeGdo(UserAbbonato user, List<GdoVenditaDto> list);

	public void validateRichiestaDati(UserAbbonato user, Date dataBolla) throws IGerivBusinessException;
}
