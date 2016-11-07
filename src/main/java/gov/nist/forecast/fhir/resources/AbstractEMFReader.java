package gov.nist.forecast.fhir.resources;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fhir.util.DeSerialize;

public abstract class AbstractEMFReader<T extends EObject> implements MessageBodyReader<T> {

	Logger log = LoggerFactory.getLogger(AbstractEMFReader.class);

	public final String sURI;

	public AbstractEMFReader(String sURI) {
		super();
		this.sURI = sURI;
	}

	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] ann, MediaType media) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T readFrom(Class<T> clazz, Type arg1, Annotation[] ann, MediaType arg3, MultivaluedMap<String, String> map,
			InputStream stream) throws IOException, WebApplicationException {
		log.trace("readFrom==>" + this);
		log.trace("it==> stream="  + stream + " sURI=" + sURI);
		DeSerialize load = new DeSerialize();
		EObject eObject = load.it(stream, sURI);
		return (T) eObject;
	}
}
