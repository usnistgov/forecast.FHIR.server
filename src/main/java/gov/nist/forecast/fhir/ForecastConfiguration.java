package gov.nist.forecast.fhir;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tch.fc.model.Service;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class ForecastConfiguration extends Configuration {

	private static Logger log = LoggerFactory.getLogger(ForecastConfiguration.class);

	Map<Service, URL> forecasters = new HashMap<Service, URL>();

	public Map<Service, URL> getForcasters() {
		return forecasters;
	}

	// This property corresponds to an entry in forecaster.yml.  
	// Here we are loading a map of forcaster servers.
	@JsonProperty("forecasters")
	public void setForcasters(Map<String, String> forecasters) {
		log.trace("setForcasters==>");
		try {
			for (Map.Entry<String, String> entry : forecasters.entrySet()) {
				log.debug(entry.getKey() + " " + entry.getValue());
				Service service = Service.getService(entry.getKey());
				if (service == null) {
					log.error("Service " + entry.getKey() + " is invalid and is not loaded. Fix your yaml.");
				} else {
					this.forecasters.put(service, new URL(entry.getValue()));
				}
			}
		} catch (MalformedURLException e) {
			log.error("", e);
		}
	}
}
