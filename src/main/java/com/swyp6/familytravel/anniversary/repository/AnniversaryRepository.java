package com.swyp6.familytravel.anniversary.repository;

import com.swyp6.familytravel.anniversary.entiry.Anniversary;
import com.swyp6.familytravel.travel.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AnniversaryRepository extends JpaRepository< Anniversary,Long> {
    Optional<Anniversary> findFirstByFamilyIdAndDateGreaterThanEqualOrderByDateAsc(Long familyId, LocalDate date);
}
