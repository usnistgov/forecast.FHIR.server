package gov.nist.forecast.fhir;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.SerializationFeature;

import gov.nist.forecast.fhir.resources.EMF2JSONReader;
import gov.nist.forecast.fhir.resources.EMF2JSONWriter;
import gov.nist.forecast.fhir.resources.EMF2XMLReader;
import gov.nist.forecast.fhir.resources.EMF2XMLWriter;
import gov.nist.forecast.fhir.resources.IndexResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ForecastApplication extends Application<ForecastConfiguration> {

	private static Logger log = LoggerFactory
			.getLogger(ForecastApplication.class);

	@Override
	public void initialize(Bootstrap<ForecastConfiguration> bootstrap) {
		log.trace("initialize==>");
	}

	@Override
	public void run(ForecastConfiguration configuration, Environment environment)
			throws Exception {
		log.trace("run==>");
				
		environment.jersey().register(
				new IndexResource(configuration.getForcasters()));
		// We have to register our XML serializer and deserializer with jersey. 
		environment.jersey().getResourceConfig().register(new EMF2XMLReader<EObject>());		
		environment.jersey().getResourceConfig().register(new EMF2XMLWriter<EObject>());		
		environment.jersey().getResourceConfig().register(new EMF2JSONReader<EObject>());		
		environment.jersey().getResourceConfig().register(new EMF2JSONWriter<EObject>());	
		log.debug("hasEMF2XMLReader=" + environment.jersey().getResourceConfig().isRegistered(EMF2XMLReader.class));
		log.debug("hasEMF2XMLWriter=" + environment.jersey().getResourceConfig().isRegistered(EMF2XMLWriter.class));
		log.debug("hasEMF2JSONReader=" + environment.jersey().getResourceConfig().isRegistered(EMF2JSONReader.class));
		log.debug("hasEMF2JSONWriter=" + environment.jersey().getResourceConfig().isRegistered(EMF2JSONWriter.class));
		environment.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		environment.getObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
		log.trace("<==run");
	}

	public static void main(String[] args) throws Exception {
		log.trace("main==>");
		ForecastApplication app = new ForecastApplication();
		app.run(args);
		log.trace("<==main");
	}
}