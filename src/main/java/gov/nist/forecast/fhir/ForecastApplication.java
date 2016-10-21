package gov.nist.forecast.fhir;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.SerializationFeature;

import gov.nist.forecast.fhir.resources.FHIRXMLReader;
import gov.nist.forecast.fhir.resources.FHIRXMLWriter;
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
		environment.jersey().getResourceConfig().register(new FHIRXMLReader<EObject>());		
		environment.jersey().getResourceConfig().register(new FHIRXMLWriter<EObject>());		
		environment.getObjectMapper().configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		environment.getObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
//		environment.getObjectMapper().addMixIn(TheClass.class, TheClassMixin.class);
//		environment.getObjectMapper().addMixIn(ElementImpl.class, ElementMixin.class);
//		environment.getObjectMapper().addMixIn(ParametersImpl.class, ParametersMixin.class);
		log.trace("<==run");
	}

	public static void main(String[] args) throws Exception {
		log.trace("main==>");
		ForecastApplication app = new ForecastApplication();
		app.run(args);
		log.trace("<==main");
	}
}