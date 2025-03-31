package se.sundsvall.oepintegrator.service;

import static se.sundsvall.oepintegrator.service.mapper.CaseMapper.toConfirmDelivery;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusResponse;
import se.sundsvall.oepintegrator.integration.opene.rest.OpeneRestIntegration;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.service.mapper.StatusMapper;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

@Service
public class CaseService {

	private final OpeneSoapIntegration openeSoapIntegration;
	private final OpeneRestIntegration openeRestIntegration;

	public CaseService(final OpeneSoapIntegration openeSoapIntegration, final OpeneRestIntegration openeRestIntegration) {
		this.openeSoapIntegration = openeSoapIntegration;
		this.openeRestIntegration = openeRestIntegration;
	}

	public void confirmDelivery(final String municipalityId, final InstanceType instanceType, final String flowInstanceId, final ConfirmDeliveryRequest request) {
		openeSoapIntegration.confirmDelivery(municipalityId, instanceType, toConfirmDelivery(flowInstanceId, request));
	}

	public SetStatusResponse setStatusByFlowinstanceId(final String municipalityId, final InstanceType instanceType, final SetStatusRequest request, final String flowInstanceId) {
		return new SetStatusResponse().withEventId(openeSoapIntegration.setStatus(municipalityId, instanceType, StatusMapper.toSetStatus(request, flowInstanceId)).getEventID());
	}

	public SetStatusResponse setStatusByExternalId(final String municipalityId, final InstanceType instanceType, final SetStatusRequest request, final String externalId, final String system) {
		return new SetStatusResponse().withEventId(openeSoapIntegration.setStatus(municipalityId, instanceType, StatusMapper.toSetStatus(request, externalId, system)).getEventID());
	}

	public List<CaseEnvelope> getCaseEnvelopeListByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		return openeRestIntegration.getCaseListByFamilyId(municipalityId, instanceType, familyId, status, fromDate, toDate);
	}
}
