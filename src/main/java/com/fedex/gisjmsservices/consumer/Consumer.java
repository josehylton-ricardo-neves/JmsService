package com.fedex.gisjmsservices.consumer;

import java.io.IOException;
import java.time.Instant;
import java.util.Properties;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import com.tibco.tibjms.TibjmsBytesMessage;
import com.tibco.tibjms.TibjmsTextMessage;
import com.fedex.mi.decorator.jms.fxJMSClient;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fedex.gisjmsservices.common.FedExUtils;
import com.fedex.gisjmsservices.common.FileUtils;
import com.fedex.gisjmsservices.connectionconsumer.ConnectionConsumer;

@Service
public class Consumer implements Runnable {

	private static final Logger logger = FedExUtils.getLogger(Consumer.class);

	private Properties properties;
	private ConnectionConsumer connection = new ConnectionConsumer();
	private MessageConsumer consumer;

	public Consumer(Properties properties) {
		this.properties = properties;
	}

	public void run() {

		logger.info("Starting consumer {}", Thread.currentThread().getName());

		new fxJMSClient().checkPassword("");

		logger.info("Tryng to connect consumer {} with queue...", Thread.currentThread().getName());

		while (consumer == null) {
			consumer = connection.connect(properties);
		}

		logger.info("Consumer {} has successfully connected", Thread.currentThread().getName());

		FileUtils.createDirectory(properties.getProperty(FedExUtils.DIRECTORY_INPUT_OUTPUT));

		Message m = null;
		String prefixMessageName = "Message-" + Thread.currentThread().getName() + "-";
		String fileName = "";
		while (true) {

			if (checkConnection()) {

				try {
					m = consumer.receive();

					if (m != null) {
						fileName = prefixMessageName + Instant.now().toString().replace(":", "") + FedExUtils.REGEX_2
								+ FedExUtils.FILE_EXTENSION;

						if (m instanceof TibjmsBytesMessage) {
							FileUtils.saveFile(properties.getProperty(FedExUtils.DIRECTORY_INPUT_OUTPUT), fileName,
									m.getBody(byte[].class));
							logger.info("{} message saved", fileName);
							m.acknowledge();
						} else if (m instanceof TibjmsTextMessage) {
							FileUtils.saveFile(properties.getProperty(FedExUtils.DIRECTORY_INPUT_OUTPUT), fileName,
									m.getBody(String.class).getBytes("UTF-16"));
							logger.info("{} message saved", fileName);
							m.acknowledge();
						} else {
							logger.error("Unsuported Message to consumer {}", Thread.currentThread().getName());
							m.acknowledge();
							throw new JMSException("Unsuported Message to consumer {}",
									Thread.currentThread().getName());
						}
					}

				} catch (IOException e) {
					logger.error("Consumer {} File error {}", Thread.currentThread().getName(), e);
				} catch (IllegalStateException e) {
					logger.error("Consumer {} error {}", Thread.currentThread().getName(), e);
					connection.close();
					connection.setSession(null);
					connection.setConsumer(null);
					consumer = connection.connect(properties);
				} catch (JMSException e) {
					logger.error("Consumer {} error {}", Thread.currentThread().getName(), e);
				} catch (Exception e) {
					logger.error("Consumer {} error {}", Thread.currentThread().getName(), e);
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.error("Consumer {} error in Thread: {}", Thread.currentThread().getName(), e);
			}
		}
	}

	private boolean checkConnection() {
		while (!connection.checkConnection()) {
			connection.connect(properties);
		}
		return true;
	}
}
