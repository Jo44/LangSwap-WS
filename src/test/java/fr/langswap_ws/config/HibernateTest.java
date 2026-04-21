package fr.langswap_ws.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import fr.langswap_ws.domain.exception.TechnicalException;

/** Hibernate test
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@DisplayName("Hibernate test")
class HibernateTest {

	private Hibernate hibernate;
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;

	/** Set up */
	@BeforeEach
	void setUp() throws Exception {
		Settings settings = mock(Settings.class);
		when(settings.getString("smtp.hostname")).thenReturn("smtp.test.com");
		when(settings.getInt("smtp.port")).thenReturn(587);
		when(settings.getBoolean("smtp.ssl")).thenReturn(true);
		when(settings.getString("smtp.user")).thenReturn("test@test.com");
		when(settings.getString("smtp.pass")).thenReturn("password");
		when(settings.getString("email.target")).thenReturn("admin@test.com");
		when(settings.getString("email.object")).thenReturn("Test Object");
		when(settings.getString("email.content")).thenReturn("Test Content");

		hibernate = new Hibernate(settings);
		sessionFactory = mock(SessionFactory.class);
		session = mock(Session.class);
		transaction = mock(Transaction.class);

		setField("factory", sessionFactory);
		setField("registry", mock(StandardServiceRegistry.class));

		when(sessionFactory.openSession()).thenReturn(session);
		when(session.getTransaction()).thenReturn(transaction);
	}

	/** Group : Session management tests */
	@Nested
	@DisplayName("Group : Session management tests")
	class SessionManagementTests {

		/** Test : Should open a session successfully
		 * 
		 * @throws TechnicalException */
		@Test
		@DisplayName("Test : Should open a session successfully")
		void shouldOpenSessionSuccessfully() throws TechnicalException {
			when(transaction.getStatus()).thenReturn(TransactionStatus.ACTIVE);

			Session result = hibernate.openSession();

			assertSame(session, result);
			verify(session).beginTransaction();
		}

		/** Test : Should throw an error when transaction is not active */
		@Test
		@DisplayName("Test : Should throw an error when transaction is not active")
		void shouldThrowWhenTransactionIsNotActive() {
			when(transaction.getStatus()).thenReturn(TransactionStatus.NOT_ACTIVE);

			assertThrows(TechnicalException.class, () -> hibernate.openSession());
		}

		/** Test : Should validate a session successfully */
		@Test
		@DisplayName("Test : Should validate a session successfully")
		void shouldValidateSessionSuccessfully() {
			when(session.isOpen()).thenReturn(true);

			hibernate.validateSession(session);

			verify(session).flush();
			verify(transaction).commit();
			verify(session).close();
		}

		/** Test : Should close a session with rollback */
		@Test
		@DisplayName("Test : Should close a session with rollback")
		void shouldCloseSessionSuccessfully() {
			when(session.isOpen()).thenReturn(true);

			hibernate.closeSession(session);

			verify(transaction).rollback();
			verify(session).close();
		}
	}

	/** Group : Connection tests */
	@Nested
	@DisplayName("Group : Connection tests")
	class ConnectionTests {

		/** Test : Should confirm an active connection */
		@Test
		@DisplayName("Test : Should confirm an active connection")
		void shouldTestConnectionSuccessfully() {
			Session connectionSession = mock(Session.class);
			when(sessionFactory.openSession()).thenReturn(connectionSession);
			when(connectionSession.isConnected()).thenReturn(true);

			boolean connected = hibernate.connectionTest();

			assertTrue(connected);
			verify(connectionSession).beginTransaction();
			verify(connectionSession).close();
		}

		/** Test : Should return false when connection is inactive */
		@Test
		@DisplayName("Test : Should return false when connection is inactive")
		void shouldReturnFalseWhenConnectionIsInactive() {
			Session connectionSession = mock(Session.class);
			when(sessionFactory.openSession()).thenReturn(connectionSession);
			when(connectionSession.isConnected()).thenReturn(false);

			boolean connected = hibernate.connectionTest();

			assertFalse(connected);
			verify(connectionSession).close();
		}
	}

	/** Set field
	 * 
	 * @param fieldName
	 * @param value
	 * @throws Exception */
	private void setField(String fieldName, Object value) throws Exception {
		Field field = Hibernate.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(hibernate, value);
	}
}
