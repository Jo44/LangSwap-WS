package fr.langswap_ws.presentation.model.miscellaneous;

/** Error codes enumeration
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public enum ErrorCodes {
	Unauthorized(401), PreconditionFailed(412), ServiceUnavailable(503);

	private final int value;

	/** Constructor
	 *
	 * @param value */
	ErrorCodes(int value) {
		this.value = value;
	}

	/** To String
	 *
	 * @return String */
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	/* Getter */

	public int get() {
		return value;
	}
}
