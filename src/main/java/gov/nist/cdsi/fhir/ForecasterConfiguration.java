package gov.nist.cdsi.fhir;


import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecasterConfiguration extends Configuration {
    
	private static Logger log = LoggerFactory.getLogger(ForecasterConfiguration.class);
	
//    @Valid
//    @NotNull
//    private DataSourceFactory database = new DataSourceFactory();
//
//    @JsonProperty("database")
//    public DataSourceFactory getDataSourceFactory() {
//        return database;
//    }
// 
//    @JsonProperty("database")
//    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
//        this.database = dataSourceFactory;
//    }
}
