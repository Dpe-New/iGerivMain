package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.util.DPEWebContants;

import java.util.Iterator;

import org.extremecomponents.table.bean.Export;
import org.extremecomponents.table.bean.Table;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.CompactToolbar;
import org.extremecomponents.table.view.html.BuilderConstants;
import org.extremecomponents.table.view.html.BuilderUtils;
import org.extremecomponents.table.view.html.TableActions;
import org.extremecomponents.table.view.html.ToolbarBuilder;
import org.extremecomponents.table.view.html.toolbar.ImageItem;
import org.extremecomponents.table.view.html.toolbar.ToolbarItem;
import org.extremecomponents.util.HtmlBuilder;

/**
 * @author romanom
 *
 */
@SuppressWarnings("rawtypes")
public class DpeCompactToolbar extends CompactToolbar {

	public DpeCompactToolbar(HtmlBuilder html, TableModel model) {
		super(html, model);
	}
	
	@Override
	public void layout() {
        if (!showLayout(getTableModel())) {
            return;
        }
        
        Table table = getTableModel().getTableHandler().getTable();
        
        
		HtmlBuilder tab = getHtmlBuilder().table(2).border("0").cellPadding("0").cellSpacing("0").width("100%");
		if (table.getAttribute("toolbarClass") != null) {
			tab = tab.styleClass(table.getAttributeAsString("toolbarClass"));
		}
		if (table.getAttribute("footerStyle") != null) {
        	tab = tab.style(table.getAttributeAsString("footerStyle"));
        }
		tab.close();
		
		getHtmlBuilder().tr(3).close();

        // layout area left
        columnLeft(getHtmlBuilder(), getTableModel());
        
        // layout area center
        columnCenter(getHtmlBuilder(), getTableModel());

        // layout area right
        columnRight(getHtmlBuilder(), getTableModel());

        getHtmlBuilder().trEnd(3);
        getHtmlBuilder().tableEnd(2);
    }
	
	@Override
	protected void columnRight(HtmlBuilder html, TableModel model) {
		boolean filterable = BuilderUtils.filterable(model);
        boolean showPagination = BuilderUtils.showPagination(model);
        boolean showExports = BuilderUtils.showExports(model);

        ToolbarBuilder toolbarBuilder = new DpeToolbarBuilder(html, model);

        html.td(4).styleClass(BuilderConstants.COMPACT_TOOLBAR_CSS).align("right").close();
        
        html.table(4).border("0").cellPadding("1").cellSpacing("2").close();
        html.tr(5).close();
        
        if (showPagination) {
            html.td(5).close();
            toolbarBuilder.firstPageItemAsImage();
            html.tdEnd();

            html.td(5).close();
            toolbarBuilder.prevPageItemAsImage();
            html.tdEnd();

            html.td(5).close();
            toolbarBuilder.nextPageItemAsImage();
            html.tdEnd();

            html.td(5).close();
            toolbarBuilder.lastPageItemAsImage();
            html.tdEnd();

            html.td(5).close();
            toolbarBuilder.separator();
            html.tdEnd();

            html.td(5).close();
            toolbarBuilder.rowsDisplayedDroplist();
            html.tdEnd();

            if (showExports) {
                html.td(5).close();
                toolbarBuilder.separator();
                html.tdEnd();
            }
        }

        if (showExports) {
			Iterator iterator = model.getExportHandler().getExports().iterator();
            for (Iterator iter = iterator; iter.hasNext();) {
                html.td(5).close();
                Export export = (Export) iter.next();
                exportItemAsImage(export);
                html.tdEnd();
            }
        }

        if (filterable) {
            if (showExports || showPagination) {
                html.td(5).close();
                toolbarBuilder.separator();
                html.tdEnd();
            }

            html.td(5).close();
            toolbarBuilder.filterItemAsImage();
            html.tdEnd();

            html.td(5).close();
            toolbarBuilder.clearItemAsImage();
            html.tdEnd();
        }

        html.trEnd(5);

        html.tableEnd(4);

        html.tdEnd();
	}
	
	public void exportItemAsImage(Export export) {
        ImageItem item = null;
        if (getTableModel().getContext().getRequestAttribute("strOnSubmitFunction") != null && !getTableModel().getContext().getRequestAttribute("strOnSubmitFunction").toString().trim().equals("")) {
        	item = new BollaResaPdfImageItem();
        } else {
        	item = new ImageItem();
        }
        item.setTooltip(export.getTooltip());
        item.setImage(BuilderUtils.getImage(getTableModel(), export.getImageName()));
        item.setAlt(export.getText());
        item.setStyle("border:0");
        buildExport(getHtmlBuilder(), getTableModel(), item, export);
    }
	
