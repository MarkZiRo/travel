package com.swyp6.familytravel.travel.controller;

import com.swyp6.familytravel.travel.dto.CreateTravelDto;
import com.swyp6.familytravel.travel.dto.DDayResponse;
import com.swyp6.familytravel.travel.dto.TravelDto;
import com.swyp6.familytravel.travel.service.TravelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    @Operation(summary = "여행 생성 API", description = "여행을 생성합니다.")
    @PostMapping
    public TravelDto createTravel(@RequestBody CreateTravelDto dto)
    {
        return  travelService.createTravel(dto);
    }

    @Operation(summary = "여행 정보 API", description = "여행정보를 가져옵니다.")
    @GetMapping("/{id}")
    public TravelDto getTravel(@PathVariable Long id)
    {
        return travelService.getTravel(id);
    }

    @Operation(summary = "여행 정보 API", description = "여행정보를 모두 가져옵니다.")
    @GetMapping("/all")
    public List<TravelDto> getAllTravels()
    {
        return travelService.getAllTravelsForFamily();
    }

    @Operation(summary = "여행 삭제 API", description = "여행정보를 삭제합니다.")
    @DeleteMapping("/{id}")
    public void deleteTravel(@PathVariable Long id)
    {
        travelService.deleteTravel(id);
    }

    @Operation(summary = "여행 수정 API", description = "여행정보를 수정합니다.")
    @PatchMapping("/{id}")
    public TravelDto updateTravel(@PathVariable Long id, @RequestBody CreateTravelDto dto)
    {
        return travelService.updateTravel(id, dto);
    }

    @Operation(summary = "D-Day API", description = "가장 가까운 기념일, 여행을 불러옵니다.")
    @GetMapping("/d-day")
    public DDayResponse getDDay()
    {
        return travelService.getDDay();
    }
}
