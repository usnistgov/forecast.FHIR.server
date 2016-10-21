package gov.nist.forecast.fhir.service;

import org.hl7.fhir.FhirFactory;
import org.hl7.fhir.ImplementationGuide;

import fhir.util.FHIRUtil;
import forecast.util.ForecastUtil;

public class ImplementationGuideService {

	public static ImplementationGuide getImplementationGuide() {
		ImplementationGuide ig = FhirFactory.eINSTANCE.createImplementationGuide();
		ig.setId(FHIRUtil.createId());
		ig.setFhirVersion(FHIRUtil.FHIR_VERSION);
		ig.getPage();
		ig.setName(FHIRUtil.convert("Immunization Forecast Implementation Guide"));
		ig.setPublisher(FHIRUtil.convert("CDC/AIRA"));
		ig.setUrl(ForecastUtil.URIs.FORECAST_IMPLEMENTATIONGUIDE.uri);
		ig.setExperimental(FHIRUtil.BOOLEAN.FALSE.bool);
		return ig;
	}
}
