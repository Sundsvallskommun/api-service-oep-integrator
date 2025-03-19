package se.sundsvall.oepintegrator.api.model.webmessage;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.Objects;

@Schema(description = "Webmessage attachment data model")
public class WebmessageAttachmentData {

	private byte[] data;

	public static WebmessageAttachmentData create() {
		return new WebmessageAttachmentData();
	}

	public WebmessageAttachmentData withData(final byte[] data) {
		this.data = data;
		return this;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		WebmessageAttachmentData that = (WebmessageAttachmentData) o;
		return Objects.deepEquals(data, that.data);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(data);
	}

	@Override
	public String toString() {
		return "WebmessageAttachmentData{" +
			"data=" + Arrays.toString(data) +
			'}';
	}
}
