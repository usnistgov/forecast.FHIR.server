package gov.nist.forecast.fhir.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import io.dropwizard.jersey.errors.ErrorMessage;

public class ForecastExceptionMapper implements ExceptionMapper<ForecastException> {

	@Override
	public Response toResponse(ForecastException ex) {
		return Response.status(ex.getStatus()).entity(new ErrorMessage(ex.getDeveloperMessage()))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
