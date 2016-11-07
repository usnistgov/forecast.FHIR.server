package gov.nist.forecast.fhir.resources;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.emf.ecore.EObject;


// This class is required to get JSON serialization done the EMF way.
// All EMF object are EObjects.
@Produces(MediaType.APPLICATION_JSON)
public class EMF2JSONWriter<T extends EObject> extends AbstractEMFWriter<T> implements JSONReaderWriter {

	public EMF2JSONWriter() {
		super(_URI);
		log.trace("EMF2JSONWriter==>" + _URI);
	}
}
