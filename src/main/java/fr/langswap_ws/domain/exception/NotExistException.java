package fr.langswap_ws.domain.exception;

/** Functional exception - Translation does not exist
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public class NotExistException extends FunctionalException {

	private static final long serialVersionUID = 930448801449184469L;

	/** Constructor
	 *
	 * @param message */
	public NotExistException(String message) {
		super(message);
	}
}
