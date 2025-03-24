package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.Objects;

@Schema(description = "SetStatus request model")
public class SetStatusRequest {

	@Schema(description = "The status")
	private String status;

	@Schema(description = "Id of status", example = "123")
	private int statusId;

	@Valid
	@Schema(description = "The principal")
	private Principal principal;

	public static SetStatusRequest create() {
		return new SetStatusRequest();
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public SetStatusRequest withStatus(String status) {
		this.status = status;
		return this;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getStatusId() {
		return statusId;
	}

	public SetStatusRequest withStatusId(int statusId) {
		this.statusId = statusId;
		return this;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public SetStatusRequest withPrincipal(Principal principal) {
		this.principal = principal;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final SetStatusRequest that = (SetStatusRequest) o;
		return Objects.equals(status, that.status) && Objects.equals(statusId, that.statusId) && Objects.equals(principal, that.principal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, statusId, principal);
	}

	@Override
	public String toString() {
		return "SetStatusRequest{" +
			", status=" + status +
			", statusId=" + statusId +
			", principal=" + principal +
			'}';
	}
}
