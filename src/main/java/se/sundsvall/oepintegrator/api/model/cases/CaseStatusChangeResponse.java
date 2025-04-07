package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "SetStatus response model")
public class CaseStatusChangeResponse {

	@Schema(description = "Event id", example = "123")
	private Integer eventId;

	public static CaseStatusChangeResponse create() {
		return new CaseStatusChangeResponse();
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(final Integer eventId) {
		this.eventId = eventId;
	}

	public CaseStatusChangeResponse withEventId(final Integer eventId) {
		this.eventId = eventId;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventId);
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
		CaseStatusChangeResponse other = (CaseStatusChangeResponse) obj;
		return Objects.equals(eventId, other.eventId);
	}

	@Override
	public String toString() {
		return "CaseStatusChangeResponse [eventId=" + eventId + "]";
	}
}
