package com.enotes.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.model.Category;
import com.enotes.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@PostMapping("/save-cetegory")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto category) {
		Boolean saveCategory = categoryService.saveCategory(category);
		if (saveCategory) {
			return new ResponseEntity<>("saved success", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get-cetegory")
	public ResponseEntity<?> getAllCategory() {
		List<CategoryDto> allCategory = categoryService.getAllCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return new ResponseEntity<>("not found", HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

	@GetMapping("/active-cetegory")
	public ResponseEntity<?> getActiveCategory() {
		List<CategoryResponse> allCategory = categoryService.getActiveCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return new ResponseEntity<>("not found", HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}
}
