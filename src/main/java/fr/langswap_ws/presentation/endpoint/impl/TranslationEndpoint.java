package fr.langswap_ws.presentation.endpoint.impl;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import fr.langswap_ws.domain.usecase.api.ITranslationUC;
import fr.langswap_ws.presentation.endpoint.api.ITranslationEndpoint;
import fr.langswap_ws.presentation.exception.UnknownException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

/** Translation endpoint
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Singleton
@Path("/obfuscated-translations")
public class TranslationEndpoint implements ITranslationEndpoint {

	private static final Logger LOGGER = LogManager.getLogger(TranslationEndpoint.class);

	@Context
	private HttpServletRequest request;
	@Inject
	private ITranslationUC translationUC;

	/** Constructor */
	public TranslationEndpoint() {
		super();
	}

	/** Get translations
	 *
	 * @return List<TranslationDTO>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	// Endpoint : /obfuscated-translations
	@Override
	public List<TranslationDTO> getTranslations() throws FunctionalException, TechnicalException {
		LOGGER.info("Endpoint --> [GET] Translation - /obfuscated-translations");
		try {
			// Get translations
			List<TranslationDTO> translations = translationUC.getAll();
			return translations;
		} catch (FunctionalException fex) {
			LOGGER.error(fex.getMessage() != null ? fex.getMessage() : "Unexpected error");
			throw fex;
		} catch (TechnicalException tex) {
			LOGGER.error(tex.getMessage() != null ? tex.getMessage() : "Unexpected error");
			throw tex;
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage() != null ? ex.getMessage() : "Unexpected error");
			throw new UnknownException();
		}
	}

	/** Add translations
	 *
	 * @param translations
	 * @return List<TranslationDTO>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	// Endpoint : /obfuscated-translations
	@Override
	public List<TranslationDTO> addTranslations(List<TranslationDTO> translations)
			throws FunctionalException, TechnicalException {
		LOGGER.info("Endpoint --> [POST] Translation - /obfuscated-translations");
		List<TranslationDTO> addedTranslations = null;
		try {
			// Add translations
			addedTranslations = translationUC.addAll(translations);
		} catch (FunctionalException fex) {
			LOGGER.error(fex.getMessage() != null ? fex.getMessage() : "Unexpected error");
			throw fex;
		} catch (TechnicalException tex) {
			LOGGER.error(tex.getMessage() != null ? tex.getMessage() : "Unexpected error");
			throw tex;
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage() != null ? ex.getMessage() : "Unexpected error");
			throw new UnknownException();
		}
		// Return added translations
		return addedTranslations;
	}
}
