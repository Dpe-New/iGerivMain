package it.dpe.igeriv.vo;

import it.dpe.igeriv.comparator.RichiestaRifonrnimentoComparator;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.AnagraficaPubblicazioniPk;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * Tabella Anagrafica Pubblicazioni
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_PUBBLICAZIONI_BY_ID, query = "from AnagraficaPubblicazioniVo vo join fetch vo.limitiPeriodicitaVo left join fetch vo.periodicitaVo where vo.pk.codFiegDl = ? and vo.pk.codicePubblicazione = ?")
})
@Table(name = "tbl_9606", schema = "")
public class AnagraficaPubblicazioniVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private AnagraficaPubblicazioniPk pk;
	@Column(name = "edi9606")
	private Integer codFornitore;
	@Column(name = "titolo9606")
	private String titolo;
	@Column(name = "ciq9606")
	private Integer codInizioQuotidiano;
	@Column(name = "cfq9606")
	private Integer codFineQuotidiano;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(formula = @JoinFormula(value = "" + IGerivConstants.TIPO_OPERAZIONE_GESDIS, referencedColumnName = "tipop9216")),
		@JoinColumnOrFormula(column = @JoinColumn(name = "peridl9606", insertable = true, updatable = true, referencedColumnName = "perio9216"))
	})
	private PeriodicitaVo periodicitaVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "coddl9606", insertable = false, updatable = false, referencedColumnName = "coddl9217")),
		@JoinColumnOrFormula(column = @JoinColumn(name = "argodl9606", insertable = false, updatable = false, referencedColumnName = "segm9217"))
	})
	private ArgomentoVo argomentoVo;
	@Column(name = "cedinf9606")
	private Integer codFiegFornitore;
	@Column(name = "titinf9606")
	private String titoloInforete;
	@Column(name = "perinf9606")
	private Integer periodicitaInforete;
	@Column(name = "seminf9606")
	private Integer segmentoMercatoInforete;
	@Column(name = "npdtri9606")
	private Integer numCopertinePrecedentiPerRifornimenti;
	@Column(name = "ipacco9606")
	private Integer indicatorePaccotto;
	@Column(name = "cge9606")
	private Integer codGruppoEditoriale;
	@OneToMany(fetch=FetchType.LAZY, targetEntity = StoricoCopertineVo.class, mappedBy = "anagraficaPubblicazioniVo")
	@Sort(type = SortType.COMPARATOR, comparator = RichiestaRifonrnimentoComparator.class)
	private List<StoricoCopertineVo> storicoCopertineVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "coddl9606", insertable = false, updatable = false, referencedColumnName = "coddl9108")),
		@JoinColumnOrFormula(column = @JoinColumn(name = "peridl9606", insertable = false, updatable = false, referencedColumnName = "perio9108"))
	})
	private LimitiPeriodicitaVo limitiPeriodicitaVo;
	@Column(name = "img9606")
	@Basic(fetch = FetchType.LAZY) 
	private byte[] imgMiniatura;
	@Column(name = "imgnm9606")
	private String imgMiniaturaName;
	//@Column(name="ARGODL9606")
	//private Integer argomentoDl;
	
	
}
