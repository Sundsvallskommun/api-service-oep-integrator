package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

@Schema(description = "ConfirmDelivery request model")
public class ConfirmDeliveryRequest {

	@NotBlank
	@Schema(description = "The case id in the system that delivered the case. From Open-E Platforms perspective this is the 'external case id", example = "caa230c6-abb4-4592-ad9a-34e263c2787d")
	private String caseId;

	@Schema(description = "If the case was delivered", example = "true")
	private boolean delivered;

	@Schema(description = "Any log message", example = "The case was delivered successfully")
	private String logMessage;

	@NotBlank
	@Schema(description = "The system that delivered the case", example = "ByggR")
	private String system;

	public static ConfirmDeliveryRequest create() {
		return new ConfirmDeliveryRequest();
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(final String caseId) {
		this.caseId = caseId;
	}

	public ConfirmDeliveryRequest withCaseId(final String caseId) {
		this.caseId = caseId;
		return this;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(final boolean delivered) {
		this.delivered = delivered;
	}

	public ConfirmDeliveryRequest withDelivered(final boolean delivered) {
		this.delivered = delivered;
		return this;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(final String logMessage) {
		this.logMessage = logMessage;
	}

	public ConfirmDeliveryRequest withLogMessage(final String logMessage) {
		this.logMessage = logMessage;
		return this;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(final String system) {
		this.system = system;
	}

	public ConfirmDeliveryRequest withSystem(final String system) {
		this.system = system;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final ConfirmDeliveryRequest that = (ConfirmDeliveryRequest) o;
		return delivered == that.delivered && Objects.equals(caseId, that.caseId) && Objects.equals(logMessage, that.logMessage) && Objects.equals(system, that.system);
	}

	@Override
	public int hashCode() {
		return Objects.hash(caseId, delivered, logMessage, system);
	}

	@Override
	public String toString() {
		return "ConfirmDeliveryRequest{" +
			"caseId='" + caseId + '\'' +
			", delivered=" + delivered +
			", logMessage='" + logMessage + '\'' +
			", system='" + system + '\'' +
			'}';
	}
}
