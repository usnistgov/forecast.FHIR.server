package gov.nist.forecast.fhir.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import fhir.String;
import fhir.util.FHIRUtil;

public class UtilTest {

	@Test
	public void testConvertStringForecastActualTestCase() {
//		Util.convert(date, vaccineCvx)
	}

	@Test
	public void testConvertURN() {
		String s = FHIRUtil.convert("XYZ");
		String urn = FHIRUtil.createURN(s);
		assertNotNull(urn);
		java.lang.String[] ss = urn.getValue().split(":");
		assertEquals("urn", ss[0]);
		assertEquals("uuid", ss[1]);
		assertEquals("XYZ", ss[2]);
	}
}
