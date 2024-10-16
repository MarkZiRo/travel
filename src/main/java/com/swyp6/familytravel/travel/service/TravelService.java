package com.swyp6.familytravel.travel.service;

import com.swyp6.familytravel.anniversary.entiry.Anniversary;
import com.swyp6.familytravel.anniversary.repository.AnniversaryRepository;
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
    private final AnniversaryRepository anniversaryRepository;
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

    public TravelDto updateTravel(Long id, CreateTravelDto dto) {
        UserEntity user = authFacade.extractUser();

        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("여행을 찾을 수 없습니다."));

        if (!travel.getFamily().getId().equals(user.getFamily().getId())) {
            throw new IllegalStateException("해당 여행에 대한 권한이 없습니다.");
        }

        travel.setName(dto.getName());
        travel.setStartDate(dto.getStartDate());
        travel.setEndDate(dto.getEndDate());

        return TravelDto.fromEntity(travel);
    }


    public void deleteTravel(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("여행을 찾을 수 없습니다."));
        // TODO : 권한 검사 추가

        Family family = travel.getFamily();
        family.removeTravel(travel);
        travelRepository.delete(travel);
    }

    public DDayResponse getDDay() {
        UserEntity user = authFacade.extractUser();
        Family family = user.getFamily();
        Objects.requireNonNull(family);
        Travel travel = travelRepository.findFirstByFamilyIdAndStartDateGreaterThanEqualOrderByStartDateAsc(user.getFamily().getId(), LocalDate.now()).orElse(null);
        Anniversary anniversary = anniversaryRepository.findFirstByFamilyIdAndDateGreaterThanEqualOrderByDateAsc(user.getFamily().getId(), LocalDate.now()).orElse(null);

        if (travel != null && anniversary != null) {
            if (anniversary.getDate().isBefore(travel.getStartDate())) {
                return new DDayResponse(anniversary.getContent(), anniversary.getDate().toEpochDay() - LocalDate.now().toEpochDay());
            } else {
                return new DDayResponse(travel.getName(), travel.getStartDate().toEpochDay() - LocalDate.now().toEpochDay());
            }
        } else if (travel != null) {
            return new DDayResponse(travel.getName(), travel.getStartDate().toEpochDay() - LocalDate.now().toEpochDay());
        } else if (anniversary != null) {
            return new DDayResponse(anniversary.getContent(), anniversary.getDate().toEpochDay() - LocalDate.now().toEpochDay());
        }
        throw new EntityNotFoundException("가장 가까운 기념일, 여행을 찾을 수 없습니다.");
    }
}
