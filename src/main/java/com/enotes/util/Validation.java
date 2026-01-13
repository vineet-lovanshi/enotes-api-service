package com.enotes.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.TodoDto;
import com.enotes.dto.TodoDto.StatusDto;
import com.enotes.dto.UserDto;
import com.enotes.enums.TodoStatus;
import com.enotes.exception.ExixtDataException;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.exception.ValidationException;
import com.enotes.repository.RoleRepository;
import com.enotes.repository.UserRepository;

@Component
public class Validation {

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private UserRepository userRepository;

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

	public void userValidation(UserDto userDto) {
		if (!StringUtils.hasText(userDto.getFirstName())) {
			throw new IllegalArgumentException("First name is invalid");
		}

		if (!StringUtils.hasText(userDto.getLastName())) {
			throw new IllegalArgumentException("Last name is invalid");
		}
		if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constant.EMAIL_REGEX)) {
			throw new IllegalArgumentException("Email is invalid");
		} else {
			Boolean existEmail = userRepository.existsByEmail(userDto.getEmail());
			if (existEmail) {
				throw new ExixtDataException("Email id allready exist");
			}
		}
		if (!StringUtils.hasText(userDto.getMobileNo()) || !userDto.getMobileNo().matches(Constant.MOBNO_REGEX)) {
			throw new IllegalArgumentException("Mobile No. is invalid");
		}
		if (CollectionUtils.isEmpty(userDto.getRoles())) {
			throw new IllegalArgumentException("role is invalid");
		} else {

			List<Integer> roleIds = roleRepo.findAll().stream().map(r -> r.getId()).toList();

			List<Integer> invalidReqRoleids = userDto.getRoles().stream().map(r -> r.getId())
					.filter(roleId -> !roleIds.contains(roleId)).toList();

			if (!CollectionUtils.isEmpty(invalidReqRoleids)) {
				throw new IllegalArgumentException("role is invalid " + invalidReqRoleids);
			}

		}
	}
}
