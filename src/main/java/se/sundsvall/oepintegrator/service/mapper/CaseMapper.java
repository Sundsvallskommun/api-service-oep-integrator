package se.sundsvall.oepintegrator.service.mapper;

import static java.util.Optional.ofNullable;
import static se.sundsvall.oepintegrator.service.util.XPathUtil.evaluateXPath;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;

import callback.ConfirmDelivery;
import callback.ExternalID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jsoup.select.Elements;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatus;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;

public final class CaseMapper {

	private CaseMapper() {
		// Prevent instantiation
	}

	public static List<CaseEnvelope> toCaseEnvelopeList(final byte[] xml) {
		final var flowInstances = evaluateXPath(xml, "/FlowInstances/FlowInstance");
		return Optional.ofNullable(flowInstances).orElse(new Elements()).stream()
			.map(element -> CaseEnvelope.create()
				.withFlowInstanceId(evaluateXPath(element, "/flowInstanceID").text())
				.withCreated(parseLocalDateTime(evaluateXPath(element, "/added").text()))
				.withStatusUpdated(parseLocalDateTime(evaluateXPath(element, "/lastStatusChange").text())))
			.toList();
	}

	public static CaseStatus toCaseStatus(final byte[] xml) {
		return CaseStatus.create()
			.withName(evaluateXPath(xml, "/Status/name").text());
	}

	private static LocalDateTime parseLocalDateTime(final String value) {
		return ofNullable(value).map(dateStr -> LocalDateTime.parse(value, OPEN_E_DATE_TIME_FORMAT)).orElse(null);
	}

	public static ConfirmDelivery toConfirmDelivery(final String flowInstanceId, final ConfirmDeliveryRequest request) {
		return new ConfirmDelivery()
			.withLogMessage(request.getLogMessage())
			.withDelivered(request.isDelivered())
			.withExternalID(new ExternalID()
				.withSystem(request.getSystem())
				.withID(request.getCaseId()))
			.withFlowInstanceID(Integer.parseInt(flowInstanceId));
	}
}
