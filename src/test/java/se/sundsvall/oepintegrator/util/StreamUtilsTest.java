package se.sundsvall.oepintegrator.util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import se.sundsvall.dept44.problem.Problem;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StreamUtilsTest {

	@Test
	void copyResponseEntityToHttpServletResponse() {
		// Arrange
		final var mockHttpServletResponse = new MockHttpServletResponse();
		final var headers = Map.of(
			"Content-Type", List.of("application/pdf"),
			"Content-Disposition", List.of("attachment; filename=case.pdf"),
			"Content-Length", List.of("0"),
			"Last-Modified", List.of("Wed, 21 Oct 2015 07:28:00 GMT"));
		final Resource resource = new InputStreamResource(new ByteArrayInputStream(new byte[10]));
		final var responseEntity = ResponseEntity.ok()
			.headers(httpHeaders -> httpHeaders.putAll(headers))
			.body(resource);

		// Act
		StreamUtils.copyResponseEntityToHttpServletResponse(responseEntity, mockHttpServletResponse, "Unable to get case pdf");

		// Assert
		assertThat(mockHttpServletResponse.getHeader("Content-Type")).isEqualTo("application/pdf");
		assertThat(mockHttpServletResponse.getHeader("Content-Disposition")).isEqualTo("attachment; filename=case.pdf");
		assertThat(mockHttpServletResponse.getHeader("Content-Length")).isEqualTo("0");
		assertThat(mockHttpServletResponse.getHeader("Last-Modified")).isEqualTo("Wed, 21 Oct 2015 07:28:00 GMT");
	}

	@Test
	void copyResponseEntityToHttpServletResponseWithException() throws IOException {
		// Arrange
		final var mockHttpServletResponse = mock(HttpServletResponse.class);
		final ResponseEntity<Resource> responseEntity = ResponseEntity.ok()
			.body(new InputStreamResource(new ByteArrayInputStream(new byte[10])));

		when(mockHttpServletResponse.getOutputStream()).thenThrow(new IOException("Unable to write to output stream"));

		// Act & Assert
		assertThatThrownBy(() -> StreamUtils.copyResponseEntityToHttpServletResponse(responseEntity, mockHttpServletResponse, "Unable to get case pdf"))
			.isInstanceOf(Problem.class)
			.hasMessage("Internal Server Error: Unable to get case pdf");
	}
}
