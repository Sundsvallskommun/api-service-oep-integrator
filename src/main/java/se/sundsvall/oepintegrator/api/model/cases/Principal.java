package se.sundsvall.oepintegrator.api.model.cases;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

@Schema(description = "Principal model")
public class Principal {

	@Schema(description = "The name", example = "John Doe")
	private String name;

	@NotBlank
	@Schema(description = "The user id", example = "joh12doe", requiredMode = REQUIRED)
	private String userId;

	public static Principal create() {
		return new Principal();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Principal withName(String name) {
		this.name = name;
		return this;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public Principal withUserId(String userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, userId);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Principal other = (Principal) obj;
		return Objects.equals(name, other.name) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "Principal [name=" + name + ", userId=" + userId + "]";
	}
}
