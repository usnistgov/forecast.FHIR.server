package gov.nist.forecast.fhir.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;

import fhir.FhirFactory;
import fhir.impl.ParametersImpl;

public class ParametersMixin {

	private static Logger log = LoggerFactory.getLogger(ParametersMixin.class);

	@JsonCreator
	public static ParametersImpl create() {
		log.trace("ParametersImpl==>");
		return (ParametersImpl)FhirFactory.eINSTANCE.createParameters();
	}

	public ParametersMixin() {
		super();
		log.trace("ParametersMixin==>");
	}
}
