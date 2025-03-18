package se.sundsvall.oepintegrator.service;

import static java.util.Collections.emptyList;
import static org.zalando.problem.Status.BAD_REQUEST;
import static se.sundsvall.oepintegrator.utility.Constants.REFERENCE_FLOW_INSTANCE_ID;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.service.mapper.MessageMapper;

@Service
public class WebmessageService {

	private final OpeneSoapIntegration openeSoapIntegration;

	public WebmessageService(final OpeneSoapIntegration openeSoapIntegration) {
		this.openeSoapIntegration = openeSoapIntegration;
	}

	public Integer createWebmessage(final String municipalityId, final InstanceType instanceType, final WebmessageRequest request, final List<MultipartFile> attachments) {
		return openeSoapIntegration.addMessage(municipalityId, instanceType, MessageMapper.toAddMessage(request, retrieveFlowInstanceId(request), attachments)).getMessageID();
	}

	private Integer retrieveFlowInstanceId(final WebmessageRequest webMessageRequest) {
		return Optional.ofNullable(webMessageRequest.getExternalReferences())
			.orElseThrow(() -> Problem.valueOf(BAD_REQUEST, "Flow instance id is required"))
			.stream()
			.filter(reference -> REFERENCE_FLOW_INSTANCE_ID.equalsIgnoreCase(reference.getKey()))
			.map(ExternalReference::getValue)
			.map(Integer::parseInt)
			.findFirst()
			.orElseThrow(() -> Problem.valueOf(BAD_REQUEST, "Flow instance id is required"));

	}

	public List<Webmessage> getWebmessages(@ValidMunicipalityId final String municipalityId, final InstanceType instanceType, final String familyId, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
		return emptyList(); // TODO: UF-14938 - Implement integration
	}
}
