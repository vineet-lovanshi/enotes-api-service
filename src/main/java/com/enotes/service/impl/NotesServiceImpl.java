package com.enotes.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
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
//		 category validation
		checkCategoryExist(notesDto.getCategory());
		Notes map = mapper.map(notesDto, Notes.class);

		FileDetails fileDetails = saveFileDetails(file);
		if (!ObjectUtils.isEmpty(fileDetails)) {
			map.setFileDetails(fileDetails);
		} else {
			map.setFileDetails(null);
		}
		Notes saveNotes = notesRepository.save(map);

		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

			String originalFilename = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFilename);
			List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg");

			if (!extensionAllow.contains(extension)) {
				throw new IllegalArgumentException("Invalid file format ! uplaod only .pdf , .xlsx , .jpg format");
			}

			String rendomString = UUID.randomUUID().toString();

			String uploadFileName = rendomString + "." + extension;

			File saveFile = new File(uploadPath);
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}

			// path : enotes-api-service/notesFile/java.pdf

			String storePath = uploadPath.concat(uploadFileName);

			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));

			if (upload != 0) {
				FileDetails fileDetails = new FileDetails();
				fileDetails.setOriginalFileName(originalFilename);
				fileDetails.setDisplayFileName(getDisplayName(originalFilename));
				fileDetails.setUploadFileName(uploadFileName);
				fileDetails.setFileSize(file.getSize());
				fileDetails.setPath(storePath);
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

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {

		InputStream io = new FileInputStream(fileDetails.getPath());
		return StreamUtils.copyToByteArray(io);

	}

	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDtls = fileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File is not available"));
		return fileDtls;
	}
}
