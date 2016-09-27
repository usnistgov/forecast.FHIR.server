package gov.nist.cdsi.fhir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import fhir.Identifier;
import fhir.String;

public class UtilTest {

	@Test
	public void testConvertStringForecastActualTestCase() {
//		Util.convert(date, vaccineCvx)
	}

	@Test
	public void testConvertURN() {
		String s = Util.convert("XYZ");
		String urn = Util.createURN(s);
		assertNotNull(urn);
		java.lang.String[] ss = urn.getValue().split(":");
		assertEquals("urn", ss[0]);
		assertEquals("uuid", ss[1]);
		assertEquals("XYZ", ss[2]);
	}
}
