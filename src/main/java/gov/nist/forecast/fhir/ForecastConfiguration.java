package gov.nist.forecast.fhir;


import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecastConfiguration extends Configuration {
    
	private static Logger log = LoggerFactory.getLogger(ForecastConfiguration.class);
	
	
}
