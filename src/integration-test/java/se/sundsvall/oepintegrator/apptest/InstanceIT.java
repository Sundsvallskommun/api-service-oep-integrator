package se.sundsvall.oepintegrator.apptest;

import static java.text.MessageFormat.format;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

@AutoConfigureWireMock(port = 9090, files = "classpath:/InstanceIT")
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = Application.class)
@ActiveProfiles("it")
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class InstanceIT extends AbstractAppTest {

	private static final String REQUEST_FILE = "request.json";
	private static final String RESPONSE_FILE = "response.json";
	private static final String PATH = "/{0}/instances";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String INSTANCE_ID = "b97977a2-fe12-4397-bc83-be5ce61e950e";

	@Autowired
	private OpeneClientFactory openeClientFactory;

	@BeforeEach
	void beforeEach() {
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");
	}

	@Test
	void test01_getInstances() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH, MUNICIPALITY_ID))
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getInstanceById() {
		setupCall()
			.withHttpMethod(GET)
			.withServicePath(format(PATH + "/{1}", MUNICIPALITY_ID, INSTANCE_ID))
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_createInstance() {
		final var location = setupCall()
			.withServicePath(format(PATH, MUNICIPALITY_ID))
			.withHttpMethod(POST)
			.withRequest(REQUEST_FILE)
			.withExpectedResponseStatus(CREATED)
			.sendRequest()
			.getResponseHeaders().get(LOCATION).getFirst();

		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withHeader(ACCEPT, APPLICATION_JSON_VALUE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_updateInstance() {
		setupCall()
			.withServicePath(format(PATH + "/{1}", MUNICIPALITY_ID, INSTANCE_ID))
			.withHttpMethod(PATCH)
			.withRequest(REQUEST_FILE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequest();

		setupCall()
			.withServicePath(format(PATH + "/{1}", MUNICIPALITY_ID, INSTANCE_ID))
			.withHttpMethod(GET)
			.withHeader(ACCEPT, APPLICATION_JSON_VALUE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponseHeader(CONTENT_TYPE, List.of(APPLICATION_JSON_VALUE))
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_deleteInstance() {
		setupCall()
			.withServicePath(format(PATH + "/{1}", MUNICIPALITY_ID, INSTANCE_ID))
			.withHttpMethod(DELETE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequest();

		setupCall()
			.withServicePath(format(PATH + "/{1}", MUNICIPALITY_ID, INSTANCE_ID))
			.withHttpMethod(GET)
			.withHeader(ACCEPT, APPLICATION_JSON_VALUE)
			.withExpectedResponseStatus(NOT_FOUND)
			.sendRequestAndVerifyResponse();
	}
}
