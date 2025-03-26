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

	public static CaseEnvelope create() {
		return new CaseEnvelope();
	}

	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public CaseEnvelope withFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
		return this;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public CaseEnvelope withCreated(LocalDateTime created) {
		this.created = created;
		return this;
	}

	public LocalDateTime getStatusUpdated() {
		return statusUpdated;
	}

	public void setStatusUpdated(LocalDateTime statusUpdated) {
		this.statusUpdated = statusUpdated;
	}

	public CaseEnvelope withStatusUpdated(LocalDateTime statusUpdated) {
		this.statusUpdated = statusUpdated;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(flowInstanceId, created, statusUpdated);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CaseEnvelope other = (CaseEnvelope) obj;
		return Objects.equals(flowInstanceId, other.flowInstanceId) && Objects.equals(created, other.created) && Objects.equals(statusUpdated, other.statusUpdated);
	}

	@Override
	public String toString() {
		return "CaseEnvelope [flowInstanceId=" + flowInstanceId + ", created=" + created + ", statusUpdated=" + statusUpdated + "]";
	}
}
