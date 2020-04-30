package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import it.dpe.igeriv.util.StringUtility;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiBollaDettaglioPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "id9546")
	private Long idDocumento;
	@Column(name = "prog9546")
	private Integer progressivo;
	
	@Override
	public String toString() {
		return (getIdDocumento() != null && getProgressivo() != null) ? getIdDocumento() + "|" + getProgressivo() : "";
	}

	//GIFT CARD
	//progressivo numerico di lunghezza max 9 
	//padding sinistro es 000000005	
	public String getIdRichiestaWSEpipoli(){
		return (getIdDocumento() != null && getProgressivo() != null) ? getIdDocumento() +""+ StringUtility.fillSx(getProgressivo().toString(), 2, '0'): "";
	}
	
	
}
