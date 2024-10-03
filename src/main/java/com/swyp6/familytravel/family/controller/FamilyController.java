package com.swyp6.familytravel.family.controller;

import com.swyp6.familytravel.family.DTO.*;
import com.swyp6.familytravel.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @PostMapping
    public FamilyDto createFamily(@RequestBody CreateFamilyDto dto)
    {
        return familyService.createFamily(dto);
    }

    @PutMapping("/{id}/profile")
    public FamilyDto updateProfileImage(@PathVariable Long id,@RequestBody FamilyProfileDto dto) throws AccessDeniedException {
        return familyService.updateFamilyProfile(id, dto);
    }

    @PostMapping("/{id}/anniversary")
    public FamilyDto addAnniversary(@PathVariable Long id, @RequestBody FamilyAnniversaryDto dto) {
        return familyService.updateFamilyAnniversary(id, dto);
    }

    @PostMapping("/{id}/invite")
    public FamilyDto inviteFamilyMember(@PathVariable Long id, @RequestBody InviteUserDto dto) {
        return familyService.inviteUser(id, dto);
    }
}
