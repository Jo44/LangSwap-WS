package fr.langswap_ws.domain.usecase.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.langswap_ws.data.repository.inter.ITranslationRepo;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.NotExistException;
import fr.langswap_ws.domain.model.base.Language;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import fr.langswap_ws.domain.model.entity.Translation;
import fr.langswap_ws.domain.model.mapper.inter.ITranslationMapper;
import fr.langswap_ws.presentation.validator.RequestValidator;

/** Translation use-cases test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Translation use-cases test")
@ExtendWith(MockitoExtension.class)
class TranslationUCTest {

	/** Mock */
	@Mock
	private ITranslationRepo translationRepo;
	@Mock
	private ITranslationMapper translationMapper;
	@Mock
	private RequestValidator requestValidator;

	@InjectMocks
	private TranslationUC translationUC;

	/** Group : GetAll tests */
	@Nested
	@DisplayName("Group : GetAll tests")
	class GetAllTests {

		/** Test : GetAll returns a list of translations */
		@Test
		@DisplayName("Test : GetAll returns a list of translations")
		void testGetAllReturnsTranslations() throws Exception {
			List<Translation> entities = new ArrayList<>();
			entities.add(createTranslation(1, "action1", "action001", Language.English));

			List<TranslationDTO> dtos = new ArrayList<>();
			dtos.add(createTranslationDTO(1, "action1", "action001", Language.English.getValue()));

			when(translationRepo.getAll()).thenReturn(entities);
			when(translationMapper.toDTOList(entities)).thenReturn(dtos);

			List<TranslationDTO> result = translationUC.getAll();

			assertNotNull(result);
			assertEquals(1, result.size());
		}

		/** Test : GetAll returns an empty list */
		@Test
		@DisplayName("Test : GetAll returns an empty list")
		void testGetAllReturnsEmpty() throws Exception {
			when(translationRepo.getAll()).thenReturn(Collections.emptyList());
			when(translationMapper.toDTOList(Collections.emptyList()))
					.thenReturn(Collections.emptyList());

			List<TranslationDTO> result = translationUC.getAll();

			assertNotNull(result);
			assertEquals(0, result.size());
		}
	}

	/** Group : AddAll tests */
	@Nested
	@DisplayName("Group : AddAll tests")
	class AddAllTests {

		/** Scenario 1 : Malformed input */
		@Nested
		@DisplayName("Scenario 1 : Malformed input")
		class MalformedInputTests {

			/** Test : Rejects null list */
			@Test
			@DisplayName("Test : Rejects null list")
			void testAddAllRejectsNullList() throws Exception {
				when(requestValidator.checkAllTranslations(null)).thenReturn(false);
				try {
					translationUC.addAll(null);
					fail("Should throw FunctionalException");
				} catch (FunctionalException fex) {
					// Expected behavior
				}
			}

			/** Test : Rejects empty list */
			@Test
			@DisplayName("Test : Rejects empty list")
			void testAddAllRejectsEmptyList() throws Exception {
				when(requestValidator.checkAllTranslations(Collections.emptyList())).thenReturn(false);
				try {
					translationUC.addAll(Collections.emptyList());
					fail("Should throw FunctionalException");
				} catch (FunctionalException fex) {
					// Expected behavior
				}
			}
		}

		/** Scenario 2 : All new translations */
		@Nested
		@DisplayName("Scenario 2 : All new translations")
		class AllNewTranslationsTests {

			/** Test : Adds all new translations */
			@Test
			@DisplayName("Test : Adds all new translations")
			void testAddAllNewTranslations() throws Exception {
				TranslationDTO dto = createTranslationDTO(1, "new1", "Nouvelle",
						Language.French.getValue());
				List<TranslationDTO> dtoList = new ArrayList<>();
				dtoList.add(dto);

				when(requestValidator.checkAllTranslations(dtoList)).thenReturn(true);
				when(translationRepo.getOne(1, "new1")).thenThrow(NotExistException.class);
				when(translationMapper.toEntity(dto))
						.thenReturn(createTranslation(1, "new1", "Nouvelle", Language.French));
				when(translationMapper.toDTOList(anyList())).thenReturn(dtoList);

				List<TranslationDTO> result = translationUC.addAll(dtoList);

				assertNotNull(result);
				assertEquals(1, result.size());
				verify(translationRepo, times(1)).add(any());
			}

			/** Test : Adds with accents */
			@Test
			@DisplayName("Test : Adds with accents")
			void testAddNewWithAccents() throws Exception {
				TranslationDTO dto = createTranslationDTO(10, "accent", "Café et croißant",
						Language.French.getValue());
				List<TranslationDTO> dtoList = new ArrayList<>();
				dtoList.add(dto);

				when(requestValidator.checkAllTranslations(dtoList)).thenReturn(true);
				when(translationRepo.getOne(10, "accent")).thenThrow(NotExistException.class);
				when(translationMapper.toEntity(dto))
						.thenReturn(createTranslation(10, "accent", "Café et croißant", Language.French));
				when(translationMapper.toDTOList(anyList())).thenReturn(dtoList);

				List<TranslationDTO> result = translationUC.addAll(dtoList);

				assertNotNull(result);
				assertEquals(1, result.size());
			}

			/** Test : Adds with Japanese characters */
			@Test
			@DisplayName("Test : Adds with Japanese characters")
			void testAddNewWithJapanese() throws Exception {
				TranslationDTO dto = createTranslationDTO(11, "jp", "日本語", Language.Japanese.getValue());
				List<TranslationDTO> dtoList = new ArrayList<>();
				dtoList.add(dto);

				when(requestValidator.checkAllTranslations(dtoList)).thenReturn(true);
				when(translationRepo.getOne(11, "jp")).thenThrow(NotExistException.class);
				when(translationMapper.toEntity(dto))
						.thenReturn(createTranslation(11, "jp", "日本語", Language.Japanese));
				when(translationMapper.toDTOList(anyList())).thenReturn(dtoList);

				List<TranslationDTO> result = translationUC.addAll(dtoList);

				assertNotNull(result);
				assertEquals(1, result.size());
			}
		}

		/** Scenario 3 : Existing translations */
		@Nested
		@DisplayName("Scenario 3 : Existing translations")
		class ExistingTranslationsTests {

			/** Test : Ignores existing translations */
			@Test
			@DisplayName("Test : Ignores existing translations")
			void testAddAllExistingTranslations() throws Exception {
				TranslationDTO dto = createTranslationDTO(1, "existing1", "Francais",
						Language.French.getValue());
				List<TranslationDTO> dtoList = new ArrayList<>();
				dtoList.add(dto);

				when(requestValidator.checkAllTranslations(dtoList)).thenReturn(true);
				when(translationRepo.getOne(1, "existing1"))
						.thenReturn(createTranslation(1, "existing1", "Francais", Language.French));
				when(translationMapper.toEntity(dto))
						.thenReturn(createTranslation(1, "existing1", "Francais", Language.French));
				when(translationMapper.toDTOList(anyList())).thenReturn(Collections.emptyList());

				List<TranslationDTO> result = translationUC.addAll(dtoList);

				assertNotNull(result);
				assertEquals(0, result.size());
				verify(translationRepo, never()).add(any());
			}

			/** Test : Ignores existing translations with special characters */
			@Test
			@DisplayName("Test : Ignores existing translations with special characters")
			void testAddExistingWithSpecialChars() throws Exception {
				TranslationDTO dto = createTranslationDTO(3, "special", "カフェ",
						Language.Japanese.getValue());
				List<TranslationDTO> dtoList = new ArrayList<>();
				dtoList.add(dto);

				when(requestValidator.checkAllTranslations(dtoList)).thenReturn(true);
				when(translationRepo.getOne(3, "special"))
						.thenReturn(createTranslation(3, "special", "カフェ", Language.Japanese));
				when(translationMapper.toEntity(dto))
						.thenReturn(createTranslation(3, "special", "カフェ", Language.Japanese));
				when(translationMapper.toDTOList(anyList())).thenReturn(Collections.emptyList());

				List<TranslationDTO> result = translationUC.addAll(dtoList);

				assertNotNull(result);
				assertEquals(0, result.size());
				verify(translationRepo, never()).add(any());
			}
		}

		/** Scenario 4 : Mixed scenarios */
		@Nested
		@DisplayName("Scenario 4 : Mixed scenarios")
		class MixedScenarioTests {

			/** Test : Mix of new and existing translations */
			@Test
			@DisplayName("Test : Mix of new and existing translations")
			void testAddAllMixed() throws Exception {
				TranslationDTO dtoNew = createTranslationDTO(10, "new", "NewFr",
						Language.French.getValue());
				TranslationDTO dtoExist = createTranslationDTO(20, "exist", "ExEn",
						Language.English.getValue());
				List<TranslationDTO> dtoList = new ArrayList<>();
				dtoList.add(dtoNew);
				dtoList.add(dtoExist);
				List<TranslationDTO> addedDtos = List.of(dtoNew);

				Translation existComplete = createTranslation(20, "exist", "ExEn", Language.English);

				when(requestValidator.checkAllTranslations(dtoList)).thenReturn(true);
				when(translationRepo.getOne(10, "new")).thenThrow(NotExistException.class);
				when(translationRepo.getOne(20, "exist")).thenReturn(existComplete);

				when(translationMapper.toEntity(dtoNew))
						.thenReturn(createTranslation(10, "new", "NewFr", Language.French));
				when(translationMapper.toEntity(dtoExist)).thenReturn(existComplete);
				when(translationMapper.toDTOList(anyList())).thenReturn(addedDtos);

				List<TranslationDTO> result = translationUC.addAll(dtoList);

				assertNotNull(result);
				assertEquals(1, result.size());
				verify(translationRepo, times(1)).add(any());
			}

			/** Test : Mix with special characters */
			@Test
			@DisplayName("Test : Mix with special characters")
			void testAddAllMixedWithSpecialChars() throws Exception {
				TranslationDTO dtoNew = createTranslationDTO(100, "new_sp", "Élève ß Lycée",
						Language.French.getValue());
				TranslationDTO dtoExist = createTranslationDTO(101, "exist_jp", "既存の",
						Language.Japanese.getValue());
				List<TranslationDTO> dtoList = new ArrayList<>();
				dtoList.add(dtoNew);
				dtoList.add(dtoExist);
				List<TranslationDTO> addedDtos = List.of(dtoNew);

				Translation existComplete = createTranslation(101, "exist_jp", "既存の", Language.Japanese);

				when(requestValidator.checkAllTranslations(dtoList)).thenReturn(true);
				when(translationRepo.getOne(100, "new_sp")).thenThrow(NotExistException.class);
				when(translationRepo.getOne(101, "exist_jp")).thenReturn(existComplete);

				when(translationMapper.toEntity(dtoNew))
						.thenReturn(createTranslation(100, "new_sp", "Élève ß Lycée", Language.French));
				when(translationMapper.toEntity(dtoExist)).thenReturn(existComplete);
				when(translationMapper.toDTOList(anyList())).thenReturn(addedDtos);

				List<TranslationDTO> result = translationUC.addAll(dtoList);

				assertNotNull(result);
				assertEquals(1, result.size());
				verify(translationRepo, times(1)).add(any());
			}
		}
	}

	/** Create translation
	 * 
	 * @param actionId
	 * @param obfuscatedName
	 * @param deobfuscatedName
	 * @param language
	 * @return Translation */
	private Translation createTranslation(Integer actionId, String obfuscatedName,
			String deobfuscatedName, Language language) {
		Translation entity = new Translation();
		entity.setActionId(actionId);
		entity.setObfuscatedName(obfuscatedName);
		entity.setDeobfuscatedName(deobfuscatedName);
		entity.setLanguageId(language);
		entity.setCharacterName("Tester");
		return entity;
	}

	/** Create translation DTO
	 * 
	 * @param actionId
	 * @param obfuscatedName
	 * @param deobfuscatedName
	 * @param languageId
	 * @return TranslationDTO */
	private TranslationDTO createTranslationDTO(Integer actionId, String obfuscatedName,
			String deobfuscatedName, Integer languageId) {
		TranslationDTO dto = new TranslationDTO();
		dto.setActionId(actionId);
		dto.setObfuscatedName(obfuscatedName);
		dto.setDeobfuscatedName(deobfuscatedName);
		dto.setLanguageId(languageId);
		dto.setCharacterName("Tester");
		return dto;
	}
}
