package fr.langswap_ws.domain.model.mapper.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import fr.langswap_ws.domain.model.base.Language;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import fr.langswap_ws.domain.model.entity.Translation;

/** Translation mapper test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Translation mapper test")
class TranslationMapperTest {

	private final TranslationMapper translationMapper = new TranslationMapper();

	/** Group : ToEntity tests */
	@Nested
	@DisplayName("Group : ToEntity tests")
	class ToEntityTests {

		/** Test : Should convert DTO to entity */
		@Test
		@DisplayName("Test : Should convert DTO to entity")
		void shouldConvertDtoToEntity() {
			long creationDate = 1776528985L;
			TranslationDTO dto = createTranslationDTO();
			dto.setCreationDate(creationDate);

			Translation result = translationMapper.toEntity(dto);

			assertNotNull(result);
			assertNull(result.getId());
			assertEquals(dto.getActionId(), result.getActionId());
			assertEquals(dto.getObfuscatedName(), result.getObfuscatedName());
			assertEquals(dto.getDeobfuscatedName(), result.getDeobfuscatedName());
			assertEquals(Language.English, result.getLanguageId());
			assertEquals(dto.getCharacterName(), result.getCharacterName());
			assertEquals(new Timestamp(creationDate * 1000), result.getCreationDate());
		}

		/** Test : Should initialize creation date when missing */
		@Test
		@DisplayName("Test : Should initialize creation date when missing")
		void shouldInitializeCreationDateWhenMissing() {
			TranslationDTO dto = createTranslationDTO();
			dto.setCreationDate(null);

			Translation result = translationMapper.toEntity(dto);

			assertNotNull(result.getCreationDate());
		}

		/** Test : Should return null for null DTO */
		@Test
		@DisplayName("Test : Should return null for null DTO")
		void shouldReturnNullForNullDto() {
			assertNull(translationMapper.toEntity(null));
		}
	}

	/** Group : ToDTO tests */
	@Nested
	@DisplayName("Group : ToDTO tests")
	class ToDTOTests {

		/** Test : Should convert entity to DTO */
		@Test
		@DisplayName("Test : Should convert entity to DTO")
		void shouldConvertEntityToDto() {
			Translation translation = createTranslationEntity();

			TranslationDTO result = translationMapper.toDTO(translation);

			assertNotNull(result);
			assertEquals(translation.getId(), result.getId());
			assertEquals(translation.getActionId(), result.getActionId());
			assertEquals(translation.getObfuscatedName(), result.getObfuscatedName());
			assertEquals(translation.getDeobfuscatedName(), result.getDeobfuscatedName());
			assertEquals(translation.getLanguageId().getValue(), result.getLanguageId());
			assertEquals(translation.getCharacterName(), result.getCharacterName());
			assertEquals(translation.getCreationDate().getTime() / 1000, result.getCreationDate());
		}

		/** Test : Should return null for null entity */
		@Test
		@DisplayName("Test : Should return null for null entity")
		void shouldReturnNullForNullEntity() {
			assertNull(translationMapper.toDTO(null));
		}
	}

	/** Group : ToDTOList tests */
	@Nested
	@DisplayName("Group : ToDTOList tests")
	class ToDTOListTests {

		/** Test : Should convert entity list to DTO list */
		@Test
		@DisplayName("Test : Should convert entity list to DTO list")
		void shouldConvertEntityListToDtoList() {
			List<TranslationDTO> result = translationMapper
					.toDTOList(List.of(createTranslationEntity(), createTranslationEntity()));

			assertNotNull(result);
			assertEquals(2, result.size());
			assertTrue(result.stream().allMatch(dto -> dto.getObfuscatedName().equals("abc_xyz")));
			assertTrue(
					result.stream().allMatch(dto -> dto.getLanguageId().equals(Language.English.getValue())));
		}

		/** Test : Should return empty list for null entity list */
		@Test
		@DisplayName("Test : Should return empty list for null entity list")
		void shouldReturnEmptyListForNullEntityList() {
			List<TranslationDTO> result = translationMapper.toDTOList(null);

			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	/** Create translation DTO
	 * 
	 * @return TranslationDTO */
	private TranslationDTO createTranslationDTO() {
		TranslationDTO dto = new TranslationDTO();
		dto.setId(1);
		dto.setActionId(100);
		dto.setObfuscatedName("abc_xyz");
		dto.setDeobfuscatedName("Deobfuscated Name");
		dto.setLanguageId(Language.English.getValue());
		dto.setCharacterName("Tester");
		return dto;
	}

	/** Create translation entity
	 * 
	 * @return Translation */
	private Translation createTranslationEntity() {
		return new Translation(1, 100, "abc_xyz", "DeobfuscatedName", Language.English, "Tester",
				Timestamp.valueOf("2026-01-01 12:00:00"));
	}
}
