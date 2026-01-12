package com.enotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
