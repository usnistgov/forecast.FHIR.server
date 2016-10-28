package gov.nist.forecast.fhir.service;

import static org.junit.Assert.assertNotNull;

import org.hl7.fhir.ImplementationGuide;
import org.junit.Test;

public class ImplementationGuildeServiceTest {

	@Test
	public void testGetImplementationGuideService() {
		ImplementationGuide ig = ImplementationGuideService.getImplementationGuide();
		assertNotNull(ig);
	}

}
