package se.sundsvall.oepintegrator.service.mapper;

import static java.util.Optional.ofNullable;
import static se.sundsvall.oepintegrator.util.Constants.OPEN_E_DATE_TIME_FORMAT;
import static se.sundsvall.oepintegrator.util.XPathUtil.evaluateXPath;
import static se.sundsvall.oepintegrator.util.XPathUtil.parseXmlDocument;

import callback.ConfirmDelivery;
import callback.ExternalID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jsoup.select.Elements;
import se.sundsvall.oepintegrator.api.model.cases.Case;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.CaseStatus;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;

public final class CaseMapper {

	private CaseMapper() {}

	public static List<CaseEnvelope> toCaseEnvelopeList(final byte[] xml) {
		final var flowInstances = evaluateXPath(xml, "/FlowInstances/FlowInstance");
		return Optional.ofNullable(flowInstances).orElse(new Elements()).stream()
			.map(element -> CaseEnvelope.create()
				.withFlowInstanceId(evaluateXPath(element, "/flowInstanceID").text())
				.withCreated(parseLocalDateTime(evaluateXPath(element, "/added").text()))
				.withStatusUpdated(parseLocalDateTime(evaluateXPath(element, "/lastStatusChange").text())))
			.toList();
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

	private static LocalDateTime parseLocalDateTime(final String value) {
		return ofNullable(value).map(dateStr -> LocalDateTime.parse(value, OPEN_E_DATE_TIME_FORMAT)).orElse(null);
	}

	public static Case toCase(final byte[] xml) {

		return Case.create()
			.withFlowInstanceId(evaluateXPath(xml, "/FlowInstance/Header/Flow/FamilyID").text())
			.withFamilyId(evaluateXPath(xml, "/FlowInstance/Header/Flow/FamilyID").text())
			.withFlowInstanceId(evaluateXPath(xml, "/FlowInstance/Header/FlowInstanceID").text())
			.withVersion(Integer.valueOf(evaluateXPath(xml, "/FlowInstance/Header/Flow/Version").text()))
			.withFlowId(evaluateXPath(xml, "/FlowInstance/Header/Flow/FlowID").text())
			.withTitle(evaluateXPath(xml, "/FlowInstance/Header/Flow/Name").text())
			.withStatus(CaseStatus.create()
				.withId(Integer.valueOf(evaluateXPath(xml, "/FlowInstance/Header/Status/ID").text()))
				.withName(evaluateXPath(xml, "/FlowInstance/Header/Status/Name").text()))
			.withCreated(LocalDateTime.parse(evaluateXPath(xml, "/FlowInstance/Header/Posted").text()))
			.withPayload(parseXmlDocument(xml).toString());
	}
}
