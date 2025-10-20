package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "Case envelope model")
public class CaseEnvelope {

	@Schema(description = "The flowInstanceId (caseId)", example = "109581")
	private String flowInstanceId;

	@Schema(description = "The case create date", example = "2023-11-15T11:55")
	private LocalDateTime created;

	@Schema(description = "The case status change date", example = "2023-11-16T12:01")
	private LocalDateTime statusUpdated;

	@Schema(description = "The familyId", example = "832", hidden = true)
	private String familyId;

	@Schema(description = "The display name for the family the case belongs to", example = "Kompiskortet")
	private String displayName;

	@Schema(description = "The case status")
	private CaseStatus status;

	public static CaseEnvelope create() {
		return new CaseEnvelope();
	}

	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(final String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public CaseEnvelope withFlowInstanceId(final String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
		return this;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(final LocalDateTime created) {
		this.created = created;
	}

	public CaseEnvelope withCreated(final LocalDateTime created) {
		this.created = created;
		return this;
	}

	public LocalDateTime getStatusUpdated() {
		return statusUpdated;
	}

	public void setStatusUpdated(final LocalDateTime statusUpdated) {
		this.statusUpdated = statusUpdated;
	}

	public CaseEnvelope withStatusUpdated(final LocalDateTime statusUpdated) {
		this.statusUpdated = statusUpdated;
		return this;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(final String familyId) {
		this.familyId = familyId;
	}

	public CaseEnvelope withFamilyId(final String familyId) {
		this.familyId = familyId;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public CaseEnvelope withDisplayName(final String displayName) {
		this.displayName = displayName;
		return this;
	}

	public CaseStatus getStatus() {
		return status;
	}

	public void setStatus(final CaseStatus status) {
		this.status = status;
	}

	public CaseEnvelope withStatus(final CaseStatus status) {
		this.status = status;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final CaseEnvelope that = (CaseEnvelope) o;
		return Objects.equals(flowInstanceId, that.flowInstanceId) && Objects.equals(created, that.created) && Objects.equals(statusUpdated, that.statusUpdated) && Objects.equals(familyId, that.familyId)
			&& Objects.equals(displayName, that.displayName) && Objects.equals(status, that.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(flowInstanceId, created, statusUpdated, familyId, displayName, status);
	}

	@Override
	public String toString() {
		return "CaseEnvelope{" +
			"flowInstanceId='" + flowInstanceId + '\'' +
			", created=" + created +
			", statusUpdated=" + statusUpdated +
			", familyId='" + familyId + '\'' +
			", displayName='" + displayName + '\'' +
			", status=" + status +
			'}';
	}
}
