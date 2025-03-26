package se.sundsvall.oepintegrator.integration.opene.rest;

import static se.sundsvall.oepintegrator.service.mapper.CaseMapper.toCaseEnvelopeList;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.integration.opene.OpeneClientFactory;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;

/**
 * Integration class for Open-E REST API. This will be used to call the Open-E REST API like this:
 * 
 * <pre>
 * {@code
 * public void doStuff(final String municipalityId, final String instanceId) {
 *     LOG.info("Example of calling OpenE REST client");
 *     final var client = clientFactory.getRestClient(municipalityId, instanceId);
 *     client.doCall();
 * }}
 */
@Component
public class OpeneRestIntegration {

	private final OpeneClientFactory clientFactory;

	public OpeneRestIntegration(final OpeneClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	public List<CaseEnvelope> getCaseListByFamilyId(final String municipalityId, final InstanceType instanceType, final String familyId, final String status, final LocalDate fromDate, final LocalDate toDate) {
		final var client = clientFactory.getRestClient(municipalityId, instanceType);
		return toCaseEnvelopeList(client.getCaseListByFamilyId(familyId, status, fromDate, toDate).orElse(new byte[0]));
	}
}
