package com.enotes.model;

import com.enotes.dto.UserDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

	private UserDto user;
	
	private String token;
}
