package gov.nist.forecast.fhir.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;

import fhir.Element;
import fhir.FhirFactory;

public class ElementMixin {

	private static Logger log = LoggerFactory.getLogger(ElementMixin.class);

	@JsonCreator
	public static Element create() {
		log.error("Element==>");
		return FhirFactory.eINSTANCE.createElement();
	}
}
