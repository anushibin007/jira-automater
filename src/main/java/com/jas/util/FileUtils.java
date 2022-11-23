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
		Resource resource = new ClassPathResource(fileName);
		try {
			return Files.readAllLines(Paths.get(resource.getURI()), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
