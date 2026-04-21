package fr.langswap_ws.presentation.model.mapper;

import fr.langswap_ws.presentation.exception.UnknownException;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorCodes;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Unknown exception mapper
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Provider
public class UnknownExceptionMapper implements ExceptionMapper<UnknownException> {

	/** Convert exception to HTTP response
	 *
	 * @param uex
	 * @return Response */
	@Override
	public Response toResponse(UnknownException uex) {
		return Response.status(ErrorCodes.ServiceUnavailable.get())
				.entity(new ErrorResponse("UNKNOWN_ERROR", uex.getMessage()))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
