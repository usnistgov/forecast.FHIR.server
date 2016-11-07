package gov.nist.forecast.fhir.resources;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.emf.ecore.EObject;


// This class is required to get XML serialization done the EMF way.
// All EMF object are EObjects.
@Produces(MediaType.APPLICATION_XML)
public class EMF2XMLWriter<T extends EObject> extends AbstractEMFWriter<T> implements XMLReaderWriter {

	public EMF2XMLWriter() {
		super(_URI);
		log.trace("EMF2XMLWriter==>" + _URI);
	}
}
