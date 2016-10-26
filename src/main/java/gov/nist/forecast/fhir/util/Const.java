package gov.nist.forecast.fhir.util;

import java.text.SimpleDateFormat;

import org.tch.fc.model.Service;

public abstract class Const {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public enum RESPONSE_CODE {
		
		OK(200), NOT_FOUND(404), SERVER_ERROR(500);

		public final int responseCode;

		RESPONSE_CODE(int responseCode) {
			this.responseCode = responseCode;
		}
	}

	public static java.lang.String getMessage(RESPONSE_CODE rc, String s) {
		switch (rc) {
		case NOT_FOUND:

			return java.lang.String.format("Invalid parameter %s. Valid parameters are %s", s, Service.valueList());
			
		case SERVER_ERROR:
			
			return java.lang.String.format("Server error");
			
		default:
			return null;
		}
	}

	public java.lang.String getLink(RESPONSE_CODE rc) {
		switch (rc) {
		case NOT_FOUND:

			return String.format("");
		default:
			return null;
		}

	}

	public java.lang.String getDeveloperMessage(RESPONSE_CODE rc) {
		switch (rc) {
		case NOT_FOUND:

			return java.lang.String.format("");
		default:
			return null;
		}

	}
}
