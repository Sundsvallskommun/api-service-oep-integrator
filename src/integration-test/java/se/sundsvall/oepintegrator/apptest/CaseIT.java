package se.sundsvall.oepintegrator.apptest;

import static java.text.MessageFormat.format;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;

@AutoConfigureWireMock(port = 9090, files = "classpath:/CaseIT")
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = Application.class)
@ActiveProfiles("it")
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class CaseIT extends AbstractAppTest {

	private static final String REQUEST_FILE = "request.json";
	private static final String RESPONSE_FILE = "response.json";
	private static final String PATH_GET_CASES_BY_FAMILY_ID = "/{0}/{1}/cases/families/{2}";
	private static final String PATH_GET_CASES_BY_PARTY_ID = "/{0}/{1}/cases/parties/{2}";
	private static final String PATH_GET_CASE_ATTACHMENT = "/{0}/{1}/cases/{2}/queries/{3}/files/{4}";
	private static final String PATH_SET_STATUS_BY_FLOW_INSTANCE_ID = "/{0}/{1}/cases/{2}/status";
	private static final String PATH_SET_STATUS_BY_EXTERNAL_ID = "/{0}/{1}/cases/systems/{2}/{3}/status";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String FAMILY_ID = "123";
	private static final String STATUS = "TheStatus";

	@Autowired
	private OpeneClientFactory openeClientFactory;

	@BeforeEach
	void beforeEach() {
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");
	}

	@Test
	void test01_getCasesByFamilyId() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH_GET_CASES_BY_FAMILY_ID + "?status={3}&fromDate={4}&toDate={5}", MUNICIPALITY_ID, EXTERNAL, FAMILY_ID, STATUS, "2020-01-01", "2025-12-31"))
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getCasesByFamilyIdWhenNotFoundInOep() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH_GET_CASES_BY_FAMILY_ID, MUNICIPALITY_ID, EXTERNAL, FAMILY_ID))
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getCasePdf() throws IOException {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format("/{0}/{1}/cases/{2}/pdf", MUNICIPALITY_ID, EXTERNAL, "123456789"))
			.withExpectedResponseStatus(OK)
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_PDF_VALUE))
			.withExpectedBinaryResponse("response/test.pdf")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getCasesByPartyId() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH_GET_CASES_BY_PARTY_ID + "?status={3}&fromDate={4}&toDate={5}", MUNICIPALITY_ID, EXTERNAL, "e19981ad-34b2-4e14-88f5-133f61ca85aa", STATUS, "2020-01-01", "2025-12-31"))
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_getCaseStatus() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format("/{0}/{1}/cases/{2}/status", MUNICIPALITY_ID, EXTERNAL, "123456789"))
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test06_getCaseAttachment() throws IOException {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH_GET_CASE_ATTACHMENT, MUNICIPALITY_ID, EXTERNAL, "123456789", "123", "456"))
			.withExpectedResponseStatus(OK)
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(IMAGE_PNG_VALUE))
			.withExpectedBinaryResponse("response/picture.png")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test07_setCaseStatusByFlowinstanceId() {
		setupCall()
			.withHttpMethod(PUT)
			.withServicePath(format(PATH_SET_STATUS_BY_FLOW_INSTANCE_ID, MUNICIPALITY_ID, EXTERNAL, "12345"))
			.withContentType(APPLICATION_JSON)
			.withRequest(REQUEST_FILE)
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test08_setCaseStatusByExternalId() {
		setupCall()
			.withHttpMethod(PUT)
			.withServicePath(format(PATH_SET_STATUS_BY_EXTERNAL_ID, MUNICIPALITY_ID, EXTERNAL, "System", "externalId"))
			.withContentType(APPLICATION_JSON)
			.withRequest(REQUEST_FILE)
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test09_getCaseXml() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format("/{0}/{1}/cases/{2}", MUNICIPALITY_ID, EXTERNAL, "123456789"))
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
