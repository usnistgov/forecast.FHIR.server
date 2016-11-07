package gov.nist.forecast.fhir.resources;

import java.net.URL;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Conformance;
import org.hl7.fhir.ImplementationGuide;
import org.hl7.fhir.Parameters;
import org.hl7.fhir.ParametersParameter;
import org.hl7.fhir.Reference;
import org.hl7.fhir.StructureDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tch.fc.model.Service;

import gov.nist.forecast.fhir.exceptions.ForecastException;
import gov.nist.forecast.fhir.exceptions.ForecastExceptionMapper;
import gov.nist.forecast.fhir.service.ConformanceService;
import gov.nist.forecast.fhir.service.ImmunizationRecommendationService;
import gov.nist.forecast.fhir.service.ImplementationGuideService;
import gov.nist.forecast.fhir.service.ProfileService;
import gov.nist.forecast.fhir.util.Const;
import gov.nist.forecast.fhir.util.Const.RESPONSE_CODE;

@Path("forecast")
public class IndexResource {

	private static Logger log = LoggerFactory.getLogger(IndexResource.class);

	public static final String serviceUrl = "http://tchforecasttester.org/fv/forecast";
	Map<Service, URL> forecasters;
	ForecastExceptionMapper fExMapper = new ForecastExceptionMapper();

	public IndexResource(Map<Service, URL> forecasters) {
		super();
		this.forecasters = forecasters;
		log.trace("start==>");
		for (Map.Entry<Service, URL> entry : forecasters.entrySet()) {
			log.debug(entry.getKey() + " " + entry.getValue());
		}
	}

	public IndexResource() {
		super();
		log.trace("start==>");
	}

	// We have this to determine if anything is even working. 
	// A response of healty indicates minimal functionality.
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String health() {
		log.info("healthy==>");
		return "healthy";
	}

	@GET
	@Path("/ImplementationGuide")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ImplementationGuide getImplementationGuide() {
		return ImplementationGuideService.getImplementationGuide();
	}

	// Conformance contains a collection of references to the profiles supported.
	// Use these to get a specific profile.
	@GET
	@Path("/Conformance")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Conformance getConformance() {
		log.trace("Conformance==>");
			Conformance conformance = ConformanceService.getConformance();
		log.trace("<==Conformance");
		return conformance;
	}
	
	@POST
	@Path("/ImmunizationRecommendations")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getImmunizationRecommendations(Parameters parameters) {
		log.trace("getImmunizationRecommendations==>" + parameters);
		ParametersParameter pp = ImmunizationRecommendationService.findParametersParameter("serviceType", parameters);
		java.lang.String serviceType = pp.getValueString().getValue();
		log.debug("serviceType=" + serviceType + " java.lang.String=" + (serviceType instanceof java.lang.String));
		Service service = Service.getService(serviceType);
		if (service == null) {
			return fExMapper.toResponse(new ForecastException(Const.RESPONSE_CODE.NOT_FOUND.responseCode,
					Const.getMessage(Const.RESPONSE_CODE.NOT_FOUND, serviceType)));
		}
		Bundle bundle = ImmunizationRecommendationService.getImmunizationRecommendation(parameters);

		log.trace("<==getImmunizationRecommendations");
		return Response.status(RESPONSE_CODE.OK.responseCode).entity(bundle).build();
	}

	@POST
	@Path("/Profile")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getProfile(Reference reference) {
		StructureDefinition profile = ProfileService.getProfile(reference);
		return Response.status(RESPONSE_CODE.OK.responseCode).entity(profile).build();
	}

}