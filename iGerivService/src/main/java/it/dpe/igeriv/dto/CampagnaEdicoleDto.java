package it.dpe.igeriv.dto;



import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper=false)
public class CampagnaEdicoleDto extends BaseDto{

	public Integer crivw9227;
	public Date dtOp1Al9227;
	public Date dtOp1Dal9227;
	public Date dtOp2Al9227;
	public Date dtOp2Dal9227;
	public Date dtconferma9227;
	public Integer flgaperto9227;
	public Integer flgstato9227;
	public Integer totup9227;
	public Integer turno9227;

	public CampagnaDto campagna;
}
