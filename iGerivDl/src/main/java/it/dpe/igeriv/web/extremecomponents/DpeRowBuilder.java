package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.FondoBollaDettaglioDto;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.RowBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.google.common.base.Strings;

public class DpeRowBuilder extends RowBuilder {

	public DpeRowBuilder(HtmlBuilder html, TableModel model) {
		super(html, model);
	}
	
	@Override
	public void rowStart() {
		getHtmlBuilder().tr(1);
        styleClass();
        style();
        onclick();
        onmouseover();
        onmouseout();
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("pk"))) {
        	getHtmlBuilder().append(" pk='" + getRow().getAttributeAsString("pk") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("cddif"))) {
        	getHtmlBuilder().append(" cddif='" + getRow().getAttributeAsString("cddif") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("scdif"))) {
        	getHtmlBuilder().append(" scdif='" + getRow().getAttributeAsString("scdif") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("href1"))) {
        	getHtmlBuilder().append(" divParam='" + getRow().getAttributeAsString("href1") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("iv"))) {
        	getHtmlBuilder().append(" iv='" + getRow().getAttributeAsString("iv") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("fb"))) {
        	getHtmlBuilder().append(" fb='" + getRow().getAttributeAsString("fb") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("prodottoDl"))) {
        	getHtmlBuilder().append(" prodottoDl='" + getRow().getAttributeAsString("prodottoDl") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("dpa"))) {
        	getHtmlBuilder().append(" dpa='" + getRow().getAttributeAsString("dpa") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("resp"))) {
        	getHtmlBuilder().append(" resp='" + getRow().getAttributeAsString("resp") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("idtn"))) {
        	getHtmlBuilder().append(" idtn='" + getRow().getAttributeAsString("idtn") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("pv"))) {
        	getHtmlBuilder().append(" pv='" + getRow().getAttributeAsString("pv") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("fbcd"))) {
        	getHtmlBuilder().append(" fbcd='" + getRow().getAttributeAsString("fbcd") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("qfdv"))) {
        	getHtmlBuilder().append(" qfdv='" + getRow().getAttributeAsString("qfdv") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("an"))) {
        	getHtmlBuilder().append(" an='" + getRow().getAttributeAsString("an") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("an1"))) {
        	getHtmlBuilder().append(" an1='" + getRow().getAttributeAsString("an1") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("an2"))) {
        	getHtmlBuilder().append(" an2='" + getRow().getAttributeAsString("an2") + "'");
        }
        if (!Strings.isNullOrEmpty(getRow().getAttributeAsString("rigaEvasione"))) {
        	getHtmlBuilder().append(" rigaEvasione='" + getRow().getAttributeAsString("rigaEvasione") + "'");
        }
        getHtmlBuilder().close();
	}
	
	@Override
	public void styleClass() {
		super.styleClass();
		Object currentRowBean = getTableModel().getCurrentRowBean();
        if (currentRowBean instanceof FondoBollaDettaglioDto) {
			String style = (getRow().getStyle().indexOf(";background-color") != -1) ? getRow().getStyle().substring(0, getRow().getStyle().indexOf(";background-color")) : getRow().getStyle();
			if (getTableModel().getRowHandler().isRowEven()) {
				getRow().setStyle(style + ";background-color:#ffcc99;");
			} else {
				getRow().setStyle(style + ";background-color:#ffffcc;");
			}
		} 
	}

}
