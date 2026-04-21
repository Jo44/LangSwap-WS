package fr.langswap_ws.presentation.endpoint.api;

import java.util.List;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.domain.model.dto.TranslationDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/** Translation endpoint interface
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ITranslationEndpoint {

	/** Get translations
	 *
	 * @return List<TranslationDTO>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	// Endpoint : /obfuscated-translations
	@GET
	public List<TranslationDTO> getTranslations() throws FunctionalException, TechnicalException;

	/** Add translations
	 *
	 * @param translations
	 * @return List<TranslationDTO>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	// Endpoint : /obfuscated-translations
	@POST
	public List<TranslationDTO> addTranslations(List<TranslationDTO> translations)
			throws FunctionalException, TechnicalException;
}
