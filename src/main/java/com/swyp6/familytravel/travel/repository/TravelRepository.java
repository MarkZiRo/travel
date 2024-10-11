package com.swyp6.familytravel.travel.repository;

import com.swyp6.familytravel.travel.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findFirstByFamilyIdAndStartDateAfterOrStartDateOrderByStartDateAsc(Long familyId, LocalDate after, LocalDate equal);
}
