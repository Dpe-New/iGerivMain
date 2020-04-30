package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.AssociazioneGruppoModuliPk;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella associazione gruppo moduli
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "TBL_9210", schema = "")
public class AssociazioneGruppoModuliVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private AssociazioneGruppoModuliPk pk;
}
