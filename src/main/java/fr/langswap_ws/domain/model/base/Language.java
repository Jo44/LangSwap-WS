package fr.langswap_ws.domain.model.base;

/** Language enumeration
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public enum Language {

	/** Enumeration */
	Japanese(0), English(1), German(2), French(3);

	/** Value */
	private final Integer value;

	/** Constructor
	 *
	 * @param value */
	Language(Integer value) {
		this.value = value;
	}

	/** Get value
	 * 
	 * @return Integer */
	public Integer getValue() {
		return value;
	}

	/** From value
	 * 
	 * @param value
	 * @return Language */
	public static Language fromValue(Integer value) {
		for (Language language : values()) {
			if (language.value == value) {
				return language;
			}
		}
		throw new IllegalArgumentException("Unsupported language value: " + value);
	}
}
