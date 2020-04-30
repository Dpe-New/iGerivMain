package it.dpe.igeriv.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
	
@Entity
@Table(name="TBL_9226")
@NamedQuery(name="CampagnaVo.findAll", query="SELECT t FROM CampagnaVo t")

public class CampagnaVo extends BaseVo  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, precision=4)
	private Integer id9226;

	@Column(nullable=false, precision=4)
	private Integer coddl9226;

	@Column(nullable=false, precision=4)
	private Integer dlrif9226;

	@Temporal(TemporalType.DATE)
	private Date dtfine9226;

	@Temporal(TemporalType.DATE)
	private Date dtfinesoll9226;

	@Temporal(TemporalType.DATE)
	private Date dtinizio9226;

	@Temporal(TemporalType.DATE)
	@Column(name="TR1_OP1_AL9226")
	private Date tr1Op1Al9226;

	@Temporal(TemporalType.DATE)
	@Column(name="TR1_OP1_DAL9226")
	private Date tr1Op1Dal9226;

	@Temporal(TemporalType.DATE)
	@Column(name="TR1_OP2_AL9226")
	private Date tr1Op2Al9226;

	@Temporal(TemporalType.DATE)
	@Column(name="TR1_OP2_DAL9226")
	private Date tr1Op2Dal9226;

	@Temporal(TemporalType.DATE)
	@Column(name="TR2_OP1_AL9226")
	private Date tr2Op1Al9226;

	@Temporal(TemporalType.DATE)
	@Column(name="TR2_OP1_DAL9226")
	private Date tr2Op1Dal9226;

	@Temporal(TemporalType.DATE)
	@Column(name="TR2_OP2_AL9226")
	private Date tr2Op2Al9226;

	@Temporal(TemporalType.DATE)
	@Column(name="TR2_OP2_DAL9226")
	private Date tr2Op2Dal9226;
//
//	//bi-directional many-to-one association to Tbl9227
//	@OneToMany(fetch=FetchType.LAZY, mappedBy = "campagnaVo")
//	private List<CampagnaEdicoleVo> campagnaEdicoleVo;
	
	public Integer getId9226() {
		return this.id9226;
	}

	public void setId9226(Integer id9226) {
		this.id9226 = id9226;
	}

	public Integer getCoddl9226() {
		return this.coddl9226;
	}

	public void setCoddl9226(Integer coddl9226) {
		this.coddl9226 = coddl9226;
	}

	public Integer getDlrif9226() {
		return this.dlrif9226;
	}

	public void setDlrif9226(Integer dlrif9226) {
		this.dlrif9226 = dlrif9226;
	}

	public Date getDtfine9226() {
		return this.dtfine9226;
	}

	public void setDtfine9226(Date dtfine9226) {
		this.dtfine9226 = dtfine9226;
	}

	public Date getDtfinesoll9226() {
		return this.dtfinesoll9226;
	}

	public void setDtfinesoll9226(Date dtfinesoll9226) {
		this.dtfinesoll9226 = dtfinesoll9226;
	}

	public Date getDtinizio9226() {
		return this.dtinizio9226;
	}

	public void setDtinizio9226(Date dtinizio9226) {
		this.dtinizio9226 = dtinizio9226;
	}

	public Date getTr1Op1Al9226() {
		return this.tr1Op1Al9226;
	}

	public void setTr1Op1Al9226(Date tr1Op1Al9226) {
		this.tr1Op1Al9226 = tr1Op1Al9226;
	}

	public Date getTr1Op1Dal9226() {
		return this.tr1Op1Dal9226;
	}

	public void setTr1Op1Dal9226(Date tr1Op1Dal9226) {
		this.tr1Op1Dal9226 = tr1Op1Dal9226;
	}

	public Date getTr1Op2Al9226() {
		return this.tr1Op2Al9226;
	}

	public void setTr1Op2Al9226(Date tr1Op2Al9226) {
		this.tr1Op2Al9226 = tr1Op2Al9226;
	}

	public Date getTr1Op2Dal9226() {
		return this.tr1Op2Dal9226;
	}

	public void setTr1Op2Dal9226(Date tr1Op2Dal9226) {
		this.tr1Op2Dal9226 = tr1Op2Dal9226;
	}

	public Date getTr2Op1Al9226() {
		return this.tr2Op1Al9226;
	}

	public void setTr2Op1Al9226(Date tr2Op1Al9226) {
		this.tr2Op1Al9226 = tr2Op1Al9226;
	}

	public Date getTr2Op1Dal9226() {
		return this.tr2Op1Dal9226;
	}

	public void setTr2Op1Dal9226(Date tr2Op1Dal9226) {
		this.tr2Op1Dal9226 = tr2Op1Dal9226;
	}

	public Date getTr2Op2Al9226() {
		return this.tr2Op2Al9226;
	}

	public void setTr2Op2Al9226(Date tr2Op2Al9226) {
		this.tr2Op2Al9226 = tr2Op2Al9226;
	}

	public Date getTr2Op2Dal9226() {
		return this.tr2Op2Dal9226;
	}

	public void setTr2Op2Dal9226(Date tr2Op2Dal9226) {
		this.tr2Op2Dal9226 = tr2Op2Dal9226;
	}
//
//	public List<CampagnaEdicoleVo> getCampagnaEdicole() {
//		return this.campagnaEdicoleVo;
//	}
//
//	public void setCampagnaEdicole(List<CampagnaEdicoleVo> tbl9227s) {
//		this.campagnaEdicoleVo = tbl9227s;
//	}
//
//	public CampagnaEdicoleVo addCampagnaEdicole(CampagnaEdicoleVo tbl9227) {
//		getCampagnaEdicole().add(tbl9227);
//		tbl9227.setCampagnaVo(this);
//
//		return tbl9227;
//	}
//
//	public CampagnaEdicoleVo removeCampagnaEdicole(CampagnaEdicoleVo tbl9227) {
//		getCampagnaEdicole().remove(tbl9227);
//		tbl9227.setCampagnaVo(null);
//
//		return tbl9227;
//	}

	}