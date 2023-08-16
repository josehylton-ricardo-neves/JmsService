package com.fedex.gisjmsservices.connectionconsumer;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.Logger;

import com.fedex.gisjmsservices.common.FedExUtils;

public class ConnectionConsumer {

	private static final Logger logger = FedExUtils.getLogger(ConnectionConsumer.class);

	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageConsumer consumer;

	public ConnectionConsumer() {
	}

	public MessageConsumer connect(Properties properties) {
		close();

		InitialContext context = null;
		while ((consumer == null)) {
			try {
				logger.info("Starting conenction consumer {}", Thread.currentThread().getName());

				logger.info("Starting InitialContext consumer {}", Thread.currentThread().getName());
				InitialContext ctx = new InitialContext(properties);
				logger.info("InitialContext Started consumer {}", Thread.currentThread().getName());

				logger.info("receiving connectionFactory consumer {}", Thread.currentThread().getName());
				connectionFactory = (ConnectionFactory) ctx.lookup(properties.getProperty("connectionFactory"));
				logger.info("received connectionFactory consumer {}", Thread.currentThread().getName());

				String createQueueDirectly = properties.getProperty("createQueueDirectly");
				if (createQueueDirectly == null)
					throw new RuntimeException(
							"ERROR: createQueueDirectly not found in runtime.properties file, aborting.");

				if (!createQueueDirectly.equalsIgnoreCase("true")) {

					destination = (Destination) ctx.lookup(properties.getProperty("destination"));
				}

				logger.info("receiving connection consumer {}", Thread.currentThread().getName());
				connection = connectionFactory.createConnection(properties.getProperty("USER_ID"),
						properties.getProperty("USER_PASSWORD"));
				logger.info("connection received consumer {}", Thread.currentThread().getName());

				logger.info("receiving session consumer {}", Thread.currentThread().getName());
				session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
				logger.info("session received consumer {}", Thread.currentThread().getName());

				if (createQueueDirectly.equalsIgnoreCase("true")) {
					String queueName = properties.getProperty("queueName");
					if (queueName == null)
						throw new RuntimeException("ERROR: queueName not found in runtime.properties file, aborting.");

					destination = session.createQueue(queueName);
				}

				logger.info("receiving consumer consumer {}", Thread.currentThread().getName());
				consumer = session.createConsumer(destination);
				logger.info("consumer received consumer {}", Thread.currentThread().getName());

				connection.start();

				logger.info("Consumer {} connected: {}", Thread.currentThread().getName(), connection);
				return consumer;

			} catch (NamingException ne) {
				logger.error("Error {} consumer {}", ne, Thread.currentThread().getName());

			} catch (JMSException e) {
				logger.error("Error {} consumer {}", e, Thread.currentThread().getName());

			} finally {
				if (context != null) {
					try {
						context.close();
						logger.info("Closed Context consumer {}", Thread.currentThread().getName());

					} catch (NamingException ne) {
						logger.error("Error consumer {} closing context", Thread.currentThread().getName());
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
		if (consumer != null) {
			try {
				consumer.close();
				logger.info("Closed consumer {}", Thread.currentThread().getName());

			} catch (JMSException e) {
				logger.error("Error closing consumer, consumer {}", Thread.currentThread().getName());
			} finally {
				consumer = null;
			}
		}
		if (session != null) {
			try {
				session.close();
				logger.info("Closed session consumer {}", Thread.currentThread().getName());

			} catch (JMSException e) {
				logger.error("Error closing session, consumer {}", Thread.currentThread().getName());
			} finally {
				session = null;
			}
		}
		if (connection != null) {
			try {
				connection.close();
				logger.info("Closed connection consumer {}", Thread.currentThread().getName());

			} catch (JMSException e) {
				logger.error("Error closing connection, consumer {}", Thread.currentThread().getName());
			} finally {
				consumer = null;
			}
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}

	public boolean checkConnection() {
		if (connection != null && session != null && consumer != null) {
			return true;
		}
		return false;
	}

}
