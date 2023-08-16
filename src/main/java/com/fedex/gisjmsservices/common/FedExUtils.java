package com.fedex.gisjmsservices.common;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FedExUtils {

	private static final Logger logger = FedExUtils.getLogger(FedExUtils.class);

	public static final String CONNECTION_TYPE = "connection.type";

	public static final String JAVA_NAME_PROVIDER_URL = "java.naming.provider.url";

	public static final String JAVA_NAMING_FACTORY_INITIAL = "java.naming.factory.initial";

	public static final String CONNECTION_FACTORY = "connectionFactory";

	public static final String CREATE_QUEUE_DIRECTLY = "createQueueDirectly";

	public static final String DESTINATION = "destination";

	public static final String QUEUE_NAME = "queueName";

	public static final String USER_ID = "USER_ID";

	public static final String USER_PASSWORD = "USER_PASSWORD";

	public static final String DIRECTORY_INPUT_OUTPUT = "directory.InputOutput";

	public static final String CONFIRM_BACKUP_ERROR = "save.directory.backup.and.error";

	public static final String DIRECTORY_BACKUP = "directory.backup";

	public static final String DIRECTORY_ERROR = "directory.error";

	public static final String REGEX_1 = ", ";

	public static final String REGEX_2 = ".";

	public static final String REGEX_3 = "/";

	public static final String FILE_EXTENSION = "txt";

	public static Logger getLogger(final Class<?> clazz) {
		return LogManager.getLogger(clazz);
	}

	public static Properties createProperties(String prefixo, String connectionType, String providerUrl,
			String factoryInitial, String connectionFactory, String createQueueDirectly, String destination,
			String queueName, String userID, String userPassword, String directoryInOut, String confirmBackupAndError,
			String directoryBackup, String directoryError) {

		Properties properties = new Properties();
		properties.setProperty("prefixContext", prefixo);
		properties.setProperty(JAVA_NAME_PROVIDER_URL, providerUrl);
		properties.setProperty(JAVA_NAMING_FACTORY_INITIAL, factoryInitial);
		properties.setProperty(CONNECTION_FACTORY, connectionFactory);
		properties.setProperty(CREATE_QUEUE_DIRECTLY, createQueueDirectly);
		properties.setProperty(DESTINATION, destination);
		properties.setProperty(QUEUE_NAME, queueName);
		properties.setProperty(USER_ID, userID);
		properties.setProperty(USER_PASSWORD, userPassword);
		properties.setProperty(DIRECTORY_INPUT_OUTPUT, directoryInOut);

		if (connectionType.equals("producer") || connectionType.equals("produtor")
				|| connectionType.equals("publish")) {
			properties.setProperty(CONFIRM_BACKUP_ERROR, confirmBackupAndError);
			properties.setProperty(DIRECTORY_BACKUP, directoryBackup);
			properties.setProperty(DIRECTORY_ERROR, directoryError);
		}
		logger.info("Properties file {} has been loaded", properties.getProperty("prefixContext"));
		return properties;
	}
}
