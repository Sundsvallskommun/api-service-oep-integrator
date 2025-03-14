package se.sundsvall.oepintegrator.api.model.webmessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Schema(description = "Sender model")
public class Sender {

	@Schema(description = "The user ID of the sender. I.e. employee ID", example = "joe01doe")
	@NotNull
	private String userId;

	public static Sender create() {
		return new Sender();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public Sender withUserId(final String userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Sender other = (Sender) obj;
		return Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "Sender [userId=" + userId + "]";
	}
}
