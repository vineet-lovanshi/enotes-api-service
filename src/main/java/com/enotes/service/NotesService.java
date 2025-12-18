package com.enotes.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.FavouriteNoteDto;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.model.FileDetails;

public interface NotesService {
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

	public List<NotesDto> getAllNotes();

	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

	public FileDetails getFileDetails(Integer id) throws Exception;

	byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public Boolean softDeleteNotes(Integer id) throws Exception;

	public Boolean restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes(Integer userId);

	public void unFavouriteNote(Integer id) throws Exception;

	public void favouriteNote(Integer id) throws Exception;

	public List<FavouriteNoteDto> getFavouriteNote() throws Exception;

}
