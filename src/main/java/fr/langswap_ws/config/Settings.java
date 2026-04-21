package fr.langswap_ws.config;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.inject.Singleton;

/** Settings
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Singleton
public class Settings {

	private static final Logger LOGGER = LogManager.getLogger(Settings.class);
	private final Properties properties;

	/** Constructor */
	public Settings() {
		properties = new Properties();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			// Load file 'settings.properties'
			properties.load(classLoader.getResourceAsStream("settings.properties"));
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
	}

	/* Getters */

	public String getString(String key) {
		return properties.getProperty(key, null);
	}

	public int getInt(String key) {
		String parametreStr = properties.getProperty(key, null);
		return Integer.parseInt(parametreStr);
	}

	public boolean getBoolean(String key) {
		String parametreStr = properties.getProperty(key, null);
		return Boolean.parseBoolean(parametreStr);
	}
}
