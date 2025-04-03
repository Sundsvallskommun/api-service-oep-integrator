package se.sundsvall.oepintegrator.configuration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.oepintegrator.Application;

@SpringBootTest(classes = Application.class, webEnvironment = MOCK)
@ActiveProfiles("junit")
class CredentialsPropertiesTest {

	@Autowired
	private CredentialsProperties properties;

	@Test
	void testProperties() {
		assertThat(properties.secretKey()).isEqualTo("WbVG8XC%m&9Z!7a$xyKGWzB^#kUSoUUs");
	}
}
