package com.swyp6.familytravel.image.entity;

import com.swyp6.familytravel.common.entity.BaseEntity;
import jakarta.persistence.*;
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
    @Column(name = "image_order")
    private Integer order;

    @Builder
    public Image(String savedName, Integer order) {
        this.savedName = savedName;
        this.order = order;
    }
}
