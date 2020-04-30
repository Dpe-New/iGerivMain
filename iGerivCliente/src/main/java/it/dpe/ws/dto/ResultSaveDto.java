package it.dpe.ws.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSaveDto implements Serializable {
	private static final long serialVersionUID = 155767716273623737L;
	private ResultType type;
	private String message;
	
	public static enum ResultType {
        SUCCESS,
        ERROR,
        WARNING;
        
        public String getString() {     
        	return this.name(); 
        } 
    }

}
