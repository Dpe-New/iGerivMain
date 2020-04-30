package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.NoteRivenditaPk;

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
@Table(name = "tbl_9640", schema = "")
public class NoteRivenditaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private NoteRivenditaPk pk;
	@Column(name ="noter9640")
	private String note;
}
