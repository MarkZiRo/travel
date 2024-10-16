package com.swyp6.familytravel.anniversary.controller;

import com.swyp6.familytravel.anniversary.service.AnniversaryService;
import com.swyp6.familytravel.anniversary.dto.AnniversaryRequest;
import com.swyp6.familytravel.family.DTO.FamilyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Anniversary")
@RestController
@RequestMapping("/api/v1/anniversary")
@AllArgsConstructor
public class AnniversaryController {

    private final AnniversaryService anniversaryService;

    @Operation(summary = "가족 기념일 설정 API", description = "가족의 기념일을 설정합니다.")
    @PostMapping
    public FamilyDto addAnniversary(@RequestBody AnniversaryRequest request) {
        return anniversaryService.addFamilyAnniversary(request);
    }

    @Operation(summary = "가족 기념일 설정 API", description = "가족의 기념일을 수정합니다.")
    @PatchMapping("/{id}")
    public FamilyDto addAnniversary(@PathVariable Long id, @RequestBody AnniversaryRequest request) {
        return anniversaryService.updateFamilyAnniversary(id, request);
    }

    @Operation(summary = "가족 기념일 제거 API", description = "가족의 기념일을 삭제합니다.")
    @DeleteMapping("/{id}")
    public FamilyDto deleteAnniversary(@PathVariable Long id){
        return anniversaryService.deleteFamilyAnniversary(id);
    }
}
