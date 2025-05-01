package com.enotes.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {

	private Map<String, Object> errMap;

	public ValidationException(Map<String, Object> errMap) {
		super("Validation Failed");
		this.errMap = errMap;
	}

	public Map<String, Object> getError() {
		return errMap;
	}
}
