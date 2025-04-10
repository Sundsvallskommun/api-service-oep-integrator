package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.Objects;

@Schema(description = "SetStatus request model")
public class CaseStatusChangeRequest {

	@Schema(description = "The status ID", example = "123")
	private Integer id;

	@Schema(description = "The status name", example = "Inskickat")
	private String name;

	@Valid
	@Schema(description = "The principal")
	private Principal principal;

	public static CaseStatusChangeRequest create() {
		return new CaseStatusChangeRequest();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CaseStatusChangeRequest withId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CaseStatusChangeRequest withName(String name) {
		this.name = name;
		return this;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

	public CaseStatusChangeRequest withPrincipal(Principal principal) {
		this.principal = principal;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, principal);
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
		CaseStatusChangeRequest other = (CaseStatusChangeRequest) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(principal, other.principal);
	}

	@Override
	public String toString() {
		return "CaseStatusChangeRequest [id=" + id + ", name=" + name + ", principal=" + principal + "]";
	}
}
