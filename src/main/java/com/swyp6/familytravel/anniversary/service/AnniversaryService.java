package com.swyp6.familytravel.anniversary.service;

import com.swyp6.familytravel.anniversary.entiry.Anniversary;
import com.swyp6.familytravel.anniversary.repository.AnniversaryRepository;
import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.anniversary.dto.AnniversaryRequest;
import com.swyp6.familytravel.family.DTO.FamilyDto;
import com.swyp6.familytravel.family.entity.Family;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnniversaryService {

    private final AnniversaryRepository anniversaryRepository;
    private final AuthenticationFacade facade;


    public FamilyDto addFamilyAnniversary(AnniversaryRequest request){
        Family family = facade.extractUser().getFamily();

        Anniversary anniversary = new Anniversary(request);
        anniversaryRepository.save(anniversary);
        family.addAnniversary(anniversary);
        return FamilyDto.fromEntity(family);
    }

    public FamilyDto updateFamilyAnniversary(Long anniversaryId, AnniversaryRequest request) {
        Family family = facade.extractUser().getFamily();
        Anniversary anniversary = anniversaryRepository.findById(anniversaryId)
                .orElseThrow(()-> new EntityNotFoundException("기념일이 없습니다."));

        anniversary.updateAnniversary(request);
        return FamilyDto.fromEntity(family);
    }

    public FamilyDto deleteFamilyAnniversary(Long anniversaryId) {
        Family family = facade.extractUser().getFamily();
        Anniversary anniversary = anniversaryRepository.findById(anniversaryId)
                .orElseThrow(()-> new EntityNotFoundException("기념일이 없습니다."));

        family.deleteAnniversary(anniversary);
        return FamilyDto.fromEntity(family);
    }
}
