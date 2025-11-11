package com.enotes.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.NotesDto;
import com.enotes.model.FileDetails;

public interface NotesService {
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

	public List<NotesDto> getAllNotes();

	public FileDetails getFileDetails(Integer id) throws Exception;

	byte[] downloadFile(FileDetails fileDetails) throws Exception;
}
