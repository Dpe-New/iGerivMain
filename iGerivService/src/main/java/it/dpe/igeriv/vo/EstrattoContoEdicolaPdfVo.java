package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.EstrattoContoEdicolaPdfPk;

import javax.persistence.Column;
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
@Table(name = "tbl_9633", schema = "")
public class EstrattoContoEdicolaPdfVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private EstrattoContoEdicolaPdfPk pk;
	@Column(name = "nfile9633")
	private String nomeFile;
}
