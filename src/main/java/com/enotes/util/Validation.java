package com.enotes.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.exception.ValidationException;

@Component
public class Validation {
	public void categoryValidation(CategoryDto categoryDto) {

		Map<String, Object> errorMap = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("Category Object Should'n be Null or Empty");
		} else {
			// validation name field
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				errorMap.put("Name", "Name feild is empty or null");
			} else {
				if (categoryDto.getName().length() < 3) {
					errorMap.put("Name", "Name length min 3");
				}
				if (categoryDto.getName().length() > 100) {
					errorMap.put("Name", "Name length max 100");
				}
			}

			// validation description field
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				errorMap.put("Description", "Description feild is empty or null");
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
}
