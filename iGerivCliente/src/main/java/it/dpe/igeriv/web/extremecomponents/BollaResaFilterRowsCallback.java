package it.dpe.igeriv.web.extremecomponents;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.vo.BollaResa;

import java.util.Collection;

import org.extremecomponents.table.callback.FilterRowsCallback;
import org.extremecomponents.table.core.TableModel;

@SuppressWarnings({"unchecked","rawtypes"}) 
public class BollaResaFilterRowsCallback implements FilterRowsCallback {

	@Override
	public Collection filterRows(TableModel model, Collection rows) throws Exception {
		Boolean isExport = false;
		if (model.getContext().getParameter("ec_i") != null) {
			Boolean exportOnlyResoValorizzato = Boolean.valueOf(model.getContext().getParameter("exportOnlyResoValorizzato") != null ? model.getContext().getParameter("exportOnlyResoValorizzato") : "false");
			String param = model.getContext().getParameter(model.getContext().getParameter("ec_i") + "_ev");
			if (param != null && (param.toUpperCase().equals("PDF") || param.toUpperCase().equals("XLS")) && exportOnlyResoValorizzato) {
				isExport = true;
			}
		}
		if (isExport) {
			rows = select(rows, having(on(BollaResa.class).getReso(), notNullValue()).and(having(on(BollaResa.class).getReso(), not(equalTo(0)))));
		}
		return rows;
	}

}
