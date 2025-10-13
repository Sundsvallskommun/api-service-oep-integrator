package se.sundsvall.oepintegrator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;
import se.sundsvall.oepintegrator.api.model.cases.Principal;

@ExtendWith(ResourceLoaderExtension.class)
class CaseStatusMapperTest {

	@Test
	void toSetStatusWithFlowinstanceId() {
		// Arrange
		final var flowInstanceId = "123";
		final var statusId = 456;
		final var statusName = "statusName";
		final var userId = "userId";
		final var name = "name";
		final var request = new CaseStatusChangeRequest().withId(statusId).withName(statusName)
			.withPrincipal(new Principal().withName(name).withUserId(userId));

		// Act
		final var result = CaseStatusMapper.toSetStatus(request, flowInstanceId);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(Integer.parseInt(flowInstanceId));
		assertThat(result.getStatusID()).isEqualTo(statusId);
		assertThat(result.getStatusAlias()).isEqualTo(statusName);
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
		final var statusName = "statusName";
		final var userId = "userId";
		final var name = "name";
		final var request = new CaseStatusChangeRequest().withId(statusId).withName(statusName)
			.withPrincipal(new Principal().withName(name).withUserId(userId));

		// Act
		final var result = CaseStatusMapper.toSetStatus(request, externalId, system);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getExternalID().getID()).isEqualTo(externalId);
		assertThat(result.getExternalID().getSystem()).isEqualTo(system);
		assertThat(result.getStatusID()).isEqualTo(statusId);
		assertThat(result.getStatusAlias()).isEqualTo(statusName);
		assertThat(result.getPrincipal()).isNotNull();
		assertThat(result.getPrincipal().getUserID()).isEqualTo(userId);
		assertThat(result.getPrincipal().getName().getValue()).isEqualTo(name);
		assertThat(result.getFlowInstanceID()).isNull();
	}

	@Test
	void toCaseStatus(@Load("/mappings/case-status.xml") final String xml) {

		// Act
		final var result = CaseStatusMapper.toCaseStatus(xml.getBytes());

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getId()).isEqualTo(10361);
		assertThat(result.getName()).isEqualTo("Inskickat");
		assertThat(result.getNewExternalMessagesDisallowed()).isTrue();
		assertThat(result.getAddExternalMessage()).isTrue();
		assertThat(result.getAddInternalMessage()).isTrue();
		assertThat(result.getIsRestrictedAdminDeletable()).isTrue();
		assertThat(result.getStatus()).isEqualTo("SUBMITTED");
	}
}
