package gov.nist.cdsi.fhir.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.TestCase;

import cdsi.CdsiFactory;
import cdsi.CdsiImmunizationRecomendation;
import cdsi.CdsiImmunizationRecommendationRecommendation;
import cdsi.CdsiPatient;
import fhir.AdministrativeGenderList;
import fhir.Code;
import fhir.CodeableConcept;
import fhir.Date;
import fhir.DateTime;
import fhir.FhirFactory;
import fhir.Id;
import fhir.Identifier;
import fhir.ImmunizationRecommendationRecommendation;
import fhir.Meta;
import fhir.Reference;
import fhir.String;
import fhir.Uri;

public class Util {

	private static Logger log = LoggerFactory.getLogger(Util.class);

	public static final java.lang.String URL = "http://localhost/tch/CdsiImmunizationRecomendation";

	public static final String ABSOLUTE_URL = convert(URL);

	static Map<String, Uri> mapProfile = new HashMap<String, Uri>();

	public enum CONFORMANCE_STATUS {
		DRAFT("draft"), ACTIVE("active"), RETIRED("retired");

		public final Code code;

		CONFORMANCE_STATUS(java.lang.String code) {
			this.code = FhirFactory.eINSTANCE.createCode();
			this.code.setId(createId().getValue());
			this.code.setValue(code);
		}
	}

	public enum URIs {

		CDSI_PATIENT("StructureDefinition/CdsiPatient"), CDSI_IMMUNIZATION(
				"StructureDefinition/CdsiImmunization"), CDSI_IMMUNIZATIONRECOMMENDATION(
						"StructureDefinition/CdsiImmunizationRecomendation"), CDSI_IMMUNIZATIONRECOMMENDATIONRECOMMENDATION(
								"StructureDefinition/ImmunizationRecommendationRecommendation"), CDSI_CONFORMANCE(
										"Conformance");

		public final Uri uri;

		URIs(java.lang.String uri) {
			this.uri = FhirFactory.eINSTANCE.createUri();
			this.uri.setId(createId().getValue());
			this.uri.setValue(uri);
		}
	}

	public enum BOOLEAN {
		
		TRUE(true),
		FALSE(false);
		
		public final fhir.Boolean bool;
		
		BOOLEAN(java.lang.Boolean bool) {
			this.bool = FhirFactory.eINSTANCE.createBoolean();
			this.bool.setValue(bool);
		}
	}
	
	public static CdsiImmunizationRecomendation convert(java.lang.String id, ForecastActual i, TestCase testCase) {
		CdsiImmunizationRecomendation o = CdsiFactory.eINSTANCE.createCdsiImmunizationRecomendation();
		o.setId(createId());
		o.setMeta(createMeta(URIs.CDSI_IMMUNIZATIONRECOMMENDATION));
		o.setPatient(createReference(convert(testCase)));
		log.debug("vaccineGroup=" + i.getVaccineGroup());
		ImmunizationRecommendationRecommendation irr = convert(i.getDueDate(), i.getVaccineGroup().getVaccineCvx());
		o.getRecommendation().add(irr);
		return o;
	}

	public static ImmunizationRecommendationRecommendation convert(java.util.Date date, java.lang.String vaccineCvx) {
		log.debug("vaccineCvx=" + vaccineCvx);
		CdsiImmunizationRecommendationRecommendation o = CdsiFactory.eINSTANCE
				.createCdsiImmunizationRecommendationRecommendation();
		o.setId(createId().getValue());
		o.setDate(convertDateTime(date));
		CodeableConcept code = FhirFactory.eINSTANCE.createCodeableConcept();
		code.setText(convert(vaccineCvx));
		o.setVaccineCode(code);
		return (ImmunizationRecommendationRecommendation) o;
	}

	public static CdsiPatient convert(TestCase i) {
		log.trace("convert==> i=" + i);
		CdsiPatient o = CdsiFactory.eINSTANCE.createCdsiPatient();
		o.setId(createId());
		o.setMeta(createMeta(URIs.CDSI_PATIENT));
		o.setGender(convertGender(i.getPatientSex()));
		o.setBirthDate(convert(i.getPatientDob()));
		return o;
	}

	public static Meta createMeta(URIs profile) {
		Meta meta = FhirFactory.eINSTANCE.createMeta();
		meta.setId(createId().getValue());
		meta.getProfile().add(profile.uri);
		return meta;
	}

	public static Reference createReference(CdsiPatient i) {
		Reference o = FhirFactory.eINSTANCE.createReference();
		o.setId(createId().getValue());
		o.setReference(createURN(i.getIdentifier().get(0)));
		return o;
	}

	static Code convertGender(java.lang.String gender) {
		Code code = FhirFactory.eINSTANCE.createCode();
		if ("M".equals(gender)) {
			code.setValue(AdministrativeGenderList.MALE.name());
		} else if ("F".equals(gender)) {
			code.setValue(AdministrativeGenderList.FEMALE.name());
		} else {
			code.setValue(AdministrativeGenderList.OTHER.name());
		}
		return code;
	}

	public static Date convert(java.util.Date i) {
		XMLGregorianCalendar xgc = checkDateInput(i);
		Date date = FhirFactory.eINSTANCE.createDate();
		date.setValue(xgc);
		return date;
	}

	public static DateTime convertDateTime(java.util.Date i) {
		XMLGregorianCalendar xgc = checkDateInput(i);
		DateTime date = FhirFactory.eINSTANCE.createDateTime();
		date.setValue(xgc);
		return date;
	}

	public static XMLGregorianCalendar convertString2XMLCalendar(java.lang.String i) {
		XMLGregorianCalendar xgc = null;
		try {
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(i);
		} catch (DatatypeConfigurationException e) {
			log.error("", e);
		}
		return xgc;
	}

	public static String convert(java.lang.String i) {
		String s = FhirFactory.eINSTANCE.createString();
		s.setValue(i);
		return s;
	}

	public static XMLGregorianCalendar checkDateInput(java.util.Date i) {
		XMLGregorianCalendar xgc = null;
		if (i == null) {
			return null;
		}
		try {
			Calendar gcal = GregorianCalendar.getInstance();
			gcal.setTime(i);
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar) gcal);
		} catch (DatatypeConfigurationException e) {
			log.error("", e);
		}
		return xgc;
	}

	public static String createURN(Identifier i) {
		return createURN(i.getValue());
	}

	public static String createURN(String i) {
		StringBuilder bld = new StringBuilder();
		bld.append("urn");
		bld.append(":");
		bld.append("uuid");
		bld.append(":");
		bld.append(i.getValue());
		return Util.convert(bld.toString());
	}

	public static Id createId() {
		Id id = FhirFactory.eINSTANCE.createId();
		id.setValue(UUID.randomUUID().toString());
		return id;
	}

	public static Identifier createIdentifier() {
		Identifier id = FhirFactory.eINSTANCE.createIdentifier();
		id.setValue(createUuid());
		return id;
	}

	public static String createUuid() {
		return Util.convert(UUID.randomUUID().toString());
	}

}
