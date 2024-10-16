package com.swyp6.familytravel.anniversary.dto;

import com.swyp6.familytravel.anniversary.entiry.Anniversary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AnniversaryResponse {
    private Long id;
    private String content;
    private LocalDate date;

    public AnniversaryResponse(Anniversary anniversary){
        this.id = anniversary.getId();
        this.content = anniversary.getContent();
        this.date = anniversary.getDate();
    }
}
