package models;

import java.io.Serializable;

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

	public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
