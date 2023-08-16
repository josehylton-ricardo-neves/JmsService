package com.fedex.gisjmsservices.config;

public class ThreadConfiguration {

	private String prefixo;
	private String connectionType;
	private String javaNamingProviderUrl;
	private String javaNamingFactoryInitial;
	private String connectionFactory;
	private String createQueueDirectly;
	private String destination;
	private String queueName;
	private String userID;
	private String userPassword;
	private String directoryInputOutput;
	private String confirmBackupAndError;
	private String directoryBackup;
	private String directoryError;

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getJavaNamingProviderUrl() {
		return javaNamingProviderUrl;
	}

	public void setJavaNamingProviderUrl(String javaNamingProviderUrl) {
		this.javaNamingProviderUrl = javaNamingProviderUrl;
	}

	public String getJavaNamingFactoryInitial() {
		return javaNamingFactoryInitial;
	}

	public void setJavaNamingFactoryInitial(String javaNamingFactoryInitial) {
		this.javaNamingFactoryInitial = javaNamingFactoryInitial;
	}

	public String getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(String connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public String getCreateQueueDirectly() {
		return createQueueDirectly;
	}

	public void setCreateQueueDirectly(String createQueueDirectly) {
		this.createQueueDirectly = createQueueDirectly;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getDirectoryInputOutput() {
		return directoryInputOutput;
	}

	public void setDirectoryInputOutput(String directoryInputOutput) {
		this.directoryInputOutput = directoryInputOutput;
	}

	public String getConfirmBackupAndError() {
		return confirmBackupAndError;
	}

	public void setConfirmBackupAndError(String confirmBackupAndError) {
		this.confirmBackupAndError = confirmBackupAndError;
	}

	public String getDirectoryBackup() {
		return directoryBackup;
	}

	public void setDirectoryBackup(String directoryBackup) {
		this.directoryBackup = directoryBackup;
	}

	public String getDirectoryError() {
		return directoryError;
	}

	public void setDirectoryError(String directoryError) {
		this.directoryError = directoryError;
	}
}
