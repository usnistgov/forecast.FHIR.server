package gov.nist.forecast.fhir.resources;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tch.fc.model.TestEvent;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class IndexResourceTest {
	
	@Test
	public void testJSON() {
		given().
        contentType(ContentType.JSON).
	    when().
	    get("/forecast").then().
	    body(containsString("healthy"));
		given().
	    when().
        contentType(ContentType.JSON).
	    get("/forecast").then().
	    log().body();
	}
	
	@Test
	public void testConformanceJSON() {
		given().
        contentType(ContentType.JSON).
	    when().
	    get("/forecast/Conformance").then().
	    body(containsString("Conformance"));
		given().
        accept(ContentType.JSON).
	    when().
	    get("/forecast/Conformance").then().log().body();		
	}
	
	@Test
	public void testConformanceXML() {
//		given().
//        contentType(ContentType.XML).
//	    when().
//	    get("/forecast/Conformance").then().
//	    body(containsString("Conformance"));
		given().accept(ContentType.XML).
	    when().
	    get("/forecast/Conformance").then().
	    log().body();		
	}
	
//	@Test
	public void testForecastImmunizationRecomendation() {
		given().
	    param("serviceType", "XXX").
	    param("evalDate", "2011-03-11").
	    param("birthDate", "2012-01-01").
	    param("gender", "M").
	    param("strings", "200").
	    param("strings", "2011-01-02").
	    param("strings", "300").
	    param("strings", "2011-01-03").
	    when().
//	    get("/forecast/ForecastImmunizationRecomendation").then().body(containsString("OK"));
	    get("/forecast/ForecastImmunizationRecomendation").then().log().body();
	}
	
//	@Test
	public void testGetProfile() {
		given().
	    when().
	    get("/forecast/Profile/ForecastPatient").then().log().body();
	}
///ForecastPatient
//	@Test
	public void testCreateTestEvents() {
		IndexResource app = new IndexResource();
		List<String> list = new ArrayList<String>();
		list.add("200");
		list.add("2011-01-02");
		list.add("300");
		list.add("2011-01-03");
		
		List<TestEvent> events = app.createTestEvents(list);
		assertNotNull(events);
		assertEquals(2, events.size());
	}

}
