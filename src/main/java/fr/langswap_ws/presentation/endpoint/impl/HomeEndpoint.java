package fr.langswap_ws.presentation.endpoint.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.langswap_ws.config.Settings;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.presentation.endpoint.inter.IHomeEndpoint;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** Home endpoint
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Singleton
@Path("/home")
public class HomeEndpoint implements IHomeEndpoint {

	private static final Logger LOGGER = LogManager.getLogger(HomeEndpoint.class);

	@Context
	private HttpServletRequest request;
	@Inject
	private Settings settings;

	/** Constructor */
	public HomeEndpoint() {
		super();
	}

	/** Get translations
	 *
	 * @return Response
	 * @throws TechnicalException */
	// Endpoint : /home
	@Override
	public Response getTranslations() throws TechnicalException {
		LOGGER.info("Endpoint --> [GET] Home - /home");
		try {
			// Get API key
			String APIKey = settings.getString("api.key");
			// Get API url
			String APIUrl = buildAPIUrl();
			// Send request
			HttpResponse<String> response = sendRequest(APIUrl, APIKey);
			return Response.status(response.statusCode()).type(MediaType.APPLICATION_JSON)
					.entity(response.body()).build();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage() != null ? ex.getMessage() : "Unexpected error");
			throw new TechnicalException("Unable to load translations for the home page.");
		}
	}

	/** Build API URL
	 *
	 * @return String */
	private String buildAPIUrl() {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);
		if (("http".equalsIgnoreCase(scheme) && serverPort != 80)
				|| ("https".equalsIgnoreCase(scheme) && serverPort != 443)) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath).append("/api/obfuscated-translations");
		return url.toString();
	}

	/** Send HTTP request
	 *
	 * @param APIUrl
	 * @param APIKey
	 * @return HttpResponse<String>
	 * @throws Exception */
	protected HttpResponse<String> sendRequest(String APIUrl, String APIKey) throws Exception {
		HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(APIUrl))
				.header("X-API-Key", APIKey).header("Accept", MediaType.APPLICATION_JSON).GET().build();
		return HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
	}
}
