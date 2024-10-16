package com.swyp6.familytravel.anniversary.entiry;

import com.swyp6.familytravel.anniversary.dto.AnniversaryRequest;
import com.swyp6.familytravel.family.entity.Family;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Anniversary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    public void setFamily(Family family) {
        this.family = Objects.requireNonNull(family);
    }

    public void updateAnniversary(AnniversaryRequest request){
        this.content = Objects.requireNonNull(request.getContent());
        this.date = Objects.requireNonNull(request.getDate());
    }

    public Anniversary(AnniversaryRequest request){
        this.content = Objects.requireNonNull(request.getContent());
        this.date = Objects.requireNonNull(request.getDate());
    }
}
