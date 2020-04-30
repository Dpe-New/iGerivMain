package it.dpe.inforiv.dto.input;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.exception.InvalidRecordException;

/**
 * @author romanom
 *
 */
@Component("InforivNullDto")
@Scope("prototype")
public class InforivNullDtoImpl implements InforivDto {

	@Override
	public String getTipoRecord() {
		return null;
	}

	@Override
	public Integer getCodEdicola() {
		return null;
	}

	@Override
	public Integer getCodFiegDl() {
		return null;
	}

	@Override
	public void validate(String riga) throws InvalidRecordException {
		
	}

}
