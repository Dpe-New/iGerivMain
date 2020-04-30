package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.tag.RowTag;

public class DpeRowTag extends RowTag {
	private static final long serialVersionUID = 1L;
	private String href;
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	@Override
	public void addRowAttributes(TableModel model, Row row) {
		row.addAttribute("href", href);
		super.addRowAttributes(model, row);
    }   
}
