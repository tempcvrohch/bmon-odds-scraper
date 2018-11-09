package com.rohanch.bmonoddsscraper.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Inject {
	public static String constructJSExpressionWithFile(String jsExpression, String jsFilePath) throws Exception {
		byte[] encoded = Files.readAllBytes(Paths.get(jsFilePath));
		return new String(encoded, StandardCharsets.UTF_8) + "; " + jsExpression;
	}
}
