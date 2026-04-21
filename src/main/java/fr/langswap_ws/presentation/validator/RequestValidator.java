package fr.langswap_ws.presentation.validator;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import jakarta.inject.Singleton;

/** Request validator
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Singleton
public class RequestValidator {

	private static final Logger LOGGER = LogManager.getLogger(RequestValidator.class);

	/** Constructor */
	public RequestValidator() {
		super();
	}

	/** Check all translations
	 *
	 * @param translations
	 * @return Boolean */
	public Boolean checkAllTranslations(List<TranslationDTO> translations) {
		LOGGER.debug("Validating received translations.");
		boolean valid = true;
		if (translations == null || translations.size() < 1) {
			LOGGER.error("Translation list is null or empty.");
			valid = false;
		} else {
			for (TranslationDTO translation : translations) {
				// Action ID must be between 1 and 100000
				if (translation.getActionId() == null || translation.getActionId() < 1
						|| translation.getActionId() > 100000) {
					LOGGER.error("Action ID is invalid.");
					valid = false;
				}
				// Obfuscated name must contain between 1 and 255 characters
				if (translation.getObfuscatedName() == null || translation.getObfuscatedName().isEmpty()
						|| translation.getObfuscatedName().length() > 255) {
					LOGGER.error("Obfuscated name is invalid.");
					valid = false;
				}
				// Deobfuscated name must contain between 1 and 255 characters
				if (translation.getDeobfuscatedName() == null || translation.getDeobfuscatedName().isEmpty()
						|| translation.getDeobfuscatedName().length() > 255) {
					LOGGER.error("Deobfuscated name is invalid.");
					valid = false;
				}
				// Language ID must be between 0 and 3
				if (translation.getLanguageId() == null || translation.getLanguageId() < 0
						|| translation.getLanguageId() > 3) {
					LOGGER.error("Language ID is invalid.");
					valid = false;
				}
				// Character name must contain between 1 and 255 characters
				if (translation.getCharacterName() == null || translation.getCharacterName().isEmpty()
						|| translation.getCharacterName().length() > 255) {
					LOGGER.error("Character name is invalid.");
					valid = false;
				}
			}
		}
		if (valid) {
			LOGGER.debug("Translation list is valid.");
		} else {
			LOGGER.error("Translation list is invalid.");
		}
		return valid;
	}
}
