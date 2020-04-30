package it.dpe.igeriv.vo;

import it.dpe.igeriv.dto.CampagnaEdicoleDto;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jsoup.helper.DataUtil;


@Entity
@Table(name="TBL_9227")
@NamedQuery(name="CampagnaEdicoleVo.findAll", query="SELECT t FROM CampagnaEdicoleVo t")
public class CampagnaEdicoleVo extends BaseVo {
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy=GenerationType.TABLE)
		@Column(unique=true, nullable=false, precision=12)
		private Integer crivw9227;

		@Temporal(TemporalType.DATE)
		@Column(name="DT_OP1_AL9227")
		private Date dtOp1Al9227;

		@Temporal(TemporalType.DATE)
		@Column(name="DT_OP1_DAL9227")
		private Date dtOp1Dal9227;

		@Temporal(TemporalType.DATE)
		@Column(name="DT_OP2_AL9227")
		private Date dtOp2Al9227;

		@Temporal(TemporalType.DATE)
		@Column(name="DT_OP2_DAL9227")
		private Date dtOp2Dal9227;

		@Temporal(TemporalType.DATE)
		private Date dtconferma9227;

		@Column(nullable=false, precision=1)
		private Integer flgaperto9227;

		@Column(nullable=false, precision=1)
		private Integer flgstato9227;

		@Column(nullable=false, precision=2)
		private Integer totup9227;

		@Column(precision=2)
		private Integer turno9227;

		//bi-directional many-to-one association to Tbl9226
		@ManyToOne
		@JoinColumn(name="IDCAMP9227", nullable=false)
		private CampagnaVo campagna;

		public Integer getCrivw9227() {
			return this.crivw9227;
		}

		public void setCrivw9227(Integer crivw9227) {
			this.crivw9227 = crivw9227;
		}

		public Date getDtOp1Al9227() {
			return this.dtOp1Al9227;
		}

		public void setDtOp1Al9227(Date dtOp1Al9227) {
			this.dtOp1Al9227 = dtOp1Al9227;
		}

		public Date getDtOp1Dal9227() {
			return this.dtOp1Dal9227;
		}

		public void setDtOp1Dal9227(Date dtOp1Dal9227) {
			this.dtOp1Dal9227 = dtOp1Dal9227;
		}

		public Date getDtOp2Al9227() {
			return this.dtOp2Al9227;
		}

		public void setDtOp2Al9227(Date dtOp2Al9227) {
			this.dtOp2Al9227 = dtOp2Al9227;
		}

		public Date getDtOp2Dal9227() {
			return this.dtOp2Dal9227;
		}

		public void setDtOp2Dal9227(Date dtOp2Dal9227) {
			this.dtOp2Dal9227 = dtOp2Dal9227;
		}

		public Date getDtconferma9227() {
			return  this.dtconferma9227;
		}

		public void setDtconferma9227(Date dtconferma9227) {
			this.dtconferma9227 = dtconferma9227;
		}

		public Integer getFlgaperto9227() {
			return this.flgaperto9227;
		}

		public void setFlgaperto9227(Integer flgaperto9227) {
			this.flgaperto9227 = flgaperto9227;
		}

		public Integer getFlgstato9227() {
			return this.flgstato9227;
		}

		public void setFlgstato9227(Integer flgstato9227) {
			this.flgstato9227 = flgstato9227;
		}

		public Integer getTotup9227() {
			return this.totup9227;
		}

		public void setTotup9227(Integer totup9227) {
			this.totup9227 = totup9227;
		}

		public Integer getTurno9227() {
			return this.turno9227;
		}

		public void setTurno9227(Integer turno9227) {
			this.turno9227 = turno9227;
		}

		public CampagnaVo getCampagnaVo() {
			return this.campagna;
		}

		public void setCampagnaVo(CampagnaVo tbl9226) {
			this.campagna = tbl9226;
		}

		
		
		
		
	}