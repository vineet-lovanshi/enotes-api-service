package com.enotes.service;

import java.util.List;

import com.enotes.model.Category;

public interface CategoryService {
	public Boolean saveCategory(Category category);

	public List<Category> getAllCategory();
}
