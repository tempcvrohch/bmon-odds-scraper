package com.rohanch.bmonoddsscraper.services.helpers;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class Inject {
	public String ConstructJSExpressionWithFile(String jsExpression, String jsFilePath) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(jsFilePath));
			return new String(encoded, StandardCharsets.UTF_8) + "; " + jsExpression;
		} catch (IOException e) {
			throw new JSFileNotFoundException();
		}
	}

	public class JSFileNotFoundException extends RuntimeException {

	}
}

