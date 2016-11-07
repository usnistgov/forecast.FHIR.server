package gov.nist.forecast.fhir.resources;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fhir.util.Serialize;

public abstract class AbstractEMFWriter<T extends EObject> implements MessageBodyWriter<T> {
	
	Logger log = LoggerFactory.getLogger(AbstractEMFWriter.class);

	public final String sURI;

	public AbstractEMFWriter(String sURI) {
		super();
		this.sURI = sURI;
	}

	@Override
	public long getSize(T arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4) {
		return 0;
	}

	@Override
	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return true;
	}

	@Override
	public void writeTo(T eObject, Class<?> clazz, Type type, Annotation[] ann, MediaType mt,
			MultivaluedMap<String, Object> mvm, java.io.OutputStream stream)
			throws IOException, WebApplicationException {
		log.trace("writeTo==>");
		Serialize save = new Serialize();
		save.it(eObject, sURI, stream);
	}
}
