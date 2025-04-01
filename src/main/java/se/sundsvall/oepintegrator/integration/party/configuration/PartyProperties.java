package se.sundsvall.oepintegrator.integration.party.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("integration.party")
record PartyProperties(@DefaultValue("10") int connectTimeout, @DefaultValue("20") int readTimeout) {
}
