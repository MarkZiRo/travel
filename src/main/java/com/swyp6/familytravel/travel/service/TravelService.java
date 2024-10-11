package com.swyp6.familytravel.travel.service;

import com.swyp6.familytravel.auth.config.AuthenticationFacade;
import com.swyp6.familytravel.family.entity.Family;
import com.swyp6.familytravel.family.repository.FamilyRepository;
import com.swyp6.familytravel.travel.dto.CreateTravelDto;
import com.swyp6.familytravel.travel.dto.DDayResponse;
import com.swyp6.familytravel.travel.dto.TravelDto;
import com.swyp6.familytravel.travel.entity.Travel;
import com.swyp6.familytravel.travel.repository.TravelRepository;
import com.swyp6.familytravel.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        Family family = familyRepository.findById(user.getFamily().getId())
                .orElseThrow(() -> new EntityNotFoundException("Family not found"));

        Travel travel = Travel.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .checklist(new ArrayList<>())
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

        Family family = familyRepository.findById(user.getFamily().getId())
                .orElseThrow(() -> new EntityNotFoundException("Family not found"));

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

    public DDayResponse getDDay() {
        UserEntity user = authFacade.extractUser();
        Family family = user.getFamily();
        Objects.requireNonNull(family);
        Travel travel = travelRepository.findFirstByFamilyIdAndStartDateAfterOrStartDateOrderByStartDateAsc(user.getFamily().getId(), LocalDate.now(),  LocalDate.now()).orElse(null);
        Map<LocalDate, String> anniversary = family.getAnniversary();

        LocalDate closestAnniversary = anniversary.keySet().stream()
                .filter(date -> date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now()))
                .min(LocalDate::compareTo).orElse(null);

        if (travel != null && closestAnniversary != null) {
            if (closestAnniversary.isBefore(travel.getStartDate())) {
                return new DDayResponse(anniversary.get(closestAnniversary), closestAnniversary.toEpochDay() - LocalDate.now().toEpochDay());
            } else {
                return new DDayResponse(travel.getName(), travel.getStartDate().toEpochDay() - LocalDate.now().toEpochDay());
            }
        } else if (travel != null) {
            return new DDayResponse(travel.getName(), travel.getStartDate().toEpochDay() - LocalDate.now().toEpochDay());
        } else if (closestAnniversary != null) {
            return new DDayResponse(anniversary.get(closestAnniversary), closestAnniversary.toEpochDay() - LocalDate.now().toEpochDay());
        }
        throw new EntityNotFoundException("가장 가까운 기념일, 여행을 찾을 수 없습니다.");
    }
}
