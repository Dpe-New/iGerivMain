package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class DlRichiamoRifornimentoInvioPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9133")
	private Integer codDl;
	@Column(name = "tipor9133")
	private Integer tipo;
	@Column(name = "datainvio9133")
	private Timestamp dataInvio;
}
