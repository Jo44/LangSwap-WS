package fr.langswap_ws.domain.model.mapper.inter;

import java.util.List;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import fr.langswap_ws.domain.model.entity.Translation;

/** Translation mapper interface
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public interface ITranslationMapper {

	/** Convert TranslationDTO to Translation
	 *
	 * @param translationDTO
	 * @return Translation */
	public Translation toEntity(TranslationDTO translationDTO);

	/** Convert Translation to TranslationDTO
	 *
	 * @param translation
	 * @return TranslationDTO */
	public TranslationDTO toDTO(Translation translation);

	/** Convert Translation list to TranslationDTO list
	 *
	 * @param translations
	 * @return List<TranslationDTO> */
	public List<TranslationDTO> toDTOList(List<Translation> translations);
}
