package com.example.allinworks.module.approval.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity @Builder
@Table(name = "APPROVAL_DOC")
@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDoc {
    @Id
    @Column(name = "document_no")
    private int documentNo;

    @Column(name = "user_no")
    private String userNo;

    @Column(name = "file_no")
    private String fileNo;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private String status; // 10: 결재중, 20: 반려, 30: 완료

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "del_yn")
    private String delYn;


    public void updateDelYn() {
        this.delYn = "Y";
    }

    public void modifyDocument() {
        this.content = "test";
    }
}
