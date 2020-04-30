package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

/**
 * Tabella trascodifica periodicità inforete
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9608", schema = "")
public class PeriodicitaTrascodificaInforeteVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "perioi9608")
	private Integer periodicitaInforete;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(formula = @JoinFormula(value = "" + IGerivConstants.TIPO_OPERAZIONE_GESDIS, referencedColumnName = "tipop9216")),
		@JoinColumnOrFormula(column = @JoinColumn(name = "period9608", insertable = true, updatable = true, referencedColumnName = "perio9216"))
	})
	private PeriodicitaVo periodicita;
}
