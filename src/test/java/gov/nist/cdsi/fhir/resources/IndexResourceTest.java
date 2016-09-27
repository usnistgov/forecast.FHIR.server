package gov.nist.cdsi.fhir.resources;

import static com.jayway.restassured.RestAssured.given;

import org.junit.Test;

public class IndexResourceTest {
	
	@Test
	public void test() {
		given().
	    when().
//	    get("/tch/Conformance").then().body(containsString("OK"));
	    get("/tch").then().log().body();		
	}
	
//	@Test
	public void testConformance() {
		given().
	    when().
//	    get("/tch/Conformance").then().body(containsString("OK"));
	    get("/tch/Conformance").then().log().body();		
	}
	
//	@Test
	public void testCdsiImmunizationRecomendation() {
		given().
	    param("evalDate", "2011-03-11").
	    param("birthDate", "2012-01-01").
	    param("gender", "M").
	    param("strings", "200").
	    param("strings", "2011-01-02").
	    param("strings", "300").
	    param("strings", "2011-01-03").
	    when().
//	    get("/tch/CdsiImmunizationRecomendation").then().body(containsString("OK"));
	    get("/tch/CdsiImmunizationRecomendation").then().log().body();
	}

//	@Test
	public void testCreateTestEvents() {
//		IndexResource app = new IndexResource();
//		List<String> list = new ArrayList<String>();
//		list.add("200");
//		list.add("2011-01-02");
//		list.add("300");
//		list.add("2011-01-03");
//		
//		List<TestEvent> events = app.createTestEvents(list);
//		assertNotNull(events);
//		assertEquals(2, events.size());
	}

}
