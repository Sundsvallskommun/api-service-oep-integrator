package se.sundsvall.oepintegrator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.oepintegrator.api.model.cases.CaseEnvelope;
import se.sundsvall.oepintegrator.api.model.cases.ConfirmDeliveryRequest;

@ExtendWith(ResourceLoaderExtension.class)
class CaseMapperTest {

	@Test
	void toCaseEnvelopList(@Load("/mappings/flow-instances.xml") final String xml) {

		// Act
		final var result = CaseMapper.toCaseEnvelopeList(xml.getBytes());

		// Assert
		assertThat(result).isNotNull().hasSize(4);
		assertThat(result)
			.extracting(CaseEnvelope::getFlowInstanceId, CaseEnvelope::getCreated, CaseEnvelope::getStatusUpdated)
			.containsExactlyInAnyOrder(
				tuple("4999", LocalDateTime.parse("2025-02-27T11:16"), LocalDateTime.parse("2025-03-06T09:10")),
				tuple("4965", LocalDateTime.parse("2025-02-18T19:12"), LocalDateTime.parse("2025-02-18T19:45")),
				tuple("4933", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-14T12:40")),
				tuple("4932", LocalDateTime.parse("2025-02-14T12:39"), LocalDateTime.parse("2025-02-18T20:10")));
	}

	@Test
	void toCaseEnvelopListWhenResultIsEmpty(@Load("/mappings/empty-flow-instances.xml") final String xml) {

		// Act
		final var result = CaseMapper.toCaseEnvelopeList(xml.getBytes());

		// Assert
		assertThat(result).isEmpty();
	}

	@Test
	void toConfirmDelivery() {
		// Arrange
		final var flowInstanceId = "123456";
		final var request = new ConfirmDeliveryRequest()
			.withCaseId("789012")
			.withDelivered(true)
			.withLogMessage("The case was delivered successfully")
			.withSystem("ByggR");

		// Act
		final var result = CaseMapper.toConfirmDelivery(flowInstanceId, request);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(123456);
		assertThat(result.getExternalID().getID()).isEqualTo("789012");
		assertThat(result.getExternalID().getSystem()).isEqualTo("ByggR");
		assertThat(result.isDelivered()).isTrue();
		assertThat(result.getLogMessage()).isEqualTo("The case was delivered successfully");
	}

	@Test
	void toConfirmDeliveryWithNullValues() {
		// Arrange
		final var flowInstanceId = "123456";
		final var request = new ConfirmDeliveryRequest()
			.withCaseId(null)
			.withDelivered(false)
			.withLogMessage(null)
			.withSystem(null);

		// Act
		final var result = CaseMapper.toConfirmDelivery(flowInstanceId, request);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFlowInstanceID()).isEqualTo(Integer.valueOf(flowInstanceId));
		assertThat(result.getExternalID().getID()).isNull();
		assertThat(result.getExternalID().getSystem()).isNull();
		assertThat(result.isDelivered()).isFalse();
		assertThat(result.getLogMessage()).isNull();
	}

