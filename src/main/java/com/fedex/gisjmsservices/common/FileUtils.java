package com.fedex.gisjmsservices.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.Logger;

public class FileUtils {

	private static final Logger logger = FedExUtils.getLogger(FileUtils.class);

	public static void createDirectory(String directory) {
		File directoryPath = new File(directory);
		if (!directoryPath.exists()) {
			directoryPath.mkdirs();
		}
	}

	public static void saveFile(String directory, String fileName, byte[] content) throws IOException {
		Files.write(Paths.get(directory + FedExUtils.REGEX_3 + fileName), content);
		logger.info("File has been saved");
	}

	public static String readFile(File arquivo) throws IOException {
		logger.info("File was read");
		return new String(Files.readAllBytes(Paths.get(arquivo.getPath())));
	}

	public static void copyFile(File arquivo, String directoryToMove) throws IOException {
		Files.copy(arquivo.toPath(),
				Files.createFile(Paths.get(directoryToMove + FedExUtils.REGEX_3 + arquivo.getName())),
				StandardCopyOption.REPLACE_EXISTING);
		logger.info("File was copied");
	}

	public static void moveFile(File arquivo, String directoryToMove) throws IOException {
		Files.move(arquivo.toPath(),
				Files.createFile(Paths.get(directoryToMove + FedExUtils.REGEX_3 + arquivo.getName())),
				StandardCopyOption.REPLACE_EXISTING);
		logger.info("File has been moved");
	}

	public static void deleteFile(File arquivo) throws IOException {
		Files.delete(arquivo.toPath());
		logger.info("File has been deleted");
	}

	public static void deleteFileIfExist(File arquivo) throws IOException {
		Files.deleteIfExists(arquivo.toPath());
		logger.info("File has been deleted");
	}
}
