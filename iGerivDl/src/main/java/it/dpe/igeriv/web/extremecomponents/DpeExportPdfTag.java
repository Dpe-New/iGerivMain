package it.dpe.igeriv.web.extremecomponents;

import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.bean.Export;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.tag.ExportPdfTag;
import org.extremecomponents.table.tag.TagUtils;
import org.extremecomponents.table.view.PdfView;
import org.extremecomponents.table.view.html.BuilderConstants;

public class DpeExportPdfTag extends ExportPdfTag {
	private static final long serialVersionUID = 1L;
	private Boolean isLandscape;
	private String headerBackgroundColor;
    private String headerTitle;
    private String totalRow;
    private String headerColor;
    private String headFontSize;
    private String regionBeforeExtentInches;
    private String marginTopInches;
    private String onSubmitFunction;
    private String repeatColumnHeaders;
    private String logoImage; 
    
	public Boolean getIsLandscape() {
		return isLandscape;
	}

	public void setIsLandscape(Boolean isLandscape) {
		this.isLandscape = isLandscape;
	}
	
	/**
     * @jsp.attribute description="The background color on the header column."
     *                required="false" rtexprvalue="true"
     */
    public void setHeaderBackgroundColor(String headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }

    /**
     * @jsp.attribute description="The font color for the header column."
     *                required="false" rtexprvalue="true"
     */
    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }

    /**
     * @jsp.attribute description="The title displayed at the top of the page."
     *                required="false" rtexprvalue="true"
     */
    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }
    
	public String getHeadFontSize() {
		return headFontSize;
	}

	public void setHeadFontSize(String headFontSize) {
		this.headFontSize = headFontSize;
	}

	public String getRegionBeforeExtentInches() {
		return regionBeforeExtentInches;
	}
	
	public String getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(String totalRow) {
		this.totalRow = totalRow;
	}

	public void setRegionBeforeExtentInches(String regionBeforeExtentInches) {
		this.regionBeforeExtentInches = regionBeforeExtentInches;
	}

	public String getMarginTopInches() {
		return marginTopInches;
	}

	public void setMarginTopInches(String marginTopInches) {
		this.marginTopInches = marginTopInches;
	}
	
	public String getOnSubmitFunction() {
		return onSubmitFunction;
	}

	public void setOnSubmitFunction(String onSubmitFunction) {
		this.onSubmitFunction = onSubmitFunction;
	}
	
	public String getRepeatColumnHeaders() {
		return repeatColumnHeaders;
	}

	public void setRepeatColumnHeaders(String repeatColumnHeaders) {
		this.repeatColumnHeaders = repeatColumnHeaders;
	}
	
	public String getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	@Override
	public void addExportAttributes(TableModel model, Export export) {
		if (StringUtils.isBlank(export.getView())) {
            export.setView(TableConstants.VIEW_PDF);
        }

        if (StringUtils.isBlank(export.getViewResolver())){
            export.setViewResolver(TableConstants.VIEW_PDF);
        }

        if (StringUtils.isBlank(export.getImageName())) {
            export.setImageName(TableConstants.VIEW_PDF);
        }
        
        if (StringUtils.isBlank(export.getText())) {
            export.setText(BuilderConstants.TOOLBAR_PDF_TEXT);
        }

        export.addAttribute(PdfView.HEADER_BACKGROUND_COLOR, TagUtils.evaluateExpressionAsString("headerBackgroundColor", headerBackgroundColor, this, pageContext));
        export.addAttribute(PdfView.HEADER_COLOR, TagUtils.evaluateExpressionAsString("headerColor", headerColor, this, pageContext));
        if (headerTitle.contains(".")) {
			String message = model.getMessages().getMessage(headerTitle);
			if (message != null) {
				message = message.replaceAll("&nbsp;", " ");
			}
			this.headerTitle = message;
		}
        export.addAttribute(PdfView.HEADER_TITLE, TagUtils.evaluateExpressionAsString("headerTitle", headerTitle, this, pageContext));
        export.addAttribute("headFontSize", TagUtils.evaluateExpressionAsString("headFontSize", headFontSize, this, pageContext));
        export.addAttribute(DpePdfView.TOTAL_ROW, TagUtils.evaluateExpressionAsString(DpePdfView.TOTAL_ROW, totalRow, this, pageContext));
        export.addAttribute("isLandscape", isLandscape);
        export.addAttribute("regionBeforeExtentInches", regionBeforeExtentInches);
        export.addAttribute("marginTopInches", marginTopInches);
        export.addAttribute("onSubmitFunction", onSubmitFunction);
        export.addAttribute("repeatColumnHeaders", repeatColumnHeaders);
        export.addAttribute("logoImage", logoImage);
	}

}
