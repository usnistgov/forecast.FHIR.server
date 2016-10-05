package gov.nist.forecast.fhir.util;

import org.tch.fc.model.Service;

public abstract class Const {

	public enum RESPONSE_CODE {
		
		OK(200), NOT_FOUND(404), SERVER_ERROR(500);

		public final int responseCode;

		RESPONSE_CODE(int responseCode) {
			this.responseCode = responseCode;
		}
	}

	public static String getMessage(RESPONSE_CODE rc, String s) {
		switch (rc) {
		case NOT_FOUND:

			return String.format("Invalid parameter %s. Valid parameters are %s", s, Service.valueList());
			
		case SERVER_ERROR:
			
			return String.format("Server error");
			
		default:
			return null;
		}
	}

	public String getLink(RESPONSE_CODE rc) {
		switch (rc) {
		case NOT_FOUND:

			return String.format("");
		default:
			return null;
		}

	}

	public String getDeveloperMessage(RESPONSE_CODE rc) {
		switch (rc) {
		case NOT_FOUND:

			return String.format("");
		default:
			return null;
		}

	}
}
