package se.sundsvall.oepintegrator.api.model.webmessage;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "ExternalReference model")
public class ExternalReference {

	@Schema(description = "The external reference key", example = "flowInstanceId")
	private String key;

	@Schema(description = "The external reference value", example = "356746349")
	private String value;

	public static ExternalReference create() {
		return new ExternalReference();
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public ExternalReference withKey(final String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public ExternalReference withValue(final String value) {
		this.value = value;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ExternalReference other = (ExternalReference) obj;
		return Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "ExternalReference [key=" + key + ", value=" + value + "]";
	}
}
