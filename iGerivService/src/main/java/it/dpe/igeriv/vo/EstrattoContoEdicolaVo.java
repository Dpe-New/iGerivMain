package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.EstrattoContoEdicolaPk;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ESTRATTO_CONTO_EDICOLA_DATE, query = "select vo.pk.dataEstrattoConto as dataEstrattoConto from EstrattoContoEdicolaVo vo where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dataEstrattoConto < :dataInizioEstrattoContoPdf order by vo.pk.dataEstrattoConto desc"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ESTRATTO_CONTO_BY_PK, query = "select vo.numEstrattoConto as numEstrattoConto, vo.pk.dataEstrattoConto as dataEstrattoConto from EstrattoContoEdicolaVo vo where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dataEstrattoConto = :dataEstrattoConto")
})
@Table(name = "tbl_9630", schema = "")
public class EstrattoContoEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private EstrattoContoEdicolaPk pk;
	@Column(name = "nec9630")
	private Integer numEstrattoConto;
	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true, fetch=FetchType.EAGER, targetEntity = EstrattoContoEdicolaDettaglioVo.class, mappedBy = "estrattoConto")
	private List<EstrattoContoEdicolaDettaglioVo> dettagli;
	
	@Override
	public String toString() {
		return getPk().toString();
	}
}
