package gov.nist.forecast.fhir.exceptions;

public class ForecastException extends Exception {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -53161181668934983L;

	Integer status;

	String developerMessage;

	public ForecastException(Integer status, String developerMessage) {
		super();
		this.status = status;
		this.developerMessage = developerMessage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
}