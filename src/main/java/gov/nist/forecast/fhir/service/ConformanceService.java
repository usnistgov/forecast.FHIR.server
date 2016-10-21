package gov.nist.forecast.fhir.service;

import java.util.Date;

import org.hl7.fhir.Conformance;
import org.hl7.fhir.FhirFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fhir.util.FHIRUtil;
import forecast.util.ForecastUtil;

public class ConformanceService {

	private static Logger log = LoggerFactory.getLogger(ConformanceService.class);

	public static Conformance getConformance() {
		Conformance conformance = FhirFactory.eINSTANCE.createConformance();
		conformance.setId(FHIRUtil.createId());
		conformance.setUrl(ForecastUtil.URIs.FORECAST_CONFORMANCE.uri);
		conformance.setStatus(FHIRUtil.CONFORMANCE_STATUS.DRAFT.code);
		conformance.setExperimental(FHIRUtil.BOOLEAN.TRUE.bool);
		conformance.setFhirVersion(FHIRUtil.FHIR_VERSION);
		conformance.setDate(FHIRUtil.convertDateTime(new Date()));
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_IMPLEMENTATIONGUIDE.reference);
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_IMMUNIZATIONRECOMMENDATIONRECOMMENDATION.reference);
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_IMMUNIZATIONRECOMMENDATION.reference);
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_IMMUNIZATION.reference);
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_PATIENT.reference);
		conformance.getFormat().add(FHIRUtil.FORMAT.JSON.code);
		conformance.getFormat().add(FHIRUtil.FORMAT.XML.code);
		return conformance;
	}

}
