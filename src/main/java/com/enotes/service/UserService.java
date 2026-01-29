package com.enotes.service;

import com.enotes.dto.UserDto;

public interface UserService {

	public boolean registerUser(UserDto userDto) throws Exception;
}
