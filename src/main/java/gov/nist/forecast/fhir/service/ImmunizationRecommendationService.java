package gov.nist.forecast.fhir.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tch.fc.ConnectFactory;
import org.tch.fc.ConnectorInterface;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.Service;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;

import fhir.Bundle;
import fhir.BundleEntry;
import fhir.BundleType;
import fhir.BundleTypeList;
import fhir.DateTime;
import fhir.FhirFactory;
import fhir.Immunization;
import fhir.Parameters;
import fhir.ParametersParameter;
import fhir.ResourceContainer;
import fhir.util.FHIRUtil;
import forecast.ForecastImmunizationRecommendation;
import forecast.ForecastPatient;
import forecast.util.ForecastUtil;
import gov.nist.forecast.fhir.exceptions.ForecastException;
import gov.nist.forecast.fhir.exceptions.ForecastExceptionMapper;
import gov.nist.forecast.fhir.util.Const;

public class ImmunizationRecommendationService {

	private static Logger log = LoggerFactory.getLogger(ImmunizationRecommendationService.class);

	public static Bundle getImmunizationRecommendation(Parameters parameters) {
		Bundle bundle = FhirFactory.eINSTANCE.createBundle();
		bundle.setId(FHIRUtil.createId());
		BundleType type = FhirFactory.eINSTANCE.createBundleType();
		type.setValue(BundleTypeList.SEARCHSET);
		bundle.setType(type);

		Software software = new Software();
		ParametersParameter ppServiceUrl = findParametersParameter(ForecastUtil.FORECAST_PARAMETERs.SERVICE_URL.code,
				parameters);
		software.setServiceUrl(ppServiceUrl.getValueString().getValue());
		ParametersParameter ppServiceType = findParametersParameter(ForecastUtil.FORECAST_PARAMETERs.SERVICE_TYPE.code,
				parameters);
		Service service = Service.getService(ppServiceType.getValueString().getValue());
		software.setService(service);

		TestCase testCase = new TestCase();
		ForecastPatient patient = findPatient(parameters);
		try {
			ParametersParameter ppAssessment = findParametersParameter(ForecastUtil.FORECAST_PARAMETERs.ASSESMENT_DATE.code,
					parameters);
			testCase.setEvalDate(FHIRUtil.convert(ppAssessment.getValueDate()));
			ParametersParameter ppBirthDate = findParametersParameter(ForecastUtil.FORECAST_PARAMETERs.BIRTH_DATE.code, parameters);
			testCase.setPatientDob(FHIRUtil.convert(ppBirthDate.getValueDate()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ParametersParameter ppGender = findParametersParameter(ForecastUtil.FORECAST_PARAMETERs.GENDER.code, parameters);
		testCase.setPatientSex(ppGender.getValueString().toString());
		List<TestEvent> events = createTestEvents(parameters);
		testCase.setTestEventList(events);
//		ForecastUtil.createForecastPatient(testCase);
		ConnectorInterface connector = null;
		try {
			connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
			java.util.List<ForecastActual> forecastActualList = connector.queryForForecast(testCase,
					new SoftwareResult());
			log.trace("forecastActualList=" + forecastActualList.size());
			for (ForecastActual forecastActual : forecastActualList) {
				log.trace(ForecastUtil.forecastToString(forecastActual));
				log.trace(ForecastUtil.testEventsToString(events));
				 ForecastImmunizationRecommendation recommendation =
				 ForecastUtil.createForecastImmunizationRecommendation(forecastActual, patient, events);
				 BundleEntry entry =
				 FhirFactory.eINSTANCE.createBundleEntry();
				 entry.setId(recommendation.getIdentifier().get(0).toString());
				 entry.setFullUrl(recommendation.getImplicitRules());
				 ResourceContainer container =
				 FhirFactory.eINSTANCE.createResourceContainer();
				 container.setImmunizationRecommendation(recommendation);
				 entry.setResource(container);
				 bundle.getEntry().add(entry);
			}
//			BundleEntry entry = FhirFactory.eINSTANCE.createBundleEntry();
//			entry.setId(patient.getIdentifier().get(0).getId());
//			entry.setFullUrl(patient.getImplicitRules());
//			ResourceContainer container = FhirFactory.eINSTANCE.createResourceContainer();
//			container.setPatient(patient);
//			entry.setResource(container);
//			bundle.getEntry().add(entry);

		} catch (Exception e) {
			log.error("", e);
		}

		return bundle;
	}
	
	static ForecastPatient findPatient(Parameters parameters) {
		if (parameters.getParameter().size() > 0) {
			return (ForecastPatient) parameters.getParameter().get(0).getResource().getImmunization()
				.getPatient();
		}
		return null;
	}

	static List<TestEvent> createTestEvents(Parameters parameters) {
		List<TestEvent> events = new ArrayList<TestEvent>();
		ParametersParameter pp = findParametersParameter(ForecastUtil.FORECAST_PARAMETERs.IMMUNIZATIONS.code,
				parameters);
		for (ParametersParameter pp1 : pp.getPart()) {
			ResourceContainer rc = pp1.getResource();
			Immunization imm = rc.getImmunization();
			String code = imm.getVaccineCode().getCoding().get(0).getCode().getValue();
			DateTime dateTime = imm.getDate();
			TestEvent event = new TestEvent(Integer.parseInt(code), FHIRUtil.convert(dateTime));
			events.add(event);
		}
		return events;
	}

	static List<TestEvent> createTestEvents(List<String> strings) {
		List<TestEvent> events = new ArrayList<TestEvent>();
		int pos = 0;
		Integer hold = null;
		for (String s : strings) {
			if (pos % 2 == 0) {
				hold = Integer.parseInt(s);
			} else {
				TestEvent event = new TestEvent(hold.intValue(), parse(s));
				events.add(event);
			}
			pos++;
		}
		return events;
	}

	static java.util.Date parse(String s) {
		java.util.Date date = null;
		try {
			date = FHIRUtil.sdf.parse(s);
		} catch (ParseException e) {
			log.error("", e);
		}
		return date;
	}

	public static ParametersParameter findParametersParameter(java.lang.String name, Parameters parameters) {
		for (ParametersParameter pp : parameters.getParameter()) {
			if (name.equals(pp.getName().getValue())) {
				return pp;
			}
		}
		return null;
	}
}
