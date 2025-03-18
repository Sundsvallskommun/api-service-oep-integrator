package se.sundsvall.oepintegrator.integration.opene.soap.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalMessageAttachment {

	@JacksonXmlProperty(localName = "attachmentID")
	private int attachmentID;

	@JacksonXmlProperty(localName = "filename")
	private String fileName;

	public int getAttachmentID() {
		return attachmentID;
	}

	public void setAttachmentID(final int attachmentID) {
		this.attachmentID = attachmentID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final ExternalMessageAttachment that = (ExternalMessageAttachment) o;
		return attachmentID == that.attachmentID && Objects.equals(fileName, that.fileName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(attachmentID, fileName);
	}

	@Override
	public String toString() {
		return "ExternalMessageAttachment{" +
			"attachmentID=" + attachmentID +
			", fileName='" + fileName + '\'' +
			'}';
	}
}
