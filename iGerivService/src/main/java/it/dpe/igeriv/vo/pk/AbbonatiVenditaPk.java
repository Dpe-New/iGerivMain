package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class AbbonatiVenditaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "idtes9803")
	private Integer idTessera;
	@Column(name = "dapro9803")
	private Timestamp dataProdottoVenduto;
}
