package se.sundsvall.oepintegrator.apptest;

import static java.text.MessageFormat.format;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;
import static se.sundsvall.oepintegrator.utility.enums.InstanceType.EXTERNAL;

@AutoConfigureWireMock(port = 9090, files = "classpath:/SetStatusIT")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ActiveProfiles("it")
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class SetStatusIT extends AbstractAppTest {

	private static final String REQUEST_FILE = "request.json";
	private static final String RESPONSE_FILE = "response.json";
	private static final String PATH_SET_STATUS_BY_FLOW_INSTANCE_ID = "/{0}/{1}/cases/{2}/status";
	private static final String PATH_SET_STATUS_BY_EXTERNAL_ID = "/{0}/{1}/cases/systems/{2}/{3}/status";
	private static final String MUNICIPALITY_ID = "2281";

	@Autowired
	private OpeneClientFactory openeClientFactory;

	@Test
	void test01_setStatusByFlowinstanceId() {
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");

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
	void test02_setStatusByExternalId() {
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");

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

}
