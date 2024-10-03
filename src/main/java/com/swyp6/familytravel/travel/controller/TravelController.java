package com.swyp6.familytravel.travel.controller;

import com.swyp6.familytravel.travel.dto.CreateTravelDto;
import com.swyp6.familytravel.travel.dto.TravelDto;
import com.swyp6.familytravel.travel.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    @PostMapping
    public TravelDto createTravel(@RequestBody CreateTravelDto dto)
    {
        return  travelService.createTravel(dto);
    }

    @GetMapping("/{id}")
    public TravelDto getTravel(@PathVariable Long id)
    {
        return travelService.getTravel(id);
    }

    @GetMapping("/all")
    public List<TravelDto> getAllTravels()
    {
        return travelService.getAllTravelsForFamily();
    }

    @DeleteMapping("/{id}")
    public void deleteTravel(@PathVariable Long id)
    {
        travelService.deleteTravel(id);
    }
}
