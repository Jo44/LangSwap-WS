package fr.langswap_ws.config;

import fr.langswap_ws.domain.exception.TechnicalException;

/** Hibernate singleton for tests
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
public class HibernateUtils {

	private static Settings settings;
	private static Hibernate hibernate;
	private static boolean initialized = false;

	/** Constructor */
	private HibernateUtils() {
	}

	/** Initialize Settings and Hibernate instances (if needed)
	 *
	 * @throws TechnicalException */
	public static synchronized void initialize() throws TechnicalException {
		if (!initialized) {
			settings = new Settings();
			hibernate = new Hibernate(settings);
			initialized = true;
		}
	}

	/** Get Settings instance
	 *
	 * @return Settings
	 * @throws TechnicalException */
	public static Settings getSettings() throws TechnicalException {
		if (!initialized) {
			initialize();
		}
		return settings;
	}

	/** Get Hibernate instance
	 *
	 * @return Hibernate
	 * @throws TechnicalException */
	public static Hibernate getHibernate() throws TechnicalException {
		if (!initialized) {
			initialize();
		}
		return hibernate;
	}

	/** Close Hibernate */
	public static void shutdown() {
		if (initialized && hibernate != null) {
			hibernate.shutdown();
			initialized = false;
			hibernate = null;
			settings = null;
		}
	}
}
