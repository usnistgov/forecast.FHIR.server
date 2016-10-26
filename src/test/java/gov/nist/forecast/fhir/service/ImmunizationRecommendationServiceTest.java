package gov.nist.forecast.fhir.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Code;
import org.hl7.fhir.CodeableConcept;
import org.hl7.fhir.FhirFactory;
import org.hl7.fhir.Immunization;
import org.hl7.fhir.Parameters;
import org.hl7.fhir.ParametersParameter;
import org.hl7.fhir.Patient;
import org.hl7.fhir.ResourceContainer;
import org.hl7.fhir.impl.ParametersImpl;
import org.junit.Test;
import org.tch.fc.model.TestEvent;

import fhir.util.FHIRUtil;
import fhir.util.Serialize;
import forecast.util.ForecastUtil;

public class ImmunizationRecommendationServiceTest {

	@Test
	public void testFindPatient() {
		Parameters params = null;
		try {
			params = createParameters("TCH");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Patient patient = ImmunizationRecommendationService.findPatient(params);
		assertEquals(ForecastUtil.createGender("M").getValue(), patient.getGender().getValue());
	}

	@Test
	public void testGetImmunizationRecommendation() {
		Parameters params = null;
		try {
			params = createParameters("TCH");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Bundle bundle = ImmunizationRecommendationService.getImmunizationRecommendation(params);
		assertNotNull(bundle);
	}

	@Test
	public void testCreateTestEvents() {
		Parameters ps = null;
		try {
			ps = createParameters("TCH");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<TestEvent> events = ImmunizationRecommendationService.createTestEvents(ps);
		assertNotNull(events);
		assertEquals(2, events.size());
	}

	public static ParametersImpl createParameters(String serviceType) throws ParseException {
		java.lang.String sGender = "M";
		java.lang.String sBirthDate = "2011-01-01T00:00:00";
		Code gender = ForecastUtil.createGender(sGender);
		org.hl7.fhir.Date birthDate = FHIRUtil.convert(FHIRUtil.sdf.parse(sBirthDate));
		ParametersImpl parameters = (ParametersImpl) FhirFactory.eINSTANCE.createParameters();

		ParametersParameter ppServiceURL = FhirFactory.eINSTANCE.createParametersParameter();
		ppServiceURL.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.SERVICE_URL.code));
		ppServiceURL.setValueString(FHIRUtil.convert("http://tchforecasttester.org/fv/forecast"));
		parameters.getParameter().add(ppServiceURL);

		ParametersParameter ppServiceType = FhirFactory.eINSTANCE.createParametersParameter();
		ppServiceType.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.SERVICE_TYPE.code));
		ppServiceType.setValueString(FHIRUtil.convert(serviceType));
		parameters.getParameter().add(ppServiceType);

		ParametersParameter ppAssessmentDate = FhirFactory.eINSTANCE.createParametersParameter();
		ppAssessmentDate.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.ASSESMENT_DATE.code));
		ppAssessmentDate.setValueDate(FHIRUtil.convert(new java.util.Date()));
		parameters.getParameter().add(ppAssessmentDate);

		ParametersParameter ppGender = FhirFactory.eINSTANCE.createParametersParameter();
		ppGender.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.GENDER.code));
		ppGender.setValueCode(gender);
		parameters.getParameter().add(ppGender);

		ParametersParameter ppBirthDate = FhirFactory.eINSTANCE.createParametersParameter();
		ppBirthDate.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.BIRTH_DATE.code));
		ppBirthDate.setValueDate(birthDate);
		parameters.getParameter().add(ppBirthDate);

		ParametersParameter ppImmunizations = FhirFactory.eINSTANCE.createParametersParameter();
		ppImmunizations.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.IMMUNIZATIONS.code));

		ParametersParameter ppImmunization0 = FhirFactory.eINSTANCE.createParametersParameter();
		ppImmunization0.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.IMMUNIZATION.code));
		ResourceContainer rc0 = FhirFactory.eINSTANCE.createResourceContainer();
		Patient patient0 = createPatient(sGender, sBirthDate);
		Immunization imm0 = createImmunization("08", "1", patient0);
		rc0.setImmunization(imm0);
		ppImmunization0.setResource(rc0);
		ppImmunizations.getPart().add(ppImmunization0);

		ParametersParameter ppImmunization1 = FhirFactory.eINSTANCE.createParametersParameter();
		ppImmunization1.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.IMMUNIZATION.code));
		ResourceContainer rc1 = FhirFactory.eINSTANCE.createResourceContainer();
		Patient patient1 = createPatient(sGender, sBirthDate);
		Immunization imm1 = createImmunization("53", "2", patient1);
		rc1.setImmunization(imm1);
		ppImmunization1.setResource(rc1);
		ppImmunizations.getPart().add(ppImmunization1);

		parameters.getParameter().add(ppImmunizations);

		return parameters;
	}

	static Immunization createImmunization(String vaccineCode, String version, Patient patient) {
		Immunization imm = FhirFactory.eINSTANCE.createImmunization();
		imm.setDate(FHIRUtil.convertDateTime(new java.util.Date()));
		imm.setMeta(ForecastUtil.createMeta(FHIRUtil.sdf.format(new Date()), version));

		CodeableConcept cc = ForecastUtil.createCodeableConcept(vaccineCode, null,
				"http://hl7.org/fhir/v3/vs/VaccineType");
		imm.setVaccineCode(cc);
		ResourceContainer rc1 = FhirFactory.eINSTANCE.createResourceContainer();
		rc1.setPatient(patient);
		imm.getContained().add(rc1);
		return imm;
	}

	static Patient createPatient(java.lang.String sGender, java.lang.String sBirthDate) {
		Code gender = ForecastUtil.createGender(sGender);
		org.hl7.fhir.Date birthDate = null;
		try {
			birthDate = FHIRUtil.convert(FHIRUtil.sdf.parse(sBirthDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Patient patient = FhirFactory.eINSTANCE.createPatient();
		patient.setGender(gender);
		patient.setBirthDate(birthDate);
		return patient;
	}
}
