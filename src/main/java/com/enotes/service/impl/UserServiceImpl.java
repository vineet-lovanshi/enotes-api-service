package com.enotes.service.impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.EmailRequest;
import com.enotes.dto.UserDto;
import com.enotes.model.AccountStatus;
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

	@Autowired
	private EmailService emailService;

	@Override
	public boolean registerUser(UserDto userDto) throws Exception {

		validation.userValidation(userDto);

		User mapUser = mapper.map(userDto, User.class);
		setRole(userDto, mapUser);

		AccountStatus accountStatus = AccountStatus.builder().isActive(false)
				.varificationCode(UUID.randomUUID().toString()).build();

		mapUser.setStatus(accountStatus);
		User saveUser = userRepository.save(mapUser);

		if (!ObjectUtils.isEmpty(saveUser)) {
			// send email
			emailSend(saveUser);
			return true;
		}
		return false;
	}

	private void emailSend(User saveUser) throws Exception {
		String message = "Hi,<b>" + saveUser.getFirstName() + "</b> " + "<br> Your account register sucessfully.<br>"
				+ "<br> Click the below link verify & Active your account <br>"
				+ "<a href='[[url]]'>Click Here</a> <br><br>" + "Thanks,<br>Enotes.com";

		message = message.replace("[[url]]",
				"http://localhost:8080/api/v1/home/verify?uid=" + saveUser.getId() + "&&code=" + saveUser.getStatus().getVarificationCode());

		EmailRequest emailRequest = EmailRequest.builder().to(saveUser.getEmail()).title("From Enotes Java App")
				.subject("Account Created Success").message(message).build();

		emailService.sendEmail(emailRequest);
	}

	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepository.findAllById(reqRoleId);
		user.setRoles(roles);
	}

}
