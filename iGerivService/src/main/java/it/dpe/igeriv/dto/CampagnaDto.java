package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CampagnaDto extends BaseDto{

	public Integer id9226;
	public Integer coddl9226;
	public Integer dlrif9226;
	public Date dtfine9226;
	public Date dtfinesoll9226;
	public Date dtinizio9226;
	public Date tr1Op1Al9226;
	public Date tr1Op1Dal9226;
	public Date tr1Op2Al9226;
	public Date tr1Op2Dal9226;
	public Date tr2Op1Al9226;
	public Date tr2Op1Dal9226;
	public Date tr2Op2Al9226;
	public Date tr2Op2Dal9226;
	
	public List<CampagnaEdicoleDto> campagnaEdicola;
}
