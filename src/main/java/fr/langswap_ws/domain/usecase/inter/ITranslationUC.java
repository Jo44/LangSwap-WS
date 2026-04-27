package fr.langswap_ws.domain.usecase.inter;

import java.util.List;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.domain.model.dto.TranslationDTO;

/** Translation use-cases interface
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public interface ITranslationUC {

	/** Get all translations
	 *
	 * @return List<TranslationDTO>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	public List<TranslationDTO> getAll() throws FunctionalException, TechnicalException;

	/** Add all translations
	 *
	 * @param translations
	 * @return List<TranslationDTO>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	public List<TranslationDTO> addAll(List<TranslationDTO> translations)
			throws FunctionalException, TechnicalException;
}
