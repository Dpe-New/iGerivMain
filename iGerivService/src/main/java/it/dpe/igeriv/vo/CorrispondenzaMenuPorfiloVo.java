package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.CorrispondenzaMenuPorfiloPk;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9210", schema = "")
public class CorrispondenzaMenuPorfiloVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private CorrispondenzaMenuPorfiloPk pk;
}
