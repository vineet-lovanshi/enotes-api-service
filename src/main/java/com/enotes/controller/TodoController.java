package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.TodoDto;
import com.enotes.service.TodoService;
import com.enotes.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

	@Autowired
	private TodoService todoService;

	@PostMapping("/")
	public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws Exception {
		boolean saveTodo = todoService.saveTodo(todoDto);
		if (saveTodo) {
			return CommonUtils.createBuildResponseMessage("Todo Saved", HttpStatus.CREATED);
		} else {
			return CommonUtils.createBuildResponseMessage("Todo not Saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTodo(@PathVariable Integer id) throws Exception {
		TodoDto todoById = todoService.getTodoById(id);
		return CommonUtils.createBuildResponse(todoById, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<?> getAllTodoByUser() throws Exception {
		List<TodoDto> todoByUser = todoService.getTodoByUser();
		if (ObjectUtils.isEmpty(todoByUser)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtils.createBuildResponse(todoByUser, null);
	}
}
