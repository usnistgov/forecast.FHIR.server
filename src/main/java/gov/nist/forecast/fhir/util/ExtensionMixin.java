package gov.nist.forecast.fhir.util;

import org.hl7.fhir.Extension;
import org.hl7.fhir.FhirFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;


public class ExtensionMixin {

	private static Logger log = LoggerFactory.getLogger(ExtensionMixin.class);

	@JsonCreator
	public static Extension create() {
		log.error("Extension==>");
		return FhirFactory.eINSTANCE.createExtension();
	}

	public ExtensionMixin() {
		super();
		log.error("ExtensionMixin==>");
	}
}
