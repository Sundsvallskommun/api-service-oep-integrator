package se.sundsvall.oepintegrator.api.model.cases;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.time.LocalDateTime;
import java.util.Random;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CaseTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(Case.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {
		// Arrange
		final var caseId = "caseId";
		final var familyId = "familyId";
		final var version = 1;
		final var flowId = "flowId";
		final var title = "title";
		final var status = CaseStatus.create();
		final var created = now();
		final var payload = "payload";

		// Act
		final var result = Case.create()
			.withFlowInstanceId(caseId)
			.withFamilyId(familyId)
			.withVersion(version)
			.withFlowId(flowId)
			.withTitle(title)
			.withStatus(status)
			.withCreated(created)
			.withPayload(payload);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getFlowInstanceId()).isEqualTo(caseId);
		assertThat(result.getFamilyId()).isEqualTo(familyId);
		assertThat(result.getVersion()).isEqualTo(version);
		assertThat(result.getFlowId()).isEqualTo(flowId);
		assertThat(result.getTitle()).isEqualTo(title);
		assertThat(result.getStatus()).isEqualTo(status);
		assertThat(result.getCreated()).isEqualTo(created);
		assertThat(result.getPayload()).isEqualTo(payload);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Case.create()).hasAllNullFieldsOrProperties();
		assertThat(new Case()).hasAllNullFieldsOrProperties();
	}

}
