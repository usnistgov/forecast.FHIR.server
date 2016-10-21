package gov.nist.forecast.fhir.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;

import try_.TryFactory;
import try_.impl.TheClassImpl;

public class TheClassMixin {

	private static Logger log = LoggerFactory.getLogger(TheClassMixin.class);

	@JsonCreator
	public static TheClassImpl factory() {
		log.error("TheClass==>");
		return (TheClassImpl)TryFactory.eINSTANCE.createTheClass();
	}

	public TheClassMixin() {
		super();
		log.error("TheClassMixin==>");
	}
}
