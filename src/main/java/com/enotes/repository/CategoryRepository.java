package com.enotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByIsActive(boolean isActive);

}
