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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tch.fc.model.Service;

import fhir.Bundle;
import fhir.Conformance;
import fhir.ImplementationGuide;
import fhir.Parameters;
import fhir.ParametersParameter;
import fhir.StructureDefinition;
import fhir.util.Load;
import fhir.util.Save;
import gov.nist.forecast.fhir.exceptions.ForecastException;
import gov.nist.forecast.fhir.exceptions.ForecastExceptionMapper;
import gov.nist.forecast.fhir.service.ConformanceService;
import gov.nist.forecast.fhir.service.ImmunizationRecommendationService;
import gov.nist.forecast.fhir.service.ImplementationGuideService;
import gov.nist.forecast.fhir.util.Const;
import gov.nist.forecast.fhir.util.Const.RESPONSE_CODE;

@Path("forecast")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
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
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String health() {
		log.info("healthy==>");
		return "healthy";
	}

	@GET
	@Path("/ImplementationGuide")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ImplementationGuide getImplementationGuide(@HeaderParam("content-type") String contentType) {
		return ImplementationGuideService.getImplementationGuide();
	}

	@GET
	@Path("/Conformance")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Conformance getConformance(@HeaderParam("content-type") String contentType) {
		log.trace("Conformance==>");
		log.debug("contentType=" + contentType);
		Conformance conformance = ConformanceService.getConformance();
		log.trace("<==Conformance");
		return conformance;
	}
	
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public void post(String patient) {
		log.debug("post==>");
		log.debug(patient);
		log.debug("<==post");
	}
	
	@POST
	@Path("/ForecastImmunizationRecommendations")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getImmunizationRecommendations(Parameters parameters) {
		log.trace("getImmunizationRecommendations==>" + parameters);
		log.trace("getImmunizationRecommendations==>" + Save.it(parameters, "xxx.xml"));
		ParametersParameter pp = ImmunizationRecommendationService.findParametersParameter("serviceType", parameters);
		String serviceType = pp.getValueString().toString();
		Service service = Service.getService(serviceType);
		if (service == null) {
			return fExMapper.toResponse(new ForecastException(Const.RESPONSE_CODE.NOT_FOUND.responseCode,
					Const.getMessage(Const.RESPONSE_CODE.NOT_FOUND, serviceType)));
		}
		Bundle bundle = ImmunizationRecommendationService.getImmunizationRecommendation(parameters);

		log.trace("<==getImmunizationRecommendations");
		return Response.status(RESPONSE_CODE.OK.responseCode).entity(parameters).build();
	}

	@GET
	@Path("/Profile/{name}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public StructureDefinition getProfile(@PathParam("name") String name) {
		log.trace("getProfile==>" + name);
		String fileName = "/" + name + ".structuredefinition.xml";
		log.trace("<==getProfile " + fileName);
		return (StructureDefinition) Load.it(fileName);
	}

}