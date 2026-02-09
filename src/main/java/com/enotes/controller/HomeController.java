package com.enotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.service.HomeService;
import com.enotes.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

	@Autowired
	private HomeService homeService;

	@GetMapping("/verify")
	public ResponseEntity<?> varifyUserAcount(@RequestParam Integer uid, @RequestParam String code) throws Exception {
		Boolean verefied = homeService.verifyAccount(uid, code);
		if (verefied) {
			return CommonUtils.createBuildResponseMessage("Account verified sucess", HttpStatus.OK);
		}
		return CommonUtils.createErrorResponseMessage("Invalid varification link", HttpStatus.BAD_REQUEST);

	}
}
