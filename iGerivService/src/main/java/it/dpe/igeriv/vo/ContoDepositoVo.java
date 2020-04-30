package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ContoDepositoPk;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

/**
 * @author romanom
 *
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9618", schema = "")
public class ContoDepositoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ContoDepositoPk pk;
	@Column(name = "quant9618")
	private Integer quantita;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9618", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9618", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Formula(value = "(select sum(r1.qreso9621) from tbl_9621 r1 where r1.coddl9621 = coddl9618 and r1.crivw9621 = crivw9618 and r1.idtn9621 = idtn9618)")
	@Basic(fetch = FetchType.LAZY)
	private Integer resoBolla;
	@Formula(value = "(select sum(r2.qreso9622) from tbl_9622 r2 where r2.coddl9622 = coddl9618 and r2.crivw9622 = crivw9618 and r2.idtn9622 = idtn9618)")
	@Basic(fetch = FetchType.LAZY)
	private Integer resoFuoriVoce;
	@Formula(value = "(select sum(r3.qreso9622b) from tbl_9622b r3 where r3.coddl9622b = coddl9618 and r3.crivw9622b = crivw9618 and r3.idtn9622b = idtn9618)")
	@Basic(fetch = FetchType.LAZY)
	private Integer resoRichiamoPersonalizzato;
}
