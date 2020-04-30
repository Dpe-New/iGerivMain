package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class FerieEdicolePk extends BasePk {
		private static final long serialVersionUID = 1L;
		@Column(name = "crivw9228")
		private Integer codEdicola;
		@Column(name = "din9228")
		private Timestamp dataInizioFerie;
		@Column(name = "dif9228")
		private Timestamp dataFineFerie;
		
}
