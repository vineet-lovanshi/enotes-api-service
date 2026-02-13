package com.enotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.UserDto;
import com.enotes.model.LoginRequest;
import com.enotes.model.LoginResponse;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) throws Exception {
		boolean registerUser = userService.registerUser(userDto);
		if (registerUser) {
			return CommonUtils.createBuildResponseMessage("Register Success", HttpStatus.CREATED);
		}
		return CommonUtils.createErrorResponseMessage("Register Failed", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
		LoginResponse succes = userService.loginUser(loginRequest);
		if (ObjectUtils.isEmpty(succes)) {
			return CommonUtils.createErrorResponseMessage("Invalid credentials", HttpStatus.BAD_REQUEST);
		}
		return CommonUtils.createBuildResponse(succes, HttpStatus.OK);
	}
}
