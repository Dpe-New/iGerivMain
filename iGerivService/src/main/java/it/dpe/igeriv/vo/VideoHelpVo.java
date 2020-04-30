package it.dpe.igeriv.vo;

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
@Table(name = "tbl_9215", schema = "")
public class VideoHelpVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codvid9215")
	private Integer codice;
	@Column(name = "titvid9215")
	private String titolo;
	@Column(name = "nomvid9215")
	private String nomeFile;
}
