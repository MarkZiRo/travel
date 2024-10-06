package com.swyp6.familytravel.travel.dto;

import com.swyp6.familytravel.check.dto.CheckDto;
import com.swyp6.familytravel.travel.entity.Travel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class TravelDto {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CheckDto> checklist;
    private Long familyId;

    public static TravelDto fromEntity(Travel travel) {
        return TravelDto.builder()
                .id(travel.getId())
                .name(travel.getName())
                .startDate(travel.getStartDate())
                .endDate(travel.getEndDate())
                .checklist(travel.getChecklist().stream()
                        .map(CheckDto::fromEntity)
                        .collect(Collectors.toList()))
                .familyId(travel.getFamily().getId())
                .build();
    }
}
