package com.enotes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByIsActive(boolean isActive);

	Optional<Category> findByIdAndIsDeletedFalse(Integer id);

	List<Category> findByIsDeletedFalse();

	List<Category> findByIsActiveTrueAndIsDeletedFalse();

	Boolean existsByName(String trim);

}
