package se.sundsvall.oepintegrator.service;

import static org.zalando.problem.Status.BAD_REQUEST;
import static se.sundsvall.oepintegrator.utility.Constants.REFERENCE_FLOW_INSTANCE_ID;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.WebMessageRequest;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.service.mapper.MessageMapper;

@Service
public class WebMessageService {

	private final OpeneSoapIntegration openeSoapIntegration;

	public WebMessageService(final OpeneSoapIntegration openeSoapIntegration) {
		this.openeSoapIntegration = openeSoapIntegration;
	}

	public Integer createWebMessage(final String municipalityId, final InstanceType instanceType, final WebMessageRequest request, final List<MultipartFile> files) {

		return openeSoapIntegration.addMessage(municipalityId, instanceType, MessageMapper.toAddMessage(request, retrieveFlowInstanceId(request), files)).getMessageID();
	}

	private Integer retrieveFlowInstanceId(final WebMessageRequest webMessageRequest) {
		return Optional.ofNullable(webMessageRequest.getExternalReferences())
			.orElseThrow(() -> Problem.valueOf(BAD_REQUEST, "Flow instance id is required"))
			.stream()
			.filter(reference -> REFERENCE_FLOW_INSTANCE_ID.equalsIgnoreCase(reference.getKey()))
			.map(ExternalReference::getValue)
			.map(Integer::parseInt)
			.findFirst()
			.orElseThrow(() -> Problem.valueOf(BAD_REQUEST, "Flow instance id is required"));

	}
}