	@Test
	void toCase() {

		// Resource loading does not work with ISO-8859-1 encoding, so we have to use a string literal
		final var xml = """
			<?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
			<FlowInstance xmlns="http://www.oeplatform.org/version/2.0/schemas/flowinstance">
				<Header>
					<Flow>
						<FamilyID>832</FamilyID>
						<Version>4</Version>
						<FlowID>2411</FlowID>
						<Name><![CDATA[Anmälan om sjukfrånvaro]]></Name>
					</Flow>
					<FlowInstanceID>235212</FlowInstanceID>
					<Status>
						<ID>17453</ID>
						<Name><![CDATA[Preliminär]]></Name>
					</Status>
					<Poster>
						<Firstname><![CDATA[Namn]]></Firstname>
						<Lastname><![CDATA[Efternamn]]></Lastname>
						<Email><![CDATA[namn.efternamn@sundsvall.se]]></Email>
						<ID>17811</ID>
					</Poster>
					<Owner>
						<Firstname><![CDATA[Namn]]></Firstname>
						<Lastname><![CDATA[Efternamn]]></Lastname>
						<Email><![CDATA[namn.efternamn@sundsvall.se]]></Email>
						<ID>17811</ID>
					</Owner>
					<Posted>2024-12-05T21:41:45</Posted>
					<FirstSubmitted>2024-12-05T21:41:45</FirstSubmitted>
					<LastSubmitted>2024-12-05T21:41:45</LastSubmitted>
				</Header>
				<Values>
					<applicant>
						<QueryID>66864</QueryID>
						<Name><![CDATA[Anmälare]]></Name>
						<firstname><![CDATA[Namn]]></firstname>
						<lastname><![CDATA[Efternamn]]></lastname>
						<username><![CDATA[nam99eft]]></username>
						<email><![CDATA[namn.efternamn@sundsvall.se]]></email>
						<phone><![CDATA[060-192306]]></phone>
						<title><![CDATA[Enhetschef]]></title>
						<organization><![CDATA[VOF MYN Ledning]]></organization>
					</applicant>
					<administrativeUnit>
						<QueryID>66866</QueryID>
						<Name><![CDATA[I vilken förvaltning/verksamhet arbetar medarbetaren?]]></Name>
						<Value>vof</Value>
					</administrativeUnit>
					<employmentType>
						<QueryID>66867</QueryID>
						<Name><![CDATA[Gäller det sjukfrånvaro för en]]></Name>
						<Value>Månadsavlönad</Value>
					</employmentType>
					<employeeData>
						<QueryID>66875</QueryID>
						<Name><![CDATA[Vilken anställd gäller det?]]></Name>
						<firstname><![CDATA[Namn]]></firstname>
						<lastname><![CDATA[Efternamn]]></lastname>
						<username><![CDATA[nam00eft]]></username>
						<citizenIdentifier><![CDATA[************]]></citizenIdentifier>
						<title><![CDATA[Biståndshandläggare]]></title>
						<organization><![CDATA[VOF MYN LSS Biståndshandl.]]></organization>
						<formOfEmployment><![CDATA[Tillsvidare                   ]]></formOfEmployment>
					</employeeData>
					<employeeTitle>
						<QueryID>66879</QueryID>
						<Name><![CDATA[Medarbetarens befattningar]]></Name>
						<Value>Biståndshandläggare</Value>
					</employeeTitle>
					<absentNewOld>
						<QueryID>66895</QueryID>
						<Name><![CDATA[Sjukfrånvaro avser]]></Name>
						<Value>Ny sjukfrånvaro</Value>
					</absentNewOld>
					<absentDateFrom>
						<QueryID>66896</QueryID>
						<Name><![CDATA[Datum första sjukdag]]></Name>
						<StartDate>2024-11-30</StartDate>
					</absentDateFrom>
					<sickNotePeriod>
						<QueryID>66897</QueryID>
						<Name><![CDATA[Antal sjukskrivningsperioder i sjukintyget:]]></Name>
						<Value>2</Value>
					</sickNotePeriod>
					<sickNotePercentRow1>
						<QueryID>66900</QueryID>
						<Name><![CDATA[Omfattning på sjukfrånvaro enligt läkarintyg]]></Name>
						<Value>100%</Value>
					</sickNotePercentRow1>
					<sickNotePeriodRow1>
						<QueryID>66901</QueryID>
						<Name><![CDATA[Sjukskrivningsperioden from tom]]></Name>
						<Datum_fran><![CDATA[2024-11-30]]></Datum_fran>
						<Datum_till><![CDATA[2024-12-08]]></Datum_till>
					</sickNotePeriodRow1>
					<sickNotePercentRow2>
						<QueryID>66909</QueryID>
						<Name><![CDATA[Omfattning på sjukfrånvaro enligt läkarintyg]]></Name>
						<Value>50%</Value>
					</sickNotePercentRow2>
					<sickNotePeriodRow2>
						<QueryID>66910</QueryID>
						<Name><![CDATA[Sjukskrivningsperioden from tom]]></Name>
						<Datum_fran><![CDATA[2024-12-09]]></Datum_fran>
						<Datum_till><![CDATA[2024-12-20]]></Datum_till>
					</sickNotePeriodRow2>
				</Values>
			</FlowInstance>
			""";

		// Act
		final var result = CaseMapper.toCase(xml.getBytes(StandardCharsets.ISO_8859_1));

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getFlowInstanceId()).isEqualTo("235212");
		assertThat(result.getFamilyId()).isEqualTo("832");
		assertThat(result.getVersion()).isEqualTo(4);
		assertThat(result.getFlowId()).isEqualTo("2411");
		assertThat(result.getTitle()).isEqualTo("Anmälan om sjukfrånvaro");
		assertThat(result.getStatus().getName()).isEqualTo("Preliminär");
		assertThat(result.getStatus().getId()).isEqualTo(17453);
		assertThat(result.getCreated()).isEqualTo(LocalDateTime.parse("2024-12-05T21:41:45"));
		assertThat(result.getPayload().trim()).isEqualTo("""
			"<Values>
					<applicant>
						<QueryID>66864</QueryID>
						<Name><![CDATA[Anmälare]]></Name>
						<firstname><![CDATA[Namn]]></firstname>
						<lastname><![CDATA[Efternamn]]></lastname>
						<username><![CDATA[nam99eft]]></username>
						<email><![CDATA[namn.efternamn@sundsvall.se]]></email>
						<phone><![CDATA[060-192306]]></phone>
						<title><![CDATA[Enhetschef]]></title>
						<organization><![CDATA[VOF MYN Ledning]]></organization>
					</applicant>
					<administrativeUnit>
						<QueryID>66866</QueryID>
						<Name><![CDATA[I vilken förvaltning/verksamhet arbetar medarbetaren?]]></Name>
						<Value>vof</Value>
					</administrativeUnit>
					<employmentType>
						<QueryID>66867</QueryID>
						<Name><![CDATA[Gäller det sjukfrånvaro för en]]></Name>
						<Value>Månadsavlönad</Value>
					</employmentType>
					<employeeData>
						<QueryID>66875</QueryID>
						<Name><![CDATA[Vilken anställd gäller det?]]></Name>
						<firstname><![CDATA[Namn]]></firstname>
						<lastname><![CDATA[Efternamn]]></lastname>
						<username><![CDATA[nam00eft]]></username>
						<citizenIdentifier><![CDATA[************]]></citizenIdentifier>
						<title><![CDATA[Biståndshandläggare]]></title>
						<organization><![CDATA[VOF MYN LSS Biståndshandl.]]></organization>
						<formOfEmployment><![CDATA[Tillsvidare                   ]]></formOfEmployment>
					</employeeData>
					<employeeTitle>
						<QueryID>66879</QueryID>
						<Name><![CDATA[Medarbetarens befattningar]]></Name>
						<Value>Biståndshandläggare</Value>
					</employeeTitle>
					<absentNewOld>
						<QueryID>66895</QueryID>
						<Name><![CDATA[Sjukfrånvaro avser]]></Name>
						<Value>Ny sjukfrånvaro</Value>
					</absentNewOld>
					<absentDateFrom>
						<QueryID>66896</QueryID>
						<Name><![CDATA[Datum första sjukdag]]></Name>
						<StartDate>2024-11-30</StartDate>
					</absentDateFrom>
					<sickNotePeriod>
						<QueryID>66897</QueryID>
						<Name><![CDATA[Antal sjukskrivningsperioder i sjukintyget:]]></Name>
						<Value>2</Value>
					</sickNotePeriod>
					<sickNotePercentRow1>
						<QueryID>66900</QueryID>
						<Name><![CDATA[Omfattning på sjukfrånvaro enligt läkarintyg]]></Name>
						<Value>100%</Value>
					</sickNotePercentRow1>
					<sickNotePeriodRow1>
						<QueryID>66901</QueryID>
						<Name><![CDATA[Sjukskrivningsperioden from tom]]></Name>
						<Datum_fran><![CDATA[2024-11-30]]></Datum_fran>
						<Datum_till><![CDATA[2024-12-08]]></Datum_till>
					</sickNotePeriodRow1>
					<sickNotePercentRow2>
						<QueryID>66909</QueryID>
						<Name><![CDATA[Omfattning på sjukfrånvaro enligt läkarintyg]]></Name>
						<Value>50%</Value>
					</sickNotePercentRow2>
					<sickNotePeriodRow2>
						<QueryID>66910</QueryID>
						<Name><![CDATA[Sjukskrivningsperioden from tom]]></Name>
						<Datum_fran><![CDATA[2024-12-09]]></Datum_fran>
						<Datum_till><![CDATA[2024-12-20]]></Datum_till>
					</sickNotePeriodRow2>
				</Values>"
			""".trim());
	}
}
