package fr.langswap_ws.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Settings test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Settings test")
class SettingsTest {

	private Settings settings;

	/** Set up */
	@BeforeEach
	void setUp() {
		settings = new Settings();
	}

	/** Group : String retrieval tests */
	@Nested
	@DisplayName("Group : String retrieval tests")
	class StringSettingsTests {

		/** Test : Should retrieve an existing string */
		@Test
		@DisplayName("Test : Should retrieve an existing string")
		void shouldGetExistingString() {
			String key = "smtp.hostname";

			String value = settings.getString(key);

			assertEquals("smtp.yopmail.com", value);
		}

		/** Test : Should return null for non-existent key */
		@Test
		@DisplayName("Test : Should return null for non-existent key")
		void shouldReturnNullForNonExistentKey() {
			String key = "non.existent.key";

			String value = settings.getString(key);

			assertNull(value);
		}
	}

	/** Group : Integer retrieval tests */
	@Nested
	@DisplayName("Group : Integer retrieval tests")
	class IntegerSettingsTests {

		/** Test : Should retrieve an existing integer */
		@Test
		@DisplayName("Test : Should retrieve an existing integer")
		void shouldGetExistingInteger() {
			String key = "smtp.port";

			int value = settings.getInt(key);

			assertEquals(587, value);
		}

		/** Test : Should throw exception for non-existent key */
		@Test
		@DisplayName("Test : Should throw exception for non-existent key")
		void shouldThrowExceptionForNonExistentKey() {
			String key = "non.existent.key";

			NumberFormatException nfex = assertThrows(NumberFormatException.class,
					() -> settings.getInt(key));

			assertNotNull(nfex.getMessage());
		}

		/** Test : Should throw exception for non-numeric value */
		@Test
		@DisplayName("Test : Should throw exception for non-numeric value")
		void shouldThrowExceptionForNonNumericValue() {
			String key = "smtp.hostname";

			NumberFormatException nfex = assertThrows(NumberFormatException.class,
					() -> settings.getInt(key));

			assertNotNull(nfex.getMessage());
		}
	}

	/** Group : Boolean retrieval tests */
	@Nested
	@DisplayName("Group : Boolean retrieval tests")
	class BooleanSettingsTests {

		/** Test : Should retrieve a true boolean */
		@Test
		@DisplayName("Test : Should retrieve a true boolean")
		void shouldGetTrueBoolean() {
			String key = "smtp.ssl";

			boolean value = settings.getBoolean(key);

			assertTrue(value);
		}

		/** Test : Should return false for non-existent key */
		@Test
		@DisplayName("Test : Should return false for non-existent key")
		void shouldReturnFalseForNonExistentKey() {
			String key = "non.existent.key";

			boolean value = settings.getBoolean(key);

			assertFalse(value);
		}

		/** Test : Should return false for non-boolean value */
		@Test
		@DisplayName("Test : Should return false for non-boolean value")
		void shouldReturnFalseForNonBooleanValue() {
			String key = "smtp.hostname";

			boolean value = settings.getBoolean(key);

			assertFalse(value);
		}
	}
}
