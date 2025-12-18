package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.FavouriteNoteDto;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.model.FileDetails;
import com.enotes.service.NotesService;
import com.enotes.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

	@Autowired
	private NotesService notesService;

	@PostMapping("/")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {
		Boolean saveNotes = notesService.saveNotes(notes, file);
		if (saveNotes) {
			return CommonUtils.createBuildResponseMessage("Notes saved success", HttpStatus.CREATED);
		}
		return CommonUtils.createErrorResponseMessage("Notes note saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
		FileDetails fileDetails = notesService.getFileDetails(id);
		byte[] data = notesService.downloadFile(fileDetails);
		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtils.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> allNotes = notesService.getAllNotes();
		if (CollectionUtils.isEmpty(allNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtils.createBuildResponse(allNotes, HttpStatus.OK);
	}

	@GetMapping("/user-notes")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		Integer userId = 2;
		NotesResponse allNotes = notesService.getAllNotesByUser(userId, pageNo, pageSize);
//		if (CollectionUtils.isEmpty(allNotes)) {
//			return ResponseEntity.noContent().build();
//		}
		return CommonUtils.createBuildResponse(allNotes, HttpStatus.OK);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {

		Boolean deleted = notesService.softDeleteNotes(id);
		return CommonUtils.createBuildResponseMessage("Deleted Succesfully", HttpStatus.OK);

	}

	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {

		Boolean restore = notesService.restoreNotes(id);
		return CommonUtils.createBuildResponseMessage("Restore Succesfully", HttpStatus.OK);

	}

	@GetMapping("/recycle-bin")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
		Integer userId = 2;
		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
		if (ObjectUtils.isEmpty(notes)) {
			return CommonUtils.createBuildResponseMessage("Notes not available in recycle bin", HttpStatus.OK);
		}
		return CommonUtils.createBuildResponse(notes, HttpStatus.OK);

	}

	@PostMapping("/fav/{noteId}")
	public ResponseEntity<?> favNotes(@PathVariable Integer noteId) throws Exception {
		notesService.favouriteNote(noteId);
		return CommonUtils.createBuildResponseMessage("Added in favourite", HttpStatus.CREATED);

	}

	@GetMapping("/un-fav/{favId}")
	public ResponseEntity<?> unFavNotes(@PathVariable Integer favId) throws Exception {
		notesService.unFavouriteNote(favId);
		return CommonUtils.createBuildResponseMessage("Removed from favourite", HttpStatus.OK);

	}

	@GetMapping("/fav-notes")
	public ResponseEntity<?> getFavNotes() throws Exception {
		List<FavouriteNoteDto> favouriteNote = notesService.getFavouriteNote();
		if (ObjectUtils.isEmpty(favouriteNote)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtils.createBuildResponse(favouriteNote, HttpStatus.OK);

	}

}
