package it.dpe.igeriv.web.extremecomponents;

import org.apache.log4j.Logger;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;

public class DpeViewImageCell extends DisplayCell {
	private final Logger log = Logger.getLogger(getClass());
	
	
	@Override
	protected String getCellValue(TableModel model, Column column) {
		StringBuilder val = new StringBuilder("");
		try {
			Boolean isViewImg = (Boolean) model.getContext().getSessionAttribute("viewImageByProfile");
			String path_img = (String) column.getPropertyValue();
			
			// 14/10/2016 
			//ERROR it.dpe.igeriv.web.extremecomponents.DpeViewImageCell.getCellValue(DpeViewImageCell.java:33)                                                             - Errore in DpeViewImageCell
			//java.lang.NullPointerException at it.dpe.igeriv.web.extremecomponents.DpeViewImageCell.getCellValue(DpeViewImageCell.java:24)
			isViewImg =(isViewImg==null)?false:isViewImg;
			
			if(isViewImg && path_img !=null && !path_img.equals("") ){
				val.append("<a href=\"/immagini/"+path_img+"\" rel=\"thumbnail\"><img src=\"/app_img/camera-active.png\" width=\"16px\""
						+ "	height=\"16px\" border=\"0\" style=\"border-style: none\" /></a>");
			}else if(!isViewImg && path_img !=null && !path_img.equals("") ){
				val.append("<img src=\"/app_img/camera-disab.png\" width=\"16px\""
						+ "	height=\"16px\" border=\"0\" style=\"border-style: none\" />");
			}

		} catch (Exception e) {
			log.error("Errore in DpeViewImageCell", e);
		}
		return val.toString();
	}
}
