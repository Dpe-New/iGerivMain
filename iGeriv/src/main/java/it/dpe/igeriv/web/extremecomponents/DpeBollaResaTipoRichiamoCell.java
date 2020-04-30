package it.dpe.igeriv.web.extremecomponents;

import java.text.MessageFormat;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.BollaResaBaseDto;
import it.dpe.igeriv.util.IGerivConstants;

public class DpeBollaResaTipoRichiamoCell extends DisplayCell {

	@Override
	public String getExportDisplay(TableModel model, Column column) {
		BollaResaBaseDto dto = (BollaResaBaseDto) model.getCurrentRowBean();
		boolean hasRichResa = dto.getTipoRichiamo() != null && !Strings.isNullOrEmpty(dto.getTipoRichiamo().trim());
		return (hasRichResa ? dto.getTipoRichiamo().trim() : "") 
			+ (!Strings.isNullOrEmpty(dto.getNoteRivendita().trim()) ? (hasRichResa ? " | " : "")  + dto.getNoteRivendita().trim() : "");
	}

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        HtmlBuilder htmlBuilder = columnBuilder.getHtmlBuilder();
        htmlBuilder.span().styleClass("richiamoResaCls").close();
    	htmlBuilder.append(getCellValue(model, column));
    	htmlBuilder.spanEnd();
        BollaResaBaseDto dto = (BollaResaBaseDto) model.getCurrentRowBean();
        if (dto.getOrdini() != null && dto.getOrdini() > 0) {
        	htmlBuilder.nbsp();
        	htmlBuilder.span()
        		.styleClass("ordiniCls")
        		.style("cursor:pointer")
        		.title(dto.getOrdini() > 1 ? MessageFormat.format(model.getMessages().getMessage("igeriv.x.copie.ordinate.clienti"), dto.getOrdini()) : MessageFormat.format(model.getMessages().getMessage("igeriv.x.copia.ordinata.clienti"), dto.getOrdini()))
        		.close();
        	htmlBuilder.img("/app_img/ordini_clienti_small.png", null)
        		.onclick("openOrdini('" + dto.getIdtn() + "')")
        		.xclose();
        	htmlBuilder.spanEnd();
        }
        if (!Strings.isNullOrEmpty(dto.getBarcode()) && dto.getBarcode().startsWith(IGerivConstants.PREFIX_BARCODE_FITTIZIO)) {
        	htmlBuilder.nbsp();
        	htmlBuilder.img("/app_img/avviso_barcode_fittizio.png", null)
        		.styleClass("noBarcode")
        		.title(model.getMessages().getMessage("igeriv.message.codice.barre.non.compatibile"))
	    		.xclose();
        }
        
        columnBuilder.tdEnd();
        return columnBuilder.toString();
	}

}
