package fr.langswap_ws.presentation.model.mapper;

import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorCodes;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Technical exception mapper
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Provider
public class TechnicalExceptionMapper implements ExceptionMapper<TechnicalException> {

	/** Convert exception to HTTP response
	 *
	 * @param tex
	 * @return Response */
	@Override
	public Response toResponse(TechnicalException tex) {
		return Response.status(ErrorCodes.ServiceUnavailable.get())
				.entity(new ErrorResponse("TECHNICAL_ERROR", tex.getMessage()))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
