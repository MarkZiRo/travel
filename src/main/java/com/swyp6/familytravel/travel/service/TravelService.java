package com.swyp6.familytravel.travel.service;

import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.check.dto.CheckDto;
import com.swyp6.familytravel.check.dto.CreateCheckDto;
import com.swyp6.familytravel.check.entity.Check;
import com.swyp6.familytravel.family.entity.Family;
import com.swyp6.familytravel.family.repository.FamilyRepository;
import com.swyp6.familytravel.travel.dto.CreateTravelDto;
import com.swyp6.familytravel.travel.dto.TravelDto;
import com.swyp6.familytravel.travel.entity.Travel;
import com.swyp6.familytravel.travel.repository.TravelRepository;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelService {

    private final TravelRepository travelRepository;
    private final FamilyRepository familyRepository;
    private final AuthenticationFacade authFacade;

    public TravelDto createTravel(CreateTravelDto dto)
    {
        UserEntity user = authFacade.extractUser();
        Family family = user.getFamily();

        Travel travel = Travel.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        family.addTravel(travel);
        return TravelDto.fromEntity(travelRepository.save(travel));
    }


    public TravelDto getTravel(Long id) {

        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("여행을 찾을 수 없습니다."));
        return TravelDto.fromEntity(travel);
    }

    public List<TravelDto> getAllTravelsForFamily() {
        UserEntity user = authFacade.extractUser();
        Family family = user.getFamily();
        return family.getTravels().stream()
                .map(TravelDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteTravel(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("여행을 찾을 수 없습니다."));

        Family family = travel.getFamily();
        family.removeTravel(travel);
        travelRepository.delete(travel);
    }
}
