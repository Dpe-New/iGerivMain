package it.dpe.igeriv.web.extremecomponents;

import java.math.BigDecimal;
import java.util.Collection;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.calc.CalcHandler;
import org.extremecomponents.table.calc.CalcUtils;
import org.extremecomponents.table.calc.TotalCalc;
import org.extremecomponents.table.core.TableModel;

@SuppressWarnings("rawtypes")
public class DpeTotalCalc extends TotalCalc {
	
	@Override
	public Number getCalcResult(TableModel model, Column column) {
		Collection rows = model.getCollectionOfFilteredBeans();
        String property = column.getProperty();
        TotalValue totalValue = new TotalValue();
        CalcUtils.eachRowCalcValue(totalValue, rows, property);
        return totalValue.getTotalValue();
	}
	
	private static class TotalValue implements CalcHandler {
        BigDecimal total = new BigDecimal(0);
        
        public void processCalcValue(Number calcValue) {
        	if (calcValue instanceof BigDecimal) {
        		total = total.add((BigDecimal) calcValue);
        	} else {
        		total = total.add(new BigDecimal(calcValue.doubleValue()));
        	}
        }
        
        public Number getTotalValue() {
            return total;
        }
    }
}
