package com.swyp6.familytravel.family.controller;

import com.swyp6.familytravel.family.DTO.*;
import com.swyp6.familytravel.family.service.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @Operation(summary = "가족 생성 API", description = "가족을 생성합니다.")
    @PostMapping
    public FamilyDto createFamily(@RequestBody CreateFamilyDto dto)
    {
        return familyService.createFamily(dto);
    }

    @Operation(summary = "가족 정보 API", description = "가족을 id로 가져옵니다")
    @GetMapping("/{id}")
    public FamilyDto getFamilyById(@PathVariable Long id) {
        return familyService.getFamilyById(id);
    }

    @Operation(summary = "가족 정보 API", description = "모든 가족을 가져옵니다.")
    @GetMapping("/all")
    public List<FamilyDto> getAllFamilies() {
        return familyService.getAllFamilies();
    }

    @Operation(summary = "가족 update image API", description = "가족의 image를 업데이트 합니다.(아직미완)")
    @PutMapping(path = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FamilyDto updateProfileImage(
            @RequestPart(value = "imageFiles", required = false)
            MultipartFile imageFile) throws AccessDeniedException {
        return familyService.updateFamilyProfile(imageFile);
    }

    @Operation(summary = "가족 기념일 설정 API", description = "가족의 기념일을 설정합니다.")
    @PostMapping("/{id}/anniversary")
    public FamilyDto addAnniversary(@PathVariable Long id, @RequestBody FamilyAnniversaryDto dto) {
        return familyService.updateFamilyAnniversary(id, dto);
    }

    @Operation(summary = "가족 초대 API", description = "가족에 초대합니다")
    @PostMapping("/{id}/invite")
    public FamilyDto inviteFamilyMember(@PathVariable Long id, @RequestBody InviteUserDto dto) {
        return familyService.inviteUser(id, dto);
    }
}
