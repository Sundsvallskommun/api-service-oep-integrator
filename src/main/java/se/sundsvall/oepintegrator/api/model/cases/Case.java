package se.sundsvall.oepintegrator.api.model.cases;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "Case model")
public class Case {

	@Schema(description = "The flowInstanceId (caseId)", examples = "109581")
	private String flowInstanceId;

	@Schema(description = "The familyId", examples = "832")
	private String familyId;

	@Schema(description = "Version of the case", examples = "4")
	private Integer version;

	@Schema(description = "The flowId", examples = "2411")
	private String flowId;

	@Schema(description = "The title of the case", examples = "Anmälan om sjukfrånvaro")
	private String title;

	@Schema(description = "The case status")
	private CaseStatus status;

	@Schema(description = "The case create date", examples = "2023-11-15T11:55")
	private LocalDateTime created;

	@Schema(description = "The payload of the case in xml",
		examples = "<Values><sickNotePercentRow2><QueryID>66909</QueryID><Name><![CDATA[Omfattning p* sjukfrånvaro enligt läkarintyg]]></Name><Value>50%</Value></sickNotePercentRow2><sickNotePeriodRow2><QueryID>66910</QueryID><Name><![CDATA[Sjukskrivningsperioden from tom]]></Name><Datum_fran><![CDATA[2024-12-09]]></Datum_fran><Datum_till><![CDATA[2024-12-20]]></Datum_till></sickNotePeriodRow2></Values>")
	private String payload;

	public static Case create() {
		return new Case();
	}

	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(final String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public Case withFlowInstanceId(final String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
		return this;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(final String familyId) {
		this.familyId = familyId;
	}

	public Case withFamilyId(final String familyId) {
		this.familyId = familyId;
		return this;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}

	public Case withVersion(final Integer version) {
		this.version = version;
		return this;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(final String flowId) {
		this.flowId = flowId;
	}

	public Case withFlowId(final String flowId) {
		this.flowId = flowId;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public Case withTitle(final String title) {
		this.title = title;
		return this;
	}

	public CaseStatus getStatus() {
		return status;
	}

	public void setStatus(final CaseStatus status) {
		this.status = status;
	}

	public Case withStatus(final CaseStatus status) {
		this.status = status;
		return this;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(final LocalDateTime created) {
		this.created = created;
	}

	public Case withCreated(final LocalDateTime created) {
		this.created = created;
		return this;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(final String payload) {
		this.payload = payload;
	}

	public Case withPayload(final String payload) {
		this.payload = payload;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final Case aCase = (Case) o;
		return Objects.equals(flowInstanceId, aCase.flowInstanceId) && Objects.equals(familyId, aCase.familyId) && Objects.equals(version, aCase.version) && Objects.equals(flowId, aCase.flowId)
			&& Objects.equals(title, aCase.title) && Objects.equals(status, aCase.status) && Objects.equals(created, aCase.created) && Objects.equals(payload, aCase.payload);
	}

	@Override
	public int hashCode() {
		return Objects.hash(flowInstanceId, familyId, version, flowId, title, status, created, payload);
	}

	@Override
	public String toString() {
		return "Case{" +
			"flowInstanceId='" + flowInstanceId + '\'' +
			", familyId='" + familyId + '\'' +
			", version=" + version +
			", flowId='" + flowId + '\'' +
			", title='" + title + '\'' +
			", status=" + status +
			", created='" + created + '\'' +
			", payload='" + payload + '\'' +
			'}';
	}
}
