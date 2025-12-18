package com.enotes.dto;

import com.enotes.model.Notes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouriteNoteDto {
	private Integer id;
	private NotesDto notes;
	private Integer userId;
}
