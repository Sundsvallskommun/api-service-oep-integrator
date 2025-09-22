package se.sundsvall.oepintegrator.integration.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Objects;
import org.hibernate.annotations.UuidGenerator;
import se.sundsvall.oepintegrator.util.enums.InstanceType;

@Entity
@Table(name = "black_list", indexes = {
	@Index(name = "idx_municipality_id", columnList = "municipality_id"),
	@Index(name = "idx_instance_type", columnList = "instance_type"),
	@Index(name = "idx_family_id", columnList = "family_id"),
	@Index(name = "idx_municipality_id_instance_type", columnList = "municipality_id, instance_type")
})
public class BlackListEntity {

	@Id
	@UuidGenerator
	@Column(name = "id")
	private String id;

	@Column(name = "municipality_id", length = 8)
	private String municipalityId;

	@Column(name = "family_id")
	private String familyId;

	@Column(name = "instance_type")
	private InstanceType instanceType;

	public static BlackListEntity create() {
		return new BlackListEntity();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BlackListEntity withId(String id) {
		this.id = id;
		return this;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public BlackListEntity withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public BlackListEntity withFamilyId(String familyId) {
		this.familyId = familyId;
		return this;
	}

	public InstanceType getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(InstanceType instanceType) {
		this.instanceType = instanceType;
	}

	public BlackListEntity withInstanceType(InstanceType instanceType) {
		this.instanceType = instanceType;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(familyId, id, instanceType, municipalityId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlackListEntity other = (BlackListEntity) obj;
		return Objects.equals(familyId, other.familyId) && Objects.equals(id, other.id) && instanceType == other.instanceType && Objects.equals(municipalityId, other.municipalityId);
	}

	@Override
	public String toString() {
		return "BlackListEntity [id=" + id + ", municipalityId=" + municipalityId + ", familyId=" + familyId + ", instanceType=" + instanceType + "]";
	}
}
