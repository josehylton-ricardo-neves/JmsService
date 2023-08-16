package com.fedex.gisjmsservices.producer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.util.Properties;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageProducer;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fedex.gisjmsservices.common.FedExUtils;
import com.fedex.gisjmsservices.common.FileUtils;
import com.fedex.gisjmsservices.connectionproducer.ConnectionProducer;

@Service
public class Producer implements Runnable {

	private Properties properties;
	private ConnectionProducer connection = new ConnectionProducer();
	private MessageProducer producer;

	private static final Logger logger = FedExUtils.getLogger(Producer.class);

	public Producer(Properties properties) {
		this.properties = properties;
	}

	public void run() {

		logger.info("Starting publisher {}", Thread.currentThread().getName());

		while (producer == null) {
			producer = connection.connect(properties);
		}

		logger.info("Publisher {} started", Thread.currentThread().getName());

		if (Boolean.parseBoolean(properties.getProperty(FedExUtils.CONFIRM_BACKUP_ERROR))) {
			FileUtils.createDirectory(properties.getProperty(FedExUtils.DIRECTORY_BACKUP));
			FileUtils.createDirectory(properties.getProperty(FedExUtils.DIRECTORY_ERROR));
		}

		while (true) {
			try {
				checkFolder();
				Thread.sleep(5000);
			} catch (Exception e) {
				logger.error("Publisher {} error {}", Thread.currentThread().getName(), e);
			}
		}

	}

	private void checkFolder() {

		try {
			File dirWithFilesToSend = new File(properties.getProperty(FedExUtils.DIRECTORY_INPUT_OUTPUT));
			File arquivos[] = dirWithFilesToSend.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".txt");
				}
			});

			if (arquivos.length > 0) {
				sendMessage(arquivos);
			}

		} catch (Exception e) {
			logger.error("Publisher {} error looking for files to upload {}", Thread.currentThread().getName(), e);
		}
	}

	private void sendMessage(File arquivos[]) throws IOException, JMSException {

		for (File arquivo : arquivos) {

			try {
				if (checkConnection()) {
					String message = FileUtils.readFile(arquivo);
					producer.send(connection.getSession().createTextMessage(message));
					// condição para gravar backup/erro
					if (Boolean.parseBoolean(properties.getProperty(FedExUtils.CONFIRM_BACKUP_ERROR))) {
						FileUtils.copyFile(arquivo, properties.getProperty(FedExUtils.DIRECTORY_BACKUP));
					}
					FileUtils.deleteFileIfExist(arquivo);
				}

			} catch (IOException e) {
				logger.error("Publisher {} File error {}", Thread.currentThread().getName(), e);
				// condição para gravar backup/erro
				if (Boolean.parseBoolean(properties.getProperty(FedExUtils.CONFIRM_BACKUP_ERROR))) {
					FileUtils.copyFile(arquivo, properties.getProperty(FedExUtils.DIRECTORY_ERROR));
				}
				FileUtils.deleteFileIfExist(arquivo);
			} catch (IllegalStateException e) {
				logger.error("Publisher {} error {}", Thread.currentThread().getName(), e);
				connection.close();
				connection.setSession(null);
				connection.setProducer(null);
				producer = connection.connect(properties);
				continue;
			} catch (JMSException e) {
				logger.error("Publisher {} error sending message to queue {}", Thread.currentThread().getName(), e);
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
