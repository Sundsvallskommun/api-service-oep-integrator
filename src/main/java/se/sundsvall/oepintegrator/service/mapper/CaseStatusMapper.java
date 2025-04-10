package se.sundsvall.oepintegrator.service.mapper;

import static se.sundsvall.oepintegrator.util.XPathUtil.evaluateXPath;

import callback.ExternalID;
import callback.Principal;
import callback.SetStatus;
import jakarta.xml.bind.JAXBElement;
import java.util.Optional;
import javax.xml.namespace.QName;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatus;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatusChangeRequest;

public final class CaseStatusMapper {

	private static final String CALLBACK_NAMESPACE = "http://www.oeplatform.org/version/1.0/schemas/integration/callback";
	private static final String CALLBACK_LOCAL_PART = "name";

	private CaseStatusMapper() {}

	public static SetStatus toSetStatus(final CaseStatusChangeRequest request, final String flowInstanceId) {
		return new SetStatus()
			.withFlowInstanceID(Integer.valueOf(flowInstanceId))
			.withStatusAlias(request.getName())
			.withStatusID(request.getId())
			.withPrincipal(toPrincipal(request.getPrincipal()));

	}

	public static SetStatus toSetStatus(final CaseStatusChangeRequest request, final String externalId, final String system) {
		return new SetStatus()
			.withExternalID(toExternalID(externalId, system))
			.withStatusAlias(request.getName())
			.withStatusID(request.getId())
			.withPrincipal(toPrincipal(request.getPrincipal()));
	}

	public static CaseStatus toCaseStatus(final byte[] xml) {
		return CaseStatus.create()
			.withId(Optional.ofNullable(evaluateXPath(xml, "/Status/statusID").text()).map(Integer::valueOf).orElse(null))
			.withName(evaluateXPath(xml, "/Status/name").text());
	}

	private static Principal toPrincipal(final se.sundsvall.oepintegrator.api.model.cases.Principal principal) {
		return Optional.ofNullable(principal)
			.map(p -> new Principal().withUserID(p.getUserId()).withName(toJAXBElement(p.getName())))
			.orElse(null);
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
