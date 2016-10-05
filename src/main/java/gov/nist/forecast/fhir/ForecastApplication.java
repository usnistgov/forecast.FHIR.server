package gov.nist.forecast.fhir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fhir.Conformance;
import gov.nist.forecast.fhir.resources.FHIRMessageWriter;
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
				new IndexResource());
		environment.jersey().getResourceConfig().register(new FHIRMessageWriter<Conformance>());		
		log.trace("<==run");
	}

	public static void main(String[] args) throws Exception {
		log.trace("main==>");
		ForecastApplication app = new ForecastApplication();
		app.run(args);
		log.trace("<==main");
	}
}