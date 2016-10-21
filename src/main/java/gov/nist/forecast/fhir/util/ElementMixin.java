package gov.nist.forecast.fhir.util;

import org.hl7.fhir.Element;
import org.hl7.fhir.FhirFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;


public class ElementMixin {

	private static Logger log = LoggerFactory.getLogger(ElementMixin.class);

	@JsonCreator
	public static Element create() {
		log.error("Element==>");
		return FhirFactory.eINSTANCE.createElement();
	}
}
