package com.swyp6.familytravel.travel.entity;

import com.swyp6.familytravel.check.entity.Check;
import com.swyp6.familytravel.family.entity.Family;
import com.swyp6.familytravel.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "travels")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder.Default
    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Check> checklist = new ArrayList<>();;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @OneToMany(mappedBy = "travel")
    private List<Review> reviews;

    public void addCheck(Check check) {
        checklist.add(check);
        check.setTravel(this);
    }

    public void removeCheck(Check check)
    {
        checklist.remove(check);
    }
}
