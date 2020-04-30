package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Anagrafica Agenzia
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries({
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_ABBONATO, query = "from AnagraficaAbbonatiVo vo where vo.idTessera = :idTessera")
})
@Table(name = "tbl_9801", schema = "")
public class AnagraficaAbbonatiVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idtes9801")
	private Integer idTessera;
	@Column(name = "tipab9801")
	private String tipoAbbonamento;
	@Column(name = "codzo9801")
	private String codZona;
	@Column(name = "datin9801")
	private Timestamp dataInizioAbbonamento;
	@Column(name = "datfi9801")
	private Timestamp dataFineAbbonamento;
	@Column(name = "stato9801")
	private String statoTessera;
	@Column(name = "crivp9801")
	private Integer codRivenditaDlPreferito;
	@Column(name = "coplu9801")
	private Integer copieLunedi;
	@Column(name = "copma9801")
	private Integer copieMertedi;
	@Column(name = "copme9801")
	private Integer copieMercoledi;
	@Column(name = "copgi9801")
	private Integer copieGiovedi;
	@Column(name = "copve9801")
	private Integer copieVenerdi;
	@Column(name = "copsa9801")
	private Integer copiesabato;
	@Column(name = "copdo9801")
	private Integer copieDomenica;
	@Column(name = "copfe9801")
	private Integer copieFestivo;
}
