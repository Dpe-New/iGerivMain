package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Table;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.tag.TableTag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DpeTableTag extends TableTag {
	private static final long serialVersionUID = 1L;
	private String extraToolButton;
	private String extraToolButton1;
	private String extraToolButton2;
	private String extraToolButton3;
	private String extraToolButton4;
	private String extraToolButton5;
	private String extraToolButtonStyle;
	private String extraToolButton1Style;
	private String extraToolButton2Style;
	private String extraToolButton3Style;
	private String extraToolButton4Style;
	private String extraToolButton5Style;
	private String id;
	private String toolbarClass;
	private String footerStyle;
	private String beforeUnloadValidationScript;
	private String minRowsDisplayed;
	
	@Override
	public void addTableAttributes(TableModel model, Table table) {
		table.addAttribute("extraToolButton", extraToolButton);
		table.addAttribute("extraToolButton1", extraToolButton1);
		table.addAttribute("extraToolButton2", extraToolButton2);
		table.addAttribute("extraToolButton3", extraToolButton3);
		table.addAttribute("extraToolButton4", extraToolButton4);
		table.addAttribute("extraToolButton5", extraToolButton5);
		table.addAttribute("extraToolButtonStyle", extraToolButtonStyle);
		table.addAttribute("extraToolButton1Style", extraToolButton1Style);
		table.addAttribute("extraToolButton2Style", extraToolButton2Style);
		table.addAttribute("extraToolButton3Style", extraToolButton3Style);
		table.addAttribute("extraToolButton4Style", extraToolButton4Style);
		table.addAttribute("extraToolButton5Style", extraToolButton5Style);
		table.addAttribute("id", id);
		table.addAttribute("toolbarClass", toolbarClass);
		table.addAttribute("footerStyle", footerStyle);
		table.addAttribute("beforeUnloadValidationScript", beforeUnloadValidationScript);
		table.addAttribute("minRowsDisplayed", minRowsDisplayed);
		super.addTableAttributes(model, table);
	}
}
