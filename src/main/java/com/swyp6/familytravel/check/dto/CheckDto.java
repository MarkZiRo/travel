package com.swyp6.familytravel.check.dto;

import com.swyp6.familytravel.check.entity.Check;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckDto {

    private Long id;
    private String checkName;
    private String content;
    private boolean success;

    public static CheckDto fromEntity(Check check) {
        return CheckDto.builder()
                .id(check.getId())
                .checkName(check.getCheckName())
                .content(check.getContent())
                .success(check.isSuccess())
                .build();
    }
}
