package it.dpe.igeriv.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.google.common.base.Strings;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants.SQLType;

@Data
@EqualsAndHashCode(callSuper=false)
public class ParametriEdicolaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codEdicola;
	private Integer codParametro;
	private SQLType sqlType;
	@Getter(AccessLevel.NONE)
	private String value;
	private String defaultValue;
	
	public String getValue() {
		return value == null ? "" : value;
	}

	public String getParamValue() {
		return Strings.isNullOrEmpty(getValue()) ? getDefaultValue() : getValue(); 
	}

	public String getNomeParametro() {
		if (getCodParametro() != null) {
			return IGerivMessageBundle.get("igeriv.params.edicola." + getCodParametro().toString());
		}
		return "";
	}

}
