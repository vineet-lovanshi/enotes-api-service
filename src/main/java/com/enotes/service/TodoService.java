package com.enotes.service;

import java.util.List;

import com.enotes.dto.TodoDto;

public interface TodoService {
	public boolean saveTodo(TodoDto todoDto) throws Exception;

	public TodoDto getTodoById(Integer id) throws Exception;

	public List<TodoDto> getTodoByUser();
}
