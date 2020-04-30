package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author romanom
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PositionSizeDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer top;
	private Integer left;
	private Integer width;
	private Integer height;
	private Boolean barraProdottiVariVisible;
}
