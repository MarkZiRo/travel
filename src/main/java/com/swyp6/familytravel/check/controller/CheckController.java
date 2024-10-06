package com.swyp6.familytravel.check.controller;

import com.swyp6.familytravel.check.dto.CheckDto;
import com.swyp6.familytravel.check.dto.CreateCheckDto;
import com.swyp6.familytravel.check.dto.UpdateCheckDto;
import com.swyp6.familytravel.check.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{travelId}/checks")
@RequiredArgsConstructor
public class CheckController {

    private final CheckService checkService;

    @PostMapping
    public CheckDto createCheck(@PathVariable Long travelId, @RequestBody CreateCheckDto dto) {
        return checkService.addCheckToTravel(travelId, dto);
    }

    @GetMapping
    public List<CheckDto> getChecksForTravel(@PathVariable Long travelId) {
        return checkService.getChecksForTravel(travelId);
    }

    @PutMapping("/{checkId}")
    public CheckDto updateCheck(@PathVariable Long travelId, @PathVariable Long checkId, @RequestBody UpdateCheckDto dto) {
        return checkService.updateCheck(travelId, checkId, dto);
    }

    @DeleteMapping("/{checkId}")
    public void deleteCheck(@PathVariable Long travelId, @PathVariable Long checkId) {
        checkService.deleteCheck(travelId, checkId);
    }
}
