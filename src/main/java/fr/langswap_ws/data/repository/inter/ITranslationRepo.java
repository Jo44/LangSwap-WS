package fr.langswap_ws.data.repository.inter;

import java.util.List;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.domain.model.entity.Translation;

/** Translation repository interface
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public interface ITranslationRepo {

	/** Get all translations
	 *
	 * @return List<Translation>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	public List<Translation> getAll() throws FunctionalException, TechnicalException;

	/** Get one translation
	 *
	 * @param actionId
	 * @param obfuscatedName
	 * @return Translation
	 * @throws FunctionalException
	 * @throws TechnicalException */
	public Translation getOne(Integer actionId, String obfuscatedName)
			throws FunctionalException, TechnicalException;

	/** Add translation
	 *
	 * @param translation
	 * @return Translation
	 * @throws FunctionalException
	 * @throws TechnicalException */
	public Translation add(Translation translation) throws FunctionalException, TechnicalException;
}
