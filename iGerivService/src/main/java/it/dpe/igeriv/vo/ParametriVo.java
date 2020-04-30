package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants.SQLType;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ParamDef;

/**
 * Tabella parametri edicola igeriv
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Entity
@Table(name = "tbl_9709", schema = "")
@FilterDefs({
	@FilterDef(name="ParamtriFilter", parameters=@ParamDef( name="codEdicola", type="integer" ) )
})
public class ParametriVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codpar9709")
	private Integer codParametro;
	@Column(name = "despar9709")
	private String titolo;
	@Column(name = "tippar9709")
	@Enumerated(EnumType.STRING)  
	private SQLType sqlType;
	@Column(name = "valdef9709")
	private String defaultValue;
	@Formula(value = "(select t1.valpar9710 from tbl_9710 t1 where t1.codpar9710 = codpar9709 and t1.crivw9710 = :ParamtriFilter.codEdicola)")
	@Basic(fetch = FetchType.LAZY)
	private String value;
}
