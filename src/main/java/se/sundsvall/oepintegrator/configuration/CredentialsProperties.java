package se.sundsvall.oepintegrator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "properties.credentials")
public record CredentialsProperties(String secretKey) {
}
