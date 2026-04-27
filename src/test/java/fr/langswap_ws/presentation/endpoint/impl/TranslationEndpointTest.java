package fr.langswap_ws.presentation.endpoint.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.domain.model.base.Language;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import fr.langswap_ws.domain.usecase.inter.ITranslationUC;

/** Translation endpoint test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Translation endpoint test")
@ExtendWith(MockitoExtension.class)
class TranslationEndpointTest {

	/** Mock */
	@InjectMocks
	private TranslationEndpoint translationEndpoint;
	@Mock
	private ITranslationUC translationUC;

	/** Group : Retrieval tests */
	@Nested
	@DisplayName("Group : Retrieval tests")
	class GetTranslationsTests {

		/** Test : Should retrieve all translations successfully */
		@Test
		@DisplayName("Test : Should retrieve all translations successfully")
		void shouldGetAllTranslationsSuccessfully() throws FunctionalException, TechnicalException {
			List<TranslationDTO> expectedTranslations = List.of(createTranslationDTO(1),
					createTranslationDTO(2));
			when(translationUC.getAll()).thenReturn(expectedTranslations);

			List<TranslationDTO> response = translationEndpoint.getTranslations();

			assertNotNull(response);
			assertEquals(2, response.size());
			verify(translationUC).getAll();
		}

		/** Test : Should convert unknown error to technical error */
		@Test
		@DisplayName("Test : Should convert unknown error to technical error")
		void shouldWrapUnknownExceptionOnGetAll() throws FunctionalException, TechnicalException {
			when(translationUC.getAll()).thenThrow(new RuntimeException("boom"));

			TechnicalException exception = assertThrows(TechnicalException.class,
					() -> translationEndpoint.getTranslations());

			assertNotNull(exception.getMessage());
		}
	}

	/** Group : Addition tests */
	@Nested
	@DisplayName("Group : Addition tests")
	class AddTranslationsTests {

		/** Test : Should add a valid list of translations */
		@Test
		@DisplayName("Test : Should add a valid list of translations")
		void shouldAddTranslationsSuccessfully() throws FunctionalException, TechnicalException {
			List<TranslationDTO> translations = List.of(createTranslationDTO(1));
			when(translationUC.addAll(translations)).thenReturn(translations);

			List<TranslationDTO> response = translationEndpoint.addTranslations(translations);

			assertNotNull(response);
			assertEquals(1, response.size());
			verify(translationUC).addAll(translations);
		}

		/** Test : Should reject invalid translation list from UseCase */
		@Test
		@DisplayName("Test : Should reject invalid translation list from UseCase")
		void shouldRejectInvalidTranslations() throws FunctionalException, TechnicalException {
			List<TranslationDTO> translations = List.of(createTranslationDTO(1));
			when(translationUC.addAll(translations))
					.thenThrow(new FunctionalException("Invalid translations"));

			FunctionalException exception = assertThrows(FunctionalException.class,
					() -> translationEndpoint.addTranslations(translations));

			assertNotNull(exception.getMessage());
			verify(translationUC).addAll(translations);
		}

		/** Test : Should convert technical error to TechnicalException */
		@Test
		@DisplayName("Test : Should convert technical error to TechnicalException")
		void shouldWrapTechnicalExceptionOnAddAll() throws FunctionalException, TechnicalException {
			List<TranslationDTO> translations = List.of(createTranslationDTO(1));
			when(translationUC.addAll(translations)).thenThrow(new TechnicalException("db error"));

			TechnicalException exception = assertThrows(TechnicalException.class,
					() -> translationEndpoint.addTranslations(translations));

			assertNotNull(exception.getMessage());
			verify(translationUC).addAll(translations);
		}
	}

	/** Create translation DTO
	 * 
	 * @param id
	 * @return TranslationDTO */
	private TranslationDTO createTranslationDTO(Integer id) {
		TranslationDTO translation = new TranslationDTO();
		translation.setId(id);
		translation.setActionId(100 + id);
		translation.setObfuscatedName("name_" + id);
		translation.setDeobfuscatedName("Deobfuscated Name");
		translation.setLanguageId(Language.English.getValue());
		translation.setCharacterName("Tester");
		return translation;
	}
}
