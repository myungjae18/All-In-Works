package com.example.allinworks.module.user.domain;

import jakarta.persistence.*;
import lombok.*;
//
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "POSITION")
public class Position {
    @Id
    @Column(name = "POSITION_NO", nullable = false, length = 10)
    private String positionNo;

    @Column(name = "POSITION_NAME", nullable = false, length = 50)
    private String positionName;
}
