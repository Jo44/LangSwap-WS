package fr.langswap_ws.presentation.endpoint.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import fr.langswap_ws.config.Settings;
import fr.langswap_ws.domain.exception.TechnicalException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/** Home endpoint test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Home endpoint test")
class HomeEndpointTest {

	/** Group : Configuration tests */
	@Nested
	@DisplayName("Group : Configuration tests")
	class ConfigurationTests {

		/** Test : Should expose /home path */
		@Test
		@DisplayName("Test : Should expose /home path")
		void shouldExposeHomePath() {
			Path path = HomeEndpoint.class.getAnnotation(Path.class);

			assertNotNull(path);
			assertEquals("/home", path.value());
		}
	}

	/** Group : Retrieval tests */
	@Nested
	@DisplayName("Group : Retrieval tests")
	class GetHomeTranslationsTests {

		/** Test : Should proxy translations successfully */
		@Test
		@DisplayName("Test : Should proxy translations successfully")
		void shouldProxyTranslationsSuccessfully() throws Exception {
			TestableHomeEndpoint endpoint = new TestableHomeEndpoint();
			Settings settings = mock(Settings.class);
			HttpServletRequest request = mock(HttpServletRequest.class);
			@SuppressWarnings("unchecked")
			HttpResponse<String> httpResponse = mock(HttpResponse.class);
			setField(endpoint, "settings", settings);
			setField(endpoint, "request", request);
			when(settings.getString("api.key")).thenReturn("test-api-key");
			when(request.getScheme()).thenReturn("http");
			when(request.getServerName()).thenReturn("localhost");
			when(request.getServerPort()).thenReturn(8080);
			when(request.getContextPath()).thenReturn("/LangSwap-WS");
			when(httpResponse.statusCode()).thenReturn(200);
			when(httpResponse.body()).thenReturn("[]");
			endpoint.httpResponse = httpResponse;

			Response response = endpoint.getTranslations();

			assertEquals(200, response.getStatus());
			assertEquals("[]", response.getEntity());
			assertEquals("http://localhost:8080/LangSwap-WS/api/obfuscated-translations",
					endpoint.lastAPIUrl);
			assertEquals("test-api-key", endpoint.lastAPIKey);
		}

		/** Test : Should omit default HTTP port */
		@Test
		@DisplayName("Test : Should omit default HTTP port")
		void shouldOmitDefaultHttpPort() throws Exception {
			TestableHomeEndpoint endpoint = new TestableHomeEndpoint();
			Settings settings = mock(Settings.class);
			HttpServletRequest request = mock(HttpServletRequest.class);
			@SuppressWarnings("unchecked")
			HttpResponse<String> httpResponse = mock(HttpResponse.class);
			setField(endpoint, "settings", settings);
			setField(endpoint, "request", request);
			when(settings.getString("api.key")).thenReturn("test-api-key");
			when(request.getScheme()).thenReturn("http");
			when(request.getServerName()).thenReturn("localhost");
			when(request.getServerPort()).thenReturn(80);
			when(request.getContextPath()).thenReturn("/LangSwap-WS");
			when(httpResponse.statusCode()).thenReturn(200);
			when(httpResponse.body()).thenReturn("[]");
			endpoint.httpResponse = httpResponse;

			endpoint.getTranslations();

			assertEquals("http://localhost/LangSwap-WS/api/obfuscated-translations", endpoint.lastAPIUrl);
		}

		/** Test : Should convert errors to TechnicalException */
		@Test
		@DisplayName("Test : Should convert errors to TechnicalException")
		void shouldWrapErrorsIntoTechnicalException() throws Exception {
			TestableHomeEndpoint endpoint = new TestableHomeEndpoint();
			Settings settings = mock(Settings.class);
			HttpServletRequest request = mock(HttpServletRequest.class);
			setField(endpoint, "settings", settings);
			setField(endpoint, "request", request);
			when(settings.getString("api.key")).thenReturn("test-api-key");
			when(request.getScheme()).thenReturn("http");
			when(request.getServerName()).thenReturn("localhost");
			when(request.getServerPort()).thenReturn(8080);
			when(request.getContextPath()).thenReturn("/LangSwap-WS");
			endpoint.exceptionToThrow = new RuntimeException("boom");

			TechnicalException exception = assertThrows(TechnicalException.class,
					endpoint::getTranslations);

			assertNotNull(exception.getMessage());
		}
	}

	/** Set field
	 * 
	 * @param target
	 * @param fieldName
	 * @param value
	 * @throws Exception */
	private static void setField(Object target, String fieldName, Object value) throws Exception {
		Field field = HomeEndpoint.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	/** Testable home endpoint */
	private static class TestableHomeEndpoint extends HomeEndpoint {
		private String lastAPIUrl;
		private String lastAPIKey;
		private HttpResponse<String> httpResponse;
		private Exception exceptionToThrow;

		/** Send request
		 * 
		 * @param */
		@Override
		protected HttpResponse<String> sendRequest(String APIUrl, String APIKey) throws Exception {
			lastAPIUrl = APIUrl;
			lastAPIKey = APIKey;
			if (exceptionToThrow != null) {
				throw exceptionToThrow;
			}
			return httpResponse;
		}
	}

}
