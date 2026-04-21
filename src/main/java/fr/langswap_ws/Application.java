package fr.langswap_ws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import fr.langswap_ws.config.Hibernate;
import fr.langswap_ws.config.Settings;
import fr.langswap_ws.data.repository.api.ITranslationRepo;
import fr.langswap_ws.data.repository.impl.TranslationRepo;
import fr.langswap_ws.domain.model.mapper.api.ITranslationMapper;
import fr.langswap_ws.domain.model.mapper.impl.TranslationMapper;
import fr.langswap_ws.domain.usecase.api.ITranslationUC;
import fr.langswap_ws.domain.usecase.impl.TranslationUC;
import fr.langswap_ws.presentation.endpoint.api.IHomeEndpoint;
import fr.langswap_ws.presentation.endpoint.api.ITranslationEndpoint;
import fr.langswap_ws.presentation.endpoint.impl.HomeEndpoint;
import fr.langswap_ws.presentation.endpoint.impl.TranslationEndpoint;
import fr.langswap_ws.presentation.filter.CharacterEncodingFilter;
import fr.langswap_ws.presentation.model.mapper.JacksonObjectMapperProvider;
import fr.langswap_ws.presentation.validator.RequestValidator;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ApplicationPath;

/** LangSwap-WS
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@ApplicationPath("/api")
public class Application extends ResourceConfig {

	private static final Logger LOGGER = LogManager.getLogger(Application.class);

	/** Constructor */
	public Application() {

		// WebService start
		LOGGER.info("#############################################");
		LOGGER.info("LangSwap WebService {LangSwap_WS}");
		LOGGER.info("#############################################");
		LOGGER.info("Configuring dependency injection.");

		// Register packages to scan
		LOGGER.info("Registering packages to scan.");
		packages("fr.langswap_ws.presentation.endpoint.impl");
		packages("fr.langswap_ws.presentation.filter");
		packages("fr.langswap_ws.presentation.model.mapper");
		packages("fr.langswap_ws.domain");
		packages("fr.langswap_ws.data");

		// Register configuration classes
		LOGGER.info("Registering configuration classes.");
		register(Settings.class);
		register(Hibernate.class);

		// Register UTF-8 encoding support
		LOGGER.info("Registering UTF-8 encoding support.");
		register(CharacterEncodingFilter.class);
		register(JacksonObjectMapperProvider.class);

		// Register dependency injection configuration
		register(new AbstractBinder() {

			/** Dependency injection configuration */
			@Override
			protected void configure() {

				LOGGER.info("Registering classes for injection.");

				// Utils
				bind(Settings.class).to(Settings.class).in(Singleton.class);
				bind(Hibernate.class).to(Hibernate.class).in(Singleton.class);
				bind(RequestValidator.class).to(RequestValidator.class).in(Singleton.class);

				// Mapper
				bind(TranslationMapper.class).to(ITranslationMapper.class).in(Singleton.class);

				// Repository
				bind(TranslationRepo.class).to(ITranslationRepo.class).in(Singleton.class);

				// Use-cases
				bind(TranslationUC.class).to(ITranslationUC.class).in(Singleton.class);

				// Endpoints
				bind(HomeEndpoint.class).to(IHomeEndpoint.class).in(Singleton.class);
				bind(TranslationEndpoint.class).to(ITranslationEndpoint.class).in(Singleton.class);
			}
		});
	}
}
