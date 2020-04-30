package it.dpe.igeriv.web.extremecomponents.filter;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collection;

import org.extremecomponents.table.callback.FilterRowsCallback;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.EstrattoContoDinamicoDto;

public class EstrattoContoDinamicoFilterRows implements FilterRowsCallback {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<EstrattoContoDinamicoDto> filterRows(TableModel model, @SuppressWarnings("rawtypes") Collection rows) throws Exception {
		return select(rows, having(on(EstrattoContoDinamicoDto.class).isCalcRow(), equalTo(false)));
	}

}
