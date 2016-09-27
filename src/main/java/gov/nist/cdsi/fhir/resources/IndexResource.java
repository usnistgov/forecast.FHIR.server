package gov.nist.cdsi.fhir.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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

import cdsi.CdsiImmunizationRecomendation;
import cdsi.CdsiPatient;
import fhir.Bundle;
import fhir.BundleEntry;
import fhir.BundleType;
import fhir.BundleTypeList;
import fhir.Conformance;
import fhir.FhirFactory;
import fhir.ResourceContainer;
import gov.nist.cdsi.fhir.util.Util;


@Path("tch")
@Produces(MediaType.APPLICATION_JSON)
public class IndexResource {

	private static Logger log = LoggerFactory.getLogger(IndexResource.class);
    
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String serviceUrl = "http://tchforecasttester.org/fv/forecast";
    public static final Service service = Service.TCH;

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
	public Conformance getConformance() {
		log.trace("Conformance==>");
		Conformance conformance = FhirFactory.eINSTANCE.createConformance();
		conformance.setUrl(Util.URIs.CDSI_CONFORMANCE.uri);
		conformance.setStatus(Util.CONFORMANCE_STATUS.DRAFT.code);
		conformance.setExperimental(Util.BOOLEAN.TRUE.bool);
		log.trace("<==Conformance");
		return conformance;
	}

	@GET
	@Path("/CdsiImmunizationRecomendation")
	public Bundle getImmunizationReccomendations(@QueryParam("evalDate") String evalDate, @QueryParam("birthDate") String birthDate, @QueryParam("gender") String gender, @QueryParam("strings") List<String> strings) {
		log.trace("getImmunizationReccomendations==>");
    	Bundle list = FhirFactory.eINSTANCE.createBundle();
    	list.setId(Util.createId());
    	BundleType type = FhirFactory.eINSTANCE.createBundleType();
    	type.setValue(BundleTypeList.SEARCHSET);
    	list.setType(type);
    	
	    Software software = new Software();
	    software.setServiceUrl(serviceUrl);
	    software.setService(service);
	    TestCase testCase = new TestCase();
	    testCase.setEvalDate(parse(evalDate));
	    testCase.setPatientSex(gender);
	    testCase.setPatientDob(parse(birthDate));
	    List<TestEvent> events = createTestEvents(strings);
	    log.debug("events=" + events);
	    testCase.setTestEventList(events);
	    CdsiPatient patient = (CdsiPatient) Util.convert(testCase);
	    ConnectorInterface connector;
		try {
			connector = ConnectFactory.createConnecter(software, VaccineGroup.getForecastItemList());
		    java.util.List<ForecastActual> forecastActualList = connector.queryForForecast(testCase, new SoftwareResult());
		    log.trace("forecastActualList=" + forecastActualList.size());
		    for (ForecastActual forecastActual  : forecastActualList) {
			    String uuid = UUID.randomUUID().toString();
		    	log.debug("id=" + uuid);
		    	CdsiImmunizationRecomendation recommendation = Util.convert(uuid, forecastActual, testCase);
		    	BundleEntry entry = FhirFactory.eINSTANCE.createBundleEntry();
		    	entry.setId(recommendation.getIdentifier().get(0).toString());
		    	entry.setFullUrl(recommendation.getImplicitRules());
		    	ResourceContainer container = FhirFactory.eINSTANCE.createResourceContainer();
		    	container.setImmunizationRecommendation(recommendation);
		    	entry.setResource(container);
		    	list.getEntry().add(entry);
		    }
	    	BundleEntry entry = FhirFactory.eINSTANCE.createBundleEntry();
	    	entry.setId(patient.getIdentifier().get(0).getId());
	    	entry.setFullUrl(patient.getImplicitRules());
	    	ResourceContainer container = FhirFactory.eINSTANCE.createResourceContainer();
	    	container.setPatient(patient);
	    	entry.setResource(container);
	    	list.getEntry().add(entry);
		    
		} catch (Exception e) {
			log.error("" , e);
		}

		log.trace("<==getImmunizationReccomendations");
		return list;
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
			if(pos % 2 == 0) {
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