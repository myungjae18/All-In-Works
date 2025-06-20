package com.example.allinworks.module.project.domain;

import com.example.allinworks.module.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROJECT_DETAIL")
@Entity
public class ProjectDetail {
    @Id
    @Column(name = "DETAIL_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailNo;

    @Column(name = "TITLE", nullable = false, length = 50)
    private String title;

    @Column(name = "PART")
    private String part;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "DETAIL_ORDER")
    private Integer detailOrder;

    //projectStatus에 선언된 값으로 고정
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PROJECT_NO")
    private Project project;
}
