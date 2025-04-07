package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Case status model")
public class CaseStatus {

	@Schema(description = "The status ID", example = "123")
	private Integer id;

	@Schema(description = "The status name", example = "Inskickat")
	private String name;

	public static CaseStatus create() {
		return new CaseStatus();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CaseStatus withId(Integer id) {
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

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CaseStatus other = (CaseStatus) obj;
		return id == other.id && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "CaseStatus [name=" + name + ", id=" + id + "]";
	}
}
