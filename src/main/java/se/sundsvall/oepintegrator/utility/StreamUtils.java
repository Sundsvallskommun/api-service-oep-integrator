package se.sundsvall.oepintegrator.utility;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LAST_MODIFIED;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.zalando.problem.Problem;

public final class StreamUtils {

	private StreamUtils() {
		// Prevent instantiation
	}

	public static void copyResponseEntityToHttpServletResponse(final ResponseEntity<InputStreamResource> responseEntity, final HttpServletResponse response, final String errorMessage) {
		try (final var inputStream = Objects.requireNonNull(responseEntity.getBody()).getInputStream();
			final var outputStream = response.getOutputStream()) {

			final var headerSet = Set.of(LAST_MODIFIED, CONTENT_TYPE, CONTENT_DISPOSITION, CONTENT_LENGTH);
			headerSet.forEach(header -> responseEntity.getHeaders()
				.getOrDefault(header, List.of())
				.stream()
				.findFirst().ifPresent(value -> response.setHeader(header, value)));

			org.springframework.util.StreamUtils.copy(inputStream, outputStream);
		} catch (final IOException e) {
			throw Problem.valueOf(INTERNAL_SERVER_ERROR, errorMessage);
		}
	}
}
