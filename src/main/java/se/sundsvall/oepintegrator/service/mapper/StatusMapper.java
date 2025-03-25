package se.sundsvall.oepintegrator.service.mapper;

import callback.ExternalID;
import callback.Principal;
import callback.SetStatus;
import jakarta.xml.bind.JAXBElement;
import java.util.Optional;
import javax.xml.namespace.QName;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;

public final class StatusMapper {

	private static final String CALLBACK_NAMESPACE = "http://www.oeplatform.org/version/1.0/schemas/integration/callback";
	private static final String CALLBACK_LOCAL_PART = "name";

	private StatusMapper() {}

	public static SetStatus toSetStatus(final SetStatusRequest request, final String flowInstanceId) {

		return new SetStatus()
			.withFlowInstanceID(Integer.valueOf(flowInstanceId))
			.withStatusAlias(request.getStatus())
			.withStatusID(toStatusId(request.getStatusId()))
			.withPrincipal(toPrincipal(request.getPrincipal()));

	}

	public static SetStatus toSetStatus(final SetStatusRequest request, final String externalId, final String system) {

		return new SetStatus()
			.withExternalID(toExternalID(externalId, system))
			.withStatusAlias(request.getStatus())
			.withStatusID(toStatusId(request.getStatusId()))
			.withPrincipal(toPrincipal(request.getPrincipal()));
	}

	private static Principal toPrincipal(final se.sundsvall.oepintegrator.api.model.cases.Principal principal) {
		return Optional.ofNullable(principal)
			.map(p -> new Principal().withUserID(p.getUserId()).withName(toJAXBElement(p.getName())))
			.orElse(null);
	}

	private static Integer toStatusId(final int statusId) {
		return statusId == 0 ? null : statusId;
	}

	private static JAXBElement<String> toJAXBElement(final String value) {
		return value != null ? new JAXBElement<>(new QName(CALLBACK_NAMESPACE, CALLBACK_LOCAL_PART), String.class, value) : null;
	}

	private static ExternalID toExternalID(final String externalId, final String system) {
		return new ExternalID()
			.withID(externalId)
			.withSystem(system);
	}
}
