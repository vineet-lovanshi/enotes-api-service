package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.service.CategoryService;
import com.enotes.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@PostMapping("/save-cetegory")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto category) {
		Boolean saveCategory = categoryService.saveCategory(category);
		if (saveCategory) {
//			return new ResponseEntity<>("saved success", HttpStatus.CREATED);
			return CommonUtils.createBuildResponseMessage("save success", HttpStatus.CREATED);
		} else {
//			return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
			return CommonUtils.createErrorResponseMessage("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllCategory() {
//		String nmString=null;
//		nmString.toUpperCase();
		List<CategoryDto> allCategory = categoryService.getAllCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return new ResponseEntity<>("not found", HttpStatus.NO_CONTENT);
		} else {
//			return new ResponseEntity<>(allCategory, HttpStatus.OK);
			return CommonUtils.createBuildResponse(allCategory, HttpStatus.OK);
		}

	}

	@GetMapping("/active")
	public ResponseEntity<?> getActiveCategory() {
		List<CategoryResponse> allCategory = categoryService.getActiveCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return new ResponseEntity<>("not found", HttpStatus.NO_CONTENT);
		} else {
			return CommonUtils.createBuildResponse(allCategory, HttpStatus.OK);
//			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id) throws Exception {
		CategoryDto categoryDto = categoryService.getCategoryById(id);
		if (ObjectUtils.isEmpty(categoryDto)) {
			return CommonUtils.createErrorResponseMessage("Internal Server Error", HttpStatus.NOT_FOUND);
//			return new ResponseEntity<>("Internal server error ", HttpStatus.NOT_FOUND);
		}
//		return new ResponseEntity<>(categoryDto, HttpStatus.OK);
		return CommonUtils.createBuildResponse(categoryDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) {
		Boolean deleted = categoryService.deleteCategoryById(id);
		if (deleted) {
			return CommonUtils.createBuildResponse("Category deleted successfully", HttpStatus.NOT_FOUND);
//			return new ResponseEntity<>("Category deleted successfully " + id, HttpStatus.OK);
		} else {

			return CommonUtils.createErrorResponseMessage("Category Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
//			return new ResponseEntity<>("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
