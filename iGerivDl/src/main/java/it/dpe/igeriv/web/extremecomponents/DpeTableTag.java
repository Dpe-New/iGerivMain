package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Table;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.tag.TableTag;

public class DpeTableTag extends TableTag {
	private static final long serialVersionUID = 1L;
	public String extraToolButton;
	public String extraToolButton1;
	public String extraToolButton2;
	public String extraToolButton3;
	public String extraToolButtonStyle;
	public String extraToolButton1Style;
	public String extraToolButton2Style;
	public String extraToolButton3Style;
	private String id;
	private String toolbarClass;
	private String footerStyle;
	
	public String getExtraToolButton() {
		return extraToolButton;
	}

	public void setExtraToolButton(String extraToolButton) {
		this.extraToolButton = extraToolButton;
	}
	
	public String getExtraToolButton1() {
		return extraToolButton1;
	}

	public void setExtraToolButton1(String extraToolButton1) {
		this.extraToolButton1 = extraToolButton1;
	}
	
	public String getExtraToolButton2() {
		return extraToolButton2;
	}

	public void setExtraToolButton2(String extraToolButton2) {
		this.extraToolButton2 = extraToolButton2;
	}

	public String getExtraToolButtonStyle() {
		return extraToolButtonStyle;
	}

	public void setExtraToolButtonStyle(String extraToolButtonStyle) {
		this.extraToolButtonStyle = extraToolButtonStyle;
	}

	public String getExtraToolButton1Style() {
		return extraToolButton1Style;
	}

	public void setExtraToolButton1Style(String extraToolButton1Style) {
		this.extraToolButton1Style = extraToolButton1Style;
	}
	
	public String getExtraToolButton2Style() {
		return extraToolButton2Style;
	}

	public void setExtraToolButton2Style(String extraToolButton2Style) {
		this.extraToolButton2Style = extraToolButton2Style;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getToolbarClass() {
		return toolbarClass;
	}

	public void setToolbarClass(String toolbarClass) {
		this.toolbarClass = toolbarClass;
	}

	public String getFooterStyle() {
		return footerStyle;
	}

	public void setFooterStyle(String footerStyle) {
		this.footerStyle = footerStyle;
	}
	
	public String getExtraToolButton3() {
		return extraToolButton3;
	}

	public void setExtraToolButton3(String extraToolButton3) {
		this.extraToolButton3 = extraToolButton3;
	}

	public String getExtraToolButton3Style() {
		return extraToolButton3Style;
	}

	public void setExtraToolButton3Style(String extraToolButton3Style) {
		this.extraToolButton3Style = extraToolButton3Style;
	}

	@Override
	public void addTableAttributes(TableModel model, Table table) {
		table.addAttribute("extraToolButton", extraToolButton);
		table.addAttribute("extraToolButton1", extraToolButton1);
		table.addAttribute("extraToolButton2", extraToolButton2);
		table.addAttribute("extraToolButton3", extraToolButton3);
		table.addAttribute("extraToolButtonStyle", extraToolButtonStyle);
		table.addAttribute("extraToolButton1Style", extraToolButton1Style);
		table.addAttribute("extraToolButton2Style", extraToolButton2Style);
		table.addAttribute("extraToolButton3Style", extraToolButton3Style);
		table.addAttribute("id", id);
		table.addAttribute("toolbarClass", toolbarClass);
		table.addAttribute("footerStyle", footerStyle);
		super.addTableAttributes(model, table);
	}
}
