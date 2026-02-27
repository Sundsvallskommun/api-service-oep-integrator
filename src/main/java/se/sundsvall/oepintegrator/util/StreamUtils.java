package se.sundsvall.oepintegrator.util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import se.sundsvall.dept44.problem.Problem;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LAST_MODIFIED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public final class StreamUtils {

	private StreamUtils() {}

	public static void copyResponseEntityToHttpServletResponse(final ResponseEntity<Resource> responseEntity, final HttpServletResponse response, final String errorMessage) {
		try (final var inputStream = Objects.requireNonNull(responseEntity.getBody()).getInputStream();
			final var outputStream = response.getOutputStream()) {

			final var headerSet = Set.of(LAST_MODIFIED, CONTENT_TYPE, CONTENT_DISPOSITION, CONTENT_LENGTH);
			headerSet.forEach(header -> responseEntity.getHeaders()
				.getOrDefault(header, emptyList())
				.stream()
				.findFirst().ifPresent(value -> response.setHeader(header, value)));

			org.springframework.util.StreamUtils.copy(inputStream, outputStream);
		} catch (final IOException e) {
			throw Problem.valueOf(INTERNAL_SERVER_ERROR, errorMessage);
		}
	}
}
