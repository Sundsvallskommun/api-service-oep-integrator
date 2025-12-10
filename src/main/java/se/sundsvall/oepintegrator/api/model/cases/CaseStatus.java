package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Case status model")
public class CaseStatus {

	@Schema(description = "The status ID", examples = "123")
	private Integer id;

	@Schema(description = "The status name", examples = "Inskickat")
	private String name;

	@Schema(description = "Whether new external messages are disallowed")
	private Boolean newExternalMessagesDisallowed;

	@Schema(description = "Whether to add external message")
	private Boolean addExternalMessage;

	@Schema(description = "Whether to add internal message")
	private Boolean addInternalMessage;

	@Schema(description = "Whether restricted admin can delete")
	private Boolean isRestrictedAdminDeletable;

	@Schema(description = "The status", examples = "ARCHIVED")
	private String status;

	public static CaseStatus create() {
		return new CaseStatus();
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public CaseStatus withId(final Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public CaseStatus withName(final String name) {
		this.name = name;
		return this;
	}

	public Boolean getNewExternalMessagesDisallowed() {
		return newExternalMessagesDisallowed;
	}

	public void setNewExternalMessagesDisallowed(final Boolean newExternalMessagesDisallowed) {
		this.newExternalMessagesDisallowed = newExternalMessagesDisallowed;
	}

	public CaseStatus withNewExternalMessagesDisallowed(final Boolean newExternalMessagesDisallowed) {
		this.newExternalMessagesDisallowed = newExternalMessagesDisallowed;
		return this;
	}

	public Boolean getAddExternalMessage() {
		return addExternalMessage;
	}

	public void setAddExternalMessage(final Boolean addExternalMessage) {
		this.addExternalMessage = addExternalMessage;
	}

	public CaseStatus withAddExternalMessage(final Boolean addExternalMessage) {
		this.addExternalMessage = addExternalMessage;
		return this;
	}

	public Boolean getAddInternalMessage() {
		return addInternalMessage;
	}

	public void setAddInternalMessage(final Boolean addInternalMessage) {
		this.addInternalMessage = addInternalMessage;
	}

	public CaseStatus withAddInternalMessage(final Boolean addInternalMessage) {
		this.addInternalMessage = addInternalMessage;
		return this;
	}

	public Boolean getIsRestrictedAdminDeletable() {
		return isRestrictedAdminDeletable;
	}

	public void setIsRestrictedAdminDeletable(final Boolean isRestrictedAdminDeletable) {
		this.isRestrictedAdminDeletable = isRestrictedAdminDeletable;
	}

	public CaseStatus withIsRestrictedAdminDeletable(final Boolean isRestrictedAdminDeletable) {
		this.isRestrictedAdminDeletable = isRestrictedAdminDeletable;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public CaseStatus withStatus(final String status) {
		this.status = status;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, newExternalMessagesDisallowed, addExternalMessage,
			addInternalMessage, isRestrictedAdminDeletable, status);
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
		final CaseStatus other = (CaseStatus) obj;
		return Objects.equals(id, other.id)
			&& Objects.equals(name, other.name)
			&& Objects.equals(newExternalMessagesDisallowed, other.newExternalMessagesDisallowed)
			&& Objects.equals(addExternalMessage, other.addExternalMessage)
			&& Objects.equals(addInternalMessage, other.addInternalMessage)
			&& Objects.equals(isRestrictedAdminDeletable, other.isRestrictedAdminDeletable)
			&& Objects.equals(status, other.status);
	}

	@Override
	public String toString() {
		return "CaseStatus [id=" + id + ", name=" + name
			+ ", newExternalMessagesDisallowed=" + newExternalMessagesDisallowed
			+ ", addExternalMessage=" + addExternalMessage
			+ ", addInternalMessage=" + addInternalMessage
			+ ", isRestrictedAdminDeletable=" + isRestrictedAdminDeletable
			+ ", status=" + status + "]";
	}
}
