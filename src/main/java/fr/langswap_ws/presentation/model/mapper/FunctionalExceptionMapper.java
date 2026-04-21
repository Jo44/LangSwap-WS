package fr.langswap_ws.presentation.model.mapper;

import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorCodes;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Functional exception mapper
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Provider
public class FunctionalExceptionMapper implements ExceptionMapper<FunctionalException> {

	/** Convert exception to HTTP response
	 *
	 * @param fex
	 * @return Response */
	@Override
	public Response toResponse(FunctionalException fex) {
		return Response.status(ErrorCodes.PreconditionFailed.get())
				.entity(new ErrorResponse("FUNCTIONAL_ERROR", fex.getMessage()))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
