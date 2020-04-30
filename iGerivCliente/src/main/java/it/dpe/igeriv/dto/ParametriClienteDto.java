package it.dpe.igeriv.dto;

import com.google.common.base.Strings;

import it.dpe.igeriv.util.IGerivConstants.SQLType;
import it.dpe.igeriv.resources.IGerivMessageBundle;

public class ParametriClienteDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codCliente;
	private Integer codParametro;
	private SQLType sqlType;
	private String value;
	private String defaultValue;
	
	public Integer getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}

	public Integer getCodParametro() {
		return codParametro;
	}

	public void setCodParametro(Integer codParametro) {
		this.codParametro = codParametro;
	}

	public SQLType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SQLType sqlType) {
		this.sqlType = sqlType;
	}

	public String getValue() {
		return value == null ? "" : value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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
