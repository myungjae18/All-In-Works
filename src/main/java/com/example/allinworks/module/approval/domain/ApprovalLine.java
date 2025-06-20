package com.example.allinworks.module.approval.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.thymeleaf.spring6.processor.SpringUErrorsTagProcessor;

import java.time.LocalDateTime;

@Entity
@Table(name = "APPROVAL_LINE")
@Getter @Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalLine {
    @Id
    @Column(name = "line_no")
    private int lineNo;

    @Column(name = "approval_no")
    private int approvalNo;

    @Column(name = "user_no")
    private String userNo; //결재자 id

    @Column(name = "approval_order")
    private String approvalOrder;

    @Column(name = "approval_type")
    private String approvalType;

    @Column(name = "status")
    private String status; //대기-100, 반려-200, 승인-300

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;


    public void updateStatus(String status) {
        this.status = status;
        this.approvedAt = LocalDateTime.now();
    }

}
