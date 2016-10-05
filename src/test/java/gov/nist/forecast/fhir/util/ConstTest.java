package gov.nist.forecast.fhir.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConstTest {

	@Test
	public void testGetMessage() {
		System.out.println(Const.getMessage(Const.RESPONSE_CODE.NOT_FOUND, "XXX"));
		assertNotNull(Const.getMessage(Const.RESPONSE_CODE.NOT_FOUND, "XXX"));
	}
}
