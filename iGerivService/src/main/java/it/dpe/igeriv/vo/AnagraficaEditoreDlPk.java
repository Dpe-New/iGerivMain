package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import it.dpe.igeriv.vo.pk.BasePk;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class AnagraficaEditoreDlPk extends BasePk{
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CODDL9114")
	private Integer codFiegDl;
	
	@Column(name = "EDIT9114")
	private Integer codFornitore;
}
