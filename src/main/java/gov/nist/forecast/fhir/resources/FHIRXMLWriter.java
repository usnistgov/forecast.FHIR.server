package gov.nist.forecast.fhir.resources;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.hl7.fhir.FhirPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3._1999.xhtml.XhtmlPackage;


// This class is required to get XML serialization done the EMF way.
// All EMF object are EObjects.
@Produces(MediaType.APPLICATION_XML)
public class FHIRXMLWriter<T extends EObject> implements MessageBodyWriter<T> {
	
	Logger log = LoggerFactory.getLogger(FHIRXMLWriter.class);

	protected static ResourceSet resourceSet = new ResourceSetImpl();
	protected static Resource resource;

	
	static {
		// We must register these packages.
		FhirPackage.eINSTANCE.eClass();
		XhtmlPackage.eINSTANCE.eClass();
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		// We are not writing to a file so we create a dummy URI.
		resource = resourceSet.createResource(URI.createURI("xxx.xml"));
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
		resource.getContents().add((EObject) eObject);

		try {
			resource.save(stream, Collections.EMPTY_MAP);
		} catch (IOException e) {
			log.error("", e);
		}
	}
}
