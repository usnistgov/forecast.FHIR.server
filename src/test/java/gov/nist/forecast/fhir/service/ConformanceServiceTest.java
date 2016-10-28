package gov.nist.forecast.fhir.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hl7.fhir.Conformance;
import org.junit.Test;

import forecast.util.ForecastUtil;

public class ConformanceServiceTest {

	@Test
	public void testGetConformance() {
		Conformance sut = ConformanceService.getConformance();
		int profCount = ForecastUtil.PROFILEs.values().length;
		assertNotNull(sut);
		assertEquals(profCount, sut.getProfile().size());
	}

}