	public static void buildExport(HtmlBuilder html, TableModel model, ToolbarItem item, Export export) {
        String action = new TableActions(model).getExportAction(export.getView(), export.getFileName());
        item.setAction(action);
        if (model.getContext().getRequestAttribute("strOnSubmitFunction") != null && !model.getContext().getRequestAttribute("strOnSubmitFunction").toString().trim().equals("")) {
	        String result =  model.getContext().getRequestAttribute("strOnSubmitFunction").toString() + ";";
	        result = result.replaceAll("strToReplace", item.getAction().replaceAll("javascript:", ""));
	        item.setAction(result);
        }
        item.enabled(html, model);
    }
	
	/**
	 * @param html
	 * @param model
	 */
	protected void columnCenter(HtmlBuilder html, TableModel model) {
        Object extraToolButton = model.getTableHandler().getTable().getAttribute("extraToolButton");
        Object extraToolButton1 = model.getTableHandler().getTable().getAttribute("extraToolButton1");
        Object extraToolButton2 = model.getTableHandler().getTable().getAttribute("extraToolButton2");
        Object extraToolButton3 = model.getTableHandler().getTable().getAttribute("extraToolButton3");
        Object extraToolButtonStyle = model.getTableHandler().getTable().getAttribute("extraToolButtonStyle");
        Object extraToolButton1Style = model.getTableHandler().getTable().getAttribute("extraToolButton1Style");
        Object extraToolButton2Style = model.getTableHandler().getTable().getAttribute("extraToolButton2Style");
        Object extraToolButton3Style = model.getTableHandler().getTable().getAttribute("extraToolButton3Style");
		//Object numMaxResaDimenticata = model.getContext().getSessionAttribute("numMaxCpuResaDimeticata");
		Boolean isBollaResa = model.getTableHandler().getTable() != null && model.getTableHandler().getTable().getAction() != null
				&& model.getTableHandler().getTable().getAction().contains("bollaResa");
		if (extraToolButton != null && !extraToolButton.equals(DPEWebContants.BLANK)) {
			if (isBollaResa) {
				/*if (numMaxResaDimenticata != null && (new Integer(numMaxResaDimenticata.toString()) > 0))*/ {
					HtmlBuilder td = html.td(1);
		        	td.styleClass(BuilderConstants.STATUS_BAR_CSS);
		        	if (extraToolButtonStyle != null) {
		        		td.style(extraToolButtonStyle.toString());
		        	}
		        	td.close();
		        	html.append(extraToolButton);
		        	html.tdEnd();
				}
			} else {
				HtmlBuilder td = html.td(1);
	        	td.styleClass(BuilderConstants.STATUS_BAR_CSS);
	        	if (extraToolButtonStyle != null) {
	        		td.style(extraToolButtonStyle.toString());
	        	}
	        	td.close();
	        	html.append(extraToolButton);
	        	html.tdEnd();
			}
		}
        if (extraToolButton1 != null && !extraToolButton1.equals(DPEWebContants.BLANK)) {
        	HtmlBuilder td = html.td(1);
        	td.styleClass(BuilderConstants.STATUS_BAR_CSS);
        	if (extraToolButton1Style != null) {
        		td.style(extraToolButton1Style.toString());
        	}
        	td.close();
        	html.append(extraToolButton1);
        	html.tdEnd();
		}
        if (extraToolButton2 != null && !extraToolButton2.equals(DPEWebContants.BLANK)) {
        	HtmlBuilder td = html.td(1);
        	td.styleClass(BuilderConstants.STATUS_BAR_CSS);
        	if (extraToolButton2Style != null) {
        		td.style(extraToolButton2Style.toString());
        	}
        	td.close();
        	html.append(extraToolButton2);
        	html.tdEnd();
		}
        if (extraToolButton3 != null && !extraToolButton3.equals(DPEWebContants.BLANK)) {
        	HtmlBuilder td = html.td(1);
        	td.styleClass(BuilderConstants.STATUS_BAR_CSS);
        	if (extraToolButton3Style != null) {
        		td.style(extraToolButton3Style.toString());
        	}
        	td.close();
        	html.append(extraToolButton3);
        	html.tdEnd();
		}
	}
	
}
