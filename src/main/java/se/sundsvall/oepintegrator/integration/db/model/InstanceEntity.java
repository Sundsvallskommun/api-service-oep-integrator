package se.sundsvall.oepintegrator.integration.db.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.UuidGenerator;
import se.sundsvall.oepintegrator.integration.db.model.enums.InstanceType;
import se.sundsvall.oepintegrator.integration.db.model.enums.IntegrationType;

@Entity
@Table(name = "open_e_instance", indexes = {
	@Index(name = "idx_municipality_id", columnList = "municipality_id"),
	@Index(name = "idx_municipality_id_id", columnList = "municipality_id, id"),
	@Index(name = "idx_integration_type", columnList = "integration_type"),
	@Index(name = "idx_id_municipality_id", columnList = "id, municipality_id")
})
public class InstanceEntity {

	@Id
	@UuidGenerator
	@Column(name = "id")
	private String id;

	@Column(name = "municipality_id")
	private String municipalityId;

	@Column(name = "integration_type")
	private IntegrationType integrationType;

	@Column(name = "instance_type")
	private InstanceType instanceType;

	@Column(name = "base_url")
	private String baseUrl;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@ElementCollection
	@CollectionTable(
		name = "family_ids",
		joinColumns = @JoinColumn(name = "instance_id", foreignKey = @ForeignKey(name = "fk_family_ids_instance_id")))
	@Column(name = "family_id")
	private List<@NotBlank String> familyIds;

	@Column(name = "connect_timeout")
	private int connectTimeout;

	@Column(name = "read_timeout")
	private int readTimeout;

	public static InstanceEntity create() {
		return new InstanceEntity();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public InstanceEntity withId(final String id) {
		this.id = id;
		return this;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public InstanceEntity withMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public IntegrationType getIntegrationType() {
		return integrationType;
	}

	public void setIntegrationType(final IntegrationType integrationType) {
		this.integrationType = integrationType;
	}

	public InstanceEntity withIntegrationType(final IntegrationType integrationType) {
		this.integrationType = integrationType;
		return this;
	}

	public InstanceType getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(final InstanceType instanceType) {
		this.instanceType = instanceType;
	}

	public InstanceEntity withInstanceType(final InstanceType instanceType) {
		this.instanceType = instanceType;
		return this;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public InstanceEntity withBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public InstanceEntity withUsername(final String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public InstanceEntity withPassword(final String password) {
		this.password = password;
		return this;
	}

	public List<String> getFamilyIds() {
		return familyIds;
	}

	public void setFamilyIds(final List<String> familyIds) {
		this.familyIds = familyIds;
	}

	public InstanceEntity withFamilyIds(final List<String> familyIds) {
		this.familyIds = familyIds;
		return this;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(final int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public InstanceEntity withConnectTimeout(final int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(final int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public InstanceEntity withReadTimeout(final int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final InstanceEntity that = (InstanceEntity) o;
		return connectTimeout == that.connectTimeout && readTimeout == that.readTimeout && Objects.equals(id, that.id) && Objects.equals(municipalityId, that.municipalityId) && integrationType == that.integrationType
			&& instanceType == that.instanceType && Objects.equals(baseUrl, that.baseUrl) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(familyIds,
				that.familyIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, municipalityId, integrationType, instanceType, baseUrl, username, password, familyIds, connectTimeout, readTimeout);
	}

	@Override
	public String toString() {
		return "InstanceEntity{" +
			"id='" + id + '\'' +
			", municipalityId='" + municipalityId + '\'' +
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
