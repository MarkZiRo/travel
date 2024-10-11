package com.swyp6.familytravel.family.service;

import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.family.DTO.*;
import com.swyp6.familytravel.family.entity.Family;
import com.swyp6.familytravel.family.repository.FamilyRepository;
import com.swyp6.familytravel.image.service.ImageService;
import com.swyp6.familytravel.user.entity.UserEntity;
import com.swyp6.familytravel.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade facade;
    private final ImageService imageService;

    public FamilyDto createFamily(CreateFamilyDto dto)
    {
        UserEntity user = facade.extractUser();
        user = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(user.getFamily() != null)
            throw new IllegalArgumentException("가족이 이미 존재합니다.");

        Family family = new Family();
        family.setFamilyName(dto.getFamilyName());
        family.addUser(user);

        return FamilyDto.fromEntity(familyRepository.save(family));
    }

    public FamilyDto updateFamilyProfile(MultipartFile imageFile) throws AccessDeniedException {
        UserEntity userEntity = facade.extractUser();

        Family family = userEntity.getFamily();

        String imageFileName = imageService.storeImageFile(imageFile);

        family.setProfileImage(imageFileName);

        return FamilyDto.fromEntity(familyRepository.save(family));

    }

    public FamilyDto updateFamilyAnniversary(Long familyId, FamilyAnniversaryDto dto) {

        Family family = familyRepository.findById(familyId)
                .orElseThrow(()-> new EntityNotFoundException("not found"));

        family.getAnniversary().put(dto.getAnniversaryDate(), dto.getAnniversaryContent());

        return FamilyDto.fromEntity(familyRepository.save(family));
    }

    public FamilyDto inviteUser(Long id, InviteUserDto dto) {

        Family family = familyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Family not found"));

        UserEntity invitedUser = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (invitedUser.getFamily() != null) {
            throw new IllegalStateException("User is already a member of a family");
        }

        family.getUserList().add(invitedUser);
        invitedUser.setFamily(family);

        familyRepository.save(family);
        userRepository.save(invitedUser);

        return FamilyDto.fromEntity(family);
    }

    public FamilyDto getFamilyById(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new EntityNotFoundException("Family not found"));
        return FamilyDto.fromEntity(family);
    }

    public List<FamilyDto> getAllFamilies() {
        List<Family> families = familyRepository.findAll();
        return families.stream()
                .map(FamilyDto::fromEntity)
                .collect(Collectors.toList());
    }

    public FamilyDto joinFamily(Long id) {
        UserEntity user = facade.extractUser();
        Family family = familyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Family not found"));

        if(user.getFamily() != null)
            throw new IllegalArgumentException("가족이 이미 존재합니다.");

        family.addUser(user);

        return FamilyDto.fromEntity(family);
    }
}
