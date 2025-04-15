package com.enotes.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.model.Category;
import com.enotes.repository.CategoryRepository;
import com.enotes.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {
		// TODO Auto-generated method stubS
//		Category category = new Category();
//		category.setName(categoryDto.getName());
//		category.setDescription(categoryDto.getDescription());
//		category.setIsActive(categoryDto.getIsActive());

		Category category = modelMapper.map(categoryDto, Category.class);
//		category.setIsActive(true);
		if(ObjectUtils.isEmpty(category.getId())) {
			category.setIsDeleted(false);
			category.setCreatedBy(1);
			category.setCreatedOn(new Date());
		}else {
			updateCategory(category);
		}
		
		Category saveCategory = categoryRepository.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		}
		return true;
	}

	private void updateCategory(Category category) {
		Optional<Category> byId = categoryRepository.findById(category.getId());
		if(byId.isPresent()) {
			Category existCategory = byId.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());
			category.setUpdatedBy(existCategory.getId());
			category.setUpdatedOn(new Date());
		}
		
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		// TODO Auto-generated method stub
		List<Category> categories = categoryRepository.findByIsDeletedFalse();
		List<CategoryDto> categoryDtosList = categories.stream().map(cat -> modelMapper.map(cat, CategoryDto.class))
				.toList();
		return categoryDtosList;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
		List<CategoryResponse> categoryResponsesList = categories.stream()
				.map(cat -> modelMapper.map(cat, CategoryResponse.class)).toList();
		return categoryResponsesList;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) {
		Optional<Category> byId = categoryRepository.findByIdAndIsDeletedFalse(id);
		if (byId.isPresent()) {
			Category category = byId.get();
			return modelMapper.map(category, CategoryDto.class);
		}
		return null;
	}

	@Override
	public Boolean deleteCategoryById(Integer id) {
		Optional<Category> byId = categoryRepository.findById(id);
		if (byId.isPresent()) {
			Category category = byId.get();
			category.setIsDeleted(true);
			categoryRepository.save(category);
			return true;
		}
		return false;
	}

}
