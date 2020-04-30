package it.dpe.igeriv.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9725", schema = "")
public class RichiestaDatiWSVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codri9725")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9725", allocationSize = 1)
	private Integer codRichiesta;
	@Column(name = "crivw9725")
	private Integer codEdicola;
	@Column(name = "datbc9725")
	private Timestamp dataBolla;
	@Column(name = "count9725")
	private Integer count;
}
