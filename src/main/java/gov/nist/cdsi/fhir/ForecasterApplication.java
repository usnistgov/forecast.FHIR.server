package gov.nist.cdsi.fhir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.nist.cdsi.fhir.resources.IndexResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ForecasterApplication extends Application<ForecasterConfiguration> {

	private static Logger log = LoggerFactory
			.getLogger(ForecasterApplication.class);

	@Override
	public void initialize(Bootstrap<ForecasterConfiguration> bootstrap) {
		log.debug("initialize==>");
	}

	@Override
	public void run(ForecasterConfiguration configuration, Environment environment)
			throws Exception {
		log.debug("run==>");
				
		environment.jersey().register(
				new IndexResource());
		log.debug("<==run");
	}

	public static void main(String[] args) throws Exception {
		log.debug("main==>");
		ForecasterApplication app = new ForecasterApplication();
		app.run(args);
		log.debug("<==main");
	}
}