package com.jas.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FileUtils {

	public static List<String> fileToStringArray(String fileName) {
		return fileToStringArray(fileName, true);
	}

	public static List<String> fileToStringArray(String fileName, boolean skipCommentsAndEmptyLines) {
		Resource resource = new ClassPathResource(fileName);
		try {
			List<String> lines = Files.readAllLines(Paths.get(resource.getURI()), Charset.defaultCharset());
			if (skipCommentsAndEmptyLines) {
				lines.removeIf(trimmedLine -> (trimmedLine.isEmpty() || trimmedLine.startsWith("#")));
			}
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
