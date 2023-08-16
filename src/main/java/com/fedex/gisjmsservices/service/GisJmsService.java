package com.fedex.gisjmsservices.service;

import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fedex.gisjmsservices.config.ThreadConfiguration;
import com.fedex.gisjmsservices.consumer.Consumer;
import com.fedex.gisjmsservices.producer.Producer;
import com.fedex.gisjmsservices.common.FedExUtils;

@Service
public class GisJmsService {
	
	private static final Logger logger = FedExUtils.getLogger(GisJmsService.class);

	@Autowired @Qualifier("listThreads")
	private List<ThreadConfiguration> listThreads;
	
	public void run() {
		
		logger.info("Starting Application GIS JMS Service");
		if(listThreads.size() > 0) {
			String checkType= "";
			for (ThreadConfiguration threadConfiguration : listThreads) {
				
				checkType = threadConfiguration.getConnectionType().toLowerCase();
				
				try {
					Properties properties = new Properties();
					properties = FedExUtils.createProperties(threadConfiguration.getPrefixo(),
															 threadConfiguration.getConnectionType(),
															 threadConfiguration.getJavaNamingProviderUrl(),
															 threadConfiguration.getJavaNamingFactoryInitial(),
															 threadConfiguration.getConnectionFactory(),
															 threadConfiguration.getCreateQueueDirectly(),
															 threadConfiguration.getDestination(),
															 threadConfiguration.getQueueName(),
															 threadConfiguration.getUserID(),
															 threadConfiguration.getUserPassword(),
															 threadConfiguration.getDirectoryInputOutput(),
															 threadConfiguration.getConfirmBackupAndError(),
															 threadConfiguration.getDirectoryBackup(),
															 threadConfiguration.getDirectoryError());

					if(checkType.equals("consumer") || checkType.equals("consumidor")) {
						
						logger.info("Consumer {} started", threadConfiguration.getPrefixo());
						Consumer consumer = new Consumer(properties);
						new Thread(consumer, threadConfiguration.getPrefixo()).start();
						
					}else if(checkType.equals("producer") || checkType.equals("produtor")) {
						
						logger.info("Publisher {} started", threadConfiguration.getPrefixo());
						Producer producer = new Producer(properties);
						new Thread(producer, threadConfiguration.getPrefixo()).start();
						
					}
					
				} catch (Exception e) {
					if(checkType.equals("consumer") || checkType.equals("consumidor")) {
						logger.error("Error starting consumer {}: {}", threadConfiguration.getPrefixo(), e);
					}else if(checkType.equals("producer") || checkType.equals("produtor") || checkType.equals("publisher")) {
						logger.error("Error starting publisher {}: {}", threadConfiguration.getPrefixo(), e);
					}
				}
			}
		
		}else {
			logger.error("Could not find settings in properties file");
			throw new NullPointerException("Could not find settings in properties file");
			
		}
		
	}

}
