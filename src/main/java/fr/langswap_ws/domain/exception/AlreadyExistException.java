package fr.langswap_ws.domain.exception;

/** Functional exception - Translation already exists
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public class AlreadyExistException extends FunctionalException {

	private static final long serialVersionUID = 930448801449184469L;

	/** Constructor
	 *
	 * @param message */
	public AlreadyExistException(String message) {
		super(message);
	}
}
