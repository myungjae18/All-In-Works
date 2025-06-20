package com.example.allinworks.module.board.domain;

import com.example.allinworks.module.user.domain.Department;
import com.example.allinworks.module.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POST")
@Getter@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_NO", nullable = false)
    private Integer postNo;

    @Column(name = "BOARD_NO", nullable = false)
    private Integer boardNo; // 외래 키로 매핑 가능 (Board와 연관 관계 설정)

//    @Column(name = "USER_NO", nullable = false)
//    private String userNo;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;

    @Column(name = "FILE_NO")
    private String fileNo;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Column(name = "CONTENT", nullable = false, length = 2000)
    private String content;

    @Column(name = "VIEWS", nullable = false)
    private int views;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "IS_NOTICE", nullable = false)
    private boolean notice;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();
}