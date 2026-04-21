package fr.langswap_ws.config;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import fr.langswap_ws.domain.exception.TechnicalException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.PersistenceException;

/** Hibernate
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Singleton
public class Hibernate {

	private static final Logger LOGGER = LogManager.getLogger(Hibernate.class);
	private SessionFactory factory;
	private StandardServiceRegistry registry;
	private final String smtpHostname;
	private final int smtpPort;
	private final boolean smtpSsl;
	private final String smtpUser;
	private final String smtpPass;
	private final String emailTarget;
	private final String emailObject;
	private final String emailContent;
	private final int maxRetryNb = 5;
	private int retryNb = 0;

	/** Constructor
	 *
	 * @param settings */
	@Inject
	public Hibernate(Settings settings) {
		// Load parameters
		smtpHostname = settings.getString("smtp.hostname");
		smtpPort = settings.getInt("smtp.port");
		smtpSsl = settings.getBoolean("smtp.ssl");
		smtpUser = settings.getString("smtp.user");
		smtpPass = settings.getString("smtp.pass");
		emailTarget = settings.getString("email.target");
		emailObject = settings.getString("email.object");
		emailContent = settings.getString("email.content");
		// Initialize factory
		initFactory();
	}

	/** Initialize factory */
	private void initFactory() {
		try {
			LOGGER.info("Initializing Hibernate ...");
			// Load file 'hibernate.cfg.xml'
			registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
			MetadataSources sources = new MetadataSources(registry);
			Metadata metadata = sources.getMetadataBuilder().build();
			factory = metadata.getSessionFactoryBuilder().build();
		} catch (Exception ex) {
			LOGGER.error("Error during Hibernate initialization: " + ex.getMessage());
			if (registry != null) {
				StandardServiceRegistryBuilder.destroy(registry);
			}
		}
	}

	/** Close session factory and registry */
	public void shutdown() {
		if (factory != null && !factory.isClosed()) {
			factory.close();
		}
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
		LOGGER.info("Hibernate shut down successfully.");
	}

	/** Get a ready-to-use Hibernate session (= transaction started)
	 *
	 * @return Session
	 * @throws TechnicalException */
	public Session openSession() throws TechnicalException {
		Session session = factory.openSession();
		LOGGER.debug("Hibernate session started ..");
		try {
			// Start transaction
			session.beginTransaction();
		} catch (JDBCConnectionException jdbccex) {
			retryNb++;
			LOGGER.error("Error #" + retryNb + " while opening a new transaction.");
			session = openSessionRetry(session);
		}
		// Return exception if unable to start transaction
		if (session == null || session.getTransaction() == null
				|| session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
			throw new TechnicalException("Unable to start the transaction.");
		}
		LOGGER.debug("Transaction started ..");
		// Reset try number
		retryNb = 0;
		return session;
	}

	/** Refresh session factory and try to get a new session
	 *
	 * @param session
	 * @return Session
	 * @throws TechnicalException */
	private Session openSessionRetry(Session session) throws TechnicalException {
		if (retryNb < maxRetryNb) {
			LOGGER.info("Retrying transaction initialization ..");
			// Close session
			session.close();
			// Reinitialize factory
			initFactory();
			// Reopen session
			session = openSession();
		} else {
			// Send notification email to administrator
			sendEmail(emailTarget, emailObject, emailContent);
			throw new TechnicalException(
					"Unable to start the transaction after " + retryNb + " attempts .. Aborted.");
		}
		return session;
	}

	/** Validate transaction and close session
	 *
	 * @param session
	 * @throws PersistenceException */
	public void validateSession(Session session) throws PersistenceException {
		if (session != null && session.isOpen() && session.getTransaction() != null) {
			try (session) {
				session.flush();
				session.getTransaction().commit();
				LOGGER.debug("Transaction committed.");
			} catch (PersistenceException pex) {
				LOGGER.debug("Transaction rolled back !");
				throw pex;
			} finally {
				LOGGER.debug("Hibernate session closed.");
			}
		}
	}

	/** Rollback transaction and close session
	 *
	 * @param session
	 * @throws PersistenceException */
	public void closeSession(Session session) throws PersistenceException {
		if (session != null && session.isOpen()) {
			try (session) {
				if (session.getTransaction() != null) {
					session.getTransaction().rollback();
					LOGGER.debug("Transaction rolled back.");
				}
			} catch (PersistenceException pex) {
				LOGGER.debug("Transaction rolled back !");
				throw pex;
			} finally {
				LOGGER.debug("Hibernate session closed.");
			}
		}
	}

	/** Hibernate connection test
	 *
	 * @return boolean */
	public boolean connectionTest() {
		LOGGER.info("Running Hibernate connection test :");
		boolean ready = false;
		try (Session session = factory.openSession()) {
			if (session != null && session.isConnected()) {
				session.beginTransaction();
				ready = true;
				LOGGER.info("Hibernate connection test passed.");
			}
		} catch (HibernateException hex) {
			LOGGER.error("Hibernate connection test failed !");
		}
		return ready;
	}

	/** Send email to administrator
	 *
	 * @param target
	 * @param subject
	 * @param message */
	private void sendEmail(String target, String subject, String message) {
		try {
			Email email = new SimpleEmail();
			email.setHostName(smtpHostname);
			email.setSmtpPort(smtpPort);
			email.setSSLOnConnect(smtpSsl);
			email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPass));
			email.setSSLOnConnect(true);
			email.setFrom(smtpUser);
			email.setSubject(subject);
			email.setMsg(message);
			email.addTo(target);
			email.send();
			LOGGER.info("Notification email sent to administrator : " + target);
		} catch (EmailException eex) {
			LOGGER.error("Error while sending notification email to administrator : " + target);
		}
	}
}
