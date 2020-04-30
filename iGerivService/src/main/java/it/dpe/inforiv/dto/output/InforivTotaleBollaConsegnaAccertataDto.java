package it.dpe.inforiv.dto.output;

import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Component("InforivTotaleBollaConsegnaAccertataDto")
@Scope("prototype")
public class InforivTotaleBollaConsegnaAccertataDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private String descrizioneBolla;
	@Getter(AccessLevel.NONE)
	private Integer totaleCopie;
	@Getter(AccessLevel.NONE)
	private Float totaleBollaConsegna;
	@Getter(AccessLevel.NONE)
	private Float valoreFondoBolla;
	private BigDecimal totaleBollaConsegnaBd;
	private BigDecimal valoreFondoBollaBd;
	private Float totlaeFieg;
	private Float totlaeAltri;
	private Integer numeroPacchi;
	private Integer numeroCeste;
	private Integer numeroTitoliFornitiInBolla;
	private String note;
	private Long totaleCopieConsegnate;
	private Long totaleDifferenze;

	public Integer getTotaleCopie() {
		if (totaleCopieConsegnate != null && totaleDifferenze != null) {
			Long totale = totaleCopieConsegnate + totaleDifferenze;
			return new Integer(totale.toString());
		}
		return totaleCopie;
	}

	public Float getTotaleBollaConsegna() {
		if (totaleBollaConsegnaBd != null) {
			return totaleBollaConsegnaBd.floatValue();
		}
		return totaleBollaConsegna;
	}

	public Float getValoreFondoBolla() {
		if (valoreFondoBollaBd != null) {
			return valoreFondoBollaBd.floatValue();
		}
		return valoreFondoBolla;
	}
	
}
