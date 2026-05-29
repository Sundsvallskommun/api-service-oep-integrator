package se.sundsvall.oepintegrator.integration.opene.rest;

import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import se.sundsvall.oepintegrator.integration.opene.OpeneClient;
import se.sundsvall.oepintegrator.integration.opene.rest.model.MetadataRoot;

/**
 * Interface for OpenE REST clients. This contains the methods for calling the Open-E REST API.
 */
public interface OpeneRestClient extends OpeneClient {

	String TEXT_XML_CHARSET_ISO_8859_1 = "text/xml; charset=ISO-8859-1";

	@GetMapping(path = "/api/messageapi/getmessages/family/{familyId}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getWebmessagesByFamilyId(
		@PathVariable final String familyId,
		@RequestParam(name = "fromDate") final String fromDate,
		@RequestParam(name = "toDate") final String toDate);

	@GetMapping(path = "/api/messageapi/getmessages/flowinstance/{flowInstanceId}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	byte[] getWebmessagesByFlowInstanceId(
		@PathVariable final String flowInstanceId,
		@RequestParam(name = "fromDate") final String fromDate,
		@RequestParam(name = "toDate") final String toDate);

	@GetMapping(path = "/api/messageapi/getattachment/{attachmentId}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	ResponseEntity<Resource> getAttachmentById(@PathVariable final int attachmentId);

	@GetMapping(path = "/api/instanceapi/getinstances/family/{familyId}/{status}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	Optional<byte[]> getCaseListByFamilyId(
		@PathVariable final String familyId,
		@PathVariable(required = false) final String status,
		@RequestParam(name = "fromDate", required = false) final String fromDate,
		@RequestParam(name = "toDate", required = false) final String toDate);

	@GetMapping(path = "/api/instanceapi/getinstances/owner/citizenidentifier/{legalId}/{status}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	Optional<byte[]> getCaseListByCitizenIdentifier(
		@PathVariable final String legalId,
		@PathVariable(required = false) final String status,
		@RequestParam(name = "fromDate", required = false) final String fromDate,
		@RequestParam(name = "toDate", required = false) final String toDate,
		@RequestParam(name = "showStatus", required = false) final Boolean showStatus);

	@GetMapping(path = "/api/multisigninstanceapi/getwaitinginstances/citizenidentifier/{legalId}/{status}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	Optional<byte[]> getWaitingCaseListByCitizenIdentifier(
		@PathVariable final String legalId,
		@PathVariable(required = false) final String status,
		@RequestParam(name = "fromDate", required = false) final String fromDate,
		@RequestParam(name = "toDate", required = false) final String toDate,
		@RequestParam(name = "showStatus", required = false) final Boolean showStatus);

	@GetMapping(path = "/api/multisigninstanceapi/getwaitinginstances/userid/{userId}/{status}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	Optional<byte[]> getWaitingCaseListByUserId(
		@PathVariable final String userId,
		@PathVariable(required = false) final String status,
		@RequestParam(name = "fromDate", required = false) final String fromDate,
		@RequestParam(name = "toDate", required = false) final String toDate,
		@RequestParam(name = "showStatus", required = false) final Boolean showStatus);

	@GetMapping(path = "/api/instanceapi/getstatus/{flowInstanceId}", produces = TEXT_XML_CHARSET_ISO_8859_1)
	Optional<byte[]> getCaseStatusByFlowInstanceId(@PathVariable final String flowInstanceId);

	@GetMapping(path = "/api/instanceapi/getinstance/{flowInstanceId}/pdf")
	ResponseEntity<Resource> getCasePdfByFlowInstanceId(@PathVariable final String flowInstanceId);

	@GetMapping(path = "/api/fileuploadqueryapi/getfile/{flowInstanceId}/{queryId}/{fileId}")
	ResponseEntity<Resource> getCaseAttachment(
		@PathVariable final String flowInstanceId,
		@PathVariable final String queryId,
		@PathVariable final String fileId);

	@GetMapping(path = "/api/instanceapi/getinstance/{flowInstanceId}/xml")
	Optional<byte[]> getCaseXmlByFlowInstanceId(@PathVariable final String flowInstanceId);

	@GetMapping(path = "/api/v1/getflows/json", produces = TEXT_XML_CHARSET_ISO_8859_1)
	MetadataRoot getMetadata();

	@GetMapping(path = "/api/restrictedflowinfo/getflows/json", produces = TEXT_XML_CHARSET_ISO_8859_1)
	MetadataRoot getRestrictedMetadata();
}
