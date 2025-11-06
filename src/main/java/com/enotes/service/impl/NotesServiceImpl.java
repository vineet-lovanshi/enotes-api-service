package com.enotes.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.CategoryDto;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.model.FileDetails;
import com.enotes.model.Notes;
import com.enotes.repository.CategoryRepository;
import com.enotes.repository.FileRepository;
import com.enotes.repository.NotesRepository;
import com.enotes.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService {
	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private FileRepository fileRepository;

	@Value("${file.upload.path}")
	private String uploadPath;

	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		NotesDto notesDto = objectMapper.readValue(notes, NotesDto.class);

		FileDetails fileDetails = saveFileDetails(file);

//		 category validation
		checkCategoryExist(notesDto.getCategory());
		Notes map = mapper.map(notesDto, Notes.class);
		Notes saveNotes = notesRepository.save(map);

		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			FileDetails fileDetails = new FileDetails();

			String originalFilename = file.getOriginalFilename();
			fileDetails.setOriginalFileName(originalFilename);
			fileDetails.setDisplayFileName(getDisplayName(originalFilename));

			String rendomString = UUID.randomUUID().toString();
			String extension = FilenameUtils.getExtension(originalFilename);
			String uploadFileName = rendomString + "." + extension;

			fileDetails.setUploadFileName(uploadFileName);
			fileDetails.setFileSize(file.getSize());

			File saveFile = new File(uploadPath);
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}

			// path : enotes-api-service/notesFile/java.pdf

			String storePath = uploadPath.concat(uploadFileName);

			fileDetails.setPath(storePath);

			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));

			if (upload != 0) {
				FileDetails saveFileDetails = fileRepository.save(fileDetails);
				return saveFileDetails;
			}
		}
		return null;
	}

	private String getDisplayName(String originalFilename) {
		// JavaProgramming_tutorial.pdf

		// using apache common library dependency from mvn repository for get file
		// extension
		String extension = FilenameUtils.getExtension(originalFilename);
		// remove file extension
		String fileName = FilenameUtils.removeExtension(originalFilename);

		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName + "." + extension;
		return fileName;
	}

	private void checkCategoryExist(CategoryDto category) throws Exception {
		// TODO Auto-generated method stub

		categoryRepo.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("category id is invalid"));

	}

	@Override
	public List<NotesDto> getAllNotes() {
		return notesRepository.findAll().stream().map(note -> mapper.map(note, NotesDto.class)).toList();

	}

}
