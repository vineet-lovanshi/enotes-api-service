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
import com.enotes.exception.ExixtDataException;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.model.Category;
import com.enotes.repository.CategoryRepository;
import com.enotes.service.CategoryService;
import com.enotes.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validation validation;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

		// Validation checking
		validation.categoryValidation(categoryDto);
		
		//check existing
//		Boolean exisBoolean=categoryRepository.existsByName(categoryDto.getName().trim());
//		if(exisBoolean) {
//			//throw error
//			throw new ExixtDataException("Category allresdy exist");
//		}

		Category category = modelMapper.map(categoryDto, Category.class);
//		category.setIsActive(true);
		if (ObjectUtils.isEmpty(category.getId())) {
			Boolean exisBoolean=categoryRepository.existsByName(categoryDto.getName().trim());
			if(exisBoolean) {
				//throw error
				throw new ExixtDataException("Category allresdy exist");
			}
			category.setIsDeleted(false);
//			category.setCreatedBy(1);
			category.setCreatedOn(new Date());
		} else {
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
		if (byId.isPresent()) {
			Category existCategory = byId.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());
//			category.setUpdatedBy(existCategory.getId());
//			category.setUpdatedOn(new Date());
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
	public CategoryDto getCategoryById(Integer id) throws Exception {
		Category byId = categoryRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
		if (!ObjectUtils.isEmpty(byId)) {
			byId.getName().toUpperCase();
			return modelMapper.map(byId, CategoryDto.class);
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
