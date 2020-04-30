package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.PrezzoEdicolaPk;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Periodicità
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_PREZZO_EDICOLA, query = "from PrezzoEdicolaVo vo where vo.pk.codFiegDl = :codFiegDl and vo.pk.idtn = :idtn and vo.pk.gruppoSconto = :gruppoSconto") 
})
@Table(name = "tbl_9617", schema = "")
public class PrezzoEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private PrezzoEdicolaPk pk;
	@Column(name = "prnet9617")
	private BigDecimal prezzoNetto;
}
