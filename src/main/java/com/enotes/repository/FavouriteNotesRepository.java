package com.enotes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.model.FavouriteNote;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNote, Integer> {

	List<FavouriteNote> findByUserId(int userId);

}
