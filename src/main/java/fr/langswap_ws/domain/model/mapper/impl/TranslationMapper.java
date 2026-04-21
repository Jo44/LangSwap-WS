package fr.langswap_ws.domain.model.mapper.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.langswap_ws.domain.model.base.Language;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import fr.langswap_ws.domain.model.entity.Translation;
import fr.langswap_ws.domain.model.mapper.api.ITranslationMapper;
import jakarta.inject.Singleton;

/** Translation mapper
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Singleton
public class TranslationMapper implements ITranslationMapper {

	private static final Logger LOGGER = LogManager.getLogger(TranslationMapper.class);

	/** Constructor */
	public TranslationMapper() {
		super();
	}

	/** Convert TranslationDTO to Translation
	 *
	 * @param translationDTO
	 * @return Translation */
	@Override
	public Translation toEntity(TranslationDTO translationDTO) {
		if (translationDTO == null) {
			return null;
		}
		LOGGER.debug("Convert DTO to translation");
		Translation translation = new Translation();
		translation.setId(null);
		translation.setActionId(translationDTO.getActionId());
		translation.setObfuscatedName(translationDTO.getObfuscatedName());
		translation.setDeobfuscatedName(translationDTO.getDeobfuscatedName());
		translation.setLanguageId(Language.fromValue(translationDTO.getLanguageId()));
		translation.setCharacterName(translationDTO.getCharacterName());
		translation.setCreationDate(translationDTO.getCreationDate() != null
				? new Timestamp(translationDTO.getCreationDate() * 1000)
				: new Timestamp(System.currentTimeMillis()));
		return translation;
	}

	/** Convert Translation to TranslationDTO
	 *
	 * @param translation
	 * @return TranslationDTO */
	@Override
	public TranslationDTO toDTO(Translation translation) {
		if (translation == null) {
			return null;
		}
		LOGGER.debug("Convert translation to DTO");
		TranslationDTO translationDTO = new TranslationDTO();
		translationDTO.setId(translation.getId());
		translationDTO.setActionId(translation.getActionId());
		translationDTO.setObfuscatedName(translation.getObfuscatedName());
		translationDTO.setDeobfuscatedName(translation.getDeobfuscatedName());
		translationDTO.setLanguageId(translation.getLanguageId().getValue());
		translationDTO.setCharacterName(translation.getCharacterName());
		translationDTO.setCreationDate(translation.getCreationDate() != null
				? translation.getCreationDate().getTime() / 1000
				: null);
		return translationDTO;
	}

	/** Convert Translation list to TranslationDTO list
	 *
	 * @param translations
	 * @return List<TranslationDTO> */
	@Override
	public List<TranslationDTO> toDTOList(List<Translation> translations) {
		if (translations == null) {
			return new ArrayList<>();
		}
		LOGGER.debug("Convert translation list to DTO");
		return translations.stream().map(translation -> toDTO(translation))
				.collect(Collectors.toList());
	}
}
