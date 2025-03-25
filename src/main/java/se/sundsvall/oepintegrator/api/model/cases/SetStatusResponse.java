package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "SetStatus response model")
public class SetStatusResponse {

	@Schema(description = "Event id", example = "123")
	private Integer eventId;

	public static SetStatusResponse create() {
		return new SetStatusResponse();
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(final Integer eventId) {
		this.eventId = eventId;
	}

	public SetStatusResponse withEventId(final Integer eventId) {
		this.eventId = eventId;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final SetStatusResponse that = (SetStatusResponse) o;
		return Objects.equals(eventId, that.eventId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventId);
	}

	@Override
	public String toString() {
		return "SetStatusResponse{" +
			", eventId=" + eventId +
			'}';
	}
}
