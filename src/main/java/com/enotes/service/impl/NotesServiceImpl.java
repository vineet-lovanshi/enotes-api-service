package com.enotes.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.CategoryDto;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.model.Notes;
import com.enotes.repository.CategoryRepository;
import com.enotes.repository.NotesRepository;
import com.enotes.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {
	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CategoryRepository categoryRepo;
	@Override
	public Boolean saveNotes(NotesDto notesDto) throws Exception {
		// category validation
				checkCategoryExist(notesDto.getCategory());
		Notes map = mapper.map(notesDto, Notes.class);
		Notes saveNotes = notesRepository.save(map);

		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}
	

	private void checkCategoryExist(CategoryDto category) throws Exception {
		// TODO Auto-generated method stub

		categoryRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category id is invalid"));
	
		
	}


	@Override
	public List<NotesDto> getAllNotes() {
		return notesRepository.findAll().stream().map(note -> mapper.map(note, NotesDto.class)).toList();

	}

}
