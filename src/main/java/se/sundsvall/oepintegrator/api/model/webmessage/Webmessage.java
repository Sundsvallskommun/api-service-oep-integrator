package se.sundsvall.oepintegrator.api.model.webmessage;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

@Schema(description = "Webmessage model")
public class Webmessage {

	@Schema(description = "The webMessageCollector Id for the message", example = "1")
	private Integer id;

	@Schema(description = "If the message is inbound or outbound. Inbound means coming from open-E. Outbound means sent to open-E", example = "INBOUND")
	private Direction direction;

	@Schema(description = "The municipality id", example = "2281")
	private String municipalityId;

	@Schema(description = "What E-service the message was found in", example = "501")
	private String familyId;

	@Schema(description = "The external caseID ", example = "caa230c6-abb4-4592-ad9a-34e263c2787b")
	private String externalCaseId;

	@Schema(description = "The message ", example = "Hello World")
	private String message;

	@Schema(description = "The unique messageId from openE for the message", example = "12")
	private String messageId;

	@Schema(description = "Time and date the message was sent ", example = "2023-02-23 17:26:23")
	private String sent;

	@Schema(description = "Username for the poster", example = "te01st")
	private String username;

	@Schema(description = "Firstname of the poster ", example = "Test")
	private String firstName;

	@Schema(description = "Lastname of the poster", example = "Testsson")
	private String lastName;

	@Schema(description = "Email for the poster", example = "test@sundsvall.se")
	private String email;

	@Schema(description = "The userId for the poster", example = "123")
	private String userId;

	@ArraySchema(schema = @Schema(description = "List of attachments for the message", example = "attachment1, attachment2"))
	private List<WebmessageAttachment> attachments;

	@Schema(description = "The instance of the message", example = "external")
	private String instance;

	public static Webmessage create() {
		return new Webmessage();
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Webmessage withId(final Integer id) {
		this.id = id;
		return this;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(final Direction direction) {
		this.direction = direction;
	}

	public Webmessage withDirection(final Direction direction) {
		this.direction = direction;
		return this;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public Webmessage withMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(final String familyId) {
		this.familyId = familyId;
	}

	public Webmessage withFamilyId(final String familyId) {
		this.familyId = familyId;
		return this;
	}

	public String getExternalCaseId() {
		return externalCaseId;
	}

	public void setExternalCaseId(final String externalCaseId) {
		this.externalCaseId = externalCaseId;
	}

	public Webmessage withExternalCaseId(final String externalCaseId) {
		this.externalCaseId = externalCaseId;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public Webmessage withMessage(final String message) {
		this.message = message;
		return this;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(final String messageId) {
		this.messageId = messageId;
	}

	public Webmessage withMessageId(final String messageId) {
		this.messageId = messageId;
		return this;
	}

	public String getSent() {
		return sent;
	}

	public void setSent(final String sent) {
		this.sent = sent;
	}

	public Webmessage withSent(final String sent) {
		this.sent = sent;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public Webmessage withUsername(final String username) {
		this.username = username;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public Webmessage withFirstName(final String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public Webmessage withLastName(final String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public Webmessage withEmail(final String email) {
		this.email = email;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public Webmessage withUserId(final String userId) {
		this.userId = userId;
		return this;
	}

	public List<WebmessageAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(final List<WebmessageAttachment> attachments) {
		this.attachments = attachments;
	}

	public Webmessage withAttachments(final List<WebmessageAttachment> attachments) {
		this.attachments = attachments;
		return this;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(final String instance) {
		this.instance = instance;
	}

	public Webmessage withInstance(final String instance) {
		this.instance = instance;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final Webmessage that = (Webmessage) o;
		return Objects.equals(id, that.id) && direction == that.direction && Objects.equals(municipalityId, that.municipalityId) && Objects.equals(familyId, that.familyId) && Objects.equals(externalCaseId,
			that.externalCaseId) && Objects.equals(message, that.message) && Objects.equals(messageId, that.messageId) && Objects.equals(sent, that.sent) && Objects.equals(username, that.username)
			&& Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(userId, that.userId) && Objects.equals(attachments,
				that.attachments) && Objects.equals(instance, that.instance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, direction, municipalityId, familyId, externalCaseId, message, messageId, sent, username, firstName, lastName, email, userId, attachments, instance);
	}

	@Override
	public String toString() {
		return "Webmessage{" +
			"id=" + id +
			", direction=" + direction +
			", municipalityId='" + municipalityId + '\'' +
			", familyId='" + familyId + '\'' +
			", externalCaseId='" + externalCaseId + '\'' +
			", message='" + message + '\'' +
			", messageId='" + messageId + '\'' +
			", sent='" + sent + '\'' +
			", username='" + username + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", email='" + email + '\'' +
			", userId='" + userId + '\'' +
			", attachments=" + attachments +
			", instance='" + instance + '\'' +
			'}';
	}
}
