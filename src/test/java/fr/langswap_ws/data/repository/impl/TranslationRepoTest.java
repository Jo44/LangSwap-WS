package fr.langswap_ws.data.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import fr.langswap_ws.config.Hibernate;
import fr.langswap_ws.config.Settings;
import fr.langswap_ws.domain.exception.FunctionalException;
import fr.langswap_ws.domain.exception.TechnicalException;
import fr.langswap_ws.domain.model.base.Language;
import fr.langswap_ws.domain.model.entity.Translation;

/** Translation repository test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Translation repository test")
@ExtendWith(MockitoExtension.class)
class TranslationRepoTest {

	/** Mock */
	@Mock
	private Hibernate hibernate;
	@Mock
	private Settings settings;
	@Mock
	private Session session;
	@Mock
	private Query<Translation> translationQuery;
	@Mock
	private Query<Long> countQuery;

	private TranslationRepo translationRepo;

	/** Set up */
	@BeforeEach
	void setUp() throws TechnicalException {
		when(settings.getString("sql.get.all")).thenReturn("SELECT t FROM Translation t");
		when(settings.getString("sql.get.one")).thenReturn(
				"SELECT t FROM Translation t WHERE t.action_id = :actionId AND t.obfuscated_name = :obfuscatedName");
		when(hibernate.openSession()).thenReturn(session);

		translationRepo = new TranslationRepo(hibernate, settings);
	}

	/** Group : FindAll tests */
	@Nested
	@DisplayName("Group : FindAll tests")
	class FindAllTests {

		/** Test : Should return all sorted translations */
		@Test
		@DisplayName("Test : Should return all sorted translations")
		void shouldReturnAllTranslationsSorted() throws FunctionalException, TechnicalException {
			Translation second = createTranslation(2, 20, "zzz_name");
			Translation first = createTranslation(1, 10, "aaa_name");
			when(session.createQuery("SELECT t FROM Translation t", Translation.class))
					.thenReturn(translationQuery);
			when(translationQuery.getResultList()).thenReturn(List.of(first, second));

			List<Translation> result = translationRepo.getAll();

			assertNotNull(result);
			assertEquals(2, result.size());
			assertEquals("aaa_name", result.get(0).getObfuscatedName());
			assertEquals("zzz_name", result.get(1).getObfuscatedName());
			verify(hibernate).validateSession(session);
			verify(hibernate).closeSession(session);
		}
	}

	/** Group : FindOne tests */
	@Nested
	@DisplayName("Group : FindOne tests")
	class FindOneTests {

		/** Test : Should return an existing translation */
		@Test
		@DisplayName("Test : Should return an existing translation")
		void shouldReturnExistingTranslation() throws FunctionalException, TechnicalException {
			Translation expected = createTranslation(1, 100, "abc_xyz");
			when(session.createQuery(
					"SELECT t FROM Translation t WHERE t.action_id = :actionId AND t.obfuscated_name = :obfuscatedName",
					Translation.class)).thenReturn(translationQuery);
			when(translationQuery.getResultList()).thenReturn(List.of(expected));

			Translation result = translationRepo.getOne(100, "abc_xyz");

			assertSame(expected, result);
			verify(translationQuery).setParameter("actionId", 100);
			verify(translationQuery).setParameter("obfuscatedName", "abc_xyz");
		}

		/** Test : Should throw an exception if translation does not exist */
		@Test
		@DisplayName("Test : Should throw an exception if translation does not exist")
		void shouldThrowWhenTranslationDoesNotExist() {
			when(session.createQuery(
					"SELECT t FROM Translation t WHERE t.action_id = :actionId AND t.obfuscated_name = :obfuscatedName",
					Translation.class)).thenReturn(translationQuery);
			when(translationQuery.getResultList()).thenReturn(List.of());

			assertThrows(FunctionalException.class, () -> translationRepo.getOne(100, "missing"));
		}
	}

	/** Group : Add tests */
	@Nested
	@DisplayName("Group : Add tests")
	class AddTests {

		/** Test : Should persist a translation */
		@Test
		@DisplayName("Test : Should persist a translation")
		void shouldAddTranslation() throws FunctionalException, TechnicalException {
			Translation translation = createTranslation(null, 100, "abc_xyz");
			when(session.createQuery(
					"SELECT t FROM Translation t WHERE t.action_id = :actionId AND t.obfuscated_name = :obfuscatedName",
					Translation.class)).thenReturn(translationQuery);
			when(translationQuery.getResultList()).thenReturn(List.of());

			Translation result = translationRepo.add(translation);

			assertSame(translation, result);
			verify(session).persist(translation);
			verify(hibernate).validateSession(session);
		}
	}

	/** Create translation
	 * 
	 * @param id
	 * @param actionId
	 * @param obfuscatedName
	 * @return Translation */
	private Translation createTranslation(Integer id, Integer actionId, String obfuscatedName) {
		Translation translation = new Translation();
		translation.setId(id);
		translation.setActionId(actionId);
		translation.setObfuscatedName(obfuscatedName);
		translation.setDeobfuscatedName("Deobfuscated Name");
		translation.setLanguageId(Language.English);
		translation.setCharacterName("Tester");
		translation.setCreationDate(new Timestamp(System.currentTimeMillis()));
		return translation;
	}
}
