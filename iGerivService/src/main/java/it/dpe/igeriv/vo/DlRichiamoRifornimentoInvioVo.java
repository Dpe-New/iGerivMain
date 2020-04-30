package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.DlRichiamoRifornimentoInvioPk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9133", schema = "")
public class DlRichiamoRifornimentoInvioVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private DlRichiamoRifornimentoInvioPk pk;
	@Column(name = "datacre9133")
	private Timestamp dataCreazioneFile;
	@Column(name = "nomefile9133")
	private String nomeFile;
	@Column(name = "datadownl9133")
	private Timestamp dataDownload;
	@Column(name = "nrec9133")
	private Integer numRecordRichiesteRifornimento;
	@Column(name = "nedrif9133")
	private Integer numEdicoleConRecordRichiesteRifornimento;
	@Column(name = "nrecril9133")
	private Integer numRecordRilevamenti;
	@Column(name = "nedril9133")
	private Integer numEdicoleConRilevamenti;
}
