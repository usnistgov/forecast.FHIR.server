package gov.nist.forecast.fhir.resources;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import fhir.Element;
import fhir.Extension;
import fhir.FhirFactory;
import fhir.Parameters;
import fhir.impl.ParametersImpl;
import fhir.util.FHIRUtil;
import fhir.util.Save;
import gov.nist.forecast.fhir.util.ElementMixin;
import gov.nist.forecast.fhir.util.ExtensionMixin;
import gov.nist.forecast.fhir.util.TheClassMixin;
import try_.TheClass;
import try_.TryFactory;

public class IndexResourceTest {

	private static Logger log = LoggerFactory.getLogger(IndexResourceTest.class);

	// @Test
	public void testJSON() {
		given().accept(ContentType.JSON).when().get("/forecast").then().body(containsString("healthy"));
	}

	// @Test
	public void testImplementationGuideJSON() {
		given().accept(ContentType.JSON).when().get("/forecast/ImplementationGuide").then()
				.body(containsString("ImplementationGuide"));
		given().accept(ContentType.JSON).when().get("/forecast/ImplementationGuide").then().log().body();
	}

	// @Test
	public void testImplementationGuideXML() {
		given().accept(ContentType.XML).when().get("/forecast/ImplementationGuide").then()
				.body(containsString("ImplementationGuide"));
		given().accept(ContentType.XML).when().get("/forecast/ImplementationGuide").then().log().body();
	}

	// @Test
	public void testConformanceJSON() {
		given().accept(ContentType.JSON).when().get("/forecast/Conformance").then().body(containsString("Conformance"));
		given().accept(ContentType.JSON).when().get("/forecast/Conformance").then().log().body();
	}

	// @Test
	public void testConformanceXML() {
		given().accept(ContentType.XML).when().get("/forecast/Conformance").then().body(containsString("Conformance"));
		given().accept(ContentType.XML).when().get("/forecast/Conformance").then().log().body();
	}

	// @Test
	public void testForecastImmunizationRecomendationTCH_JSON() {
		runQuery("TCH", ContentType.JSON);
	}

	// @Test
	public void testForecastImmunizationRecomendationTCH_XML() {
		runQuery("TCH", ContentType.XML);
	}

	// @Test
	public void testForecastImmunizationRecomendationSWP_JSON() {
		runQuery("SWP", ContentType.JSON);
	}

	// @Test
	public void testForecastImmunizationRecomendationSWP_XML() {
		runQuery("SWP", ContentType.XML);
	}

	// @Test
	public void testForecastImmunizationRecomendationSTC_JSON() {
		runQuery("STC", ContentType.JSON);
	}

	// @Test
	public void testForecastImmunizationRecomendationSTC_XML() {
		runQuery("STC", ContentType.XML);
	}

	// @Test
	public void testForecastImmunizationRecomendationICE_JSON() {
		runQuery("ICE", ContentType.JSON);
	}

	// @Test
	public void testForecastImmunizationRecomendationICE_XML() {
		runQuery("ICE", ContentType.XML);
	}

	@SuppressWarnings("static-access")
	void runQuery(String serviceType, ContentType type) {
		given().accept(type.XML).param("serviceType", serviceType).param("evalDate", "2011-03-11")
				.param("birthDate", "2012-01-01").param("gender", "M").param("strings", "08")
				.param("strings", "2011-01-02").param("strings", "42").param("strings", "2011-01-02")
				.param("strings", "53").param("strings", "2011-01-03").when().
				// get("/forecast/ForecastImmunizationRecomendation").then().body(containsString("OK"));
				// get("/forecast/ForecastImmunizationRecomendation").then().log().body();
				get("/forecast/ForecastImmunizationRecomendation").then().extract().response();

	}

	// @Test
	public void testGetProfileXML() {
		Response response = given().accept(ContentType.XML).when().get("/forecast/Profile/ForecastPatient").then()
				.extract().response();
		String s = response.asString();
	}

	// @Test
	public void testGetProfileJSON() {
		Response response = given().accept(ContentType.JSON).when().get("/forecast/Profile/ForecastPatient").then()
				.extract().response();
		String s = response.asString();
	}

	@Test
	public void testPatientJSON() {
		String fp = "{'patient' : 'the pat'}";
		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(fp).when().post("/forecast/post").then()
				.statusCode(200);
	}

	@Test
	public void testParametersJSON() {
		Parameters parameters;
		try {
			parameters = createParameters();
			String xml = Save.it(parameters, "xxx.xml");
			given().contentType(ContentType.XML).accept(ContentType.XML).body(xml).when()
			.post("/forecast/ForecastImmunizationRecommendations").then().statusCode(200);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testParametersJSON1() {
		TheClass tc0 = TryFactory.eINSTANCE.createTheClass();
		Element el0 = FhirFactory.eINSTANCE.createElement();
		try {
//			fp = ImmunizationRecommendationServiceTest.createParameters("TCH");
//			fp = createParameters();
			tc0.setId("CBATC");
			el0.setId("CBAEL");
			ObjectMapper mapper = new ObjectMapper();
			mapper.addMixIn(TheClass.class, TheClassMixin.class);
			mapper.addMixIn(Element.class, ElementMixin.class);
			mapper.addMixIn(Extension.class, ExtensionMixin.class);
//			mapper.registerModule(new EMFModule());
			
			StringWriter writertc = new StringWriter();
			mapper.writeValue(writertc, tc0);
			log.debug("TheClass=" + writertc.toString());
			log.debug("TheClass tc0=" + Save.it(tc0, "xxx.xml"));
			
			StringReader readertc = new StringReader(writertc.toString());
	//		TheClass tc1 = mapper.readValue(readertc, TheClass.class);
	//		log.debug("TheClass tc1=" + Save.it(tc1, "xxx.xml"));
			
			StringWriter writerel = new StringWriter();
			mapper.writeValue(writerel, el0);
			log.debug("TheClass=" + writerel.toString());
			log.debug("TheClass el0=" + Save.it(el0, "xxx.xml"));
			
			StringReader readerel = new StringReader(writerel.toString());
			Element el1 = mapper.readValue(readerel, Element.class);
			log.debug("TheClass el1=" + Save.it(el1, "xxx.xml"));
//			given().contentType(ContentType.JSON).accept(ContentType.JSON).body(fp).when()
//					.post("/forecast/ForecastImmunizationRecommendations").then().statusCode(200);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ParametersImpl createParameters() throws ParseException {
//		ParametersImpl parameters = (ParametersImpl)FhirFactory.eINSTANCE.createParameters();
		ParametersImpl parameters = new ParametersImpl();
		parameters.setId(FHIRUtil.createId());
		return parameters;
	}

}
