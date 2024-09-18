package com.swyp6.familytravel.image.repository;

import com.swyp6.familytravel.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findBySavedName(String savedName);
}
