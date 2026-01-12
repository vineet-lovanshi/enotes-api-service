package com.enotes.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.UserDto;
import com.enotes.model.Role;
import com.enotes.model.User;
import com.enotes.repository.RoleRepository;
import com.enotes.repository.UserRepository;
import com.enotes.service.UserService;
import com.enotes.util.Validation;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private Validation validation;

	@Autowired
	private ModelMapper mapper;

	@Override
	public boolean registerUser(UserDto userDto) {

		validation.userValidation(userDto);

		User mapUser = mapper.map(userDto, User.class);
		setRole(userDto, mapUser);

		User saveUser = userRepository.save(mapUser);

		if (!ObjectUtils.isEmpty(saveUser)) {
			return true;
		}
		return false;
	}
	
	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepository.findAllById(reqRoleId);
		user.setRoles(roles);
	}

}
