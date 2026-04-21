package fr.langswap_ws.presentation.filter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import fr.langswap_ws.config.Settings;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorCodes;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.UriInfo;

/** Authentication filter test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Authentication filter tests")
@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

	private static final String VALID_API_KEY = "valid-api-key";

	/** Mock */
	@InjectMocks
	private AuthenticationFilter filter;
	@Mock
	private ContainerRequestContext requestContext;
	@Mock
	private UriInfo uriInfo;
	@Mock
	private Settings settings;

	/** Set up */
	@BeforeEach
	void setUp() {
		when(requestContext.getUriInfo()).thenReturn(uriInfo);
	}

	/** Group : Filter tests */
	@Nested
	@DisplayName("Group : Filter tests")
	class FilterTests {

		/** Test : Should bypass routes containing /home */
		@Test
		@DisplayName("Test : Should bypass routes containing /home")
		void shouldBypassNonProtectedRoutes() throws Exception {
			when(uriInfo.getPath()).thenReturn("api/home");

			filter.filter(requestContext);

			verify(requestContext, never()).abortWith(org.mockito.ArgumentMatchers.any());
		}

		/** Test : Should reject a request without X-API-Key header */
		@Test
		@DisplayName("Test : Should reject a request without X-API-Key header")
		void shouldRejectMissingAPIKeyHeader() throws Exception {
			when(uriInfo.getPath()).thenReturn("api/obfuscated-translations");
			when(requestContext.getHeaderString("X-API-Key")).thenReturn(null);

			filter.filter(requestContext);

			verify(requestContext).abortWith(org.mockito.ArgumentMatchers
					.argThat(response -> response.getStatus() == ErrorCodes.Unauthorized.get()));
		}

		/** Test : Should reject an empty X-API-Key header */
		@Test
		@DisplayName("Test : Should reject an empty X-API-Key header")
		void shouldRejectEmptyAPIKeyHeader() throws Exception {
			when(uriInfo.getPath()).thenReturn("api/obfuscated-translations");
			when(requestContext.getHeaderString("X-API-Key")).thenReturn("");

			filter.filter(requestContext);

			verify(requestContext).abortWith(org.mockito.ArgumentMatchers
					.argThat(response -> response.getStatus() == ErrorCodes.Unauthorized.get()));
		}

		/** Test : Should reject an invalid API key */
		@Test
		@DisplayName("Test : Should reject an invalid API key")
		void shouldRejectInvalidAPIKey() throws Exception {
			when(uriInfo.getPath()).thenReturn("api/obfuscated-translations");
			when(requestContext.getHeaderString("X-API-Key")).thenReturn("wrong-key");
			when(settings.getString("api.key")).thenReturn(VALID_API_KEY);

			filter.filter(requestContext);

			verify(requestContext).abortWith(org.mockito.ArgumentMatchers
					.argThat(response -> response.getStatus() == ErrorCodes.Unauthorized.get()));
		}

		/** Test : Should accept a valid API key */
		@Test
		@DisplayName("Test : Should accept a valid API key")
		void shouldAcceptValidAPIKey() throws Exception {
			when(uriInfo.getPath()).thenReturn("api/obfuscated-translations");
			when(requestContext.getHeaderString("X-API-Key")).thenReturn(VALID_API_KEY);
			when(settings.getString("api.key")).thenReturn(VALID_API_KEY);

			filter.filter(requestContext);

			verify(requestContext, never()).abortWith(org.mockito.ArgumentMatchers.any());
		}
	}
}
