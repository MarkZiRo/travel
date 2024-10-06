package com.swyp6.familytravel.check.entity;

import com.swyp6.familytravel.travel.entity.Travel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="checks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String checkName;
    private String content;
    private boolean success;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;
}
