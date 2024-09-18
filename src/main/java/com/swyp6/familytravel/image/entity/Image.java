package com.swyp6.familytravel.image.entity;

import com.swyp6.familytravel.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String savedName;
    private Integer order;

    @Builder
    public Image(String savedName, Integer order) {
        this.savedName = savedName;
        this.order = order;
    }
}
