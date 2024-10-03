package com.swyp6.familytravel.check.service;

import com.swyp6.familytravel.check.dto.CheckDto;
import com.swyp6.familytravel.check.dto.CreateCheckDto;
import com.swyp6.familytravel.check.dto.UpdateCheckDto;
import com.swyp6.familytravel.check.entity.Check;
import com.swyp6.familytravel.check.repository.CheckRepository;
import com.swyp6.familytravel.travel.entity.Travel;
import com.swyp6.familytravel.travel.repository.TravelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckService {

    private final CheckRepository checkRepository;
    private final TravelRepository travelRepository;

    public CheckDto addCheckToTravel(Long travelId, CreateCheckDto dto) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new IllegalStateException("Check not found."));

        Check check = Check.builder()
                .checkName(dto.getCheckName())
                .content(dto.getContent())
                .success(false)
                .build();

        travel.addCheck(check);
        travelRepository.save(travel);

        return CheckDto.fromEntity(check);
    }


    public CheckDto updateCheck(Long travelId, Long checkId, UpdateCheckDto dto) {

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(()->new IllegalStateException("Check not found."));

        Check check = checkRepository.findById(checkId)
                .orElseThrow(() ->new IllegalStateException("Check not found."));

        check.setSuccess(dto.isSuccess());

        check = checkRepository.save(check);
        return CheckDto.fromEntity(check);
    }

    public void deleteCheck(Long travelId, Long checkId) {
        Check check = checkRepository.findById(checkId)
                .orElseThrow(() ->new IllegalStateException("Check not found."));
        checkRepository.delete(check);
    }

    public List<CheckDto> getChecksForTravel(Long travelId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new IllegalStateException("Check not found."));

        return travel.getChecklist().stream()
                .map(CheckDto::fromEntity)
                .collect(Collectors.toList());
    }

}
