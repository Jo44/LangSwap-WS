package fr.langswap_ws.presentation.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.langswap_ws.config.Settings;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorCodes;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/** Authentication filter
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LogManager.getLogger(AuthenticationFilter.class);

	@Inject
	private Settings settings;

	/** Filter
	 *
	 * @param requestContext */
	@Override
	public void filter(ContainerRequestContext requestContext) {
		// Exclude home page
		String path = requestContext.getUriInfo().getPath();
		if (path.contains("home")) {
			LOGGER.debug("Home request. Request authorized.");
			return;
		}
		// Check API key in header
		String headerAPIKey = requestContext.getHeaderString("X-API-Key");
		if (headerAPIKey == null || headerAPIKey.isEmpty()) {
			LOGGER.error("Missing or empty X-API-Key header. Request unauthorized.");
			// Unauthorized request => Error code 401
			requestContext.abortWith(Response.status(ErrorCodes.Unauthorized.get()).build());
			return;
		}
		// Get API key from config
		String configAPIKey = settings.getString("api.key");
		// Compare keys
		if (headerAPIKey.equals(configAPIKey)) {
			LOGGER.debug("API key validated. Request authorized.");
			return;
		} else {
			LOGGER.error("Invalid API key. Request unauthorized.");
			// Unauthorized request => Error code 401
			requestContext.abortWith(Response.status(ErrorCodes.Unauthorized.get()).build());
			return;
		}
	}
}
