package se.sundsvall.oepintegrator.service;

import org.springframework.stereotype.Service;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusRequest;
import se.sundsvall.oepintegrator.api.model.cases.SetStatusResponse;
import se.sundsvall.oepintegrator.integration.opene.soap.OpeneSoapIntegration;
import se.sundsvall.oepintegrator.service.mapper.StatusMapper;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

@Service
public class CaseService {

	private final OpeneSoapIntegration openeSoapIntegration;

	public CaseService(final OpeneSoapIntegration openeSoapIntegration) {
		this.openeSoapIntegration = openeSoapIntegration;
	}

	public SetStatusResponse setStatusByFlowinstanceId(final String municipalityId, final InstanceType instanceType, final SetStatusRequest request, String flowInstanceId) {
		return new SetStatusResponse().withEventId(openeSoapIntegration.setStatus(municipalityId, instanceType, StatusMapper.toSetStatus(request, flowInstanceId)).getEventID());
	}

	public SetStatusResponse setStatusByExternalId(final String municipalityId, final InstanceType instanceType, final SetStatusRequest request, final String externalId, final String system) {
		return new SetStatusResponse().withEventId(openeSoapIntegration.setStatus(municipalityId, instanceType, StatusMapper.toSetStatus(request, externalId, system)).getEventID());
	}
}
