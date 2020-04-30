package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.toolbar.ImageItem;
import org.extremecomponents.util.HtmlBuilder;

public class BollaResaPdfImageItem extends ImageItem {
	
	@Override
	public void enabled(HtmlBuilder html, TableModel model) {
        boolean showTooltips = model.getTableHandler().getTable().isShowTooltips();
        if (showTooltips) {
            html.img().src(getImage()).onclick(getAction()).style(getStyle()).title(getTooltip()).onmouseover(getOnmouseover()).onmouseout(getOnmouseout()).alt(getAlt()).xclose();
        } else {
            html.img().src(getImage()).onclick(getAction()).style(getStyle()).onmouseover(getOnmouseover()).onmouseout(getOnmouseout()).alt(getAlt()).xclose();
        }
	}
	
}
