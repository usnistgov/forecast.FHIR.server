package gov.nist.forecast.fhir.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.nist.forecast.fhir.ForecastApplication;

//This class is required to get JSON serialization done the EMF way.
//All EMF object are EObjects.
@Consumes(MediaType.APPLICATION_JSON)
public class EMF2JSONReader<T extends EObject> extends AbstractEMFReader<T> implements JSONReaderWriter {
	
	private static Logger log = LoggerFactory
			.getLogger(ForecastApplication.class);

	public EMF2JSONReader() {
		super(EXTENSION);
		log.trace("EMF2JSONReader==>" + EXTENSION);
	}	
}