package se.sundsvall.oepintegrator.apptest;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
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

	@Test
	void test01_createWebmessage() throws FileNotFoundException {
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");

		setupCall()
			.withHttpMethod(POST)
			.withServicePath(format(PATH, MUNICIPALITY_ID, InstanceType.EXTERNAL))
			.withContentType(MULTIPART_FORM_DATA)
			.withRequestFile("request", REQUEST_FILE)
			.withRequestFile("attachments", "test.txt")
			.withRequestFile("attachments", "test2.txt")
			.withExpectedResponseStatus(CREATED)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getWebmessages() {
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");

		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH + ("/{2}?fromDateTime={3}&toDateTime={4}"), MUNICIPALITY_ID, InstanceType.EXTERNAL, "familyId", LocalDateTime.now().minusDays(7), LocalDateTime.now()))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getWebmessage_emptyResult() {
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");

		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH + ("/{2}?fromDateTime={3}&toDateTime={4}"), MUNICIPALITY_ID, InstanceType.EXTERNAL, "familyId", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
