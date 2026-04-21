package fr.langswap_ws.presentation.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import fr.langswap_ws.domain.model.base.Language;
import fr.langswap_ws.domain.model.dto.TranslationDTO;

/** Request validator test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Request validator test")
class RequestValidatorTest {

	private RequestValidator validator;

	/** Set up */
	@BeforeEach
	void setUp() {
		validator = new RequestValidator();
	}

	/** Group : Translation validation tests */
	@Nested
	@DisplayName("Group : Translation validation tests")
	class TranslationValidationTests {

		/** Test : Should validate a correct list of translations */
		@Test
		@DisplayName("Test : Should validate a correct list of translations")
		void shouldValidateTranslations() {
			Boolean result = validator.checkAllTranslations(List.of(createValidTranslation()));

			assertTrue(result);
		}

		/** Test : Should reject null list */
		@Test
		@DisplayName("Test : Should reject null list")
		void shouldRejectNullList() {
			Boolean result = validator.checkAllTranslations(null);

			assertFalse(result);
		}

		/** Test : Should reject empty list */
		@Test
		@DisplayName("Test : Should reject empty list")
		void shouldRejectEmptyList() {
			Boolean result = validator.checkAllTranslations(List.of());

			assertFalse(result);
		}

		/** Test : Should reject null action ID */
		@Test
		@DisplayName("Test : Should reject null action ID")
		void shouldRejectNullActionID() {
			TranslationDTO translation = createValidTranslation();
			translation.setActionId(null);

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject invalid action ID */
		@Test
		@DisplayName("Test : Should reject invalid action ID")
		void shouldRejectInvalidActionID() {
			TranslationDTO translation = createValidTranslation();
			translation.setActionId(0);

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject null obfuscated name */
		@Test
		@DisplayName("Test : Should reject null obfuscated name")
		void shouldRejectNullObfuscatedName() {
			TranslationDTO translation = createValidTranslation();
			translation.setObfuscatedName(null);

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject empty obfuscated name */
		@Test
		@DisplayName("Test : Should reject empty obfuscated name")
		void shouldRejectEmptyObfuscatedName() {
			TranslationDTO translation = createValidTranslation();
			translation.setObfuscatedName("");

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject null deobfuscated name */
		@Test
		@DisplayName("Test : Should reject null deobfuscated name")
		void shouldRejectNullDeobfuscatedName() {
			TranslationDTO translation = createValidTranslation();
			translation.setDeobfuscatedName(null);

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject empty deobfuscated name */
		@Test
		@DisplayName("Test : Should reject empty deobfuscated name")
		void shouldRejectEmptyDeobfuscatedName() {
			TranslationDTO translation = createValidTranslation();
			translation.setDeobfuscatedName("");

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject null language ID */
		@Test
		@DisplayName("Test : Should reject null language ID")
		void shouldRejectNullLanguageID() {
			TranslationDTO translation = createValidTranslation();
			translation.setLanguageId(null);

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject invalid language ID */
		@Test
		@DisplayName("Test : Should reject invalid language ID")
		void shouldRejectInvalidLanguageID() {
			TranslationDTO translation = createValidTranslation();
			translation.setLanguageId(4);

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject null character name */
		@Test
		@DisplayName("Test : Should reject null character name")
		void shouldRejectNullCharacterName() {
			TranslationDTO translation = createValidTranslation();
			translation.setCharacterName(null);

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}

		/** Test : Should reject empty character name */
		@Test
		@DisplayName("Test : Should reject empty character name")
		void shouldRejectEmptyCharacterName() {
			TranslationDTO translation = createValidTranslation();
			translation.setCharacterName("");

			Boolean result = validator.checkAllTranslations(List.of(translation));

			assertFalse(result);
		}
	}

	/** Create valid translation
	 * 
	 * @return TranslationDTO */
	private TranslationDTO createValidTranslation() {
		TranslationDTO translation = new TranslationDTO();
		translation.setId(1);
		translation.setActionId(100);
		translation.setObfuscatedName("abc_xyz");
		translation.setDeobfuscatedName("xyz_abc");
		translation.setLanguageId(Language.English.getValue());
		translation.setCharacterName("Tester");
		return translation;
	}
}
