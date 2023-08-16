package com.fedex.gisjmsservices.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;

import com.fedex.gisjmsservices.common.FedExUtils;

@Configuration
@ComponentScan({ "com.fedex" })
@PropertySources({ @PropertySource("file:application.properties") })
@EnableAsync
@EnableCaching
public class AppConfig {

	@Autowired
	private Environment env;

	@Value("${prefixos.threads:#{null}}")
	private String prefixosThreads;

	@Bean("listThreads")
	public List<ThreadConfiguration> listThreads() {
		List<ThreadConfiguration> listThreads = new ArrayList<ThreadConfiguration>();
		if (StringUtils.hasText(prefixosThreads)) {
			String[] prefixos = prefixosThreads.split(FedExUtils.REGEX_1);
			if (prefixos != null) {
				for (String prefixo : prefixos) {
					if (StringUtils.hasText(prefixo)) {
						ThreadConfiguration threadConfiguration = new ThreadConfiguration();
						threadConfiguration.setPrefixo(prefixo);
						threadConfiguration.setConnectionType(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.CONNECTION_TYPE));
						threadConfiguration.setJavaNamingProviderUrl(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.JAVA_NAME_PROVIDER_URL));
						threadConfiguration.setJavaNamingFactoryInitial(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.JAVA_NAMING_FACTORY_INITIAL));
						threadConfiguration.setConnectionFactory(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.CONNECTION_FACTORY));
						threadConfiguration
								.setDestination(env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.DESTINATION));
						threadConfiguration.setCreateQueueDirectly(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.CREATE_QUEUE_DIRECTLY));
						threadConfiguration
								.setQueueName(env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.QUEUE_NAME));
						threadConfiguration
								.setUserID(env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.USER_ID));
						threadConfiguration.setUserPassword(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.USER_PASSWORD));
						threadConfiguration.setDirectoryInputOutput(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.DIRECTORY_INPUT_OUTPUT));
						threadConfiguration.setConfirmBackupAndError(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.CONFIRM_BACKUP_ERROR));
						threadConfiguration.setDirectoryBackup(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.DIRECTORY_BACKUP));
						threadConfiguration.setDirectoryError(
								env.getProperty(prefixo + FedExUtils.REGEX_2 + FedExUtils.DIRECTORY_ERROR));
						listThreads.add(threadConfiguration);

					}
				}
			}
		}
		return listThreads;
	}
}
