package it.dpe.inforiv.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultDto {
	private String errorMessage;
	private String stackTrace;
	private Boolean success;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	
}
