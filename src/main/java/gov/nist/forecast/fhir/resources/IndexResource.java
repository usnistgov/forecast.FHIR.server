package gov.nist.forecast.fhir.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import fhir.Conformance;
import fhir.FhirFactory;
import fhir.ResourceContainer;
import fhir.StructureDefinition;
import fhir.util.FHIRUtil;
import fhir.util.Load;
import forecast.ForecastImmunizationRecomendation;
import forecast.ForecastPatient;
import forecast.util.ForecastUtil;
import gov.nist.forecast.fhir.exceptions.ForecastException;
import gov.nist.forecast.fhir.exceptions.ForecastExceptionMapper;
import gov.nist.forecast.fhir.util.Const;
import gov.nist.forecast.fhir.util.Const.RESPONSE_CODE;

@Path("forecast")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class IndexResource {

	private static Logger log = LoggerFactory.getLogger(IndexResource.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String serviceUrl = "http://tchforecasttester.org/fv/forecast";
	ForecastExceptionMapper fExMapper = new ForecastExceptionMapper();

	public IndexResource() {
		super();
		log.trace("start==>");
	}

	@GET
	public String health() {
		log.info("healthy==>");
		return "healthy";
	}

	@GET
	@Path("/Conformance")
	@Produces(MediaType.APPLICATION_XML)
	public Conformance getConformance(@HeaderParam("content-type") String contentType) {
		log.trace("Conformance==>");
		log.debug("contentType=" + contentType);
		Conformance conformance = FhirFactory.eINSTANCE.createConformance();
		conformance.setId(FHIRUtil.createId());
		conformance.setUrl(ForecastUtil.URIs.FORECAST_CONFORMANCE.uri);
		conformance.setStatus(FHIRUtil.CONFORMANCE_STATUS.DRAFT.code);
		conformance.setExperimental(FHIRUtil.BOOLEAN.TRUE.bool);
		conformance.setFhirVersion(FHIRUtil.createId());
		conformance.setDate(FHIRUtil.convertDateTime(new Date()));
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_IMMUNIZATIONRECOMMENDATIONRECOMMENDATION.reference);
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_IMMUNIZATIONRECOMMENDATION.reference);
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_IMMUNIZATION.reference);
		conformance.getProfile().add(ForecastUtil.PROFILEs.FORECAST_PATIENT.reference);
		conformance.getFormat().add(FHIRUtil.FORMAT.JSON.code);
		conformance.getFormat().add(FHIRUtil.FORMAT.XML.code);
		log.trace("<==Conformance");
		return conformance;
	}

	@GET
	@Path("/ForecastImmunizationRecomendation")
	public Response getImmunizationReccomendations(@QueryParam("serviceType") String serviceType,
			@QueryParam("evalDate") String evalDate, @QueryParam("birthDate") String birthDate,
			@QueryParam("gender") String gender, @QueryParam("strings") List<String> strings) {
		log.trace("getImmunizationReccomendations==>"
				+ String.format("serviceType=%s evalDate=%s birthDate=%s gender=%s strings=%s", serviceType, evalDate,
						birthDate, gender, strings));
		Bundle bundle = FhirFactory.eINSTANCE.createBundle();
		bundle.setId(FHIRUtil.createId());
		BundleType type = FhirFactory.eINSTANCE.createBundleType();
		type.setValue(BundleTypeList.SEARCHSET);
		bundle.setType(type);

		Software software = new Software();
		software.setServiceUrl(serviceUrl);
		Service service = Service.getService(serviceType);
		if (service == null) {
			return fExMapper.toResponse(new ForecastException(Const.RESPONSE_CODE.NOT_FOUND.responseCode,
					Const.getMessage(Const.RESPONSE_CODE.NOT_FOUND, serviceType)));
		}
		software.setService(Service.getService(serviceType));
		TestCase testCase = new TestCase();
		testCase.setEvalDate(parse(evalDate));
		testCase.setPatientSex(gender);
		testCase.setPatientDob(parse(birthDate));
		List<TestEvent> events = createTestEvents(strings);
		log.debug("events=" + events);
		testCase.setTestEventList(events);
		ForecastPatient patient = (ForecastPatient) ForecastUtil.convert(testCase);
		ConnectorInterface connector;
		try {
			connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
			java.util.List<ForecastActual> forecastActualList = connector.queryForForecast(testCase,
					new SoftwareResult());
			log.trace("forecastActualList=" + forecastActualList.size());
			for (ForecastActual forecastActual : forecastActualList) {
				String uuid = UUID.randomUUID().toString();
				log.debug("id=" + uuid);
				ForecastImmunizationRecomendation recommendation = ForecastUtil.convert(uuid, forecastActual, testCase);
				BundleEntry entry = FhirFactory.eINSTANCE.createBundleEntry();
				entry.setId(recommendation.getIdentifier().get(0).toString());
				entry.setFullUrl(recommendation.getImplicitRules());
				ResourceContainer container = FhirFactory.eINSTANCE.createResourceContainer();
				container.setImmunizationRecommendation(recommendation);
				entry.setResource(container);
				bundle.getEntry().add(entry);
			}
			BundleEntry entry = FhirFactory.eINSTANCE.createBundleEntry();
			entry.setId(patient.getIdentifier().get(0).getId());
			entry.setFullUrl(patient.getImplicitRules());
			ResourceContainer container = FhirFactory.eINSTANCE.createResourceContainer();
			container.setPatient(patient);
			entry.setResource(container);
			bundle.getEntry().add(entry);

		} catch (Exception e) {
			log.error("", e);
			return fExMapper.toResponse(new ForecastException(Const.RESPONSE_CODE.SERVER_ERROR.responseCode,
					Const.getMessage(Const.RESPONSE_CODE.SERVER_ERROR, e.getMessage())));
		}

		log.trace("<==getImmunizationReccomendations");
		return Response.status(RESPONSE_CODE.OK.responseCode).entity(bundle).build();
	}

	@GET
	@Path("/Profile/{name}")
	public StructureDefinition getProfile(@PathParam("name") String name) {
		log.trace("getProfile==>" + name);
		String fileName = "/" + name + ".structuredefinition.xml";
		log.trace("<==getProfile " + fileName);
		return (StructureDefinition) Load.it(fileName);
	}

	java.util.Date parse(String s) {
		java.util.Date date = null;
		try {
			date = sdf.parse(s);
		} catch (ParseException e) {
			log.error("", e);
		}
		return date;
	}

	List<TestEvent> createTestEvents(List<String> strings) {
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
}