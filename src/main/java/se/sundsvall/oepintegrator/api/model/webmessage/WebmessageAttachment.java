package se.sundsvall.oepintegrator.api.model.webmessage;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Webmessage attachment model")
public class WebmessageAttachment {

	@Schema(description = "The Id for the attachment", example = "1")
	private Integer attachmentId;

	@Schema(description = "The name of the file", example = "file.txt")
	private String name;

	@Schema(description = "The extension of the file", example = "txt")
	private String extension;

	@Schema(description = "The mime type of the file", example = "text/plain")
	private String mimeType;

	public static WebmessageAttachment create() {
		return new WebmessageAttachment();
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(final Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public WebmessageAttachment withAttachmentId(final Integer attachmentId) {
		this.attachmentId = attachmentId;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public WebmessageAttachment withName(final String name) {
		this.name = name;
		return this;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(final String extension) {
		this.extension = extension;
	}

	public WebmessageAttachment withExtension(final String extension) {
		this.extension = extension;
		return this;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	public WebmessageAttachment withMimeType(final String mimeType) {
		this.mimeType = mimeType;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final WebmessageAttachment that = (WebmessageAttachment) o;
		return Objects.equals(attachmentId, that.attachmentId) && Objects.equals(name, that.name) && Objects.equals(extension, that.extension) && Objects.equals(mimeType, that.mimeType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(attachmentId, name, extension, mimeType);
	}

	@Override
	public String
		toString() {
		return "WebmessageAttachment{" +
			"attachmentId=" + attachmentId +
			", name='" + name + '\'' +
			", extension='" + extension + '\'' +
			", mimeType='" + mimeType + '\'' +
			'}';
	}
}
