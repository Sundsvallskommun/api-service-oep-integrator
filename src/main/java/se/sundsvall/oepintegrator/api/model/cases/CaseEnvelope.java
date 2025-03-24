package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "Principal model")
public class CaseEnvelope {

	@Schema(description = "The caseId (flowInstanceId in OEP)", example = "109581")
	private String caseId;

	@Schema(description = "The case create date", example = "2023-11-15T11:55")
	private LocalDateTime created;

	@Schema(description = "The case update date", example = "2023-11-16T12:01")
	private LocalDateTime updated;

	public static CaseEnvelope create() {
		return new CaseEnvelope();
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public CaseEnvelope withCaseId(String caseId) {
		this.caseId = caseId;
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

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public CaseEnvelope withUpdated(LocalDateTime updated) {
		this.updated = updated;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(caseId, created, updated);
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
		return Objects.equals(caseId, other.caseId) && Objects.equals(created, other.created) && Objects.equals(updated, other.updated);
	}

	@Override
	public String toString() {
		return "CaseEnvelope [caseId=" + caseId + ", created=" + created + ", updated=" + updated + "]";
	}
}
