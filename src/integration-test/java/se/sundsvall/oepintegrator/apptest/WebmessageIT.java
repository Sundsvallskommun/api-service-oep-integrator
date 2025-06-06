package se.sundsvall.oepintegrator.apptest;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
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

@AutoConfigureWireMock(port = 9090, files = "classpath:/WebmessageIT")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ActiveProfiles("it")
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class WebmessageIT extends AbstractAppTest {

	private static final String REQUEST_FILE = "request.json";
	private static final String RESPONSE_FILE = "response.json";
	private static final String PATH = "/{0}/{1}/webmessages";
	private static final String MUNICIPALITY_ID = "2281";

	@Autowired
	private OpeneClientFactory openeClientFactory;

	@BeforeEach
	void beforeEach() {
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");
	}

	@Test
	void test01_createWebmessageByAdministratorId() throws FileNotFoundException {
		setupCall()
			.withHttpMethod(POST)
			.withServicePath(format(PATH, MUNICIPALITY_ID, EXTERNAL))
			.withContentType(MULTIPART_FORM_DATA)
			.withRequestFile("request", REQUEST_FILE)
			.withRequestFile("attachments", "test.txt")
			.withRequestFile("attachments", "test2.txt")
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of("/2281/webmessages/151"))
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_createWebmessageByPartyId() throws FileNotFoundException {
		setupCall()
			.withHttpMethod(POST)
			.withServicePath(format(PATH, MUNICIPALITY_ID, EXTERNAL))
			.withContentType(MULTIPART_FORM_DATA)
			.withRequestFile("request", REQUEST_FILE)
			.withRequestFile("attachments", "test.txt")
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of("/2281/webmessages/152"))
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_createWebmessageByPartyIdNotFoundInParty() throws FileNotFoundException {
		setupCall()
			.withHttpMethod(POST)
			.withServicePath(format(PATH, MUNICIPALITY_ID, EXTERNAL))
			.withContentType(MULTIPART_FORM_DATA)
			.withRequestFile("request", REQUEST_FILE)
			.withRequestFile("attachments", "test.txt")
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_createWebmessageByUserId() throws FileNotFoundException {
		setupCall()
			.withHttpMethod(POST)
			.withServicePath(format(PATH, MUNICIPALITY_ID, EXTERNAL))
			.withContentType(MULTIPART_FORM_DATA)
			.withRequestFile("request", REQUEST_FILE)
			.withRequestFile("attachments", "test.txt")
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of("/2281/webmessages/153"))
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_getWebmessagesByFamilyId() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH + ("/families/{2}?fromDateTime={3}&toDateTime={4}"), MUNICIPALITY_ID, EXTERNAL, "123", LocalDateTime.now().minusDays(7), LocalDateTime.now()))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test06_getWebmessagesByFamilyId_emptyResult() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH + ("/families/{2}?fromDateTime={3}&toDateTime={4}"), MUNICIPALITY_ID, EXTERNAL, "123", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test07_getWebmessagesByFlowInstanceId() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH + ("/cases/{2}?fromDateTime={3}&toDateTime={4}"), MUNICIPALITY_ID, EXTERNAL, "102251", LocalDateTime.now().minusDays(7), LocalDateTime.now()))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test08_getWebmessagesByFlowInstanceId_emptyResult() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH + ("/cases/{2}?fromDateTime={3}&toDateTime={4}"), MUNICIPALITY_ID, EXTERNAL, "102255", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test09_getAttachmentById() throws IOException {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH + ("/attachments/{2}"), MUNICIPALITY_ID, EXTERNAL, 123))
			.withExpectedResponseStatus(OK)
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(IMAGE_JPEG_VALUE))
			.withExpectedBinaryResponse("response/Test_image.jpg")
			.sendRequestAndVerifyResponse();
	}
}
