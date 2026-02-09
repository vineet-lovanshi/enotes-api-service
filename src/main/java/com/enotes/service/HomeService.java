package com.enotes.service;

public interface HomeService {

	public Boolean verifyAccount(Integer userId, String varificationCode) throws Exception;
}
