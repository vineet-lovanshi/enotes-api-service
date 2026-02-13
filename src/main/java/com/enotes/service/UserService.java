package com.enotes.service;

import com.enotes.dto.UserDto;
import com.enotes.model.LoginRequest;
import com.enotes.model.LoginResponse;

public interface UserService {

	public boolean registerUser(UserDto userDto) throws Exception;

	public LoginResponse loginUser(LoginRequest loginRequest);
}
