package fr.langswap_ws.domain.model.base;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Language converter
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Converter(autoApply = false)
public class LanguageConverter implements AttributeConverter<Language, Integer> {

	/** Convert to database column
	 * 
	 * @param language
	 * @return Integer */
	@Override
	public Integer convertToDatabaseColumn(Language language) {
		return language != null ? language.getValue() : null;
	}

	/** Convert to entity attribute
	 * 
	 * @param value
	 * @return Language */
	@Override
	public Language convertToEntityAttribute(Integer value) {
		return value != null ? Language.fromValue(value) : null;
	}
}
