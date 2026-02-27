package se.sundsvall.oepintegrator.integration.opene.soap.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Messages {

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "ExternalMessage")
	public List<ExternalMessage> externalMessages;

	public List<ExternalMessage> getExternalMessages() {

		return externalMessages;
	}

	public void setExternalMessages(final List<ExternalMessage> externalMessages) {
		this.externalMessages = externalMessages;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final Messages messages = (Messages) o;
		return Objects.equals(externalMessages, messages.externalMessages);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(externalMessages);
	}

	@Override
	public String toString() {
		return "Messages{" +
			"externalMessages=" + externalMessages +
			'}';
	}
}
