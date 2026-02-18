package se.sundsvall.oepintegrator.api.model.webmessage;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import se.sundsvall.oepintegrator.api.validation.ValidExternalReferences;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Webmessage request model")
public class WebmessageRequest {

	@Valid
	@Schema(description = "The sender")
	private Sender sender;

	@Schema(description = "The message", examples = "This is a message", requiredMode = REQUIRED)
	@NotBlank
	private String message;

	@ValidExternalReferences
	@ArraySchema(schema = @Schema(implementation = ExternalReference.class))
	private List<ExternalReference> externalReferences;

	public static WebmessageRequest create() {
		return new WebmessageRequest();
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(final Sender sender) {
		this.sender = sender;
	}

	public WebmessageRequest withSender(final Sender sender) {
		this.sender = sender;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public WebmessageRequest withMessage(final String message) {
		this.message = message;
		return this;
	}

	public List<ExternalReference> getExternalReferences() {
		return externalReferences;
	}

	public void setExternalReferences(final List<ExternalReference> externalReferences) {
		this.externalReferences = externalReferences;
	}

	public WebmessageRequest withExternalReferences(final List<ExternalReference> externalReferences) {
		this.externalReferences = externalReferences;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final WebmessageRequest that = (WebmessageRequest) o;
		return Objects.equals(sender, that.sender) && Objects.equals(message, that.message) && Objects.equals(externalReferences, that.externalReferences);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sender, message, externalReferences);
	}

	@Override
	public String toString() {
		return "WebmessageRequest{" +
			", sender=" + sender +
			", message='" + message + '\'' +
			", externalReferences=" + externalReferences +
			'}';
	}
}
