package se.sundsvall.oepintegrator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import se.sundsvall.oepintegrator.api.model.cases.Principal;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;

class StatusMapperTest {

	@Test
	void toSetStatusWithFlowinstanceId() {
		// Arrange
		final var flowInstanceId = "123";
		final var statusId = 456;
		final var status = "status";
		final var userId = "userId";
		final var name = "name";
		final var request = new SetStatusRequest().withStatusId(statusId).withStatus(status)
			.withPrincipal(new Principal().withName(name).withUserId(userId));

		// Act
		final var result = StatusMapper.toSetStatus(request, flowInstanceId);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(Integer.parseInt(flowInstanceId));
		assertThat(result.getStatusID()).isEqualTo(statusId);
		assertThat(result.getStatusAlias()).isEqualTo(status);
		assertThat(result.getPrincipal()).isNotNull();
		assertThat(result.getPrincipal().getUserID()).isEqualTo(userId);
		assertThat(result.getPrincipal().getName().getValue()).isEqualTo(name);
		assertThat(result.getExternalID()).isNull();
	}

	@Test
	void toSetStatusWithExternalId() {
		// Arrange
		final var externalId = "externalId";
		final var system = "system";
		final var statusId = 456;
		final var status = "status";
		final var userId = "userId";
		final var name = "name";
		final var request = new SetStatusRequest().withStatusId(statusId).withStatus(status)
			.withPrincipal(new Principal().withName(name).withUserId(userId));

		// Act
		final var result = StatusMapper.toSetStatus(request, externalId, system);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getExternalID().getID()).isEqualTo(externalId);
		assertThat(result.getExternalID().getSystem()).isEqualTo(system);
		assertThat(result.getStatusID()).isEqualTo(statusId);
		assertThat(result.getStatusAlias()).isEqualTo(status);
		assertThat(result.getPrincipal()).isNotNull();
		assertThat(result.getPrincipal().getUserID()).isEqualTo(userId);
		assertThat(result.getPrincipal().getName().getValue()).isEqualTo(name);
		assertThat(result.getFlowInstanceID()).isNull();
	}
}
