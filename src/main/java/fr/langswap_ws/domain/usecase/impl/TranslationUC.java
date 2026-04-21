package fr.langswap_ws.domain.usecase.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.langswap_ws.data.repository.api.ITranslationRepo;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.NotExistException;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import fr.langswap_ws.domain.model.entity.Translation;
import fr.langswap_ws.domain.model.mapper.api.ITranslationMapper;
import fr.langswap_ws.domain.usecase.api.ITranslationUC;
import fr.langswap_ws.presentation.exception.InvalidRequestException;
import fr.langswap_ws.presentation.validator.RequestValidator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/** Translation use-cases
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Singleton
public class TranslationUC implements ITranslationUC {

	private static final Logger LOGGER = LogManager.getLogger(TranslationUC.class);
	@Inject
	private RequestValidator validator;
	@Inject
	private ITranslationRepo translationRepo;
	@Inject
	private ITranslationMapper translationMapper;

	/** Constructor */
	public TranslationUC() {
		super();
	}

	/** Get all translations
	 *
	 * @return List<TranslationDTO>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	@Override
	public List<TranslationDTO> getAll() throws FunctionalException, TechnicalException {
		LOGGER.debug("Retrieving all translations.");
		// Get all translations
		List<Translation> translations = translationRepo.getAll();
		// Convert to DTO
		return translationMapper.toDTOList(translations);
	}

	/** Add all translations
	 *
	 * @param translations
	 * @return List<TranslationDTO>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	@Override
	public List<TranslationDTO> addAll(List<TranslationDTO> translations)
			throws FunctionalException, TechnicalException {
		LOGGER.debug("Adding all translations.");
		// Check translations
		if (!validator.checkAllTranslations(translations)) {
			throw new InvalidRequestException("Translation list is invalid.");
		}
		// Initialize return list
		List<Translation> addedTranslations = new ArrayList<Translation>();
		// Add each translation
		for (TranslationDTO translationDTO : translations) {
			// Convert DTO to translation
			Translation translation = translationMapper.toEntity(translationDTO);
			try {
				// Check if translation already exists
				translationRepo.getOne(translation.getActionId(), translation.getObfuscatedName());
				LOGGER.debug("Translation already exists.");
			} catch (NotExistException nee) {
				// Add new translation
				Translation addedTranslation = translationRepo.add(translation);
				addedTranslations.add(addedTranslation);
				LOGGER.debug("Translation added.");
			}
		}
		// Convert to DTO
		return translationMapper.toDTOList(addedTranslations);
	}
}
