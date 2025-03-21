package se.sundsvall.oepintegrator.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import se.sundsvall.oepintegrator.utility.enums.InstanceType;
import se.sundsvall.oepintegrator.utility.enums.IntegrationType;

@Schema(description = "Instance model")
public class Instance {

	@Schema(description = "InstanceType ID", example = "123e4567-e89b-12d3-a456-426614174000")
	private String id;

	@Schema(description = "Type of integration. SOAP/REST", example = "SOAP")
	private IntegrationType integrationType;

	@Schema(description = "InstanceType type. INTERNAL/EXTERNAL", example = "INTERNAL")
	private InstanceType instanceType;

	@Schema(description = "Base URL of the instanceType", example = "https://example.com")
	private String baseUrl;

	@Schema(description = "Username for the instanceType", accessMode = WRITE_ONLY, example = "user123")
	private String username;

	@Schema(description = "Password for the instanceType", accessMode = WRITE_ONLY, example = "pass123")
	private String password;

	@Schema(description = "List with family IDs", example = "[\"family1\", \"family2\"]")
	private List<String> familyIds;

	@Schema(description = "Connection timeout in seconds", example = "5")
	private Integer connectTimeout;

	@Schema(description = "Read timeout in seconds", example = "60")
	private Integer readTimeout;

	public static Instance create() {
		return new Instance();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Instance withId(final String id) {
		this.id = id;
		return this;
	}

	public IntegrationType getIntegrationType() {
		return integrationType;
	}

	public void setIntegrationType(final IntegrationType integrationType) {
		this.integrationType = integrationType;
	}

	public Instance withIntegrationType(final IntegrationType integrationType) {
		this.integrationType = integrationType;
		return this;
	}

	public InstanceType getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(final InstanceType instanceType) {
		this.instanceType = instanceType;
	}

	public Instance withInstanceType(final InstanceType instanceType) {
		this.instanceType = instanceType;
		return this;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Instance withBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public Instance withUsername(final String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Instance withPassword(final String password) {
		this.password = password;
		return this;
	}

	public List<String> getFamilyIds() {
		return familyIds;
	}

	public void setFamilyIds(final List<String> familyIds) {
		this.familyIds = familyIds;
	}

	public Instance withFamilyIds(final List<String> familyIds) {
		this.familyIds = familyIds;
		return this;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(final Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Instance withConnectTimeout(final Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(final Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Instance withReadTimeout(final Integer readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final Instance instance = (Instance) o;
		return Objects.equals(id, instance.id) && integrationType == instance.integrationType && instanceType == instance.instanceType && Objects.equals(baseUrl, instance.baseUrl) && Objects.equals(username, instance.username)
			&& Objects.equals(password, instance.password) && Objects.equals(familyIds, instance.familyIds) && Objects.equals(connectTimeout, instance.connectTimeout) && Objects.equals(readTimeout, instance.readTimeout);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, integrationType, instanceType, baseUrl, username, password, familyIds, connectTimeout, readTimeout);
	}

	@Override
	public String toString() {
		return "Instance{" +
			"id='" + id + '\'' +
			", integrationType=" + integrationType +
			", instanceType=" + instanceType +
			", baseUrl='" + baseUrl + '\'' +
			", username='" + username + '\'' +
			", password='" + password + '\'' +
			", familyIds=" + familyIds +
			", connectTimeout=" + connectTimeout +
			", readTimeout=" + readTimeout +
			'}';
	}
}
