package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Case status model")
public class CaseStatus {

	private String name;

	public static CaseStatus create() {
		return new CaseStatus();
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
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CaseStatus that = (CaseStatus) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "CaseStatus{" +
			"name='" + name + '\'' +
			'}';
	}
}
