package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author romanom
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class InventarioResultDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private ResultType type;
	private Object result;
	private String exceptionMessage;
	
	public static enum ResultType {
		OK,
        EXCEPTION;
        
        public String getString() {     
        	return this.name(); 
        } 
    }
	
}	
