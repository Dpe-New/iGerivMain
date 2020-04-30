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
@Table(name = "tbl_9760", schema = "")
public class InforivFtpFileVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codfile9760")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9760", allocationSize = 1)
	private Long codFile;
	@Column(name = "nomfile9760")
	private String fileName;
	@Column(name = "datfile9760")
	private Timestamp dataDownload;
}
