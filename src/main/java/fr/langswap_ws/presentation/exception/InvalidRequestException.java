package fr.langswap_ws.presentation.exception;

import fr.langswap_ws.domain.exception.FunctionalException;

/** Functional exception - Invalid request
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public class InvalidRequestException extends FunctionalException {

	private static final long serialVersionUID = 930448801449184469L;

	/** Constructor
	 *
	 * @param message */
	public InvalidRequestException(String message) {
		super(message);
	}
}
