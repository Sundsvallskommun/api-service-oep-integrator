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

class CaseEnvelopeTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> LocalDateTime.now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(CaseEnvelope.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void builder() {

		// Arrange
		final var flowInstanceId = "flowInstanceId";
		final var created = now();
		final var statusUpdated = now();

		// Act
		final var bean = CaseEnvelope.create()
			.withFlowInstanceId(flowInstanceId)
			.withCreated(created)
			.withStatusUpdated(statusUpdated);

		// Assert
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getFlowInstanceId()).isEqualTo(flowInstanceId);
		assertThat(bean.getCreated()).isEqualTo(created);
		assertThat(bean.getStatusUpdated()).isEqualTo(statusUpdated);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CaseEnvelope.create()).hasAllNullFieldsOrProperties();
		assertThat(new CaseEnvelope()).hasAllNullFieldsOrProperties();
	}
}
