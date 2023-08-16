package com.fedex.gisjmsservices.connectionproducer;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.Logger;

import com.fedex.gisjmsservices.common.FedExUtils;

public class ConnectionProducer {

	private static final Logger logger = FedExUtils.getLogger(ConnectionProducer.class);

	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageProducer producer;

	public ConnectionProducer() {
	}

	public MessageProducer connect(Properties properties) {
		close();

		InitialContext context = null;
		while (producer == null) {
			try {
				logger.info("Starting conenction producer {}", Thread.currentThread().getName());

				logger.info("Starting InitialContext producer {}", Thread.currentThread().getName());
				InitialContext ctx = new InitialContext(properties);
				logger.info("InitialContext Started producer {}", Thread.currentThread().getName());

				logger.info("receiving connectionFactory producer {}", Thread.currentThread().getName());
				connectionFactory = (ConnectionFactory) ctx.lookup(properties.getProperty("connectionFactory"));
				logger.info("received connectionFactory producer {}", Thread.currentThread().getName());

				String createQueueDirectly = properties.getProperty("createQueueDirectly");
				if (createQueueDirectly == null)
					throw new RuntimeException(
							"ERROR: createQueueDirectly not found in runtime.properties file, aborting.");

				if (!createQueueDirectly.equalsIgnoreCase("true")) {

					destination = (Destination) ctx.lookup(properties.getProperty("destination"));
				}

				logger.info("receiving connection producer {}", Thread.currentThread().getName());
				connection = connectionFactory.createConnection(properties.getProperty("USER_ID"),
						properties.getProperty("USER_PASSWORD"));
				logger.info("connection received producer {}", Thread.currentThread().getName());

				logger.info("receiving session producer {}", Thread.currentThread().getName());
				session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
				logger.info("session received producer {}", Thread.currentThread().getName());

				if (createQueueDirectly.equalsIgnoreCase("true")) {
					String queueName = properties.getProperty("queueName");
					if (queueName == null)
						throw new RuntimeException("ERROR: queueName not found in runtime.properties file, aborting.");

					destination = session.createQueue(queueName);
				}

				logger.info("receiving producer {}", Thread.currentThread().getName());
				producer = session.createProducer(destination);
				logger.info("producer received {}", Thread.currentThread().getName());

				connection.start();

				logger.info("Producer {} connected: {}", Thread.currentThread().getName(), connection);
				return producer;

			} catch (NamingException ne) {
				logger.error("Error {} producer {}", ne, Thread.currentThread().getName());

			} catch (JMSException e) {
				logger.error("Error {} producer {}", e, Thread.currentThread().getName());

			} finally {
				if (context != null) {
					try {
						context.close();
						logger.info("Closed Context producer {}", Thread.currentThread().getName());
					} catch (NamingException ne) {
						logger.error("Error producer {} closing context", Thread.currentThread().getName());
					}
				}
			}
			close();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
			}
		}

		return null;
	}

	public void close() {
		if (connection != null)
			logger.info("Connection producer {} stats: {}", Thread.currentThread().getName(), connection);

		if (producer != null) {
			try {
				producer.close();
				logger.info("Closed producer {}.", Thread.currentThread().getName());
			} catch (JMSException e) {
				logger.error("Error closing producer, producer: {}", Thread.currentThread().getName());
			} finally {
				producer = null;
			}
		}
		if (session != null) {
			try {
				session.close();
				logger.info("Closed session producer {}.", Thread.currentThread().getName());
			} catch (JMSException e) {
				logger.error("Error closing session, producer: {}", Thread.currentThread().getName());
			} finally {
				session = null;
			}
		}
		if (connection != null) {
			try {
				connection.close();
				logger.info("Closed connection producer {}.", Thread.currentThread().getName());
			} catch (JMSException e) {
				logger.error("Error closing connection, producer: {}", Thread.currentThread().getName());
			} finally {
				producer = null;
			}
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public MessageProducer getProducer() {
		return producer;
	}

	public void setProducer(MessageProducer producer) {
		this.producer = producer;
	}

	public boolean checkConnection() {
		if (connection != null && session != null && producer != null) {
			return true;
		}
		return false;
	}
}
