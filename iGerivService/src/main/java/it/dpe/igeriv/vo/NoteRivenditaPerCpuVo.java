package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.NoteRivenditaPerCpuPk;

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
@Table(name = "tbl_9641", schema = "")
public class NoteRivenditaPerCpuVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private NoteRivenditaPerCpuPk pk;
	@Column(name ="noter9641")
	private String note;
}
