package com.enotes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enotes.exception.ResourceNotFoundException;
import com.enotes.exception.SuccessException;
import com.enotes.model.AccountStatus;
import com.enotes.model.User;
import com.enotes.repository.UserRepository;
import com.enotes.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Boolean verifyAccount(Integer userId, String varificationCode) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("invalid user"));

		if (user.getStatus().getVarificationCode() == null) {
			throw new SuccessException("Account allready verified");
		}
		if (user.getStatus().getVarificationCode().equals(varificationCode)) {
			AccountStatus accountStatus = user.getStatus();
			accountStatus.setIsActive(true);
			accountStatus.setVarificationCode(null);

			userRepository.save(user);
			return true;
		}

		return false;
	}

}
