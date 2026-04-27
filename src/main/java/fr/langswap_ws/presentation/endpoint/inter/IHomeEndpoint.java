package fr.langswap_ws.presentation.endpoint.inter;

import fr.langswap_ws.domain.exception.TechnicalException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** Home endpoint interface
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Produces(MediaType.APPLICATION_JSON)
public interface IHomeEndpoint {

	/** Get translations
	 *
	 * @return Response
	 * @throws TechnicalException */
	// Endpoint : /home
	@GET
	public Response getTranslations() throws TechnicalException;
}
