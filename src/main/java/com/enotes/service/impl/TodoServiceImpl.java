package com.enotes.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.TodoDto;
import com.enotes.dto.TodoDto.StatusDto;
import com.enotes.enums.TodoStatus;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.model.Todo;
import com.enotes.repository.TodoRepository;
import com.enotes.service.TodoService;
import com.enotes.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validation validation;

	@Override
	public boolean saveTodo(TodoDto todoDto) throws Exception {
		// vlaidate todo status
		validation.todoValidation(todoDto);

		Todo todo = modelMapper.map(todoDto, Todo.class);
		todo.setStatusId(todoDto.getStatus().getId());
		Todo save = todoRepository.save(todo);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}

		return false;
	}

	@Override
	public TodoDto getTodoById(Integer id) throws Exception {
		Todo byId = todoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Todo not found! invalid id"));
		TodoDto todoDto = modelMapper.map(byId, TodoDto.class);
		setStatus(byId, todoDto);
		return todoDto;
	}

	private void setStatus(Todo byId, TodoDto todoDto) {
		for (TodoStatus st : TodoStatus.values()) {
			if (st.getId().equals(byId.getStatusId())) {
				StatusDto statusDto = StatusDto.builder().id(st.getId()).name(st.getName()).build();
				todoDto.setStatus(statusDto);
			}
		}

	}

	@Override
	public List<TodoDto> getTodoByUser() {
		Integer userId = 2;
		List<Todo> todoList = todoRepository.findByCreatedBy(userId);
		return todoList.stream().map(td -> modelMapper.map(td, TodoDto.class)).toList();

	}

}
