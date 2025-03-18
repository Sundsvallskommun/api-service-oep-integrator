package se.sundsvall.oepintegrator.integration.opene.soap.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalMessage {

	private boolean postedByManager;

	private int messageID;

	private String message;

	private Poster poster;

	private String added;

	private int flowInstanceID;

	@JacksonXmlElementWrapper(localName = "attachments")
	private List<ExternalMessageAttachment> attachments;

	public String getAdded() {
		return added;
	}

	public void setAdded(final String added) {
		this.added = added;
	}

	public boolean isPostedByManager() {
		return postedByManager;
	}

	public void setPostedByManager(final boolean postedByManager) {
		this.postedByManager = postedByManager;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(final int messageID) {
		this.messageID = messageID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public Poster getPoster() {
		return poster;
	}

	public void setPoster(final Poster poster) {
		this.poster = poster;
	}

	public int getFlowInstanceID() {
		return flowInstanceID;
	}

	public void setFlowInstanceID(final int flowInstanceID) {
		this.flowInstanceID = flowInstanceID;
	}

	public List<ExternalMessageAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(final List<ExternalMessageAttachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final ExternalMessage that = (ExternalMessage) o;
		return postedByManager == that.postedByManager && messageID == that.messageID && flowInstanceID == that.flowInstanceID && Objects.equals(message, that.message) && Objects.equals(poster, that.poster) && Objects.equals(
			added, that.added) && Objects.equals(attachments, that.attachments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(postedByManager, messageID, message, poster, added, flowInstanceID, attachments);
	}

	@Override
	public String toString() {
		return "ExternalMessage{" +
			"postedByManager=" + postedByManager +
			", messageID=" + messageID +
			", message='" + message + '\'' +
			", poster=" + poster +
			", added='" + added + '\'' +
			", flowInstanceID=" + flowInstanceID +
			", attachments=" + attachments +
			'}';
	}
}
