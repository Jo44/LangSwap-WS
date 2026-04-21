package fr.langswap_ws.presentation.model.mapper;

import fr.langswap_ws.presentation.exception.InvalidRequestException;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorCodes;
import fr.langswap_ws.presentation.model.miscellaneous.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Invalid request exception mapper
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Provider
public class InvalidRequestExceptionMapper implements ExceptionMapper<InvalidRequestException> {

	/** Convert exception to HTTP response
	 *
	 * @param irex
	 * @return Response */
	@Override
	public Response toResponse(InvalidRequestException irex) {
		return Response.status(ErrorCodes.PreconditionFailed.get())
				.entity(new ErrorResponse("INVALID_REQUEST", irex.getMessage()))
				.type(MediaType.APPLICATION_JSON).build();
	}
}
