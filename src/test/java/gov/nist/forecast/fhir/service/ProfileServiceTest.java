package gov.nist.forecast.fhir.service;

import static org.junit.Assert.assertNotNull;

import org.hl7.fhir.StructureDefinition;
import org.junit.Test;

import forecast.util.ForecastUtil;
import forecast.util.ForecastUtil.PROFILEs;

public class ProfileServiceTest {

	@Test
	public void testGetProfile() {
		for (ForecastUtil.PROFILEs prof : PROFILEs.values()) {
			StructureDefinition sut = ProfileService.getProfile(prof.reference);
			assertNotNull(sut);
		}
	}

}
