package se.sundsvall.oepintegrator.service;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.oepintegrator.api.model.webmessage.ExternalReference;
import se.sundsvall.oepintegrator.api.model.webmessage.Webmessage;
import se.sundsvall.oepintegrator.api.model.webmessage.WebmessageRequest;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.integration.party.PartyClient;
import se.sundsvall.oepintegrator.util.enums.InstanceType;

import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static se.sundsvall.oepintegrator.service.mapper.MessageMapper.toAddMessage;
import static se.sundsvall.oepintegrator.service.mapper.MessageMapper.toAddMessageAsOwner;
import static se.sundsvall.oepintegrator.util.Constants.REFERENCE_FLOW_INSTANCE_ID;
import static se.sundsvall.oepintegrator.util.StreamUtils.copyResponseEntityToHttpServletResponse;

@Service
public class WebmessageService {

	private final OpeneSoapIntegration openeSoapIntegration;
	private final OpeneRestIntegration openeRestIntegration;
	private final PartyClient partyClient;

	public WebmessageService(final OpeneSoapIntegration openeSoapIntegration, final OpeneRestIntegration openeRestIntegration, final PartyClient partyClient) {
		this.openeSoapIntegration = openeSoapIntegration;
		this.openeRestIntegration = openeRestIntegration;
		this.partyClient = partyClient;
	}

	public Integer createWebmessage(final String municipalityId, final InstanceType instanceType, final WebmessageRequest request, final List<MultipartFile> attachments) {

		if (isCaseAdministratorRequest(request)) {
			return openeSoapIntegration.addMessage(municipalityId, instanceType, toAddMessage(request, retrieveFlowInstanceId(request), attachments)).getMessageID();
		}

		final var legalId = fetchLegalId(municipalityId, request.getSender().getPartyId());

		return openeSoapIntegration.addMessageAsOwner(municipalityId, instanceType, toAddMessageAsOwner(request, legalId, retrieveFlowInstanceId(request), attachments)).getMessageID();
	}

	private Integer retrieveFlowInstanceId(final WebmessageRequest webMessageRequest) {
		return ofNullable(webMessageRequest.getExternalReferences())
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
		final var responseEntity = openeRestIntegration.getAttachmentById(municipalityId, instanceType, attachmentId);
		copyResponseEntityToHttpServletResponse(responseEntity, response, "Unable to get attachment by ID");

	}

	private boolean isCaseAdministratorRequest(final WebmessageRequest request) {
		return ofNullable(request.getSender())
			.map(sender -> ofNullable(sender.getAdministratorId()).isPresent())
			.orElse(true);
	}

	private String fetchLegalId(final String municipalityId, final String partyId) {
		return ofNullable(partyId)
			.map(id -> partyClient.getLegalId(municipalityId, PRIVATE, id)
				.orElseThrow(() -> Problem.valueOf(NOT_FOUND, "The provided partyId doesn't match any person!")))
			.orElse(null);
	}
}
