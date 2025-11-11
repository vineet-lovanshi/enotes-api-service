package com.enotes.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.enotes.handler.GenericResponse;

public class CommonUtils {

	public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {
		GenericResponse genericResponse = GenericResponse.builder().responseStatus(status).status("succes")
				.message("succes").data(data).build();
		return genericResponse.create();
	}

	public static ResponseEntity<?> createBuildResponseMessage(String message, HttpStatus status) {
		GenericResponse genericResponse = GenericResponse.builder().responseStatus(status).status("succes")
				.message(message).build();
		return genericResponse.create();
	}

	public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {
		GenericResponse genericResponse = GenericResponse.builder().responseStatus(status).status("failed")
				.message("failed").data(data).build();
		return genericResponse.create();
	}

	public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {
		GenericResponse genericResponse = GenericResponse.builder().responseStatus(status).status("failed")
				.message(message).build();
		return genericResponse.create();
	}

	public static String getContentType(String originalFileName) {
		String extension = FilenameUtils.getExtension(originalFileName); // java_programing.pdf

		switch (extension) {
		case "pdf":
			return "application/pdf";
		case "xlsx":
			return "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
		case "txt":
			return "text/plan";
		case "png":
			return "image/png";
		case "jpg":
			return "image/jpg";
		default:
			return "application/octet-stream";
		}
	}

}
