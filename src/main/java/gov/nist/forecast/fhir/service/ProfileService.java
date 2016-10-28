package gov.nist.forecast.fhir.service;

import org.hl7.fhir.Reference;
import org.hl7.fhir.StructureDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fhir.util.DeSerialize;

public class ProfileService {

	private static Logger log = LoggerFactory.getLogger(ProfileService.class);

	public static StructureDefinition getProfile(Reference ref) {
		log.trace("getProfile==>" + ref.getReference().getValue());
		String fileName = "/" + ref.getReference().getValue() + ".structuredefinition.xml";
		log.trace("<==getProfile " + fileName);
		DeSerialize load = new DeSerialize();
		return (StructureDefinition) load.it(fileName);
	}
}
