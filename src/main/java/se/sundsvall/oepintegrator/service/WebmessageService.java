package se.sundsvall.oepintegrator.service;

import static org.zalando.problem.Status.BAD_REQUEST;
import static se.sundsvall.oepintegrator.utility.Constants.REFERENCE_FLOW_INSTANCE_ID;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.service.mapper.MessageMapper;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

@Service
public class WebmessageService {

	private final OpeneSoapIntegration openeSoapIntegration;

	private final OpeneRestIntegration openeRestIntegration;

	public WebmessageService(final OpeneSoapIntegration openeSoapIntegration, final OpeneRestIntegration openeRestIntegration) {
		this.openeSoapIntegration = openeSoapIntegration;
		this.openeRestIntegration = openeRestIntegration;
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

	public List<Webmessage> getWebmessagesByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
		return openeRestIntegration.getWebmessagesByFamilyId(municipalityId, instanceType, familyId, fromDateTime, toDateTime);
	}

	public List<Webmessage> getWebmessagesByFlowInstanceId(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
		return openeRestIntegration.getWebmessagesByFlowInstanceId(municipalityId, instanceType, flowInstanceId, fromDateTime, toDateTime);
	}

	public void getAttachmentById(final String municipalityId, final InstanceType instanceType, final Integer attachmentId, final HttpServletResponse response) {
		openeRestIntegration.getAttachmentById(municipalityId, instanceType, attachmentId, response);
	}
}
