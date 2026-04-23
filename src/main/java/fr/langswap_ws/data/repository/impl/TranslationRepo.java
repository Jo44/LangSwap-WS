package fr.langswap_ws.data.repository.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import fr.langswap_ws.config.Hibernate;
import fr.langswap_ws.config.Settings;
import fr.langswap_ws.data.repository.api.ITranslationRepo;
import fr.langswap_ws.domain.exception.AlreadyExistException;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.NotExistException;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.domain.model.entity.Translation;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.PersistenceException;

/** Translation repository
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Singleton
public class TranslationRepo implements ITranslationRepo {

	private static final Logger LOGGER = LogManager.getLogger(TranslationRepo.class);
	private final Hibernate hibernate;
	private final String getAll;
	private final String getOne;

	/** Constructor
	 *
	 * @param hibernate
	 * @param settings */
	@Inject
	public TranslationRepo(Hibernate hibernate, Settings settings) {
		super();
		this.hibernate = hibernate;
		// Load SQL requests
		getAll = settings.getString("sql.get.all");
		getOne = settings.getString("sql.get.one");
	}

	/* Translation - Get */

	/** Get all translations
	 *
	 * @return List<Translation>
	 * @throws FunctionalException
	 * @throws TechnicalException */
	@Override
	public List<Translation> getAll() throws FunctionalException, TechnicalException {
		List<Translation> translations = new ArrayList<>();
		Session session = hibernate.openSession();
		try {
			// Get query
			Query<Translation> query = session.createQuery(getAll, Translation.class);
			// Get all translations
			List<Translation> translationsResult = query.getResultList();
			// Add translations to the list
			translations.addAll(translationsResult);
			// Sort list
			translations.sort(Comparator.comparing(Translation::getCreationDate)
					.thenComparing(Translation::getObfuscatedName));
			// Close session
			hibernate.validateSession(session);
			LOGGER.debug("Translations retrieved successfully.");
		} catch (PersistenceException pex) {
			LOGGER.debug("Failed to retrieve translations.");
			throw new TechnicalException("Unable to retrieve translations.");
		} finally {
			hibernate.closeSession(session);
		}
		return translations;
	}

	/** Get one translation
	 *
	 * @param actionId
	 * @param obfuscatedName
	 * @return Translation
	 * @throws FunctionalException
	 * @throws TechnicalException */
	@Override
	public Translation getOne(Integer actionId, String obfuscatedName)
			throws FunctionalException, TechnicalException {
		Translation translation = null;
		Session session = hibernate.openSession();
		try {
			// Get query
			Query<Translation> query = session.createQuery(getOne, Translation.class);
			query.setParameter("actionId", actionId);
			query.setParameter("obfuscatedName", obfuscatedName);
			// Get translation
			List<Translation> translationsResult = query.getResultList();
			if (translationsResult == null || translationsResult.size() < 1) {
				throw new NotExistException("Translation does not exist.");
			} else {
				translation = translationsResult.getFirst();
			}
			// Close session
			hibernate.validateSession(session);
			LOGGER.debug("Translation retrieved successfully.");
		} catch (FunctionalException fex) {
			LOGGER.debug("Failed to retrieve translation.");
			hibernate.closeSession(session);
			throw fex;
		} catch (PersistenceException pex) {
			LOGGER.debug("Failed to retrieve translation.");
			hibernate.closeSession(session);
			throw new TechnicalException("Unable to retrieve translation.");
		} finally {
			hibernate.closeSession(session);
		}
		return translation;
	}

	/* Translation - Add */

	/** Add translation
	 *
	 * @param translation
	 * @return Translation
	 * @throws FunctionalException
	 * @throws TechnicalException */
	@Override
	public Translation add(Translation translation) throws FunctionalException, TechnicalException {
		Session session = hibernate.openSession();
		try {
			// Get query
			Query<Translation> query = session.createQuery(getOne, Translation.class);
			query.setParameter("actionId", translation.getActionId());
			query.setParameter("obfuscatedName", translation.getObfuscatedName());
			// Get translation
			List<Translation> translationsResult = query.getResultList();
			if (translationsResult != null && translationsResult.size() > 0) {
				throw new AlreadyExistException("Translation already exists.");
			}
			// Save translation
			session.persist(translation);
			// Close session
			hibernate.validateSession(session);
			LOGGER.debug("Translation added successfully.");
		} catch (PersistenceException pex) {
			LOGGER.debug("Failed to add translation.");
			hibernate.closeSession(session);
			throw new TechnicalException("Unable to add translation.");
		}
		return translation;
	}
}
