package fr.langswap_ws.domain.exception;

/** Functional exception
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public class FunctionalException extends Exception {

	private static final long serialVersionUID = 930448801449184469L;
	private String message;

	/** Constructor
	 *
	 * @param message */
	public FunctionalException(String message) {
		this.message = message;
	}

	/* Getter / Setter */

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
