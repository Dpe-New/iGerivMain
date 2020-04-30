package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class FatturazioneDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer coddl;
	private String nomeDl;
	private Timestamp dtAttivazioneEdicola;
	private Integer codEdicolaDl;
	private String nomeEdicola;
	private Integer giorni;
	private Timestamp dtSospensioneEdicola;
	@Getter(AccessLevel.NONE)
	private String roleName;
	@Getter(AccessLevel.NONE)
	private Float importoiGeriv;
	@Getter(AccessLevel.NONE)
	private Float importoiGerivPlus;
	@Getter(AccessLevel.NONE)
	private Float importoiGerivPromo;
	private Boolean edicolaTest;

	public String getRoleName() {
		if (edicolaTest != null && edicolaTest) {
			return roleName +" "+ IGerivMessageBundle.get("igeriv.edicola.fatturazione.prova");
		} else if (getEdicolaPlus()) {
			return roleName +" "+ IGerivMessageBundle.get("igeriv.edicola.fatturazione.plus");
		} 
		return roleName;
	}

	public Float getImportoiGeriv() {
		return importoiGeriv == null ? 0f : importoiGeriv;
	}

	public Float getImportoiGerivPlus() {
		return importoiGerivPlus == null ? 0f : importoiGerivPlus;
	}
	
	public Float getImportoiGerivPromo() {
		return importoiGerivPromo == null ? 0f : importoiGerivPromo;
	}

	public Float getImporto() {
		return getImportoiGeriv() + getImportoiGerivPlus() + getImportoiGerivPromo();
	}

	public Boolean getEdicolaPlus() {
		return getImportoiGerivPlus() > 0f;
	}

	
}
