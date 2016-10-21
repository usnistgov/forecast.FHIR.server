package gov.nist.forecast.fhir.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.tch.fc.model.TestEvent;

import fhir.Code;
import fhir.CodeableConcept;
import fhir.FhirFactory;
import fhir.Immunization;
import fhir.Parameters;
import fhir.ParametersParameter;
import fhir.ResourceContainer;
import fhir.impl.ParametersImpl;
import fhir.util.FHIRUtil;
import fhir.util.Save;
import forecast.ForecastFactory;
import forecast.ForecastImmunization;
import forecast.ForecastPatient;
import forecast.util.ForecastUtil;

public class ImmunizationRecommendationServiceTest {

//	@Test
	public void testGetImmunizationRecommendation() {
		Parameters params = null;
		try {
			params = createParameters("TCH");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = Save.it(params, "xxx.xml");
		ImmunizationRecommendationService.getImmunizationRecommendation(params);
	}

	/// ForecastPatient
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
		Code gender = ForecastUtil.createGender("M");
		fhir.Date birthDate = FHIRUtil.convert(FHIRUtil.sdf.parse("2011-01-01T00:00:00"));
		ParametersImpl parameters = (ParametersImpl)FhirFactory.eINSTANCE.createParameters();
		
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
		ForecastPatient patient0 = createPatient(gender, birthDate);
		Immunization imm0 = createImmunization("08", "1", patient0);
		rc0.setImmunization(imm0);
		ppImmunization0.setResource(rc0);
		ppImmunizations.getPart().add(ppImmunization0);
		
		ParametersParameter ppImmunization1= FhirFactory.eINSTANCE.createParametersParameter();
		ppImmunization1.setName(FHIRUtil.convert(ForecastUtil.FORECAST_PARAMETERs.IMMUNIZATION.code));
		ResourceContainer rc1 = FhirFactory.eINSTANCE.createResourceContainer();
		ForecastPatient patient1 = createPatient(gender, birthDate);
		Immunization imm1 = createImmunization("53", "2", patient1);
		rc1.setImmunization(imm1);
		ppImmunization1.setResource(rc1);
		ppImmunizations.getPart().add(ppImmunization1);
		
		parameters.getParameter().add(ppImmunizations);
		
		return parameters;
	}
	
	 static ForecastImmunization createImmunization(String vaccineCode, String version, ForecastPatient patient) {
		ForecastImmunization imm = ForecastFactory.eINSTANCE.createForecastImmunization();
		imm.setDate(FHIRUtil.convertDateTime(new java.util.Date()));
		imm.setMeta(ForecastUtil.createMeta(FHIRUtil.sdf.format(new Date()), version));
		
		CodeableConcept cc = ForecastUtil.createCodeableConcept(vaccineCode, null, "http://hl7.org/fhir/v3/vs/VaccineType");
		imm.setVaccineCode(cc);
		ResourceContainer rc1 = FhirFactory.eINSTANCE.createResourceContainer();
		rc1.setPatient(patient);
		imm.getContained().add(rc1);
		return imm;
	}
	
	 static ForecastPatient createPatient(Code gender, fhir.Date birthDate) {
		ForecastPatient patient = ForecastFactory.eINSTANCE.createForecastPatient();
		patient.setGender(gender);
		patient.setBirthDate(birthDate);
		return patient;
	}
}
