package fr.langswap_ws.presentation.model.miscellaneous;

import java.sql.Timestamp;

/** Error response model
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public class ErrorResponse {

	private String code;
	private String message;
	private Timestamp timestamp;

	/** Constructor */
	public ErrorResponse() {
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	/** Constructor
	 *
	 * @param code
	 * @param message */
	public ErrorResponse(String code, String message) {
		this();
		this.code = code;
		this.message = message;
	}

	/* Getters */

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
