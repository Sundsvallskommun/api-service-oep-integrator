package se.sundsvall.oepintegrator.apptest;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static se.sundsvall.oepintegrator.util.enums.InstanceType.EXTERNAL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.oepintegrator.Application;
import se.sundsvall.oepintegrator.integration.db.InstanceRepository;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;

@WireMockAppTestSuite(files = "classpath:/ConfirmDeliveryIT/", classes = Application.class)
@ActiveProfiles("it")
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class ConfirmDeliveryIT extends AbstractAppTest {

	private static final String REQUEST_FILE = "request.json";
	private static final String PATH_CONFIRM_DELIVERY = "/{0}/{1}/cases/{2}/delivery";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String FLOW_INSTANCE_ID = "123";

	@Autowired
	private InstanceRepository instanceRepository;

	@Autowired
	private OpeneClientFactory openeClientFactory;

	@BeforeEach
	void beforeEach() {
		instanceRepository.findAll().forEach(instance -> {
			instance.setBaseUrl(instance.getBaseUrl().replace("localhost:9090", "localhost:" + wiremock.port()));
			instanceRepository.save(instance);
		});
		ReflectionTestUtils.invokeMethod(openeClientFactory, "init");
	}

	@Test
	void test01_confirmDelivery() {
		setupCall()
			.withHttpMethod(POST)
			.withServicePath(format(PATH_CONFIRM_DELIVERY, MUNICIPALITY_ID, EXTERNAL, FLOW_INSTANCE_ID))
			.withRequest(REQUEST_FILE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequestAndVerifyResponse();
	}
}
