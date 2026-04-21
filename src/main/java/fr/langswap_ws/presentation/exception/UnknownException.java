package fr.langswap_ws.presentation.exception;

import fr.langswap_ws.domain.exception.TechnicalException;

/** Technical exception - Unknown exception
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public class UnknownException extends TechnicalException {

	private static final long serialVersionUID = 930448801449184469L;

	/** Constructor */
	public UnknownException() {
		super("An internal error occurred :/");
	}
}
