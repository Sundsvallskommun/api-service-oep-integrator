package se.sundsvall.oepintegrator.api.model.webmessage;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.oepintegrator.api.validation.ValidSender;

@ValidSender
@Schema(description = "Sender model. The attributes in this object are mutually exclusive, meaning that only one of them can be set at a time.")
public class Sender {

	@Schema(description = "The user ID of the sender. I.e. employee ID", example = "joe01doe")
	private String userId;

	@Schema(description = "The user ID of the case administrator. I.e. the employee ID of the administrative official.", example = "joe01doe")
	private String administratorId;

	@ValidUuid(nullable = true)
	@Schema(description = "The party ID of the sender.", example = "582462d3-ae94-476f-96de-f495a927dea8")
	private String partyId;

	public static Sender create() {
		return new Sender();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Sender withUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getAdministratorId() {
		return administratorId;
	}

	public void setAdministratorId(String administratorId) {
		this.administratorId = administratorId;
	}

	public Sender withAdministratorId(String administratorId) {
		this.administratorId = administratorId;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public Sender withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(administratorId, partyId, userId);
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
		Sender other = (Sender) obj;
		return Objects.equals(administratorId, other.administratorId) && Objects.equals(partyId, other.partyId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "Sender [userId=" + userId + ", administratorId=" + administratorId + ", partyId=" + partyId + "]";
	}
}
