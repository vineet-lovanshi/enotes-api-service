package com.enotes.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.TodoDto;
import com.enotes.dto.TodoDto.StatusDto;
import com.enotes.enums.TodoStatus;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.exception.ValidationException;

@Component
public class Validation {

	public void categoryValidation(CategoryDto categoryDto) {

		Map<String, Object> errorMap = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("Category Object Should'n be Null or Empty");
		} else {
			// validate name field
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				errorMap.put("name", "Name field is empty or null");
			} else {
				String name = categoryDto.getName().trim();

				if (name.isEmpty()) {
					errorMap.put("name", "Name cannot contain only whitespace");
				} else {
					if (name.length() < 3) {
						errorMap.put("name", "Name length must be at least 3 characters");
					}
					if (name.length() > 100) {
						errorMap.put("name", "Name length must not exceed 100 characters");
					}

				}
			}

			// validation description field
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				errorMap.put("description", "Description feild is empty or null");
			} else {
				String description = categoryDto.getDescription().trim();

				if (description.isEmpty()) {
					errorMap.put("description", "Description cannot contain only whitespace");
				}
			}

			// validation isActive field
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				errorMap.put("isActive", "isActive feild is empty or null");
			} else {
				if (categoryDto.getIsActive() != Boolean.TRUE && categoryDto.getIsActive() != Boolean.FALSE) {
					errorMap.put("isActive", "Invalid value in isActive field");
				}

			}

		}
		if (!errorMap.isEmpty()) {
			throw new ValidationException(errorMap);
		}

	}

	public void todoValidation(TodoDto todoDto) throws Exception {
		StatusDto status = todoDto.getStatus();
		TodoStatus[] values = TodoStatus.values();

		boolean statusFound = false;
		for (TodoStatus st : values) {
			if (st.getId().equals(status.getId())) {
				statusFound = true;
			}
		}
		if (!statusFound) {
			throw new ResourceNotFoundException("invalid status");
		}
	}
}
