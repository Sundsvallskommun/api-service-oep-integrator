package se.sundsvall.oepintegrator.service.mapper;

import static se.sundsvall.oepintegrator.service.util.XPathUtil.evaluateXPath;
import static se.sundsvall.oepintegrator.utility.Constants.OPEN_E_DATE_TIME_FORMAT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jsoup.select.Elements;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;

public class CaseMapper {

	private CaseMapper() {}

	public static List<CaseEnvelope> toCaseEnvelopeList(byte[] xml) {
		final var flowInstances = evaluateXPath(xml, "/FlowInstances/FlowInstance");
		return Optional.ofNullable(flowInstances).orElse(new Elements()).stream()
			.map(element -> CaseEnvelope.create()
				.withFlowInstanceId(evaluateXPath(element, "/flowInstanceID").text())
				.withCreated(parseLocalDateTime(evaluateXPath(element, "/added").text()))
				.withStatusUpdated(parseLocalDateTime(evaluateXPath(element, "/lastStatusChange").text())))
			.toList();
	}

	private static LocalDateTime parseLocalDateTime(String value) {
		return LocalDateTime.parse(value, OPEN_E_DATE_TIME_FORMAT);
	}
}
