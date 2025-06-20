package com.example.allinworks.module.board.domain;

import com.example.allinworks.module.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENT")
@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_NO", nullable = false)
    private Integer commentNo;

    @ManyToOne
    @JoinColumn(name = "POST_NO", nullable = false)
    private Post post; // 외래 키로 매핑 가능 (Post와 연관 관계 설정)

//    @Column(name = "USER_NO", nullable = false)
//    private String userNo;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;

    @Column(name = "CONTENT", nullable = false, length = 500)
    private String content;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}