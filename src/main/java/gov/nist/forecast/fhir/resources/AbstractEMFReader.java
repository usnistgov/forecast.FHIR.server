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

	public final String eXTENSION;

	public AbstractEMFReader(String eXTENSION) {
		super();
		this.eXTENSION = eXTENSION;
	}

	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] ann, MediaType media) {
		log.info("clazz=" + clazz);
		log.info("type=" + type);
		log.info("media=" + media);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T readFrom(Class<T> clazz, Type arg1, Annotation[] ann, MediaType arg3, MultivaluedMap<String, String> map,
			InputStream stream) throws IOException, WebApplicationException {
		log.trace("readFrom==>" + this);
		log.trace("it==> stream="  + stream + " eXTENSION=" + eXTENSION);
		DeSerialize load = new DeSerialize();
		EObject eObject = load.it(stream, eXTENSION);
		return (T) eObject;
	}
}
